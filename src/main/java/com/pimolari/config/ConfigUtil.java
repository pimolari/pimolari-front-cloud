package com.pimolari.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public class ConfigUtil {

	 public static Properties properties = null;
	
	 public ConfigUtil(Properties properties) throws IOException {		 
		 this.properties = properties;
	 }
	 
	 public ConfigUtil(String propertiesPath) throws IOException {
		 init(propertiesPath);
	 }
	 
	 public void init(String propertiesPath) throws IOException {
		 
		 System.out.println("Properties initialization in progress... " + propertiesPath);
		 properties = new Properties();
		 properties.load(new FileInputStream(new File(propertiesPath)));
		 //properties.load(this.getClass().getResourceAsStream("/config/dev/backend.properties"));
		 
		 System.out.println("" + properties.getProperty("service.name") + " service loaded");
	 }

	 public static String get(String key) {
		 return properties.getProperty(key);
	 }

	 public static String get(String key, String... value) {
		 return MessageFormat.format(get(key), (Object[]) value);
	 }

}
