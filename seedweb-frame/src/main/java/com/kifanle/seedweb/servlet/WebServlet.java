package com.kifanle.seedweb.servlet;

import com.kifanle.seedweb.context.MethodContext;
import com.kifanle.seedweb.context.WebApplicationContext;
import com.kifanle.seedweb.util.core.PathUtils;
import com.kifanle.seedweb.util.core.WebContextUtils;
import com.kifanle.seedweb.util.ext.WebUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

/**
 *  框架servlet 基础类
 *  @author zhouqin
 */
public class WebServlet extends AbstractSeedWebServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(WebServlet.class);
	 
	public void init() {
		if (!WebApplicationContext.getInstance().init()) {
			WebApplicationContext.getInstance().destory();
			System.exit(-1);
			return;
		}
	}

	public void handRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		WebContextUtils.init(request,response);
		String path = WebContextUtils.getPath();
		
		if(PathUtils.isIndex(path)){
			WebUtils.forward("/index", "","");
			return;
		}
		
		MethodContext mc =WebApplicationContext.mcMappings.get(path);

		if(null==mc){
			for(Entry<String, MethodContext> entry :WebApplicationContext.mcRexMappings.entrySet()){
				if(path.matches(entry.getKey())){
					mc = entry.getValue();
					LinkedHashSet<String> setMetas = mc.getRestParams();
					if(setMetas.size()>0){
						LinkedHashSet<String> setValues = PathUtils.getRestParamsValue(entry.getKey(),WebContextUtils.getPath());
						Iterator<String> it = setValues.iterator();
						for(String meta:setMetas){
							WebContextUtils.putRest(meta, it.hasNext()?it.next():null);
						}
					}
					break;
				}
			}
		}
		
		if(null!=mc){
			try {
				ReflectionUtils.invokeMethod(mc.getProxyMethod(), mc.getProxyObject());
				return;
			} catch (Exception e) {
				log.error("[invoking method error][method:{}][Object:{}][msg:{}]", 
						new Object[]{mc.getProxyMethod(),mc.getProxyObject(),e.toString()});
			} 
			
		};

		WebUtils.forward("/404", "error","没有此页面。请联系管理员");
		return;
	}
}
