package com.yinting.test;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import org.mybatis.spring.SqlSessionTemplate;
//import com.nonobank.dao.cif_user_infoMapper;
//import com.nonobank.dao.pcs_product_fof_nav_infoMapper;
//import com.nonobank.domain.cif_user_info;
import com.yinting.core.BaseTestCase;
import com.yinting.jdbc.DataSourceContextHolder;
import com.yinting.mybatis.SqlSessionContextHolder;

public class Test4 extends BaseTestCase {
//	@Resource
//	pcs_product_fof_nav_infoMapper nav;
//	@Resource
//	cif_user_infoMapper cif_dev;
//	@Resource
//	SqlSession sqlSession;
	@Test
	public void test() throws Exception {
//		org.apache.ibatis.binding.MapperProxy
//		org.mybatis.spring.SqlSessionFactoryBean
//		org.apache.ibatis.executor.statement.RoutingStatementHandler
//		org.mybatis.spring.mapper.MapperScannerConfigurer
//		org.mybatis.spring.SqlSessionTemplate
//		DataSourceContextHolder.setDbType("qa");
//		org.mybatis.spring.SqlSessionFactoryBean;
//		Map map=applicationContext.getBeansOfType(SqlSession.class);
//		SqlSessionTemplate sqlSessionTemplate=(SqlSessionTemplate)map.get("sqlSessionTemplate");


//		System.out.println(map.size());
//		Iterator it=map.keySet().iterator();
//		while(it.hasNext()){
//			System.out.println(it.next());
//		}

//		sqlSessionFactory.setDataSource((DataSource)applicationContext.getBean("dataSourceCif"));
//		jdbcTemplate.setDataSource((DataSource)applicationContext.getBean("dataSourceCif"));
//
//		System.out.println("开始数据切换");
//		DataSourceContextHolder.setDbType("qa");
//		System.out.println(nav.selectByPrimaryKey("10"));
//		SqlSessionContextHolder.setSessionFactoryKey("dev");
//		System.out.println(cif_dev.selectByPrimaryKey("00120170512144254000000000000270"));
//		
////		SqlSessionContextHolder.setSessionFactoryKey("product");
//
//		System.out.println(nav.selectByPrimaryKey("10"));
	}
}