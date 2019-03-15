package com.api.rest.utils;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropLoader {
	private static Logger logger = Logger.getLogger(PropLoader.class);
	
	private static Properties properties;
	
	public static void initialize() {
		try {
			String propName = "api.rest.properties";
			InputStream propFile = PropLoader.class.getClassLoader().getResourceAsStream(propName);
			properties = new Properties();
			properties.load(propFile);
			logger.debug("Loaded config: "+properties.toString());
		} catch (Exception e) {
			logger.error("Error initializing properties: ", e);
		}
	}
	
	public static String get(String key) {
		return properties.getProperty(key);
	}
	
	public static String get(String key, String placeholder) {
		key = key.replace("?", placeholder);
		return properties.getProperty(key);
	}
	
	public static boolean getB(String key) {
		return Boolean.parseBoolean(properties.getProperty(key));
	}
	
	public static boolean getB(String key, String placeholder) {
		key = key.replace("?", placeholder);
		return Boolean.parseBoolean(properties.getProperty(key));
	}
	
	public static int getI(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}
	
	public static int getI(String key, String placeholder) {
		key = key.replace("?", placeholder);
		return Integer.parseInt(properties.getProperty(key));
	}
	
	public static long getL(String key) {
		return Long.valueOf(properties.getProperty(key));
	}
	
	public static long getL(String key, String placeholder) {
		key = key.replace("?", placeholder);
		return Long.valueOf(properties.getProperty(key));
	}
	
	public static Map<String,String> readMap(String baseKey) {
		Map<String,String> propMap = new HashMap<String,String>();
		for (Enumeration<?> e = properties.propertyNames();e.hasMoreElements();) {
			String name = (String)e.nextElement();
			if (name.startsWith(baseKey)) {
				String key = name.split("\\.")[name.split("\\.").length-1];
				String value = properties.getProperty(name);
				propMap.put(key, value);
			}
		}
		return propMap;
	}
	
}
