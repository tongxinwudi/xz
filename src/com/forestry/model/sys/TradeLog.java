/**
 * 
 */
package com.forestry.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.forestry.model.sys.param.ErrorParameter;

/**
 * @author Steve
 *	活期宝错误qj_cashbao_db库的交易日志表
 */
@Entity
@Table(name = "operat_log")
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TradeLog extends ErrorParameter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8063477497999881335L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	@Column(name="uid",nullable=false)
	private String uid;
	@Column(name="remark")
	private String remark;
	@Column(name="opDate",nullable=false)
	private Date opDate;
	@Column(name="state")
	private int state;
	public int getState() {
		return state;
	}
	public void setState(int state) {
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getOpDate() {
		return opDate;
	}
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
}
