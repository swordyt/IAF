package com.yinting.core.Test;

import java.util.ArrayList;
import java.util.List;

public class TestSuite {
	private long startTime = 0;
	private long endTime = 0;
	private List<TestClass> classList = new ArrayList<TestClass>();

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

	public List<TestClass> getClassList() {
		return classList;
	}

	public void setClassList(List<TestClass> classList) {
		this.classList = classList;
	}

	public void insertClass(TestClass e) {
		classList.add(e);
	}
}
