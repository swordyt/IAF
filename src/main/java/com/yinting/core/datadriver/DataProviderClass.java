package com.yinting.core.datadriver;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import com.yinting.core.Test.TestngListener;

public class DataProviderClass {
	@DataProvider(name = TestngListener.DATAPROVIDER_DB)
	public DataDriver db(Method method) {
		return new DbDriver(method);
	}

	@DataProvider(name = TestngListener.DATAPROVIDER_EXCEL)
	public DataDriver excel(Method method) {
		return new ExcelDriver(method);
	}

	@DataProvider(name = TestngListener.DATAPROVIDER_XML)
	public DataDriver xml(Method method) {
		return new XmlDriver(method);
	}
}
