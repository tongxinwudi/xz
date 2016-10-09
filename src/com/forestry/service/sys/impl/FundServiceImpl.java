package com.forestry.service.sys.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.forestry.dao.sys.AttachmentDao;
import com.forestry.dao.sys.ForestryDao;
import com.forestry.dao.sys.ForestryTypeDao;
import com.forestry.dao.sys.FundDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.Attachment;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.FundHold;
import com.forestry.model.sys.FundLimit;
import com.forestry.model.sys.Profit;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.FundService;
import com.forestry.service.sys.ProfitService;

import core.service.BaseService;

/**
 * 
 *  Class Name: ProfitServiceImpl.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年7月13日 下午3:23:50    
 *  @version 1.0
 */
@Service
public class FundServiceImpl extends BaseService<Fund> implements FundService {

	@Resource
	private FundDao fundDao;

 
	@Override
	public List<Fund> getFundLimitGraphList(Fund fund) {
		// TODO Auto-generated method stub
		return fundDao.getFundLimitGraphList(fund);
	}


	@Override
	public List<Fund> getFundList(Fund fund) {
		// TODO Auto-generated method stub
		return fundDao.getFundList(fund);
	}


	@Override
	public long getFundListCount(Fund fund) {
		// TODO Auto-generated method stub
		return fundDao.getFundListCount(fund);
	}
	 
	
	@Override
	public List<Fund> getFundFrequencyList(Fund fund) {
		// TODO Auto-generated method stub
		return fundDao.getFundFrequencyList(fund);
	}


	@Override
	public long getFundFrequencyCount(Fund fund) {
		// TODO Auto-generated method stub
		return fundDao.getFundFrequencyCount(fund);
	}
	
	/**
	 * 按月份统计基金的总申购频率
	 * @return
	 */
	@Override
	public String getFundFrequencyMonth(){
		List list= fundDao.getFundFrequencyMonth();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			sb.append("{'buy':");
			sb.append(o[0] == null ? 0 : o[0]);
			sb.append(",'sale':");
			sb.append(o[1] == null ? 0 : o[1]);
			sb.append(",'opMonth':'");
			sb.append(o[2]);
			sb.append("'},");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 按月份统计投资组合的总申购频率
	 * @return
	 */
	@Override
	public String getAssemblyFrequencyMonth(){
		List list= fundDao.getAssemblyFrequencyMonth();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			sb.append("{'buy':");
			sb.append(o[0] == null ? 0 : o[0]);
			sb.append(",'sale':");
			sb.append(o[1] == null ? 0 : o[1]);
			sb.append(",'opMonth':'");
			sb.append(o[2]);
			sb.append("'},");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
 
	/**
	 * 按月份统计现金宝的总申购频率
	 * @return
	 */
	@Override
	public String getCashbaoFrequencyMonth(){
		List list= fundDao.getCashbaoFrequencyMonth();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			sb.append("{'buy':");
			sb.append(o[0] == null ? 0 : o[0]);
			sb.append(",'sale':");
			sb.append(o[1] == null ? 0 : o[1]);
			sb.append(",'opMonth':'");
			sb.append(o[2]);
			sb.append("'},");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金和金额
	 * @return
	 */
	public List getSaleAllOnceFund(){
		return fundDao.getSaleAllOnceFund();
	}
	
	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金持有时间
	 * @return
	 */
	public Map<String,Integer> getSaleAllOnceFundHoldTime(){
		return fundDao.getSaleAllOnceFundHoldTime();
	}
	
	/**
	 * 单只基金，一次或多次申购，一次赎回部分基金
	 * @return
	 */
	public List getSalePartOnceFund(){
		return fundDao.getSalePartOnceFund();
	}
	
	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金持有时间
	 * @return
	 */
	public Map<String,Integer> getSalePartOnceFundHoldTime(){
		return fundDao.getSalePartOnceFundHoldTime();
	}
	
	/**
	 * 单只基金，一次或多次申购，未赎回的基金
	 * @return
	 */
	public List getNoSaleFund(){
		return fundDao.getNoSaleFund();
	}
	
	/**
	 * 单只基金，一次或多次申购，未赎回基金的持有时间
	 * @return
	 */
	public Map<String,Integer> getNoSaleFundHoldTime(){
		return fundDao.getNoSaleFundHoldTime();
	}
	
	/**
	 * 单只基金，一次或多次申购，多次赎回
	 * @return
	 */
	public List getSaleMoreThanOnceFund(){
		return fundDao.getSaleMoreThanOnceFund();
	}
	
	/**
	 * 根据基金号和用户编号返回对应类型的基金操作记录
	 * @param fdcode，基金号
	 * @param uid，用户编码
	 * @param operate，操作类型
	 * @return
	 */
	public List getFundByCodeAndUid(String fdcode,String uid,int operate){
		return fundDao.getFundByCodeAndUid(fdcode, uid, operate);
	}
	
	/**
	 * 获取所有基金的持有时间
	 * @return
	 */
	@Override
	public List<FundHold> getAllFundHoldTime(FundHold fund){
		
		return fundDao.getAllFundHoldTime(fund);
	}
	
	/**
	 * 获取所有基金数量
	 * @return
	 */
	@Override
	public int getAllFundCount(FundHold fundHold){
		return fundDao.getAllFundCount(fundHold);
	}
	
	/**
	 * 保存基金的持有时间
	 */
	@Override
	public void saveFundHoldTime(){
		fundDao.saveFundHoldTime();
	}


	/* (非 Javadoc) 
	* <p>Title: statistic_month_limit</p> 
	* <p>Description: </p>  
	* @see com.forestry.service.sys.FundService#statistic_month_limit() 
	*/
	@Override
	public void statistic_month_limit() {
		// TODO Auto-generated method stub
		fundDao.statistic_month_limit();
	}


	/* (非 Javadoc) 
	* <p>Title: getFundlimtLineGraph</p> 
	* <p>Description: </p> 
	* @return 
	* @see com.forestry.service.sys.FundService#getFundlimtLineGraph() 
	*/
	@Override
	public List<FundLimit> getFundlimtLineGraph(FundLimit fundLimit) {
		// TODO Auto-generated method stub
		return fundDao.getFundlimtLineGraph(fundLimit);
	}
}
