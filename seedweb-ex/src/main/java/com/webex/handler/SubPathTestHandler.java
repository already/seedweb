package com.webex.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kifanle.seedweb.aj.ResponseType;
import com.kifanle.seedweb.annotation.RequestMapping;
import com.kifanle.seedweb.handler.HandlerSupport;
import com.kifanle.seedweb.util.ext.RestfulUtils;

@Component
@RequestMapping(value="/sub")
public class SubPathTestHandler extends HandlerSupport{
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SubPathTestHandler.class);
	@RequestMapping(value="/test")
	public void testSubPathHtmlView(){
		this.putView("test", "/sub/test ok");
	}
	
	@RequestMapping(value="/test1")
	public void test1(){
		this.putView("test", "/sub/test1 ok");
	}
	
	@RequestMapping(value="/user/{id}/msg/{msg}",resType=ResponseType.JSON)
	public Object testSubPathRestful(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", RestfulUtils.getIntParam("id"));
		map.put("msg", RestfulUtils.getStringParam("msg"));
		return map; 
	}

}
