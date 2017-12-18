package com.swordyt.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Get implements Request {
	private final HttpClient client = new DefaultHttpClient();
	private HttpResponse response;
	private final HttpGet get;
	private String url;

	public Get(String url) {
		this.url = url;
		get = new HttpGet();
	}

	public HttpRequest body(String name, String value) {
		if (this.url != null && this.url.contains("?")) {
			this.url += "&" + name + "=" + value;
		} else {
			this.url += "?" + name + "=" + value;
		}
		return null;
	}
	public HttpRequest header(String name, String value) {
		this.get.addHeader(name, value);
		return null;
	}

	public com.swordyt.http.HttpResponse invoke() {
		try {
			get.setURI(new URI(url));
			this.response = client.execute(this.get);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new com.swordyt.http.HttpResponse(this.response);
	}

	public Request bodies(Map<String, String> bodyes) {
		// TODO Auto-generated method stub
		return null;
	}

	public Request headeres(Map<String, String> headeres) {
		// TODO Auto-generated method stub
		return null;
	}

}
