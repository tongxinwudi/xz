package com.forestry.service.sys.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.forestry.dao.sys.AttachmentDao;
import com.forestry.dao.sys.ChoiceDao;
import com.forestry.dao.sys.ForestryDao;
import com.forestry.dao.sys.ForestryTypeDao;
import com.forestry.dao.sys.FundDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.Attachment;
import com.forestry.model.sys.Choice;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.Profit;
import com.forestry.service.sys.ChoiceService;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.FundService;
import com.forestry.service.sys.ProfitService;

import core.service.BaseService;

/**
 * 
 * Class Name: ProfitServiceImpl.java Function: Modifications:
 * 
 * @author TongXin DateTime 2015年7月13日 下午3:23:50
 * @version 1.0
 */
@Service
public class ChoiceServiceImpl extends BaseService<Choice> implements
		ChoiceService {

	@Resource
	private ChoiceDao choiceDao;

	@Override
	public List<Choice> getChoiceSelfList(Choice choice) {
		// TODO Auto-generated method stub
		return choiceDao.getChoiceSelfList(choice);
	}

	@Override
	public long getChoiceSelfCount(Choice choice) {
		// TODO Auto-generated method stub
		return choiceDao.getChoiceSelfCount(choice);
	}

	@Override
	public List<Choice> getChoiceSelfGraphList(Choice choice) {
		// TODO Auto-generated method stub
		return choiceDao.getChoiceSelfGraphList(choice);
	}

	/**
	 * 返回定制组合的性别分布
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSchemaUserCountBySex(int type) {
		return choiceDao.getSchemaUserCountBySex(type);
	}

	/**
	 * 返回定制组合的年龄分布
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSchemaUserCountByAge(int type) {
		return choiceDao.getSchemaUserCountByAge(type);
	}

	/**
	 * 返回定制组合各年龄段用户按月份的时间分布
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public String getSchemaMonthDistribute(int type) {
		List list = choiceDao.getSchemaMonthDistribute(type);
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			sb.append("{'opDate':");
			sb.append("'"+o[0]+"'");
			sb.append(",'90s':");
			sb.append(o[1] == null ? 0 : o[1]);
			sb.append(",'80s':");
			sb.append(o[2] == null ? 0 : o[2]);
			sb.append(",'70s':");
			sb.append(o[3] == null ? 0 : o[3]);
			sb.append(",'60s':");
			sb.append(o[4] == null ? 0 : o[4]);
			sb.append(",'50s':");
			sb.append(o[5] == null ? 0 : o[5]);
			sb.append(",'other':");
			sb.append(o[6] == null ? 0 : o[6]);
			sb.append("},");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 返回定制组合投资额的年龄分布
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getSchemaSumByAge(int type){
		return choiceDao.getSchemaSumByAge(type);
	}
	
	/**
	 * 返回定制组合投资额各年龄段用户按月份的分布
	 * @param type
	 * @return
	 */
	@Override
	public String getSchemaMonthSumDistribute(int type){
		List list = choiceDao.getSchemaMonthSumDistribute(type);
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			sb.append("{'opDate':");
			sb.append("'"+o[0]+"'");
			sb.append(",'90s':");
			sb.append(o[1] == null ? 0 : o[1]);
			sb.append(",'80s':");
			sb.append(o[2] == null ? 0 : o[2]);
			sb.append(",'70s':");
			sb.append(o[3] == null ? 0 : o[3]);
			sb.append(",'60s':");
			sb.append(o[4] == null ? 0 : o[4]);
			sb.append(",'50s':");
			sb.append(o[5] == null ? 0 : o[5]);
			sb.append(",'other':");
			sb.append(o[6] == null ? 0 : o[6]);
			sb.append("},");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}

	/* (非 Javadoc) 
	* <p>Title: getMonthSumLineGraph</p> 
	* <p>Description: </p> 
	* @param choice
	* @return 
	* @see com.forestry.service.sys.ChoiceService#getMonthSumLineGraph(com.forestry.model.sys.Choice) 
	*/
	@Override
	public List getMonthSumLineGraph(Choice choice) {
		// TODO Auto-generated method stub
		return choiceDao.getMonthSumLineGraph(choice);
	}

	/* (非 Javadoc) 
	* <p>Title: getChoiceRedempSelfList</p> 
	* <p>Description: </p> 
	* @param choice
	* @return 
	* @see com.forestry.service.sys.ChoiceService#getChoiceRedempSelfList(com.forestry.model.sys.Choice) 
	*/
	@Override
	public List<Choice> getChoiceRedempSelfList(Choice choice) {
		// TODO Auto-generated method stub
		return choiceDao.getChoiceRedempSelfGraphList(choice);
	}
	
	@Override
	public List getMonthRedempLineGraph(Choice choice) {
		// TODO Auto-generated method stub
		return choiceDao.getMonthRedempLineGraph(choice);
	}
}
