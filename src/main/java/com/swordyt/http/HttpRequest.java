package com.swordyt.http;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.swordyt.datadriver.ExcelDriver;

@Service
public class HttpRequest {
	public static final Logger log=Logger.getLogger(HttpRequest.class);
	private HttpRequest request;
	private String message;

	public HttpRequest body(String name, String value) {
		this.message += ".body(" + name + "," + value + ")";
		this.request.body(name, value);
		return this;
	}

	public HttpRequest bodyes(Map<String, String> bodyes) {
		for (Entry<String, String> entry : bodyes.entrySet()) {
			body(entry.getKey(), entry.getValue());
		}
		return this;
	}

	public HttpRequest GET(String url) {
		url = assertUrl(url);
		this.request = new Get(url);
		this.message = "driver.GET(" + url + ")";
		return this;
	}

	public HttpRequest POST(String url) {
		url = assertUrl(url);
		this.message = "driver.POST(" + url + ")";
		this.request = new Post(url);
		return this;
	}

	public HttpRequest PUT(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpRequest DELETE(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpRequest HEAD(String url) {
		return null;
	}

	public HttpRequest header(String name, String value) {
		this.message += ".header(" + name + "," + value + ")";
		this.request.header(name, value);
		return this;
	}

	public HttpRequest headeres(Map<String, String> headeres) {
		for (Entry<String, String> entry : headeres.entrySet()) {
			this.header(entry.getKey(), entry.getValue());
		}
		return this;
	}

	public HttpResponse invoke() {
		this.message += ".invoke()";
		log.info(this.message);
		return this.request.invoke();
	}

	/**
	 * 将传入的uri转换成完整的Url
	 */
	private String assertUrl(String url) {
		// "http:"+System.getProperty("Domain")+"/"
		if (url.toLowerCase().startsWith("http://")) {
			return url;
		}
		if (!url.startsWith("/")) {
			url = "/" + url;
		}
		return "http://" + System.getProperty("Domain") + url;
	}

}
