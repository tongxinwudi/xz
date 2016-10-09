package com.forestry.dao.sys.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.forestry.controller.sys.OperateController;
import com.forestry.dao.sys.ForestryDao;
import com.forestry.dao.sys.MemberDao;
import com.forestry.dao.sys.OperateDao;
import com.forestry.dao.sys.ProfitDao;
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
import com.forestry.model.sys.param.TradeUserParameter;

 
import core.dao.BaseDao;
import core.util.Arith;
import core.util.DBUtil;
import core.util.DBUtil_119;
import core.util.DateUtil;
import core.util.StringUtil;

/**
 * 
 *  Class Name: MemberDaoImpl.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年9月11日 下午2:30:03    
 *  @version 1.0
 */
@Repository
public class MemberDaoImpl extends BaseDao<Member_week_ration>implements MemberDao {

	public MemberDaoImpl() {
		super(Member_week_ration.class);
	}

	private static final Logger log = Logger.getLogger(MemberDaoImpl.class);

 

	@Override
	public void statistic_week_ration() {
		// TODO Auto-generated method stub
		try {
			// List<Operate> listOperates = caculate("2015-01-01",
			// "2015-08-13");
			List<Member_week_ration> listOperates = caculate(DateUtil.currentTimeMinus(7), DateUtil.currentTimeMinus(1));
			saveDay_investToDb(listOperates);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	/**
	 * 
	 *  Function:入库操作
	 *  @author TongXin  DateTime 2015年9月11日 下午3:25:15
	 *  @param list
	 */
	public void saveDay_investToDb(List<Member_week_ration> list) {
		try {
			if (list == null) {
				return;
			}
			int m = 500;// 批量处理条数
			StringBuffer sql = new StringBuffer();
			long c1 = Calendar.getInstance().getTimeInMillis();
			sql.append(" replace into qj_qjs_db.week_member_ration(sdate,edate,num_common,sum_common,num_vip,sum_vip,ration_vip) values");
			for (int i = 0; i < list.size(); i++) {
				Member_week_ration op = list.get(i);
				sql.append("('" + op.getSdate() + "','");
				sql.append(op.getEdate() + "','");
				sql.append(op.getNum_common() + "','");
				sql.append(op.getSum_common() + "','");
				sql.append(op.getNum_vip() + "','");
				sql.append(op.getSum_vip() + "','");
				sql.append(op.getRation_vip() );
				if (i % m == 0 || i == (list.size() - 1)) {
					sql.append("');");
					DBUtil.dbInsert(sql.toString());
					sql.setLength(0);
					sql.append(" replace into qj_qjs_db.week_member_ration(sdate,edate,num_common,sum_common,num_vip,sum_vip,ration_vip) values");
				} else {
					sql.append("'),");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

    /**
     * 
     *  Function:
     *  @author TongXin  DateTime 2015年9月11日 下午3:25:48
     *  @param sdate
     *  @param edate
     *  @return
     *  @throws IOException
     */
	private List<Member_week_ration> caculate(String sdate, String edate) throws IOException {

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Member_week_ration> returnList = new ArrayList<Member_week_ration>();
		try {
			conn = DBUtil.getConnection();
			 NumberFormat nt = NumberFormat.getPercentInstance();
			  nt.setMinimumFractionDigits(2);
				Member_week_ration mwr = new Member_week_ration();
				mwr.setSdate(sdate);
				mwr.setEdate(edate);
				mwr.setNum_common(String.valueOf(getNum_common(pstm, rs, conn, edate)));//普通会员数目
				mwr.setSum_common(String.valueOf(getSum_common(pstm, rs, conn, edate)));//普通会员申购总金额
				mwr.setNum_vip(String.valueOf(getNum_vip(pstm, rs, conn, edate)));//vip会员数量
				mwr.setSum_vip(String.valueOf(getSum_vip(pstm, rs, conn, edate)));//vip申购总金额
				int lastWeekVip = getNum_vip(pstm, rs, conn, DateUtil.currentTimeMinus(8) );
				if(lastWeekVip==0){
					mwr.setRation_vip("100%");
				}else{
					 float num= (float)(Integer.parseInt(mwr.getNum_vip())  - lastWeekVip)/lastWeekVip;   
					 DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
					 String s = df.format(num)+"%";//返回的是String类型
					mwr.setRation_vip(s) ;
				}
				returnList.add(mwr);
			 

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pstm, rs);
		}

		return returnList;
	}

	/**
	 * 
	 *  Function:获取普通会员的数量
	 *  @author TongXin  DateTime 2015年9月11日 下午3:28:59
	 *  @param pstm
	 *  @param rs
	 *  @param conn
	 *  @param date
	 *  @return
	 *  @throws SQLException
	 */
	public int getNum_common(PreparedStatement pstm, ResultSet rs, Connection conn,String date ) throws SQLException {
		int total = 0;

		String sql = "select count(1) as countNum  from qj_user_db.`user` where state=1 and uid not in(select uid from qj_member_db.s_member)";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total = Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}
		return total;
	}

 

	/**
	 * 
	 * Function:普通用户申购量
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午4:11:33
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public String getSum_common(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		double total = 0;

		String sql = "select ifnull(sum(sum),0)  sum from qj_fund_db.operat_log where operate=1 and state=3  and uid not in(select uid from qj_member_db.s_member) ";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}

		sql = "select  ifnull(sum(sum),0) sum from qj_cashbao_db.trade_log where (operate=1 or operate=4)  and state=3  and uid not in(select uid from qj_member_db.s_member)";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}
		return   new BigDecimal( Arith.round(total, 2)).setScale(2,  BigDecimal.ROUND_HALF_UP).toPlainString();
		//return new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		 
	}

	/**
	 * 
	 *  Function:vip用户数量
	 *  @author TongXin  DateTime 2015年9月11日 下午4:09:23
	 *  @param pstm
	 *  @param rs
	 *  @param conn
	 *  @param date
	 *  @return
	 *  @throws SQLException
	 */
	public int getNum_vip(PreparedStatement pstm, ResultSet rs, Connection conn,String date ) throws SQLException {
		int total = 0;

		String sql = "select count(1) as countNum  from   qj_member_db.s_member where c_time <=  '" + date +" 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total = Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}
		return total;
	}
	
	/**
	 * 
	 *  Function:vip用户申购总额
	 *  @author TongXin  DateTime 2015年9月11日 下午4:11:50
	 *  @param pstm
	 *  @param rs
	 *  @param conn
	 *  @param date
	 *  @return
	 *  @throws SQLException
	 */
	public String getSum_vip(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		double total = 0;

		String sql = "select ifnull(sum(sum),0)  sum from qj_fund_db.operat_log where (operate=1 or operate=4) and state=3  and uid   in(select uid from qj_member_db.s_member) ";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}

		sql = "select  ifnull(sum(sum),0) sum from qj_cashbao_db.trade_log where (operate=1 or operate=4)  and state=3  and uid   in(select uid from qj_member_db.s_member)";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}
		return new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	@Override
	public List<Member_week_ration> getMemberRationList(Member_week_ration member_week_ration) {
		// TODO Auto-generated method stub

		String sql = "select sdate,edate,num_common,sum_common,num_vip,sum_vip,ration_vip  from qj_qjs_db.week_member_ration  where 1=1 ";

		if (!"".equals(StringUtil.trimNull(member_week_ration.getSdate()))) {
			sql += " and sdate >='" + member_week_ration.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(member_week_ration.getEdate()))) {
			sql += " and edate<='" + member_week_ration.getEdate() + "'";
		}

		String key = member_week_ration.getSortedConditions().keySet().iterator().next();
		String value = member_week_ration.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + member_week_ration.getFirstResult() + "," + member_week_ration.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Member_week_ration> listOperates = new ArrayList<Member_week_ration>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Member_week_ration op = new Member_week_ration();
			Object[] o = (Object[]) list.get(i);
			op.setSdate(StringUtil.trimNull(o[0]));
			op.setEdate(StringUtil.trimNull(o[1]));
			op.setNum_common(StringUtil.trimNull(o[2]));
			op.setSum_common(StringUtil.trimNull(o[3]));
			op.setNum_vip(StringUtil.trimNull(o[4]));
			op.setSum_vip(StringUtil.trimNull(o[5]));
			op.setRation_vip(StringUtil.trimNull(o[6]));
			listOperates.add(op);
		}
		return listOperates;
	} 
	
	@Override
	public long getMemberRationListCount(Member_week_ration member_week_ration) {
		// TODO Auto-generated method stub
	 
		String sql = "select count(1) from (select sdate,edate,num_common,sum_common,num_vip,sum_vip,ration_vip  from qj_qjs_db.week_member_ration  where 1=1 ";

		if (!"".equals(StringUtil.trimNull(member_week_ration.getSdate()))) {
			sql += " and sdate >='" + member_week_ration.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(member_week_ration.getEdate()))) {
			sql += " and edate<='" + member_week_ration.getEdate() + "'";
		}


		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	/**
	 * 会员升级
	 */
	@Override
	public void statistic_day_becomeVip() {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Member_week_ration> returnList = new ArrayList<Member_week_ration>();
		try {
			conn = DBUtil_119.getConnection();
			String sql = "insert into qj_member_db.s_member(uid,mobile,type,mark,l_id,c_time,u_time) select s.uid,u.mobile ,1,'累计申购额达到10W','vip1','" + DateUtil.currentTime() + "','" + DateUtil.currentTime() + "' from " + " (select uid,sum(sum) as sum from "
					+ " (select uid ,ifnull(sum(sum),0) as sum from qj_fund_db.operat_log where (operate=1 or operate=4) and state=3 group by uid " + "  union  all select uid ,ifnull(sum(sum),0) as sum from qj_cashbao_db.trade_log where (operate=1 or operate=4) and state=3 group by uid "
					+ "  )t group by uid having sum >=100000 " + " )s  left join qj_user_db.`user` u on (s.uid =u.uid) where s.uid not in (select uid from qj_member_db.s_member)";
			pstm = conn.prepareStatement(sql);
			pstm.execute();
			sql= "  insert into qj_member_db.s_member_log(uid,type,mark,c_time) select s.uid, 1,'累计申购额达到10W成为会员','" +DateUtil.currentTime() + "' from " 
					+" (select uid,sum(sum) as sum from " 
					+" (select uid ,ifnull(sum(sum),0) as sum from qj_fund_db.operat_log where (operate=1 or operate=4) and state=3 group by uid " 
					+"  union  all select uid ,ifnull(sum(sum),0) as sum from qj_cashbao_db.trade_log where (operate=1 or operate=4) and state=3 group by uid " 
					+"  )t group by uid having sum >=100000 " 
					+" )s  left join qj_user_db.`user` u on (s.uid =u.uid) where s.uid not in (select uid from qj_member_db.s_member_log where type=1)" ;
		    pstm= conn.prepareStatement(sql);
		    pstm.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pstm, rs);
		}
	}

	public static void main(String[] args){
		 NumberFormat nt = NumberFormat.getPercentInstance();
		 nt.setMinimumFractionDigits(2);
		 System.out.println(nt.format(2/217)) ;
		 
		 
		 float num= (float)2/217;   
		 DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
		 String s = df.format(num);//返回的是String类型
		 System.out.println(s);
	}
 
}
