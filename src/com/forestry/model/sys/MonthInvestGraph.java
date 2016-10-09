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
import com.forestry.model.sys.param.ProfitParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

/**
 * 
 *  Class Name: MonthInvestGraph.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月18日 下午6:25:24    
 *  @version 1.0
 */
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MonthInvestGraph   {

	private static final long serialVersionUID = -1659957832585685551L;
	 
	private String date;
	private String invest;
	private String redemp;
	private String profit;
	
	private String investWithCashbao;
	private String redempWithCashbao;
	private String profitWithCashbao;
	
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
	* @return 获得 redemp
	*/
	public String getRedemp() {
		return redemp;
	}
	
	/**
	* @param redemp 设置 redemp
	*/
	public void setRedemp(String redemp) {
		this.redemp = redemp;
	}
	
	/**
	* @return 获得 profit
	*/
	public String getProfit() {
		return profit;
	}
	
	/**
	* @param profit 设置 profit
	*/
	public void setProfit(String profit) {
		this.profit = profit;
	}
	
	/**
	* @return 获得 investWithCashbao
	*/
	public String getInvestWithCashbao() {
		return investWithCashbao;
	}
	
	/**
	* @param investWithCashbao 设置 investWithCashbao
	*/
	public void setInvestWithCashbao(String investWithCashbao) {
		this.investWithCashbao = investWithCashbao;
	}
	
	/**
	* @return 获得 redempWithCashbao
	*/
	public String getRedempWithCashbao() {
		return redempWithCashbao;
	}
	
	/**
	* @param redempWithCashbao 设置 redempWithCashbao
	*/
	public void setRedempWithCashbao(String redempWithCashbao) {
		this.redempWithCashbao = redempWithCashbao;
	}
	
	/**
	* @return 获得 profitWithCashbao
	*/
	public String getProfitWithCashbao() {
		return profitWithCashbao;
	}
	
	/**
	* @param profitWithCashbao 设置 profitWithCashbao
	*/
	public void setProfitWithCashbao(String profitWithCashbao) {
		this.profitWithCashbao = profitWithCashbao;
	}
	
	
	
	
}
