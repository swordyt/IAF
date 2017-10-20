package com.yinting.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.yinting.core.Log;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class ShellClient {
	static private ShellClient instance;
	private String ip;
	private int port;
	private String userName;
	private String password;
	private String charset = Charset.defaultCharset().toString();
	private Connection conn;
	private SCPClient client;
	private StringBuilder outBuilder = null;// 存放shell执行后输出的全部信息
	private StringBuilder errBuilder = null;// 存放shell执行后错误输出信息
	private Integer timeOut = 60000;// 等待延迟时间

	public Integer getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Connection getConn() {
		return conn;
	}

	/**
	 * 获取输出错误流信息
	 */
	public StringBuilder getStderr() {
		return errBuilder;
	}

	/**
	 * 获取输出流信息
	 */
	public StringBuilder getStdout() {
		return outBuilder;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private ShellClient(String IP, String port, String username, String password, String charset) {
		this.ip = IP;
		this.port = Integer.parseInt(port);
		this.userName = username;
		this.password = password;
		this.charset = charset;
	}

	static synchronized public ShellClient getInstance(String ip, String port, String username, String passward,
			String charset) {
		Log.log("Shell服务初始化开始：");
		Log.log("ip:" + ip);
		Log.log("port:" + port);
		Log.log("username:" + username);
		Log.log("passward:" + passward);
		if (instance == null) {
			instance = new ShellClient(ip, port, username, passward, charset);
		}
		
		return instance.login()==true?instance:null;
	}

	/**
	 * 设置默认编码方式为utf-8
	 */
	static synchronized public ShellClient getInstance(String ip, String port, String username, String passward) {
		return getInstance(ip, port, username, passward, "utf-8");

	}

	static public ShellClient getInstance() {
		return getInstance("maiziyunweb_"+System.getProperty("ENV"));

	}

	/**
	 * 根据env中配置服务名称自动获取配置信息，如：maiziyunweb_dev.ip、maiziyunweb_dev.port，传入maiziyunweb_dev即可。
	 * 程序将自动获取**.ip/.port/.name/.password
	 */
	static public ShellClient getInstance(String serverName) {
		return getInstance(System.getProperty(serverName + ".ip"), System.getProperty(serverName + ".port"),
				System.getProperty(serverName + ".username"), System.getProperty(serverName + ".password"));
	}

	/**
	 * 登录远端服务器，true：登录成功，false：登录失败。
	 */
	public boolean login() {
		conn = new Connection(ip, port);
		try {
			conn.connect();
			return conn.authenticateWithPassword(userName, password);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取远端文件
	 */
	public void getFile(String remoteFile, String localTargetDirectory) {
		client = new SCPClient(conn);
		try {
			client.get(remoteFile, localTargetDirectory);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 上传文件
	 */
	public void putFile(String localFile, String remoteTargetDirectory) {
		client = new SCPClient(conn);
		try {
			client.put(localFile, remoteTargetDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void putFile(String localFile, String remoteFileName, String remoteTargetDirectory, String mode) {
		Connection conn = new Connection(ip, port);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(userName, password);
			if (isAuthenticated == false) {
				Log.log("authentication failed");
			}
			SCPClient client = new SCPClient(conn);
			if ((mode == null) || (mode.length() == 0)) {
				mode = "0600";
			}
			client.put(localFile, remoteFileName, remoteTargetDirectory, mode);

			// 重命名
			ch.ethz.ssh2.Session sess = conn.openSession();
			String tmpPathName = remoteTargetDirectory + File.separator + remoteFileName;
			String newPathName = tmpPathName.substring(0, tmpPathName.lastIndexOf("."));
			sess.execCommand("mv " + remoteFileName + " " + newPathName);// 重命名回来

			conn.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// public void putFile(String localFile, String remoteFileName,String
	// remoteTargetDirectory) {
	// Connection conn = new Connection(ip,port);
	// try {
	// conn.connect();
	// boolean isAuthenticated = conn.authenticateWithPassword(username,
	// password);
	// if (isAuthenticated == false) {
	// System.err.println("authentication failed");
	// }
	// SCPClient client = new SCPClient(conn);
	// client.put(getBytes(localFile), remoteFileName, remoteTargetDirectory);
	// conn.close();
	// } catch (IOException ex) {
	// Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
	// }
	// }
	/**
	 * 读取指定文件
	 */
	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream(1024 * 1024);
			byte[] b = new byte[1024 * 1024];
			int i;
			while ((i = fis.read(b)) != -1) {
				byteArray.write(b, 0, i);
			}
			fis.close();
			byteArray.close();
			buffer = byteArray.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 格式化文件内容为list 忽略第一行内容
	 */
	public List<Map<String, String>> formatFileToList(String filePath) {
		// Scpclient scp = Scpclient.getInstance("10.30.199.71", "22", "log",
		// "Devopslog2017");
		// scp.getFile("/opt/files/fund/product/confirmorder/yingmi_1063_confirm_20170818.txt",
		// "E:\\Desktop");
		// File file=new File("E:\\Desktop\\yingmi_1063_confirm_20170818.txt");
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String lines = null;
		List<String> columnName = new ArrayList<String>();
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();// 解析后的所有数据
		int number = 1;// 记录当前行
		try {
			while ((lines = buffer.readLine()) != null) {
				if (number == 1) {
					number++;
					continue;
				}
				String[] keys = lines.split("\\|");
				if (number == 2) {// 如果是第二列就保存作为列明
					Collections.addAll(columnName, keys);
					number++;
					continue;
				}
				number++;
				Map<String, String> rowMap = new HashMap<String, String>();
				for (int i = 0; i < columnName.size(); i++) {
					if (keys.length > i) {// values值存在的情况
						if (keys[i].equals("")) {
							rowMap.put(columnName.get(i), "null");
							continue;
						} else {
							rowMap.put(columnName.get(i), keys[i]);
						}
					} else {// values不存在
						rowMap.put(columnName.get(i), "null");
					}
					rows.add(rowMap);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		for (int i = 0; i < rows.size(); i++) {
			Map row = rows.get(i);
			Iterator<String> it = row.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				System.out.print(key + "=" + row.get(key));
			}
			System.out.println();
		}
		return rows;
	}

	/**
	 * 获取远端文件并转为list。 忽略第一行内容
	 */
	public static List<Map<String, String>> getContent(String serverName, String remoteFile) {
		ShellClient client = ShellClient.getInstance(serverName);
		client.getFile(remoteFile, System.getProperty("tmp"));
		return client.formatFileToList(System.getProperty("tmp"));
	}

	/**
	 * 根据指定格式化字符，将指定inputstream数据返回
	 */
	public StringBuilder processStd(InputStream in, String charset) {
		StringBuilder builder = new StringBuilder();
		BufferedReader stdoutReader = null;
		try {
			stdoutReader = new BufferedReader(new InputStreamReader(in, charset));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		System.out.println("Here is the output from stdout:");

		while (true) {
			String line = null;
			try {
				line = stdoutReader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (line == null) {
				break;
			}
			Log.log(line);
			builder.append(line);
		}
		return builder;
	}

	/**
	 * 执行远端shell命令，并返回执行结果
	 */
	public ShellClient exec(String cmds) {
		try {
			if (this.login()) {
				Session session = conn.openSession();
				session.requestPTY("bash");
				session.startShell();
				InputStream stdout = new StreamGobbler(session.getStdout());
				InputStream stderr = new StreamGobbler(session.getStderr());
				PrintWriter out = new PrintWriter(session.getStdin());
				out.println(cmds);
				out.println("exit");
				out.close();
				session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS,
						this.timeOut);
				// session.execCommand(cmds);
				// in = session.getStdout();// 获取输出流
				outBuilder = this.processStd(stdout, this.charset);// 将输入流返回信息格式化输出
				errBuilder = this.processStd(stderr, this.charset);// 将输入流返回信息格式化输出
				Log.log("ExitCode: " + session.getExitStatus());
				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 根据盈米返回的原始字符串，以“_httpContent=”和“,
	 * _httpStatusCode=”作为分割，取其中字符串作为响应json。盈米原始字符串如下：
	 * {ETag=W/"ae9-uczI+obPQYfRuUU1Nh3p6KNfNBE", Vary=Accept-Encoding,
	 * Access-Control-Allow-Origin=*, Transfer-Encoding=chunked, Date=Fri, 15
	 * Sep 2017 02:30:54 GMT, _httpContent=
	 * {"poCode":"ZH006358","poName":"安心动盈","poDesc":"“安心动盈”由盈米与天弘基金共同推荐，组合中80%配置于债券资产，20%随市场变化进行资产轮动，二者偏离度超过10%进行再平衡，回到80/20的比例。","poRichDesc":"80%投资于债券资产，20%投资于“二八轮动”组合。随着市场波动，80/20的股债配比会在偏离度大于10%时“再平衡”。\n\n“二八轮动”主要在沪深300指数与中证500指数间切换，也考虑到两者都不乐观的情况－投资于货基来躲避A股的趋势性大跌。“二八轮动”调仓逻辑主要参考过去20个交易日的收益情况，为避免调仓过于频繁，两次调仓之间限制不少于10个交易日。\n\n“安心动盈“结合了”二八轮动“与”20/80股债再平衡“。“二八轮动”在趋势行情中有着绝对的优势，善于在市场的上涨、下跌中抓住机会；而“20/80股债再平衡”则通过有效的被动管理方式做资产配置，有分散、降低组合风险的优势。本组合汲取了两者各自的优势，使得组合拥有整体资产配置，小部分资产轮动的效果，是一款“稳健的二八轮动”，也是“活跃的20/80”；但最关键的是1份风险换2.49倍左右收益，达到了比前两个组合“性价比”更高的效果。","poStatus":"1","establishedOn":"2015-01-22","ceasedOn":null,"ceasedComment":"NULL","poManagers":[{"poManagerId":30,"poManagerName":"盈米","poManagerDesc":"","poManagerRichDesc":"","poManagerAvatarUrl":null,"verified":true}],"nav":"1.0080","navDate":"2017-09-14","dailyReturn":"0.0000","weeklyReturn":"0.0010","monthlyReturn":"0.0004","quarterlyReturn":null,"halfYearlyReturn":null,"yearlyReturn":null,"fromSetupReturn":"0.0080","maxDrawdown":"0.0241","sharpe":null,"volatility":null,"annualCompoundedReturn":null,"amacRisk5Level":3,"canBuy":true,"canRedeem":true,"cannotBuyReason":null,"cannotRedeemReason":null,"personalHighestBuyAmount":"10000000.00","personalLowestBuyAmount":"1000.00","composition":[{"prodType":"1","prodCode":"000509","prodName":"广发钱袋子货币","percent":"0.7597","nav":"1.0000","navDate":"2017-09-14","fundType":"4","riskLevel":1,"amacRisk5Level":1,"yearlyRoe":"0.04450","unitYield":"1.14150","dailyReturn":"0.0000"},{"prodType":"1","prodCode":"002295","prodName":"广发稳安保本","percent":"0.2403","nav":"1.0430","navDate":"2017-09-14","fundType":"5","riskLevel":2,"amacRisk5Level":2,"yearlyRoe":null,"unitYield":null,"dailyReturn":"0.0000"}],"adjustInfo":{"adjustmentId":1531,"comment":"ForTesting","adjustedOn":"2017-08-25","details":[{"adjustmentId":1531,"prodCode":"000509","prodType":1,"percent":0.76},{"adjustmentId":1531,"prodCode":"002295","prodType":1,"percent":0.24}]},"riskLevel":2}
	 * , _httpStatusCode=200, X-Request-Id=1505442654.700-172.19.9.3-209118452,
	 * Connection=keep-alive, Content-Type=application/json; charset=utf-8,
	 * Server=nginx/1.8.0}
	 */
	public String formatYingMiJson(String content) {
		return jsonFormat(content, "_httpContent=", ", _httpStatusCode=");
	}

	/**
	 * 根据json.sh返回的原始字符串，以“===start===”和“===end===”作为分割，取其中字符串作为响应json,json.sh脚本返回原始字符串如下：
	 * ===start=== {ETag=W/"ae9-uczI+obPQYfRuUU1Nh3p6KNfNBE",
	 * Vary=Accept-Encoding, Access-Control-Allow-Origin=*,
	 * Transfer-Encoding=chunked, Date=Fri, 15 Sep 2017 02:30:54 GMT,
	 * _httpContent={"poCode":"ZH006358","poName":"安心动盈","poDesc":"“安心动盈”由盈米与天弘基金共同推荐，组合中80%配置于债券资产，20%随市场变化进行资产轮动，二者偏离度超过10%进行再平衡，回到80/20的比例。","poRichDesc":"80%投资于债券资产，20%投资于“二八轮动”组合。随着市场波动，80/20的股债配比会在偏离度大于10%时“再平衡”。\n\n“二八轮动”主要在沪深300指数与中证500指数间切换，也考虑到两者都不乐观的情况－投资于货基来躲避A股的趋势性大跌。“二八轮动”调仓逻辑主要参考过去20个交易日的收益情况，为避免调仓过于频繁，两次调仓之间限制不少于10个交易日。\n\n“安心动盈“结合了”二八轮动“与”20/80股债再平衡“。“二八轮动”在趋势行情中有着绝对的优势，善于在市场的上涨、下跌中抓住机会；而“20/80股债再平衡”则通过有效的被动管理方式做资产配置，有分散、降低组合风险的优势。本组合汲取了两者各自的优势，使得组合拥有整体资产配置，小部分资产轮动的效果，是一款“稳健的二八轮动”，也是“活跃的20/80”；但最关键的是1份风险换2.49倍左右收益，达到了比前两个组合“性价比”更高的效果。","poStatus":"1","establishedOn":"2015-01-22","ceasedOn":null,"ceasedComment":"NULL","poManagers":[{"poManagerId":30,"poManagerName":"盈米","poManagerDesc":"","poManagerRichDesc":"","poManagerAvatarUrl":null,"verified":true}],"nav":"1.0080","navDate":"2017-09-14","dailyReturn":"0.0000","weeklyReturn":"0.0010","monthlyReturn":"0.0004","quarterlyReturn":null,"halfYearlyReturn":null,"yearlyReturn":null,"fromSetupReturn":"0.0080","maxDrawdown":"0.0241","sharpe":null,"volatility":null,"annualCompoundedReturn":null,"amacRisk5Level":3,"canBuy":true,"canRedeem":true,"cannotBuyReason":null,"cannotRedeemReason":null,"personalHighestBuyAmount":"10000000.00","personalLowestBuyAmount":"1000.00","composition":[{"prodType":"1","prodCode":"000509","prodName":"广发钱袋子货币","percent":"0.7597","nav":"1.0000","navDate":"2017-09-14","fundType":"4","riskLevel":1,"amacRisk5Level":1,"yearlyRoe":"0.04450","unitYield":"1.14150","dailyReturn":"0.0000"},{"prodType":"1","prodCode":"002295","prodName":"广发稳安保本","percent":"0.2403","nav":"1.0430","navDate":"2017-09-14","fundType":"5","riskLevel":2,"amacRisk5Level":2,"yearlyRoe":null,"unitYield":null,"dailyReturn":"0.0000"}],"adjustInfo":{"adjustmentId":1531,"comment":"ForTesting","adjustedOn":"2017-08-25","details":[{"adjustmentId":1531,"prodCode":"000509","prodType":1,"percent":0.76},{"adjustmentId":1531,"prodCode":"002295","prodType":1,"percent":0.24}]},"riskLevel":2},
	 * _httpStatusCode=200, X-Request-Id=1505442654.700-172.19.9.3-209118452,
	 * Connection=keep-alive, Content-Type=application/json; charset=utf-8,
	 * Server=nginx/1.8.0} ===end===
	 */
	public String formatNonobankJson(String content) {
		return jsonFormat(content, "===start===", "===end===");
	}

	/**
	 * 获取两个字符串之间的字符串 begin:content中第一次出现的位置 end:content中最后一次出现的位置
	 */
	public String jsonFormat(String content, String begin, String end) {
		return content.substring(content.indexOf(begin) + begin.length(), content.lastIndexOf(end));
	}
	public String jsonFormat(String content, String begin, String end,int offset) {
		Integer beginIndex=0;
		Integer endIndex=0;
		for(int i=0;i<offset;i++){
			beginIndex=content.indexOf(begin,beginIndex)+begin.length();
			endIndex=content.indexOf(end,endIndex+end.length());
		}
		return content.substring(beginIndex,endIndex);
	}

	/**
	 * 退出远端服务器
	 */
	public void close() {
		conn.close();
	}

}
