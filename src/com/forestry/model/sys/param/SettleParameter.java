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
public class SettleParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 4462121985297150686L;
	private String sdate;
	private String edate;
	
 
	private String $like_type;
	private String $like_platform;
	private String $like_platform1;
	private String $like_fdCode;
	
	public String getSdate() {
		return sdate;
	}
	public String get$like_platform1() {
		return $like_platform1;
	}
	public void set$like_platform1(String $like_platform1) {
		this.$like_platform1 = $like_platform1;
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
	 
	public String get$like_type() {
		return $like_type;
	}
	public void set$like_type(String $like_type) {
		this.$like_type = $like_type;
	}
	public String get$like_platform() {
		return $like_platform;
	}
	public void set$like_platform(String $like_platform) {
		this.$like_platform = $like_platform;
	}
	public String get$like_fdCode() {
		return $like_fdCode;
	}
	public void set$like_fdCode(String $like_fdCode) {
		this.$like_fdCode = $like_fdCode;
	}
 
	 
	
	 
	
	 
	
	
	
	
 
}
