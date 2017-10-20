package com.yinting.core.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCase {
	private long startTime = 0;
	private long endTime = 0;
	private CaseResultType result;
	private List<TestStep> stepList = new ArrayList<TestStep>();

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

	public List<TestStep> getStepList() {
		return stepList;
	}

	public void setStepList(List<TestStep> stepList) {
		this.stepList = stepList;
	}

	public void insertStep(TestStep step) {
		stepList.add(step);
	}

	public CaseResultType getResult() {
		return result;
	}

	public void setResult(CaseResultType result) {
		this.result = result;
	}

}
