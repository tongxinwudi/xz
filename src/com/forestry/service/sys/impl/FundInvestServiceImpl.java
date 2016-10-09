/**
 * 
 */
package com.forestry.service.sys.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.forestry.dao.sys.FundInvestDao;
import com.forestry.model.sys.OperatLog;
import com.forestry.service.sys.FundInvestService;

import core.service.BaseService;

/**
 * @author Steve
 *
 */
@Service
public class FundInvestServiceImpl extends BaseService<OperatLog> implements
		FundInvestService {
	
	private FundInvestDao fiDao;
	
	@Resource
	public void setFiDao(FundInvestDao fiDao){
		this.fiDao=fiDao;
		this.dao=fiDao;
	}

	@Override
	public long fundInvestSumCount(OperatLog op) {
		return fiDao.fundInvestSumCount(op);
	}
	
	@Override
	public List<OperatLog> fundInvestSum(OperatLog op,boolean isPage) {
		return fiDao.fundInvestSum(op,isPage);
	}

	@Override
	public long fundInvestEverydayCount(OperatLog op) {
		return fiDao.fundInvestEverydayCount(op);
	}
	
	@Override
	public List<OperatLog> fundInvestEveryday(OperatLog op,boolean isPage) {
		return fiDao.fundInvestEveryday(op,isPage);
	}

}
