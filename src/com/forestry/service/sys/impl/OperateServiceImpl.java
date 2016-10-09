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
import com.forestry.dao.sys.OperateDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.Attachment;
import com.forestry.model.sys.Channel;
import com.forestry.model.sys.Channel2;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.Fail;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Inventory;
import com.forestry.model.sys.Money;
import com.forestry.model.sys.NewOld;
import com.forestry.model.sys.Operate;
import com.forestry.model.sys.Profit;
import com.forestry.model.sys.Settle;
import com.forestry.model.sys.TradeUser;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.OperateService;
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
public class OperateServiceImpl extends BaseService<Operate> implements OperateService {

	@Resource
	private OperateDao operateDao;

	@Override
	public List<Operate> getOperateList(Operate operate) {
		// TODO Auto-generated method stub
		return operateDao.getOperateList(operate);
	}

	@Override
	public long getOperateListCount(Operate operate) {
		// TODO Auto-generated method stub
		return operateDao.getOperateListCount(operate);
	}
	
	@Override
	public void statistic_day_invest(){
		operateDao.statistic_day_invest();
	}

	/* (非 Javadoc) 
	* <p>Title: getOperateList</p> 
	* <p>Description: </p> 
	* @param fail
	* @return 
	* @see com.forestry.service.sys.OperateService#getOperateList(com.forestry.model.sys.Fail) 
	*/
	@Override
	public List<Fail> getFailList(Fail fail) {
		// TODO Auto-generated method stub
		return operateDao.getFailList(fail);
	}

	/* (非 Javadoc) 
	* <p>Title: getFailListCount</p> 
	* <p>Description: </p> 
	* @param fail
	* @return 
	* @see com.forestry.service.sys.OperateService#getFailListCount(com.forestry.model.sys.Fail) 
	*/
	@Override
	public long getFailListCount(Fail fail) {
		// TODO Auto-generated method stub
		return operateDao.getFailListCount(fail);
	}

	/* (非 Javadoc) 
	* <p>Title: statistic_day_fail</p> 
	* <p>Description: </p>  
	* @see com.forestry.service.sys.OperateService#statistic_day_fail() 
	*/
	@Override
	public void statistic_day_fail() {
		// TODO Auto-generated method stub
		operateDao.statistic_day_fail();
	}

	/* (非 Javadoc) 
	* <p>Title: getChannelList</p> 
	* <p>Description: </p> 
	* @param channel
	* @return 
	* @see com.forestry.service.sys.OperateService#getChannelList(com.forestry.model.sys.Channel) 
	*/
	@Override
	public List<Channel> getChannelList(Channel channel) {
		// TODO Auto-generated method stub
		return operateDao.getChannelList(channel);
	}

	/* (非 Javadoc) 
	* <p>Title: getChannelListCount</p> 
	* <p>Description: </p> 
	* @param channel
	* @return 
	* @see com.forestry.service.sys.OperateService#getChannelListCount(com.forestry.model.sys.Channel) 
	*/
	@Override
	public long getChannelListCount(Channel channel) {
		// TODO Auto-generated method stub
		return operateDao.getChannelListCount(channel);
	}

	@Override
	public List<TradeUser> getTradeUserlList(TradeUser tradeUser) {
		// TODO Auto-generated method stub
		return operateDao.getTradeUserList(tradeUser);
	}

	@Override
	public long getTradeUserListCount(TradeUser tradeUser) {
		// TODO Auto-generated method stub
		return operateDao.getTradeUserListCount(tradeUser);
	}

	@Override
	public List<NewOld> getNewOldlList(NewOld newOld) {
		// TODO Auto-generated method stub
		return operateDao.getNewOldList(newOld);
	}

	@Override
	public long getNewOldListCount(NewOld newOld) {
		// TODO Auto-generated method stub
		return operateDao.getNewOldListCount(newOld);
	}

	@Override
	public List<Money> getMoneylList(Money money) {
		// TODO Auto-generated method stub
		return operateDao.getMoneyList(money);
	}

	@Override
	public long getMoneyListCount(Money money) {
		// TODO Auto-generated method stub
		return operateDao.getMoneyListCount(money);
	}

	@Override
	public List<Channel2> getChannel2List(Channel2 channel) {
		// TODO Auto-generated method stub
		return operateDao.getChannel2List(channel);
	}

	@Override
	public long getChannel2ListCount(Channel2 channel) {
		// TODO Auto-generated method stub
		return operateDao.getChannel2ListCount(channel);
	}

	@Override
	public List<ChannelType> getChannelTypeList(ChannelType channelType) {
		// TODO Auto-generated method stub
		return operateDao.getChannelTypeList(channelType);
	}

	@Override
	public List<Channel2> getChannel2NumGraph(Channel2 channel2) {
		// TODO Auto-generated method stub
		return operateDao.getChannel2NumGraph(channel2);
	}

	@Override
	public List<Channel2> getChannel2RateGraph(Channel2 channel2) {
		// TODO Auto-generated method stub
		return operateDao.getChannel2RateGraph(channel2);
	}

	@Override
	public List<Channel2> getChannel2MoneyGraph(Channel2 channel2) {
		// TODO Auto-generated method stub
		return operateDao.getChannel2MoneyGraph(channel2);
	}

	@Override
	public void statistic_day_inventory() {
		// TODO Auto-generated method stub
		operateDao.statistic_day_inventory();
	}

	@Override
	public List<Inventory> getInventoryList(Inventory inventory) {
		// TODO Auto-generated method stub
		return operateDao.getInventoryList(inventory);
	}

	@Override
	public long getInventoryListCount(Inventory inventory) {
		// TODO Auto-generated method stub
		return operateDao.getInventoryListCount(inventory);
	}

	@Override
	public List<Inventory> getInventoryDetailList(Inventory inventory) {
		// TODO Auto-generated method stub
		return operateDao.getInventoryDetailList(inventory);
	}

	@Override
	public long getInventoryDetailListCount(Inventory inventory) {
		// TODO Auto-generated method stub
		return operateDao.getInventoryDetailListCount(inventory);
	}

	@Override
	public List<Inventory> exportInventoryList(Inventory inventory) {
		// TODO Auto-generated method stub
		return operateDao.ExportInventoryList(inventory);
	}

	@Override
	public List<Settle> getSettleList(Settle settle) {
		// TODO Auto-generated method stub
		return operateDao.getSettleList(settle);
	}

	@Override
	public long getSettleListCount(Settle settle) {
		// TODO Auto-generated method stub
		return operateDao.getSettleListCount(settle);
	}

	@Override
	public List<Settle> getAcessFeeList(Settle settle) {
		// TODO Auto-generated method stub
		return operateDao.getAcessFeeList(settle);
	}

	@Override
	public float getSettleRate() {
		// TODO Auto-generated method stub
		return operateDao.getSettleRate();
	}

	@Override
	public List<Channel2> getChannel2List2(Channel2 channel) {
		// TODO Auto-generated method stub
		return operateDao.getChannel2List2(channel);
	}

 

	 
}
