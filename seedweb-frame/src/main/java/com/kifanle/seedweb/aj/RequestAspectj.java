package com.kifanle.seedweb.aj;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.kifanle.seedweb.annotation.RequestMapping;
import com.kifanle.seedweb.context.MethodContext;
import com.kifanle.seedweb.context.WebApplicationContext;
import com.kifanle.seedweb.steps.StepsUtils;
import com.kifanle.seedweb.util.core.WebContextUtils;

/**
 * 
 * @author zhouqin
 *
 */
@Component
@Aspect
public class RequestAspectj {
	@Around("@annotation(requestMapping)")
    public void checkIp(ProceedingJoinPoint joinPoint,RequestMapping requestMapping) throws Throwable  {
		try {
			if(StringUtils.isNotBlank(requestMapping.steps())){
				Map<String,LinkedHashMap<String,String>> steps = StepsUtils.getStepsMap(requestMapping.steps());
				for(Entry<String,LinkedHashMap<String,String>> entry: steps.entrySet()){
					String stepKey = entry.getKey();
					Map<String,String> values = entry.getValue();
					WebContextUtils.putExt(values);
					MethodContext mc =WebApplicationContext.stepMappings.get(stepKey);
					ReflectionUtils.invokeMethod(mc.getProxyMethod(), mc.getProxyObject());
				}
			}
			
			requestMapping.resType().doResponse(joinPoint.proceed());
		} catch (Throwable e) {
			WebContextUtils.clear();
		}
	}
}
