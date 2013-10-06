package com.kifanle.seedweb.context;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;

/**
 * 记录spring代理情况下proxy方法信息
 * @author zhouqin
 *
 */
public class MethodContext {
	private String path;

	private String clzName;
	
	private Method proxyMethod;
	
	private Object proxyObject;
	
	private String methodName;
	
	private LinkedHashSet<String> restParams = new LinkedHashSet<String>();
	
	public String getPath() {
		return path;
	}

	public MethodContext setPath(String path) {
		this.path = path;
		return this;
	}
	
	public String getClzName() {
		return clzName;
	}

	public MethodContext setClzName(String clzName) {
		this.clzName = clzName;
		return this;
	}
	
	public Method getProxyMethod() {
		return proxyMethod;
	}

	public MethodContext setProxyMethod(Method proxyMethod) {
		this.proxyMethod = proxyMethod;
		return this;
	}

	public Object getProxyObject() {
		return proxyObject;
	}

	public MethodContext setProxyObject(Object proxyObject) {
		this.proxyObject = proxyObject;
		return this;
	}

	public String getMethodName() {
		return methodName;
	}

	public MethodContext setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	public LinkedHashSet<String> getRestParams() {
		return restParams;
	}

	public MethodContext setRestParams(LinkedHashSet<String> restParams) {
		this.restParams = restParams;
		return this;
	}
	
	private String steps;
	public String getSteps() {
		return steps;
	}

	public MethodContext setSteps(String steps) {
		this.steps = steps;
		return this;
	}
	
	
}

