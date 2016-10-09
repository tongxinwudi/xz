package com.forestry.dao.sys;

import java.util.List;

import com.forestry.model.sys.ErrorCommonInfo;
import com.forestry.model.sys.ErrorCountView;

public interface ErrorDao {
	/**
	 * 获取错误总数
	 * @return
	 */
	long getErrorCount(ErrorCommonInfo model);
	/**
	 * 获取某个用户的错误总数
	 * @return
	 */
	long getUserErrorCount(String uid);
	/**
	 * 获取错误列表
	 * @return
	 */
	List<ErrorCommonInfo> getAllErrorInfoList(ErrorCommonInfo model);
	/**
	 * 获取某个用户的错误列表
	 * @return
	 */
	List<ErrorCommonInfo> getUserErrorInfo(ErrorCommonInfo model);
	/**
	 * 根据年、月获取交易基金类错误总数
	 * @return
	 */
	List getErrorCountByMonth(ErrorCommonInfo model);
}
