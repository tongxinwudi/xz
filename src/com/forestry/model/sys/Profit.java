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
 * @author Yang Tian
 * @email 1298588579@qq.com
 * @树木信息实体类
 */

@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Profit extends ProfitParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	private String id;
	private String name;
	private String mobile;
	private String profit;
	private String invest;
	private String graphData;
	private String graphName;
	private String minus;
	private String rates;
	private String redemping;
	
	
	public String getId() {
		return id;
	}
	public String getMinus() {
		return minus;
	}
	public void setMinus(String minus) {
		this.minus = minus;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getInvest() {
		return invest;
	}
	public void setInvest(String invest) {
		this.invest = invest;
	}
	public String getGraphData() {
		return graphData;
	}
	public void setGraphData(String graphData) {
		this.graphData = graphData;
	}
	public String getGraphName() {
		return graphName;
	}
	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}
	public String getRates() {
		return rates;
	}
	public void setRates(String rates) {
		this.rates = rates;
	}
	
	/**
	* @return 获得 redemping
	*/
	public String getRedemping() {
		return redemping;
	}
	
	/**
	* @param redemping 设置 redemping
	*/
	public void setRedemping(String redemping) {
		this.redemping = redemping;
	}
	
	
}
