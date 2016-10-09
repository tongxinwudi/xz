package com.forestry.dao.sys.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.forestry.dao.sys.ForestryDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.MonthInvestGraph;
import com.forestry.model.sys.Profit;

import core.dao.BaseDao;
import core.util.DateUtil;
import core.util.StringUtil;

/**
 * 
 *  Class Name: ProfitDaoImpl.java
 *  Function:投资额统计
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月17日 下午3:59:07    
 *  @version 1.0
 */
@Repository
public class ProfitDaoImpl extends BaseDao<Profit> implements ProfitDao {

	public ProfitDaoImpl() {
		super(Profit.class);
	}

	@Override
	public List<Profit> getProfitList(Profit profit) {
		// TODO Auto-generated method stub
		 
		String sql ="";
		
		if("true".equals(StringUtil.trimNull(profit.getCashbao()))){
			sql=" select u.uid as id ,u.name,u.mobile,   ifnull(sum(a.shares*a.nav+a.unpaid+a.income-a.invest),0) +ifnull(sum(cb.shares*1+cb.unpaid+cb.income-cb.invest ),0) as profit,ifnull(sum(a.invest),0)+ifnull(sum(cb.invest),0) as invest,ifnull(re.redemping,0)+ifnull(re1.redemping,0) redemping from qj_fund_db.assets a "
	                   + " left join qj_cashbao_db.assets cb on(cb.uid=a.uid)"  
					   + " left join qj_user_db.`user` u on(a.uid=u.uid) "
	                   + " left join (select uid ,sum(sum) redemping from qj_fund_db.operat_log where operate=2 and state=3 group by uid) re on(re.uid = a.uid)"
	                   + " left join (select uid ,sum(sum) redemping from qj_cashbao_db.trade_log where operate=2 and state=3 group by uid) re1 on(re1.uid = a.uid)"
	                   + " where 1=1 ";
		}else{
		   sql=" select u.uid as id ,u.name,u.mobile,  sum(shares*nav+unpaid+income-invest) as profit,sum(invest) as invest,ifnull(re.redemping,0)+ifnull(re1.redemping,0) redemping from qj_fund_db.assets a "
	                   + " left join qj_user_db.`user` u on(a.uid=u.uid) "
	                   + " left join (select uid ,sum(sum) redemping from qj_fund_db.operat_log where operate=2 and state=3 group by uid) re on(re.uid = a.uid)"
	                   + " left join (select uid ,sum(sum) redemping from qj_cashbao_db.trade_log where operate=2 and state=3 group by uid) re1 on(re1.uid = a.uid)"
	                   + " where 1=1 ";
		}
		
		
		if(!"".equals(StringUtil.trimNull(profit.get$like_mobile()))){
			sql +=" and u.mobile like '%" + profit.get$like_mobile() +"%'";
		}
		
		String key = profit.getSortedConditions().keySet().iterator().next();
		String value = profit.getSortedConditions().values().iterator().next();
		if(!"".equals(StringUtil.trimNull(profit.get$like_name()))){
			sql +=" and u.name like '%" + profit.get$like_name() +"%'";
		}
		
           sql += " group by u.mobile    order by " +key + " "+ value +" limit " +profit.getFirstResult() +","+profit.getMaxResults();
		
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Profit> listProfit = new ArrayList<Profit>();
		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);
		for(int i=0;i<list.size();i++){
			Profit p = new Profit();
			Object[] o = (Object[]) list.get(i);
			p.setId( (String) o[0]);
			p.setName( (String) o[1]);
			p.setMobile( (String) o[2]);
			p.setProfit( StringUtil.trimNull(o[3])  ) ;
			p.setInvest( StringUtil.trimNull(o[4]) ) ;
			p.setRedemping( StringUtil.trimNull(o[5]) );
			if(Double.parseDouble(StringUtil.trimNull(o[4]))!=0){
				p.setRates(nt.format( Double.parseDouble(String.valueOf( o[3]))/Double.parseDouble(StringUtil.trimNull(o[4]))));
			}else{
				p.setRates("0.00%");
			}
			listProfit.add(p);
		}
	 return listProfit;
	}

	@Override
	public long getProfitListCount(Profit profit) {
		// TODO Auto-generated method stub
		
String sql ="";
		
		if("true".equals(StringUtil.trimNull(profit.getCashbao()))){
			sql=" select count(1) from (select u.uid as id ,u.name,u.mobile,   ifnull(sum(a.shares*a.nav+a.unpaid+a.income-a.invest),0) +ifnull(sum(cb.shares*1+cb.unpaid+cb.income-cb.invest ),0) as profit,ifnull(sum(a.invest),0)+ifnull(sum(cb.invest),0) as invest,ifnull(re.redemping,0)+ifnull(re1.redemping,0) redemping from qj_fund_db.assets a "
	                   + " left join qj_cashbao_db.assets cb on(cb.uid=a.uid)"  
					   + " left join qj_user_db.`user` u on(a.uid=u.uid) "
	                   + " left join (select uid ,sum(sum) redemping from qj_fund_db.operat_log where operate=2 and state=3 group by uid) re on(re.uid = a.uid)"
	                   + " left join (select uid ,sum(sum) redemping from qj_cashbao_db.trade_log where operate=2 and state=3 group by uid) re1 on(re1.uid = a.uid)"
	                   + " where 1=1 ";
		}else{
		   sql=" select count(1) from (select  u.uid as id ,u.name,u.mobile,  sum(shares*nav+unpaid+income-invest) as profit,sum(invest) as invest,ifnull(re.redemping,0)+ifnull(re1.redemping,0) redemping from qj_fund_db.assets a "
	                   + " left join qj_user_db.`user` u on(a.uid=u.uid) "
	                   + " left join (select uid ,sum(sum) redemping from qj_fund_db.operat_log where operate=2 and state=3 group by uid) re on(re.uid = a.uid)"
	                   + " left join (select uid ,sum(sum) redemping from qj_cashbao_db.trade_log where operate=2 and state=3 group by uid) re1 on(re1.uid = a.uid)"
	                   + " where 1=1 ";
		}
		
		
		if(!"".equals(StringUtil.trimNull(profit.get$like_mobile()))){
			sql +=" and u.mobile like '%" + profit.get$like_mobile() +"%'";
		}
		
		if(!"".equals(StringUtil.trimNull(profit.get$like_name()))){
			sql +=" and u.name like '%" + profit.get$like_name() +"%'";
		}
		
        sql += " group by u.mobile  )t" ;
		
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		 
		String r = StringUtil.trimNull(list.get(0));
		
		return Integer.parseInt(r);
	}

	 
	public List<Profit> getProfitGraphList1(Profit profit) {
		// TODO Auto-generated method stub
		 
			String sql = " select  sum(shares*nav+unpaid+income-invest) as profit,sum(invest) as invest from qj_fund_db.assets a "
	                   + " left join qj_user_db.`user` u on(a.uid=u.uid)  where 1=1 ";
			if(!"".equals(StringUtil.trimNull(profit.get$like_mobile()))){
				sql +=" and u.mobile like '%" + profit.get$like_mobile() +"%'";
			}
			
		 
			if(!"".equals(StringUtil.trimNull(profit.get$like_name()))){
				sql +=" and u.name like '%" + profit.get$like_name() +"%'";
			}
			
            sql += " limit 1";
			
            if( "".equals(StringUtil.trimNull(profit.get$like_mobile()))&&"".equals(StringUtil.trimNull(profit.get$like_name()))){
            	sql=" select   sum(shares*nav+unpaid+income-invest) as profit,sum(invest) as invest from qj_fund_db.assets ";
            }
			
			Query query = getSession().createSQLQuery(sql);
			List list = query.list();
			Profit p1 = new Profit();
			Profit p2 = new Profit();
			List<Profit> returnList = new ArrayList<Profit>();
			
			
			
			if(list.size()!=0){
				Object[] o =(Object[]) list.get(0);
				p1.setGraphName("收益");
				p1.setGraphData(String.valueOf(Math.abs( new BigDecimal(Double.parseDouble(StringUtil.trimNull(o[0]))).setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue())));
				if(Double.parseDouble(StringUtil.trimNull(o[0]))<0){
					p1.setMinus("0");
				}else{
					p1.setMinus("1");
				}
				p2.setGraphName("投资额");
				p2.setGraphData( new BigDecimal(Double.parseDouble(StringUtil.trimNull(o[1]))).setScale(2,  BigDecimal.ROUND_HALF_UP).toString());
				p2.setMinus("1");
				
				if(!"0.00".equals(p2.getInvest())){
					double p = Double.parseDouble(StringUtil.trimNull(o[0]));
					double invest = Double.parseDouble(StringUtil.trimNull(o[1]));
					
					NumberFormat nt = NumberFormat.getPercentInstance();
					nt.setMinimumFractionDigits(2);
					
					p1.setRates(nt.format(p/invest));
					//p1.setRates(nt.format(invest/total));
				}
				
				returnList.add(p1);
				returnList.add(p2);
			}
			
			return returnList;
	}
	
	@Override
	public List<MonthInvestGraph> getProfitGraphList(Profit profit) {
		// TODO Auto-generated method stub
		 	String sdate = DateUtil.currentMonthMinusWithoutdd(6);
		 	String edate = DateUtil.currentMonthMinusWithoutdd(0);
			String sql = " select date,invest,profit,redemp,investwithcashbao,profitwithcashbao,redempwithcashbao from qj_qjs_db.month_invest where date between  '"+sdate+"' and '" +edate+"'";
		 
			Query query = getSession().createSQLQuery(sql);
	 
			List<MonthInvestGraph> returnList = new ArrayList<MonthInvestGraph>();
			
			//p2.setGraphData( new BigDecimal(Double.parseDouble(StringUtil.trimNull(o[1]))).setScale(2,  BigDecimal.ROUND_HALF_UP).toString());
			
			List list = query.list();
			for(int i=0;i<list.size();i++){
				MonthInvestGraph  mig = new MonthInvestGraph();
				Object[] o = (Object[]) list.get(i);
				mig.setDate(StringUtil.trimNull(o[0]));
				mig.setInvest(StringUtil.trimNull(o[1]));
				mig.setProfit(StringUtil.trimNull(o[2]));
				mig.setRedemp(StringUtil.trimNull(o[3]));
				mig.setInvestWithCashbao(StringUtil.trimNull(o[4]));
				mig.setProfitWithCashbao(StringUtil.trimNull(o[5]));
				mig.setRedempWithCashbao(StringUtil.trimNull(o[6]));
				returnList.add(mig);
			}
		 
			
			return returnList;
	}

	/* (非 Javadoc) 
	* <p>Title: static_month_invest</p> 
	* <p>Description: </p>  
	* @see com.forestry.dao.sys.ProfitDao#static_month_invest() 
	*/
	@Override
	public void static_month_invest() {
		// TODO Auto-generated method stub
		
		List<String> listMonth = DateUtil.getMonthWithoutdd(DateUtil.currentMonthMinusWithoutdd(1),DateUtil.currentMonthMinusWithoutdd(0));
		for(int i=0;i<listMonth.size();i++){
			 String sdate =listMonth.get(i);
			 String edate=listMonth.get(i);
		
		
		//String sdate =DateUtil.currentMonthMinusWithoutdd(1);
		//String edate= DateUtil.currentMonthMinusWithoutdd(1);
		String sql="";
	 
		//基金投资 
		//sql= "select ifnull(sum(invest),0) ,ifnull(sum(a.shares*a.nav+a.unpaid+a.income-a.invest),0) from  qj_fund_db.assets a where moditm between '" + sdate +"-01 00:00:00' and '" + edate +"-31 23:59:59'";
		sql="select ifnull(sum(sum),0) invest from qj_fund_db.operat_log where (operate=1 or operate=4) and (state=2 or state=3) and opdate between '" + sdate +"-01 00:00:00' and '" + edate +"-31 23:59:59'";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		//Object[] o =(Object[]) list.get(0);
		String invest = StringUtil.trimNull(list.get(0)); 
		//String profit = StringUtil.trimNull(o[1]); 
		
		//基金收益
		sql="select ifnull(sum(day_profit),0)  profit from qj_fund_db.profit_detail where    dt   between '" + sdate +"-01' and '" + edate +"-31'";
		query = getSession().createSQLQuery(sql);
		list = query.list();
	    String profit = StringUtil.trimNull(list.get(0)); 
		
		//活期宝投资 
		//sql= "select ifnull(sum(invest),0) ,ifnull(sum(a.shares*1+a.unpaid+a.income-a.invest),0) from  qj_cashbao_db.assets a where FROM_UNIXTIME(modify_time) between '" + sdate +"-01 00:00:00' and '" + edate +"-31 23:59:59'";
		sql="select ifnull(sum(sum),0) invest from qj_cashbao_db.trade_log where (operate=1 or operate=4) and (state=2 or state=3) and FROM_UNIXTIME(opdate) between '" + sdate +"-01 00:00:00' and '" + edate +"-31 23:59:59'";
		query = getSession().createSQLQuery(sql);
		list = query.list();
	 	String investCashbao = StringUtil.trimNull(list.get(0)); 
		
		//活期宝收益
		sql="select  ifnull(sum(profit),0) profit From qj_cashbao_db.everyday_detail where   dt between   '" + sdate.replaceAll("-", "") +"01' and '" + edate.replaceAll("-", "") +"31'";
		query = getSession().createSQLQuery(sql);
		list = query.list();
	    String profitCashbao = StringUtil.trimNull(list.get(0)); 
	 
		
		//基金赎回
		sql="select ifnull(sum(sum),0) redemping from qj_fund_db.operat_log where operate=2 and state=3 and opdate between '" + sdate +"-01 00:00:00' and '" + edate +"-31 23:59:59'";
		query = getSession().createSQLQuery(sql);
		list = query.list();
		String redemp =StringUtil.trimNull(list.get(0)); 
		
		//活期宝赎回
		sql="select ifnull(sum(sum),0) redemping from qj_cashbao_db.trade_log where operate=2 and state=3 and FROM_UNIXTIME(opdate) between '" + sdate +"-01 00:00:00' and '" + edate +"-31 23:59:59'";
		query = getSession().createSQLQuery(sql);
		list = query.list();
		String redempcashBao =StringUtil.trimNull(list.get(0)); 
		
		invest=new BigDecimal(invest).setScale(2,  BigDecimal.ROUND_HALF_UP).toString();
		profit=new BigDecimal(profit).setScale(2,  BigDecimal.ROUND_HALF_UP).toString();
		redemp=new BigDecimal(redemp).setScale(2,  BigDecimal.ROUND_HALF_UP).toString();
		
		investCashbao =new BigDecimal(Double.parseDouble(invest)+Double.parseDouble(investCashbao)).setScale(2,  BigDecimal.ROUND_HALF_UP).toString();
 		profitCashbao =new BigDecimal(Double.parseDouble(profit)+Double.parseDouble(profitCashbao)).setScale(2,  BigDecimal.ROUND_HALF_UP).toString();
 		redempcashBao=new BigDecimal(Double.parseDouble(redemp)+Double.parseDouble(redempcashBao)).setScale(2,  BigDecimal.ROUND_HALF_UP).toString();
		sql = "replace into qj_qjs_db.month_invest(date,invest,profit,redemp,investwithcashbao,profitwithcashbao,redempwithcashbao) values('"  + sdate+ "','" +invest +"','" +profit +"','" +redemp   +"','" +investCashbao  +"','" +profitCashbao  +"','" + redempcashBao     +"')";
		getSession().createSQLQuery(sql).executeUpdate();
		
		}
		//System.out.println(111);
	}
	
	 
}
