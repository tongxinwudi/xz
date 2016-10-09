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
import com.forestry.model.sys.param.MoneyParameter;
import com.forestry.model.sys.param.NewOldParameter;
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
public class Money extends MoneyParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	 
	private String date;
	private String cashbao_num;
	private String single_num;
	private String jinqu_num;
	private String baoshou_num;
	private String wenjian_num;
	private String licai_num;
	private String licai2_num;
	private String yanglao_num;
	private String maifang_num;
	private String zinv_num;
	private String jiehun_num;
	private String duanqi_num;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
 
	public String getCashbao_num() {
		return cashbao_num;
	}
	public void setCashbao_num(String cashbao_num) {
		this.cashbao_num = cashbao_num;
	}
	public String getSingle_num() {
		return single_num;
	}
	public void setSingle_num(String single_num) {
		this.single_num = single_num;
	}
	public String getJinqu_num() {
		return jinqu_num;
	}
	public void setJinqu_num(String jinqu_num) {
		this.jinqu_num = jinqu_num;
	}
	public String getBaoshou_num() {
		return baoshou_num;
	}
	public void setBaoshou_num(String baoshou_num) {
		this.baoshou_num = baoshou_num;
	}
	 
	
	public String getWenjian_num() {
		return wenjian_num;
	}
	public void setWenjian_num(String wenjian_num) {
		this.wenjian_num = wenjian_num;
	}
	public String getLicai_num() {
		return licai_num;
	}
	public void setLicai_num(String licai_num) {
		this.licai_num = licai_num;
	}
	public String getLicai2_num() {
		return licai2_num;
	}
	public void setLicai2_num(String licai2_num) {
		this.licai2_num = licai2_num;
	}
	public String getYanglao_num() {
		return yanglao_num;
	}
	public void setYanglao_num(String yanglao_num) {
		this.yanglao_num = yanglao_num;
	}
	public String getMaifang_num() {
		return maifang_num;
	}
	public void setMaifang_num(String maifang_num) {
		this.maifang_num = maifang_num;
	}
	public String getZinv_num() {
		return zinv_num;
	}
	public void setZinv_num(String zinv_num) {
		this.zinv_num = zinv_num;
	}
	public String getJiehun_num() {
		return jiehun_num;
	}
	public void setJiehun_num(String jiehun_num) {
		this.jiehun_num = jiehun_num;
	}
	public String getDuanqi_num() {
		return duanqi_num;
	}
	public void setDuanqi_num(String duanqi_num) {
		this.duanqi_num = duanqi_num;
	}
	 
	

	 
	 
	
	
 

	 
	
}
