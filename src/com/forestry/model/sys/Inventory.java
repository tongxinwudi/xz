package com.forestry.model.sys;

import java.util.Date;
import java.util.List;

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
import com.forestry.model.sys.param.InventoryParameter;
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
public class Inventory extends InventoryParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	public String date;//日期
	public String fdCode;//基金代码
	public String fdName;//基金名称
	public String partner;//第三方 
	public String inventory;//日均保有量
	public String sum;//总保有量
	public List<String> day;
	public List<String> dayInventory; 
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	 
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
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	public List<String> getDay() {
		return day;
	}
	public void setDay(List<String> day) {
		this.day = day;
	}
	public List<String> getDayInventory() {
		return dayInventory;
	}
	public void setDayInventory(List<String> dayInventory) {
		this.dayInventory = dayInventory;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	
 
	 
  
	

	 
	
}
