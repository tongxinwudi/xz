/**
 * 
 */
package com.forestry.dao.sys;

import java.util.List;

import com.forestry.model.sys.FundRate;

import core.dao.Dao;

/**
 * @author Steve
 *
 */
public interface FundRateDao extends Dao<FundRate> {

	Long getFundRateCount(FundRate entity);
	
	List<FundRate> getFundRate(FundRate entity);
	
	FundRate getFundRateByCode(String fdcode);
}
