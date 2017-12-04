package com.swordyt.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.log4testng.Logger;

public class PropertiesTool {
	private static final Logger log = Logger.getLogger(PropertiesTool.class);

	public static String fillPath(String path) {
		Pattern p = Pattern.compile("\\$\\{.*?\\}");
		Matcher m = p.matcher(path);
		while (m.find()) {
			String parameter = m.group().substring(2, m.group().length() - 1);
			path = path.replace(m.group(),
					System.getProperty(parameter) == null ? "null" : System.getProperty(parameter));
			m = p.matcher(path);
		}
		log.info("格式化字符串为：" + path);
		return path;
	}

	/**
	 * 
	 * 从System中提取指定key,并根据指令进行转化
	 */
	public static String getProperty(String key) {
		return fillPath(System.getProperty(key));
	}
}
