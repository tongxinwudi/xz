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
public class Channel2Parameter extends ExtJSBaseParameter {

	private static final long serialVersionUID = 4462121985297150686L;
	private String sdate;
	private String edate;
	
	private String $like_platform;
	private String $like_channel;
	private String $like_channel2;
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
	
	/**
	* @return 获得 $like_platform
	*/
	public String get$like_platform() {
		return $like_platform;
	}
	
	/**
	* @param $like_platform 设置 $like_platform
	*/
	public void set$like_platform(String $like_platform) {
		this.$like_platform = $like_platform;
	}
	
	/**
	* @return 获得 $like_channel
	*/
	public String get$like_channel() {
		return $like_channel;
	}
	
	/**
	* @param $like_channel 设置 $like_channel
	*/
	public void set$like_channel(String $like_channel) {
		this.$like_channel = $like_channel;
	}
	public String get$last() {
		return $last;
	}
	public void set$last(String $last) {
		this.$last = $last;
	}
	public String get$like_channel2() {
		return $like_channel2;
	}
	public void set$like_channel2(String $like_channel2) {
		this.$like_channel2 = $like_channel2;
	}
	
	
	 
	
	
	
	
 
}
