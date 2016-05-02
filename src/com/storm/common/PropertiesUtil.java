package com.storm.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	// TODO configure logger for this class

	private static PropertiesUtil propertyUtil;

	public static final String PROPERTY_FILE_NAME = "config.properties";

	protected Properties properties = new Properties();

	private PropertiesUtil() {

		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
			properties.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static PropertiesUtil getInstance() {
		if (propertyUtil == null) {
			propertyUtil = new PropertiesUtil();
		}
		return propertyUtil;
	}

	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	// TODO add other methods to get individual datatype properties like
	// getInteger, etc

}
