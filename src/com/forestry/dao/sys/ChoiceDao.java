package com.forestry.dao.sys;

import java.util.List;
import java.util.Map;

import com.forestry.model.sys.Choice;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.Profit;

import core.dao.Dao;

/**
 * 
 *  Class Name: ChoiceDao.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月21日 上午10:17:49    
 *  @version 1.0
 */
public interface ChoiceDao extends Dao<Choice> {
	
	 
	 
	List<Choice> getChoiceSelfList(Choice choice);
	long getChoiceSelfCount(Choice choice);
	List<Choice> getChoiceSelfGraphList(Choice choice);
	
	/**
	 * 返回定制组合的性别分布
	 * @param type
	 * @return
	 */
	List<Map<String,Object>> getSchemaUserCountBySex(int type);
	
	/**
	 * 返回定制组合的年龄分布
	 * @param type
	 * @return
	 */
	List<Map<String,Object>> getSchemaUserCountByAge(int type);
	/**
	 * 返回定制组合各年龄段用户按月份的时间分布
	 * @param type
	 * @return
	 */
	List getSchemaMonthDistribute(int type);
	
	/**
	 * 返回定制组合投资额的年龄分布
	 * @param type
	 * @return
	 */
	List<Map<String, Object>> getSchemaSumByAge(int type);
	
	/**
	 * 返回定制组合投资额各年龄段用户按月份的分布
	 * @param type
	 * @return
	 */
	List getSchemaMonthSumDistribute(int type);
	
	 /**
	 *  Function:
	 *  @author TongXin  DateTime 2015年8月21日 下午8:14:05
	 *  @param choice
	 *  @return
	 */
	List getMonthSumLineGraph(Choice choice);
	
	 /**
	 *  Function:
	 *  @author TongXin  DateTime 2015年8月24日 下午12:04:38
	 *  @param choice
	 *  @return
	 */
	List<Choice> getChoiceRedempSelfGraphList(Choice choice);
	
	 /**
	 *  Function:
	 *  @author TongXin  DateTime 2015年8月24日 下午1:53:54
	 *  @param choice
	 *  @return
	 */
	List getMonthRedempLineGraph(Choice choice);
}
