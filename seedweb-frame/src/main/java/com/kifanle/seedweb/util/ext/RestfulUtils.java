package com.kifanle.seedweb.util.ext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kifanle.seedweb.util.core.WebContextUtils;


/**
 * rest 格式数据工具类
 * @author zhouqin
 *
 */
public class RestfulUtils {
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 获取rest参数，返回date值，默认null
	 * @param format
	 * @param name
	 * @return date
	 */
	public static Date getDateParam(DateFormat format, String name) {
		return getDateParam(format, name, null);
	}

	/**
	 * 获取rest参数，返回date值
	 * @param format
	 * @param name
	 * @param defaultValue
	 * @return Date
	 */
	public static Date getDateParam(DateFormat format, String name,
			Date defaultValue) {
		Object value = WebContextUtils.getRest(name);
		Date date = defaultValue;
		if (value != null) {
			try {
				format.setLenient(false);
				date = format.parse((String) value);
			} catch (Exception e) {
				date = defaultValue;
			}
		}
		return date;
	}

	/**
	 * 获取rest参数，返回double值，默认0.0
	 * @param name
	 * @return double
	 */
	public static double getDoubleParam(String name) {
		return getDoubleParam(name, 0.0);
	}

	/**
	 * 获取rest参数，返回double值 
	 * @param name
	 * @param defaultValue
	 * @return double
	 */
	public static double getDoubleParam(String name, Double defaultValue) {
		Object value = WebContextUtils.getRest(name);
		try {
			return Double.parseDouble((String) value);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 获取rest参数，返回int值，默认0
	 * @param name
	 * @return int
	 */
	public static int getIntParam(String name) {
		return getIntParam(name, 0);
	}
	
	/**
	 * 获取rest参数，返回int值
	 * @param name
	 * @param defaultVal
	 * @return int
	 */
	public static int getIntParam(String name, int defaultVal) {
		Object value = WebContextUtils.getRest(name);
		try {
			return Integer.parseInt((String) value);
		} catch (Exception e) {
		}
		return defaultVal;
	}

	/**
	 * 获取rest参数，返回long值，默认0L
	 * @param name
	 * @return long
	 */
	public static long getLongParam(String name) {
		return getLongParam(name, 0L);
	}

	/**
	 * 获取rest参数，返回long值
	 * @param name
	 * @param defaultVal
	 * @return long
	 */
	public static long getLongParam(String name, long defaultVal) {
		Object value = WebContextUtils.getRest(name);
		try {
			return Long.parseLong((String) value);
		} catch (Exception e) {
		}
		return defaultVal;
	}

	/**
	 * 获取rest参数，返回String值
	 * @param name
	 * @return String
	 */
	public static String getStringParam(String name) {
		return getStringParam(name, "");
	}

	/**
	 * 获取rest参数，返回String值,默认null
	 * @param name
	 * @param defaultVal
	 * @return String
	 */
	public static String getStringParam(String name, String defaultVal) {
		Object value = WebContextUtils.getRest(name);
		try {
			return (String) value;
		} catch (Exception e) {
		}
		return defaultVal;
	}
}
