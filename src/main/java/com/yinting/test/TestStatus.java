package com.yinting.test;

import org.testng.annotations.Test;

import com.yinting.core.Http.Json;
import com.yinting.tools.ShellClient;

public class TestStatus {

	public void test(String name, String value) {
		System.out.println(name + ":" + value);

	}

	@Test
	public void test1() {
		ShellClient shell = ShellClient.getInstance("maiziyunweb_dev");
		shell.login();
		shell.exec("/opt/logs/mgw/json.sh  e7e93ae099bd11e784c600163e132bdd /opt/logs/mgw/mgw-info.2017-09-15.0.log");
		StringBuilder builder = shell.getStdout();
		System.out.println(processJson(shell.getStdout()));
		String response = processJson(shell.getStdout());
		Json json = new Json(
				response.substring(response.indexOf("_httpContent=") + 13, response.lastIndexOf(", _httpStatusCode=")));
		System.out.println(json.getValue("resp.poDesc"));
	}

	public String processJson(StringBuilder builder) {
		return builder.substring(builder.indexOf("===start===") + 11, builder.lastIndexOf("===end==="));
	}
}
