/**
 * 
 */
package com.forestry.service.sys.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.forestry.dao.sys.RateConfigDao;
import com.forestry.model.sys.RateConfig;
import com.forestry.service.sys.RateConfigService;

import core.service.BaseService;
import core.support.BaseParameter;
import core.support.QueryResult;

/**
 * @author Steve
 *
 */
@Service
public class RateConfigServiceImpl extends BaseService<RateConfig> implements
		RateConfigService {

	private RateConfigDao rcDao;
	
	@Resource
	public void setRcDao(RateConfigDao dao){
		this.rcDao=dao;
		super.dao=dao;
	}

	/* (non-Javadoc)
	 * @see com.forestry.service.sys.RateConfigService#getAllRateConfig()
	 */
	@Override
	public List<RateConfig> getAllRateConfig() {
		return rcDao.getAllRateConfig();
	}
	
	@Override
	public Long getAllRateConfigCount(){
		return rcDao.getAllRateConfigCount();
	}

}
