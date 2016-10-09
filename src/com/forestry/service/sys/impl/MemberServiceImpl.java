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
import com.forestry.model.sys.Profit;
import com.forestry.model.sys.TradeUser;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.MemberService;
import com.forestry.service.sys.OperateService;
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
public class MemberServiceImpl extends BaseService<Member_week_ration> implements MemberService {

	@Resource
	private MemberDao memberDao;

	@Override
	public void statistic_week_ration() {
		// TODO Auto-generated method stub
		memberDao.statistic_week_ration();
	}

	@Override
	public List<Member_week_ration> getMemberRationList(Member_week_ration member_week_ration) {
		// TODO Auto-generated method stub
		return memberDao.getMemberRationList(member_week_ration);
	}

	@Override
	public long getMemberRationListCount(Member_week_ration member_week_ration) {
		// TODO Auto-generated method stub
		return memberDao.getMemberRationListCount(member_week_ration);
	}

	@Override
	public void statistic_day_becomeVip() {
		// TODO Auto-generated method stub
		memberDao.statistic_day_becomeVip();
	}

	 

 

	 
}
