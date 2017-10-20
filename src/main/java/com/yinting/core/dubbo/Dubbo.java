package com.yinting.core.dubbo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.yinting.core.Log;

public class Dubbo {
	private static List<String> serviceMapping;
	private static ClassPathXmlApplicationContext context;

	public static void addServiceMapping(String serviceName) {
		if (serviceMapping == null) {
			serviceMapping = new ArrayList<String>();
		}
		serviceMapping.add(serviceName);
	}

	public static String[] getServiceMapping() {
		return serviceMapping.toArray(new String[serviceMapping.size()]);
	}

	public static void scanDubboXml() {
		scanDubboXml("src/main/resources/ormapping/dubbo/" + System.getProperty("ENV"));
	}

	public static void scanDubboXml(String path) {
		File[] files;
		File dubbo = new File(path);
		if (!dubbo.exists()) {
			Log.log("地址不存在：" + path + ",无法加载dubbo配置文件。");
			return;
		}
		if (!dubbo.isDirectory()) {
			Log.log(path + "不是目录，请检查存放dubbo配置文件地址。");
			return;
		}
		files = dubbo.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				addServiceMapping(path.substring(18) + "/" + files[i].getName());
				continue;
			}
			if (files[i].isDirectory()) {
				scanDubboXml(path + "/" + files[i].getName());
				continue;
			}
			return;
		}
	}

	public static Object getService(String benaName) {
		if (serviceMapping == null) {
			Log.log("dubbo 无映射文件，不能调用服务。");
			return null;
		}
		if (context == null) {
			context = new ClassPathXmlApplicationContext(serviceMapping.toArray(new String[serviceMapping.size()]));
		}
		context.start();

		return context.getBean(benaName);

	}
}
