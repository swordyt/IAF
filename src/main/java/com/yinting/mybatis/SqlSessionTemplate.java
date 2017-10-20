package com.yinting.mybatis;

/**
 *    Copyright 2010-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.ibatis.reflection.ExceptionUtil.unwrapThrowable;
import static org.mybatis.spring.SqlSessionUtils.closeSqlSession;
import static org.mybatis.spring.SqlSessionUtils.getSqlSession;
import static org.mybatis.spring.SqlSessionUtils.isSqlSessionTransactional;
import static org.springframework.util.Assert.notNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import com.yinting.jdbc.DbHandller;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Thread safe, Spring managed, {@code SqlSession} that works with Spring
 * transaction management to ensure that that the actual SqlSession used is the
 * one associated with the current Spring transaction. In addition, it manages
 * the session life-cycle, including closing, committing or rolling back the
 * session as necessary based on the Spring transaction configuration.
 * <p>
 * The template needs a SqlSessionFactory to create SqlSessions, passed as a
 * constructor argument. It also can be constructed indicating the executor type
 * to be used, if not, the default executor type, defined in the session factory
 * will be used.
 * <p>
 * This template converts MyBatis PersistenceExceptions into unchecked
 * DataAccessExceptions, using, by default, a
 * {@code MyBatisExceptionTranslator}.
 * <p>
 * Because SqlSessionTemplate is thread safe, a single instance can be shared by
 * all DAOs; there should also be a small memory savings by doing this. This
 * pattern can be used in Spring configuration files as follows:
 *
 * <pre class="code">
 * {@code
 * <bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
 *   <constructor-arg ref="sqlSessionFactory" />
 * </bean>
 * }
 * </pre>
 *
 * @author Putthibong Boonbong
 * @author Hunter Presnall
 * @author Eduardo Macarron
 *
 * @see SqlSessionFactory
 * @see MyBatisExceptionTranslator
 */
public class SqlSessionTemplate extends org.mybatis.spring.SqlSessionTemplate {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();//保存当前线程上一次切换DataSource关键字
	private final SqlSessionFactory sqlSessionFactory;

	private final ExecutorType executorType;

	private final SqlSession sqlSessionProxy;

	private final PersistenceExceptionTranslator exceptionTranslator;
	private Map<Object, SqlSessionFactoryBean> targetSqlSessionFactory;

	public void setTargetSqlSessionFactory(Map<Object, SqlSessionFactoryBean> targetSqlSessionFactory) {
		this.targetSqlSessionFactory = targetSqlSessionFactory;
	}

	/**
	 * Constructs a Spring managed SqlSession with the {@code SqlSessionFactory}
	 * provided as an argument.
	 *
	 * @param sqlSessionFactory
	 */
	public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
	}

	/**
	 * Constructs a Spring managed SqlSession with the {@code SqlSessionFactory}
	 * provided as an argument and the given {@code ExecutorType}
	 * {@code ExecutorType} cannot be changed once the
	 * {@code SqlSessionTemplate} is constructed.
	 *
	 * @param sqlSessionFactory
	 * @param executorType
	 */
	public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		this(sqlSessionFactory, executorType, new MyBatisExceptionTranslator(
				sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true));
	}

	/**
	 * Constructs a Spring managed {@code SqlSession} with the given
	 * {@code SqlSessionFactory} and {@code ExecutorType}. A custom
	 * {@code SQLExceptionTranslator} can be provided as an argument so any
	 * {@code PersistenceException} thrown by MyBatis can be custom translated
	 * to a {@code RuntimeException} The {@code SQLExceptionTranslator} can also
	 * be null and thus no exception translation will be done and MyBatis
	 * exceptions will be thrown
	 *
	 * @param sqlSessionFactory
	 * @param executorType
	 * @param exceptionTranslator
	 */
	public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
			PersistenceExceptionTranslator exceptionTranslator) {
		super(sqlSessionFactory, executorType, exceptionTranslator);
		notNull(sqlSessionFactory, "Property 'sqlSessionFactory' is required");
		notNull(executorType, "Property 'executorType' is required");

		this.sqlSessionFactory = sqlSessionFactory;
		this.executorType = executorType;
		this.exceptionTranslator = exceptionTranslator;
		this.sqlSessionProxy = (SqlSession) newProxyInstance(SqlSessionFactory.class.getClassLoader(),
				new Class[] { SqlSession.class }, new SqlSessionInterceptor());
	}

	private DataSource newDatasource(String url, String username, String password) {
		HikariDataSource dataSource = null;
		dataSource = new HikariDataSource();
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
		return dataSource;
	}

	private DataSource ruleTransform(String classInfo) {
		String env = System.getProperty("ENV");
		String dataSourceKey = SqlSessionContextHolder.getDataSourceKey();

		if (env.equals("dev")) {
			Properties pro = new Properties();
			String[] claNames = classInfo.split("\\.");
			String claName = claNames[claNames.length - 2];// 类名
			String key = claName.split("_")[0];// 获取关键字

			if (dataSourceKey != null) {//如果dataSourceKey不为空直接使用作为key值
				key = dataSourceKey;
			} else {
				try {// key.properties 中存在该key对应的value时使用配置，不存在使用key本身
					pro.load(new FileInputStream(
							new File("src/main/resources/config/dev/key.properties")));
					String keyValue = pro.getProperty(key + ".key");
					if (keyValue != null) {
						key = keyValue;
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if(contextHolder.get()==null||!contextHolder.get().equals(key)){
				try {
					pro.load(new FileInputStream("src/main/resources/config/dev//jdbc.properties"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String url = MessageFormat.format(pro.getProperty("hyper." + env + ".mysql.url"), key);
				String username = MessageFormat.format(pro.getProperty("hyper." + env + ".mysql.username"), key);
				String password = MessageFormat.format(pro.getProperty("hyper." + env + ".mysql.password"), key);
				contextHolder.set(key);
				return newDatasource(url, username, password);
			}
		}
		return null;
	}

	public SqlSessionFactory getSqlSessionFactory(String classInfo) {
		SqlSessionFactoryBean sqlSessionFactoryBean = this.targetSqlSessionFactory.get(System.getProperty("ENV"));// 获取SqlSessionFactoryBean
		DataSource dataSource = ruleTransform(classInfo);
		if (dataSource != null) {
			sqlSessionFactoryBean.setDataSource(dataSource);
		}

		SqlSessionFactory targetSqlSessionFactory = null;
		try {
			targetSqlSessionFactory = sqlSessionFactoryBean.getSqlSessionFactory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (targetSqlSessionFactory != null) {
			return targetSqlSessionFactory;
		} else if (this.sqlSessionFactory != null) {
			return this.sqlSessionFactory;
		}
		throw new IllegalArgumentException("sqlSessionFactory or targetSqlSessionFactory must set one at least");
	}

	public ExecutorType getExecutorType() {
		return this.executorType;
	}

	public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
		return this.exceptionTranslator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T selectOne(String statement) {
		return this.sqlSessionProxy.<T>selectOne(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T selectOne(String statement, Object parameter) {
		return this.sqlSessionProxy.<T>selectOne(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, mapKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, parameter, mapKey);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, parameter, mapKey, rowBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Cursor<T> selectCursor(String statement) {
		return this.sqlSessionProxy.selectCursor(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter) {
		return this.sqlSessionProxy.selectCursor(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList(String statement) {
		return this.sqlSessionProxy.<E>selectList(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		return this.sqlSessionProxy.<E>selectList(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionProxy.<E>selectList(statement, parameter, rowBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select(String statement, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select(String statement, Object parameter, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert(String statement) {
		return this.sqlSessionProxy.insert(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert(String statement, Object parameter) {
		return this.sqlSessionProxy.insert(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(String statement) {
		return this.sqlSessionProxy.update(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(String statement, Object parameter) {
		return this.sqlSessionProxy.update(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(String statement) {
		return this.sqlSessionProxy.delete(statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete(String statement, Object parameter) {
		return this.sqlSessionProxy.delete(statement, parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getMapper(Class<T> type) {
		return getConfiguration().getMapper(type, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit(boolean force) {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback(boolean force) {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		throw new UnsupportedOperationException("Manual close is not allowed over a Spring managed SqlSession");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearCache() {
		this.sqlSessionProxy.clearCache();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public Configuration getConfiguration() {
		return this.sqlSessionFactory.getConfiguration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection getConnection() {
		return this.sqlSessionProxy.getConnection();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.2
	 *
	 */
	@Override
	public List<BatchResult> flushStatements() {
		return this.sqlSessionProxy.flushStatements();
	}

	/**
	 * Allow gently dispose bean:
	 * 
	 * <pre>
	 * {@code
	 *
	 * <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
	 *  <constructor-arg index="0" ref="sqlSessionFactory" />
	 * </bean>
	 * }
	 * </pre>
	 *
	 * The implementation of {@link DisposableBean} forces spring context to use
	 * {@link DisposableBean#destroy()} method instead of
	 * {@link SqlSessionTemplate#close()} to shutdown gently.
	 *
	 * @see SqlSessionTemplate#close()
	 * @see org.springframework.beans.factory.support.DisposableBeanAdapter#inferDestroyMethodIfNecessary
	 * @see org.springframework.beans.factory.support.DisposableBeanAdapter#CLOSE_METHOD_NAME
	 */
	@Override
	public void destroy() throws Exception {
		// This method forces spring disposer to avoid call of
		// SqlSessionTemplate.close() which gives UnsupportedOperationException
	}

	/**
	 * Proxy needed to route MyBatis method calls to the proper SqlSession got
	 * from Spring's Transaction Manager It also unwraps exceptions thrown by
	 * {@code Method#invoke(Object, Object...)} to pass a
	 * {@code PersistenceException} to the
	 * {@code PersistenceExceptionTranslator}.
	 */
	private class SqlSessionInterceptor implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			SqlSession sqlSession = getSqlSession(SqlSessionTemplate.this.getSqlSessionFactory(args[0].toString()),
					SqlSessionTemplate.this.executorType, SqlSessionTemplate.this.exceptionTranslator);
			try {
				Object result = method.invoke(sqlSession, args);
				if (!isSqlSessionTransactional(sqlSession, SqlSessionTemplate.this.sqlSessionFactory)) {
					// force commit even on non-dirty sessions because some
					// databases require
					// a commit/rollback before calling close()
					sqlSession.commit(true);
				}
				return result;
			} catch (Throwable t) {
				Throwable unwrapped = unwrapThrowable(t);
				if (SqlSessionTemplate.this.exceptionTranslator != null && unwrapped instanceof PersistenceException) {
					// release the connection to avoid a deadlock if the
					// translator is no loaded. See issue #22
					closeSqlSession(sqlSession, SqlSessionTemplate.this.sqlSessionFactory);
					sqlSession = null;
					Throwable translated = SqlSessionTemplate.this.exceptionTranslator
							.translateExceptionIfPossible((PersistenceException) unwrapped);
					if (translated != null) {
						unwrapped = translated;
					}
				}
				throw unwrapped;
			} finally {
				if (sqlSession != null) {
					closeSqlSession(sqlSession, SqlSessionTemplate.this.sqlSessionFactory);
				}
			}
		}
	}

}
