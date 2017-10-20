package com.yinting.core;

public enum RequestType {
	GET("get"), POST("post"), PUT("put"), DELETE("delete"), HEAD("head");
	private String value;
	private RequestType(String value){
		this.value=value;
	}
	public String  getValue(){
		return this.value;
	}
}
