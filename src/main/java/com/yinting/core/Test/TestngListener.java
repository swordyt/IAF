package com.yinting.core.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.testng.IAnnotationTransformer2;
import org.testng.IClassListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.ITestAnnotation;

import com.yinting.core.datadriver.DataDriver;
import com.yinting.core.datadriver.DataProviderClass;
import com.yinting.core.datadriver.Driver;
import com.yinting.core.dubbo.Dubbo;

public class TestngListener implements ISuiteListener, ITestListener, IClassListener, IAnnotationTransformer2 {
	public static final String DATAPROVIDER_DB = "DATAPROVIDER_DB";
	public static final String DATAPROVIDER_EXCEL = "DATAPROVIDER_EXCEL";
	public static final String DATAPROVIDER_XML = "DATAPROVIDER_XML";
	public static String[] parameter;

	public void onStart(ISuite suite) {
		Properties systemPro = System.getProperties();
		Properties userPro = new Properties();
		try {
			if (!systemPro.containsKey("ENV")) {
				userPro.load(new FileInputStream("src/main/resources/config/env.properties"));
				Enumeration keys = userPro.propertyNames();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					System.setProperty(key, userPro.getProperty(key));
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dubbo.scanDubboXml();
		TestFactory.getSuite().setStartTime(new Date().getTime());
	}

	public void onFinish(ISuite suite) {
		TestFactory.getSuite().setEndTime(new Date().getTime());
		TestFactory.endSuite();

	}

	public void onTestStart(ITestResult result) {
		TestFactory.getCas().setStartTime(new Date().getTime());

	}

	public void onTestSuccess(ITestResult result) {
		TestFactory.getCas().setResult(CaseResultType.SUCCESS);
		TestFactory.getCas().setEndTime(new Date().getTime());
		TestFactory.endCase();

	}

	public void onTestFailure(ITestResult result) {
		TestFactory.getCas().setResult(CaseResultType.FAILURE);
		TestFactory.getCas().setEndTime(new Date().getTime());
		TestFactory.endCase();

	}

	public void onTestSkipped(ITestResult result) {
		TestFactory.getCas().setResult(CaseResultType.SKIPPED);
		TestFactory.getCas().setEndTime(new Date().getTime());
		TestFactory.endCase();

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		TestFactory.getCas().setResult(CaseResultType.FBWSP);
		TestFactory.getCas().setEndTime(new Date().getTime());
		TestFactory.endCase();

	}

	public void onStart(ITestContext context) {

	}

	public void onFinish(ITestContext context) {
	}

	public void onBeforeClass(ITestClass testClass) {
		TestFactory.getCls().setStartTime(new Date().getTime());

	}

	public void onAfterClass(ITestClass testClass) {
		TestFactory.getCls().setEndTime(new Date().getTime());
		TestFactory.endClass();
	}

	// 实现数据驱动分类，并带着参数
	private void dataDriver(ITestAnnotation annotation, Method method, Driver driver) {
		annotation.setDataProviderClass(DataProviderClass.class);
		DataDriver.parameteres.put(DataDriver.md5(method), driver.parameter());
		switch (driver.type().getType()) {
		case 0: // excel
			annotation.setDataProvider(DATAPROVIDER_EXCEL);
			break;
		case 1: // xml
			annotation.setDataProvider(DATAPROVIDER_XML);
			break;
		case 2:// db
			annotation.setDataProvider(DATAPROVIDER_DB);
			break;
		default:
			annotation.setDataProvider(DATAPROVIDER_EXCEL);
		}
	}

	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		Annotation ann = testMethod.getAnnotation(Driver.class);
		if (ann != null) {
			dataDriver(annotation, testMethod, (Driver) ann);
		}

	}

	public void transform(IConfigurationAnnotation annotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		// TODO Auto-generated method stub

	}

	public void transform(IDataProviderAnnotation annotation, Method method) {
		// TODO Auto-generated method stub

	}

	public void transform(IFactoryAnnotation annotation, Method method) {
		// TODO Auto-generated method stub

	}
}
