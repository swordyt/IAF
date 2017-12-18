package com.swordyt.core;

import java.util.Map;

import com.swordyt.http.HttpRequest;
import com.swordyt.http.HttpResponse;
import com.swordyt.http.Post;
import com.swordyt.http.Request;

public abstract class TestCase extends BaseTestCase implements Request {
	private HttpRequest driver = null;

	public abstract String getUrl();

	private synchronized HttpRequest Instance() {
		if (driver == null) {
			this.driver = new HttpRequest(new Post(getUrl()));
		}
		return driver;
	}

	/**
	 * 当不使用Post时可自行赋值
	 */
	public void setDriver(HttpRequest request) {
		this.driver = request;
	}

	public Request body(String name, String value) {
		return this;
	}

	public Request bodies(Map<String, String> bodyes) {
		return this;
	}

	public Request header(String name, String value) {
		return this;
	}

	public Request headeres(Map<String, String> headeres) {
		return this;
	}

	public HttpResponse invoke() {
		return this.driver.invoke();
	}

}
