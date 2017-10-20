package com.yinting.core;

import java.util.Map;

public interface Response {
	public Response assertStatus(String expect);

	public String status();

	public Response assertHeader(String header, String expect);

	public String header(String parameter);

	public Map getHeaderes();

	public Response assertValue(String path, String expect);

	public String getValue(String path);
	public String getValue(String path,Object ... parameter);
	public Integer getIndex(String path,Object ... parameter);
	public String getBody();

	public <T> T toBean(Class<T> cls);

	public Integer getIndex(String keyStr);
}
