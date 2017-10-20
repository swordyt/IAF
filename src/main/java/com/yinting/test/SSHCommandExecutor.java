package com.yinting.test;
import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import java.util.Vector;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Buffer;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;  
  
/** 
 * This class provide interface to execute command on remote Linux. 
 */  
  
public class SSHCommandExecutor {  
    private String ipAddress;  
  
    private String username;  
  
    private String password;  
  
    public static final int DEFAULT_SSH_PORT = 22;  
  
    private Vector<String> stdout;  
  
    public SSHCommandExecutor(final String ipAddress, final String username, final String password) {  
        this.ipAddress = ipAddress;  
        this.username = username;  
        this.password = password;  
        stdout = new Vector<String>();  
    }  
  
    public int execute(final String command) {  
        int returnCode = 0;  
        JSch jsch = new JSch();  
        MyUserInfo userInfo = new MyUserInfo();  
  
        try {  
            // Create and connect session.  
            Session session = jsch.getSession(username, ipAddress, DEFAULT_SSH_PORT);  
            session.setPassword(password);  
            session.setUserInfo(userInfo);  
//            Buffer buffer=session.read(arg0)
            session.connect();  
  
            // Create and connect channel.  
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);  
  
            channel.setInputStream(null);  
            BufferedReader input = new BufferedReader(new InputStreamReader(channel  
                    .getInputStream()));  
  
            channel.connect();  
            System.out.println("The remote command is: " + command);  
  
            // Get the output of remote command.  
            String line;  
            while ((line = input.readLine()) != null) {  
                stdout.add(line);  
            }  
            input.close();  
  
            // Get the return code only after the channel is closed.  
            if (channel.isClosed()) {  
                returnCode = channel.getExitStatus();  
            }  
  
            // Disconnect the channel and session.  
            channel.disconnect();  
            session.disconnect();  
        } catch (JSchException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return returnCode;  
    }  
  
    public Vector<String> getStandardOutput() {  
        return stdout;  
    }  
  
    public static void main(final String [] args) {  
    	String logFile="mgw-info.2017-09-12.0.log";
    	String keyword="1000009016";
        SSHCommandExecutor sshExecutor = new SSHCommandExecutor("10.30.199.116", "log", "Devopslog2017");  
//        sshExecutor.execute("uname -s -r -v");  
//        sshExecutor.execute("awk 'BEGIN {num=0;} /"+keyword+"/{num=NR} END {print num}' "+logFile); 
        sshExecutor.execute("cat /opt/logs/mgw/mgw-info.2017-09-12.0.log"); 
//        sshExecutor.execute("/opt/logs/mgw/json.sh 1000009016 mgw-info.2017-09-12.0.log");  
        Vector<String> stdout = sshExecutor.getStandardOutput();  
        for (String str : stdout) {  
            System.out.println(str);  
        }  
    }  
}  