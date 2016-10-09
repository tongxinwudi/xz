package com.forestry.dao.sys;

import java.io.IOException;
import java.util.List;

import com.forestry.model.sys.Channel;
import com.forestry.model.sys.Channel2;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.Fail;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Member_week_ration;
import com.forestry.model.sys.Money;
import com.forestry.model.sys.NewOld;
import com.forestry.model.sys.Operate;
import com.forestry.model.sys.Profit;
import com.forestry.model.sys.TradeUser;

import core.dao.Dao;

/**
 * 
 * Class Name: OperateDao.java Function: Modifications:
 * 
 * @author TongXin DateTime 2015年8月12日 下午2:30:42
 * @version 1.0
 */
public interface MemberDao extends Dao<Member_week_ration> {

	void statistic_week_ration();

	List<Member_week_ration> getMemberRationList(Member_week_ration member_week_ration);

	long getMemberRationListCount(Member_week_ration member_week_ration);

	void statistic_day_becomeVip();

}
