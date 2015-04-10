package com.poicom.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dreampie.mail.MailerPlugin;
import cn.dreampie.quartz.QuartzKey;
import cn.dreampie.quartz.QuartzPlugin;
import cn.dreampie.quartz.job.QuartzCronJob;
import cn.dreampie.routebind.RouteBind;
import cn.dreampie.shiro.core.ShiroInterceptor;
import cn.dreampie.shiro.core.ShiroPlugin;
import cn.dreampie.shiro.freemarker.ShiroTags;
import cn.dreampie.sqlinxml.SqlInXmlPlugin;
import cn.dreampie.tablebind.SimpleNameStyles;
import cn.dreampie.tablebind.TableBindPlugin;
import cn.dreampie.web.interceptor.UrlInterceptor;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;
import com.poicom.common.freemarker.FreeMarkerRenderFactory;
import com.poicom.common.handler.GlobalHandler;
import com.poicom.common.job.AlertJob;
import com.poicom.common.resource.ResourceTags;
import com.poicom.common.shiro.MyJdbcAuthzService;
import com.poicom.common.thread.ThreadSysLog;
import com.poicom.function.app.CommonInterceptor;

public class AppConfig extends JFinalConfig {
	
	private static Logger logger =LoggerFactory.getLogger(AppConfig.class);
	
	/** 
	 * 供Shiro插件使用。
	 */
	Routes routes;

	@Override
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("application.properties");
		//设置是否开发模式
		me.setDevMode(getPropertyToBoolean("devMode", false));
		//设置视图FreeMarker设置
		me.setMainRenderFactory(new FreeMarkerRenderFactory());
		//Logger.setLoggerFactory(new Slf4jLogFactory());
		//设置异常跳转视图
		me.setError401View("/page/index/_signin.ftl");
		me.setError403View("/page/app/error403.html");
		me.setError404View("/page/app/error404.html");
		
	}

	@Override
	public void configHandler(Handlers me) {
		logger.info("configHandler 全局配置处理器，主要是记录日志和request域值处理");
		me.add(new GlobalHandler());

	}

	@Override
	public void configInterceptor(Interceptors me) {
		//方法执行时间
		//me.add(new ExecuteTimeInteceptor());
		//添加shiro的过滤器到interceptor
		me.add(new ShiroInterceptor());
		me.add(new SessionInViewInterceptor());
		me.add(new UrlInterceptor());
		me.add(new CommonInterceptor());
		
	}

	@Override
	public void configPlugin(Plugins me) {
	
		//配置druid连接池
		DruidPlugin druidPlugin = new DruidPlugin(getProperty("db.default.url"), getProperty("db.default.user"), getProperty("db.default.password"), getProperty("db.default.driver"));
		// StatFilter提供JDBC层的统计信息
		druidPlugin.addFilter(new StatFilter());
		// WallFilter的功能是防御SQL注入攻击
		WallFilter wallFilter=new WallFilter();
		wallFilter.setDbType("h2");
		druidPlugin.addFilter(wallFilter);
		
		druidPlugin.setInitialSize(getPropertyToInt("db.default.poolInitialSize"));
		druidPlugin.setMaxPoolPreparedStatementPerConnectionSize(getPropertyToInt("db.default.poolMaxSize"));
		druidPlugin.setTimeBetweenConnectErrorMillis(getPropertyToInt("db.default.connectionTimeoutMillis"));
	    
		me.add(druidPlugin);

		//配置通过Model的Table注解，映射到数据库
		//通过TableBindPlugin中的start方法，实现了扫描注解为TableBind的model
		//调用了arp.addMapping(tableName, modelClass)方法，实现了ORM功能
		TableBindPlugin tableBindPlugin=new TableBindPlugin(druidPlugin,SimpleNameStyles.LOWER);
		//忽略字段大小写(Container集装箱、sensitive敏感的)
		tableBindPlugin.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		tableBindPlugin.setShowSql(getPropertyToBoolean("devMode", false));
		//非mysql的数据库方言
		tableBindPlugin.setDialect(new AnsiSqlDialect());
		me.add(tableBindPlugin);
		
		
		//sql语句plugin
		me.add(new SqlInXmlPlugin());
		//ehcache缓存
		me.add(new EhCachePlugin());
		//shiro权限框架
	    me.add(new ShiroPlugin(routes, new MyJdbcAuthzService())); 
	    
	    //邮件插件
	    me.add(new MailerPlugin());
	    
	    //定时器
	    me.add(new QuartzPlugin());
	}

	@Override
	public void configRoute(Routes me) {

		this.routes=me;
		RouteBind routeBind=new RouteBind();
		
		me.add(routeBind);
	}
	
	public void afterJFinalStart(){
		super.afterJFinalStart();
		FreeMarkerRender.setProperties(loadPropertyFile("freemarker.properties"));
		FreeMarkerRender.getConfiguration().setSharedVariable("shiro",new ShiroTags());
		FreeMarkerRender.getConfiguration().setSharedVariable("resource", new ResourceTags());
		new QuartzCronJob(new QuartzKey(1, "test", "test"), "0 30 08 * * ?", AlertJob.class).start();
		
		logger.info("afterJFinalStart 启动操作日志入库线程");
		ThreadSysLog.startSaveDBThread();
	}
	
	public void beforeJFinalStop(){
		
		logger.info("beforeJFinalStop 释放日志入库线程");
		ThreadSysLog.setThreadRun(false);
		
	}
	
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
	}

}
