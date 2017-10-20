package com.yinting.core.Http;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Xml extends HttpResponse{
	Document doc=null;
	@Override
	/**
	 * resp.city.updatetime
	 * resp.city.INDEX
	 * resp.city.updatetime.wendu.PROPERTY
	 * resp.city.updatetime.PROPERTY.INDEX
	 * */
	public String getValue(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public Xml(String response) {
		SAXReader sr=new SAXReader();
		try {
			doc = sr.read(new StringReader(response));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("格式化响应为xml格式出错，请检查格式正确");
		}
	}

}
