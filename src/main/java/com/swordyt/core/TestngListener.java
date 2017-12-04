package com.swordyt.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

import com.swordyt.core.annotation.Driver;
import com.swordyt.tools.PropertiesTool;

public class TestngListener implements ISuiteListener, ITestListener, IClassListener, IAnnotationTransformer2 {
	public static String[] parameter;

	/**
	 * 加载配置文件
	 */
	public void onStart(ISuite suite) {
		Properties systemPro = System.getProperties();
		Properties userPro = new Properties();
		try {
			if (!systemPro.containsKey("swordyt.qa.env.location")) {
				// userPro.load(new
				// FileInputStream("src/main/resources/config/env.properties"));
				userPro.load(new FileInputStream("src/main/java/config.properties"));
				loadEnv(userPro);
				userPro.load(
						new FileInputStream(PropertiesTool.fillPath((String) userPro.get("swordyt.qa.env.location"))));
				loadEnv(userPro);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Dubbo.scanDubboXml();
		// TestFactory.getSuite().setStartTime(new Date().getTime());
	}

	/**
	 * 将传入的Properties 导入System中
	 */
	private void loadEnv(Properties p) {
		Enumeration keys = p.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			System.setProperty(key, p.getProperty(key));
		}
	}

	public void onFinish(ISuite suite) {
		// TestFactory.getSuite().setEndTime(new Date().getTime());
		// TestFactory.endSuite();

	}

	public void onTestStart(ITestResult result) {
		// TestFactory.getCas().setStartTime(new Date().getTime());

	}

	public void onTestSuccess(ITestResult result) {
		// TestFactory.getCas().setResult(CaseResultType.SUCCESS);
		// TestFactory.getCas().setEndTime(new Date().getTime());
		// TestFactory.endCase();

	}

	public void onTestFailure(ITestResult result) {
		// TestFactory.getCas().setResult(CaseResultType.FAILURE);
		// TestFactory.getCas().setEndTime(new Date().getTime());
		// TestFactory.endCase();

	}

	public void onTestSkipped(ITestResult result) {
		// TestFactory.getCas().setResult(CaseResultType.SKIPPED);
		// TestFactory.getCas().setEndTime(new Date().getTime());
		// TestFactory.endCase();

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TestFactory.getCas().setResult(CaseResultType.FBWSP);
		// TestFactory.getCas().setEndTime(new Date().getTime());
		// TestFactory.endCase();

	}

	public void onStart(ITestContext context) {

	}

	public void onFinish(ITestContext context) {
	}

	public void onBeforeClass(ITestClass testClass) {
		// TestFactory.getCls().setStartTime(new Date().getTime());

	}

	public void onAfterClass(ITestClass testClass) {
		// TestFactory.getCls().setEndTime(new Date().getTime());
		// TestFactory.endClass();
	}

	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		/**
		 * 实现测试却动生效
		 */
		Annotation ann = testMethod.getAnnotation(Driver.class);
		if (ann != null) {
			try {
				DataProviderClass.dataDriver(annotation, testMethod, (Driver) ann);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
