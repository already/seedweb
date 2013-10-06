package com.kifanle.seedweb.util.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class StringUtil {
	
	private final static Pattern  PATTERN_PARAM = Pattern.compile("^-(\\w+):([A-Za-z0-9\\-\\.]+)$");
	/**
	 * 首字母变小写
	 * @param var
	 * @return
	 */
	public static String tramsFirstChar2LowerCase(String var){
		return var.substring(0, 1).toLowerCase()+var.substring(1, var.length());
	}

	/**
	 * 按"PATTERN_PARAM"获取符合要求的参数
	 * @param args
	 * @return Map
	 */
	public static Map<String,String> getParamMap(String[] args){
		Map<String,String> paramMap = new HashMap<String,String>();
    	if(ArrayUtils.isNotEmpty(args)){
    		for(String arg:args){
    			Matcher m = PATTERN_PARAM.matcher(arg);
    			while(m.find()){
    				paramMap.put(m.group(1), m.group(2));
    			}
    		}
    	}
    	return paramMap;
	}
	
	public static String getString(Map<String,String> map,String key,String defaultValue){
		String value = map.get(key);
		return StringUtils.isNotBlank(value)?value:defaultValue;
	}
	
	public static int getInt(Map<String,String> map,String key,int defaultValue){
		String value = map.get(key);
		if (StringUtils.isBlank(value))
			return defaultValue;
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
		}
		return defaultValue;
	}
}
