package com.forestry.service.sys;

import java.util.List;
import java.util.Map;

import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.FundHold;
import com.forestry.model.sys.FundLimit;
import com.forestry.model.sys.Profit;

import core.service.Service;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public interface FundService extends Service<Fund> {

	 
	List<Fund> getFundLimitGraphList(Fund fund);
	List<Fund> getFundList(Fund fund);
	long getFundListCount(Fund fund);
	List<Fund> getFundFrequencyList(Fund fund);
	long getFundFrequencyCount(Fund fund);
	void statistic_month_limit();
	List<FundLimit> getFundlimtLineGraph(FundLimit fundLimit);
	/**
	 * 获取所有基金的持有时间
	 * @return
	 */
	List<FundHold> getAllFundHoldTime(FundHold fund);
	
	/**
	 * 获取所有基金数量
	 * @return
	 */
	int getAllFundCount(FundHold fundHold);
	
	/**
	 * 保存基金的持有时间
	 */
	void saveFundHoldTime();
	
	/**
	 * 按月份统计基金的总申购频率
	 * @return
	 */
	String getFundFrequencyMonth();
	
	/**
	 * 按月份统计投资组合的总申购频率
	 * @return
	 */
	String getAssemblyFrequencyMonth();
	
	/**
	 * 按月份统计现金宝的总申购频率
	 * @return
	 */
	String getCashbaoFrequencyMonth();
}
