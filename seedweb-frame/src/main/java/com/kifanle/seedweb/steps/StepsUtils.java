package com.kifanle.seedweb.steps;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.kifanle.seedweb.util.core.WebContextUtils;
/**
 * steps 解析工具类。ex "step1{a:as+*d.f;sdxx:123;xxx:122},step2{sadf:xxx},step3,step4"<br>
 * 代表这个steps 有4个步骤：step1,step2,step3,step4.带过step1的配置参数为括号里的键值对。<br>
 * 具体使用 参考 注解 Step:{@link com.kifanle.seedweb.annotation.Step}} 和默认处理Steps类<br>
 * DefaultSteps:{@link com.kifanle.seedweb.steps.DefaultSteps}}
 * @author zhouqin
 * 2013-09-28 20:33:21
 */
public class StepsUtils {
	public final static String REGX_KEY_VAL ="(\\w+:[\\w\\.\\+\\*]+)";
	
	public final static String REGX_KEY_VAL_S ="("+ REGX_KEY_VAL+"|("+REGX_KEY_VAL+";)+"+REGX_KEY_VAL+")";
	
	public final static String REGX_STEP_UNIT="(\\w+(\\{"+REGX_KEY_VAL_S+"\\})?)";
	
	public final static String REGX_STEPS=REGX_STEP_UNIT+"|("+ REGX_STEP_UNIT+",)+"+REGX_STEP_UNIT;
	
	private static Pattern  p = Pattern.compile(REGX_STEPS);
	private static Pattern  p1 = Pattern.compile("(\\w+)\\{");
	private static Pattern  p2 = Pattern.compile("(\\{"+REGX_KEY_VAL_S+"\\})");
	private static Pattern  p3 = Pattern.compile(REGX_KEY_VAL);
	
	public static boolean iss(String s){
		return s.matches(REGX_STEPS);
	}
	
	/**
	 * 应用初始化已对入口参数做检测。参数不做格式判断。
	 * @param s
	 * @return
	 */
	public static Map<String,LinkedHashMap<String,String>> getStepsMap(String s){
		Map<String, LinkedHashMap<String,String>> maps = new LinkedHashMap<String,LinkedHashMap<String,String>>();
		Matcher m = p.matcher(s);
		while(m.find()){
			String group = m.group();
			Matcher m1 = p1.matcher(group);
			String step = "";
			if(m1.find()){
				step = m1.group().replaceAll("\\{", "");
			}else{
				step = group;
			}
			maps.put(step, new LinkedHashMap<String,String>());
			Matcher m2 = p2.matcher(group);
			while(m2.find()){
				if(StringUtils.isNotBlank(m2.group())){
					Matcher m3 = p3.matcher(m2.group());
					while(m3.find()){
						String[] kv = m3.group().split(":");
						maps.get(step).put(kv[0], kv[1]);
					}
				}
			}
		}
		return maps;
	}

	/**
	 * 获取step参数，返回double值，默认0.0
	 * @param name
	 * @return double
	 */
	public static double getDoubleParam(String name) {
		return getDoubleParam(name, 0.0);
	}

    /**
     * 获取step参数，返回double值
     * @param name
     * @param defaultValue
     * @return double
     */
	public static double getDoubleParam(String name, Double defaultValue) {
		Object value = WebContextUtils.getExt(name);
		try {
			return Double.parseDouble((String) value);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 获取step参数，返回int值，默认0
	 * @param name
	 * @return int
	 */
	public static int getIntParam(String name) {
		return getIntParam(name, 0);
	}
	
	/**
	 * 获取step参数，返回int值
	 * @param name
	 * @param defaultVal
	 * @return int
	 */
	public static int getIntParam(String name, int defaultVal) {
		Object value = WebContextUtils.getExt(name);
		try {
			return Integer.parseInt((String) value);
		} catch (Exception e) {
		}
		return defaultVal;
	}

	/**
	 * 获取step参数，返回long值，默认0L
	 * @param name
	 * @return long
	 */
	public static long getLongParam(String name) {
		return getLongParam(name, 0L);
	}

	/**
	 * 获取step参数，返回long值
	 * @param name
	 * @param defaultVal
	 * @return long
	 */
	public static long getLongParam(String name, long defaultVal) {
		Object value = WebContextUtils.getExt(name);
		try {
			return Long.parseLong((String) value);
		} catch (Exception e) {
		}
		return defaultVal;
	}

	/**
	 * 获取step参数，返回字符串，默认""
	 * @param name
	 * @return String
	 */
	public static String getStringParam(String name) {
		return getStringParam(name, "");
	}

	/**
	 * 获取step参数，返回字符串
	 * @param name
	 * @param defaultVal
	 * @return String
	 */
	public static String getStringParam(String name, String defaultVal) {
		Object value = WebContextUtils.getExt(name);
		try {
			return (String) value;
		} catch (Exception e) {
		}
		return defaultVal;
	}
	
}
