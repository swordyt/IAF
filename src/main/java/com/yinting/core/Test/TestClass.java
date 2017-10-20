package com.yinting.core.Test;

import java.util.ArrayList;
import java.util.List;

public class TestClass {
	private long startTime = 0;
	private long endTime = 0;
	private List<TestCase> caseList = new ArrayList<TestCase>();

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<TestCase> getCaseList() {
		return caseList;
	}

	public void setCaseList(List<TestCase> caseList) {
		this.caseList = caseList;
	}

	public void insertCase(TestCase testCase) {
		this.caseList.add(testCase);
	}
}
