package com.swordyt.mybatis;

/**
 * 保存当前进程的前一个数据源
 */
public class SqlSessionContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setSessionFactoryKey(String dataSourceKey) {
		contextHolder.set(dataSourceKey);
	}
/**
 * 当contextHolder不为空时，getDataSourceKey优先级最高。
 * 当contextHolder为空时，将根据各自的环境决定使用规则
 * */
	public static String getDataSourceKey() {
		return contextHolder.get();
	}
}
