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
 *	绑卡出现错误qj_user_db.bank_account 
 */
@Entity
@Table(name = "bank_account")
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BankAccount extends ErrorParameter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8510069867571794960L;
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	@Column(name="uid",nullable=false)
	private String uid;
	@Column(name="stateInfo")
	private String stateInfo;
	@Column(name="boundT")
	private Date boundT;
	@Column(name="state")
	private int state;
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
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	public Date getBoundT() {
		return boundT;
	}
	public void setBoundT(Date boundT) {
		this.boundT = boundT;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
