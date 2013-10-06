package com.kifanle.seedweb.steps;

import org.springframework.stereotype.Component;

import com.kifanle.seedweb.annotation.Step;
import com.kifanle.seedweb.handler.DefaultHandler;
import com.kifanle.seedweb.util.core.WebContextUtils;
import com.kifanle.seedweb.util.ext.IPMatchHelper;
import com.kifanle.seedweb.util.ext.WebUtils;

/**
 * 默认例子，ip过滤
 * @author zhouqin
 *
 */
@Component
@Step
public class DefaultSteps {
	@Step(value="ipFilter")
	public void ipFilter(){
		if(!IPMatchHelper.isMatchedIp(WebContextUtils.getExt("ips").toString(), WebUtils.getRemoteIP())){
			//通过forward 移交控制权，直接跳页面。
			WebUtils.forward("/404", DefaultHandler.MSG,"ip地址不匹配。请联系管理员");
		}
	}
	
}
