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
import com.forestry.model.sys.PersonalInfo;
import com.forestry.model.sys.Profit;
import com.forestry.model.sys.TradeUser;

import core.service.Service;

/**
 * 
 *  Class Name: PersonalService.java
 *  Function:个人信息
 *  Modifications:   
 *  @author TongXin  DateTime 2015年9月18日 下午3:20:44    
 *  @version 1.0
 */
public interface PersonalService extends Service<PersonalInfo> {


	List<PersonalInfo> getPersonalList(PersonalInfo personalinfo);

	long getPersonalListCount(PersonalInfo personalinfo);
	
	void statistic_day_personal_invest();
}
