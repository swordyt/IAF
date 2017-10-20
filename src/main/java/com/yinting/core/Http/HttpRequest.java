package com.yinting.core.Http;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yinting.core.Log;
import com.yinting.core.Request;
import com.yinting.core.Response;
import com.yinting.core.Test.TestFactory;

@Service
public class HttpRequest implements Request {
	private HttpRequest request;
	private String message;

	public Request request(Map<String, String> data) {
		if (data.get("Method").equalsIgnoreCase("post")) {
			this.POST(data.get("Url"));
		} else {
			this.GET(data.get("Url"));
		}
		this.body("deviceId", data.get("DeviceId"));
		this.body("merchantId", data.get("MerchantId"));
		this.body("source", data.get("Source"));
		this.body("tokenId", data.get("TokenId"));
		this.body("userId", data.get("UserId"));

		JSONObject json = new JSONObject();
		for (String key : data.keySet()) {
			String value = data.get(key);
			if (key.equals("DeviceId") || key.equals("MerchantId") || key.equals("Source") || key.equals("TokenId")
					|| key.equals("Description") || key.equals("Run") || key.equals("Method") || key.equals("Url")
					|| key.equals("UserId")) {
				continue;
			}
			json.put(key, value);
		}
		this.body("content", json.toJSONString());

		return this;
	}

	public Request body(String name, String value) {
		this.message += ".body(" + name + "," + value + ")";
		this.request.body(name, value);
		return this;
	}

	public Request bodyes(Map<String, String> bodyes) {
		for (Entry<String, String> entry : bodyes.entrySet()) {
			body(entry.getKey(), entry.getValue());
		}
		return this;
	}

	public Request GET(String url) {
		url = assertUrl(url);
		TestFactory.getStep().setStartTime(new Date().getTime());
		this.request = new Get(url);
		this.message = "driver.GET(" + url + ")";
		return this;
	}

	public Request POST(String url) {
		url = assertUrl(url);
		TestFactory.getStep().setStartTime(new Date().getTime());
		this.message = "driver.POST(" + url + ")";
		this.request = new Post(url);
		return this;
	}

	public Request PUT(String url) {
		TestFactory.getStep().setStartTime(new Date().getTime());
		// TODO Auto-generated method stub
		return null;
	}

	public Request DELETE(String url) {
		TestFactory.getStep().setStartTime(new Date().getTime());
		// TODO Auto-generated method stub
		return null;
	}

	public Request HEAD(String url) {
		TestFactory.getStep().setStartTime(new Date().getTime());
		return null;
	}

	public Request header(String name, String value) {
		this.message += ".header(" + name + "," + value + ")";
		this.request.header(name, value);
		return this;
	}

	public Request headeres(Map<String, String> headeres) {
		this.headeres(headeres);
		return this;
	}

	public Response invoke() {
		this.message += ".invoke()";
		Log.log(this.message);
		TestFactory.getStep().setEndTime(new Date().getTime());
		TestFactory.getStep().setStep(this.message);
		TestFactory.endStep();
		return this.request.invoke();
	}

	/**
	 * 将传入的uri转换成完整的Url
	 */
	private String assertUrl(String url) {
		// "http:"+System.getProperty("Domain")+"/"
		if(url.toLowerCase().startsWith("http://")){
			return url;
		}
		if (!url.startsWith("/")) {
			url = "/" + url;
		}
		return "http://" + System.getProperty("Domain") + url;
	}

}
