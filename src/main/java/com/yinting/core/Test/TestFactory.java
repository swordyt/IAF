package com.yinting.core.Test;

import java.util.List;

public class TestFactory {
	private static TestSuite suite;
	private static TestClass cls;
	private static TestCase cas;
	private static TestStep step;

	public static TestStep getStep() {
		if (step == null) {
			step = new TestStep();
		}
		return step;
	}

	public static void endStep() {
		if (cas != null) {
			TestFactory.cas.insertStep(TestFactory.step);
			TestFactory.step = null;
		}

	}

	public static TestSuite getSuite() {
		if (TestFactory.suite == null) {
			TestFactory.suite = new TestSuite();
		}
		return TestFactory.suite;
	}

	public static TestClass getCls() {
		if (TestFactory.cls == null) {
			TestFactory.cls = new TestClass();
		}
		return TestFactory.cls;
	}

	public static TestCase getCas() {
		if (TestFactory.cas == null) {
			TestFactory.cas = new TestCase();
		}
		return TestFactory.cas;
	}

	public static void endSuite() {
//		List<TestClass> classList = TestFactory.suite.getClassList();
//		System.out.println("===============SuiteStart==================");
//		System.out.println("套件开始时间：" + TestFactory.suite.getStartTime());
//		for (int i = 0; i < classList.size(); i++) { // 类
//			List<TestCase> caseList = classList.get(i).getCaseList();
//			System.out
//					.println("=================ClassStart====================");
//			System.out.println("类开始时间：" + classList.get(i).getStartTime());
//			for (int j = 0; j < caseList.size(); j++) {// test
//				List<TestStep> stepList = caseList.get(j).getStepList();
//				System.out
//						.println("==============TestStart===================");
//				System.out.println("测试开始时间：" + caseList.get(j).getStartTime());
//				for (int k = 0; k < stepList.size(); k++) {
//					TestStep step = stepList.get(k);
//					System.out.println("==========StepStart==================");
//					System.out.println("步骤开始时间：" + step.getStartTime());
//					System.out.println("步骤" + (k + 1) + "：" + step.getStep());
//					System.out.println("步骤结束时间：" + step.getEndTime());
//					System.out.println("==========StepEnd====================");
//				}
//				System.out.println("测试结束时间：" + caseList.get(j).getEndTime());
//				System.out
//						.println("==============TestEnd=====================");
//			}
//
//			System.out.println("类结束时间：" + classList.get(i).getEndTime());
//			System.out.println("=================ClassEnd====================");
//		}
//		System.out.println("套件结束时间：" + TestFactory.suite.getEndTime());
//		System.out.println("===============SuiteEnd==================");
	}

	public static void endClass() {
		TestFactory.suite.insertClass(cls);
		cls = null;
	}

	public static void endCase() {
		if(TestFactory.cls !=null){
			TestFactory.cls.insertCase(cas);
		}
		cas = null;
	}
}
