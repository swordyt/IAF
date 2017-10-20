package com.yinting.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataPersistence {
	private String ID;
	private String cls;
	private String method;
	private String time;
	private String status = "init";//true:已执行,false:未执行成功,init:刚创建未执行
	private List<Parameter> parameters = new ArrayList<Parameter>();
	public Parameter getParameter() {
		return new Parameter();
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(String id) {
		this.ID = id;
	}

	public DataPersistence() {
		ID = "id"+new Date().getTime() + "";
	}

	public String getId() {
		return ID;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(String type, String value) {
		Parameter parameter = new Parameter();
		parameter.setType(type);
		parameter.setValue(value);
		this.parameters.add(parameter);
	}
	public void setParameters(Parameter parameter) {
		this.parameters.add(parameter);
	}

	public class Parameter {
		private String id;
		private String type;
		private String value;
		public Parameter() {
			this.id=new Date().getTime()+"";
		}
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
