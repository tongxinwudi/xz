/**
 * 
 */
package com.forestry.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.forestry.model.sys.param.ErrorParameter;
import com.google.common.base.Objects;

/**
 * @author Steve
 *
 */
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ErrorCommonInfo extends ErrorParameter {
	private Long id;
	private String uid;
	private String name;
	private String mobile;
	private String reason;
	private Date opDate;
	private int state;
	private int errorType;		//错误类型，1=交易错误，2=活期宝错误，3=帮卡错误
	
	public int getErrorType() {
		return errorType;
	}
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}
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
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ErrorCommonInfo other = (ErrorCommonInfo) obj;
		return Objects.equal(this.uid, other.uid) && Objects.equal(this.reason, other.reason) && Objects.equal(this.opDate, other.opDate) && Objects.equal(this.state, other.state) && Objects.equal(this.errorType, other.errorType);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.uid, this.reason,this.opDate,this.state,this.errorType);
	}
}
