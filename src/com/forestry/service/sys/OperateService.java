package com.forestry.service.sys;

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

import core.service.Service;

/**
 * 
 *  Class Name: OperateService.java
 *  Function:运营service
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月12日 下午2:20:45    
 *  @version 1.0
 */
public interface OperateService extends Service<Operate> {

	List<Operate> getOperateList(Operate operate);
	long getOperateListCount(Operate operate);
  
	List<Fail> getFailList(Fail fail);
	long getFailListCount(Fail fail);
	
	List<Channel> getChannelList(Channel channel);
	long getChannelListCount(Channel channel);
	
	List<Channel2> getChannel2List(Channel2 channel);
	List<Channel2> getChannel2List2(Channel2 channel);
	long getChannel2ListCount(Channel2 channel);
	List<ChannelType> getChannelTypeList(ChannelType channelType);
	List<Channel2> getChannel2NumGraph(Channel2 channel2);
	List<Channel2> getChannel2RateGraph(Channel2 channel2);
	List<Channel2> getChannel2MoneyGraph(Channel2 channel2);
	
	List<TradeUser> getTradeUserlList(TradeUser tradeUser);
	long getTradeUserListCount(TradeUser tradeUser);
	
	List<NewOld> getNewOldlList(NewOld newOld);
	long getNewOldListCount(NewOld newOld);
	
	List<Money> getMoneylList(Money money);
	long getMoneyListCount(Money money);
	
	
	List<Inventory> getInventoryList(Inventory inventory);
	long getInventoryListCount(Inventory inventory);
	List<Inventory> exportInventoryList(Inventory inventory);
	
	List<Inventory> getInventoryDetailList(Inventory inventory);
	long getInventoryDetailListCount(Inventory inventory);
	
	
	void statistic_day_invest();
	void statistic_day_fail();
	void statistic_day_inventory();
	
	List<Settle> getSettleList(Settle settle);
	long getSettleListCount(Settle settle);
	 
	List<Settle> getAcessFeeList(Settle settle);
	float getSettleRate();

}
