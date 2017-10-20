package com.yinting.core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {
	private static Logger log;

	private Log(String name) {

	}

	public static Logger instance() {
		if (log == null) {
			PropertyConfigurator.configure("src/main/java/log4j.properties");
			log = Logger.getLogger(Log.class);
		}
		return log;
	}

	public static void debug(String message) {
		Log.instance().debug(message);
	}

	public static void debug(String message, Throwable t) {
		Log.instance().debug(message,t);
	}

	public static void log(String message) {
		Log.instance().info(message);
	}

	public static void log(String message, Throwable t) {
		Log.instance().info(message, t);
	}
}
