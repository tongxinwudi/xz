package com.forestry.dao.sys;

import java.io.IOException;
import java.util.List;

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

import core.dao.Dao;

/**
 * 
 *  Class Name: OperateDao.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月12日 下午2:30:42    
 *  @version 1.0
 */
public interface OperateDao extends Dao<Operate> {
	
	List<Operate> getOperateList(Operate operate);
	long getOperateListCount(Operate operate);
	
	List<Fail> getFailList(Fail fail);
	long getFailListCount(Fail fail);
	
	List<Channel> getChannelList(Channel channel);
	long getChannelListCount(Channel channel);
	
    void statistic_day_invest();
    void statistic_day_fail();
    
	long getTradeUserListCount(TradeUser tradeUser);
	List<TradeUser> getTradeUserList(TradeUser tradeUser);
 
	long getNewOldListCount(NewOld newOld);
	List<NewOld> getNewOldList(NewOld newOld);
	long getMoneyListCount(Money money);
	List<Money> getMoneyList(Money money);
	List<Channel2> getChannel2List(Channel2 channel);
	long getChannel2ListCount(Channel2 channel);
	List<ChannelType> getChannelTypeList(ChannelType channelType);
	List<Channel2> getChannel2NumGraph(Channel2 channel);
	List<Channel2> getChannel2RateGraph(Channel2 channel);
	List<Channel2> getChannel2MoneyGraph(Channel2 channel);
   
	public void statistic_day_inventory() ;
	long getInventoryListCount(Inventory inventory);
	List<Inventory> getInventoryList(Inventory inventory);
	List<Inventory> getInventoryDetailList(Inventory inventory);
	long getInventoryDetailListCount(Inventory inventory);
	List<Inventory> ExportInventoryList(Inventory inventory);
	List<Settle> getSettleList(Settle settle);
	long getSettleListCount(Settle settle);
	List<Settle> getAcessFeeList(Settle settle);
	float getSettleRate();
	List<Channel2> getChannel2List2(Channel2 channel);
}
