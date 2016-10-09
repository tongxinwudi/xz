package com.forestry.service.sys.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3FileHandle;
import com.forestry.dao.sys.ErrorLogDao;
import com.forestry.model.sys.AppLog;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.ErrorLog;
import com.forestry.model.sys.Mail;
import com.forestry.model.sys.MailContent;
import com.forestry.service.sys.impl.ErrorLogServiceImpl;
import com.forestry.service.sys.ErrorLogService;

import core.service.BaseService;
import core.util.DateUtil;
import core.util.MailUtil;

@Service
@Scope("singleton")
public class ErrorLogServiceImpl extends BaseService<ErrorLog> implements
		ErrorLogService {
	private ErrorLogDao errorLogDao;
	//private Connection linuxConn;
	
	@Value("#{errorLogConfig['sshIP']}")
	private String IP = "103.10.84.115";
	@Value("#{errorLogConfig['sshUsername']}")
	private String USERNAME = "qianjing";
	@Value("#{errorLogConfig['sshPassword']}")
	private String PASSWORD = "gaohui";
	@Value("#{errorLogConfig['sshKeyFile']}")
	private String sshKeyFile;
	@Value("#{errorLogConfig['phpLogPath']}")
	private String PHPLogPath;// = "D:/";
	@Value("#{errorLogConfig['dbLogPath']}")
	private String DBLogPath;// = "D:/php_errors.log";
	@Value("#{errorLogConfig['ecodeUnmail']}")
	private String ecodeUnmail;
	
	private final File keyfile = new File("E:/unix_key/115publickey-open");
	private long phpoffset = 0;
	private long dboffset = 0;
	private final String offsetPath = ErrorLogServiceImpl.class.getResource("/").getPath();
	private final String phpoffsetFile = offsetPath + "php_offset.txt";
	private final String dboffsetFile = offsetPath + "db_offset.txt";
	
	@Value("#{errorLogConfig['mailTo']}")
	private String[] receiver;// = new String[] { "gaohui@qianjing.com", "gaohui7141936@163.com" };
	@Value("#{errorLogConfig['mailCc']}")
	private String[] chaosong;// = new String[] {};
	private int reading = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

	@Resource
	public void setErrorLogDao(ErrorLogDao dao) {
		this.errorLogDao = dao;
		super.dao = dao;
	}

	@Override
	// @Scheduled(cron="0 10 20 * * ?") // 5秒fixedRate = 5000,
	public void readLog() {
		try {
			Connection linuxConn = new Connection(IP);
			linuxConn.connect();
			if (linuxConn
					.authenticateWithPublicKey(USERNAME, keyfile, PASSWORD)) {
				SFTPv3Client client = new SFTPv3Client(linuxConn);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
				String today = sdf1.format(new Date()) + "T";
				String now = sdf2.format(new Date());
				String logName = "user-sys-" + sdf.format(new Date()) + ".log";
				SFTPv3FileHandle handle = client.openFileRO(PHPLogPath
						+ logName);
				if (phpoffset == 0) {
					File file = new File(phpoffsetFile);
					if (file.exists()) {
						FileReader reader = new FileReader(phpoffsetFile);
						char[] ttt = new char[100];
						if (reader.read(ttt, 0, 100) > -1) {
							phpoffset += new Long(new String(ttt).trim());
						}
						reader.close();
						System.out.println("phpoffset=" + phpoffset);
					}
				}
				if (reading != Calendar.getInstance()
						.get(Calendar.DAY_OF_MONTH)) {
					phpoffset = 0;
					reading = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				}
				// System.out.println(reading+"---"+phpoffset);
				FileWriter writer = null;
				byte[] dst = new byte[32768];
				// 获得变化部分的
				int readLen = client.read(handle, phpoffset, dst, 0, 32768);
				if (readLen > -1) {
					phpoffset += readLen;
					String logStr = new String(dst, "UTF-8").trim();
					String regStr = today
							+ "(0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})\\+08:00 \\[UserSysLogger\\] ";
					// System.out.println(temp);
					String[] logs = logStr.split(regStr);
					// String[] logs = temp.split(regStr);
					Map<String, String> mailMap = new LinkedHashMap<String, String>();
					int errorIndex = 0;
					String mailMsg = "";
					int len = logs[0].length();
					for (int i = 0; i < logs.length; i++) {
						if (logs[i].startsWith("bound ")
								&& logs[i].contains(",ecode=")
								&& logs[i].contains(",emsg=")) { // logs[i].startsWith("bound ")&&
							if (logs[i].contains(",ecode=0")
									|| logs[i].contains(",ecode=ETS-5BP0000")) {
								if (i > 0) {
									len += 42 + logs[i].length();
								}
								continue;
							}
							ErrorLog error = new ErrorLog();
							// System.out.println(logs[i].trim());

							String[] attes = logs[i].split(",");
							String errorTime = logStr.substring(len, len + 19)
									.replace("T", " ");
							error.setError_time(errorTime);
							for (String att : attes) {
								String[] map = att.trim().split("=");
								if (map.length == 2) {
									if (map[0].equals("uid"))
										error.setUid(map[1]);
									else if (map[0].equals("mobile"))
										error.setMobile(map[1]);
									else if (map[0].equals("capitalmode"))
										error.setCapitalmode(map[1]);
									else if (map[0].equals("card")
											|| map[0].equals("bankcard"))
										error.setCard(map[1]);
									else if (map[0].equals("ecode"))
										error.setEcode(map[1]);
									else if (map[0].equals("emsg"))
										error.setEmsg(map[1]);
									else if (map[0].equals("from"))
										error.setFrom_page(map[1]);
									// else if (map[0].equals("hengsheng_res"))
									// error.setExtra(map[1]);
									else if (map[0].equals("type"))
										error.setType(map[1]);
								}
							}
							error.setCreate_time(new Date());
							if (!ecodeUnmail.contains(error
									.getEcode() + ",")) {
								mailMsg += "<table width='90%'><tr><td style='width:100px; text-align: right;'>问题编号：</td><td>"
										+ ++errorIndex
										+ "</td></tr>"
										+ "<tr><td style='width:100px; text-align: right;'>UID：</td><td>"
										+ error.getUid()
										+ "</td></tr>"
										+ "<tr><td style='width:100px; text-align: right;'>手机：</td><td>"
										+ error.getMobile()
										+ "</td></tr>"
										+ "<tr><td style='width:100px; text-align: right;'>发生时间：</td><td>"
										+ error.getError_time()
										+ "</td></tr>"
										+ "<tr><td style='width:100px; text-align: right;'>错误码：</td><td>"
										+ error.getEcode()
										+ "</td></tr>"
										+ "<tr><td style='width:100px; text-align: right;'>错误信息：</td><td>"
										+ error.getEmsg()
										+ "</td></tr>"
										+ "<tr><td style='width:100px; text-align: right;'>错误来源：</td><td>"
										+ error.getFrom_page()
										+ "</td></tr></table>";
								mailMsg += "<br/><br/>";
							}
							errorLogDao.persist(error);
						}
						if (i > 0) {
							len += 42 + logs[i].length();
						}
					}
					if (mailMsg != null && mailMsg.length() > 0) {
						mailMsg = "PHP/恒生错误：<br/><br/>" + mailMsg;
						mailMap.put("mailContent", mailMsg);
						Mail mailModel = Mail.MailErrorLog;
						mailModel.subject = "绑卡异常" + DateUtil.currentTime();
						MailContent content = new MailContent();
						content.setContentMap(mailMap);
						mailModel.setContent(content);
						MailUtil.send(mailModel, receiver, null, null);
						System.out.println("sended");
					}

					dst = new byte[32768];
					writer = new FileWriter(phpoffsetFile, false);
					writer.write("" + phpoffset);
					writer.flush();
					writer.close();
				}

				client.closeFile(handle);
				handle = client.openFileRO(DBLogPath);

				if (dboffset == 0) {
					File file = new File(dboffsetFile);
					if (file.exists()) {
						FileReader reader1 = new FileReader(dboffsetFile);
						char[] db = new char[100];
						if (reader1.read(db, 0, 100) > -1) {
							dboffset += new Long(new String(db).trim());
							reader1.close();
						}
						System.out.println("dboffset=" + dboffset);
					}
				}
				readLen = client.read(handle, dboffset, dst, 0, 32768);
				if (readLen > -1) {
					dboffset += readLen;
					String logStr = new String(dst, "UTF-8");
					// logStr="[15-Sep-2015 21:30:31 PRC] PHP Fatal error:  Call to a member function set_state() on a non-object in /home/q/share/pear/qianjing/qjfund/lib/QLibAssets.php on line 1038"+
					// "[15-Sep-2015 21:30:45 PRC] PHP Fatal error:  Call to a member function set_state() on a non-object in /home/q/share/pear/qianjing/qjfund/lib/QLibAssets.php on line 1038";
					// 匹配[18-Jul-2015 04:05:02 ，注意空格
					String regStr = "\\[" + now + " (0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1}) ";
					String[] logs = logStr.split(regStr);
					Map<String, String> mailMap = new LinkedHashMap<String, String>();
					int errorIndex = 0;
					String mailMsg = "";
					int len = logs[0].length();
					// System.out.println("sqlerrorlen="+logs.length);
					for (int i = 0; i < logs.length; i++) {

						if (logs[i].startsWith("PRC] PHP Fatal error:")) {
							// if (logs[i].startsWith("UTC] PHP Warning:")) {

							ErrorLog error = new ErrorLog();
							error.setEmsg(logs[i].substring(21).trim());// 17
							error.setType("db");
							error.setCreate_time(new Date());
							String errorTime = logStr.substring(len + 1,
									len + 21);
							error.setError_time(errorTime);
							mailMsg += "问题" + ++errorIndex + "："
									+ error.getError_time() + ","
									+ error.getEmsg() + "<br/><br/>";
							errorLogDao.persist(error);
						}
						if (i > 0) {
							len += 22 + logs[i].length();
						}
					}
					if (mailMsg != null && mailMsg.length() > 0) {
						mailMap.put("mailContent", mailMsg);
						Mail mailModel = Mail.MailErrorLog;
						mailModel.subject = "数据库异常" + DateUtil.currentTime();
						MailContent content = new MailContent();
						content.setContentMap(mailMap);
						mailModel.setContent(content);
						MailUtil.send(mailModel, receiver, null, null);
					}
					dst = new byte[32768];
					writer = new FileWriter(dboffsetFile, false);
					writer.write("" + dboffset);
					writer.flush();
					writer.close();
				}

				client.closeFile(handle);
				client.close();
				linuxConn.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readLogDirect() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String logName = "user-sys-" + sdf.format(new Date()) + ".log";
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
			String today = sdf1.format(new Date()) + "T";
			String now = sdf2.format(new Date());

			if (phpoffset == 0) {
				File file = new File(phpoffsetFile);
				if (file.exists()) {
					FileReader reader = new FileReader(phpoffsetFile);
					char[] ttt = new char[100];
					if (reader.read(ttt, 0, 100) > -1) {
						phpoffset += new Long(new String(ttt).trim());
					}
					reader.close();
				}
				file=null;
			}
			if (reading != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
				phpoffset = 0;
				reading = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			}
			
			File PHPLogFile = new File(PHPLogPath + logName);
			RandomAccessFile rf = new RandomAccessFile(PHPLogFile,"r"); 
			FileWriter writer = null;
			// 获得变化部分的
			String tmp="";
			rf.seek(phpoffset);
			String mailMsg = "";
			Map<String, String> mailMap = new LinkedHashMap<String, String>();
			int errorIndex = 0;
			while((tmp=rf.readLine())!=null){				
				//phpoffset += readLen;
				String logStr =new String(tmp.getBytes("ISO8859-1"),"UTF-8").trim();// new String(dst, "UTF-8").trim();
				//if(!logStr.startsWith(today)||logStr.length()<19||!logStr.startsWith("+08:00 [UserSysLogger]", 19))
				//	alog.append(logStr);
				//else{
				//	alog=new StringBuilder(logStr);
				String regStr = today + "(0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})\\+08:00 \\[UserSysLogger\\] ";
				String[] logs = logStr.split(regStr);				
				
				int len = logs[0].length();
				for (int i = 0; i < logs.length; i++) {
					System.out.println(logs[i]);
					if (logs[i].startsWith("bound ") && logs[i].contains(",ecode=") && logs[i].contains(",emsg=")) { // logs[i].startsWith("bound ")&&
						if (logs[i].contains(",ecode=0") || logs[i].contains(",ecode=ETS-5BP0000")) {
							if (i > 0) {
								len += 42 + logs[i].length();
							}
							continue;
						}
						
						ErrorLog error = new ErrorLog();
						String[] attes = logs[i].split(",");
						String errorTime = logStr.substring(len, len + 19).replace("T", " ");
						error.setError_time(errorTime);
						for (String att : attes) {
							String[] map = att.trim().split("=");
							if (map.length == 2) {
								if (map[0].equals("uid"))
									error.setUid(map[1]);
								else if (map[0].equals("mobile"))
									error.setMobile(map[1]);
								else if (map[0].equals("capitalmode"))
									error.setCapitalmode(map[1]);
								else if (map[0].equals("card") || map[0].equals("bankcard"))
									error.setCard(map[1]);
								else if (map[0].equals("ecode"))
									error.setEcode(map[1]);
								else if (map[0].equals("emsg"))
									error.setEmsg(map[1]);
								else if (map[0].equals("from"))
									error.setFrom_page(map[1]);
								// else if (map[0].equals("hengsheng_res"))
								// error.setExtra(map[1]);
								else if (map[0].equals("type"))
									error.setType(map[1]);
							}
						}
						error.setCreate_time(new Date());
						error.setIssend(0);
						errorLogDao.persist(error);
						/*
						//System.out.println(!Constant.ECODE_UNMAIL.contains(error.getEcode() + ","));
						if (!ecodeUnmail.contains(error.getEcode() + ",")) {
							mailMsg += "<table width='90%'><tr><td style='width:100px; text-align: right;'>问题编号：</td><td>"
									+ ++errorIndex + "</td></tr>"
									+ "<tr><td style='width:100px; text-align: right;'>UID：</td><td>" + error.getUid()
									+ "</td></tr>" + "<tr><td style='width:100px; text-align: right;'>手机：</td><td>"
									+ error.getMobile() + "</td></tr>"
									+ "<tr><td style='width:100px; text-align: right;'>银行卡号：</td><td>"
									+ error.getCard() + "</td></tr>"
									+ "<tr><td style='width:100px; text-align: right;'>发生时间：</td><td>"
									+ error.getError_time() + "</td></tr>"
									+ "<tr><td style='width:100px; text-align: right;'>错误码：</td><td>"
									+ error.getEcode() + "</td></tr>"
									+ "<tr><td style='width:100px; text-align: right;'>错误信息：</td><td>"
									+ error.getEmsg() + "</td></tr>"
									+ "<tr><td style='width:100px; text-align: right;'>错误来源：</td><td>"
									+ error.getFrom_page() + "</td></tr></table>";
							mailMsg += "<br/><br/>";
						}*/
					}
					if (i > 0) {
						len += 42 + logs[i].length();
					}
				}
			}			
			if(phpoffset!=rf.length()){
				phpoffset=rf.length();				
				writer = new FileWriter(phpoffsetFile, false);
				writer.write("" + phpoffset);
				writer.flush();
				writer.close();
			}
			rf.close();
			/*
			if (mailMsg != null && mailMsg.length() > 0) {
				mailMsg = "PHP/恒生错误：<br/><br/>" + mailMsg;
				mailMap.put("mailContent", mailMsg);
				Mail mailModel = Mail.MailErrorLog;
				mailModel.subject = "绑卡异常" + DateUtil.currentTime();
				MailContent content = new MailContent();
				content.setContentMap(mailMap);
				mailModel.setContent(content);
				MailUtil.send(mailModel, receiver, chaosong, null);
				System.out.println("sended");
			}*/
			
			if (dboffset == 0) {
				File file = new File(dboffsetFile);
				if (file.exists()) {
					FileReader reader1 = new FileReader(dboffsetFile);
					char[] db = new char[100];
					if (reader1.read(db, 0, 100) > -1) {
						dboffset += new Long(new String(db).trim());
						reader1.close();
					}
				}
				file=null;
			}
			
			File DBLogFile=new File(DBLogPath);
			if(!DBLogFile.exists())
				return;
			rf=new RandomAccessFile(DBLogFile,"r");
			rf.seek(dboffset);
			mailMap = new LinkedHashMap<String, String>();
			errorIndex = 0;
			mailMsg = "";
			while((tmp=rf.readLine())!=null){
				String logStr =new String(tmp.getBytes("ISO8859-1"),"UTF-8").trim();// new String(dst, "UTF-8").trim();
				// logStr="[15-Sep-2015 21:30:31 PRC] PHP Fatal error:  Call to a member function set_state() on a non-object in /home/q/share/pear/qianjing/qjfund/lib/QLibAssets.php on line 1038"+
				// "[15-Sep-2015 21:30:45 PRC] PHP Fatal error:  Call to a member function set_state() on a non-object in /home/q/share/pear/qianjing/qjfund/lib/QLibAssets.php on line 1038";
				// 匹配[18-Jul-2015 04:05:02 ，注意空格
				String regStr = "\\[" + now + " (0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1}) ";
				String[] logs = logStr.split(regStr);
				
				int len = logs[0].length();
				for (int i = 0; i < logs.length; i++) {
					System.out.println(logs[i]);
					if (logs[i].startsWith("PRC] PHP Fatal error:")) {
						// if (logs[i].startsWith("UTC] PHP Warning:")) {

						ErrorLog error = new ErrorLog();
						error.setEmsg(logs[i].substring(21).trim());// 17
						error.setType("db");
						error.setCreate_time(new Date());
						String errorTime = logStr.substring(len + 1,
								len + 21);
						error.setError_time(errorTime);
						error.setIssend(0);
						/*
						mailMsg += "问题" + ++errorIndex + "："
								+ error.getError_time() + ","
								+ error.getEmsg() + "<br/><br/>";
						*/
						errorLogDao.persist(error);
					}
					if (i > 0) {
						len += 22 + logs[i].length();
					}
				}
			}
			if(dboffset!=rf.length()){
				dboffset=rf.length();
				
				writer = new FileWriter(dboffsetFile, false);
				writer.write("" + dboffset);
				writer.flush();
				writer.close();
			}
			
			/*
			if (mailMsg != null && mailMsg.length() > 0) {
				mailMap.put("mailContent", mailMsg);
				Mail mailModel = Mail.MailErrorLog;
				mailModel.subject = "数据库异常" + DateUtil.currentTime();
				MailContent content = new MailContent();
				content.setContentMap(mailMap);
				mailModel.setContent(content);
				MailUtil.send(mailModel, receiver, chaosong, null);
			}*/
			
			rf.close();
			rf=null;
			writer=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 定时发送邮件
	 */
	@Override
	public void sendErrorMail(){
		List<ErrorLog> list=errorLogDao.getMailLogs();
		String ids="";
		int errorIndex=0;
		StringBuilder sb=new StringBuilder();
		if(list!=null&&list.size()>0){
			for(ErrorLog error : list){
				if(error.getType()!=null&&error.getType().equals("db")){
					sb.append("<table width='90%'>");
					sb.append("<tr><td style='width:100px; text-align: right;'>问题编号：</td><td>"+ ++errorIndex + "</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>发生时间：</td><td>"+ error.getError_time() + "</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>错误信息：</td><td>"+ error.getEmsg() + "</td></tr>");
					sb.append("</table><br/><br/>");
				}else{
					//if (!ecodeUnmail.contains(error.getEcode() + ",")) {
					sb.append("<table width='90%'>");
					sb.append("<tr><td style='width:100px; text-align: right;'>问题编号：</td><td>"+ ++errorIndex + "</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>UID：</td><td>" + error.getUid()+"</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>手机：</td><td>"+ error.getMobile() + "</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>银行卡号：</td><td>"+ error.getCard() + "</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>发生时间：</td><td>"+ error.getError_time() + "</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>错误码：</td><td>"+ error.getEcode() + "</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>错误信息：</td><td>"+ error.getEmsg() + "</td></tr>");
					sb.append("<tr><td style='width:100px; text-align: right;'>错误来源：</td><td>"+ error.getFrom_page() + "</td></tr>");
					sb.append("</table><br/><br/>");
					//}
				}
				ids+=error.getId()+",";
			}
			if (sb.length()>0) {				
				Map<String, String> mailMap = new LinkedHashMap<String, String>();
				mailMap.put("mailContent", sb.toString());
				Mail mailModel = Mail.MailErrorLog;
				mailModel.subject = "绑卡异常" + DateUtil.currentTime();
				MailContent content = new MailContent();
				content.setContentMap(mailMap);
				mailModel.setContent(content);
				MailUtil.send(mailModel, receiver, chaosong, null);
				errorLogDao.updateIsSend(ids.substring(0,ids.length()-1));
			}
		}
	}

	/**
	 * 返回日志数量
	 * 
	 * @param log
	 * @return
	 */
	@Override
	public long getLogsCount(ErrorLog log) {
		return errorLogDao.getLogsCount(log);
	}

	/**
	 * 返回日志列表
	 * 
	 * @param log
	 * @return
	 */
	@Override
	public List<ErrorLog> getLogs(ErrorLog log) {
		return errorLogDao.getLogs(log);
	}

	public static void main(String[] args) {
		// ErrorLogService service = new ErrorLogServiceImpl();
		// service.readLog();
	}
	/*
	 * String temp =
	 * "2015-09-15T20:09:54+08:00 [UserSysLogger] a user logined, uid=0020150305297406, mobile=13811845276, ucookie=HsNlkAWWi3jGMq6MD+uTt1UMOJXexY58+JG7hpWpF0DIemisFK/b6F8hwl/zvwOKkxdHWJJ79Bm5+WEsvWPLVQ==;bcookie=EDF2707A-BCFF-4821-B556-667294B9D721;tcookie=ViFoktkXFrQzoq9YEYHLIRqwM7BvnU0JUJslJUwjB+g=;software=zidonglicai-2.1.1;platform=iPhone-8.1.2;channel=0"
	 * +
	 * "2015-09-15T11:53:15+08:00 [UserSysLogger] bound sendAccountVerify, uid=0020150404385992,mobile=13311363650,card=,capitalmode=,ecode=ETS-5BP9981,emsg=Can not deserialize instance of java.util.LinkedHashMap out of START_ARRAY token"
	 * +
	 * "at [Source: java.io.StringReader@1b43a8b; line: 1, column: 1],from =Chinapay_Bank_Callback.app.php, hengsheng_res=ETS-5BP9981,type=hundsun-bound"
	 * +
	 * "2015-09-15T20:07:18+08:00 [UserSysLogger] a user logined, uid=0020150817646336, mobile=13426180624, ucookie=raUtSvLD0eeQVVNlR2yDdHuAXSiNwRC8eVpdXX6lWpujdevRbo35zn97pvHO4YZQlyFShJ2dE7sjbQPKsOru2w==;bcookie=352621061474273;tcookie=8ADUpgxjumQTsTSODG/ViAxGDDvGfA3vJPQamAIz0fk=;software=auto_financial-2.1.1;platform=android-4.4.2;channel=android_100"
	 * +
	 * "2015-09-15T20:08:03+08:00 [UserSysLogger] a user logined, uid=0020141223096145, mobile=13968202612, ucookie=KjxYYImpZG/B6aS2JEVENsuCmcpn6/AqIr0BuAK6hQipQZLo/DtjWeq263wWUCIfKxFG+gtIorPmsIqBmBWITQ==;bcookie=52C706FD-FF71-4606-8E6E-869946C3F22A;tcookie=;software=zidonglicai-2.1.1;platform=iPhone-8.3;channel=0"
	 * +
	 * "2015-09-15T20:08:13+08:00 [UserSysLogger] a user logined, uid=0020150911654187, mobile=13617585250, ucookie=jHjOiAK3Bu1quEMH9247F2Ufxj2q6inBdovYXd0Sd/Hvwzrn/8jKjW9+d0z+6wRwXiAY22vp989DxE++a2prEg==;bcookie=9A58200E-44F8-4CE2-95B8-07B709AC1474;tcookie=;software=zidonglicai-2.1.1;platform=iPhone-8.1.2;channel=0"
	 * +
	 * "2015-09-15T20:08:51+08:00 [UserSysLogger] a user logined, uid=0020150409394846, mobile=13952089669, ucookie=9jATitrr+eMFs/hy1mf9q8SHwR37Wi/HM6OEjJGZ9isS8QXMREzW1w1hGeDLgGMpe/Rz9VY1LVttg8GdgG41Zg==;bcookie=14613059-1777-43C4-9E92-A6EB42F391B4;tcookie=97Zi7WQ9vUzYhmr0KGe2cr70Jp8q0XzG5orovWx+89c=;software=zidonglicai-2.1.1;platform=iPhone-8.4.1;channel=0"
	 * +
	 * "2015-09-15T17:00:15+08:00 [UserSysLogger] bound sendAccountVerify, uid=0020150404385992,mobile=13311363650,card=,capitalmode=,ecode=ETS-5BP9981,emsg=他妈的错误"
	 * +
	 * "at [Source: java.io.StringReader@1b43a8b; line: 1, column: 1],from =Chinapay_Bank_Callback.app.php, hengsheng_res=ETS-5BP9981,type=hundsun-bound"
	 * ; public void readLog() { try { Connection linuxConn = new
	 * Connection(this.IP); linuxConn.connect(); if (linuxConn
	 * .authenticateWithPublicKey(USERNAME, keyfile, PASSWORD)) { final
	 * SFTPv3Client client = new SFTPv3Client(linuxConn); final SimpleDateFormat
	 * sdf = new SimpleDateFormat("yyyyMMdd"); final SimpleDateFormat sdf1=new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat sdf2=new
	 * SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH); final String
	 * today=sdf1.format(new Date())+"T"; final String now=sdf2.format(new
	 * Date()); final String logName = "user-sys-" + sdf.format(new Date()) +
	 * ".log"; final SFTPv3FileHandle handle = client.openFileRO(PHPLogPath +
	 * logName); final SFTPv3FileHandle handle1=client.openFileRO(DBLogPath);
	 * 
	 * final FileWriter writer = new FileWriter("php_offset.txt",false);
	 * writer.write("{logName:" + logName + ",offset:0}"); writer.flush(); final
	 * FileWriter writer_db = new FileWriter("db_offset.txt",false);
	 * writer_db.write("{logName:php_error.log" + ",offset:0}");
	 * writer_db.flush(); // 启动一个线程每10秒钟读取新增的日志信息 final ScheduledExecutorService
	 * exec = Executors .newScheduledThreadPool(1);
	 * exec.scheduleWithFixedDelay(new Runnable() { public void run() { byte[]
	 * dst = new byte[32768]; try { // 获得变化部分的 int readLen = client.read(handle,
	 * phpoffset, dst, 0, 32768); if (readLen > -1) { phpoffset += readLen;
	 * String logStr=new String(dst);
	 * 
	 * String regStr=today+
	 * "(0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})\\+08:00 \\[UserSysLogger\\] "
	 * ; String[] logs=logStr.split(regStr);
	 * 
	 * Map<String, String> mailMap = new LinkedHashMap<String, String>(); int
	 * errorIndex=1; System.out.println(logs.length); for(int
	 * i=0;i<logs.length;i++){
	 * if(logs[i].startsWith("bound ")&&logs[i].contains(
	 * ",ecode=")&&logs[i].contains(",emsg=")){ if(logs[i].contains(",ecode=0"))
	 * break; ErrorLog error=new ErrorLog(); System.out.println(logs[i].trim());
	 * String[] attes=logs[i].split(","); for(String att : attes){ String[]
	 * map=att.trim().split("="); if(map[0].equals("uid")) error.setUid(map[1]);
	 * if(map[0].equals("mobile")) error.setMobile(map[1]);
	 * if(map[0].equals("card")||map[0].equals("bankcard"))
	 * error.setCard(map[1]); if(map[0].equals("ecode")) error.setEcode(map[1]);
	 * if(map[0].equals("emsg")) error.setEmsg(map[1]);
	 * if(map[0].equals("from")) error.setFrom_page(map[1]);
	 * if(map[0].equals("hengsheng_res")) error.setExtra(map[1]);
	 * if(map[0].equals("type")) error.setType(map[1]); } String
	 * mailMsg=error.getUid()+"	"+error.getMobile()+"	"+error.getEcode()+"	"+
	 * error.getEmsg()+"	"+error.getFrom_page()+"\n";
	 * mailMap.put(""+errorIndex++, mailMsg);
	 * 
	 * errorLogDao.persist(error); } } if(mailMap.size()>0){ Mail mailModel =
	 * Mail.MailCF; mailModel.subject = "绑卡异常"+DateUtil.currentTime();
	 * MailContent content = new MailContent(); content.setContentMap(mailMap);
	 * mailModel.setContent(content); System.out.println("sended"); }
	 * 
	 * dst = new byte[32768]; writer.write(",\n{logName:" + logName + ",offset:"
	 * + phpoffset + "}"); writer.flush(); } readLen = client.read(handle1,
	 * dboffset, dst, 0,32768); if (readLen > -1) { dboffset += readLen; String
	 * logStr=new String(dst); //匹配[18-Jul-2015 04:05:02 ，注意空格 String
	 * regStr="\\["+now+" (0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1}) ";
	 * String[] logs=logStr.split(regStr);
	 * 
	 * Map<String, String> mailMap = new LinkedHashMap<String, String>(); int
	 * errorIndex=1; System.out.println("error="+logs.length); for(int
	 * i=0;i<logs.length;i++){
	 * //if(logs[i].startsWith("PRC] PHP Fatal error:")){
	 * if(logs[i].startsWith("UTC] PHP Warning:")){ ErrorLog error=new
	 * ErrorLog(); error.setEmsg(logs[i].substring(17));//21
	 * error.setType("db");
	 * 
	 * String mailMsg=error.getEmsg()+"\n"; mailMap.put(""+errorIndex++,
	 * mailMsg);
	 * 
	 * errorLogDao.persist(error); } } if(mailMap.size()>0){
	 * System.out.println(mailMap); Mail mailModel = Mail.MailCF;
	 * mailModel.subject = "数据库异常"+DateUtil.currentTime(); MailContent content =
	 * new MailContent(); content.setContentMap(mailMap);
	 * mailModel.setContent(content); } dst = new byte[32768];
	 * writer_db.write(",\n{logName:php_error.log,dboffset:" + dboffset + "}");
	 * writer_db.flush(); } //判断退出时间 if(!today.equals(sdf1.format(new Date()))){
	 * System.out.println("shutdown!!!!"); client.close(); phpoffset=0;
	 * writer.close(); exec.shutdown(); } } catch (IOException e) { throw new
	 * RuntimeException(e); } } }, 0, 1, TimeUnit.SECONDS);
	 * System.out.println("over!!!!"); } } catch (IOException e) {
	 * e.printStackTrace(); } }
	 * 
	 * 
	 * public static void main(String[] args) { String IP = "103.10.84.115";
	 * String USERNAME = "qianjing"; String PASSWORD = "gaohui"; final String
	 * PHPLogPath = "/home/q/logs/qjuser/"; final ErrorLogDao dao = new
	 * ErrorLogDaoImpl();
	 * 
	 * File keyfile = new File("E:/unix_key/115publickey-open"); String
	 * DBLogPath = "/tmp/php_error.log"; try { Connection conn = new
	 * Connection(IP); conn.connect(); if
	 * (conn.authenticateWithPublicKey(USERNAME, keyfile, PASSWORD)) { final
	 * SFTPv3Client client = new SFTPv3Client(conn); final SimpleDateFormat sdf
	 * = new SimpleDateFormat("yyyyMMdd"); final String logName = "user-sys-" +
	 * sdf.format(new Date()) + ".log"; SimpleDateFormat sdf1 = new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat sdf2 = new
	 * SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH); final String today =
	 * sdf1.format(new Date()) + "T"; final String now = sdf2.format(new
	 * Date()); final SFTPv3FileHandle handle = client.openFileRO(PHPLogPath +
	 * logName); final SFTPv3FileHandle handle1 = client
	 * .openFileRO("/tmp/php_errors.log");
	 * 
	 * final FileWriter writer = new FileWriter("offset.txt", false);
	 * writer.write("{logName:" + logName + ",offset:0}"); writer.flush(); final
	 * FileWriter writer_db = new FileWriter("db_offset.txt", false);
	 * writer_db.write("{logName:php_error.log" + ",offset:0}");
	 * writer_db.flush(); // 启动一个线程每10秒钟读取新增的日志信息 final ScheduledExecutorService
	 * exec = Executors .newScheduledThreadPool(1);
	 * exec.scheduleWithFixedDelay(new Runnable() { public void run() { byte[]
	 * dst = new byte[32768]; try { // 获得变化部分的 int readLen = client.read(handle,
	 * phpoffset, dst, 0, 32768); if (readLen > -1) { phpoffset += readLen;
	 * String logStr = new String(dst); // 匹配2015-09-11T09:47:31+08:00
	 * [UserSysLogger] // ，注意空格 String regStr = today +
	 * "(0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1})\\+08:00 \\[UserSysLogger\\] "
	 * ; String[] logs = logStr.split(regStr);
	 * 
	 * Map<String, String> mailMap = new LinkedHashMap<String, String>(); int
	 * errorIndex = 1; for (int i = 0; i < logs.length; i++) { if
	 * (logs[i].startsWith("bound ") && logs[i].contains(",ecode=") &&
	 * logs[i].contains(",emsg=")) { if (logs[i].contains(",ecode=0")) break;
	 * ErrorLog error = new ErrorLog(); System.out.println(logs[i].trim());
	 * String[] attes = logs[i].split(","); for (String att : attes) { String[]
	 * map = att.trim() .split("="); if (map[0].equals("uid"))
	 * error.setUid(map[1]); if (map[0].equals("mobile"))
	 * error.setMobile(map[1]); if (map[0].equals("card") || map[0]
	 * .equals("bankcard")) error.setCard(map[1]); if (map[0].equals("ecode"))
	 * error.setEcode(map[1]); if (map[0].equals("emsg")) error.setEmsg(map[1]);
	 * if (map[0].equals("from")) error.setFrom_page(map[1]); if
	 * (map[0].equals("hengsheng_res")) error.setExtra(map[1]); if
	 * (map[0].equals("type")) error.setType("php"); if
	 * (map[0].equals("capitalmode")) { error.setCapitalmode(map[1]); } } String
	 * mailMsg = error.getUid() + "	" + error.getMobile() + "	" +
	 * error.getEcode() + "	" + error.getEmsg() + "	" + error.getFrom_page() +
	 * "\n"; mailMap.put("" + errorIndex++, mailMsg); // dao.persist(error); } }
	 * if (mailMap.size() > 0) { System.out.println(mailMap); Mail mailModel =
	 * Mail.MailCF; mailModel.subject = "绑卡异常" + DateUtil.currentTime();
	 * MailContent content = new MailContent(); content.setContentMap(mailMap);
	 * mailModel.setContent(content); } // MailUtil.send(mailModel, new //
	 * String[]{"gaohui@qianjing.com"}, new //
	 * String[]{"gaohui7141@163.com"},null); // System.out.println(log);
	 * System.out.println(logs.length); //
	 * System.out.println(logs[0]+"---"+logs[1]); dst = new byte[32768];
	 * writer.write(",\n{logName:" + logName + ",offset:" + phpoffset + "}");
	 * writer.flush(); } readLen = client.read(handle1, dboffset, dst, 0,
	 * 32768); if (readLen > -1) { dboffset += readLen; String logStr = new
	 * String(dst); // 匹配[18-Jul-2015 04:05:02 ，注意空格 String regStr = "\\[" + now
	 * + " (0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}:([0-5]\\d{1}) "; String[] logs =
	 * logStr.split(regStr); System.out.println(regStr);
	 * 
	 * Map<String, String> mailMap = new LinkedHashMap<String, String>();
	 * 
	 * int errorIndex = 1;
	 * 
	 * for (int i = 0; i < logs.length; i++) { //
	 * if(logs[i].startsWith("PRC] PHP Fatal error:")){ if
	 * (logs[i].startsWith("UTC] PHP Warning:")) { ErrorLog error = new
	 * ErrorLog(); error.setEmsg(logs[i].substring(17));// 21
	 * error.setType("db");
	 * 
	 * String mailMsg = error.getEmsg() + "\n"; mailMap.put("" + errorIndex++,
	 * mailMsg); // dao.persist(error); } } if (mailMap.size() > 0) {
	 * System.out.println(mailMap); Mail mailModel = Mail.MailCF;
	 * mailModel.subject = "绑卡异常" + DateUtil.currentTime(); MailContent content
	 * = new MailContent(); content.setContentMap(mailMap);
	 * mailModel.setContent(content); } dst = new byte[32768]; writer_db
	 * .write(",\n{logName:php_error.log,dboffset:" + dboffset + "}");
	 * writer_db.flush(); } String nowLogName = "user-sys-" + sdf.format(new
	 * Date()) + ".log"; if (!logName.equals(nowLogName)) {
	 * System.out.println("shutdown!!!!"); client.close(); phpoffset = 0;
	 * writer.close(); exec.shutdown(); } } catch (IOException e) { throw new
	 * RuntimeException(e); } } }, 0, 1, TimeUnit.SECONDS); } } catch
	 * (IOException e) { e.printStackTrace(); } }
	 */

	@Override
	public List<AppLog> getAppLogList(AppLog appLog) {
		// TODO Auto-generated method stub
		return errorLogDao.getAppLogList(appLog);
	}

	@Override
	public long getAppLogListCount(AppLog appLog) {
		// TODO Auto-generated method stub
		return errorLogDao.getAppLogListCount(appLog);
	}

	@Override
	public List<ChannelType> getOsTypeList(ChannelType channelType) {
		// TODO Auto-generated method stub
		return errorLogDao.getOsTypeList(channelType);
	}
}
