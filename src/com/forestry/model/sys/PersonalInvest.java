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

import com.forestry.model.sys.param.ForestryParameter;
import com.forestry.model.sys.param.PersonalInfoParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 * @树木信息实体类
 */

@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersonalInvest  {

	private static final long serialVersionUID = -1659957832585685551L;
	private String uid;
	private String name;
	private String mobile;
	private String identity;
	private String regDate;
	private String boundDate;
	private String channel;
	private String first_opDate;
	private String first_invest;
	private String first_fund;
	private String opDate;
	private String invest;
	private String fund;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getBoundDate() {
		return boundDate;
	}
	public void setBoundDate(String boundDate) {
		this.boundDate = boundDate;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getFirst_opDate() {
		return first_opDate;
	}
	public void setFirst_opDate(String first_opDate) {
		this.first_opDate = first_opDate;
	}
	public String getFirst_invest() {
		return first_invest;
	}
	public void setFirst_invest(String first_invest) {
		this.first_invest = first_invest;
	}
	public String getFirst_fund() {
		return first_fund;
	}
	public void setFirst_fund(String first_fund) {
		this.first_fund = first_fund;
	}
	public String getOpDate() {
		return opDate;
	}
	public void setOpDate(String opDate) {
		this.opDate = opDate;
	}
	public String getInvest() {
		return invest;
	}
	public void setInvest(String invest) {
		this.invest = invest;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	 
	
 
	
	
}
