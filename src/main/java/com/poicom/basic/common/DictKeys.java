package com.poicom.basic.common;

/**
 * 常量数据字典
 * @author FireTercel 2015年6月2日 
 *
 */
public abstract class DictKeys {
	
	/**
	 * URL缓存Key
	 */
	public static final String cache_name_page = "SimplePageCachingFilter";
	
	/**
	 * 系统缓存，主要是权限和数据字典等
	 */
	public static final String cache_name_system = "system";
	
	/**
	 * 扫描的包  扫描的jar
	 */
	public static final String config_scan_package = "config.scan.package";
	public static final String config_scan_jar = "config.scan.jar";
	
	
	/**
	 * 开发模式
	 */
	public static final String config_devMode = "config.devMode";
	
	/**
	 * 是否重新构建Lucene索引（构建索引实在是慢）
	 */
	public static final String config_luceneIndex = "config.luceneIndex";
	
	/**
	 * 加密密钥
	 */
	public static final String config_securityKey_key = "config.securityKey";
	
	/**
	 * 密码最大错误次数
	 */
	public static final String config_passErrorCount_key = "config.passErrorCount";
	
	/**
	 * 密码错误最大次数后间隔登陆时间（小时）
	 */
	public static final String config_passErrorHour_key = "config.passErrorHour";
	
	/**
	 * #文件上传大小限制 10 * 1024 * 1024 = 10M
	 */
	public static final String config_maxPostSize_key = "config.maxPostSize";
	
	/**
	 * # cookie 值的时间
	 */
	public static final String config_maxAge_key = "config.maxAge";
	
	/**
	 * # 域名或者服务器IP，多个逗号隔开，验证Referer时使用
	 */
	public static final String config_domain_key = "config.domain";
	
	/**
	 * mail 配置：邮件服务器地址  端口
	 */
	public static final String config_mail_host = "config.mail.host";
	public static final String config_mail_port = "config.mail.port";
	
	/**
	 * mail 配置：邮件服务器账号  名称  密码  接收邮件地址
	 */
	public static final String config_mail_from = "config.mail.from";
	public static final String config_mail_userName = "config.mail.userName";
	public static final String config_mail_password = "config.mail.password";
	public static final String config_mail_to = "config.mail.to";
	
	/**
	 * 当前数据库类型
	 */
	public static final String db_type_key = "db.type";
	
	/**
	 * 当前数据库类型：postgresql  mysql  oracle
	 */
	public static final String db_type_postgresql = "postgresql";
	public static final String db_type_mysql = "mysql";
	public static final String db_type_oracle = "oracle";
	
	/**
	 * 数据库连接参数：驱动
	 */
	public static final String db_connection_driverClass = "driverClass";
	
	/**
	 * 数据库连接参数：连接URL  用户名  密码
	 */
	public static final String db_connection_jdbcUrl = "jdbcUrl";
	public static final String db_connection_userName = "userName";
	public static final String db_connection_passWord = "passWord";
	
	/**
	 * 数据库连接参数：数据库服务器IP  端口  名称
	 */
	public static final String db_connection_ip = "db_ip";
	public static final String db_connection_port = "db_port";
	public static final String db_connection_dbName = "db_name";
	
	/**
	 * 数据库连接池参数：初始化连接大小  最少连接数  最多连接数
	 */
	public static final String db_initialSize = "db.initialSize";
	public static final String db_minIdle = "db.minIdle";
	public static final String db_maxActive = "db.maxActive";
	
	/**
	 * 数据库连接池参数：空闲超时
	 */
	public static final String db_connectionTimeoutMillis="db.connectionTimeoutMillis";
	
	/**
	 *  主数据源名称：系统主数据源（配置文件暂无）在Config里面进行配置。
	 */
	public static final String db_dataSource_main = "db.dataSource.main";
	
	/**
	 * 分页参数初始化值：默认显示第几页
	 */
	public static final int default_pageNumber = 1;
	
	/**
	 * 分页参数初始化值：默认每页显示几多
	 */
	public static final int default_pageSize = 20;
	
	/**
	 * 用户登录状态码：用户不存在
	 */
	public static final int login_info_0 = 0;
	
	/**
	 * 用户登录状态码：停用账户
	 */
	public static final int login_info_1 = 1;
	
	/**
	 * 用户登录状态码：密码错误次数超限
	 */
	public static final int login_info_2 = 2;
	
	/**
	 * 用户登录状态码：密码验证成功
	 */
	public static final int login_info_3 = 3;
	
	/**
	 * 用户登录状态码：密码验证失败
	 */
	public static final int login_info_4 = 4;

}
