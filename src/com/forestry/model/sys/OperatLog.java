package com.forestry.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.base.Objects;

import core.extjs.ExtJSBaseParameter;

/**
 * 
 * @author 高辉
 * qj_fund_db库的操作日志表，查询交易基金错误
 */
@Entity
@Table(name = "operat_log")
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OperatLog extends ExtJSBaseParameter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2238938548151956466L;
	@Id
	@GeneratedValue
	@Column(name="opid")
	private Long id;
	@Column(name="uid",nullable=false)
	private String uid;
	@Column(name="reason")
	private String reason;
	@Column(name="opDate",nullable=false)
	private Date opDate;
	@Column(name="state")
	private Integer state;
	@Column(name="partner")
	private Integer partner;
	@Column(name="fdcode")
	private String fdcode;
	@Column(name="abbrev")
	private String abbrev;
	@Column(name="sum")
	private Double sum;
	
	private String opStart;
	private String opEnd;
	
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getOpDate() {
		return opDate;
	}
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
	
	
	public Integer getPartner() {
		return partner;
	}
	public void setPartner(Integer partner) {
		this.partner = partner;
	}
	public String getFdcode() {
		return fdcode;
	}
	public void setFdcode(String fdcode) {
		this.fdcode = fdcode;
	}
	public String getAbbrev() {
		return abbrev;
	}
	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	
	public String getOpStart() {
		return opStart;
	}
	public void setOpStart(String opStart) {
		this.opStart = opStart;
	}
	public String getOpEnd() {
		return opEnd;
	}
	public void setOpEnd(String opEnd) {
		this.opEnd = opEnd;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final OperatLog other = (OperatLog) obj;
		return Objects.equal(this.id, other.id) && Objects.equal(this.uid, other.uid) && Objects.equal(this.reason, other.reason) && Objects.equal(this.opDate, other.opDate);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.id, this.uid, this.reason,this.opDate);
	}
}
