/**
 * 
 */
package com.forestry.service.sys;

import java.util.List;

import com.forestry.model.sys.OperatLog;

import core.service.Service;

/**
 * 基金投资统计
 * @author Steve
 *
 */
public interface FundInvestService extends Service<OperatLog> {
	/**
	 * 
	 * @param op
	 * @return
	 */
	long fundInvestSumCount(OperatLog op);
	
	/**
	 * 各基金申购的总额统计
	 * @param op
	 * @return
	 */
	List<OperatLog> fundInvestSum(OperatLog op,boolean isPage);
	
	long fundInvestEverydayCount(OperatLog op);
	
	/**
	 * 单个基金每天的申购额
	 * @param op
	 * @return
	 */
	List<OperatLog> fundInvestEveryday(OperatLog op,boolean isPage);
}
