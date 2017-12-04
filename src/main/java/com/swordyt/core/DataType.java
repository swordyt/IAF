package com.swordyt.core;

/**
 * 驱动数据说明： Excel 第一个参数Excel路径，第二个参数为sheet名。 Db 第一个参数为xmlMapping 空间名.ID
 * 名，第二个参数为该id需要传入的参数，参数的格式为key=value格式，多个可用逗号隔开如：“name=yinting,age=26”
 */
public enum DataType {

	EXCEL("excel", 0), XML("xml", 1), DB("db", 2);
	public static final String DATAPROVIDER_DB = "DATAPROVIDER_DB";
	public static final String DATAPROVIDER_EXCEL = "DATAPROVIDER_EXCEL";
	public static final String DATAPROVIDER_XML = "DATAPROVIDER_XML";
	private String name;
	private int type;

	private DataType(String name, int type) {
		this.name = name;
		this.type = type;
	}

	public String getValue() {
		return this.name;
	}

	public int getType() {
		return this.type;
	}
}
