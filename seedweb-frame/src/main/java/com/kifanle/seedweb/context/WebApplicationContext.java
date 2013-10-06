package com.kifanle.seedweb.context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kifanle.seedweb.annotation.RequestMapping;
import com.kifanle.seedweb.annotation.Step;
import com.kifanle.seedweb.steps.StepsUtils;
import com.kifanle.seedweb.util.core.PathUtils;
import com.kifanle.seedweb.util.core.StringUtil;

/**
 * @author zhouqin
 */
public final class WebApplicationContext {
	private static final Logger log = LoggerFactory.getLogger(WebApplicationContext.class);
	
	public static ApplicationContext ac = new ClassPathXmlApplicationContext("applications.xml");
	
	public static Map<String,MethodContext> mcMappings = new HashMap<String,MethodContext>();

	public static Map<String,MethodContext> mcRexMappings = new HashMap<String,MethodContext>();
	
	public static Map<String,MethodContext> stepMappings = new HashMap<String,MethodContext>();
	
    public volatile boolean inited = false;
    
    public static WebApplicationContext getInstance(){
        return Nested.instance;
    }

    static class Nested{
        private static WebApplicationContext instance = new WebApplicationContext();
    }

    private WebApplicationContext(){
       
    } 
    
    public boolean init(){
    	try{
        	//steps
    		Map<String,Object> ms = ac.getBeansWithAnnotation(Step.class);
        	Iterator<Object> it = ms.values().iterator();
        	while(it.hasNext()){
        		Object proxyObject = it.next();
        		Class<?> proxyClz = proxyObject.getClass();
        		Class<?> c = AopUtils.getTargetClass(proxyObject);
    			for (final Method m : c.getMethods()) {
    				Step step = m.getAnnotation(Step.class);
    				if (null != step) {
    					log.info("init"+m.getName());
    					String s = step.value();
    					if(StringUtils.isNotBlank(s)){
    						MethodContext mc = new MethodContext().setClzName(StringUtil.tramsFirstChar2LowerCase(c.getSimpleName()))
    								.setMethodName(m.getName()).setProxyMethod(proxyClz.getMethod(m.getName(), proxyClz.getClasses()))
    								.setProxyObject(proxyObject);
							if(WebApplicationContext.stepMappings.containsKey(s)){
								log.error(s+" step is existed。");
								return false;
							}
							WebApplicationContext.stepMappings.put(s, 
									mc);
    					}
    				}
    			}
        	}
    		
        	ms = ac.getBeansWithAnnotation(RequestMapping.class);
        	it = ms.values().iterator();
        	while(it.hasNext()){
        		Object proxyObject = it.next();
        		Class<?> proxyClz = proxyObject.getClass();
        		Class<?> c = AopUtils.getTargetClass(proxyObject);
        		System.out.println("初始化 :"+proxyClz.getName());
        		String parentUrl = c.getAnnotation(RequestMapping.class).value();
    			for (final Method m : c.getMethods()) {
    				RequestMapping mmap = m.getAnnotation(RequestMapping.class);
    				if (null != mmap) {
    					log.info("init"+m.getName());
    					String s = parentUrl + mmap.value();
    					if(StringUtils.isNotBlank(s)){
    						MethodContext mc = new MethodContext().setClzName(StringUtil.tramsFirstChar2LowerCase(c.getSimpleName()))
    								.setMethodName(m.getName()).setProxyMethod(proxyClz.getMethod(m.getName(), proxyClz.getClasses()))
    								.setProxyObject(proxyObject);
    						if(PathUtils.isAcceptPath(s)){
        						//验证step是否已初始化
    							String steps = mmap.steps();
    							if(StringUtils.isNotBlank(steps)){
    								for(String step:StepsUtils.getStepsMap(steps).keySet()){
    									if(!WebApplicationContext.stepMappings.containsKey(step)){
    										log.error("[anotation step is existed][method:{}][step:{}]",new Object[]{mc.getMethodName(),step});
    										return false;
    									}
    								}
    							}
    							
    							//处理rest请求
    							LinkedHashSet<String> set = PathUtils.getRestParamsMeta(s);
    							if(set.size()>0){
    								String pRegx = PathUtils.path2Regx(s);
    								mc.setPath(pRegx).setRestParams(set);
    								if(WebApplicationContext.mcRexMappings.containsKey(pRegx)){
    									log.error(s+" path is existed。");
    									return false;
    								}
    								WebApplicationContext.mcRexMappings.put(pRegx, 
    										mc);	
    							}else{
    								//处理普通请求
    								mc.setPath(s);
    								if(WebApplicationContext.mcMappings.containsKey(s)){
    									log.error(s+" path is existed。");
    									return false;
    								}
    								WebApplicationContext.mcMappings.put(s, 
    										mc);
    							}
    						}
    					}
    				}
    			}
        	}
        	

    	}catch(Exception e){
    		log.error("初始化handler失败", e);
    		return false;
    	}
    	return true;
    }
    
    public void destory(){
    	
    }
}
