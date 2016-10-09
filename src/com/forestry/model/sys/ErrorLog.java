package com.forestry.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import core.extjs.ExtJSBaseParameter;

/**
 * 错误日志表
 * @author 高辉
 *
 */
@Entity
@Table(name = "error_log")
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ErrorLog extends ExtJSBaseParameter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3622592237540993296L;
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id; // ID
	@Column(name = "uid", length = 16)
	private String uid; 
	@Column(name = "mobile", length = 16)
	private String mobile;
	@Column(name = "capitalmode", length = 1)
	private String capitalmode; // 渠道
	@Column(name = "type", length = 20, nullable = true)
	private String type; // 错误类型
	@Column(name = "card", length = 32, nullable = true)
	private String card; // 银行卡
	@Column(name="ecode")
	private String ecode;
	@Column(name="from_page")
	private String from_page;
	@Column(name="emsg")
	private String emsg;
	@Column(name="extra")
	private String extra;
	@Column(name = "error_time")
	private String error_time; // 错误发生时间
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date create_time; // 错误发生时间
	@Column(name="issend")
	private Integer issend;
	
	public ErrorLog(){
		
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCapitalmode() {
		return capitalmode;
	}

	public void setCapitalmode(String capitalmode) {
		this.capitalmode = capitalmode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getEcode() {
		return ecode;
	}

	public void setEcode(String ecode) {
		this.ecode = ecode;
	}

	public String getFrom_page() {
		return from_page;
	}

	public void setFrom_page(String from_page) {
		this.from_page = from_page;
	}

	public String getEmsg() {
		return emsg;
	}

	public void setEmsg(String emsg) {
		this.emsg = emsg;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getError_time() {
		return error_time;
	}

	public void setError_time(String error_time) {
		this.error_time = error_time;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getIssend() {
		return issend;
	}

	public void setIssend(Integer issend) {
		this.issend = issend;
	}
	
}
