package com.forestry.model.sys.param;

import core.extjs.ExtJSBaseParameter;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public class ProfitParameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 4462121985297150686L;
	private String $like_mobile;
	private String $like_name;
	private String cashbao;
	
	
	
	/**
	* @return 获得 cashbao
	*/
	public String getCashbao() {
		return cashbao;
	}
	
	/**
	* @param cashbao 设置 cashbao
	*/
	public void setCashbao(String cashbao) {
		this.cashbao = cashbao;
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
}
