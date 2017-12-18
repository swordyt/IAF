package com.swordyt.http;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class HttpRequest implements Request {
	public static final Logger log = Logger.getLogger(HttpRequest.class);
	private Request request;
	private String message;

	public HttpRequest(Request request) {
		this.request = request;
	}

	public Request body(String name, String value) {
		this.message += ".body(" + name + "," + value + ")";
		this.request.body(name, value);
		return this;
	}

	public Request bodies(Map<String, String> bodyes) {
		for (Entry<String, String> entry : bodyes.entrySet()) {
			body(entry.getKey(), entry.getValue());
		}
		return this;
	}

	public Request header(String name, String value) {
		this.message += ".header(" + name + "," + value + ")";
		this.request.header(name, value);
		return this;
	}

	public Request headeres(Map<String, String> headeres) {
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
