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
import com.forestry.model.sys.param.Member_week_rationParameter;
import com.forestry.model.sys.param.OperateParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;


/**
 * 
 *  Class Name: Member_week_ration.java
 *  Function:会员比率
 *  Modifications:   
 *  @author TongXin  DateTime 2015年9月11日 上午11:36:03    
 *  @version 1.0
 */
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Member_week_ration extends Member_week_rationParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	private String sdate;
	private String edate;
	private String num_common;
	private String sum_common;
	private String num_vip;
	private String sum_vip;
	private String ration_vip;
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String getNum_common() {
		return num_common;
	}
	public void setNum_common(String num_common) {
		this.num_common = num_common;
	}
	public String getSum_common() {
		return sum_common;
	}
	public void setSum_common(String sum_common) {
		this.sum_common = sum_common;
	}
	public String getNum_vip() {
		return num_vip;
	}
	public void setNum_vip(String num_vip) {
		this.num_vip = num_vip;
	}
	public String getSum_vip() {
		return sum_vip;
	}
	public void setSum_vip(String sum_vip) {
		this.sum_vip = sum_vip;
	}
	public String getRation_vip() {
		return ration_vip;
	}
	public void setRation_vip(String ration_vip) {
		this.ration_vip = ration_vip;
	}
	
	
	
	 
}
