package com.forestry.model.sys.param;

import core.extjs.ExtJSBaseParameter;

/**
 * 
 *  Class Name: ChannelParameter.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月25日 下午4:42:16    
 *  @version 1.0
 */
public class AppLogParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 4462121985297150686L;
	private String sdate;
	private String edate;
	
	private String $like_mobile;
	private String $like_model;
	private String $like_version;
	
	
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String get$like_mobile() {
		return $like_mobile;
	}
	public void set$like_mobile(String $like_mobile) {
		this.$like_mobile = $like_mobile;
	}
	 
	
	public String get$like_model() {
		return $like_model;
	}
	public void set$like_model(String $like_model) {
		this.$like_model = $like_model;
	}
	public String get$like_version() {
		return $like_version;
	}
	public void set$like_version(String $like_version) {
		this.$like_version = $like_version;
	}
	
	 
	
	 
	
	 
	
	
	
	
 
}
