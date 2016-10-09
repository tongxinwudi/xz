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
import com.forestry.model.sys.param.TradeUserParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

 
/**
 * 
 *  Class Name: TradeUser.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月26日 下午1:59:22    
 *  @version 1.0
 */
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TradeUser extends TradeUserParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	 
	private String uid;
	private String name;
	private String reg_time;
	private String boundT;
	private String first_time;
	private String first_money;
	private String first_stype;
	private String first_platform;
	private String first_channel;
	private String second_money;
	private String second_time;
	private String second_stype;
	
	/**
	* @return 获得 uid
	*/
	public String getUid() {
		return uid;
	}
	
	/**
	* @param uid 设置 uid
	*/
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	/**
	* @return 获得 name
	*/
	public String getName() {
		return name;
	}
	
	/**
	* @param name 设置 name
	*/
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	* @return 获得 reg_time
	*/
	public String getReg_time() {
		return reg_time;
	}
	
	/**
	* @param reg_time 设置 reg_time
	*/
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}
	
	
	public String getBoundT() {
		return boundT;
	}

	public void setBoundT(String boundT) {
		this.boundT = boundT;
	}

	/**
	* @return 获得 first_time
	*/
	public String getFirst_time() {
		return first_time;
	}
	
	/**
	* @param first_time 设置 first_time
	*/
	public void setFirst_time(String first_time) {
		this.first_time = first_time;
	}
	
	/**
	* @return 获得 first_money
	*/
	public String getFirst_money() {
		return first_money;
	}
	
	/**
	* @param first_money 设置 first_money
	*/
	public void setFirst_money(String first_money) {
		this.first_money = first_money;
	}
	
	/**
	* @return 获得 first_stype
	*/
	public String getFirst_stype() {
		return first_stype;
	}
	
	/**
	* @param first_stype 设置 first_stype
	*/
	public void setFirst_stype(String first_stype) {
		this.first_stype = first_stype;
	}
	
	/**
	* @return 获得 first_platform
	*/
	public String getFirst_platform() {
		return first_platform;
	}
	
	/**
	* @param first_platform 设置 first_platform
	*/
	public void setFirst_platform(String first_platform) {
		this.first_platform = first_platform;
	}
	
	/**
	* @return 获得 first_channel
	*/
	public String getFirst_channel() {
		return first_channel;
	}
	
	/**
	* @param first_channel 设置 first_channel
	*/
	public void setFirst_channel(String first_channel) {
		this.first_channel = first_channel;
	}
	
	/**
	* @return 获得 second_time
	*/
	public String getSecond_time() {
		return second_time;
	}
	
	/**
	* @param second_time 设置 second_time
	*/
	public void setSecond_time(String second_time) {
		this.second_time = second_time;
	}
	
	/**
	* @return 获得 second_stype
	*/
	public String getSecond_stype() {
		return second_stype;
	}
	
	/**
	* @param second_stype 设置 second_stype
	*/
	public void setSecond_stype(String second_stype) {
		this.second_stype = second_stype;
	}

	
	/**
	* @return 获得 second_money
	*/
	public String getSecond_money() {
		return second_money;
	}

	
	/**
	* @param second_money 设置 second_money
	*/
	public void setSecond_money(String second_money) {
		this.second_money = second_money;
	}

	 
	 
	
	
 

	 
	
}
