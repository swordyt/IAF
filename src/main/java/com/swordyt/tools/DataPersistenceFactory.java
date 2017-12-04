package com.swordyt.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import com.swordyt.core.BaseTestCase;
import com.swordyt.tools.DataPersistence.Parameter;

/**
 * 该tools负责将数据，永久性保存，等待指定的时间到达后调用指定的class中的方法，执行。
 * 数据保存在src\main\resources\ormapping\persistence\qa\pool.xml中，并会为该类生成相应的bean文件。
 * bean文件为：pool.xml。
 */
@Service
public class DataPersistenceFactory extends BaseTestCase {
	public static final Logger log = Logger.getLogger(DataPersistenceFactory.class);
	private Document doc;
	private Element root;
	private File fileXml;
	private SAXReader reader;
	private XMLWriter writer;
	OutputFormat format;
	// private DataPersistence dao;
	private static List<String> ids;

	public DataPersistenceFactory() {
		fileXml = new File(PropertiesTool.fillPath(System.getProperty("swordyt.data.dataPersistence.location")));
		// fileBean = new File("src/main/java/pool.xml");
		reader = new SAXReader();
		format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		try {
			doc = reader.read(fileXml);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		root = doc.getRootElement();
	}

	public void executeByID(String id) {
		try {
			execute(getDataPersistence(id));
		} catch (Exception e) {
			log.error(id + "对应数据存在问题请检查数据有效性。");
			e.printStackTrace();
			return;
		}

	}

	/**
	 * 通过传入的DataPersistence bean进行反射执行
	 */
	private void execute(DataPersistence bean) {
		Status status;
		Class clazz = null;
		try {
			clazz = Class.forName(bean.getCls());
		} catch (ClassNotFoundException e2) {
			log.error("Exception:" + bean.getCls());
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// Class clazz=applica
		// tionContext.getClassLoader().loadClass(bean.getCls());
		Object obj = applicationContext.getAutowireCapableBeanFactory().createBean(clazz);// 自动创建bean
		// Object obj = applicationContext.getBean(bean.getId(), clazz);
		List<DataPersistence.Parameter> parameters = bean.getParameters();
		Class[] paraType = null;
		String[] paraValue = null;
		if (parameters.size() > 0) {
			paraType = new Class[parameters.size()];
			paraValue = new String[parameters.size()];
			for (int i = 0; i < parameters.size(); i++) {
				try {
					paraType[i] = Class.forName(parameters.get(i).getType());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				paraValue[i] = parameters.get(i).getValue();
			}
		}
		Method method = null;
		try {
			method = clazz.getMethod(bean.getMethod(), paraType);
		} catch (NoSuchMethodException e1) {
			log.warn("Exception" + clazz.toString() + "," + bean.getMethod());
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// Object reValue = method.invoke(obj, paraValue);
			Object reValue = method.invoke(obj, paraValue);
			status = (Status) reValue;
		} catch (Exception e) {
			e.printStackTrace();
			status = Status.EXCEPTION;
		}
		if (status == null) {
			log.warn(bean.getId() + "被执行方法未标明执行状态，默认设置为Exception.");
			status = Status.EXCEPTION;
		}
		bean.setStatus(status.getValue());
		writeXml(bean);
	}

	/**
	 * 通过元素id将数据装配到实体中，并返回实体。
	 */
	/**
	 * @param id
	 * @return
	 */
	private DataPersistence getDataPersistence(String id) {
		Element e = this.root.elementByID(id);// 根据ID获取到元素
		if (e == null) {
			throw new RuntimeException("未查找到ID为：" + id + "的数据。请检查ID的正确性。");
		}
		DataPersistence dao = new DataPersistence();
		dao.setId(id);
		dao.setCls(e.attributeValue("cls"));
		dao.setMethod(e.attributeValue("method"));
		dao.setStatus(e.attributeValue("status"));
		dao.setTime(e.attributeValue("time"));
		Iterator<Element> es = e.elementIterator();// 获取元素下的parameter
		while (es.hasNext()) {
			Element parameter = es.next();
			Parameter para = dao.getParameter();
			para.setId(parameter.attributeValue("ID"));
			para.setType(parameter.attributeValue("type"));
			para.setValue(parameter.getTextTrim());
			dao.setParameters(para);
			log.info("正在初始化数据:" + parameter.getTextTrim());
		}
		return dao;
	}

	/**
	 * 将xml中所有的id拼接到list中
	 */
	public List<String> read() {
		if (ids == null) {
			ids = new ArrayList<String>();
		}
		Iterator<Element> it = root.elementIterator();
		while (it.hasNext()) {
			Element e = it.next();
			// idsAll.add(e.attributeValue("ID"));
			if (e.attributeValue("status").equalsIgnoreCase("SUCCESS")) {
				continue;
			}
			long time = Long.parseLong(e.attributeValue("time"));
			long currentTime = new Date().getTime() / 1000;
			System.out.println("currentTime:" + currentTime + ",time:" + time);
			if (!(currentTime - time > 0)) {
				continue;
			}
			ids.add(e.attributeValue("ID"));
		}
		return ids;
	}
	/**
	 * 
	 * 获取bean配置文件中的id，并检查该id的有效性。如果该id在xml中不存在将删除该项
	 */
	// public List<String> readBean() {
	//// beanReader();
	// read();
	// Iterator< Element> it=rootBean.elementIterator("bean");
	// idsBean=new ArrayList<String>();
	// while(it.hasNext()) {
	// Element e=it.next();
	// String id=e.attributeValue("id");
	// if(!idsAll.contains(id)) {
	// rootBean.remove(e);
	// continue;
	// }
	// idsBean.add(id);
	// }
	// write(fileBean, docBean);
	// return idsBean;
	// }

	/**
	 * time yyyy-MM-dd-HH-mm-ss 年，月，日，时，分，秒
	 * save(TestStatus.class, "test", "1s", new String[] {"yinting","26"});
	 */
	public void save(Class cls, String method, String time, String[] parameters) {
		write(makeEntity(null, cls, method, changeTime(time), null, parameters));
	}

	/**
	 * 根据传入的信息见信息封装成bean对象并返回,对于本次刚写入的数据一般不会包含。
	 */
	private DataPersistence makeEntity(String id, Class cls, String method, String time, String status,
			String[] parameters) {
		DataPersistence data = new DataPersistence();
		data.setCls(cls.getName());
		data.setMethod(method);
		data.setTime(time);
		if (id != null) {
			data.setId(id);
		}
		if (status != null) {
			data.setStatus(status);
		}
		if (parameters == null) {
			return data;
		}
		for (int i = 0; i < parameters.length; i++) {
			String[] parameter = parameters[i].split("=");
			if (!(parameter.length == 2 || parameter.length == 1)) {
				throw new RuntimeException("传递的参数格式有误,请满足key=value格式，或value，此时key默认为string" + parameter.toString());
			}
			if (parameter.length == 1) {
				data.setParameters(String.class.getName(), parameter[0]);
				continue;
			}
			if (parameter.length == 2) {
				data.setParameters(parameter[0], parameter[1]);
			}
		}
		return data;
	}

	/**
	 * 根据传入的bean 将对象数据写入bean和xml文件，
	 */
	private void write(DataPersistence data) {
		// writeBean(data);
		writeXml(data);
	}

	/**
	 * 统一初始化root,readerBean,rootBean参数。
	 */
	// private Element beanReader() {
	// if (readerBean == null) {
	// readerBean = new SAXReader();
	// }
	// try {
	// docBean = readerBean.read(fileBean);
	// } catch (DocumentException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// rootBean = docBean.getRootElement();
	// return rootBean;
	// }

	/**
	 * 将xml中数据同步到bean文件中。
	 */
	// private void writeBean(DataPersistence data) {
	//// beanReader();
	// Element eBean = rootBean.addElement("bean");
	// eBean.setAttributeValue("id", data.getId());
	// eBean.setAttributeValue("class", data.getCls());
	// write(fileBean,docBean);
	//
	// }
	/**
	 * 根据传入的File和Document 将Document写入指定的File
	 */
	private void write(File file, Document document) {
		try {
			writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 将数据写入到xml中
	 */
	private void writeXml(DataPersistence data) {
		Element e = root.elementByID(data.getId());
		if (e == null) {
			e = root.addElement("datasource");
		}
		;
		e.setAttributeValue("ID", data.getId());
		e.setAttributeValue("cls", data.getCls());
		e.setAttributeValue("method", data.getMethod());
		e.setAttributeValue("time", data.getTime());
		e.setAttributeValue("status", data.getStatus());
		List<DataPersistence.Parameter> parameters = data.getParameters();
		for (int i = 0; i < parameters.size(); i++) {
			Element eParam = e.elementByID(parameters.get(i).getId());
			if (eParam == null) {
				eParam = e.addElement("paramerter");
			}

		}
		List<Element> els = e.elements();
		for (int i = 0; i < els.size(); i++) {
			Element el = els.get(i);
			el.setAttributeValue("type", parameters.get(i).getType());
			el.setAttributeValue("ID", parameters.get(i).getId());
			el.setText(parameters.get(i).getValue());
		}
		write(fileXml, doc);
	}

	/**
	 * 将传入的时间转换成秒，并加上当前时间的时间戳。
	 */
	private String changeTime(String time) {
		long t = 0;
		long year = 0;
		long month = 0;
		long day = 0;
		long hour = 0;
		long minute = 0;
		long second = 0;
		String[] times = time.split("-");
		log.info("时间转换开始：");
		for (int i = 0; i < times.length; i++) {
			char c = times[i].charAt(times[i].length() - 1);
			String timePrefix = times[i].substring(0, times[i].length() - 1);
			switch (c) {
			case 'y':// 年
				log.info(timePrefix + "年");
				year = Integer.parseInt(timePrefix) * 31622400;
				break;
			case 'M':// 月
				log.info(timePrefix + "月");
				month = Integer.parseInt(timePrefix) * 2592000;
				break;
			case 'd':// 日
				log.info(timePrefix + "日");
				day = Integer.parseInt(timePrefix) * 86400;
				break;
			case 'H':
				log.info(timePrefix + "时");
				hour = Integer.parseInt(timePrefix) * 3600;
				break;
			case 'm':
				log.info(timePrefix + "分");
				minute = Integer.parseInt(timePrefix) * 60;
				break;
			case 's':
				log.info(timePrefix + "秒");
				second = Integer.parseInt(timePrefix);
				break;
			default:
				throw new RuntimeException(times[i] + "time格式有误，请按*y*M*d*H*m*s 格式赋值。");
			}
		}
		return (new Date().getTime() / 1000 + year + month + day + hour + minute + second) + "";
	}

	@Test
	public void traversalData() {
		List<String> list = read();
		for (String id : list) {
			executeByID(id);
		}
		// executeByID("id1502434784613");
//		 save(TestStatus.class, "test", "1s", new String[] {"yinting","26"});
	}

}
