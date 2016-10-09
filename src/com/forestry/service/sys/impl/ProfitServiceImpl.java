package com.forestry.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.forestry.dao.sys.AttachmentDao;
import com.forestry.dao.sys.ForestryDao;
import com.forestry.dao.sys.ForestryTypeDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.Attachment;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.MonthInvestGraph;
import com.forestry.model.sys.Profit;
import com.forestry.service.sys.ForestryService;
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
public class ProfitServiceImpl extends BaseService<Profit> implements ProfitService {

	@Resource
	private ProfitDao profitDao;

	@Override
	public List<Profit> getProfitList(Profit profit) {
		// TODO Auto-generated method stub
		return profitDao.getProfitList(profit);
	}

	@Override
	public long getProfitListCount(Profit profit) {
		// TODO Auto-generated method stub
		return profitDao.getProfitListCount(profit);
	}

	@Override
	public List<MonthInvestGraph> getProfitGraphList(Profit profit) {
		// TODO Auto-generated method stub
		return profitDao.getProfitGraphList(profit);
	}
	 
	@Override
	public     void static_month_invest(){
		profitDao.static_month_invest();
	}
 

	 
}
