package com.bimuo.bugpush.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.bimuo.bugpush.client.annotation.TestCase;
import com.bimuo.bugpush.client.annotation.TestCaseStep;

public class JUnitListener extends RunListener {
	private final static Logger logger = LogManager.getLogger(JUnitListener.class);
	private long startTime;
	private long endTime;
	private StringBuffer sb = new StringBuffer();
	private Description description;

	private void sendBugMessage(String url,String projectCode, String projectVersion, int level, String creatorCode,
			String handlerCode, String environment, String testName, String testContent) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> params = new HashMap<>();
		params.put("projectCode", projectCode);
		params.put("projectVersion", projectVersion);
		params.put("taskName", testName);
		params.put("taskContent", testContent);
		params.put("taskLevel", level);
		params.put("creatorCode", creatorCode);
		params.put("handlerCode", handlerCode);
		params.put("environment", environment);

		ResponseEntity<String> ret = restTemplate.postForEntity(url, params,
				String.class);

		logger.info("提交到bug系统的数据:{}", JSON.toJSONString(ret.getBody()));
	}

	/**
	 * 所有测试开始
	 */
	@Override
	public void testRunStarted(Description description) throws Exception {
		startTime = new Date().getTime();
		this.description = description;

		sb.append(description.getDisplayName() + "用例启动测试\\r\\n");
//        System.out.println("Test Run Started!");
//        System.out.println("The Test Class is " + description.getClassName() + ". Number of Test Case is " + description.testCount());
//        System.out.println("===================================================================================");
	}

	/**
	 * 所有测试完成
	 */
	@Override
	public void testRunFinished(Result result) throws Exception {
		endTime = new Date().getTime();
		sb.append("用例测试完成\r\n");
		sb.append("执行时间:" + (endTime - startTime) / 1000 + "秒\r\n");

		TestCase userCase = this.description.getAnnotation(TestCase.class);
		String url,projectCode, projectVersion, creatorCode,handlerCode, environment, testName;
		int level;
		if (userCase != null) {
			url=userCase.url();
			projectCode = userCase.projectCode();
			projectVersion = userCase.projectVersion();
			creatorCode = userCase.creatorCode();
			handlerCode = userCase.handlerCode();
			environment = userCase.environment();
			testName = userCase.name();
			level = userCase.level();
			
			sendBugMessage(url,projectCode,  projectVersion,  level,  creatorCode,
					 handlerCode,  environment,  testName, sb.toString());
		}
		
		sb.setLength(0);
	}

	@Override
	public void testStarted(Description description) throws Exception {
		System.out.println("Test Method Named " + description.getMethodName() + " Started!");
		sb.append("开始测试方法:" + description.getMethodName());
	}

	@Override
	public void testFinished(Description description) throws Exception {
		System.out.println("Test Method Named " + description.getMethodName() + " Ended!");
		System.out.println("===================================================================================");
	}

	/**
	 * 测试异常触发
	 */
	@Override
	public void testFailure(Failure failure) throws Exception {
		TestCaseStep step = failure.getDescription().getAnnotation(TestCaseStep.class);
		if (step != null && step.value()!=null && !step.value().equals("")) {
			sb.append("Step异常:" + step.value() + "\r\n");
		}
		
		System.out.println("Test Method Named " + failure.getDescription().getMethodName() + " Failed!");
		System.out.println("Failure Cause is : " + failure.getException());
		sb.append("异常原因:" + failure.getException().getMessage() + "\r\n");
	}

	/**
	 * 断言为false触发
	 */
	@Override
	public void testAssumptionFailure(Failure failure) {
		System.out.println("Test Method Named " + failure.getDescription().getMethodName() + " Failed for Assumption!");
		sb.append("测试未通过:" + failure.getException().getMessage() + "\r\n");
	}

	@Override
	public void testIgnored(Description description) throws Exception {
		super.testIgnored(description);
	}
}
