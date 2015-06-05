package com.poicom.basic.config;

import cn.dreampie.mail.MailerPlugin;
import cn.dreampie.shiro.core.ShiroInterceptor;
import cn.dreampie.shiro.core.ShiroPlugin;
import cn.dreampie.shiro.freemarker.ShiroTags;
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
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.activerecord.tx.TxByActionMethods;
import com.jfinal.plugin.activerecord.tx.TxByRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.FreeMarkerRender;
import com.poicom.basic.common.DictKeys;
import com.poicom.basic.freemarker.FreeMarkerRenderFactory;
import com.poicom.basic.freemarker.resource.ResourceTags;
import com.poicom.basic.handler.GlobalHandler;
import com.poicom.basic.interceptor.AttrsInterceptor;
import com.poicom.basic.interceptor.SessionInterceptor;
import com.poicom.basic.kit.StringKit;
import com.poicom.basic.plugin.properties.PropertiesPlugin;
import com.poicom.basic.plugin.quartz.QuartzPlugin;
import com.poicom.basic.plugin.routebind.RouteBind;
import com.poicom.basic.plugin.sqlxml.SqlInXmlPlugin;
import com.poicom.basic.shiro.MyJdbcAuthzService;
import com.poicom.basic.thread.ThreadAlert;
import com.poicom.basic.thread.ThreadSysLog;
import com.poicom.function.*;
/**
 * 
 * @author 唐东宇
 *
 */
public class AppConfig extends JFinalConfig {
	
	private final static Logger log =Logger.getLogger(AppConfig.class);
	
	Routes routes;

	@Override
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		log.info(" == Constants == 缓存 Properties配置文件中的配置信息！");
		new PropertiesPlugin(loadPropertyFile("application.properties")).start();
		
		log.info(" == Constants == 设置字符集！");
		me.setEncoding(StringKit.encoding);
		log.info(" == Constants == 设置开发模式");
		me.setDevMode(getPropertyToBoolean(DictKeys.config_devMode, false));
		log.info("  == Constants == 设置FreeMarker视图");
		me.setMainRenderFactory(new FreeMarkerRenderFactory());
		
		
		//Logger.setLoggerFactory(new Slf4jLogFactory());
		log.info("  == Constants == 设置异常跳转视图");
		me.setError401View("/app/index/_signin.ftl");
		me.setError403View("/app/common/error403.html");
		me.setError404View("/app/common/error404.html");
		me.setError500View("/app/common/error500.html");
		
	}

	@Override
	public void configHandler(Handlers me) {
		log.info("configHandler 全局配置处理器，主要是记录日志和request域值处理");
		//me.add(new GlobalHandler());
		me.add(new ContextPathHandler("ContextPath"));
	}

	@Override
	public void configInterceptor(Interceptors me) {
		log.info("  == Interceptors ==   添加shiro的过滤器");
		me.add(new ShiroInterceptor());
		log.info("  == Interceptors ==   支持使用session");
		me.add(new SessionInViewInterceptor());
		log.info("  == Interceptors ==   添加shiro的URL拦截");
		me.add(new UrlInterceptor());
		log.info("  == Interceptors ==   添加session拦截");
		me.add(new SessionInterceptor());
		// 配置开启事物规则
		me.add(new TxByActionMethods("save", "update", "delete"));
		me.add(new TxByRegex(".*save.*"));
		me.add(new TxByRegex(".*update.*"));
		me.add(new TxByRegex(".*delete.*"));
	}

	@Override
	public void configPlugin(Plugins me) {
		log.info("  == Plugins--DruidPlugin ==   配置Druid数据库连接池连接属性");
		DruidPlugin druidPlugin = new DruidPlugin(
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_jdbcUrl), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_userName), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_passWord), 
				(String)PropertiesPlugin.getParamMapValue(DictKeys.db_connection_driverClass));
		
		log.info("  == Plugins--DruidPlugin ==    配置Druid数据库连接池大小");
		druidPlugin.set(
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_initialSize), 
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_minIdle), 
				(Integer)PropertiesPlugin.getParamMapValue(DictKeys.db_maxActive));
		log.info("  == Plugins--DruidPlugin ==    StatFilter提供JDBC层的统计信息");
		druidPlugin.addFilter(new StatFilter());
		log.info("  == Plugins--DruidPlugin ==    WallFilter防御SQL注入攻击");
		
		WallFilter wallFilter=new WallFilter();
		wallFilter.setDbType("h2");
		druidPlugin.addFilter(wallFilter);
		
		//配置通过Model的Table注解，映射到数据库
		//通过TableBindPlugin中的start方法，实现了扫描注解为TableBind的model
		//调用了arp.addMapping(tableName, modelClass)方法，实现了ORM功能
		TableBindPlugin tableBindPlugin=new TableBindPlugin(DictKeys.db_dataSource_main,druidPlugin,SimpleNameStyles.LOWER);
		//忽略字段大小写(Container集装箱、sensitive敏感的)
		tableBindPlugin.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		// 设置开发模式
		tableBindPlugin.setDevMode(getPropertyToBoolean(DictKeys.config_devMode,false));
		// 是否显示SQL
		tableBindPlugin.setShowSql(getPropertyToBoolean(DictKeys.config_devMode, false));
		
		log.info("  == Plugins ==   数据库类型判断");
		String db_type = (String) PropertiesPlugin.getParamMapValue(DictKeys.db_type_key);
		if(db_type.equals(DictKeys.db_type_postgresql)){
			log.info("  == Plugins ==    使用数据库类型是 postgresql");
			tableBindPlugin.setDialect(new PostgreSqlDialect());
		}else if(db_type.equals(DictKeys.db_type_mysql)){
			log.info("  == Plugins ==    使用数据库类型是 mysql");
			tableBindPlugin.setDialect(new MysqlDialect());
		}else if(db_type.equals(DictKeys.db_type_oracle)){
			log.info("  == Plugins ==    使用数据库类型是 oracle");
			druidPlugin.setValidationQuery("select 1 FROM DUAL"); //指定连接验证语句(用于保存数据库连接池), 这里不加会报错误:invalid oracle validationQuery. select 1, may should be : select 1 FROM DUAL 
			tableBindPlugin.setDialect(new OracleDialect());
		}
		log.info("  == Plugins ==    添加druidPlugin插件");
		me.add(druidPlugin);
		log.info("  == Plugins ==    添加TableBindPlugin插件");
		me.add(tableBindPlugin);
		
		log.info("  == Plugins--SqlXmlPlugin ==   解析并缓存 xml sql");
		me.add(new SqlInXmlPlugin());
		log.info("  == Plugins--EhCachePlugin ==  EhCache缓存");
		me.add(new EhCachePlugin());
		log.info("  == Plugins--ShiroPlugin ==  shiro权限框架");
	    me.add(new ShiroPlugin(routes, new MyJdbcAuthzService())); 
	    log.info("  == Plugins--MailerPlugin ==  邮件插件");
	    me.add(new MailerPlugin());
	    log.info("  == Plugins--QuartzPlugin ==  定时器");
	    me.add(new QuartzPlugin());
	}

	@Override
	public void configRoute(Routes me) { 
		log.info("  == Routes ==  路由扫描注册");
		this.routes=me;
		RouteBind routeBind=new RouteBind();
		me.add(routeBind);
	} 
	 
	public void afterJFinalStart(){
		super.afterJFinalStart();
		log.info("afterJFinalStart 读取freemarker配置文件");
		FreeMarkerRender.setProperties(loadPropertyFile("freemarker.properties"));
		log.info("afterJFinalStart 配置shiro标签");
		FreeMarkerRender.getConfiguration().setSharedVariable("shiro",new ShiroTags());
		log.info("afterJFinalStart 配置resource标签");
		FreeMarkerRender.getConfiguration().setSharedVariable("resource", new ResourceTags());
		 
		log.info("afterJFinalStart 启动操作日志入库线程");
		//ThreadSysLog.startSaveDBThread();
		log.info("afterJFinalStart 启动操作发送邮件、短信线程");
		//ThreadAlert.startSendEmailAndSmsThread();
	} 
	
	public void beforeJFinalStop(){
		log.info("beforeJFinalStop 释放日志入库线程");
		//ThreadSysLog.setThreadRun(false);
		log.info("beforeJFinalStop 释放发送邮件、短信线程");
		//ThreadAlert.setThreadRun(false);
	}
	
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
	}

}
