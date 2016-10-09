package core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.forestry.model.sys.Mail;

import ccc.common.manager.pub.FileHelper;


/**
 * 
 *  Class Name: MailContentBuilder.java
 *  Function:发送邮件
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月13日 下午6:21:22    
 *  @version 1.0
 */
public class MailContentBuilder {
	private Logger log=LoggerFactory.getLogger(MailContentBuilder.class);
	
	 
	public String mailContent(Mail mailType){
		String tempStr=this.readMailTemplate(mailType);
		 
		Map<String, String> contentMap=mailType.getContent().getContentMap();
		for(Iterator<Entry<String, String>> iterator = contentMap.entrySet().iterator();iterator.hasNext();){
			Map.Entry<String, String> entry=(Entry<String, String>) iterator.next();
			tempStr=this.replaceAll(tempStr,entry.getKey(),entry.getValue());
		}
		return tempStr;
	}
	
	    /**@Title: readMailTemplate 
	     * @Description: 根据邮件类型读取邮件模板
	     * @param mailType
	     * @return 邮件模板字符串
	     * @author  lidaling@foxmail.com
	     * @date 2012-3-23 下午9:21:55
	     */  
	public String readMailTemplate(Mail mailType){
		InputStreamReader reader = null;
		StringBuffer msgContent=null;
		try{
 
			reader=new InputStreamReader(new FileInputStream(MailConfigReader.class.getResource("/").getPath()+"/"+mailType+".html"),"UTF-8");
 
		} catch (FileNotFoundException e) {
			log.error("外围邮件模版未找到，使用内部默认邮件模板");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			
			BufferedReader bufread = new BufferedReader(reader);
			String temp = null;
			msgContent = new StringBuffer();
			while((temp = bufread.readLine()) != null){
				msgContent.append(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("get mail model content:{}",new Object[]{msgContent});
		return msgContent.toString();
	}
	 
    /**
     * 
     *  Function:
     *  @author TongXin  DateTime 2015年8月13日 下午6:21:44
     *  @param str1
     *  @param str2
     *  @param str3
     *  @return
     */
	private String replaceAll(String str1,String str2,String str3){	
		StringBuffer strBuf = new StringBuffer(str1);	
		int index=0;	
		while(str1.indexOf(str2,index)!=-1)	{	
			index=str1.indexOf(str2,index);	
			strBuf.replace(str1.indexOf(str2,index),str1.indexOf(str2,index)+str2.length(),str3);	
			index=index+str3.length();	
			str1=strBuf.toString();	
		}	
		return strBuf.toString();
	} 
}
