package com.Testng.run; 

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2017年6月2日 上午11:29:35 
 * 类说明 
 */
public class Test001 {
	public static void main(String args[]) {
		ArrayList<File> files = GetTestJava.getJavaFile("D:\\workspace-ta\\AT\\AutoTest\\src\\main\\java");
		ArrayList<String> pageNames = new ArrayList<String>();
		files = GetTestJava.getTestFile(files);
		String path = null;
		String className;
		for (File f : files) {
			className = f.getName();
			if (!className.contains("GetTestJava")) {
				System.out.println("file-className:" + className);
				path = f.getAbsolutePath();
				path = path.substring(0, path.length() - 5);
				path = path.replace("\\", ".");
				path = path.split("java.")[1];
				// System.out.println("file-path:" + path);
				pageNames.add(path);
			}
		}
		try {
			GetTestJava.CreateXml(pageNames, "D:\\workspace-ta\\AT\\AutoTest\\");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
 