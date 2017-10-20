package com.Testng.run; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author ex-renqing111
 * @version 创建时间：2017年6月2日 上午11:04:26 类说明
 */
public class GetTestJava {


	/**
	 * 获取指定目录下的所有的java文件（不包括文件夹），采用了递归
	 * 
	 * */
	public static ArrayList<File> getJavaFile(Object obj) {
		File directory = null;
		String fileName;
		if (obj instanceof File) {
			directory = (File) obj;
		} else {
			directory = new File(obj.toString());
		}
		ArrayList<File> files = new ArrayList<File>();
		fileName = directory.getName();
		if (directory.isFile() && fileName.contains(".java")) {
			files.add(directory);
			return files;
		} else if (directory.isDirectory()) {
			File[] fileArr = directory.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File fileOne = fileArr[i];
				files.addAll(getJavaFile(fileOne));
			}
		}
		return files;
	}

	/**
	 * 过滤出带@test标签的java文件
	 * */
	public static ArrayList<File> getTestFile(ArrayList<File> files) {
		ArrayList<File> testfiles = new ArrayList<File>();
		BufferedReader reader = null;
		String lineString = null;
		for (File f : files) {
			try {
				reader = new BufferedReader(new FileReader(f));
				while ((lineString = reader.readLine()) != null) {
					if (lineString.contains("org.testng.annotations.Test")) {
						testfiles.add(f);
						break;
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return testfiles;
	}

	/**
	 * 
	 * 
	 * */

	public static void CreateXml(ArrayList<String> content, String filepath) throws IOException {

		// 创建Document实例
		Document document = DocumentHelper.createDocument();

		// 记录test名称
		// String xmltestname = new String();

		// 记录class名称
		String xmlclassname = new String();

		// 记录要保存的xml文件位置
		String xmlfilepath = new String();

		// 获取test名称
		// xmltestname = content.get(1).toString();

		// 获取class名称
		// xmlclassname = content.get(0).toString() + "." + content.get(1).toString();

		// 获取文件保存位置
		xmlfilepath = filepath + "testng" + ".xml";
		// System.out.println(filepath);

		// 创建根节点suite，并设置name属性为xmlsuitename
		Element root = document.addElement("suite").addAttribute("name", "Suite");


		// 创建节点test，并设置name、verbose属性
		Element test = root.addElement("test")

		// 记录日志信息的详细程度，有0-10个级别，0是没有，10是最详细，对输出的测试报告无影响
				.addAttribute("verbose", "2")

				// 控制@Test标识的测试用例执行顺序，默认是false，在节点下面的所有方法的执行顺序是无序的
				// 把它设为true以后就能保证在节点下的方法是按照顺序执行的。
				.addAttribute("preserve-order", "true").addAttribute("name", "Test");

		// 创建节点classes，无属性
		Element classes = test.addElement("classes");

		// 创建节点classs，并设置name属性
		for (int i = 0; i < content.size(); i++) {
			Element classs = classes.addElement("class").addAttribute("name", content.get(i));
		}
		// Element classs = classes.addElement("class").addAttribute("name", xmlclassname);
		// 创建节点methods，无属性
		// Element methods = classs.addElement("methods");

		// 创建节点classs，并设置name属性
		// for (int i = 2; i < content.size(); i++) {
		// @SuppressWarnings("unused")
		// Element include = methods.addElement("include").addAttribute("name", content.get(i).toString());
		// }

		// 设置DocType
		// 第一个参数：名称
		// 第二个参数：PUBLIC URI
		// 第三个参数：SYSTEM URI
		document.addDocType("suite", null, "http://testng.org/testng-1.0.dtd");

		// 输出格式设置
		OutputFormat format = OutputFormat.createPrettyPrint();
		format = OutputFormat.createCompactFormat();

		// 设置输出编码
		format.setEncoding("UTF-8");
		format.setIndent(true); // 设置是否缩进
		format.setIndent("   "); // 以空格方式实现缩进
		format.setNewlines(true); // 设置是否换行
		// 创建XML文件
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(xmlfilepath), format.getEncoding()), format);
		writer.write(document);
		writer.close();
		document = null;

		// // 记录xml文件路径
		// setxmlpath(xmlfilepath);
	}

}
 