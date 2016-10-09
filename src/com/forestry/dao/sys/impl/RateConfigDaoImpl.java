/**
 * 
 */
package com.forestry.dao.sys.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.forestry.dao.sys.RateConfigDao;
import com.forestry.model.sys.RateConfig;

import core.dao.BaseDao;
import core.support.BaseParameter;
import core.support.QueryResult;

/**
 * @author Steve
 *
 */
@Repository
public class RateConfigDaoImpl extends BaseDao<RateConfig> implements
		RateConfigDao {
	
	public RateConfigDaoImpl(){
		super(RateConfig.class);
	}

	/* (non-Javadoc)
	 * @see com.forestry.dao.sys.RateConfigDao#getAllRateConfig()
	 */
	@Override
	public List<RateConfig> getAllRateConfig() {
		String hql=" from RateConfig ";
		return getSession().createQuery(hql).list();
	}

	@Override
	public Long getAllRateConfigCount(){
		String hql="select count(*) from RateConfig ";
		return (Long)getSession().createQuery(hql).uniqueResult();
	}
}
