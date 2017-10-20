package com.yinting.core.Http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.yinting.core.Request;
import com.yinting.core.Response;

public class Post extends HttpRequest {

	private final String url;
	private final HttpPost post;
	CloseableHttpClient client = HttpClients.createDefault();
	private HttpResponse response;
//	private HttpResponse response;
	private final List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	private UrlEncodedFormEntity uefEntity;

	public Post(String url) {
		post = new HttpPost();
		this.url = url;
	}

	@Override
	public Request body(String name, String value) {
		formparams.add(new BasicNameValuePair(name, value));
		return null;
	}

	@Override
	public Request header(String name, String value) {
		this.post.addHeader(name, value);
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
			post.setURI(new URI(url));
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(uefEntity);

			this.response=new HttpResponse(client.execute(this.post));
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
		return this.response;
	}

}
