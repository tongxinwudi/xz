package com.forestry.dao.sys;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.FundHold;
import com.forestry.model.sys.FundLimit;
import com.forestry.model.sys.Profit;

import core.dao.Dao;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public interface FundDao extends Dao<Fund> {
	
	 
	List<Fund> getFundList(Fund fund);
	List<Fund> getFundLimitGraphList(Fund fund);
	long getFundListCount(Fund fund);
	List<Fund> getFundFrequencyList(Fund fund);
	long getFundFrequencyCount(Fund fund);
	void statistic_month_limit();
	List<FundLimit> getFundlimtLineGraph(FundLimit fundLimit);
	
	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金和金额
	 * @return
	 */
	List getSaleAllOnceFund();
	
	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金持有时间
	 * @return
	 */
	Map<String,Integer> getSaleAllOnceFundHoldTime();
	
	/**
	 * 单只基金，一次或多次申购，一次赎回部分基金
	 * @return
	 */
	List getSalePartOnceFund();
	
	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金持有时间
	 * @return
	 */
	Map<String,Integer> getSalePartOnceFundHoldTime();
	
	/**
	 * 单只基金，一次或多次申购，未赎回的基金
	 * @return
	 */
	List getNoSaleFund();
	
	/**
	 * 单只基金，一次或多次申购，未赎回基金的持有时间
	 * @return
	 */
	Map<String,Integer> getNoSaleFundHoldTime();
	
	/**
	 * 单只基金，一次或多次申购，多次赎回
	 * @return
	 */
	List getSaleMoreThanOnceFund();
	
	/**
	 * 根据基金号和用户编号返回对应类型的基金操作记录
	 * @param fdcode，基金号
	 * @param uid，用户编码
	 * @param operate，操作类型
	 * @return
	 */
	List<FundHold> getFundByCodeAndUid(String fdcode,String uid,int operate);
	/**
	 * 根据基金号和用户编号返回对应类型的早于给定id的基金操作记录
	 * @param fdcode
	 * @param uid
	 * @param operate
	 * @param opid
	 * @return
	 */
	Map<Double,Date> getEarlierFundBuyShare(String fdcode,String uid,int operate,long startId,long endId);
	
	/**
	 * 保存基金的持有时间
	 */
	void saveFundHoldTime();
	
	/**
	 * 获取所有基金的持有时间
	 * @return
	 */
	List<FundHold> getAllFundHoldTime(FundHold fundHold);
	
	/**
	 * 获取所有基金数量
	 * @return
	 */
	int getAllFundCount(FundHold fundHold);
	
	/**
	 * 现金宝，一次或多次申购，一次全部赎回的基金持有时间
	 * @return
	 */
	int getSaleAllOnceCashbaoHoldTime();
	
	/**
	 * 现金宝，一次或多次申购，只赎回过一次，但是没有完全赎回
	 * @return
	 */
	int getSalePartOnceCashbaoHoldTime();
	
	/**
	 * 现金宝，一次或多次申购，一次都没有赎回的持有时间
	 * @return
	 */
	int getNoSaleCashbaoHoldTime();
	
	/**
	 * 现金宝，一次或多次申购，多次赎回的用户
	 * @return
	 */
	List getSaleMoreThanOnceCashbao();
	
	/**
	 * 返回对应类型的早于给定id的用户现金宝操作记录
	 * @param uid
	 * @param operate
	 * @param opid
	 * @return
	 */
	Map<Double, Date> getEarlierCashbaoBuyShare(String uid, int operate,
			long startId, long endId);
	
	/**
	 * 根据现金宝用户的操作记录
	 * @param uid，用户编码
	 * @param operate，操作类型
	 * @return
	 */
	List<FundHold> getCashbaoByUid(String uid, int operate);
	
	/**
	 * 按月份统计基金的总申购频率
	 * @return
	 */
	List getFundFrequencyMonth();
	/**
	 * 按月份统计投资组合的总申购频率
	 * @return
	 */
	List getAssemblyFrequencyMonth();
	
	/**
	 * 按月份统计现金宝的总申购频率
	 * @return
	 */
	List getCashbaoFrequencyMonth();
}
