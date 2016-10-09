/**
 * 
 */
package com.forestry.dao.sys.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.forestry.dao.sys.FundRateDao;
import com.forestry.model.sys.FundRate;

import core.dao.BaseDao;
import core.support.BaseParameter;
import core.support.QueryResult;
import core.util.StringUtil;

/**
 * @author Steve
 *
 */
@Repository
public class FundRateDaoImpl extends BaseDao<FundRate> implements FundRateDao {

	public FundRateDaoImpl(){
		super(FundRate.class);
	}
	
	@Override
	public Long getFundRateCount(FundRate entity){
		String hql="select count(*) from FundRate "+ createWhereHql(entity);
		return (Long)getSession().createQuery(hql).uniqueResult();
	}
	
	@Override
	public List<FundRate> getFundRate(FundRate entity){
		String hql=" from FundRate "+ createWhereHql(entity);
		return getSession().createQuery(hql).setFirstResult(entity.getFirstResult()).setMaxResults(entity.getMaxResults()).list();
	}
	
	public FundRate getFundRateByCode(String fdcode){
		String hql=" from FundRate where fdcode='"+fdcode+"'";
		List list=getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0)
			return (FundRate)list.get(0);
		return null;
	}

	public String createWhereHql(FundRate entity){
		String hql = " where 1=1 ";
		String key = entity.getSortedConditions().keySet().iterator().next();
		String value = entity.getSortedConditions().values().iterator().next();

		if (!"".equals(StringUtil.trimNull(entity.getName()))) {
			hql += " and name like '%" + entity.getName() + "%'";
		}
		if (!"".equals(StringUtil.trimNull(entity.getFdcode()))) {
			hql += " and fdcode like '%" + entity.getFdcode() + "%'";
		}
		
		if (!"".equals(StringUtil.trimNull(entity.getType()))) 
			hql += " and type='" + entity.getType() + "'";
		hql += " order by " + key + " " + value;		
		//		+ " limit "+ entity.getFirstResult() + "," + entity.getMaxResults();
		return hql;
	}
		
}
