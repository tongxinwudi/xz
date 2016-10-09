/**
 * Project Name:cxkm
 * File Name:SMSModel.java
 * Package Name:cn.cxkm.web.model
 *
 *   ver     createdate      	author			mail
 * ─────────────────────────────────────────────────────────────────
 *   1.0	2013-4-18上午8:34:23		lidl 		lidaling@foxmail.com
 *
 * Copyright (c) 2012,  All Rights Reserved by lidl.
 */

package com.forestry.model.sys;

import ccc.common.model.BaseModel;

/**
 * 
 *  Class Name: MailModel.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月17日 下午2:14:31    
 *  @version 1.0
 */
public class MailModel extends BaseModel {
	private String operCode,content,email;

	public String getOperCode() {
		return operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}