package com.forestry.dao.sys;

import java.util.List;

import com.forestry.model.sys.OperatLog;

import core.dao.Dao;

/**
 * 交易基金错误qj_fund_db
 * @author 高辉
 *
 */
public interface FundOperatLogDao extends Dao<OperatLog> {
	/**
	 * 获取交易基金类错误总数
	 * @return
	 */
	Long getErrorCount();
	/**
	 * 获取某个用户的交易基金类错误总数
	 * @return
	 */
	Long getUserErrorCount(String uid);
	/**
	 * 获取交易基金类错误列表
	 * @return
	 */
	List<OperatLog> getAllErrorInfoList();
	/**
	 * 获取某个用户的交易基金类错误列表
	 * @return
	 */
	List<OperatLog> getUserErrorInfo(String uid);
}
