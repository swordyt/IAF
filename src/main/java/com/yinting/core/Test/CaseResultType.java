package com.yinting.core.Test;

public enum CaseResultType {
	SUCCESS("Success", 1), FAILURE("Failure", 2), SKIPPED("Skipped", 3), FBWSP(
			"FailedButWithinSuccessPercentage", 4);
	private String name;
	private int type;

	private CaseResultType(String name, int type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

}
