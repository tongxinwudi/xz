package com.forestry.model.sys.param;

import core.extjs.ExtJSBaseParameter;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public class FundParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 4462121985297150686L;
	private String $like_mobile;
	private String $like_name;
	private String $sinvest;
	private String $einvest;
	private String $like_id;
	private String $type;
	public String get$type() {
		return $type;
	}
	public void set$type(String $type) {
		this.$type = $type;
	}
	 
	public String get$like_id() {
		return $like_id;
	}
	public void set$like_id(String $like_id) {
		this.$like_id = $like_id;
	}
	public String get$like_mobile() {
		return $like_mobile;
	}
	public void set$like_mobile(String $like_mobile) {
		this.$like_mobile = $like_mobile;
	}
	public String get$like_name() {
		return $like_name;
	}
	public void set$like_name(String $like_name) {
		this.$like_name = $like_name;
	}
	public String get$sinvest() {
		return $sinvest;
	}
	public void set$sinvest(String $sinvest) {
		this.$sinvest = $sinvest;
	}
	public String get$einvest() {
		return $einvest;
	}
	public void set$einvest(String $einvest) {
		this.$einvest = $einvest;
	}
	
}
