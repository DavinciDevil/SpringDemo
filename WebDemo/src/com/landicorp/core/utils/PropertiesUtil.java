package com.landicorp.core.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {

	public static String getProEnvValue(String name){
		String propValue = "";
		Properties properties = new Properties();
		try {
			properties.load(new InputStreamReader(PropertiesUtil.class.getResourceAsStream("/com/landicorp/config/envconf.properties"),"utf-8"));
			propValue = properties.getProperty(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propValue;
	}
}
