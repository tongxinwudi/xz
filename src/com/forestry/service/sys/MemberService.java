package com.forestry.service.sys;

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

import core.service.Service;

/**
 * 
 * Class Name: MemberService.java Function:会员service Modifications:
 * 
 * @author TongXin DateTime 2015年9月11日 上午11:34:58
 * @version 1.0
 */
public interface MemberService extends Service<Member_week_ration> {

	void statistic_week_ration();
	
	void statistic_day_becomeVip();

	List<Member_week_ration> getMemberRationList(Member_week_ration member_week_ration);
	
	long getMemberRationListCount(Member_week_ration member_week_ration);
}
