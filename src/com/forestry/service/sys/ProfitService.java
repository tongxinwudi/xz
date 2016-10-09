package com.forestry.service.sys;

import java.util.List;

import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.MonthInvestGraph;
import com.forestry.model.sys.Profit;

import core.service.Service;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
public interface ProfitService extends Service<Profit> {

	List<Profit> getProfitList(Profit profit);
	long getProfitListCount(Profit profit);
	List<MonthInvestGraph> getProfitGraphList(Profit profit);
	//List<Object[]> queryExportedForestry(Long[] ids);
	
	 /**
	 *  Function:
	 *  @author TongXin  DateTime 2015年8月18日 下午8:22:19
	 */
	void static_month_invest();

}
