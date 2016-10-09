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

import com.forestry.model.sys.param.FailParameter;
import com.forestry.model.sys.param.ForestryParameter;
import com.forestry.model.sys.param.OperateParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 * @树木信息实体类
 */

@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fail extends FailParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	public String date;//日期
	public String uid;//用户uid
	public String name;//姓名
	public String mobile;//手机号
	public String fund;//基金名称
	public String invest;//投资额
	public String bank;//银行
	public String reason;//失败原因
	
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
	* @return 获得 mobile
	*/
	public String getMobile() {
		return mobile;
	}
	
	/**
	* @param mobile 设置 mobile
	*/
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
	* @return 获得 fund
	*/
	public String getFund() {
		return fund;
	}
	
	/**
	* @param fund 设置 fund
	*/
	public void setFund(String fund) {
		this.fund = fund;
	}
	
	/**
	* @return 获得 invest
	*/
	public String getInvest() {
		return invest;
	}
	
	/**
	* @param invest 设置 invest
	*/
	public void setInvest(String invest) {
		this.invest = invest;
	}
	
	/**
	* @return 获得 bank
	*/
	public String getBank() {
		return bank;
	}
	
	/**
	* @param bank 设置 bank
	*/
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	/**
	* @return 获得 reason
	*/
	public String getReason() {
		return reason;
	}
	
	/**
	* @param reason 设置 reason
	*/
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/**
	* @return 获得 serialversionuid
	*/
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
 

	 
	
}
