package com.forestry.dao.sys.impl;

import java.applet.Applet;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.forestry.dao.sys.ErrorLogDao;
import com.forestry.model.sys.AppLog;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.ErrorLog;
import com.forestry.model.sys.Mail;
import com.forestry.model.sys.MailContent;
import com.forestry.model.sys.Member_week_ration;

 
import core.dao.BaseDao;
import core.util.DateUtil;
import core.util.MailUtil;
import core.util.StringUtil;

@Repository
public class ErrorLogDaoImpl extends BaseDao<ErrorLog> implements ErrorLogDao {

	public ErrorLogDaoImpl() {
		super(ErrorLog.class);
	}

	/**
	 * 返回日志数量
	 * 
	 * @param log
	 * @return
	 */
	@Override
	public long getLogsCount(ErrorLog log) {
		String hql = " from ErrorLog " + createWhereHql(log);
		Query query = getSession().createQuery(hql);
		List list = query.list();
		return Long.parseLong(StringUtil.trimNull(list.size()));
	}

	/**
	 * 返回日志列表
	 * 
	 * @param log
	 * @return
	 */
	@Override
	public List<ErrorLog> getLogs(ErrorLog log) {
		String hql = " from ErrorLog " + createWhereHql(log);
		List l = getSession().createQuery(hql).setFirstResult(log.getFirstResult()).setMaxResults(log.getMaxResults()).list();
		return l;
	}
	
	/**
	 * 获取要发送邮件的日志
	 * @param lastId，上次发送邮件的最后日志ID
	 * @return
	 */
	@Override
	public List<ErrorLog> getMailLogs(){
		String hql=" from ErrorLog where issend = 0 order by error_time desc";
		return getSession().createQuery(hql).list();
	}
	
	/**
	 * 设置邮件已发送
	 * @param ids
	 */
	@Override
	public void updateIsSend(String ids){
		String hql="update ErrorLog e set e.issend=1 where id in ("+ids+")";
		Query query=getSession().createQuery(hql);
		query.executeUpdate();
	}

	/**
	 * 生成sql语句的where部分
	 * 
	 * @param model
	 * @return
	 */
	public String createWhereHql(ErrorLog log) {
		String hql = " where 1=1 ";
		String key = log.getSortedConditions().keySet().iterator().next();
		String value = log.getSortedConditions().values().iterator().next();

		if (!"".equals(StringUtil.trimNull(log.getUid()))) {
			hql += " and uid like '%" + log.getUid() + "%'";
		}
		if (!"".equals(StringUtil.trimNull(log.getMobile()))) {
			hql += " and mobile like '%" + log.getMobile() + "%'";
		}
		if (!"".equals(StringUtil.trimNull(log.getEcode()))) {
			hql += " and ecode like '%" + log.getEcode() + "%'";
		}
		if (!"".equals(StringUtil.trimNull(log.getCard()))) {
			hql += " and card like '%" + log.getCard() + "%'";
		}
		if (!"".equals(StringUtil.trimNull(log.getType()))
				&& log.getType().equals("db")) {
			hql += " and type='db'";
		} else {
			hql += " and (type!='db' or type is NULL)";
		}
		/*
		 * if(!"".equals(StringUtil.trimNull(model.getStart()))){
		 * hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),DATE('"
		 * +model.getStart()+"'))>0"; }
		 * if(!"".equals(StringUtil.trimNull(model.getEnd()))){
		 * hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),DATE('"
		 * +model.getEnd()+"'))<0"; }else{
		 * hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),NOW())<0";
		 * }
		 */
		hql += " order by " + key + " " + value;
		return hql;
	}
	
	
	
	@Override
	public List<AppLog> getAppLogList(AppLog appLog) {
		// TODO Auto-generated method stub

		String sql = "select  user_id,mobile,version,system_level,system_version,model,channel,network_type,FROM_UNIXTIME(time) as time,error_url,error_url_params,error_json,error_detail,sys_time From qj_qjs_db.appLog  where 1=1 ";

		if (!"".equals(StringUtil.trimNull(appLog.getSdate()))) {
			sql += " and sys_time >='" + appLog.getSdate() + " 00:00:00'";
		}

		if (!"".equals(StringUtil.trimNull(appLog.getEdate()))) {
			sql += " and sys_time<='" + appLog.getEdate() + " 23:59:59'";
		}
		
		if (!"".equals(StringUtil.trimNull(appLog.get$like_mobile()))) {
			sql += " and mobile like '%" + appLog.get$like_mobile() + "%'";
		}
		
	 	if (!"".equals(StringUtil.trimNull(appLog.get$like_model()))) {
			sql += " and model = '" + appLog.get$like_model() + "'";
		}
		 
		if (!"".equals(StringUtil.trimNull(appLog.get$like_version()))) {
			sql += " and version = '" + appLog.get$like_version() + "'";
		}

		String key = appLog.getSortedConditions().keySet().iterator().next();
		String value = appLog.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + appLog.getFirstResult() + "," + appLog.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<AppLog> listOperates = new ArrayList<AppLog>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			AppLog op = new AppLog();
			Object[] o = (Object[]) list.get(i);
			op.setUser_id(StringUtil.trimNull(o[0]));
			op.setMobile(StringUtil.trimNull(o[1]));
			op.setVersion(StringUtil.trimNull(o[2]));
			op.setSystem_level(StringUtil.trimNull(o[3]));
			op.setSystem_version(StringUtil.trimNull(o[4]));
			op.setModel(StringUtil.trimNull(o[5]));
			op.setChannel(StringUtil.trimNull(o[6]));
			op.setNetwork_type(StringUtil.trimNull(o[7]));
			op.setTime(StringUtil.trimNull(o[8]));
			op.setError_url(StringUtil.trimNull(o[9]));
			op.setError_url_params(StringUtil.trimNull(o[10]));
			op.setError_json( new String( StringUtil.trimNull(o[11])) );
			op.setError_detail(StringUtil.trimNull(o[12]));
			op.setSys_time(StringUtil.trimNull(o[13]));
			listOperates.add(op);
		}
		return listOperates;
	} 
	
	@Override
	public long getAppLogListCount(AppLog appLog) {
		// TODO Auto-generated method stub
	 
		String sql = "select count(1) from (select  user_id,mobile,version,system_level,system_version,model,channel,network_type,time,error_url,error_url_params,error_json,error_detail,sys_time From qj_qjs_db.appLog  where 1=1 ";

		if (!"".equals(StringUtil.trimNull(appLog.getSdate()))) {
			sql += " and sys_time >='" + appLog.getSdate() + " 00:00:00'";
		}

		if (!"".equals(StringUtil.trimNull(appLog.getEdate()))) {
			sql += " and sys_time<='" + appLog.getEdate() + " 23:59:59'";
		}
		
		if (!"".equals(StringUtil.trimNull(appLog.get$like_mobile()))) {
			sql += " and mobile like '%" + appLog.get$like_mobile() + "%'";
		}
		
		if (!"".equals(StringUtil.trimNull(appLog.get$like_model()))) {
			sql += " and model = '" + appLog.get$like_model() + "'";
		}
		 
		
		if (!"".equals(StringUtil.trimNull(appLog.get$like_version()))) {
			sql += " and version = '" + appLog.get$like_version() + "'";
		}
		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	@Override
	public List<ChannelType> getOsTypeList(ChannelType channelType) {
		// TODO Auto-generated method stub
		String sql = "select distinct version as type from qj_qjs_db.appLog ";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<ChannelType> listOperates = new ArrayList<ChannelType>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			ChannelType op = new ChannelType();
			String o = (String) list.get(i);
			op.setKey(StringUtil.trimNull(o));
			op.setValue(StringUtil.trimNull(o));
			listOperates.add(op);
		}
	 
		
	 
		
		ChannelType op = new ChannelType();
		op.setKey("");
		op.setValue("全部");
		listOperates.add(op);
		 
		return listOperates;
	}
	
	
	
	
	
	
}
