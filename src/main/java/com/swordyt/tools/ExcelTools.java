package com.swordyt.tools;

import java.util.Map;

import com.swordyt.datadriver.ExcelDriver;

public class ExcelTools {
	/**
	 * 随主数据驱动取值，当无数据驱动支持时，只取第一行数据。
	 */
	public static Map data(String path, String sheet) {
		return data(path, sheet, ExcelDriver.publicPosition.get() == null ? 1 : ExcelDriver.publicPosition.get(), true);
	}

	/**
	 * @param exeNum 行号
	 * 取特定行数据
	 */
	public static Map data(String path, String sheet, int exeNum) {
		return data(path, sheet, exeNum, false);
	}

	/**
	 * @param columnNum 待搜索的列号
	 * @param flag 搜索关键字
	 * 根据传入的标识，固定返回第一次发现的行号 如未发现将返回：null；
	 */
	public static Map data(String path, String sheet, String flag,int columnNum) {
		ExcelDriver driver = new ExcelDriver(path, sheet);
		int exeNum = driver.getRowNumber(flag,columnNum);
		if (exeNum == 0) {
			return null;
		}
		return data(path, sheet, exeNum, false);
	}

	private static Map data(String path, String sheet, int exeNum, boolean runFlag) {
		if (path == null) {
			return null;
		}
		ExcelDriver driver = new ExcelDriver(path, sheet);
		int row = exeNum;
		if (runFlag) {
			row = driver.getTotal().get(exeNum - 1);
		}
		return driver.readRow(row);
	}
}
