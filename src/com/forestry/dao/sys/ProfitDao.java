package com.forestry.dao.sys;

import java.util.List;

import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.MonthInvestGraph;
import com.forestry.model.sys.Profit;

import core.dao.Dao;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public interface ProfitDao extends Dao<Profit> {
	
	//List<Object[]> queryExportedForestry(Long[] ids);
	List<Profit> getProfitList(Profit profit);
	List<MonthInvestGraph> getProfitGraphList(Profit profit);
	long getProfitListCount(Profit profit);
	//List<Profit> getProfitGraphList(Profit profit);
	void static_month_invest();
}
