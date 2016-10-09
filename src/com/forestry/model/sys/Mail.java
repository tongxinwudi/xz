package com.forestry.model.sys;  


/**
 * 
 *  Class Name: Mail.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月17日 下午2:13:53    
 *  @version 1.0
 */
public enum Mail {
	PasswdReset {
		private MailContent content;
		public MailContent getContent() {
			// TODO Auto-generated method stub
			return this.content;
		}

		@Override
		public void setContent(MailContent content) {
			// TODO Auto-generated method stub
			this.content=content;
		}
	},
	MailCF {
		private MailContent content;
		public MailContent getContent() {
			// TODO Auto-generated method stub
			return this.content;
		}

		@Override
		public void setContent(MailContent content) {
			// TODO Auto-generated method stub
			this.content=content;
		}
	},APPErrorLog {
		private MailContent content;
		public MailContent getContent() {
			// TODO Auto-generated method stub
			return this.content;
		}

		@Override
		public void setContent(MailContent content) {
			// TODO Auto-generated method stub
			this.content=content;
		}
	},
	MailErrorLog {
		private MailContent content;
		public MailContent getContent() {
			// TODO Auto-generated method stub
			return this.content;
		}

		@Override
		public void setContent(MailContent content) {
			// TODO Auto-generated method stub
			this.content=content;
		}
	};
	
	abstract public MailContent getContent();
	abstract public void setContent(MailContent content);
	public String subject;
}
  