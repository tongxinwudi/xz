package com.forestry.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.forestry.model.sys.param.FundRateParameter;

@Entity
@Table(name = "fund_rate")
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FundRate extends FundRateParameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2975294678191671084L;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	@Column(name="fdcode")
	private String fdcode;
	@Column(name="name")
	private String name;
	@Column(name="sellrate")
	private Double sellrate;
	@Column(name="managerate")
	private Double managerate;
	@Column(name="type")
	private String type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFdcode() {
		return fdcode;
	}
	public void setFdcode(String fdcode) {
		this.fdcode = fdcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSellrate() {
		return sellrate;
	}
	public void setSellrate(Double sellrate) {
		this.sellrate = sellrate;
	}
	public Double getManagerate() {
		return managerate;
	}
	public void setManagerate(Double managerate) {
		this.managerate = managerate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
