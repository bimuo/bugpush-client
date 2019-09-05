package com.bimuo.bugpush.client.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 测试用例
 * @author yuzhantao
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface TestCase {
	/**
	 * 测试用例的名称
	 * @return
	 */
	String name() default "";
	/**
	 * 测试用例编号
	 * @return
	 */
	String code() default "";
	/**
	 * 任务等级
	 * @return
	 */
	int level() default 1;
	/**
	 * 项目编号
	 * @return
	 */
	String projectCode() default "";
	/**
	 * 项目版本
	 * @return
	 */
	String projectVersion() default "";
	/**
	 * bug提交接口网址
	 * @return
	 */
	String url() default "";
	/**
	 * 处理人编号
	 * @return
	 */
	String handlerCode() default "";
	/**
	 * 测试人员编码
	 * @return
	 */
	String testerCode() default "";
	/**
	 * 创建人编号
	 * @return
	 */
	String creatorCode() default "";
	/**
	 * 运行环境
	 * @return
	 */
	String environment() default "";
	
}
