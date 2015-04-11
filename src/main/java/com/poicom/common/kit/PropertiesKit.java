package com.poicom.common.kit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;

public class PropertiesKit {

	private ConcurrentMap<String, Object> propertiesAttrs = new ConcurrentHashMap<String, Object>();
	private ConcurrentMap<String, Properties> propertiesFiles = new ConcurrentHashMap<String, Properties>();

	private static PropertiesKit propertiesKit = new PropertiesKit();

	public static PropertiesKit me() {
		return propertiesKit;
	}

	public static boolean exist(String file) {
		Enumeration<URL> urls = null;
		try {
			urls = PropertiesKit.class.getClassLoader().getResources(file);
			while (urls.hasMoreElements()) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * load 属性文件 已经存在文件则直接返回
	 *
	 * @param file
	 *            文件路径
	 * @return 属性文件
	 */
	public Properties loadPropertyFile(String file) {
		return loadPropertyFile(false, file);
	}

	/**
	 * load 属性文件
	 *
	 * @param file
	 *            文件路径
	 * @param reload
	 *            如果已经加载文件 是否重载
	 * @return 属性文件
	 */
	public Properties loadPropertyFile(boolean reload, String file) {
		Properties properties = new Properties();
		if (StrKit.isBlank(file))
			throw new IllegalArgumentException(
					"Parameter of file can not be blank");
		if (file.contains(".."))
			throw new IllegalArgumentException(
					"Parameter of file can not contains \"..\"");

		InputStream inputStream = null;
		String fullFile; // String fullFile = PathUtil.getWebRootPath() + file;
		// 判断是否带有文件分隔符
		boolean startStuff = file.startsWith("/");
		if (startStuff)
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF"
					+ File.separator + file.substring(1);
		else
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF"
					+ File.separator + file;
		File propFile = new File(fullFile);

		// 判断文件是否存在WebInf
		if (!propFile.exists()) {
			if (!startStuff) {
				fullFile = "/" + file;
			} else {
				fullFile = file;
			}
			if (hasPropertiesFile(reload, fullFile))
				return propertiesFiles.get(fullFile);

			inputStream = PropertiesKit.class.getResourceAsStream(fullFile);
		} else {
			if (hasPropertiesFile(reload, fullFile))
				return propertiesFiles.get(fullFile);
		}
		try {
			// 不是通过resource读取
			if (inputStream == null)
				inputStream = new FileInputStream(propFile);

			properties.load(inputStream);
		} catch (Exception eOne) {
			try {
				ClassLoader loader = Thread.currentThread()
						.getContextClassLoader();
				properties.load(loader.getResourceAsStream(file));
			} catch (IOException eTwo) {
				throw new IllegalArgumentException(
						"Properties file loading failed: " + eTwo.getMessage());
			}
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (properties != null) {
			propertiesFiles.put(fullFile, properties);
			for (Entry<Object, Object> entry : properties.entrySet()) {
				this.propertiesAttrs.put(entry.getKey().toString(),
						entry.getValue());
			}
		}
		return properties;
	}

	private boolean hasPropertiesFile(boolean reload, String fullFile) {
		if (!reload && propertiesFiles.containsKey(fullFile)) {
			return true;
		}
		return false;
	}

	public String getProperty(String key) {
		if (this.propertiesAttrs.containsKey(key)) {
			return propertiesAttrs.get(key).toString();
		}
		return null;
	}

	public String getProperty(String key, String defaultValue) {
		if (this.propertiesAttrs.containsKey(key)) {
			return propertiesAttrs.get(key).toString();
		}
		return defaultValue;
	}

	public Integer getPropertyToInt(String key) {
		Integer resultInt = null;
		String resultStr = this.getProperty(key);
		if (resultStr != null)
			resultInt = Integer.parseInt(resultStr);
		return resultInt;
	}

	public Integer getPropertyToInt(String key, Integer defaultValue) {
		Integer result = getPropertyToInt(key);
		return result != null ? result : defaultValue;
	}

	public Boolean getPropertyToBoolean(String key) {
		String resultStr = this.getProperty(key);
		Boolean resultBool = null;
		if (resultStr != null) {
			if (resultStr.trim().equalsIgnoreCase("true"))
				resultBool = true;
			else if (resultStr.trim().equalsIgnoreCase("false"))
				resultBool = false;
		}
		return resultBool;
	}

	public Boolean getPropertyToBoolean(String key, boolean defaultValue) {
		Boolean result = getPropertyToBoolean(key);
		return result != null ? result : defaultValue;
	}

}
