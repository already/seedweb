package com.kifanle.seedweb.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kifanle.seedweb.aj.ResponseType;

/**
 * 1.value plain   格式为“/path1/path2”<br>  
 *     &nbsp;restful 格式为“/path1/{id}/path2/{info}”<br>
 * 2.resType 参考 {@link com.kifanle.seedweb.aj.ResponseType}<br>  
 * 3.steps   定义steps，格式解析参考{@link com.kifanle.seedweb.steps.StepsUtils}<br>       
 * @author zhouqin
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestMapping {
	
	public String value() default "";
	
	public ResponseType resType() default ResponseType.HTML;
	
	public String steps() default "";
	
}
