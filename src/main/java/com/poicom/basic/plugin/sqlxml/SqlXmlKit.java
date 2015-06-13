package com.poicom.basic.plugin.sqlxml;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.beetl.core.BeetlKit;

import cn.dreampie.jaxb.JaxbKit;

import com.google.common.collect.Maps;
import com.jfinal.log.Logger;

/**
 * 封装获取xml文件中的sql。
 * @author FireTercel 2015年6月2日 
 *
 */
public class SqlXmlKit {
	
	private static final Logger log = Logger.getLogger(SqlXmlKit.class);
	
	/**
	 * 保存xml中所有的sql语句
	 */
	private static Map<String, String> sqlMap;
	
	/**
	 * 过滤掉的sql关键字
	 */
	private static final List<String> badKeyWordList=new ArrayList<String>();
	
	/**
	 * 加载关键字到List
	 */
	static {
		String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|"
				+ "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|"
				+ "table|from|grant|use|group_concat|column_name|"
				+ "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|"
				+ "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";
		badKeyWordList.addAll(Arrays.asList(badStr.split("\\|")));
	}
	
	/**
	 * sql查询关键字过滤效验 防止注入
	 * @param queryStr
	 * @return
	 */
	public static boolean keywordVali(String queryStr) {
		// 统一转为小写
		queryStr = queryStr.toLowerCase();
		for (String badKeyWord : badKeyWordList) {
			// indexOf()方法，如果匹配不到 return -1
			if (queryStr.indexOf(badKeyWord) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取SQL
	 * @param sqlId
	 * @return
	 */
	public static String sql(String sqlId) {
		String sql = sqlMap.get(sqlId);
		if (null == sql || sql.isEmpty()) {
			log.error("SQL语句不存在：SQL ID 是"+sqlId);
		}
		//超过两个空格则变为一个空格。
		return sql.replaceAll("[\\s]{2,}", " ");
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId
	 * @param param
	 * @return
	 */
	public static String sql(String sqlId,Map<String , Object>param){
		String sqlTemplate = sqlMap.get(sqlId);
		if (null == sqlTemplate || sqlTemplate.isEmpty()) {
			log.error("SQL语句不存在：SQL ID 是"+sqlId);
		}
		
		String sql = BeetlKit.render(sqlTemplate, param);
		
		Set<String> keySet = param.keySet();
		for(String key:keySet){
			if(param.get(key)==null){
				break;
			}
			String value = (String) param.get(key);
			value = value.replace("'", "").replace(";", "").replace("--", "");
			sql = sql.replace("#"+key+"#", value);
		}
		//超过两个空格则变为一个空格。
		return sql.replaceAll("[\\s]{2,}", " ");
	}
	
	/**
	 * 获取SQL，动态SQL
	 * @param sqlId		sqlId
	 * @param param	查询参数
	 * @param list			用于接收预处理的值
	 * @return
	 */
	public static String sql(String sqlId,Map<String ,String> param ,LinkedList<Object> list){
		String sqlTemplate = sqlMap.get(sqlId);
		if (null == sqlTemplate || sqlTemplate.isEmpty()) {
			log.error("SQL语句不存在：SQL ID 是"+sqlId);
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Set<String> paramKeySet = param.keySet();
		
		for(String paramKey : paramKeySet){
			paramMap.put(paramKey, (Object)param.get(paramKey));
		}
		String sql = BeetlKit.render(sqlTemplate, paramMap);
		
		Pattern pattern = Pattern.compile("#[\\w\\d\\$\\'\\%\\_]+#");	//#[\\w\\d]+#    \\$
		Pattern pattern2 = Pattern.compile("\\$[\\w\\d\\_]+\\$");
		
		Matcher matcher = pattern.matcher(sql);
		
		while(matcher.find()){
			// 得到的结果形式：#'%$names$%'#
			String clounm =  matcher.group(0);
			
			Matcher matcher2=  pattern2.matcher(clounm);
			matcher2.find();
			// 得到的结果形式：$names$
			String clounm2 = matcher2.group(0); 
			
			String clounm3 = clounm2.replace("$", "");
			
			//  数值型，可以对应处理int、long、bigdecimal、double等等
			if(clounm.equals("#"+clounm2+"#")){
				String val = (String) param.get(clounm3);
				
				try{
					Integer.parseInt(val);
					sql=sql.replace(clounm, val);
				}catch(NumberFormatException e){
					log.error("查询参数值错误，整型值传入了字符串，非法字符串是：" + val);
					return null;
				}
				
			}
			//  字符串，主要是字符串模糊查询、日期比较的查询
			else{
				String val = (String) param.get(clounm3);
				
				String clounm4 = clounm.replace("#", "").replace("'", "").replace(clounm2, val);
				list.add(clounm4);
				
				sql=sql.replace(clounm, "?");
				
			}
		}
		
		return sql.replace("[\\s]{2,}", " ");
	}
	
	/**
	 * 清除加载的SQL语句
	 */
	static void destroy() {
		sqlMap.clear();
	}
	
	static void init() {
		sqlMap = new HashMap<String, String>();
		// 加载sql文件
		Enumeration<URL> baseURLs = null;
		try {
			baseURLs = SqlXmlKit.class.getClassLoader().getResources("sql");

			if (baseURLs == null) {
				baseURLs = SqlXmlKit.class.getClassLoader().getResources("");
			}
			URL url = null;
			while (baseURLs.hasMoreElements()) {
				url = baseURLs.nextElement();
				loadFilePath(url);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		//log.debug("sqlMap" + sqlMap);
	}

	static void init(String... paths) {
		sqlMap = new HashMap<String, String>();

		for (String path : paths) {
			if (path.startsWith("/")) {
				path += path.substring(1);
			}
			Enumeration<URL> baseURLs = null;
			try {
				baseURLs = SqlXmlKit.class.getClassLoader().getResources(path);

				if (baseURLs == null) {
					baseURLs = SqlXmlKit.class.getClassLoader().getResources("");
				}
				URL url = null;
				while (baseURLs.hasMoreElements()) {
					url = baseURLs.nextElement();
					loadFilePath(url);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//log.debug("sqlMap" + sqlMap);
	}

	private static void loadFilePath(URL baseURL) {
		if (baseURL != null) {
			String protocol = baseURL.getProtocol();
			String basePath = baseURL.getFile();
			if ("jar".equals(protocol)) {
				String[] pathurls = basePath.split("!/");
				// 获取jar
				try {
					loadJarFileList(URLDecoder.decode(
							pathurls[0].replace("file:", ""), "UTF-8"),
							pathurls[1]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// 加载sql文件
				loadFileList(basePath);
			}
		}
	}
	
	/**
	 * 
	 * @param strPath
	 */
	public static void loadFileList(String strPath) {
		List<File> files = new ArrayList<File>();
		File dir = new File(strPath);
		File[] dirs = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith("sql")
						|| pathname.getName().endsWith("sql.xml")) {
					return true;
				}
				return false;
			}
		});

		if (dirs == null)
			return;
		for (int i = 0; i < dirs.length; i++) {
			if (dirs[i].isDirectory()) {
				loadFileList(dirs[i].getAbsolutePath());
			} else {
				if (dirs[i].getName().endsWith("sql.xml")) {
					files.add(dirs[i]);
				}
			}
		}
		// 加载sql文件
		loadFiles(files);
	}
	
	/**
	 * 从jar里加载文件
	 * @param filePath				文件路径
	 * @param packageName	包名
	 * @throws IOException		文件读取异常
	 */
	private static void loadJarFileList(String filePath, String packageName)
			throws IOException {
		Map<String, InputStream> sqlFiles = Maps.newHashMap();
		JarFile localJarFile = new JarFile(new File(filePath));
		sqlFiles = findInJar(localJarFile, packageName);
		// 加载sql文件
		loadStreamFiles(sqlFiles);
		localJarFile.close();
	}
	
	/**
	 * 从jar里加载文件
	 * @param localJarFile
	 * @param packageName
	 * @return
	 */
	private static Map<String, InputStream> findInJar(JarFile localJarFile,
			String packageName) {
		Map<String, InputStream> sqlFiles = Maps.newHashMap();
		Enumeration<JarEntry> entries = localJarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			String entryName = jarEntry.getName();
			if (!jarEntry.isDirectory()
					&& (packageName == null || entryName
							.startsWith(packageName))
					&& entryName.endsWith("sql.xml")) {
				sqlFiles.put(
						entryName.substring(entryName.lastIndexOf("/") + 1),
						SqlXmlKit.class.getResourceAsStream(File.separator
								+ entryName));
			}
		}
		return sqlFiles;
	}
	
	/**
	 * 加载xml文件
	 * @param files
	 */
	private static void loadFiles(List<File> files) {
		for (File xmlfile : files) {
			SqlRoot root = JaxbKit.unmarshal(xmlfile, SqlRoot.class);
			for (SqlGroup sqlGroup : root.sqlGroups) {

				getSql(xmlfile.getName(), sqlGroup);
			}
		}
	}
	
	/**
	 * 读取xml文件流集合。
	 * @param streams
	 */
	private static void loadStreamFiles(Map<String, InputStream> streams) {
		for (String filename : streams.keySet()) {
			SqlRoot root = JaxbKit.unmarshal(streams.get(filename),
					SqlRoot.class);
			for (SqlGroup sqlGroup : root.sqlGroups) {

				getSql(filename, sqlGroup);
			}
		}
	}
	
	/**
	 * 将xml文件中的sql语句保存到sqlMap中。
	 * @param filename
	 * @param sqlGroup
	 */
	private static void getSql(String filename, SqlGroup sqlGroup) {
		String name = sqlGroup.name;
		if (name == null || name.trim().equals("")) {
			name = filename;
		}
		for (SqlItem sqlItem : sqlGroup.sqlItems) {
			sqlMap.put(name + "." + sqlItem.id, sqlItem.value);
			log.debug("SQL加载：sql 文件名称 =" + filename + "， sql 主键 = " + name
					+ "." + sqlItem.id + "， sql 值 = "
					+ sqlItem.value.replaceAll("[\\s]{2,}", " "));
		}
	}
	

}
