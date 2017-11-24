package com.yinting.core;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.yinting.core.BaseTestCase;
import com.yinting.core.Http.HttpResponse;
import com.yinting.core.Http.Json;
import com.yinting.tools.ExcelTools;

public abstract class TestCase extends BaseTestCase {
	protected static final String CODE="JSON.code";
	protected static final String CODEVALUE="000000";
	protected static final String MESSAGE="JSON.message";
	protected static final String MESSAGEVALUE="成功";
	
	// 存储当前的请求参数
	private Map<String, String> data = new HashMap<String, String>();
	private Map<String, String> tmpData = new HashMap<String, String>();
	public DecimalFormat df = new DecimalFormat("######0.00");
	protected static final String COMMAND = "/opt/logs/mgw/json.sh ";
	// 修改sheet名 ，默认为“true”
	private String sheet = "true";
	// 保存当前接口json
	protected Json json;

	/**
	 * format:"######0.00"
	 */
	public String formatDouble(Double number, String format) {
		DecimalFormat df = new DecimalFormat(format);
		return df.format(number);
	}

	public String getLogPath() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return "/opt/logs/mgw/mgw-info." + formatter.format(currentTime) + ".0.log";
	}

	/**
	 * 使用emv配置格式进行数据格式化。
	 */
	public DecimalFormat getDecimalFormat() {
		return getDecimalFormat(System.getProperty("DecimalFormat"));
	}

	/**
	 * 根据传入的格式，返回格式化对象
	 */
	public DecimalFormat getDecimalFormat(String pattern) {
		return new DecimalFormat(pattern);
	}

	public TestCase setParameter(String name, String value) {
		this.tmpData.put(name, value);
		return this;// 支持链式输入
	}

	public TestCase setParameters(Map<String, String> data) {
		this.tmpData.putAll(data);
		return this;// 支持链式输入
	}

	/**
	 * 当全局接口参数存储中不存在该key时，会去搜索临时参数存储区。
	 */
	public String getParameter(String name) {
		return this.data.get(name);
	}

	public String getTmpParameter(String name) {
		return this.tmpData.get(name);
	}

	/**
	 * 当全局接口参数存储为null时，自己返回临时参数存储区
	 */
	public Map<String, String> getParameters() {
		return this.data;
	}

	public Map<String, String> getTmpParameters() {
		return this.tmpData;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	public String getSheet() {
		return sheet;
	}

	// 快速设置登录状态
	public TestCase setLogin(String tokenId, String userId) {
		this.tmpData.put("TokenId", tokenId);
		this.tmpData.put("UserId", userId);
		this.tmpData.put("userId", userId);
		return this;
	}

	// 获取每个接口绑定的xls
	public abstract String getXLS();

	public Json getJson() {
		return json;
	}

	public String getValue(String path) {
		return json.getValue(path);
	}

	// 执行接口的几种方式
	public void send(Map data) {
		if(data == null){
			data=new HashMap();
		}
		data.putAll(this.tmpData);
		this.data = data;
		HttpResponse response = (HttpResponse) driver.request(this.data).invoke();
		json = (Json) response.JSON().getResponse();
	}

	public void send(int row) {
		Map data = ExcelTools.data(this.getXLS(), this.getSheet(), row);
		send(data);
	}

	public void send(String row) {
		Map data = ExcelTools.data(this.getXLS(), this.getSheet(), row);
		send(data);
	}

	/**
	 * sheet
	 */
	public void send() {
		Map data = ExcelTools.data(this.getXLS(), this.getSheet());
		send(data);
	}
}
