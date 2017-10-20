package com.yinting.core.Http;

import java.util.ArrayList;
import java.util.List;

import com.yinting.core.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class Json extends HttpResponse {

	@Override
	public Integer getIndex(String path, Object... parameter) {
		return this.getIndex(fillPath(path, parameter));
	}

	@Override
	public String getValue(String path, Object... parameter) {
		return this.getValue(fillPath(path, parameter));
	}

	public String fillPath(String path, Object... parameter) {
		StringBuilder builder = new StringBuilder(path);
		if (parameter == null || parameter.equals("")) {
			return this.getValue(path);
		}
		for (int i = 0; i < parameter.length; i++) {
			builder.replace(builder.indexOf("#"), builder.indexOf("#") + 1, parameter[i].toString());
		}
		Log.log("填充后的Json路径为：" + builder.toString());
		return builder.toString();
	}

	JSONObject json = null;
	JSONObject obj = null;

	public <T> T toBean(Class<T> cls) {
		return (T) JSONObject.toBean(json, cls);
	}

	@Override
	public String getValue(String path) {
		obj = this.json;
		String[] keys = path.trim().split("[.]");
		String value = "";
		for (int i = 1; i < keys.length; i++) {
			if (keys[i].contains("[")) { // 判断list [4]
				String key = keys[i].substring(0, keys[i].indexOf("["));
				int num = Integer.parseInt(keys[i].substring(keys[i].indexOf("[") + 1, keys[i].indexOf("]")));
				if (!obj.has(key)) {
					return null;
				}
				obj = obj.getJSONArray(key).getJSONObject(num);
				if (i == keys.length - 1) {
					return value = obj.getJSONArray(key).getString(num);
				} else {
					continue;
				}
			}
			if (!obj.has(keys[i])) {
				return null;
			}
			if (i < keys.length - 1) {
				obj = obj.getJSONObject(keys[i]);
				continue;
			}
			value = obj.getString(keys[i]);
		}
		return value;
	}

	public Json(String body) {
		Log.debug("Json(\""+body+"\")");
		this.json = JSONObject.fromObject(body);
	}

	/**
	 * 获取json中数组个数，默认返回0.
	 */
	public Integer getIndex(String keyStr) {
		if (keyStr.endsWith("INDEX")) {
			keyStr = keyStr.substring(0, keyStr.lastIndexOf(".INDEX"));
		}
		obj = this.json;
		List<Container> list = splitString(keyStr);
		// if (list.size() == 3) {
		// return null;
		// }
		for (int i = 1; i < list.size(); i++) {
			String key = list.get(i).getKey();
			if (!obj.has(key)) {
				Log.log("不存在该元素：" + key);
				return 0;
			}
			if (i == list.size() - 1) {
				try {
					return obj.getJSONArray(key).size();
				} catch (JSONException e) {
					e.printStackTrace();
					return 0;
				}
			}
			if (list.get(i).getValue() == null) {
				obj = obj.getJSONObject(key);
			} else {
				obj = obj.getJSONArray(key).getJSONObject(list.get(i).getValue());
			}
		}
		return 0;
	}

	private List<Container> splitString(String str) {
		List<Container> list = new ArrayList<Json.Container>();
		String[] keys = str.trim().split("[.]");
		for (String string : keys) {
			Container e = new Container();
			if (string.contains("[")) { // 判断list [4]
				String key = string.substring(0, string.indexOf("["));
				Integer num = Integer.parseInt(string.substring(string.indexOf("[") + 1, string.indexOf("]")));

				e.setKey(key);
				e.setValue(num);
				list.add(e);
				continue;
			} else {
				e.setKey(string);
				list.add(e);
				continue;
			}
		}
		return list;
	}

	public class Container {
		private String key = "";
		private Integer value = null;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}
}
