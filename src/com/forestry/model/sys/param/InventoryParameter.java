package com.forestry.model.sys.param;

import core.extjs.ExtJSBaseParameter;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public class InventoryParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 4462121985297150686L;
	private String sdate;
	private String edate;
	
	private String $like_platform;
	private String $like_fdCode;
	private String $last;

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

	public String get$like_platform() {
		return $like_platform;
	}

	public void set$like_platform(String $like_platform) {
		this.$like_platform = $like_platform;
	}

	public String get$last() {
		return $last;
	}

	public void set$last(String $last) {
		this.$last = $last;
	}

	public String get$like_fdCode() {
		return $like_fdCode;
	}

	public void set$like_fdCode(String $like_fdCode) {
		this.$like_fdCode = $like_fdCode;
	}
	
	
	
	
 
	
 
}
