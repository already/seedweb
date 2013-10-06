package com.webex.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.kifanle.seedweb.aj.ResponseType;
import com.kifanle.seedweb.annotation.RequestMapping;
import com.kifanle.seedweb.handler.HandlerSupport;
import com.kifanle.seedweb.util.core.WebContextUtils;
import com.kifanle.seedweb.util.ext.RestfulUtils;
import com.kifanle.seedweb.util.ext.WebUtils;

@Component
@RequestMapping
public class PathTestHandler extends HandlerSupport{
	private static final Logger log = LoggerFactory.getLogger(PathTestHandler.class);
	
	@RequestMapping(value="/test",steps="ipFilter{ips:192.168.85.12}")
	public void testOnlyRequestParamAndReturnHtml(){
		putView("test", "/asd ok");
		putView("rid", WebUtils.getIntParam("rid"));
		putView("rname", WebUtils.getStringParam("rname"));
	}
	
	@RequestMapping(value="/test1",resType=ResponseType.JSON,steps="ipFilter{ips:127.0.0.1}")
	public Object testOnlyReturnJson(){
		Map<String,String> test = new HashMap<String,String>();
		test.put("obj1", "obj1");
		test.put("obj2", "obj2");
		test.put("obj3", "obj3");
		log.info("method"+Thread.currentThread().toString());
		test.put("thread", Thread.currentThread().toString());
		if(true)
		try {
			WebContextUtils.getResponse().getWriter().append("ssssssssssssss");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return test;
	}
	
	@RequestMapping(value="/user/info/{id}/{info}",resType=ResponseType.JSON)
	public Object testRestfulAndReturnJson1(){
		Map<String,Object> test = new HashMap<String,Object>();
		test.put("id", RestfulUtils.getIntParam("id"));
		test.put("info", RestfulUtils.getStringParam("info"));
		test.put("rid", WebUtils.getIntParam("rid"));
		test.put("rname", WebUtils.getStringParam("rname"));
		return test;
	}

	@RequestMapping(value="/user/{id}/info/{info}",resType=ResponseType.JSON)
	public Object testRestfulAndReturnJson2(){
		Map<String,Object> test = new HashMap<String,Object>();
		test.put("id", RestfulUtils.getIntParam("id"));
		test.put("info", RestfulUtils.getStringParam("info"));
		return test;
	}
	
	@RequestMapping(value="/redirect_test",resType=ResponseType.REDIRECT)
	public Object testRedirect(){
		putView("redirect", "/testRedirect ok");
		return "/test";
	}
	
	@RequestMapping(value="/forward_test",resType=ResponseType.FORWARD)
	public Object testForward(){
		putView("forward", "testForward ok");
		return "/test";
	}
	
	@RequestMapping(value="/test6")
	public Object test6(){
		putView("test5", "555555 ok");
		WebUtils.redirect("http://www.iqiyi.com");
		return "/test";
	}
	
	@RequestMapping(value="/test7",resType=ResponseType.JSON)
	public Object test7(){
		putView("test5", "555555 ok");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("test5", getView("test5"));
		map.put("step2", getView("step2"));
		WebUtils.rentJson(map);
		forward("/404", "error","无此页面，请联系管理员");
		return map;
	}
	
	@RequestMapping(value="/test9")
	public Object testRentHtml(){
		putView("test5", "555555 ok");
		WebUtils.rentHtml("<html><head></head><body>just html.</body><html>");
		return "/test";
	}
	
	@RequestMapping(value="/test8")
	public Object testJson(){
		putView("test5", "555555 ok");
		WebUtils.rentJson("{\"a1\":89;\"a2\":\"a33\"}");
		return "/test";
	}
}