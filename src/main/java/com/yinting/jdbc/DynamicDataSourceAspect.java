package com.yinting.jdbc;
import java.text.MessageFormat;
import java.util.Properties;

import org.aspectj.lang.JoinPoint;

import com.yinting.core.Log;
import com.zaxxer.hikari.HikariDataSource;
/**
 * 用于动态切入，切换数据源。
 * */
public class DynamicDataSourceAspect {
	public void intercept(JoinPoint point) throws Exception {
		String[] parameters = null;
		if (!System.getProperty("ENV").equalsIgnoreCase("dev")) {
			return;
		}

		String parameter = (String) point.getArgs()[0];
		String dbName = parameter.split("_")[0].toLowerCase();
		if (DataSourceContextHolder.getDbType() !=null&&DataSourceContextHolder.getDbType().equalsIgnoreCase(dbName)) {
			return;
		}
		Log.log("切换数据源为：" + dbName);
		DataSourceContextHolder.setDbType(dbName);
		Properties pro = System.getProperties();
		parameters = new String[] { dbName }; // 封装参数
		String url = MessageFormat.format(pro.getProperty("maiziyun.dev.mysql.url"), parameters);
		String username = MessageFormat.format(pro.getProperty("maiziyun.dev.mysql.username"), parameters);
		String password = MessageFormat.format(pro.getProperty("maiziyun.dev.mysql.password"), parameters);
		DbHandller handle = (DbHandller) point.getThis();
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setConnectionTestQuery("SELECT 1");
		dataSource.setConnectionTimeout(30000);
		dataSource.setIdleTimeout(60000);
		dataSource.setMaxLifetime(1800000);
		dataSource.setMaximumPoolSize(10);
		dataSource.setMinimumIdle(1);
		handle.setDataSource(dataSource);
	}
}
