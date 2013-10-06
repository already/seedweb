package com.kifanle.seedweb.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kifanle.seedweb.aj.ResponseType;
import com.kifanle.seedweb.util.core.WebContextUtils;


/**
 * 辅助类，可被继承。//TODO 增加cookie，参数处理类。
 * @author zhouqin
 *
 */
public abstract class HandlerSupport {
	public HttpServletRequest getRequest(){
		return WebContextUtils.getRequest();
	}
	
	public HttpServletResponse getResponse(){
		return WebContextUtils.getResponse();
	}
	
	public void putView(String key,Object value){
		WebContextUtils.getViewContext().put(key, value);
	}
	
	public Object getView(String key){
		return WebContextUtils.getViewContext().get(key);
	}
	
	public void putMsg(String msg){
		putView("message",msg);
	}
	
	/**
	 * 
	 * @param path
	 * @param msg
	 */
	public void forward(String path,String key,String msg){
		WebContextUtils.putExt(key, msg);
		WebContextUtils.putView(key, msg);
		ResponseType.FORWARD.doResponse(path);
	}
}
