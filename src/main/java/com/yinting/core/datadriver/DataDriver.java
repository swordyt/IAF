package com.yinting.core.datadriver;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class DataDriver implements Iterator<Object[]> {
	protected int totalParameter = 0;
	protected String[] parameter;
	public static Map<String, String[]> parameteres = new HashMap<String, String[]>();

	public abstract int getType();

	public static String md5(Method method) {
		String key = method.getDeclaringClass().getName() + "-"
				+ method.getName();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5",
					new sun.security.provider.Sun());
			md5.update(key.getBytes());
			String md5Encode = new BigInteger(1, md5.digest()).toString();
			return md5Encode;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("MD5计算错误！");
		}
	}
}
