/**
 * 
 */
package com.forestry.service.sys.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.forestry.dao.sys.FundRateDao;
import com.forestry.model.sys.FundRate;
import com.forestry.service.sys.FundRateService;

import core.service.BaseService;

/**
 * @author Steve
 *
 */
@Service
public class FundRateServiceImpl extends BaseService<FundRate> implements FundRateService {

	private FundRateDao fundRateDao;
	
	@Resource
	public void setFundRateDao(FundRateDao dao){
		this.fundRateDao=dao;
		super.dao=dao;
	}

	/* (non-Javadoc)
	 * @see com.forestry.service.sys.FundRateService#getFundRateCount(com.forestry.model.sys.FundRate)
	 */
	@Override
	public Long getFundRateCount(FundRate entity) {
		return fundRateDao.getFundRateCount(entity);
	}

	/* (non-Javadoc)
	 * @see com.forestry.service.sys.FundRateService#getFundRate(com.forestry.model.sys.FundRate)
	 */
	@Override
	public List<FundRate> getFundRate(FundRate entity) {
		return fundRateDao.getFundRate(entity);
	}

	public FundRate getFundRateByCode(String fdcode){
		return fundRateDao.getFundRateByCode(fdcode);
	}
}
