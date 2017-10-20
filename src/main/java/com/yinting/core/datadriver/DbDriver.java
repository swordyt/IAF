package com.yinting.core.datadriver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.yinting.jdbc.DbHandller;
import com.yinting.jdbc.Page;

/**
 * xmlMapping中返回类型需要为resultMap->Map 传入的参数需要满足格式：name=value,name1=value1
 * */
public class DbDriver extends DataDriver {
	private DbHandller handle;

	Map map = new HashMap();
	private int total = 0;
	private int position = 1;
	private String path = "";
	private Map<String, String> para = null; // 需要传递给sqlmapping的参数
	private String xmlMapping = "";// xml映射

	public DbDriver(Method method) {
		System.out.println("这里是DB驱动构造方法");
		parameter = DataDriver.parameteres.get(md5(method)); // 获取本test的参数
		this.totalParameter = parameter.length;
		ApplicationContext actx=new FileSystemXmlApplicationContext("classpath*:qacontext/applicationContext.xml");
		this.handle=(DbHandller)actx.getBean("handle");
		initPara();
	}

	public boolean hasNext() {
		// TODO Auto-generated method stub
		return position <= total;
	}

	public Object[] next() {
		List<Map<String, String>> list = handle.queryForList(this.xmlMapping,this.map);
		int index=this.position-1;
		this.position++;
		return new Object[] { list.get(index) };
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void remove() {
		// TODO Auto-generated method stub

	}

	private void initPara() {
		if (this.parameter.length < 2) {
			throw new RuntimeException(
					"数据驱动错误：参数个数不正确，一：xmlMapping->method，二：method接收的参数");
		}
		this.xmlMapping = this.parameter[0];
		String str = this.parameter[1];
		String[] paraes = str.split(",");
		for (String param : paraes) {
			String[] stres = param.split("=");
			if (stres.length != 2) {
				continue;
			}
			this.map.put(stres[0], stres[1]);
		}
		List<Map<String, String>> list = null;
		if (this.map.isEmpty()) {
			list = handle.queryForList(this.xmlMapping, "");
		} else {
			list = handle.queryForList(this.xmlMapping, this.map);
		}
		this.total = list.size();
	}

}
