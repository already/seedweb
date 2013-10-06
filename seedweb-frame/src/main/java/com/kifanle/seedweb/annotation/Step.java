package com.kifanle.seedweb.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * step 注解，使用方法需要在类上注解 和相关方法注解。例子参考{@link com.kifanle.seedweb.steps.DefaultSteps}}
 * @author zhouqin
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Step {
	public String value() default "";
}
