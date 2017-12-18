package com.swordyt.http;

import java.util.Map;

public interface Request {
	public Request body(String name, String value);

	public Request bodies(Map<String, String> bodyes);

	public Request header(String name, String value);

	public Request headeres(Map<String, String> headeres);

	public HttpResponse invoke();
}
