package com.kifanle.seedweb.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kifanle.seedweb.context.WebApplicationContext;
import com.kifanle.seedweb.util.core.StringUtil;

/**
 * jetty 服务器启动类 参数配置 。ex: -port:9998
 * @author zhouqin
 *
 */
public class WebServer {
	private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    public static void main(String[] args) throws Exception{
    	Map<String,String> pMap = StringUtil.getParamMap(args);
    	int port = StringUtil.getInt(pMap, "port", 8080);
    	String requestEncoding = StringUtil.getString(pMap, "requestEncoding", "UTF-8");
    	String responseEncoding = StringUtil.getString(pMap, "responseEncoding", "UTF-8");
		try {
			Servers servers = (Servers) WebApplicationContext.ac.getBean("servers");
			servers.setPort(port);
			servers.setRequestEncoding(requestEncoding);
			servers.setResponseEncoding(responseEncoding);
			servers.start();
		} catch (Exception e) {
			log.error("[Jetty server start error.][error:{}]", new Object[]{e.toString()});
			System.exit(-1);
		}
    }
    
    
}
