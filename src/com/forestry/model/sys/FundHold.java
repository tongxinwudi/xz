package com.forestry.model.sys;

import java.util.Date;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.forestry.model.sys.param.FundHoldParameter;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 * @树木信息实体类
 */

@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FundHold extends FundHoldParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	private long id;
	private String fdcode;
	private String uid;
	private String name;		//应该是全称
	private String abbrev;		//应该是简称
	private int type;			//基金类型
	private int operate;		//操作类型
	private Date opDate;		//操作日期
	private double shares;
	private int sid;			//单只基金还是组合基金
	private long holdTime;		//持有时间
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFdcode() {
		return fdcode;
	}
	public void setFdcode(String fdcode) {
		this.fdcode = fdcode;
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
	public String getAbbrev() {
		return abbrev;
	}
	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getOperate() {
		return operate;
	}
	public void setOperate(int operate) {
		this.operate = operate;
	}
	public Date getOpDate() {
		return opDate;
	}
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
	public double getShares() {
		return shares;
	}
	public void setShares(double shares) {
		this.shares = shares;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public long getHoldTime() {
		return holdTime;
	}
	public void setHoldTime(long holdTime) {
		this.holdTime = holdTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
