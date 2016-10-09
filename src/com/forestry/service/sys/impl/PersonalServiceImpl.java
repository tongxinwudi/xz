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
import com.forestry.dao.sys.MemberDao;
import com.forestry.dao.sys.OperateDao;
import com.forestry.dao.sys.PersonalDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.Attachment;
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
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.MemberService;
import com.forestry.service.sys.OperateService;
import com.forestry.service.sys.PersonalService;
import com.forestry.service.sys.ProfitService;

import core.service.BaseService;

/**
 * 
 *  Class Name: MemberServiceImpl.java
 *  Function:会员service实现
 *  Modifications:   
 *  @author TongXin  DateTime 2015年9月11日 下午2:20:58    
 *  @version 1.0
 */
@Service
public class PersonalServiceImpl extends BaseService<PersonalInfo> implements PersonalService {

	@Resource
	private PersonalDao personalDao;

	@Override
	public List<PersonalInfo> getPersonalList(PersonalInfo personalinfo) {
		// TODO Auto-generated method stub
		return personalDao.getPersonalList(personalinfo);
	}

	@Override
	public long getPersonalListCount(PersonalInfo personalinfo) {
		// TODO Auto-generated method stub
		return personalDao.getPersonalListCount(personalinfo);
	}

	@Override
	public void statistic_day_personal_invest() {
		// TODO Auto-generated method stub
		personalDao.statistic_day_personal_invest();
	}

	 
	 

 

	 
}
