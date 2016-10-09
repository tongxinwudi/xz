/**
 * 
 */
package com.forestry.dao.sys.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.forestry.core.Constant;
import com.forestry.dao.sys.ErrorDao;
import com.forestry.model.sys.ErrorCommonInfo;
import com.forestry.model.sys.ErrorCountView;

import core.dao.BaseDao;
import core.util.StringUtil;

/**
 * @author Steve
 *
 */
@Repository
public class CashbaoTradeLogDaoImpl extends BaseDao<ErrorCommonInfo> implements ErrorDao {

	/**
	 * @param entityClass
	 */
	public CashbaoTradeLogDaoImpl() {
		super(ErrorCommonInfo.class);
	}

	/**
	 * 获取活期宝错误总数
	 * @return
	 */
	@Override
	public long getErrorCount(ErrorCommonInfo model) {
		String hql="select count(*) as total from qj_cashbao_db.trade_log t left join qj_user_db.`user` u on(t.uid=u.uid) where t.state=4";
		if(!"".equals(StringUtil.trimNull(model.getName()))){
			hql+=" and u.name like '%"+model.getName()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getMobile()))){
			hql+=" and u.mobile like '%"+model.getMobile()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getStart()))){
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),DATE('"+model.getStart()+"'))>0";
		}
		if(!"".equals(StringUtil.trimNull(model.getEnd()))){
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),DATE('"+model.getEnd()+"'))<0";
		}else{
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),NOW())<0";
		}
		Query query=getSession().createSQLQuery(hql);
		List list=query.list();
		String r=StringUtil.trimNull(list.get(0));
		return Long.parseLong(r);
	}
	
	/**
	 * 根据年、月获取交易基金类错误总数
	 * @return
	 */
	@Override
	public List getErrorCountByMonth(ErrorCommonInfo model){
		String hql="select FROM_UNIXTIME( t.opDate, '%Y' )  as opYear,FROM_UNIXTIME( t.opDate, '%m' ) as opMonth,count(*) as total from qj_cashbao_db.trade_log t left join qj_user_db.`user` u on(t.uid=u.uid) where t.state=4";
		if(!"".equals(StringUtil.trimNull(model.getName()))){
			hql+=" and u.name like '%"+model.getName()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getMobile()))){
			hql+=" and u.mobile like '%"+model.getMobile()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getStart()))){
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),DATE('"+model.getStart()+"'))>0";
		}
		if(!"".equals(StringUtil.trimNull(model.getEnd()))){
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),DATE('"+model.getEnd()+"'))<0";
		}else{
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),NOW())<0";
		}
		//RoutingContextHolder.setContext("fundDataSource");
		hql+=" GROUP BY opYear,opMonth ORDER BY opYear,opMonth";
		Query query=getSession().createSQLQuery(hql);
		return query.list();
		/*
		List l=query.list();
		List<ErrorCountView> list=new ArrayList<ErrorCountView>();
		for(long i=0;i<l.size();i++){
			ErrorCountView error=new ErrorCountView();
			Object[] o = (Object[]) l.get((int)i);
			error.setErrorType(2);
			error.setTitle(Constant.ERROR_CASHBAO_NAME);
			error.setMonth((String)o[0]+"-"+(String)o[1]);
			error.setCount(((BigInteger) o[2]).longValue());
			list.add(error);
		}
		return list;
		*/
	}

	/**
	 * 获取某个用户的活期宝错误总数
	 * @return
	 */
	@Override
	public long getUserErrorCount(String uid){
		String hql="select count(*) as total from qj_cashbao_db.trade_log  t left join qj_user_db.`user` u on(t.uid=u.uid) where t.state=4 and uid=?";
		Query query=getSession().createSQLQuery(hql);
		query.setParameter(0, uid);
		List list=query.list();
		return Long.parseLong(StringUtil.trimNull(list.get(0)));
	}
	
	/**
	 * 生成sql语句的where部分
	 * @param model
	 * @return
	 */
	public String createWhereHql(ErrorCommonInfo model){
		String hql="";
		String key = model.getSortedConditions().keySet().iterator().next();
		String value = model.getSortedConditions().values().iterator().next();
        
		if(!"".equals(StringUtil.trimNull(model.getName()))){
			hql+=" and u.name like '%"+model.getName()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getMobile()))){
			hql+=" and u.mobile like '%"+model.getMobile()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getStart()))){
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),DATE('"+model.getStart()+"'))>0";
		}
		if(!"".equals(StringUtil.trimNull(model.getEnd()))){
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),DATE('"+model.getEnd()+"'))<0";
		}else{
			hql+=" and DATEDIFF(FROM_UNIXTIME( t.opDate, '%Y-%m-%d' ),NOW())<0";
		}
		hql += " order by " +key + " "+ value +" limit " +model.getFirstResult() +","+model.getMaxResults();
		return hql;
	}
	
	/**
	 * 获取活期宝错误列表
	 * @return
	 */
	@Override
	public List<ErrorCommonInfo> getAllErrorInfoList(ErrorCommonInfo model){
		String hql="select t.uid,t.remark as reason,t.opDate,t.state,2 as errorType,u.name,u.mobile from qj_cashbao_db.trade_log t "
				+" left join qj_user_db.`user` u on(t.uid=u.uid)  where t.state=4 ";
		hql+=createWhereHql(model);
		return convert(getSession().createSQLQuery(hql).list());
	}
	/**
	 * 获取某个用户的活期宝错误列表
	 * @return
	 */
	@Override
	public List<ErrorCommonInfo> getUserErrorInfo(ErrorCommonInfo model){
		String hql="select uid,remark as reason,opDate,state,2 as errorType from qj_cashbao_db.trade_log where state=4 and uid=? ";
		hql+=createWhereHql(model);
		Query query=getSession().createSQLQuery(hql);
		query.setString(0, model.getUid());
		return convert(query.list());
	}

	protected List<ErrorCommonInfo> convert(List l){
		
		List<ErrorCommonInfo> list=new ArrayList<ErrorCommonInfo>();
		try {
		for(long i=0;i<l.size();i++){
			ErrorCommonInfo error=new ErrorCommonInfo();
			Object[] o = (Object[]) l.get((int)i);
			error.setId(i+1);
			error.setUid((String)o[0]);
			error.setReason((String)o[1]);
			
			SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
			Long time=new Long(o[2].toString());
			String d = format.format(time);
			Date date=format.parse(d);
			error.setOpDate(date);
			
			error.setState(((Short)o[3]).intValue() );
			error.setErrorType(((BigInteger) o[4]).intValue());
			error.setName((String)o[5]);
			error.setMobile((String)o[6]);
			list.add(error);
		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
