package com.kifanle.seedweb.util.ext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

import com.kifanle.seedweb.aj.ResponseType;
import com.kifanle.seedweb.util.core.PathUtils;
import com.kifanle.seedweb.util.core.WebContextUtils;

/**
 * seedweb 请求、响应、参数获取类。
 * @author zhouqin
 *
 */
public class WebUtils{
	public static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	
	/**
	 * 获取当前request
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest(){
		return WebContextUtils.getRequest();
	}
	
	/**
	 * 获取当前response
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse(){
		return WebContextUtils.getResponse();
	}

	/**
	 * 获取当前页面渲染上下文
	 * @return
	 */
	public static Map<String,Object> getViewContext(){
		return WebContextUtils.getViewContext();
	}
	
	/**
	 * 设置页面渲染键值
	 * @param key
	 * @param value
	 */
	public static void put(String key,Object value){
		WebContextUtils.getViewContext().put(key, value);
	}
	
	/**
	 * 请求forward
	 * @param path
	 * @param msg
	 */
	public static void forward(String path){
		ResponseType.FORWARD.doResponse(path);
	}
	
	/**
	 * 请求forward
	 * @param path
	 * @param msg
	 */
	public static void forward(String path,String key,String msg){
		WebContextUtils.putExt(key, msg);
		WebContextUtils.putView(key, msg);
		ResponseType.FORWARD.doResponse(path);
	}
	
	/**
	 * 请求redirect
	 * @param path
	 */
	public static void redirect(String path){
		ResponseType.REDIRECT.doResponse(path);
	}
	
	/**
	 * 返回json，默认全局字符集
	 * @param obj
	 */
	public static void rentJson(Object obj){
		rentJson(obj,null);
	}
	
	/**
	 * 返回json
	 * @param obj, bean对象
	 * @param charset
	 */
	public static void rentJson(Object obj,String charset){
		if(StringUtils.isNotBlank(charset)){
			WebContextUtils.putExt(WebContextUtils.CONTEXT_EXT_CHAESET_TAG, charset);
		}
		ResponseType.JSON.doResponse(obj);
	}
	
	/**
	 * 返回text，默认全局字符集
	 * @param text
	 */
	public static void rentText(String text){
		rentText(text,null);
	}
	
	/**
	 * 返回text
	 * @param text
	 * @param charset
	 */
	public static void rentText(String text,String charset){
		if(StringUtils.isNotBlank(charset)){
			WebContextUtils.putExt(WebContextUtils.CONTEXT_EXT_CHAESET_TAG, charset);
		}
		ResponseType.TEXT.doResponse(text);
	}
	
	/**
	 * 返回html
	 * @param html
	 */
	public static void rentHtml(String html){
		rentHtml(html,null);
	}
	
	/**
	 * 返回html
	 * @param html
	 * @param charset
	 */
	public static void rentHtml(String html,String charset){
		WebContextUtils.putExt(WebContextUtils.CONTEXT_EXT_CONTENT_TYPE_TAG, "text/html");
		if(StringUtils.isNotBlank(charset)){
			WebContextUtils.putExt(WebContextUtils.CONTEXT_EXT_CHAESET_TAG, charset);
		}
		ResponseType.TEXT.doResponse(html);
	}
	
	/**
	 * 返回json
	 * @param json 已转换json串
	 */
	public static void rentJson(String json){
		rentJson(json,null);
	}
	
    /**
     * 返回json
     * @param json 已转换json串
     * @param charset
     */
	public static void rentJson(String json,String charset){
		WebContextUtils.putExt(WebContextUtils.CONTEXT_EXT_CONTENT_TYPE_TAG, "application/json");
		if(StringUtils.isNotBlank(charset)){
			WebContextUtils.putExt(WebContextUtils.CONTEXT_EXT_CHAESET_TAG, charset);
		}
		ResponseType.TEXT.doResponse(json);
	}
	
	/**
	 * 获取request参数，返回date,默认 null
	 * @param format
	 * @param name
	 * @return Date
	 */
	public static Date getDateParam(DateFormat format, String name) {
		return getDateParam(format, name, null);
	}

	/**
	 * 获取request参数，返回date
	 * @param format
	 * @param name
	 * @param defaultValue
	 * @return Date
	 */
	public static Date getDateParam(DateFormat format, String name,
			Date defaultValue) {
		String value = getRequest().getParameter(name);
		Date date = defaultValue;
		if (value != null) {
			try {
				format.setLenient(false);
				date = format.parse(value);
			} catch (Exception e) {
				date = defaultValue;
			}
		}
		return date;
	}

	/**
	 * 获取request参数，返回double值，默认0.0
	 * default 0.0
	 * @param name
	 * @return double
	 */
	public static double getDoubleParam(String name) {
		return getDoubleParam(name, 0.0);
	}

	/**
	 * 获取request参数，返回double值
	 * @param name
	 * @param defaultValue
	 * @return double
	 */
	public static double getDoubleParam(String name, Double defaultValue) {
		String value = getRequest().getParameter(name);
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * 获取request参数，返回int值，默认0
	 * @param name
	 * @return int
	 */
	public static int getIntParam(String name) {
		return getIntParam(name, 0);
	}
	
	/**
	 * 获取request参数，返回int值
	 * @param name
	 * @return int
	 */
	public static int getIntParam(String name, int defaultVal) {
		String value = getRequest().getParameter(name);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
		}
		return defaultVal;
	}

	/**
	 * 获取request参数，返回long值，默认0L
	 * @param name
	 * @return long
	 */
	public static long getLongParam(String name) {
		return getLongParam(name, 0L);
	}

	/**
	 * 获取request参数，返回long值
	 * @param name
	 * @param defaultVal
	 * @return long
	 */
	public static long getLongParam(String name, long defaultVal) {
		String value = getRequest().getParameter(name);
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
		}
		return defaultVal;
	}

	/**
	 * 获取request参数，返回 String数组
	 * @param name
	 * @return String数组
	 */
	public static String[] getStringParams(String name) {
		return getRequest().getParameterValues(name);
	}

    /**
     * 获取request参数，返回long数组
     * @param req
     * @param name  参数名
     * @return long 数组
     */
    public static long[] getLongParams(HttpServletRequest req, String name){
        String[] values = getStringParams(name);
        if(values==null) return null;
        return (long[]) ConvertUtils.convert(values, long.class);
    }
	
	/**
	 * 获取request参数，返回字符串，默认""
	 * @param name
	 * @return 字符串
	 */
	public static String getStringParam(String name) {
		return getStringParam(name, "");
	}

	/**
	 * 获取request参数，返回字符串
	 * @param name
	 * @param defaultVal 默认值
	 * @return 字符串
	 */
	public static String getStringParam(String name, String defaultVal) {
		String value = getRequest().getParameter(name);
		if (value == null)
			return defaultVal;
		return value.trim();
	}

	/**
	 * 获取远程请求ip
	 * @return ip字符串
	 */
	public static String getRemoteIP() {
		try {
			String ip = getRequest().getHeader(
					"X-Forwarded-For");
			if (ip == null)
				ip = getRequest().getRemoteHost();
			if (ip == null)
				ip = "127.0.0.1";
			return ip;
		} catch (Exception e) {
		}
		return "127.0.0.1";
	}
	
    /**
     * 获取cookie
     * @param request
     * @param name
     * @return Cookie or null
     */
    public static Cookie getCookie(String name) {
        Cookie[] cookies = getRequest().getCookies();
        if(cookies == null) return null;
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name,ck.getName()))
                return ck;
        }
        return null;
    }
    
    /**
     * 获取cookie 值
     * @param name
     * @return cookie值
     */
    public static String getCookieVal(String name){
    	Cookie cookie = getCookie(name);
    	return (null!=cookie)?cookie.getValue():null;
    }


    /**
     * 设置cookie
     * @param name
     * @param value
     * @param maxAge
     * @param belongAll cookie 是否为根domian使用
     */
    public static void setCookie(String name,
            String value, int maxAge, boolean belongAll) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        if(belongAll){
            String serverName = getRequest().getServerName();
            String domain = PathUtils.getDomainName(serverName);
            if(domain!=null && domain.indexOf('.')!=-1){
                cookie.setDomain('.' + domain);
            }
        }
        cookie.setPath("/");
        getResponse().addCookie(cookie);
    }
    
    /**
     * 设置cookie
     * @param name
     * @param value
     * @param maxAge 有效时
     */
    public static void setCookie(String name,
            String value, int maxAge) {
        setCookie(name,value,maxAge,true);
    }
    
    /**
     * 删除cookie
     * @param name
     * @param belongAll
     */
    public static void deleteCookie(String name, boolean belongAll) {
        setCookie(name,"",0,belongAll);
    }
}

