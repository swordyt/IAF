package com.yinting.core.Http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.yinting.core.Request;
import com.yinting.core.Response;

public class Get extends HttpRequest {
	private final HttpClient client = new DefaultHttpClient();
	private HttpResponse response;
	private final HttpGet get;
	private String url;

	public Get(String url) {
		this.url = url;
		get = new HttpGet();
	}

	@Override
	public Request body(String name, String value) {
		if (this.url != null && this.url.contains("?")) {
			this.url += "&" + name + "=" + value;
		} else {
			this.url += "?" + name + "=" + value;
		}
		return null;
	}
	@Override
	public Request header(String name, String value) {
		this.get.addHeader(name, value);
		return null;
	}

	@Override
	public Request headeres(Map<String, String> headeres) {
		for (Map.Entry<String, String> entry : headeres.entrySet()) {
			this.header(entry.getKey(), entry.getValue());
		}
		return null;
	}

	@Override
	public Response invoke() {
		try {
			get.setURI(new URI(url));
			this.response = client.execute(this.get);
//			System.out.println(EntityUtils.toString(this.response.getEntity(),
//					"utf-8"));
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
		return new com.yinting.core.Http.HttpResponse(this.response);
	}

}
