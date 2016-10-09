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

import com.forestry.model.sys.param.Channel2Parameter;
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
public class Channel2 extends Channel2Parameter {

	private static final long serialVersionUID = -1659957832585685551L;
	public String date;//日期
	public String platform;
	public String channel;
	public String reg_num;
	public String bound_num;
	public String user_num;
	public String first_user_num;
	public String money_num;
	public String new_user_num;
	public String new_user_rate;
	public String new_user_rate1;
	public String new_money_num;
	
	public String reg_num_ration;
	public String bound_num_ration;
	public String user_num_ration;
	public String first_user_num_ration;
	
	public String new_user_rate_ration;
	public String new_money_num_ration;
	
	public String partner;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getReg_num() {
		return reg_num;
	}
	public void setReg_num(String reg_num) {
		this.reg_num = reg_num;
	}
	public String getBound_num() {
		return bound_num;
	}
	public void setBound_num(String bound_num) {
		this.bound_num = bound_num;
	}
	public String getUser_num() {
		return user_num;
	}
	public void setUser_num(String user_num) {
		this.user_num = user_num;
	}
	public String getFirst_user_num() {
		return first_user_num;
	}
	public void setFirst_user_num(String first_user_num) {
		this.first_user_num = first_user_num;
	}
	public String getMoney_num() {
		return money_num;
	}
	public void setMoney_num(String money_num) {
		this.money_num = money_num;
	}
	public String getNew_user_num() {
		return new_user_num;
	}
	public void setNew_user_num(String new_user_num) {
		this.new_user_num = new_user_num;
	}
	public String getNew_money_num() {
		return new_money_num;
	}
	public void setNew_money_num(String new_money_num) {
		this.new_money_num = new_money_num;
	}
	public String getNew_user_rate() {
		return new_user_rate;
	}
	public void setNew_user_rate(String new_user_rate) {
		this.new_user_rate = new_user_rate;
	}
	public String getReg_num_ration() {
		return reg_num_ration;
	}
	public void setReg_num_ration(String reg_num_ration) {
		this.reg_num_ration = reg_num_ration;
	}
	public String getBound_num_ration() {
		return bound_num_ration;
	}
	public void setBound_num_ration(String bound_num_ration) {
		this.bound_num_ration = bound_num_ration;
	}
	public String getUser_num_ration() {
		return user_num_ration;
	}
	public void setUser_num_ration(String user_num_ration) {
		this.user_num_ration = user_num_ration;
	}
	public String getFirst_user_num_ration() {
		return first_user_num_ration;
	}
	public void setFirst_user_num_ration(String first_user_num_ration) {
		this.first_user_num_ration = first_user_num_ration;
	}
	public String getNew_user_rate_ration() {
		return new_user_rate_ration;
	}
	public void setNew_user_rate_ration(String new_user_rate_ration) {
		this.new_user_rate_ration = new_user_rate_ration;
	}
	public String getNew_money_num_ration() {
		return new_money_num_ration;
	}
	public void setNew_money_num_ration(String new_money_num_ration) {
		this.new_money_num_ration = new_money_num_ration;
	}
	public String getNew_user_rate1() {
		return new_user_rate1;
	}
	public void setNew_user_rate1(String new_user_rate1) {
		this.new_user_rate1 = new_user_rate1;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
 
 
	
	 
 

	 
	
}
