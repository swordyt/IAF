package com.swordyt.tools;

public enum Status {
	SUCCESS("success"), FALSE("false"), EXCEPTION("exception");
	private String value;

	private Status(String status) {
		this.value = status;
	}

	public String getValue() {
		return this.value;
	}
}
