package com.kifanle.seedweb.velocity;

import org.apache.velocity.VelocityContext;

import com.kifanle.seedweb.util.core.StringUtil;

/**
 * 如果在vm模板里使用工具类。可以在构造函数注入。
 * @author zhouqin
 *
 */
public class SeedwebVelocityContext extends VelocityContext {
	private static StringUtil stringUtil = new StringUtil();
    public SeedwebVelocityContext()
    {
        super();
        this.put("stringUtil", stringUtil);
    }
}
