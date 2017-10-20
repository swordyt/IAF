package com.yinting.jdbc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.ReflectionUtils;

import com.zaxxer.hikari.HikariDataSource;

@SuppressWarnings("deprecation")
public class DbHandller extends SqlMapClientDaoSupport {
	/**
	 * 分页查询时，对应查询数据库记录总数的select名称规则
	 */
	private static final String COUNT_STATEMENT_NAME_SUFFIX = "@count";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bill99.seashell.orm.QueryDao#queryForObject(java.lang.String,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <X> X queryForObject(String statementName, Object parameterObject) {
		if (null == parameterObject) {
			return (X) getSqlMapClientTemplate().queryForObject(statementName);
		}
		return (X) getSqlMapClientTemplate().queryForObject(statementName, parameterObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bill99.seashell.orm.QueryDao#queryForList(java.lang.String,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> queryForList(String statementName, Object parameterObject) {
		return getSqlMapClientTemplate().queryForList(statementName, parameterObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bill99.seashell.orm.QueryDao#queryForList(java.lang.String,
	 * com.bill99.seashell.orm.pagination.Page, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> queryForList(String statementName, Page page, Object parameterObject) {
		if (page == null) {
			return queryForList(statementName, parameterObject);
		} else {
			Number totalRecord = null;
			try {
				totalRecord = (Number) getSqlMapClientTemplate()
						.queryForObject(statementName + COUNT_STATEMENT_NAME_SUFFIX, toParameterMap(parameterObject));
				page.setTotalRecord(totalRecord.intValue());
			} catch (Exception e) {
				logger.error("No count select [" + statementName + COUNT_STATEMENT_NAME_SUFFIX + "]", e);
			}
			if (totalRecord != null && totalRecord.longValue() > 0) {
				return getSqlMapClientTemplate().queryForList(statementName, toParameterMap(parameterObject, page));
			}

		}
		return Collections.emptyList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static Map toParameterMap(Object parameter, Page p) {
		Map map = toParameterMap(parameter);
		map.put("pageSize", p.getPageSize());
		map.put("startRow", p.getStartRow());
		map.put("endRow", p.getEndRow());
		return map;
	}

	@SuppressWarnings("rawtypes")
	protected static Map toParameterMap(Object parameter) {
		if (parameter instanceof Map) {
			return (Map) parameter;
		} else {
			try {
				return PropertyUtils.describe(parameter);
			} catch (Exception e) {
				ReflectionUtils.handleReflectionException(e);
				return null;
			}
		}
	}

	public int executeUpdate(String statementName, Object parameterObject) {
		return getSqlMapClientTemplate().update(statementName, parameterObject);
	}

	public Object executeInsert(String statementName, Object parameterObject) {
		return getSqlMapClientTemplate().insert(statementName, parameterObject);
	}

	public int executeDelete(String statementName, Object parameterObject) {
		return getSqlMapClientTemplate().delete(statementName, parameterObject);
	}

	public <X, Y> Map<X, Y> queryForMap(String statementName, Object parameterObject, String keyMark,
			String valueMark) {
		return getSqlMapClientTemplate().queryForMap(statementName, parameterObject, keyMark, valueMark);
	}

	public <X, Y> Map<X, Y> queryForMap(String statementName, Object parameterObject, String keyMark) {
		return getSqlMapClientTemplate().queryForMap(statementName, parameterObject, keyMark);
	}

	public void setDataSource(HikariDataSource dataSource) {
		super.setDataSource(dataSource);
	}
}
