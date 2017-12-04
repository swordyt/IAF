package com.swordyt.mybatis;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.support.PersistenceExceptionTranslator;

public class RuleSqlSessionTemplate extends SqlSessionTemplate {

	public RuleSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
			PersistenceExceptionTranslator exceptionTranslator) {
		super(sqlSessionFactory, executorType, exceptionTranslator);
		// TODO Auto-generated constructor stub
	}

	public RuleSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		super(sqlSessionFactory, executorType);
		// TODO Auto-generated constructor stub
	}

	public RuleSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	@Override
	public DataSource ruleTransform(Object proxy, Method method, Object[] args) {
		return null;
	}

}
