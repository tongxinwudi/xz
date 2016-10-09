package core.util;  

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.forestry.model.sys.MailConfig;
 

 
/**
 * 
 *  Class Name: MailConfigReader.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月17日 下午2:15:03    
 *  @version 1.0
 */
public class MailConfigReader {
	public MailConfig read() throws IOException{
		MailConfig config=new MailConfig();
		String path =MailConfigReader.class.getResource("/").getPath()+  "/mail.properties";
	
	    InputStream in = new BufferedInputStream(new FileInputStream(path));
		ResourceBundle rs=new PropertyResourceBundle(in);
		config.setServer(rs.getString("mail.server"));
		config.setPort(rs.getString("mail.port"));
		config.setUser(rs.getString("mail.user"));
		config.setPersonal(rs.getString("mail.personal"));
		config.setPwd(rs.getString("mail.pwd"));
		config.setFrom(rs.getString("mail.from"));
		config.setTo(rs.getString("mail.to").split(","));
		config.setCc(rs.getString("mail.cc").split(","));
		return config;
	}
}
  