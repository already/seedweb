package com.kifanle.seedweb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSeedWebServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(WebServlet.class);
	private String requestEncoding;
	private String responseEncoding;

	public void setRequestEncoding(String requestEncoding) {
		this.requestEncoding = requestEncoding;
	}

	public void setResponseEncoding(String responseEncoding) {
		this.responseEncoding = responseEncoding;
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestEncoding = StringUtils.isNotBlank(requestEncoding)?requestEncoding:"UTF-8";
		request.setCharacterEncoding(requestEncoding);
		responseEncoding = StringUtils.isNotBlank(responseEncoding)?responseEncoding:"UTF-8";
		response.setCharacterEncoding(responseEncoding);
		
		handRequest(request,response);
	}

	public abstract void handRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
