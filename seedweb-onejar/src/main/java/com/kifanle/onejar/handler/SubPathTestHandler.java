package com.kifanle.onejar.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kifanle.seedweb.annotation.RequestMapping;
import com.kifanle.seedweb.handler.HandlerSupport;

@Component
@RequestMapping(value="/sub")
public class SubPathTestHandler extends HandlerSupport{
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SubPathTestHandler.class);
	
	@RequestMapping(value="/test")
	public void testSubPathHtmlView(){
		this.putView("test", "/sub/test ok");
	}
	
}
