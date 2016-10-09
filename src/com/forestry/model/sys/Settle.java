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

import com.forestry.model.sys.param.AppLogParameter;
import com.forestry.model.sys.param.ChannelParameter;
import com.forestry.model.sys.param.FailParameter;
import com.forestry.model.sys.param.ForestryParameter;
import com.forestry.model.sys.param.OperateParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.forestry.model.sys.param.SettleParameter;
import com.forestry.model.sys.param.TradeUserParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

/**
 * 
 *  Class Name: Settle.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年11月12日 下午4:17:56    
 *  @version 1.0
 */
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Settle extends SettleParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	 
	private String fdCode;
	private String fdName;
	private String dayInventory;
	private String inventory;
	private String suml6;
	private String counts6;
	private String invest;
	private String redempl5w;
	private String redemps5w;
	private String fees;
	private String saleFee;
	private String manageFee;
	private String payCost;
	private String supFee;
	private String techFee;
	
	private String platforomName;
	
	private String accessAuthFee;
	private String boundNum;
	public String getFdCode() {
		return fdCode;
	}
	public void setFdCode(String fdCode) {
		this.fdCode = fdCode;
	}
	public String getFdName() {
		return fdName;
	}
	public void setFdName(String fdName) {
		this.fdName = fdName;
	}
	public String getDayInventory() {
		return dayInventory;
	}
	public void setDayInventory(String dayInventory) {
		this.dayInventory = dayInventory;
	}
	public String getInvest() {
		return invest;
	}
	public void setInvest(String invest) {
		this.invest = invest;
	}
	public String getRedempl5w() {
		return redempl5w;
	}
	public void setRedempl5w(String redempl5w) {
		this.redempl5w = redempl5w;
	}
	public String getRedemps5w() {
		return redemps5w;
	}
	public void setRedemps5w(String redemps5w) {
		this.redemps5w = redemps5w;
	}
	public String getSaleFee() {
		return saleFee;
	}
	public void setSaleFee(String saleFee) {
		this.saleFee = saleFee;
	}
	public String getManageFee() {
		return manageFee;
	}
	public void setManageFee(String manageFee) {
		this.manageFee = manageFee;
	}
	public String getPayCost() {
		return payCost;
	}
	public void setPayCost(String payCost) {
		this.payCost = payCost;
	}
	public String getSupFee() {
		return supFee;
	}
	public void setSupFee(String supFee) {
		this.supFee = supFee;
	}
	public String getTechFee() {
		return techFee;
	}
	public void setTechFee(String techFee) {
		this.techFee = techFee;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	 
	public String getFees() {
		return fees;
	}
	public void setFees(String fees) {
		this.fees = fees;
	}
	public String getSuml6() {
		return suml6;
	}
	public void setSuml6(String suml6) {
		this.suml6 = suml6;
	}
	public String getCounts6() {
		return counts6;
	}
	public void setCounts6(String counts6) {
		this.counts6 = counts6;
	}
	public String getAccessAuthFee() {
		return accessAuthFee;
	}
	public void setAccessAuthFee(String accessAuthFee) {
		this.accessAuthFee = accessAuthFee;
	}
	public String getBoundNum() {
		return boundNum;
	}
	public void setBoundNum(String boundNum) {
		this.boundNum = boundNum;
	}
	public String getPlatforomName() {
		return platforomName;
	}
	public void setPlatforomName(String platforomName) {
		this.platforomName = platforomName;
	}
	
	
}
