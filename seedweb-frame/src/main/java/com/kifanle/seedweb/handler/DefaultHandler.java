package com.kifanle.seedweb.handler;

import org.springframework.stereotype.Component;

import com.kifanle.seedweb.aj.ResponseType;
import com.kifanle.seedweb.annotation.RequestMapping;

@Component
@RequestMapping
public class DefaultHandler extends HandlerSupport{
	public final static String MSG = "message";
	public final static String CODE = "code";
	@RequestMapping(value="/404",resType=ResponseType.HTML)
	public void _404(){
		putView(CODE,404);
		putView(MSG,null!=getView(MSG)?getView(MSG):"查不到此页面");
	}
	@RequestMapping(value="/index")
	public void _index(){

	}
}
