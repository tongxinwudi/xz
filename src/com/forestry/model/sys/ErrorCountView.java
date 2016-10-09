/**
 * 
 */
package com.forestry.model.sys;

/**
 * @author Steve
 *
 */
public class ErrorCountView {
	private int errorType;//错误类型，1=交易错误，2=活期宝错误，3=帮卡错误
	private String title;	//错误名称
	private long count;		//错误数量
	private String ratio;	//错误所占比例
	private String month;	//所属年月份
	
	public int getErrorType() {
		return errorType;
	}
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
}
