package com.forestry.model.sys.param;

import core.extjs.ExtJSBaseParameter;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public class FailParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 4462121985297150686L;
	private String sdate;
	private String edate;
	
	private String $like_uid;
	private String $like_name;
	private String $like_mobile;
	
	
	
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
	* @return 获得 $like_mobile
	*/
	public String get$like_mobile() {
		return $like_mobile;
	}
	
	/**
	* @param $like_mobile 设置 $like_mobile
	*/
	public void set$like_mobile(String $like_mobile) {
		this.$like_mobile = $like_mobile;
	}
	
	
	
	
 
}
