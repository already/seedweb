package com.kifanle.seedweb.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.stereotype.Component;

import com.kifanle.seedweb.servlet.ResourcesFilter;
import com.kifanle.seedweb.servlet.WebServlet;

/**
 * jetty server
 * @author zhouqin
 *
 */
@Component
public class Servers {
	private Integer port = 8080;
	
	public void setPort(Integer port) {
		this.port = port;
	}	
	
	private String requestEncoding;
	
	private String responseEncoding;

	public void setRequestEncoding(String requestEncoding) {
		this.requestEncoding = requestEncoding;
	}

	public void setResponseEncoding(String responseEncoding) {
		this.responseEncoding = responseEncoding;
	}
	
	public void start() throws Exception{
		WebServlet webServlet = new WebServlet();
		webServlet.setRequestEncoding(requestEncoding);
		webServlet.setResponseEncoding(responseEncoding);
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addFilter(ResourcesFilter.class,"/*",null);
        context.addServlet(new ServletHolder(webServlet), "/*");
        server.start();
        server.join();
	}

}
