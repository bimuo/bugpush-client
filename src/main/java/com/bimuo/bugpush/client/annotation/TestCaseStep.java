package com.bimuo.bugpush.client.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 测试用例步骤
 * @author yuzhantao
 *
 */
@Retention(RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface TestCaseStep {
	String value() default "";
}
