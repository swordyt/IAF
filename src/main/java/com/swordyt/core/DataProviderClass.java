package com.swordyt.core;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.DataProvider;
import org.testng.annotations.ITestAnnotation;

import com.swordyt.core.annotation.Driver;
import com.swordyt.tools.PropertiesTool;

public class DataProviderClass {
	private static final ThreadLocal<String> type = new ThreadLocal<String>();
	private static Properties pro = null;
	Map<String, String> mapping = null;

	@DataProvider(name = "DataProviderClass")
	public DataDriver dataDriver(Method method)
			throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (mapping == null) {
			String dataDriven = PropertiesTool.getProperty("swordyt.data.dataDriven");
			if (dataDriven == null) {
				throw new RuntimeException("DATADRIVER未配置。");
			}
			String[] keyValues = dataDriven.split(",");
			mapping = new HashMap<String, String>();
			for (String keyValue : keyValues) {
				String[] map = keyValue.split(":");
				if (map.length != 2) {
					throw new RuntimeException("DATADRIVER配置有误，请检查配置:" + keyValue);
				}
				mapping.put(map[0], map[1]);
			}
		}

		String value = mapping.get(type.get());
		if (value == null) {
			throw new RuntimeException("数据驱动类未配置：" + type.get());
		}

		Class[] parameter = new Class[] { Method.class };
		Class cls = Class.forName(value); // 获取类
		Constructor con = cls.getConstructor(parameter);// 获取构造器
		Object[] arg = new Object[] { method };
		DataDriver dd = (DataDriver) con.newInstance(arg);// 构造类
		return dd;
	}

	// 实现数据驱动分发，并带着参数
	public static void dataDriver(ITestAnnotation annotation, Method method, Driver driver)
			throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		annotation.setDataProviderClass(DataProviderClass.class);
		annotation.setDataProvider("DataProviderClass");
		DataDriver.parameteres.put(DataDriver.md5(method), driver.parameter());
		type.set(driver.type());
	}
}
