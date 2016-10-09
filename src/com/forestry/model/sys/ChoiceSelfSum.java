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

import com.forestry.model.sys.param.ChoiceParameter;
import com.forestry.model.sys.param.ForestryParameter;
import com.forestry.model.sys.param.FundParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 * @树木信息实体类
 */

@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChoiceSelfSum extends ChoiceParameter {

	private static final long serialVersionUID = -1659957832585685551L;
 
	String date;
	String stock_fund;
	String bond_fund;
	String money_fund;
	String mix_fund;
	String index_fund;
	String preserv_fund;
	String etf_fund;
	String qdii_fund;
	String other_fund;
	String cashbao;
	
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
	* @return 获得 stock_fund
	*/
	public String getStock_fund() {
		return stock_fund;
	}
	
	/**
	* @param stock_fund 设置 stock_fund
	*/
	public void setStock_fund(String stock_fund) {
		this.stock_fund = stock_fund;
	}
	
	/**
	* @return 获得 bond_fund
	*/
	public String getBond_fund() {
		return bond_fund;
	}
	
	/**
	* @param bond_fund 设置 bond_fund
	*/
	public void setBond_fund(String bond_fund) {
		this.bond_fund = bond_fund;
	}
	
	/**
	* @return 获得 money_fund
	*/
	public String getMoney_fund() {
		return money_fund;
	}
	
	/**
	* @param money_fund 设置 money_fund
	*/
	public void setMoney_fund(String money_fund) {
		this.money_fund = money_fund;
	}
	
	/**
	* @return 获得 mix_fund
	*/
	public String getMix_fund() {
		return mix_fund;
	}
	
	/**
	* @param mix_fund 设置 mix_fund
	*/
	public void setMix_fund(String mix_fund) {
		this.mix_fund = mix_fund;
	}
	
	/**
	* @return 获得 index_fund
	*/
	public String getIndex_fund() {
		return index_fund;
	}
	
	/**
	* @param index_fund 设置 index_fund
	*/
	public void setIndex_fund(String index_fund) {
		this.index_fund = index_fund;
	}
	
	 
	
	
	/**
	* @return 获得 preserv_fund
	*/
	public String getPreserv_fund() {
		return preserv_fund;
	}

	
	/**
	* @param preserv_fund 设置 preserv_fund
	*/
	public void setPreserv_fund(String preserv_fund) {
		this.preserv_fund = preserv_fund;
	}

	 
	
	
 
	
	
	/**
	* @return 获得 etf_fund
	*/
	public String getEtf_fund() {
		return etf_fund;
	}

	
	/**
	* @param etf_fund 设置 etf_fund
	*/
	public void setEtf_fund(String etf_fund) {
		this.etf_fund = etf_fund;
	}

	/**
	* @return 获得 qdii_fund
	*/
	public String getQdii_fund() {
		return qdii_fund;
	}

	
	/**
	* @param qdii_fund 设置 qdii_fund
	*/
	public void setQdii_fund(String qdii_fund) {
		this.qdii_fund = qdii_fund;
	}

	/**
	* @return 获得 other_fund
	*/
	public String getOther_fund() {
		return other_fund;
	}
	
	/**
	* @param other_fund 设置 other_fund
	*/
	public void setOther_fund(String other_fund) {
		this.other_fund = other_fund;
	}
	
	/**
	* @return 获得 cashbao
	*/
	public String getCashbao() {
		return cashbao;
	}
	
	/**
	* @param cashbao 设置 cashbao
	*/
	public void setCashbao(String cashbao) {
		this.cashbao = cashbao;
	}
	
	
	
	
}
