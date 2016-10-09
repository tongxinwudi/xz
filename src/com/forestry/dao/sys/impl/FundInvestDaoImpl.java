package com.forestry.dao.sys.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.forestry.dao.sys.FundInvestDao;
import com.forestry.model.sys.OperatLog;
import core.dao.BaseDao;
import core.util.StringUtil;

@Repository
public class FundInvestDaoImpl extends BaseDao<OperatLog> implements
		FundInvestDao {

	public FundInvestDaoImpl() {
		super(OperatLog.class);
	}

	@Override
	public long fundInvestSumCount(OperatLog op) {
		String hql="select fdcode,abbrev,partner,sum(sum) as sum from qj_fund_db.operat_log";
		hql+=createSumWhereHql(op,false);
		return getSession().createSQLQuery(hql).list().size();
	}
	
	

	/**
	 * 各基金申购的总额统计
	 * @param op
	 * @return
	 */
	@Override
	public List<OperatLog> fundInvestSum(OperatLog op,boolean isPage) {
		String hql="select fdcode,abbrev,partner,sum(sum) as sum,DATE_FORMAT(opDate,'%Y-%m-%d') as opDate from qj_fund_db.operat_log";
		hql+=createSumWhereHql(op,isPage);
		return convert(getSession().createSQLQuery(hql).list());
	}
	
	public long fundInvestEverydayCount(OperatLog op){
		String hql="select fdcode,DATE_FORMAT(opDate,'%Y-%m-%d') as opDate,sum(sum) as sum from qj_fund_db.operat_log";
		hql+=createFundWhereHql(op,false);
		return getSession().createSQLQuery(hql).list().size();
	}
	
	/**
	 * 单个基金每天的申购额
	 * @param op
	 * @return
	 */
	public List<OperatLog> fundInvestEveryday(OperatLog op,boolean isPage){
		String hql="select fdcode,abbrev,partner,sum(sum) as sum,DATE_FORMAT(opDate,'%Y-%m-%d') as opDate from qj_fund_db.operat_log";
		hql+=createFundWhereHql(op,isPage);
		return convert(getSession().createSQLQuery(hql).list());
	}

	/**
	 * 
	 * @param op
	 * @return
	 */
	private String createSumWhereHql(OperatLog op,boolean isPage){
		String hql=" where (operate=1 or operate=4) and (state=1 or state=3) ";
		if(!"".equals(StringUtil.trimNull(op.getFdcode()))){
			hql+=" and fdcode like '%"+op.getFdcode()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(op.getAbbrev()))){
			hql+=" and abbrev like '%"+op.getAbbrev()+"%'";
		}
		if(!"".equals(StringUtil.trimNull(op.getOpStart()))){
			hql+=" and DATEDIFF(opDate,DATE('"+op.getOpStart()+"'))>0";
		}
		if(!"".equals(StringUtil.trimNull(op.getOpEnd()))){
			hql+=" and DATEDIFF(opDate,DATE('"+op.getOpEnd()+"'))<0";
		}else{
			hql+=" and DATEDIFF(opDate,NOW())<0";
		}
		if(!"".equals(StringUtil.trimNull(op.getPartner()))){
			hql+=" and partner="+op.getPartner();
		}
		hql +=" GROUP BY fdcode,partner";
		if(isPage){
			String key = op.getSortedConditions().keySet().iterator().next();
			String value = op.getSortedConditions().values().iterator().next();
			hql += " order by " +key + " "+ value +" limit " +op.getFirstResult() +","+op.getMaxResults();
		}
		return hql;
	}
	
	private String createFundWhereHql(OperatLog op,boolean isPage){
		String hql=" where (operate=1 or operate=4) and (state=1 or state=3) ";
		if(!"".equals(StringUtil.trimNull(op.getFdcode()))){
			hql+=" and fdcode='"+op.getFdcode()+"'";
		}
		if(!"".equals(StringUtil.trimNull(op.getOpStart()))){
			hql+=" and DATEDIFF(opDate,DATE('"+op.getOpStart()+"'))>0";
		}
		if(!"".equals(StringUtil.trimNull(op.getOpEnd()))){
			hql+=" and DATEDIFF(opDate,DATE('"+op.getOpEnd()+"'))<0";
		}else{
			hql+=" and DATEDIFF(opDate,NOW())<0";
		}
		if(!"".equals(StringUtil.trimNull(op.getPartner()))){
			hql+=" and partner="+op.getPartner();
		}
		hql +=" GROUP BY YEAR(opDate),MONTH(opDate),DAY(opDate)";
		if(isPage){
			String key = op.getSortedConditions().keySet().iterator().next();
			String value = op.getSortedConditions().values().iterator().next();
			hql += " order by " +key + " "+ value +" limit " +op.getFirstResult() +","+op.getMaxResults();
		}
		return hql;
	}
	
	private List<OperatLog> convert(List list){
		List<OperatLog> retList=new ArrayList<OperatLog>();
		for(int i=0;i<list.size();i++){
			Object[] o=(Object[])list.get(i); 
			OperatLog op=new OperatLog();
			op.setFdcode(o[0].toString());
			op.setAbbrev(o[1].toString());
			if(!"".equals(o[2]))
				op.setPartner(Integer.parseInt(o[2].toString()));
			else
				op.setPartner(0);
			if(!"".equals(o[3]))
				op.setSum(Double.parseDouble(o[3].toString()));
			else
				op.setSum(0D);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				if(!"".equals(o[4]))
				op.setOpDate(sdf.parse(o[4].toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			retList.add(op);
		}
		return retList;
	}
}
