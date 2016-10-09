package com.forestry.dao.sys.impl;

import java.math.BigInteger;
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
 * 查询错误率
 * @author 高辉
 *
 */

@Repository
public class FundOperatLogDaoImpl extends BaseDao<ErrorCommonInfo> implements ErrorDao {
	public FundOperatLogDaoImpl(){
		super(ErrorCommonInfo.class);
	}
	/**
	 * 获取交易基金类错误总数
	 * @return
	 */
	@Override
	public long getErrorCount(ErrorCommonInfo model){
		String hql="select count(*) from qj_fund_db.operat_log t left join qj_user_db.`user` u on(t.uid=u.uid) where t.state=4";
		if(!"".equals(StringUtil.trimNull(model.getName()))){
			hql+=" and u.name like '%"+model.getName()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getMobile()))){
			hql+=" and u.mobile like '%"+model.getMobile()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getStart()))){
			hql+=" and DATEDIFF(t.opDate,DATE('"+model.getStart()+"'))>0";
		}
		if(!"".equals(StringUtil.trimNull(model.getEnd()))){
			hql+=" and DATEDIFF(t.opDate,DATE('"+model.getEnd()+"'))<0";
		}else{
			hql+=" and DATEDIFF(t.opDate,NOW())<0";
		}
		//RoutingContextHolder.setContext("fundDataSource");
		
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
		String hql="select year(t.opDate) as opYear,month(t.opDate) as opMonth,count(*) from qj_fund_db.operat_log t left join qj_user_db.`user` u on(t.uid=u.uid) where t.state=4";
		if(!"".equals(StringUtil.trimNull(model.getName()))){
			hql+=" and u.name like '%"+model.getName()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getMobile()))){
			hql+=" and u.mobile like '%"+model.getMobile()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(model.getStart()))){
			hql+=" and DATEDIFF(t.opDate,DATE('"+model.getStart()+"'))>0";
		}
		if(!"".equals(StringUtil.trimNull(model.getEnd()))){
			hql+=" and DATEDIFF(t.opDate,DATE('"+model.getEnd()+"'))<0";
		}else{
			hql+=" and DATEDIFF(t.opDate,NOW())<0";
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
			error.setErrorType(1);
			error.setTitle(Constant.ERROR_FUND_NAME);
			error.setMonth(o[0]+"-"+o[1]);
			error.setCount(((BigInteger) o[2]).longValue());
			list.add(error);
		}
		return list;
		*/
	}
	
	/**
	 * 获取某个用户的交易基金类错误总数
	 * @return
	 */
	@Override
	public long getUserErrorCount(String uid){
		String hql="select count(*) from qj_fund_db.operat_log  t where t.state=4 and uid=?";
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
			hql+=" and DATEDIFF(t.opDate,DATE('"+model.getStart()+"'))>0";
		}
		if(!"".equals(StringUtil.trimNull(model.getEnd()))){
			hql+=" and DATEDIFF(t.opDate,DATE('"+model.getEnd()+"'))<0";
		}else{
			hql+=" and DATEDIFF(t.opDate,NOW())<0";
		}
		hql += " order by " +key + " "+ value +" limit " +model.getFirstResult() +","+model.getMaxResults();
		return hql;
	}
	
	/**
	 * 获取交易基金类错误列表
	 * @return
	 */
	@Override
	public List<ErrorCommonInfo> getAllErrorInfoList(ErrorCommonInfo model){
		String hql="select t.uid,t.reason,t.opDate,t.state,1 as errorType,u.name,u.mobile from qj_fund_db.operat_log t "
				+" left join qj_user_db.`user` u on(t.uid=u.uid) where t.state=4";
		hql+=createWhereHql(model);
		return convert(getSession().createSQLQuery(hql).list());
	}
	/**
	 * 获取某个用户的交易基金类错误列表
	 * @return
	 */
	@Override
	public List<ErrorCommonInfo> getUserErrorInfo(ErrorCommonInfo model){
		String hql="select uid,reason,opDate,state,1 as errorType from qj_fund_db.operat_log  where state=4 and uid=?";
		hql+=createWhereHql(model);
		Query query=getSession().createSQLQuery(hql);
		query.setString(0, model.getUid());
		return convert(query.list());
	}
	
	protected List<ErrorCommonInfo> convert(List l){
		List<ErrorCommonInfo> list=new ArrayList<ErrorCommonInfo>();
		for(long i=0;i<l.size();i++){
			ErrorCommonInfo error=new ErrorCommonInfo();
			Object[] o = (Object[]) l.get((int)i);
			error.setId(i+1);
			error.setUid((String)o[0]);
			error.setReason((String)o[1]);
			error.setOpDate((Date) o[2]);
			error.setState( ((Short) o[3]).intValue());
			error.setErrorType(((BigInteger) o[4]).intValue());
			error.setName((String)o[5]);
			error.setMobile((String)o[6]);
			list.add(error);
		}
		return list;
	}
	
	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金和金额
	 * @return
	 */
	public List getSaleAllOnceFund(){
		String hql="SELECT A.uid,A.fdcode,A.tshares FROM"
				+"(SELECT uid,fdcode,sum(shares) AS tshares	FROM qj_fund_db.operat_log"
				+"	WHERE operate = 2 AND state = 3 AND sid = 0"
				+"	GROUP BY uid,fdcode	HAVING count(*)=1) A"
				+"  LEFT OUTER JOIN"
				+"(SELECT uid,fdcode,sum(shares) AS tshares	FROM qj_fund_db.operat_log"
				+"	WHERE operate = 1 AND state = 3 AND sid = 0"
				+"	GROUP BY uid,fdcode) B"
				+"  ON A.uid = B.uid AND A.fdcode = B.fdcode"
				+"	WHERE A.tshares = B.tshares";
		Query query=getSession().createSQLQuery(hql);
		return query.list();
	}
	
	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金持有时间
	 * @return
	 */
	public List getSaleAllOnceFundHoldTime(){
		String hql="SELECT C.fdcode,sum(C.holdTime) AS holdTime FROM("
				+"SELECT A.uid,A.fdcode,DATEDIFF(A.opDate, B.opDate) AS holdTime FROM("
				+"	SELECT uid,fdcode,opDate,sum(shares) AS tshares FROM qj_fund_db.operat_log"
				+"	WHERE operate = 2 AND state = 3 AND sid = 0"
				+"	GROUP BY uid,fdcode	HAVING count(*) = 1"
				+") A"
				+"	LEFT OUTER JOIN ("
				+"SELECT uid,fdcode,min(opDate) AS opDate,sum(shares) AS tshares FROM qj_fund_db.operat_log"
				+"	WHERE operate = 1 AND state = 3 AND sid = 0"
				+"	GROUP BY uid,fdcode"
				+") B ON A.uid = B.uid AND A.fdcode = B.fdcode	WHERE A.tshares = B.tshares"
				+") AS C"
				+"	GROUP BY C.fdcode";
		Query query=getSession().createSQLQuery(hql);
		return query.list();
	}
	
	/**
	 * 单只基金，一次或多次申购，一次赎回部分基金
	 * @return
	 */
	public List getSalePartOnceFund(){
		String hql="SELECT A.uid,A.fdcode,A.tshares FROM"
				+"(SELECT uid,fdcode,sum(shares) AS tshares	FROM qj_fund_db.qj_fund_db.operat_log"
				+"	WHERE operate = 2 AND state = 3 AND sid = 0"
				+"	GROUP BY uid,fdcode	HAVING count(*)=1) A"
				+"  LEFT OUTER JOIN"
				+"(SELECT uid,fdcode,sum(shares) AS tshares	FROM qj_fund_db.qj_fund_db.operat_log"
				+"	WHERE operate = 1 AND state = 3 AND sid = 0"
				+"	GROUP BY uid,fdcode) B"
				+"  ON A.uid = B.uid AND A.fdcode = B.fdcode"
				+"	WHERE A.tshares < B.tshares";
		Query query=getSession().createSQLQuery(hql);
		return query.list();
	}
	
	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金持有时间
	 * @return
	 */
	public List getSalePartOnceFundHoldTime(){
		String hql="SELECT C.fdcode,sum(C.holdTime) AS holdTime FROM("
				+"SELECT A.uid,A.fdcode,DATEDIFF(NOW(), B.opDate) AS holdTime FROM("
				+"	SELECT uid,fdcode,opDate,sum(shares) AS tshares FROM qj_fund_db.operat_log"
				+"	WHERE operate = 2 AND state = 3 AND sid = 0"
				+"	GROUP BY uid,fdcode	HAVING count(*) = 1"
				+") A"
				+"	LEFT OUTER JOIN ("
				+"SELECT uid,fdcode,min(opDate) AS opDate,sum(shares) AS tshares FROM qj_fund_db.operat_log"
				+"	WHERE operate = 1 AND state = 3 AND sid = 0"
				+"	GROUP BY uid,fdcode"
				+") B ON A.uid = B.uid AND A.fdcode = B.fdcode	WHERE (B.tshares-A.tshares)<1"
				+") AS C"
				+"	GROUP BY C.fdcode";
		Query query=getSession().createSQLQuery(hql);
		return query.list();
	}
	
	/**
	 * 单只基金，一次或多次申购，未赎回的基金
	 * @return
	 */
	public List getNoSaleFund(){
		String hql="SELECT uid,fdcode FROM qj_fund_db.qj_fund_db.operat_log"
				+"	WHERE operate = 1 AND state = 3 AND sid = 0"
				+"	AND (uid,fdcode) not in(" 
				+"	SELECT uid,fdcode FROM qj_fund_db.qj_fund_db.operat_log WHERE operate = 2 AND state = 3 AND sid = 0) " 
				+"	GROUP BY uid,fdcode";
		Query query=getSession().createSQLQuery(hql);
		return query.list();
	}
	
	/**
	 * 单只基金，一次或多次申购，未赎回基金的持有时间
	 * @return
	 */
	public List getNoSaleFundHoldTime(){
		String hql="SELECT C.fdcode,sum(holdTime) as holdTime FROM"
				+"	(SELECT uid,fdcode,DATEDIFF(NOW(),min(opDate)) as holdTime FROM qj_fund_db.qj_fund_db.operat_log"
				+"	WHERE operate = 1 AND state = 3 AND sid = 0"
				+"	AND (uid,fdcode) not in(" 
				+"	SELECT uid,fdcode FROM qj_fund_db.qj_fund_db.operat_log WHERE operate = 2 AND state = 3 AND sid = 0) " 
				+"	GROUP BY uid,fdcode) as C"
				+"	GROUP BY C.fdcode";
		Query query=getSession().createSQLQuery(hql);
		return query.list();
	}
	
	/**
	 * 单只基金，一次或多次申购，多次赎回
	 * @return
	 */
	public List getSaleMoreThanOnceFund(){
		String hql="SELECT uid,fdcode,opDate FROM qj_fund_db.operat_log"
				+"	where operate = 2 AND state = 3 AND sid=0"
				+"	GROUP BY uid,fdcode"
				+"	HAVING count(*)>1";
		Query query=getSession().createSQLQuery(hql);
		return query.list();
	}
	
	/**
	 * 根据基金号和用户编号返回对应类型的基金操作记录
	 * @param fdcode，基金号
	 * @param uid，用户编码
	 * @param operate，操作类型
	 * @return
	 */
	public List getFundByCodeAndUid(String fdcode,String uid,int operate){
		String hql="SELECT * FROM qj_fund_db.operat_log WHERE operate = ? AND state = 3 AND sid = 0 AND fdcode=? AND uid=?";
		Query query =getSession().createQuery(hql);
		query.setInteger(0, operate);
		query.setString(1, fdcode);
		query.setString(2, uid);
		return query.list();
	}
}
