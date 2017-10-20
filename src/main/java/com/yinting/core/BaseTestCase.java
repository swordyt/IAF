package com.yinting.core;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeSuite;

import com.yinting.core.Http.HttpRequest;
import com.yinting.tools.DataPersistenceFactory;

@ContextConfiguration("classpath:qacontext/applicationContext.xml")
@Transactional
// @Transactional(propagation = Propagation.REQUIRED)
//AbstractTransactionalTestNGSpringContextTests
public class BaseTestCase extends AbstractTransactionalTestNGSpringContextTests {
	@Autowired
	protected HttpRequest driver;
	/**
	 * 获取src/main/resources/properties/inter/xx/filename.properties 中指定的值
	 * */
	public static String properties(String fileName,String key) {
		String path="src/main/resources/properties/inter/"+System.getProperty("ENV")+"/"+fileName;
		Properties prop=new Properties();
		try {
			prop.load(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}
}
