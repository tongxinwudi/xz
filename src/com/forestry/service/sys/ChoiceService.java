package com.forestry.service.sys;

import java.util.List;
import java.util.Map;

import com.forestry.model.sys.Choice;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.Profit;

import core.service.Service;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public interface ChoiceService extends Service<Choice> {

	 
 
	List<Choice> getChoiceSelfList(Choice choice);
	List<Choice> getChoiceRedempSelfList(Choice choice);
	long getChoiceSelfCount(Choice choice);
	List<Choice> getChoiceSelfGraphList(Choice choice);
	List getMonthSumLineGraph(Choice choice);
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
	String getSchemaMonthDistribute(int type);
	
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
	String getSchemaMonthSumDistribute(int type);
	
	 /**
	 *  Function:
	 *  @author TongXin  DateTime 2015年8月24日 下午1:58:35
	 *  @param choice
	 *  @return
	 */
	List getMonthRedempLineGraph(Choice choice);
}
