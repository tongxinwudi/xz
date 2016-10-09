package com.forestry.core;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public interface Constant {

	public static final String AUTH_CODE = "AUTH_CODE";

	public static final String SESSION_USER = "SESSION_USER";

	public static final String SESSION_SYS_USER = "SESSION_SYS_USER";

	public static final String FORESTRY_DATA_SOURCE_BEAN_ID = "forestryDataSource";
	
	public static final String ERROR_FUND_NAME = "基金交易类错误";
	public static final String ERROR_CASHBAO_NAME = "活期宝类错误";
	public static final String ERROR_USER_NAME = "绑卡类错误";
	
	//-1请仔细核对您提交的信息、1001验证码错误，请核实后再试、HW手机号码不符、HX姓名不符、9998银行卡信息匹配失败、90610未满18周岁不允许开户、
		//90578未满18周岁不允许开户、21011您填写的手机号码与银行预留手机号码不一致，请核对后再试；或前往银行柜台更改后重试。
		//1201该身份证已存在、1202该身份证号码无效请仔细校对
	public static final String ECODE_UNMAIL="-1,1001,HW,HX,9998,90578,90610,21011,1201,1202,";

}
