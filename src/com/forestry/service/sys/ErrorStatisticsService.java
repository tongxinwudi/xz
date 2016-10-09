package com.forestry.service.sys;

import java.util.List;

import com.forestry.model.sys.ErrorCommonInfo;
import com.forestry.model.sys.ErrorCountView;

import core.service.Service;

public interface ErrorStatisticsService extends Service<ErrorCommonInfo> {
	/**
	 * @return
	 */
	List<ErrorCountView> getErrorCountPage(ErrorCommonInfo model);
	
	Long getTotalErrorCount(ErrorCommonInfo model);
	
	/**
	 * 获取交易类错误列表
	 * @return
	 */
	List<ErrorCommonInfo> getFundErrorList(ErrorCommonInfo model);
	
	/**
	 * 获取活期宝类错误列表
	 * @return
	 */
	List<ErrorCommonInfo> getCashbaoErrorList(ErrorCommonInfo model);
	
	/**
	 * 获取绑卡类错误列表
	 * @return
	 */
	List<ErrorCommonInfo> getUserErrorList(ErrorCommonInfo model);
	
	/**
	 * 获取交易类错误总数
	 * @return
	 */
	Long getFundErrorCount(ErrorCommonInfo model);
	
	/**
	 * 获取活期宝类错误总数
	 * @return
	 */
	Long getCashbaoErrorCount(ErrorCommonInfo model);
	
	/**
	 * 获取绑卡类错误总数
	 * @return
	 */
	Long getUserErrorCount(ErrorCommonInfo model);

	/**
	 * 获取所有错误列表
	 * @return
	 */
	List<ErrorCommonInfo> getAllErrorList(ErrorCommonInfo model);

	/**
	 * 按照年月返回各类错误总数、比例等信息
	 * @return
	 */
	String getErrorCountMonthPage(ErrorCommonInfo model);
}
