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
import com.forestry.model.sys.param.FundParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;



/**
 * 
 *  Class Name: Fundlimit.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月20日 上午10:56:15    
 *  @version 1.0
 */
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FundLimit extends FundParameter {

	private static final long serialVersionUID = -1659957832585685551L;
 
	String date;
	String limit10;
	String limit100;
	String limit1000;
	String limit10000;
	String limit100000;
	
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
	* @return 获得 limit10
	*/
	public String getLimit10() {
		return limit10;
	}
	
	/**
	* @param limit10 设置 limit10
	*/
	public void setLimit10(String limit10) {
		this.limit10 = limit10;
	}
	
	/**
	* @return 获得 limit100
	*/
	public String getLimit100() {
		return limit100;
	}
	
	/**
	* @param limit100 设置 limit100
	*/
	public void setLimit100(String limit100) {
		this.limit100 = limit100;
	}
	
	/**
	* @return 获得 limit1000
	*/
	public String getLimit1000() {
		return limit1000;
	}
	
	/**
	* @param limit1000 设置 limit1000
	*/
	public void setLimit1000(String limit1000) {
		this.limit1000 = limit1000;
	}
	
	/**
	* @return 获得 limit10000
	*/
	public String getLimit10000() {
		return limit10000;
	}
	
	/**
	* @param limit10000 设置 limit10000
	*/
	public void setLimit10000(String limit10000) {
		this.limit10000 = limit10000;
	}
	
	/**
	* @return 获得 limit100000
	*/
	public String getLimit100000() {
		return limit100000;
	}
	
	/**
	* @param limit100000 设置 limit100000
	*/
	public void setLimit100000(String limit100000) {
		this.limit100000 = limit100000;
	}
	
	
 
	
	
 
	
	
}
