package com.forestry.dao.sys.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.forestry.dao.sys.ChoiceDao;
import com.forestry.dao.sys.ForestryDao;
import com.forestry.dao.sys.FundDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.Choice;
import com.forestry.model.sys.ChoiceSelfSum;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.Profit;

import core.dao.BaseDao;
import core.util.StringUtil;

/**
 * 
 *  Class Name: ChoiceDaoImpl.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月21日 下午2:12:44    
 *  @version 1.0
 */
@Repository
public class ChoiceDaoImpl extends BaseDao<Choice> implements ChoiceDao {

	public ChoiceDaoImpl() {
		super(Choice.class);
	}

	public enum FundType {
		STOCK_FUND("股票基金", 1), BOND_FUND("债券基金", 2), MONEY_FUND("货币基金", 3), MIX_FUND("混合基金", 4), INDEX_FUND("指数基金", 5), PRESERV_FUND("保本基金", 6), ETF_FUND("ETF", 7), QDII_FUND("QDII", 8), OTHER_FUND("其他", 20);
		// 成员变量
		private String name;
		private int code;

		// 构造方法
		private FundType(String name, int code) {
			this.name = name;
			this.code = code;
		}

	}
	
	
	
	@Override
	public List<Choice> getChoiceSelfList(Choice choice) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public long getChoiceSelfCount(Choice choice) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Choice> getChoiceSelfGraphList(Choice choice) {
		// TODO Auto-generated method stub
		List<Choice> returnList = new ArrayList<Choice>(FundType.values().length+1);

		double total = 0;
		for (int i = 0; i < FundType.values().length; i++) {
			FundType type = FundType.values()[i];
			
			StringBuffer sql = new StringBuffer("select sum(sum)  from qj_fund_db.operat_log where fdcode  in ( select fdcode from  qj_fund_db.fund_info  where type =").append( type.code ).append( ") and (operate=1 or operate=4) and (state=1 or state=3)");
			Choice returnChoice = new Choice();
			returnChoice.setGraphName(type.name);
			Query query = getSession().createSQLQuery(sql.toString());
			List list = query.list();
			String sum = StringUtil.trimNull(list.get(0));
		
			if(!"".equals(sum)){
				total += Double.parseDouble(sum);
				returnChoice.setGraphData(new BigDecimal(sum).setScale(2,  BigDecimal.ROUND_HALF_UP).toString());
			}else{
				returnChoice.setGraphData("0");
			}
			returnList.add(returnChoice);
		}
		
		StringBuffer sql = new StringBuffer("select ifnull(sum(sum),0) sum From qj_cashbao_db.trade_log where (operate=1 or operate=4) and (state=2 or state =3 )");
		Choice returnChoice = new Choice();
		returnChoice.setGraphName("活期宝");
		Query query = getSession().createSQLQuery(sql.toString());
		List list = query.list();
		String sum = StringUtil.trimNull(list.get(0));
		returnChoice.setGraphData(new BigDecimal(sum).setScale(2,  BigDecimal.ROUND_HALF_UP).toString());
		total += Double.parseDouble(sum);
		returnList.add(returnChoice);
		
		if (total != 0) {
			for (int i = 0; i < returnList.size(); i++) {
				Choice f = returnList.get(i);
				NumberFormat nt = NumberFormat.getPercentInstance();
				nt.setMinimumFractionDigits(2);
				if("0".equals(f.getGraphData())){
					f.setRates("0.00%");
				}else{
					f.setRates(nt.format(Double.parseDouble(f.getGraphData()) / (double) total));
				}
			}
		}
		return returnList;
	}
	
	
	@Override
	public List<Choice> getChoiceRedempSelfGraphList(Choice choice) {
		// TODO Auto-generated method stub
		List<Choice> returnList = new ArrayList<Choice>(FundType.values().length+1);

		double total = 0;
		for (int i = 0; i < FundType.values().length; i++) {
			FundType type = FundType.values()[i];
			
			StringBuffer sql = new StringBuffer("select sum(sum)  from qj_fund_db.operat_log where fdcode  in ( select fdcode from  qj_fund_db.fund_info  where type =").append( type.code ).append( ") and operate=2 and ( state=3)");
			Choice returnChoice = new Choice();
			returnChoice.setGraphName(type.name);
			Query query = getSession().createSQLQuery(sql.toString());
			List list = query.list();
			String sum = StringUtil.trimNull(list.get(0));
		
			if(!"".equals(sum)){
				total += Double.parseDouble(sum);
				returnChoice.setGraphData(new BigDecimal(sum).setScale(2,  BigDecimal.ROUND_HALF_UP).toString());
			}else{
				returnChoice.setGraphData("0");
			}
			returnList.add(returnChoice);
		}
		
		StringBuffer sql = new StringBuffer("select ifnull(sum(sum),0) sum From qj_cashbao_db.trade_log where operate=2 and ( state =3 )");
		Choice returnChoice = new Choice();
		returnChoice.setGraphName("活期宝");
		Query query = getSession().createSQLQuery(sql.toString());
		List list = query.list();
		String sum = StringUtil.trimNull(list.get(0));
		returnChoice.setGraphData(new BigDecimal(sum).setScale(2,  BigDecimal.ROUND_HALF_UP).toString());
		total += Double.parseDouble(sum);
		returnList.add(returnChoice);
		
		if (total != 0) {
			for (int i = 0; i < returnList.size(); i++) {
				Choice f = returnList.get(i);
				NumberFormat nt = NumberFormat.getPercentInstance();
				nt.setMinimumFractionDigits(2);
				if("0".equals(f.getGraphData())){
					f.setRates("0.00%");
				}else{
					f.setRates(nt.format(Double.parseDouble(f.getGraphData()) / (double) total));
				}
			}
		}
		return returnList;
	}
	
	/**
	 * 
	 *  Function:
	 *  @author TongXin  DateTime 2015年8月21日 下午8:13:51
	 *  @param choice
	 *  @return
	 */
	@Override
	public List getMonthSumLineGraph(Choice choice){
		String sql =  " select   opdate1 as date,"
				+" max(case fund when '股票基金' then   sum/10000 else 0  end) as 'stock_fund',"
				+" max(case fund when '债券基金' then   sum/10000 else 0  end )as 'bond_fund'," 
				+" max(case fund when '货币基金' then   sum/10000 else 0  end) as 'money_fund',"
				+" max(case fund when '混合基金' then   sum/10000 else 0  end )as 'mix_fund'," 
				+" max(case fund when '指数基金' then   sum/10000 else 0  end) as 'index_fund',"
				+" max(case fund when '保本基金' then   sum/10000 else 0  end )as 'preserv_fund'," 
				+" max(case fund when 'ETF' then   sum/10000 else 0  end) as 'eft_fund',"
				+" max(case fund when 'QDII' then   sum/10000 else 0  end )as 'qdii_fund',"
				+" max(case fund when '其它' then   sum/10000 else 0  end) as 'other_fund',"
				+" max(case fund when '活期宝' then   sum/10000 else 0  end )as 'cashbao'"
				+" from"
				+" (select"
				+" case when type =1 then '股票基金' "
				+" when type =2 then '债券基金'  "
				+" when type =3 then '货币基金'  "
				+" when type =4 then '混合基金'  "
				+" when type =5 then '指数基金'  "
				+" when type =6 then '保本基金'  "
				+" when type =7 then 'ETF' "
				+" when type =8 then 'QDII' "
				+" when type =20 then '其它' "
				+" end  as fund ,"
				+" DATE_FORMAT(o.opDate,'%Y-%m') as opDate1 ,"
				+" sum(sum) as sum"
				+" from qj_fund_db.operat_log o,qj_fund_db.finance_schema s"
				+" where ((o.operate=1 or o.operate=4) or o.operate=4) and (o.state=1 or o.state=3) and o.sid=s.sid     and TIMESTAMPDIFF(MONTH,o.opDate,NOW())<13 "
				+" group by fund,opdate1"
				+" union ALL"
				+" select  '活期宝' as fund, DATE_FORMAT(FROM_UNIXTIME(opDate),'%Y-%m') as opDate1 ,sum(sum) as sum "
				+" from qj_cashbao_db.trade_log "
				+" where (operate=1 or operate=4) and (state=2 or state=3) "   
				+" and TIMESTAMPDIFF(MONTH,FROM_UNIXTIME(opDate),NOW())<13  group by opDate1"
				+" )t"
				+" group by opdate1";
		Query query=getSession().createSQLQuery(sql);
		 
		List list=query.list();
		
		List<ChoiceSelfSum> returnList = new ArrayList<ChoiceSelfSum>();
		
		for(int i=0;i<list.size();i++){
			ChoiceSelfSum csf = new ChoiceSelfSum();
			Object[] o = (Object[]) list.get(i);
			csf.setDate(StringUtil.trimNull(o[0]));
			csf.setStock_fund(StringUtil.trimNull(o[1]));
			csf.setBond_fund(StringUtil.trimNull(o[2]));
			csf.setMoney_fund(StringUtil.trimNull(o[3]));
			csf.setMix_fund(StringUtil.trimNull(o[4]));
			csf.setIndex_fund(StringUtil.trimNull(o[5]));
			csf.setPreserv_fund(StringUtil.trimNull(o[6]));
			csf.setEtf_fund(StringUtil.trimNull(o[7]));
			csf.setQdii_fund(StringUtil.trimNull(o[8]));
			csf.setOther_fund(StringUtil.trimNull(o[9]));
			csf.setCashbao(StringUtil.trimNull(o[10]));
			returnList.add(csf);
		}
		return returnList;
	}

	
	/**
	 * 
	 *  Function:
	 *  @author TongXin  DateTime 2015年8月21日 下午8:13:51
	 *  @param choice
	 *  @return
	 */
	@Override
	public List getMonthRedempLineGraph(Choice choice){
		String sql =  " select   opdate1 as date,"
				+" max(case fund when '股票基金' then   sum/10000 else 0  end) as 'stock_fund',"
				+" max(case fund when '债券基金' then   sum/10000 else 0  end )as 'bond_fund'," 
				+" max(case fund when '货币基金' then   sum/10000 else 0  end) as 'money_fund',"
				+" max(case fund when '混合基金' then   sum/10000 else 0  end )as 'mix_fund'," 
				+" max(case fund when '指数基金' then   sum/10000 else 0  end) as 'index_fund',"
				+" max(case fund when '保本基金' then   sum/10000 else 0  end )as 'preserv_fund'," 
				+" max(case fund when 'ETF' then   sum/10000 else 0  end) as 'eft_fund',"
				+" max(case fund when 'QDII' then   sum/10000 else 0  end )as 'qdii_fund',"
				+" max(case fund when '其它' then   sum/10000 else 0  end) as 'other_fund',"
				+" max(case fund when '活期宝' then   sum/10000 else 0  end )as 'cashbao'"
				+" from"
				+" (select"
				+" case when type =1 then '股票基金' "
				+" when type =2 then '债券基金'  "
				+" when type =3 then '货币基金'  "
				+" when type =4 then '混合基金'  "
				+" when type =5 then '指数基金'  "
				+" when type =6 then '保本基金'  "
				+" when type =7 then 'ETF' "
				+" when type =8 then 'QDII' "
				+" when type =20 then '其它' "
				+" end  as fund ,"
				+" DATE_FORMAT(o.opDate,'%Y-%m') as opDate1 ,"
				+" sum(sum) as sum"
				+" from qj_fund_db.operat_log o,qj_fund_db.finance_schema s"
				+" where o.operate=2 and (  o.state=3) and o.sid=s.sid     and TIMESTAMPDIFF(MONTH,o.opDate,NOW())<13 "
				+" group by fund,opdate1"
				+" union ALL"
				+" select  '活期宝' as fund, DATE_FORMAT(FROM_UNIXTIME(opDate),'%Y-%m') as opDate1 ,sum(sum) as sum "
				+" from qj_cashbao_db.trade_log "
				+" where operate=2 and (  state=3) "   
				+" and TIMESTAMPDIFF(MONTH,FROM_UNIXTIME(opDate),NOW())<13  group by opDate1"
				+" )t"
				+" group by opdate1";
		Query query=getSession().createSQLQuery(sql);
		 
		List list=query.list();
		
		List<ChoiceSelfSum> returnList = new ArrayList<ChoiceSelfSum>();
		
		for(int i=0;i<list.size();i++){
			ChoiceSelfSum csf = new ChoiceSelfSum();
			Object[] o = (Object[]) list.get(i);
			csf.setDate(StringUtil.trimNull(o[0]));
			csf.setStock_fund(StringUtil.trimNull(o[1]));
			csf.setBond_fund(StringUtil.trimNull(o[2]));
			csf.setMoney_fund(StringUtil.trimNull(o[3]));
			csf.setMix_fund(StringUtil.trimNull(o[4]));
			csf.setIndex_fund(StringUtil.trimNull(o[5]));
			csf.setPreserv_fund(StringUtil.trimNull(o[6]));
			csf.setEtf_fund(StringUtil.trimNull(o[7]));
			csf.setQdii_fund(StringUtil.trimNull(o[8]));
			csf.setOther_fund(StringUtil.trimNull(o[9]));
			csf.setCashbao(StringUtil.trimNull(o[10]));
			returnList.add(csf);
		}
		return returnList;
	}
	
	/**
	 * 返回定制组合的性别分布
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getSchemaUserCountBySex(int type){
		String hql="SELECT case when(LEFT(RIGHT(u.identity,2),1)%2=0) then '女' else '男' END as age,COUNT(*) as count" +
				" from qj_user_db.`user` u," +
				"(SELECT DISTINCT o.uid from qj_fund_db.operat_log o,qj_fund_db.finance_schema s" +
				" where ((o.operate=1 or o.operate=4) or o.operate=4) and o.state=3 and o.sid=s.sid and s.type=?) b" +
				" where u.uid=b.uid and LENGTH(u.identity)=18 GROUP BY age";
		Query query=getSession().createSQLQuery(hql);
		query.setParameter(0, type);
		List<Map<String,Object>> l=new LinkedList<Map<String,Object>>();
		List list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] o=(Object[])list.get(i);
			Map<String,Object> map=new Hashtable<String,Object>();
			map.put("sex", (String)o[0]);
			map.put("count", ((BigInteger)o[1]).longValue());
			l.add(map);
		}
		return l;
	}
	
	/**
	 * 返回定制组合的年龄分布
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getSchemaUserCountByAge(int type){
		String hql="SELECT case " +
				//"	when SUBSTRING(u.identity,7,4)>1999 then '00后'" +
				"	when SUBSTRING(u.identity,7,4)>1989 then '90后'" +
				"	when SUBSTRING(u.identity,7,4)>1979 then '80后'" +
				"	when SUBSTRING(u.identity,7,4)>1969 then '70后'" +
				"	when SUBSTRING(u.identity,7,4)>1959 then '60后'" +
				"	when SUBSTRING(u.identity,7,4)>1949 then '50后'" +
				"	else '其他' END as age,COUNT(*) as count" +
				" from qj_user_db.`user` u," +
				"(SELECT DISTINCT o.uid from qj_fund_db.operat_log o,qj_fund_db.finance_schema s" +
				" where  (o.operate=1 or o.operate=4) and o.state=3 and o.sid=s.sid and s.type=?) b" +
				" where u.uid=b.uid and LENGTH(u.identity)=18 GROUP BY age";
		Query query=getSession().createSQLQuery(hql);
		query.setParameter(0, type);
		List<Map<String,Object>> l=new LinkedList<Map<String,Object>>();
		List list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] o=(Object[])list.get(i);
			Map<String,Object> map=new Hashtable<String,Object>();
			map.put("age", (String)o[0]);
			map.put("count", ((BigInteger)o[1]).longValue());
			l.add(map);
		}
		return l;
	}
	
	/**
	 * 返回定制组合各年龄段用户按月份的时间分布
	 * @param type
	 * @return
	 */
	@Override
	public List getSchemaMonthDistribute(int type){
		String hql="SELECT DISTINCT opDate,sum(CASE age WHEN '90s' THEN `count` END) as '90s'," +
				"sum(CASE age WHEN '80s' THEN `count` END) as '80s',sum(CASE age WHEN '70s' THEN `count` END) as '70s'," +
				"sum(CASE age WHEN '60s' THEN `count` END) as '60s',sum(CASE age WHEN '50s' THEN `count` END) as '50s'," +
				"sum(CASE age WHEN 'other' THEN `count` END) as 'other'" +
				"	FROM (SELECT case " +
				//"	when SUBSTRING(u.identity,7,4)>1999 then '00后'" +
				"	when SUBSTRING(u.identity,7,4)>1989 then '90s'" +
				"	when SUBSTRING(u.identity,7,4)>1979 then '80s'" +
				"	when SUBSTRING(u.identity,7,4)>1969 then '70s'" +
				"	when SUBSTRING(u.identity,7,4)>1959 then '60s'" +
				"	when SUBSTRING(u.identity,7,4)>1949 then '50s'" +
				"	else 'other' END as age,COUNT(*) as count,b.opDate" +
				" from qj_user_db.`user` u," +
				"(SELECT DISTINCT o.uid,DATE_FORMAT(o.opDate,'%Y-%m') as opDate from qj_fund_db.operat_log o,qj_fund_db.finance_schema s" +
				" where (o.operate=1 or o.operate=4) and o.state=3 and o.sid=s.sid and s.type=? and TIMESTAMPDIFF(MONTH,last_day (o.opDate),last_day(curdate()))<13) b" +
				" where u.uid=b.uid and LENGTH(u.identity)=18 GROUP BY age,opDate)C" +
				" GROUP BY C.opDate";
		Query query=getSession().createSQLQuery(hql);
		query.setParameter(0, type);
		List list=query.list();
		return list;
	}
	
	/**
	 * 返回定制组合投资额的年龄分布
	 * @param type
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getSchemaSumByAge(int type){
		String hql="SELECT case " +
				//"	when SUBSTRING(u.identity,7,4)>1999 then '00后'" +
				"	when SUBSTRING(u.identity,7,4)>1989 then '90后'" +
				"	when SUBSTRING(u.identity,7,4)>1979 then '80后'" +
				"	when SUBSTRING(u.identity,7,4)>1969 then '70后'" +
				"	when SUBSTRING(u.identity,7,4)>1959 then '60后'" +
				"	when SUBSTRING(u.identity,7,4)>1949 then '50后'" +
				"	else '其他' END as age,sum(b.total) as total" +
				" from qj_user_db.`user` u," +
				"(SELECT DISTINCT o.uid,DATE_FORMAT(o.opDate,'%Y-%m') as opDate1,sum(sum) as total" +
				" from qj_fund_db.operat_log o,qj_fund_db.finance_schema s" +
				" where (o.operate=1 or o.operate=4) and o.state=3 and o.sid=s.sid and s.type=? GROUP BY o.uid,opDate1) b" +
				" where u.uid=b.uid and LENGTH(u.identity)=18 GROUP BY age";
		Query query=getSession().createSQLQuery(hql);
		query.setParameter(0, type);
		List<Map<String,Object>> l=new LinkedList<Map<String,Object>>();
		List list=query.list();
		for(int i=0;i<list.size();i++){
			Object[] o=(Object[])list.get(i);
			Map<String,Object> map=new Hashtable<String,Object>();
			map.put("age", (String)o[0]);
			map.put("count", o[1]);
			l.add(map);
		}
		return l;
	}
	
	/**
	 * 返回定制组合投资额各年龄段用户按月份的分布
	 * @param type
	 * @return
	 */
	@Override
	public List getSchemaMonthSumDistribute(int type){
		String hql="SELECT DISTINCT opDate,sum(CASE age WHEN '90s' THEN total END) as '90s'," +
				"sum(CASE age WHEN '80s' THEN total END) as '80s',sum(CASE age WHEN '70s' THEN total END) as '70s'," +
				"sum(CASE age WHEN '60s' THEN total END) as '60s',sum(CASE age WHEN '50s' THEN total END) as '50s'," +
				"sum(CASE age WHEN 'other' THEN total END) as 'other'" +
				"	FROM (SELECT case " +
				"	when SUBSTRING(u.identity,7,4)>1989 then '90s'" +
				"	when SUBSTRING(u.identity,7,4)>1979 then '80s'" +
				"	when SUBSTRING(u.identity,7,4)>1969 then '70s'" +
				"	when SUBSTRING(u.identity,7,4)>1959 then '60s'" +
				"	when SUBSTRING(u.identity,7,4)>1949 then '50s'" +
				"	else '其他' END as age,b.opDate1 as opDate,sum(b.total) as total" +
				" from qj_user_db.`user` u," +
				"(SELECT DISTINCT o.uid,DATE_FORMAT(o.opDate,'%Y-%m') as opDate1,sum(sum) as total" +
				" from qj_fund_db.operat_log o,qj_fund_db.finance_schema s" +
				" where (o.operate=1 or o.operate=4) and o.state=3 and o.sid=s.sid and s.type=? and TIMESTAMPDIFF(MONTH,last_day (o.opDate),last_day(curdate()))<13 GROUP BY o.uid,opDate1) b" +
				" where u.uid=b.uid and LENGTH(u.identity)=18 GROUP BY age,opDate)C" +
				" GROUP BY C.opDate";
		Query query=getSession().createSQLQuery(hql);
		query.setParameter(0, type);
		List list=query.list();
		return list;
	}
	
	

}
