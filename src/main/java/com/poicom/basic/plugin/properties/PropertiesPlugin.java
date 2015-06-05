package com.poicom.basic.plugin.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.poicom.basic.common.DictKeys;

/**
 * 读取Properties配置文件数据并存入缓存
 * @author FireTercel 2015年6月2日 
 *
 */
public class PropertiesPlugin implements IPlugin{
	
	private static Logger log = Logger.getLogger(PropertiesPlugin.class);
	
	private static Map<String,Object> paramMap = new HashMap<String,Object>();

	private Properties properties;
	
	public PropertiesPlugin(Properties properties){
		this.properties=properties;
	}
	
	/**
	 * 获取配置参数值
	 * @param key
	 * @return
	 */
	public static Object getParamMapValue(String key){
		return paramMap.get(key);
	}

	@Override
	public boolean start() {
		paramMap.put(DictKeys.db_type_key, properties.getProperty(DictKeys.db_type_key).trim());
	//  判断数据库类型
			String db_type=(String)getParamMapValue(DictKeys.db_type_key);
			//  pg数据库连接信息
			if(db_type.equals(DictKeys.db_type_postgresql)){
				//  读取配置文件
				paramMap.put(DictKeys.db_connection_driverClass, properties.getProperty("postgresql.driverClass").trim());
				paramMap.put(DictKeys.db_connection_jdbcUrl, properties.getProperty("postgresql.jdbcUrl").trim());
				paramMap.put(DictKeys.db_connection_userName, properties.getProperty("postgresql.userName").trim());
				paramMap.put(DictKeys.db_connection_passWord, properties.getProperty("postgresql.passWord").trim());
				
				//  解析数据库连接URL，获取数据库名称
				String jdbcUrl=(String)getParamMapValue(DictKeys.db_connection_jdbcUrl);
				String database=jdbcUrl.substring(jdbcUrl.indexOf("//")+2);
				database=database.substring(database.indexOf("/"+1));
				
				//  解析数据库连接URL，获取数据库地址IP
				String ip = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
				ip = ip.substring(0, ip.indexOf(":"));
				
				//  解析数据库连接URL，获取数据库地址端口
				String port = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
				port = port.substring(port.indexOf(":") + 1, port.indexOf("/"));
				
				//  把数据库连接信息写入常用map
				paramMap.put(DictKeys.db_connection_ip, ip);
				paramMap.put(DictKeys.db_connection_port, port);
				paramMap.put(DictKeys.db_connection_dbName, database);
				
			}
			//  mysql 数据库连接信息
			else if(db_type.equals(DictKeys.db_type_mysql)){ 
				// 读取当前配置数据库连接信息
				paramMap.put(DictKeys.db_connection_driverClass, "com.mysql.jdbc.Driver");
				paramMap.put(DictKeys.db_connection_jdbcUrl, properties.getProperty("mysql.jdbcUrl").trim());
				paramMap.put(DictKeys.db_connection_userName, properties.getProperty("mysql.userName").trim());
				paramMap.put(DictKeys.db_connection_passWord, properties.getProperty("mysql.passWord").trim());
				
				//  解析数据库连接URL，获取数据库名称
				String jdbcUrl = (String) getParamMapValue(DictKeys.db_connection_jdbcUrl);
				String database = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
				database = database.substring(database.indexOf("/") + 1, database.indexOf("?"));

				//  解析数据库连接URL，获取数据库地址IP
				String ip = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
				ip = ip.substring(0, ip.indexOf(":"));

				//  解析数据库连接URL，获取数据库地址端口
				String port = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
				port = port.substring(port.indexOf(":") + 1, port.indexOf("/"));
				
				//  把数据库连接信息写入常用map
				paramMap.put(DictKeys.db_connection_ip, ip);
				paramMap.put(DictKeys.db_connection_port, port);
				paramMap.put(DictKeys.db_connection_dbName, database);
				
			}
			//  oracle 数据库连接信息
			else if(db_type.equals(DictKeys.db_type_oracle)){ 
				// 读取当前配置数据库连接信息
				paramMap.put(DictKeys.db_connection_driverClass, properties.getProperty("oracle.driverClass").trim());
				paramMap.put(DictKeys.db_connection_jdbcUrl, properties.getProperty("oracle.jdbcUrl").trim());
				paramMap.put(DictKeys.db_connection_userName, properties.getProperty("oracle.userName").trim());
				paramMap.put(DictKeys.db_connection_passWord, properties.getProperty("oracle.passWord").trim());
				
				//  解析数据库连接URL，获取数据库名称
				String jdbcUrl = (String) getParamMapValue(DictKeys.db_connection_jdbcUrl);
				String[] prop =  jdbcUrl.substring(jdbcUrl.indexOf("@")+1).split(":");
				String database= prop[2];
				
				//  解析数据库连接URL，获取数据库地址IP
				String ip = prop[0];

				//  解析数据库连接URL，获取数据库地址端口
				String port = prop[1];
				
				//  把数据库连接信息写入常用map
				paramMap.put(DictKeys.db_connection_ip, ip);
				paramMap.put(DictKeys.db_connection_port, port);
				paramMap.put(DictKeys.db_connection_dbName, database);
				
			}
			
			//  数据库连接池信息
			paramMap.put(DictKeys.db_initialSize, Integer.valueOf(properties.getProperty(DictKeys.db_initialSize).trim()));
			paramMap.put(DictKeys.db_minIdle, Integer.valueOf(properties.getProperty(DictKeys.db_minIdle).trim()));
			paramMap.put(DictKeys.db_maxActive, Integer.valueOf(properties.getProperty(DictKeys.db_maxActive).trim()));
			paramMap.put(DictKeys.db_connectionTimeoutMillis, Integer.valueOf(properties.getProperty(DictKeys.db_connectionTimeoutMillis).trim()));
			
			//  把常用配置信息写入map
			String scan_package = properties.getProperty(DictKeys.config_scan_package).trim();
			//  扫描文件路径
			if(null != scan_package && !scan_package.isEmpty()){
				List<String> list = new ArrayList<String>();
				String[] pkgs = scan_package.split(",");
				for (String pkg : pkgs) {
					list.add(pkg.trim());
				}
				paramMap.put(DictKeys.config_scan_package, list);
			}else{
				paramMap.put(DictKeys.config_scan_package, new ArrayList<String>());
			}
			
			//  扫描包路径
			String scan_jar = properties.getProperty(DictKeys.config_scan_jar).trim();
			if(null != scan_jar && !scan_jar.isEmpty()){
				List<String> list = new ArrayList<String>();
				String[] jars = scan_jar.split(",");
				for (String jar : jars) {
					list.add(jar.trim());
				}
				paramMap.put(DictKeys.config_scan_jar, list);
			}else{
				paramMap.put(DictKeys.config_scan_jar, new ArrayList<String>());
			}
			
			paramMap.put(DictKeys.config_devMode, properties.getProperty(DictKeys.config_devMode).trim());
			
			paramMap.put(DictKeys.config_luceneIndex, properties.getProperty(DictKeys.config_luceneIndex).trim());
			
			paramMap.put(DictKeys.config_securityKey_key, properties.getProperty(DictKeys.config_securityKey_key).trim());
			
			paramMap.put(DictKeys.config_passErrorCount_key, Integer.valueOf(properties.getProperty(DictKeys.config_passErrorCount_key)));
			
			paramMap.put(DictKeys.config_passErrorHour_key, Integer.valueOf(properties.getProperty(DictKeys.config_passErrorHour_key)));
			
			paramMap.put(DictKeys.config_maxPostSize_key, Integer.valueOf(properties.getProperty(DictKeys.config_maxPostSize_key)));
			
			paramMap.put(DictKeys.config_maxAge_key, Integer.valueOf(properties.getProperty(DictKeys.config_maxAge_key)));
			
			paramMap.put(DictKeys.config_domain_key, properties.getProperty(DictKeys.config_domain_key));
			
			// mail配置
			paramMap.put(DictKeys.config_mail_host, properties.getProperty(DictKeys.config_mail_host).trim());
			paramMap.put(DictKeys.config_mail_port, properties.getProperty(DictKeys.config_mail_port).trim());
			paramMap.put(DictKeys.config_mail_from, properties.getProperty(DictKeys.config_mail_from).trim());
			paramMap.put(DictKeys.config_mail_userName, properties.getProperty(DictKeys.config_mail_userName).trim());
			paramMap.put(DictKeys.config_mail_password, properties.getProperty(DictKeys.config_mail_password).trim());
			paramMap.put(DictKeys.config_mail_to, properties.getProperty(DictKeys.config_mail_to).trim());
			
			for(String key:paramMap.keySet()){
				log.debug("全局参数配置："+key+" = "+paramMap.get(key));
			}
			return true;
	}

	@Override
	public boolean stop() {
		paramMap.clear();
		return false;
	}

}
