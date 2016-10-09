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
public class TradeUserParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 4462121985297150686L;
	private String sdate;
	private String edate;
	
	private String $like_name;
	private String $like_uid;
	
	
	
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
	
	/**
	* @return 获得 $like_name
	*/
	public String get$like_name() {
		return $like_name;
	}
	
	/**
	* @param $like_name 设置 $like_name
	*/
	public void set$like_name(String $like_name) {
		this.$like_name = $like_name;
	}
	
	/**
	* @return 获得 $like_uid
	*/
	public String get$like_uid() {
		return $like_uid;
	}
	
	/**
	* @param $like_uid 设置 $like_uid
	*/
	public void set$like_uid(String $like_uid) {
		this.$like_uid = $like_uid;
	}
	
	 
	
	 
	
	
	
	
 
}
