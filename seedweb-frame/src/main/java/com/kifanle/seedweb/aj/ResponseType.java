package com.kifanle.seedweb.aj;

import java.io.IOException;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;

import com.kifanle.seedweb.util.core.WebContextUtils;
import com.kifanle.seedweb.util.ext.JacksonUtils;
import com.kifanle.seedweb.util.ext.WebUtils;
import com.kifanle.seedweb.velocity.SeedwebVelocityContext;
import com.kifanle.seedweb.velocity.VelocityTemplate;


/**
 * 响应抽象类，代表依次处理后的响应，返回“html” “text” “json”，“forward”,“redirect”。<br>
 * 目前支持velocity视图模式，支持返回格式 html、json 、{xml待补充}<br>
 * 目前版本，返回html支持volecity。<br>
 * @author zhouqin
 *
 */
public enum ResponseType {
	HTML(0,"html"){
		protected void action(Object obj){
	    	String path = null!=obj?obj.toString():WebContextUtils.getPath();
	    	
	    	
	    	Template template = VelocityTemplate.getTemplate(path + ".vm"); 
	    	if(null==template){
				WebContextUtils.addResponseType2Stack(FORWARD);
				WebUtils.forward("/404", "error","没有此页面。请联系管理员");
				return;
	    	}
	    	
			try {
				WebContextUtils.getResponse().setContentType("text/html");
				Context context = new SeedwebVelocityContext();
				for(Entry<String, Object> entry : WebContextUtils.getViewContext().entrySet()){
					context.put(entry.getKey(), entry.getValue());
				}
				VelocityTemplate.mergeTemplate(template, context, WebContextUtils.getResponse());
				
			} catch (Exception e) {
				log.error("response html error.", e);
			}finally{
				WebContextUtils.clear();
			} 
			
		}
	},JSON(1,"json"){
		protected void action(Object obj){
			WebContextUtils.getResponse().setContentType("application/json");
			Object charset = WebContextUtils.getExt(WebContextUtils.CONTEXT_EXT_CHAESET_TAG);
			if(null!=charset){
				WebContextUtils.getResponse().setCharacterEncoding((String)charset);
			}
			try {
				String res = (null!=obj)?JacksonUtils.obj2Json(obj):"";
				WebContextUtils.getResponse().getWriter().append(res).flush();
			} catch (IOException e) {
				log.error("response json error.", e);
			}finally{
				WebContextUtils.clear();
			}
		}
	},TEXT(2,"text"){
		//默认为 text
		protected void action(Object obj){
			Object charset = WebContextUtils.getExt(WebContextUtils.CONTEXT_EXT_CHAESET_TAG);
			Object contentType = WebContextUtils.getExt(WebContextUtils.CONTEXT_EXT_CONTENT_TYPE_TAG);
			
			String cType = (null!=contentType)?contentType.toString():"text/plain";
			
			if(null!=charset){
				WebContextUtils.getResponse().setCharacterEncoding((String)charset);
			}
			
			WebContextUtils.getResponse().setContentType(cType);
			
			try {
				String res = (null!=obj)?(String)obj:"";
				WebContextUtils.getResponse().getWriter().append(res).flush();
			} catch (IOException e) {
				log.error("response text error.", e);
			}finally{
				WebContextUtils.clear();
			}
		}
	},XML(3,"xml"){
		protected void action(Object obj){
			WebContextUtils.getResponse().setContentType("application/xml");
		}
	},REDIRECT(4,"redirect"){
		protected void action(Object obj){
			try {
				WebContextUtils.getResponse().sendRedirect(obj.toString());
			} catch (IOException e) {
				log.error("redirect error.", e);
			}finally{
				WebContextUtils.clear();
			}
		}
	},FORWARD(5,"forward"){
		protected void action(Object obj){
			try {
				WebContextUtils.getRequest().getRequestDispatcher(obj.toString())
				.forward(WebContextUtils.getRequest(), WebContextUtils.getResponse());
			}catch (Exception e) {
				WebContextUtils.clear();
				log.error("forward error.", e);
			}
		}
	}
	,NULL(-1,"none"){
		protected void action(Object obj){
			
		}
	};
	
	private static final Logger log = LoggerFactory.getLogger(ResponseType.class);
	
	private int code;
	private String value;	
	
	private ResponseType(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int code() {
		return this.code;
	}

	public String value() {
		return this.value;
	}

	public static ResponseType type(int code) {
		for (ResponseType type : ResponseType.values()) {
			if (type.code == code)
				return type;
		}
		return NULL;
	}

	public void doResponse(Object obj){
		ResponseType rt = WebContextUtils.getExecuteResponseTypeFromStack();
		if(null!=rt&&(!WebContextUtils.isResponseCommitted())&&(rt==NULL || rt  == FORWARD)){
			WebContextUtils.addResponseType2Stack(this);
			action(obj);
		}
		if( WebContextUtils.isResponseCommitted() || this==HTML || this==TEXT || this==JSON || this == XML || this ==REDIRECT){
			WebContextUtils.clear();
		}
	}
	
    abstract void action(Object obj);
	
    
}
