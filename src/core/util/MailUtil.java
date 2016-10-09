package core.util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

 





import com.forestry.model.sys.Mail;
import com.forestry.model.sys.MailConfig;
import com.forestry.model.sys.MailContent;

import ccc.common.manager.pub.SendMailHelper;
import ccc.common.model.pub.SmtpMailModel;
 

public class MailUtil {

	private static final Logger log = Logger.getLogger(MailUtil.class);

	/**
	 * 
	 *  Function:
	 *  @author TongXin  DateTime 2015年8月14日 下午3:59:01
	 *  @param mail
	 *  @param to
	 *  @param cc
	 *  @param attchList
	 */
	public static void send(Mail mail,List<File> attchList) {
		// TODO Auto-generated method stub
		MailContentBuilder contentBuilder = new MailContentBuilder();
		String text = contentBuilder.mailContent(mail);
		MailConfig config = null;

		try {
			config = new MailConfigReader().read();
			SmtpMailModel model = new SmtpMailModel();
			model.setPersonal(config.getPersonal());
			model.setUser(config.getUser());
			model.setPass(config.getPwd());
			model.setMailServer(config.getServer());
			model.setTo(config.getTo());
			model.setCc(config.getCc());
			model.setSubject(mail.subject);
			model.setFrom(config.getFrom());
			model.setText(text);
			model.setAttachs(attchList);
			SendMailHelper.sendMail(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}
	
	public static void send(Mail mail, String[] to ,String[] cc,List<File> attchList) {
		// TODO Auto-generated method stub
		MailContentBuilder contentBuilder = new MailContentBuilder();
		String text = contentBuilder.mailContent(mail);
		MailConfig config = null;
		try {
			
			config = new MailConfigReader().read();
			SmtpMailModel model = new SmtpMailModel();
			model.setPersonal(config.getPersonal());
			model.setUser(config.getUser());
			model.setPass(config.getPwd());
			model.setMailServer(config.getServer());
			model.setTo(to);
			model.setCc(cc);
			model.setSubject(mail.subject);
			model.setFrom(config.getFrom());
			model.setText(text);
			model.setAttachs(attchList);
			SendMailHelper.sendMail(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}
	
	
	public static void main(String[] args){
		Map<String, String> map = new HashMap<String, String>();
		//map.put("#{loginName}",  "tongxin");
		
		String _token=UUID.randomUUID().toString();
		
		 
		
	//	map.put("#{token}", _token);
		
		MailContent content = new MailContent();
		 content.setContentMap(map);
		
		Mail mailModel = Mail.MailCF;
		mailModel.subject = "日常统计"+DateUtil.currentTime();
		mailModel.setContent(content);
		//MailUtil.send(mailModel, new String[]{"tongxin@qianjing.com"}, new String[]{"tongxin@qianjing.com"},null);
	}
}
