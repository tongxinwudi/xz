package com.forestry.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.forestry.model.sys.param.ChannelParameter;
import com.forestry.model.sys.param.FailParameter;
import com.forestry.model.sys.param.ForestryParameter;
import com.forestry.model.sys.param.OperateParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

 
/**
 * 
 *  Class Name: Channel.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月25日 下午3:08:21    
 *  @version 1.0
 */
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Channel extends ChannelParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	public String date;//日期
	public String platform;
	public String channel;
	public String reg_num;
	public String bound_num;
	public String new_bound_num;
	public String user_num;
	public String money_num;
	public String new_user_num;
	public String new_money_num;
	public String per_money_num;
	public String per_new_money_num;
	
	/**
	* @return 获得 date
	*/
	public String getDate() {
		return date;
	}
	
	/**
	* @param date 设置 date
	*/
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	* @return 获得 platform
	*/
	public String getPlatform() {
		return platform;
	}
	
	/**
	* @param platform 设置 platform
	*/
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	/**
	* @return 获得 channel
	*/
	public String getChannel() {
		return channel;
	}
	
	/**
	* @param channel 设置 channel
	*/
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	/**
	* @return 获得 reg_num
	*/
	public String getReg_num() {
		return reg_num;
	}
	
	/**
	* @param reg_num 设置 reg_num
	*/
	public void setReg_num(String reg_num) {
		this.reg_num = reg_num;
	}
	
	/**
	* @return 获得 bound_num
	*/
	public String getBound_num() {
		return bound_num;
	}
	
	/**
	* @param bound_num 设置 bound_num
	*/
	public void setBound_num(String bound_num) {
		this.bound_num = bound_num;
	}
	
	/**
	* @return 获得 new_bound_num
	*/
	public String getNew_bound_num() {
		return new_bound_num;
	}
	
	/**
	* @param new_bound_num 设置 new_bound_num
	*/
	public void setNew_bound_num(String new_bound_num) {
		this.new_bound_num = new_bound_num;
	}
	
	/**
	* @return 获得 user_num
	*/
	public String getUser_num() {
		return user_num;
	}
	
	/**
	* @param user_num 设置 user_num
	*/
	public void setUser_num(String user_num) {
		this.user_num = user_num;
	}
	
	/**
	* @return 获得 money_num
	*/
	public String getMoney_num() {
		return money_num;
	}
	
	/**
	* @param money_num 设置 money_num
	*/
	public void setMoney_num(String money_num) {
		this.money_num = money_num;
	}
	
	/**
	* @return 获得 new_user_num
	*/
	public String getNew_user_num() {
		return new_user_num;
	}
	
	/**
	* @param new_user_num 设置 new_user_num
	*/
	public void setNew_user_num(String new_user_num) {
		this.new_user_num = new_user_num;
	}
	
	/**
	* @return 获得 new_money_num
	*/
	public String getNew_money_num() {
		return new_money_num;
	}
	
	/**
	* @param new_money_num 设置 new_money_num
	*/
	public void setNew_money_num(String new_money_num) {
		this.new_money_num = new_money_num;
	}

	
	/**
	* @return 获得 per_money_num
	*/
	public String getPer_money_num() {
		return per_money_num;
	}

	
	/**
	* @param per_money_num 设置 per_money_num
	*/
	public void setPer_money_num(String per_money_num) {
		this.per_money_num = per_money_num;
	}

	
	/**
	* @return 获得 per_new_money_num
	*/
	public String getPer_new_money_num() {
		return per_new_money_num;
	}

	
	/**
	* @param per_new_money_num 设置 per_new_money_num
	*/
	public void setPer_new_money_num(String per_new_money_num) {
		this.per_new_money_num = per_new_money_num;
	}
	
	
 

	 
	
}
