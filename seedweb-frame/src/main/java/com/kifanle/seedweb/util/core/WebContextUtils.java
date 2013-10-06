package com.kifanle.seedweb.util.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kifanle.seedweb.aj.ResponseType;

public class WebContextUtils {
	public final static String CONTEXT_REQUEST_TAG = "context_request_tag";
	public final static String CONTEXT_RESPONSE_TAG = "context_response_tag";
	public final static String CONTEXT_CONTEXT_TAG = "context_context_tag";
	public final static String CONTEXT_EXT_TAG = "context_ext_tag";
	public final static String CONTEXT_EXT_CHAESET_TAG = "context_ext_charset_tag";
	public final static String CONTEXT_EXT_CONTENT_TYPE_TAG = "context_ext_content_type_tag";
	public final static String CONTEXT_REST_TAG = "context_rest_tag";
	public final static String CONTEXT_RESPONSETYPE_TAG = "context_responseType_tag";
	public final static String CONTEXT_EXECUTION_STACK_TAG = "context_execution_stack_tag";
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocalUtils util = ThreadLocalUtils.getInstance();
	@SuppressWarnings("unchecked")
	private static ThreadLocalUtils<String,HttpServletRequest> reqUtil = util;
	@SuppressWarnings("unchecked")
	private static ThreadLocalUtils<String,HttpServletResponse> resUtil = util;
	@SuppressWarnings("unchecked")
	private static ThreadLocalUtils<String,Map<String,Object>> conUitl = util;
	@SuppressWarnings("unchecked")
	private static ThreadLocalUtils<String,Map<String,Object>> extUitl = util;
	@SuppressWarnings("unchecked")
	private static ThreadLocalUtils<String,Map<String,Object>> restUitl = util;
	
	@SuppressWarnings("unchecked")
	private static ThreadLocalUtils<String,LinkedList<ResponseType>> exestackUtil = util; 
	
	private static void saveRequest(HttpServletRequest request){
		reqUtil.save(CONTEXT_REQUEST_TAG, request);
	} 
	
	private static void saveResponse(HttpServletResponse response){
		resUtil.save(CONTEXT_RESPONSE_TAG, response);
	}
	
	/**
	 * 用来记录当前动作
	 */
	public static void addResponseType2Stack(ResponseType responseType){
		exestackUtil.get(CONTEXT_EXECUTION_STACK_TAG).add(responseType);
	}
	
	public static void init(HttpServletRequest request,HttpServletResponse response){
		saveRequest(request);
		saveResponse(response);
		Map<String,Object> context = conUitl.get(CONTEXT_CONTEXT_TAG);
		conUitl.save(CONTEXT_CONTEXT_TAG,null!=context?context:new HashMap<String,Object>());
		Map<String,Object> ext = extUitl.get(CONTEXT_EXT_TAG);
		extUitl.save(CONTEXT_EXT_TAG, null!=ext?ext:new HashMap<String,Object>());
		Map<String,Object> rest = extUitl.get(CONTEXT_REST_TAG);
		restUitl.save(CONTEXT_REST_TAG, null!=rest?rest:new HashMap<String,Object>());
		LinkedList<ResponseType> stack = exestackUtil.get(CONTEXT_EXECUTION_STACK_TAG);
		exestackUtil.save(CONTEXT_EXECUTION_STACK_TAG, null!=stack?stack:new LinkedList<ResponseType>());
		if(exestackUtil.get(CONTEXT_EXECUTION_STACK_TAG).size()==0){
			exestackUtil.get(CONTEXT_EXECUTION_STACK_TAG).add(ResponseType.NULL);
		}
	}
	
	/**
	 * 清除当前请求有关context。
	 */
	public static void clear(){
		conUitl.get(CONTEXT_CONTEXT_TAG).clear();
		extUitl.get(CONTEXT_EXT_TAG).clear();
		restUitl.get(CONTEXT_REST_TAG).clear();
		exestackUtil.get(CONTEXT_EXECUTION_STACK_TAG).clear();
	}
	
	public static HttpServletRequest getRequest(){
		return reqUtil.get(CONTEXT_REQUEST_TAG);
	}
	
	public static HttpServletResponse getResponse(){
		return resUtil.get(CONTEXT_RESPONSE_TAG);
	}

	public static Map<String,Object> getViewContext(){
		return conUitl.get(CONTEXT_CONTEXT_TAG);
	}
	
	public static String getPath(){
		return PathUtils.getPath(getRequest());
	}
	
	/**
	 * 判断response是否提交
	 * @return
	 */
	public static boolean isResponseCommitted(){
		return getResponse().isCommitted();
	}
	
	public static ResponseType getExecuteResponseTypeFromStack(){
		return exestackUtil.get(CONTEXT_EXECUTION_STACK_TAG).peekLast();
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public static void putView(String key,Object value){
		extUitl.get(CONTEXT_CONTEXT_TAG).put(key, value);
	}
	
	public static void putView(Map<String,String> maps){
		extUitl.get(CONTEXT_CONTEXT_TAG).putAll(maps);
	}
	
	public static Object getView(String key){
		return extUitl.get(CONTEXT_CONTEXT_TAG).get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public static void putExt(String key,Object value){
		extUitl.get(CONTEXT_EXT_TAG).put(key, value);
	}
	
	public static void putExt(Map<String,String> maps){
		extUitl.get(CONTEXT_EXT_TAG).putAll(maps);
	}
	
	public static Object getExt(String key){
		return extUitl.get(CONTEXT_EXT_TAG).get(key);
	}
	
	public static Object removeExt(String key){
		return extUitl.get(CONTEXT_EXT_TAG).remove(key);
	}
	
	public static void putRest(String key,Object value){
		restUitl.get(CONTEXT_REST_TAG).put(key, value);
	}
	
	public static Object getRest(String key){
		return restUitl.get(CONTEXT_REST_TAG).get(key);
	}
   
}
