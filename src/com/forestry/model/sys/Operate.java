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
import com.forestry.model.sys.param.OperateParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 * @树木信息实体类
 */

@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Operate extends OperateParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	public String date;//日期
	public String investTotalPersonNum;//申购总人数
	public String investTotalNum;//申购总笔数
	public String investTotalMoney;//申购总金额
	public String investFailPersonNum;//申购失败人数
	public String investFailNum;//申购失败笔数
	public String investFailMoney;//申购失败金额
	public String investSuccessPersonNum;//申购成功人数
	public String investSuccessNum;//申购成功笔数
	public String investSuccessMoney;//申购成功金额
	public String investPartSuccessNum;//部分申购成功笔数
	public String investPartSuccessMoney;//部分申购成功金额

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInvestTotalPersonNum() {
		return investTotalPersonNum;
	}

	public void setInvestTotalPersonNum(String investTotalPersonNum) {
		this.investTotalPersonNum = investTotalPersonNum;
	}

	public String getInvestTotalNum() {
		return investTotalNum;
	}

	public void setInvestTotalNum(String investTotalNum) {
		this.investTotalNum = investTotalNum;
	}

	public String getInvestTotalMoney() {
		return investTotalMoney;
	}

	public void setInvestTotalMoney(String investTotalMoney) {
		this.investTotalMoney = investTotalMoney;
	}

	public String getInvestFailPersonNum() {
		return investFailPersonNum;
	}

	public void setInvestFailPersonNum(String investFailPersonNum) {
		this.investFailPersonNum = investFailPersonNum;
	}

	public String getInvestFailMoney() {
		return investFailMoney;
	}

	public void setInvestFailMoney(String investFailMoney) {
		this.investFailMoney = investFailMoney;
	}

	public String getInvestSuccessNum() {
		return investSuccessNum;
	}

	public void setInvestSuccessNum(String investSuccessNum) {
		this.investSuccessNum = investSuccessNum;
	}

	public String getInvestSuccessMoney() {
		return investSuccessMoney;
	}

	public void setInvestSuccessMoney(String investSuccessMoney) {
		this.investSuccessMoney = investSuccessMoney;
	}

	public String getInvestPartSuccessNum() {
		return investPartSuccessNum;
	}

	public void setInvestPartSuccessNum(String investPartSuccessNum) {
		this.investPartSuccessNum = investPartSuccessNum;
	}

	public String getInvestPartSuccessMoney() {
		return investPartSuccessMoney;
	}

	public void setInvestPartSuccessMoney(String investPartSuccessMoney) {
		this.investPartSuccessMoney = investPartSuccessMoney;
	}

	public String getInvestFailNum() {
		return investFailNum;
	}

	public void setInvestFailNum(String investFailNum) {
		this.investFailNum = investFailNum;
	}

	public String getInvestSuccessPersonNum() {
		return investSuccessPersonNum;
	}

	public void setInvestSuccessPersonNum(String investSuccessPersonNum) {
		this.investSuccessPersonNum = investSuccessPersonNum;
	}
	
}
