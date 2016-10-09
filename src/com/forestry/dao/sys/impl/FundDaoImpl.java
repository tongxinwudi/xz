package com.forestry.dao.sys.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.forestry.dao.sys.FundDao;
import com.forestry.model.sys.ErrorCommonInfo;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.FundHold;
import com.forestry.model.sys.FundLimit;

import core.dao.BaseDao;
import core.util.DateUtil;
import core.util.StringUtil;

/**
 * @author Yang Tian
 * @email 1298588579@qq.com
 */
@Repository
public class FundDaoImpl extends BaseDao<Fund> implements FundDao {

	public FundDaoImpl() {
		super(Fund.class);
	}

	static List<Long> paraList = new ArrayList<Long>();

	static {
		paraList.add((long) 10);
		paraList.add((long) 100);
		//paraList.add((long) 200);
		//paraList.add((long) 500);
		paraList.add((long) 1000);
		//paraList.add((long) 5000);
		paraList.add((long) 10000);
		//paraList.add((long) 50000);
		//paraList.add((long) 10000);
		paraList.add((long) 100000);
		//paraList.add((long) 500000);
		//paraList.add((long) 1000000);
		//paraList.add((long) 2000000);
		//paraList.add((long) 5000000);
	}

	@Override
	public List<Fund> getFundLimitGraphList(Fund fund) {
		// TODO Auto-generated method stub

		List<Fund> returnList = new ArrayList<Fund>();

		String strsql = "select count(*) as num from ("
                +" select uid, sum ,abbrev as fund,opDate from qj_fund_db.operat_log ol1 "
                +" where (ol1.operate=1 or ol1.operate=4) "
                +" and (ol1.state=1 or ol1.state=3) "
                +" and ol1.sid=0 " 
                +" union all "
                +" select ol2.uid,sum(sum)  as sum,fs.`name` as fund,ol2.opDate  from qj_fund_db.operat_log ol2 "
                +" left join qj_fund_db.finance_schema fs on(fs.sid = ol2.sid) "
                +" where  ol2.sid<>0 "
                +" and (ol2.operate=1 or ol2.operate=4) and (ol2.state=1 or ol2.state=3) "
                +" group by uid,opDate "
                +" union all "
                +" select  tl.uid,tl.sum as sum ,'活期宝' as fund ,FROM_UNIXTIME(opDate) as  opdate   From  qj_cashbao_db.trade_log tl "
                +" where (tl.operate=1 or tl.operate=4) "   
                +" and   (tl.state=2 or tl.state=3) "
                +" ) t where sum ";
               
		
		long total = 0;
		for (int i = 0; i < paraList.size(); i++) {
			StringBuffer sql = new StringBuffer(strsql);
					//"select count(*) as num from qj_fund_db.operat_log where sum ");
			
					Fund returnFund = new Fund();
			if (i == 0) {
			 
				sql.append("<").append(paraList.get(i));
				returnFund.setGraphName("申购额<" + paraList.get(i) + "元");
			} else if (i == paraList.size() - 1) {
				sql.append(">=").append(paraList.get(i));
				returnFund.setGraphName("申购额≥" + paraList.get(i) + "元");
			} else {
				sql.append(">=").append(paraList.get(i-1)).append(" and sum <")
						.append(paraList.get(i ));
				returnFund.setGraphName(paraList.get(i-1) + "元≤申购额<"
						+ paraList.get(i ) + "元");
			}

			Query query = getSession().createSQLQuery(sql.toString());
			List list = query.list();
			String sum = StringUtil.trimNull(list.get(0));
			returnFund.setGraphData(sum);
			total += Long.parseLong(sum);
			returnList.add(returnFund);
		}
		if (total != 0) {
			for (int i = 0; i < returnList.size(); i++) {
				Fund f = returnList.get(i);
				NumberFormat nt = NumberFormat.getPercentInstance();
				nt.setMinimumFractionDigits(2);
				f.setRates(nt.format(Long.parseLong(f.getGraphData())
						/ (double) total));
			}
		}

		return returnList;
	}

	@Override
	public List<Fund> getFundList(Fund fund) {
		// TODO Auto-generated method stub

		String sql = "select t.uid,u.`name`, u.mobile,t.invest as sum,t.fund,t.opDate from ("
                    +" select uid, sum as invest,abbrev as fund,opDate from qj_fund_db.operat_log ol1 "
                    +" where (ol1.operate=1 or ol1.operate=4) "
                    +" and (ol1.state=1 or ol1.state=3) "
                    +" and ol1.sid=0 " 
                    +" union all "
                    +" select ol2.uid,sum(sum)  as invest,fs.`name` as fund,ol2.opDate  from qj_fund_db.operat_log ol2 "
                    +" left join qj_fund_db.finance_schema fs on(fs.sid = ol2.sid) "
                    +" where  ol2.sid<>0 "
                    +" and (ol2.operate=1 or ol2.operate=4) and (ol2.state=1 or ol2.state=3) "
                    +" group by uid,opDate "
                    +" union all "
                    +" select  tl.uid,tl.sum as invest ,'活期宝' as fund ,FROM_UNIXTIME(opDate) as  opdate   From  qj_cashbao_db.trade_log tl "
                    +" where  (tl.operate=1 or tl.operate=4) "   
                    +" and   (tl.state=2 or tl.state=3) "
                    +" ) t "
                    +" left join qj_user_db.`user` u on(u.uid=t.uid)"
                    + "where 1=1";
		
		
		
		
		if (!"".equals(StringUtil.trimNull(fund.get$like_mobile()))) {
			sql += " and mobile like '%" + fund.get$like_mobile() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fund.get$like_name()))) {
			sql += " and name like '%" + fund.get$like_name() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fund.get$sinvest()))) {
			sql += " and invest  >=" + fund.get$sinvest();
		}

		if (!"".equals(StringUtil.trimNull(fund.get$einvest()))) {
			sql += " and invest    <" + fund.get$einvest();
		}

		String key = fund.getSortedConditions().keySet().iterator().next();
		String value = fund.getSortedConditions().values().iterator().next();

		sql += "   order by " + key + " " + value + " limit "
				+ fund.getFirstResult() + "," + fund.getMaxResults();

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Fund> listFund = new ArrayList<Fund>();

		for (int i = 0; i < list.size(); i++) {
			Fund f = new Fund();
			Object[] o = (Object[]) list.get(i);
			f.setId((String) o[0]);
			f.setName((String) o[1]);
			f.setMobile((String) o[2]);
			f.setInvest(StringUtil.trimNull(o[3]));
			f.setFund(StringUtil.trimNull(o[4]));
			f.setOpDate(StringUtil.trimNull(o[5]));
			listFund.add(f);
		}
		return listFund;
	}

	@Override
	public long getFundListCount(Fund fund) {
		// TODO Auto-generated method stub

		String sql = "select count(1) from (select t.uid,u.`name`, u.mobile,t.invest as sum,t.fund,t.opDate from ("
                +" select uid, sum as invest,abbrev as fund,opDate from qj_fund_db.operat_log ol1 "
                +" where (ol1.operate=1 or ol1.operate=4) "
                +" and (ol1.state=1 or ol1.state=3) "
                +" and ol1.sid=0 " 
                +" union all "
                +" select ol2.uid,sum(sum)  as invest,fs.`name` as fund,ol2.opDate  from qj_fund_db.operat_log ol2 "
                +" left join qj_fund_db.finance_schema fs on(fs.sid = ol2.sid) "
                +" where  ol2.sid<>0 "
                +" and (ol2.operate=1 or ol2.operate=4) and (ol2.state=1 or ol2.state=3) "
                +" group by uid,opDate "
                +" union all "
                +" select  tl.uid,tl.sum as invest ,'cashbao' as fund ,FROM_UNIXTIME(opDate) as  opdate   From  qj_cashbao_db.trade_log tl "
                +" where (tl.operate=1 or tl.operate=4) "   
                +" and   (tl.state=2 or tl.state=3) "
                +" ) t "
                +" left join qj_user_db.`user` u on(u.uid=t.uid)"
                + "where 1=1";
	
	
	
	
	if (!"".equals(StringUtil.trimNull(fund.get$like_mobile()))) {
		sql += " and mobile like '%" + fund.get$like_mobile() + "%'";
	}

	if (!"".equals(StringUtil.trimNull(fund.get$like_name()))) {
		sql += " and name like '%" + fund.get$like_name() + "%'";
	}

	if (!"".equals(StringUtil.trimNull(fund.get$sinvest()))) {
		sql += " and invest  >=" + fund.get$sinvest();
	}

	if (!"".equals(StringUtil.trimNull(fund.get$einvest()))) {
		sql += " and invest    <" + fund.get$einvest();
	}
		sql += "  )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	/**
	 * 频率统计
	 */
	@Override
	public List<Fund> getFundFrequencyList(Fund fund) {
		// TODO Auto-generated method stub
		String sql = "select * from ( select fdcode ,abbrev,count(1) count,  operate from qj_fund_db.operat_log where  uid not in ('0020141009019933','0020141204044727','0020141225104038','0020140421000094') and sid=0   and fdcode in ( select DISTINCT fdcode from qj_fund_db.operat_log  )  and   state=3  ";
		if (!"".equals(StringUtil.trimNull(fund.get$like_id()))) {
			sql += " and  fdcode like '%" + fund.get$like_id() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fund.get$like_name()))) {
			sql += " and abbrev like '%" + fund.get$like_name() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fund.get$type()))) {
			sql += " and operate   =" + fund.get$type();
		}

		sql += " group by fdcode )t ";

		String key = fund.getSortedConditions().keySet().iterator().next();
		String value = fund.getSortedConditions().values().iterator().next();

		sql += "   order by " + key + " " + value + " limit "
				+ fund.getFirstResult() + "," + fund.getMaxResults();

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Fund> listFund = new ArrayList<Fund>();

		for (int i = 0; i < list.size(); i++) {
			Fund f = new Fund();
			Object[] o = (Object[]) list.get(i);
			f.setId((String) o[0]);
			f.setName((String) o[1]);
			f.setCount(StringUtil.trimNull(o[2]));
			if ("1".equals(StringUtil.trimNull(o[3]))) {
				f.setOperate("申购");
			} else if ("2".equals(StringUtil.trimNull(o[3]))) {
				f.setOperate("赎回");
			}
			listFund.add(f);
		}
		return listFund;
	}

	@Override
	public long getFundFrequencyCount(Fund fund) {
		// TODO Auto-generated method stub
		String sql = "select count(1) from (select * from ( select fdcode ,abbrev,count(1) count, operate from qj_fund_db.operat_log where  uid not in ('0020141009019933','0020141204044727','0020141225104038','0020140421000094') and sid=0   and fdcode in ( select DISTINCT fdcode from qj_fund_db.operat_log  )  and   state=3  ";
		if (!"".equals(StringUtil.trimNull(fund.get$like_id()))) {
			sql += " and  fdcode like '%" + fund.get$like_id() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fund.get$like_name()))) {
			sql += " and abbrev like '%" + fund.get$like_name() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fund.get$type()))) {
			sql += " and operate   =" + fund.get$type();
		}
		sql += " group by fdcode )t  )tt";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}
	
	/**
	 * 按月份统计基金的总申购频率
	 * @return
	 */
	@Override
	public List getFundFrequencyMonth(){
		String hql="SELECT count(CASE operate WHEN 1 THEN operate END) as buy," +
				"count(CASE operate WHEN 2 THEN operate END) as sale," +
				"DATE_FORMAT(opDate,'%Y-%m') as opMonth " +
				"FROM qj_fund_db.operat_log WHERE uid not in ('0020141009019933','0020141204044727','0020141225104038','0020140421000094') and state=3 and sid=0 and TIMESTAMPDIFF(MONTH,last_day(opDate),last_day(curdate()))<13 " +
				"GROUP BY opMonth";
		return getSession().createSQLQuery(hql).list();
	}
	
	/**
	 * 按月份统计投资组合的总申购频率
	 * @return
	 */
	@Override
	public List getAssemblyFrequencyMonth(){
		String hql="SELECT count(DISTINCT CASE operate WHEN 1 THEN  sid END) as buy," +
				"count(DISTINCT CASE operate WHEN 2 THEN  sid END) as sale," +
				"DATE_FORMAT(opDate,'%Y-%m') as opMonth " +
				"FROM qj_fund_db.operat_log WHERE uid not in ('0020141009019933','0020141204044727','0020141225104038','0020140421000094') and state=3 and sid!=0 and TIMESTAMPDIFF(MONTH,last_day(opDate),last_day(curdate()))<13 " +
				"GROUP BY opMonth";
		return getSession().createSQLQuery(hql).list();
	}
	
	/**
	 * 按月份统计现金宝的总申购频率
	 * @return
	 */
	@Override
	public List getCashbaoFrequencyMonth(){
		String hql="SELECT count(CASE operate WHEN 1 THEN  operate END) as buy," +
				"count(CASE operate WHEN 2 THEN  operate END) as sale," +
				"FROM_UNIXTIME( opDate, '%Y-%m' ) as opMonth" +
				"	FROM qj_cashbao_db.trade_log WHERE uid not in ('0020141009019933','0020141204044727','0020141225104038','0020140421000094') and state=3 " +
				"and TIMESTAMPDIFF(MONTH,FROM_UNIXTIME( opDate, '%Y-%m-%d' ),last_day(curdate()))<13 " +
				"GROUP BY opMonth";
		return getSession().createSQLQuery(hql).list();
	}

	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金和金额
	 * 
	 * @return
	 */
	@Override
	public List getSaleAllOnceFund() {
		String hql = "SELECT A.uid,A.fdcode,A.tshares FROM"
				+ "(SELECT uid,fdcode,sum(sum) AS tshares	FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 2 AND state = 3 AND sid = 0"
				+ "	GROUP BY uid,fdcode	HAVING count(*)=1) A"
				+ "  LEFT OUTER JOIN"
				+ "(SELECT uid,fdcode,sum(sum) AS tshares	FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 1 AND state = 3 AND sid = 0"
				+ "	GROUP BY uid,fdcode) B"
				+ "  ON A.uid = B.uid AND A.fdcode = B.fdcode"
				+ "	WHERE (B.tshares-A.tshares)<1";
		Query query = getSession().createSQLQuery(hql);
		return query.list();
	}

	/**
	 * 单只基金，一次或多次申购，一次全部赎回的基金持有时间
	 * 
	 * @return
	 */
	@Override
	public Map<String, Integer> getSaleAllOnceFundHoldTime() {
		String hql = "SELECT C.fdcode,sum(C.holdTime) AS holdTime FROM("
				+ "SELECT A.uid,A.fdcode,DATEDIFF(A.opDate, B.opDate) AS holdTime FROM("
				+ "	SELECT uid,fdcode,opDate,sum(sum) AS tshares FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 2 AND state = 3 AND sid = 0"
				+ "	GROUP BY uid,fdcode	HAVING count(*) = 1"
				+ ") A"
				+ "	LEFT OUTER JOIN ("
				+ "SELECT uid,fdcode,min(opDate) AS opDate,sum(sum) AS tshares FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 1 AND state = 3 AND sid = 0"
				+ "	GROUP BY uid,fdcode"
				+ ") B ON A.uid = B.uid AND A.fdcode = B.fdcode	WHERE (B.tshares-A.tshares)<1"
				+ ") AS C" + "	GROUP BY C.fdcode";
		Query query = getSession().createSQLQuery(hql);
		Map<String, Integer> map = new Hashtable<String, Integer>();
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			map.put((String) o[0], ((BigDecimal) o[1]).intValue());
		}
		return map;
	}

	/**
	 * 单只基金，一次或多次申购，一次赎回部分基金
	 * 
	 * @return
	 */
	@Override
	public List getSalePartOnceFund() {
		String hql = "SELECT A.uid,A.fdcode,A.tshares FROM"
				+ "(SELECT uid,fdcode,sum(sum) AS tshares	FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 2 AND state = 3 AND sid = 0"
				+ "	GROUP BY uid,fdcode	HAVING count(*)=1) A"
				+ "  LEFT OUTER JOIN"
				+ "(SELECT uid,fdcode,sum(sum) AS tshares	FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 1 AND state = 3 AND sid = 0"
				+ "	GROUP BY uid,fdcode) B"
				+ "  ON A.uid = B.uid AND A.fdcode = B.fdcode"
				+ "	WHERE A.tshares < B.tshares-1";
		Query query = getSession().createSQLQuery(hql);
		return query.list();
	}

	/**
	 * 单只基金，一次或多次申购，只赎回过一次，但是没有完全赎回
	 * 
	 * @return
	 */
	@Override
	public Map<String, Integer> getSalePartOnceFundHoldTime() {
		String hql = "SELECT C.fdcode,sum(C.holdTime) AS holdTime FROM("
				+ "SELECT A.uid,A.fdcode,DATEDIFF(NOW(), B.opDate) AS holdTime FROM("
				+ "	SELECT uid,fdcode,opDate,sum(sum) AS tshares FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 2 AND state = 3 AND sid = 0"
				+ "	GROUP BY uid,fdcode	HAVING count(*) = 1"
				+ ") A"
				+ "	LEFT OUTER JOIN ("
				+ "SELECT uid,fdcode,min(opDate) AS opDate,sum(sum) AS tshares FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 1 AND state = 3 AND sid = 0"
				+ "	GROUP BY uid,fdcode"
				+ ") B ON A.uid = B.uid AND A.fdcode = B.fdcode	WHERE A.tshares < B.tshares-1"
				+ ") AS C" + "	GROUP BY C.fdcode";
		Query query = getSession().createSQLQuery(hql);
		Map<String, Integer> map = new Hashtable<String, Integer>();
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			map.put((String) o[0], ((BigDecimal) o[1]).intValue());
		}
		return map;
	}

	/**
	 * 单只基金，一次或多次申购，未赎回的基金
	 * 
	 * @return
	 */
	@Override
	public List getNoSaleFund() {
		String hql = "SELECT uid,fdcode FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 1 AND state = 3 AND sid = 0"
				+ "	AND (uid,fdcode) not in("
				+ "	SELECT uid,fdcode FROM qj_fund_db.operat_log WHERE operate = 2 AND state = 3 AND sid = 0) "
				+ "	GROUP BY uid,fdcode";
		Query query = getSession().createSQLQuery(hql);
		return query.list();
	}

	/**
	 * 单只基金，一次或多次申购，未赎回基金的持有时间
	 * 
	 * @return
	 */
	@Override
	public Map<String, Integer> getNoSaleFundHoldTime() {
		String hql = "SELECT C.fdcode,sum(holdTime) as holdTime FROM"
				+ "	(SELECT uid,fdcode,DATEDIFF(NOW(),min(opDate)) as holdTime FROM qj_fund_db.operat_log"
				+ "	WHERE operate = 1 AND state = 3 AND sid = 0"
				+ "	AND (uid,fdcode) not in("
				+ "	SELECT uid,fdcode FROM qj_fund_db.operat_log WHERE operate = 2 AND state = 3 AND sid = 0) "
				+ "	GROUP BY uid,fdcode) as C" + "	GROUP BY C.fdcode";
		Query query = getSession().createSQLQuery(hql);
		Map<String, Integer> map = new Hashtable<String, Integer>();
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			map.put((String) o[0], ((BigDecimal) o[1]).intValue());
		}
		return map;
	}

	/**
	 * 单只基金，一次或多次申购，多次赎回
	 * 
	 * @return
	 */
	@Override
	public List getSaleMoreThanOnceFund() {
		String hql = "SELECT fdcode,uid FROM qj_fund_db.operat_log"
				+ "	where operate = 2 AND state = 3 AND sid=0"
				+ "	GROUP BY uid,fdcode" + "	HAVING count(*)>1";
		Query query = getSession().createSQLQuery(hql);
		return query.list();
	}

	/**
	 * 根据基金号和用户编号返回对应类型的基金操作记录
	 * 
	 * @param fdcode
	 *            ，基金号
	 * @param uid
	 *            ，用户编码
	 * @param operate
	 *            ，操作类型
	 * @return
	 */
	@Override
	public List<FundHold> getFundByCodeAndUid(String fdcode, String uid,
			int operate) {
		List<FundHold> l = new LinkedList<FundHold>();
		try {
			String hql = "SELECT opid,uid,fdcode,opDate,sum FROM qj_fund_db.operat_log WHERE operate = "
					+ operate
					+ " AND state = 3 AND sid = 0 AND fdcode='"
					+ fdcode + "' AND uid='" + uid + "' ORDER BY opDate ASC";
			Query query = getSession().createSQLQuery(hql);
			// query.setInteger("operate", operate);
			// query.setString("fdcode", fdcode);
			// query.setString("uid", uid);
			List list = query.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[]) list.get(i);
				FundHold fund = new FundHold();
				fund.setId(new Long(o[0].toString()));
				fund.setUid((String) o[1]);
				fund.setFdcode((String) o[2]);
				// SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd"
				// );
				// Long time=new Long(o[3].toString());
				// String d = format.format(time);
				// Date date=format.parse(d);
				fund.setOpDate((Date) o[3]);
				fund.setShares(((Float) o[4]).doubleValue());
				fund.setOperate(operate);
				l.add(fund);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	/**
	 * 根据基金号和用户编号返回对应类型的早于给定id的基金操作记录
	 * 
	 * @param fdcode
	 * @param uid
	 * @param operate
	 * @param opid
	 * @return
	 */
	@Override
	public Map<Double, Date> getEarlierFundBuyShare(String fdcode, String uid,
			int operate, long startId, long endId) {
		Map<Double, Date> map = new Hashtable<Double, Date>();
		try {
			String hql = "SELECT sum(sum),opDate FROM qj_fund_db.operat_log WHERE operate = "
					+ operate
					+ " AND state = 3 AND sid = 0 AND fdcode='"
					+ fdcode
					+ "' AND uid='"
					+ uid
					+ "' AND opid>"
					+ startId
					+ " AND opid<" + endId;
			Query query = getSession().createSQLQuery(hql);
			// query.setParameter(0, operate);
			// query.setParameter(1, fdcode);
			// query.setParameter(2, uid);
			// query.setParameter(3, startId);
			// query.setParameter(4, endId);
			List list = query.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[]) list.get(i);
				map.put((Double) o[0], (Date) o[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 现金宝，一次或多次申购，一次全部赎回的基金持有时间
	 * 
	 * @return
	 */
	@Override
	public int getSaleAllOnceCashbaoHoldTime() {
		String hql = "select sum(floor((A.opDate-B.opDate)/86400)) from("
				+ " select uid,sum(sum) as shares,opDate from qj_cashbao_db.trade_log"
				+ " where operate = 2 AND state = 3 group by uid having count(uid)=1) as A"
				+ " left outer join"
				+ " (select uid,sum(sum) as shares,min(opDate) AS opDate from qj_cashbao_db.trade_log"
				+ " where operate = 1 AND state = 3 group by uid) as B "
				+ " on A.uid=B.uid where A.shares=B.shares";
		Query query = getSession().createSQLQuery(hql);
		Map<String, Integer> map = new Hashtable<String, Integer>();
		List list = query.list();
		String r = StringUtil.trimNull(list.get(0));
		return Integer.parseInt(r);
	}

	/**
	 * 现金宝，一次或多次申购，只赎回过一次，但是没有完全赎回
	 * 
	 * @return
	 */
	@Override
	public int getSalePartOnceCashbaoHoldTime() {
		String hql = "select sum(datediff(now(),from_unixtime(B.opDate))) from("
				+ " select uid,sum(sum) as shares,opDate from qj_cashbao_db.trade_log"
				+ " where operate = 2 AND state = 3 group by uid having count(uid)=1) as A"
				+ " left outer join"
				+ " (select uid,sum(sum) as shares,min(opDate) AS opDate from qj_cashbao_db.trade_log"
				+ " where operate = 1 AND state = 3 group by uid) as B "
				+ " on A.uid=B.uid where A.shares<B.shares-1";
		Query query = getSession().createSQLQuery(hql);
		Map<String, Integer> map = new Hashtable<String, Integer>();
		List list = query.list();
		String r = StringUtil.trimNull(list.get(0));
		return Integer.parseInt(r);
	}

	/**
	 * 现金宝，一次或多次申购，一次都没有赎回的持有时间
	 * 
	 * @return
	 */
	@Override
	public int getNoSaleCashbaoHoldTime() {
		String hql = "select sum(holdTime) from ("
				+ " SELECT uid,DATEDIFF(NOW(),from_unixtime(opDate)) as holdTime"
				+ " FROM qj_cashbao_db.trade_log WHERE operate = 1 AND state = 3"
				+ " AND uid not in("
				+ " SELECT uid FROM qj_cashbao_db.trade_log WHERE operate = 2 AND state = 3)"
				+ " group by uid) as A";
		Query query = getSession().createSQLQuery(hql);
		Map<String, Integer> map = new Hashtable<String, Integer>();
		List list = query.list();
		String r = StringUtil.trimNull(list.get(0));
		return Integer.parseInt(r);
	}

	/**
	 * 现金宝，一次或多次申购，多次赎回的用户
	 * 
	 * @return
	 */
	@Override
	public List getSaleMoreThanOnceCashbao() {
		String hql = "SELECT uid FROM qj_cashbao_db.trade_log where operate = 2 AND state = 3 GROUP BY uid HAVING count(*)>1";
		Query query = getSession().createSQLQuery(hql);
		return query.list();
	}

	/**
	 * 根据现金宝用户的操作记录
	 * 
	 * @param uid
	 *            ，用户编码
	 * @param operate
	 *            ，操作类型
	 * @return
	 */
	@Override
	public List<FundHold> getCashbaoByUid(String uid, int operate) {
		List<FundHold> l = new LinkedList<FundHold>();
		try {
			String hql = "SELECT id,uid,from_unixtime(opDate) as opDate,sum FROM qj_cashbao_db.trade_log WHERE operate = "
					+ operate
					+ " AND state = 3 AND uid='"
					+ uid
					+ "' ORDER BY opDate ASC";
			Query query = getSession().createSQLQuery(hql);
			// query.setInteger("operate", operate);
			// query.setString("fdcode", fdcode);
			// query.setString("uid", uid);
			List list = query.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[]) list.get(i);
				FundHold fund = new FundHold();
				fund.setId(new Long(o[0].toString()));
				fund.setUid((String) o[1]);
				fund.setOpDate((Date) o[2]);
				fund.setShares(((Float) o[3]).doubleValue());
				fund.setOperate(operate);
				l.add(fund);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}

	/**
	 * 返回对应类型的早于给定id的用户现金宝操作记录
	 * 
	 * @param uid
	 * @param operate
	 * @param opid
	 * @return
	 */
	@Override
	public Map<Double, Date> getEarlierCashbaoBuyShare(String uid, int operate,
			long startId, long endId) {
		Map<Double, Date> map = new Hashtable<Double, Date>();
		try {
			String hql = "SELECT sum(sum),from_unixtime(opDate) as opDate FROM qj_cashbao_db.trade_log WHERE operate = "
					+ operate
					+ " AND state = 3 AND uid='"
					+ uid
					+ "' AND id>"
					+ startId + " AND id<" + endId;
			Query query = getSession().createSQLQuery(hql);
			// query.setParameter(0, operate);
			// query.setParameter(1, fdcode);
			// query.setParameter(2, uid);
			// query.setParameter(3, startId);
			// query.setParameter(4, endId);
			List list = query.list();
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[]) list.get(i);
				map.put((Double) o[0], (Date) o[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 返回所有可售基金列表，持有时间默认为0
	 * 
	 * @return
	 */
	private List getAllFund() {
		String hql = "select DISTINCT fdcode from qj_fund_db.fund_info where purchaseStatus!=3";
		return getSession().createSQLQuery(hql).list();
	}

	/**
	 * 保存基金的持有时间
	 */
	public void saveFundHoldTime() {

		// 计算单只基金赎回了多次的用户持有时间
		List saleMoreThanOnceList = getSaleMoreThanOnceFund(); // 基金——人
		Map<String, Integer> saleMoreThanOnceMap = new Hashtable<String, Integer>();
		for (int i = 0; i < saleMoreThanOnceList.size(); i++) {
			Integer totalHoldTime = 0;
			long start = 0;
			Object[] o = (Object[]) saleMoreThanOnceList.get(i);
			String fdcode = (String) o[0];
			// 赎回记录
			List<FundHold> sales = getFundByCodeAndUid(fdcode, (String) o[1], 2);
			for (int j = 0; j < sales.size(); j++) {
				FundHold fund = sales.get(j);
				// 当前赎回之前买过多少份额
				Map<Double, Date> earlierBuyMap = getEarlierFundBuyShare(
						fund.getFdcode(), fund.getUid(), 1, start, fund.getId());
				Set<Entry<Double, Date>> earlierBuySet = earlierBuyMap
						.entrySet();
				Iterator<Entry<Double, Date>> earlierBuySetIterator = earlierBuySet
						.iterator();
				if (earlierBuySetIterator.hasNext()) {
					Map.Entry<Double, Date> it = (Map.Entry<Double, Date>) earlierBuySetIterator
							.next();
					// 累计赎回和累计购买比较
					// if(fund.getShares()==it.getKey()){
					if ((it.getKey() - fund.getShares()) < 1) {
						totalHoldTime += daysBetween(it.getValue(),
								fund.getOpDate());
						start = fund.getId();
						break;
					}
					// 如果到当前还有未赎回的份额
					if (j == sales.size() - 1
							&& fund.getShares() < it.getKey() - 1) {
						totalHoldTime += daysBetween(it.getValue(), new Date());
					}
				}
			}
			Set<String> keySet = saleMoreThanOnceMap.keySet();
			boolean isExist = false;
			for (String key : keySet) {
				if (key.equals(fdcode)) {
					Integer value = saleMoreThanOnceMap.get(key);
					value += totalHoldTime;
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				saleMoreThanOnceMap.put(fdcode, totalHoldTime);
			}
		}

		// 计算现金宝赎回多次的用户持有时间
		long cashBaoHoldtime = getSaleAllOnceCashbaoHoldTime()
				+ getSalePartOnceCashbaoHoldTime() + getNoSaleCashbaoHoldTime();
		List moreThanOnceCashbaoList = getSaleMoreThanOnceCashbao();
		for (int i = 0; i < moreThanOnceCashbaoList.size(); i++) {
			String uid = (String) moreThanOnceCashbaoList.get(i);
			List<FundHold> saleRecord = getCashbaoByUid((String) uid, 2);
			long start = 0;
			for (int j = 0; j < saleRecord.size(); j++) {
				FundHold cashbao = saleRecord.get(j);
				Map<Double, Date> earlierBuyMap = getEarlierCashbaoBuyShare(
						cashbao.getUid(), 1, start, cashbao.getId());
				Set<Entry<Double, Date>> earlierBuySet = earlierBuyMap
						.entrySet();
				Iterator<Entry<Double, Date>> earlierBuySetIterator = earlierBuySet
						.iterator();
				if (earlierBuySetIterator.hasNext()) {
					Map.Entry<Double, Date> it = (Map.Entry<Double, Date>) earlierBuySetIterator
							.next();
					// 累计赎回和累计购买比较
					// if(fund.getShares()==it.getKey()){
					if ((it.getKey() - cashbao.getShares()) < 1) {
						cashBaoHoldtime += daysBetween(it.getValue(),
								cashbao.getOpDate());
						start = cashbao.getId();
						break;
					}
					// 如果到当前还有未赎回的份额
					if (j == saleRecord.size() - 1
							&& cashbao.getShares() < it.getKey() - 1) {
						cashBaoHoldtime += daysBetween(it.getValue(),
								new Date());
					}
				}
			}
		}

		// 一次全部赎回
		Map<String, Integer> saleAllOnceMap = getSaleAllOnceFundHoldTime();
		// 只赎回了一次，但是没有全部赎回
		Map<String, Integer> salePartOnceMap = getSalePartOnceFundHoldTime();
		// 一次都没有赎回
		Map<String, Integer> noSaleMap = getNoSaleFundHoldTime();

		// 所有基金
		List allFund = getAllFund();
		System.out.println(allFund.size());
		// Set<Entry<String, Integer>> set=saleAllOnceMap.entrySet();
		// Iterator<Entry<String, Integer>> iterator=set.iterator();
		// Set<Entry<String, Integer>> set=allFund.entrySet();
		// Iterator<Entry<String, Integer>> iterator=set.iterator();
		StringBuffer sql = new StringBuffer();
		sql.append("delete from qj_qjs_db.fund_hold;");
		sql.append("insert into qj_qjs_db.fund_hold(fdcode,holdtime,sid) values('000000',");
		sql.append(cashBaoHoldtime);
		sql.append(",-1);");

		for (int i = 0; i < allFund.size(); i++) {
			// while(iterator.hasNext()){
			String fdCode = (String) allFund.get(i);
			// Map.Entry<String, Integer> it=(Map.Entry<String,
			// Integer>)iterator.next();
			Integer holdTime = 0;

			Set<String> keySet = saleAllOnceMap.keySet();
			for (String key : keySet) {
				Integer tmp = saleAllOnceMap.get(key);
				if (key.equals(fdCode)) {
					holdTime += tmp;
					break;
				}
			}
			keySet = salePartOnceMap.keySet();
			for (String key : keySet) {
				Integer tmp = salePartOnceMap.get(key);
				if (key.equals(fdCode)) {
					holdTime += tmp;
					break;
				}
			}
			keySet = noSaleMap.keySet();
			for (String key : keySet) {
				Integer tmp = noSaleMap.get(key);
				if (key.equals(fdCode)) {
					holdTime += tmp;
					break;
				}
			}
			keySet = saleMoreThanOnceMap.keySet();
			for (String key : keySet) {
				Integer tmp = saleMoreThanOnceMap.get(key);
				if (key.equals(fdCode)) {
					// it.setValue(it.getValue()+tmp);
					holdTime += tmp;
					break;
				}
			}
			sql.append("insert into qj_qjs_db.fund_hold(fdcode,holdtime,sid) values");
			sql.append("('" + fdCode + "'," + holdTime + ",0);");
		}

		Query query = this.getSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获取所有基金的持有时间
	 * 
	 * @return
	 */
	@Override
	public List<FundHold> getAllFundHoldTime(FundHold fundHold) {
		String hql;
		// 查询的是现金宝
		if (fundHold != null
				&& "99".equals(StringUtil.trimNull(fundHold.getType()))) {
			hql = "SELECT fdcode,'现金宝' as `name`,'现金宝' as abbrev,99 as type,holdtime from qj_qjs_db.fund_hold where fdcode='000000'";
		} else if (fundHold != null
				&& "0".equals(StringUtil.trimNull(fundHold.getType()))) { // 查询全部
			hql = "SELECT fdcode,'现金宝' as `name`,'现金宝' as abbrev,99 as type,holdtime from qj_qjs_db.fund_hold where fdcode='000000'";

			if (!"".equals(StringUtil.trimNull(fundHold.getName()))) {
				hql += " AND ('现金宝' like '%" + fundHold.getName() + "%')";
			}
			if (!"".equals(StringUtil.trimNull(fundHold.getFdcode()))) {
				hql += " AND fdcode like '%" + fundHold.getFdcode() + "%'";
			}
			hql += " UNION"
					+ " SELECT f.fdcode,f.`name`,f.abbrev,f.type,h.holdtime from qj_fund_db.fund_info f"
					+ "	LEFT OUTER JOIN qj_qjs_db.fund_hold h"
					+ "	ON f.fdcode=h.fdcode AND h.sid=0 where purchaseStatus<>3 AND f.type is not NULL ";
			hql += createWhereHql(fundHold);
		} else {
			hql = "SELECT f.fdcode,f.`name`,f.abbrev,f.type,h.holdtime from qj_fund_db.fund_info f"
					+ "	LEFT OUTER JOIN qj_qjs_db.fund_hold h"
					+ "	ON f.fdcode=h.fdcode AND h.sid=0 where purchaseStatus<>3 AND f.type is not NULL ";
			if (fundHold != null) {
				hql += createWhereHql(fundHold);
			}
		}

		Query query = getSession().createSQLQuery(hql);
		List list = query.list();
		List<FundHold> l = new LinkedList<FundHold>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o = (Object[]) list.get(i);
			FundHold fund = new FundHold();
			fund.setId((long) i);
			fund.setFdcode((String) o[0]);
			fund.setName((String) o[1]);
			fund.setAbbrev((String) o[2]);
			fund.setType(new Integer(o[3].toString()));
			if (o[4] != null) {
				fund.setHoldTime((Integer) o[4]);
			} else {
				fund.setHoldTime(0);
			}
			fund.setSid(0);
			l.add(fund);
		}
		return l;
	}

	/**
	 * 获取所有基金数量
	 * 
	 * @return
	 */
	public int getAllFundCount(FundHold model) {
		String hql;
		if (model != null && "99".equals(StringUtil.trimNull(model.getType()))) {
			return 1;
		} else if (model != null
				&& "0".equals(StringUtil.trimNull(model.getType()))) { // 查询全部
			hql = "SELECT count(*)+1 from qj_fund_db.fund_info f where purchaseStatus<>3 AND f.type is not NULL";
		} else {
			hql = "SELECT count(*) from qj_fund_db.fund_info f where purchaseStatus<>3 AND f.type is not NULL ";
		}
		if (model != null) {
			if (!"".equals(StringUtil.trimNull(model.getType()))
					&& model.getType() != 0) {
				hql += " and f.type=" + model.getType();
			}

			if (!"".equals(StringUtil.trimNull(model.getName()))) {
				hql += " and (f.name like '%" + model.getName()
						+ "%' or f.abbrev like '%" + model.getName() + "%')";
			}
			if (!"".equals(StringUtil.trimNull(model.getFdcode()))) {
				hql += " and f.fdcode like '%" + model.getFdcode() + "%'";
			}
		}
		Query query = getSession().createSQLQuery(hql);
		String r = StringUtil.trimNull(query.list().get(0));
		return Integer.parseInt(r);
	}

	/**
	 * 生成sql语句的where部分
	 * 
	 * @param model
	 * @return
	 */
	public String createWhereHql(FundHold model) {
		String hql = "";
		String key = model.getSortedConditions().keySet().iterator().next();
		String value = model.getSortedConditions().values().iterator().next();

		if (!"".equals(StringUtil.trimNull(model.getType()))
				&& model.getType() != 0) {
			hql += " and f.type=" + model.getType();
		}

		if (!"".equals(StringUtil.trimNull(model.getName()))) {
			hql += " and (f.name like '%" + model.getName()
					+ "%' or f.abbrev like '%" + model.getName() + "%')";
		}
		if (!"".equals(StringUtil.trimNull(model.getFdcode()))) {
			hql += " and f.fdcode like '%" + model.getFdcode() + "%'";
		}
		hql += " order by " + key + " " + value + " limit "
				+ model.getFirstResult() + "," + model.getMaxResults();
		return hql;
	}

	/*
	 * (非 Javadoc) <p>Title: statistic_month_limit</p> <p>Description: </p>
	 * 
	 * @see com.forestry.dao.sys.FundDao#statistic_month_limit()
	 */
	@Override
	public void statistic_month_limit() {
		// TODO Auto-generated method stub

		List<String> listMonth = DateUtil.getMonthWithoutdd(DateUtil.currentMonthMinusWithoutdd(1), DateUtil.currentMonthMinusWithoutdd(0));
		for (int i = 0; i < listMonth.size(); i++) {
			String date = listMonth.get(i);
			List<String> insertList = new ArrayList<String>();

			String strsql = "select count(*) as num from (" + " select uid, sum ,abbrev as fund,opDate from qj_fund_db.operat_log ol1 " + " where (ol1.operate=1 or ol1.operate=4) " + " and (ol1.state=1 or ol1.state=3) " + " and ol1.sid=0 " + " union all "
					+ " select ol2.uid,sum(sum)  as sum,fs.`name` as fund,ol2.opDate  from qj_fund_db.operat_log ol2 " + " left join qj_fund_db.finance_schema fs on(fs.sid = ol2.sid) " + " where  ol2.sid<>0 " + " and (ol2.operate=1 or ol2.operate=4) and (ol2.state=1 or ol2.state=3) " + " group by uid,opDate "
					+ " union all " + " select  tl.uid,tl.sum as sum ,'活期宝' as fund ,FROM_UNIXTIME(opDate) as  opdate   From  qj_cashbao_db.trade_log tl " + " where (tl.operate=1 or tl.operate=4) " + " and   (tl.state=2 or tl.state=3) " + " ) t " + "where t.opdate between '" + date + "-01 00:00:00' and '" + date
					+ "-31 23:59:59'" + " and t.sum ";

			for (int j = 0; j < paraList.size(); j++) {
				StringBuffer sql = new StringBuffer(strsql);
				if (j == 0) {
					sql.append("<").append(paraList.get(j));

				} else if (j == paraList.size() - 1) {
					sql.append(">=").append(paraList.get(j));
				} else {
					sql.append(">=").append(paraList.get(j - 1)).append(" and sum <").append(paraList.get(j));
				}

				Query query = getSession().createSQLQuery(sql.toString());
				List list = query.list();
				String sum = StringUtil.trimNull(list.get(0));
				insertList.add(sum);
			}
			
			strsql = "replace into qj_qjs_db.month_limit(date,limit10,limit100,limit1000,limit10000,limit100000) values('" +date +"'";
		    for(int j=0;j<insertList.size();j++){
		    		strsql+=",'" +insertList.get(j) +"'";
		    }
		    strsql+=")";
		    getSession().createSQLQuery(strsql).executeUpdate();
		}
		
		
	}

	/* (非 Javadoc) 
	* <p>Title: getFundlimtLineGraph</p> 
	* <p>Description: </p> 
	* @param fundLimit
	* @return 
	* @see com.forestry.dao.sys.FundDao#getFundlimtLineGraph(com.forestry.model.sys.FundLimit) 
	*/
	@Override
	public List<FundLimit> getFundlimtLineGraph(FundLimit fundLimit) {
		// TODO Auto-generated method stub
		String sql = "select date,limit10,limit100,limit1000,limit10000,limit100000 from qj_qjs_db.month_limit order by date desc limit 6";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<FundLimit> listFund = new ArrayList<FundLimit>();

		for (int i = list.size()-1; i >=0; i--) {
			FundLimit f = new FundLimit();
			Object[] o = (Object[]) list.get(i);
			f.setDate((String) o[0]);
			f.setLimit10((String) o[1]);
			f.setLimit100((String) o[2]);
			f.setLimit1000(StringUtil.trimNull(o[3]));
			f.setLimit10000(StringUtil.trimNull(o[4]));
			f.setLimit100000(StringUtil.trimNull(o[5]));
			listFund.add(f);
		}
		return listFund;
	}
}
