package com.yinting.jdbc;
/**
 * 保存当前进程的前一个数据源
 * */
public class DataSourceContextHolder {
	 private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
	  
     public static void setDbType(String dbType) {  
            contextHolder.set(dbType);  
     }  
  
     public static String getDbType() { 
    	 if(contextHolder == null) {
    		 return null;
    	 }
            return ((String) contextHolder.get());  
     }  
  
     public static void clearDbType() {  
            contextHolder.remove();  
     }  
}
