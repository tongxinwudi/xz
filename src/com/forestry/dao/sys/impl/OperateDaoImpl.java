package com.forestry.dao.sys.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.forestry.controller.sys.OperateController;
import com.forestry.dao.sys.ForestryDao;
import com.forestry.dao.sys.OperateDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.AppLog;
import com.forestry.model.sys.Channel;
import com.forestry.model.sys.Channel2;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.Fail;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Inventory;
import com.forestry.model.sys.Money;
import com.forestry.model.sys.NewOld;
import com.forestry.model.sys.Operate;
import com.forestry.model.sys.Profit;
import com.forestry.model.sys.Settle;
import com.forestry.model.sys.TradeUser;
import com.forestry.model.sys.param.TradeUserParameter;

import core.dao.BaseDao;
import core.util.DBUtil;
import core.util.DateUtil;
import core.util.StringUtil;

/**
 * 
 * Class Name: OperateDaoImpl.java Function:运营dao Modifications:
 * 
 * @author TongXin DateTime 2015年8月12日 下午2:47:00
 * @version 1.0
 */
@Repository
public class OperateDaoImpl extends BaseDao<Operate>implements OperateDao {

	public OperateDaoImpl() {
		super(Operate.class);
	}

	private static final Logger log = Logger.getLogger(OperateDaoImpl.class);

	static List<String> listExcluded = new ArrayList<String>();

	static {
		listExcluded.add("0020141009019933");// 刘安琪
		listExcluded.add("0020141204044727");// 车倩倩
		listExcluded.add("0020141225104038");// 方衍
		listExcluded.add("0020140421000094");// 李超
	}

	/**
	 * 获取投资列表
	 */
	@Override
	public List<Operate> getOperateList(Operate operate) {
		// TODO Auto-generated method stub

		String sql = "select date,investTotalPersonNum,investTotalNum,investTotalMoney,investFailPersonNum,investFailNum,investFailMoney,investSuccessPersonNum,investSuccessNum,investSuccessMoney,investPartSuccessNum,investPartSuccessMoney from qj_qjs_db.day_invest where 1=1 ";
		if (!"".equals(StringUtil.trimNull(operate.getSdate()))) {
			sql += " and date >='" + operate.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(operate.getEdate()))) {
			sql += " and date <='" + operate.getEdate() + "'";
		}

		String key = operate.getSortedConditions().keySet().iterator().next();
		String value = operate.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + operate.getFirstResult() + "," + operate.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Operate> listOperates = new ArrayList<Operate>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Operate op = new Operate();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setInvestTotalPersonNum(StringUtil.trimNull(o[1]));
			op.setInvestTotalNum(StringUtil.trimNull(o[2]));
			op.setInvestTotalMoney(StringUtil.trimNull(o[3]));
			op.setInvestFailPersonNum(StringUtil.trimNull(o[4]));
			op.setInvestFailNum(StringUtil.trimNull(o[5]));
			op.setInvestFailMoney(StringUtil.trimNull(o[6]));
			op.setInvestSuccessPersonNum(StringUtil.trimNull(o[7]));
			op.setInvestSuccessNum(StringUtil.trimNull(o[8]));
			op.setInvestSuccessMoney(StringUtil.trimNull(o[9]));
			op.setInvestPartSuccessNum(StringUtil.trimNull(o[10]));
			op.setInvestPartSuccessMoney(StringUtil.trimNull(o[11]));
			listOperates.add(op);
		}
		return listOperates;
	}

	@Override
	public long getOperateListCount(Operate operate) {
		// TODO Auto-generated method stub

		String sql = " select count(1) from ( select date,investTotalPersonNum,investTotalNum,investTotalMoney,investFailPersonNum,investFailNum,investFailMoney,investSuccessNum,investSuccessMoney,investPartSuccessNum,investPartSuccessMoney from qj_qjs_db.day_invest where 1=1 ";
		if (!"".equals(StringUtil.trimNull(operate.getSdate()))) {
			sql += " and date >='" + operate.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(operate.getEdate()))) {
			sql += " and date <='" + operate.getEdate() + "'";
		}

		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	@Override
	public void statistic_day_invest() {
		// TODO Auto-generated method stub
		try {
			// List<Operate> listOperates = caculate("2015-01-01",
			// "2015-08-13");
			List<Operate> listOperates = caculate(DateUtil.currentTimeMinus(1), DateUtil.currentTime());
			saveDay_investToDb(listOperates);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	/**
	 * 
	 * Function:将数据存入数据库
	 * 
	 * @author TongXin DateTime 2015年8月13日 下午3:26:08
	 * @param list
	 */
	public void saveDay_investToDb(List<Operate> list) {
		try {
			if (list == null) {
				return;
			}
			int m = 500;// 批量处理条数
			StringBuffer sql = new StringBuffer();
			long c1 = Calendar.getInstance().getTimeInMillis();
			sql.append(" replace into qj_qjs_db.day_invest(date,investTotalPersonNum,investTotalNum,investTotalMoney,investFailPersonNum,investFailNum,investFailMoney,investSuccessPersonNum,investSuccessNum,investSuccessMoney,investPartSuccessNum,investPartSuccessMoney) values");
			for (int i = 0; i < list.size(); i++) {
				Operate op = list.get(i);
				sql.append("('" + op.getDate() + "','");
				sql.append(op.getInvestTotalPersonNum() + "','");
				sql.append(op.getInvestTotalNum() + "','");
				sql.append(op.getInvestTotalMoney() + "','");
				sql.append(op.getInvestFailPersonNum() + "','");
				sql.append(op.getInvestFailNum() + "','");
				sql.append(op.getInvestFailMoney() + "','");
				sql.append(op.getInvestSuccessPersonNum() + "','");
				sql.append(op.getInvestSuccessNum() + "','");
				sql.append(op.getInvestSuccessMoney() + "','");
				sql.append(op.getInvestPartSuccessNum() + "','");
				sql.append(op.getInvestPartSuccessMoney());
				if (i % m == 0 || i == (list.size() - 1)) {
					sql.append("');");
					DBUtil.dbInsert(sql.toString());
					sql.setLength(0);
					sql.append(" replace into qj_qjs_db.day_invest(date,investTotalPersonNum,investTotalNum,investTotalMoney,investFailPersonNum,investFailNum,investFailMoney,investSuccessPersonNum,investSuccessNum,investSuccessMoney,investPartSuccessNum,investPartSuccessMoney) values");
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
	 * Function:计算投资额
	 * 
	 * @author TongXin DateTime 2015年8月13日 下午3:16:51
	 * @param sdate
	 * @param edate
	 * @return
	 * @throws IOException
	 */
	private List<Operate> caculate(String sdate, String edate) throws IOException {

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Operate> listInvestData = new ArrayList<Operate>();
		try {
			conn = DBUtil.getConnection();
			List<String> dateList = DateUtil.getMonth(sdate, edate);
			for (int i = 0; i < dateList.size(); i++) {
				Operate id = new Operate();
				String currentDate = dateList.get(i);
				System.out.println(currentDate);
				id.setDate(currentDate);// 时间
				id.setInvestTotalPersonNum(String.valueOf(getInvestTotalPersonNum(pstm, rs, conn, currentDate)));// 总申购人数
				id.setInvestTotalNum(String.valueOf(getInvestTotalNum(pstm, rs, conn, currentDate)));// 总申购笔数
				id.setInvestTotalMoney(String.valueOf(getInvestTotalMoney(pstm, rs, conn, currentDate)));// 总申购金额
				id.setInvestFailPersonNum(String.valueOf(getInvestFailPersonNum(pstm, rs, conn, currentDate)));// 申购失败人数
				id.setInvestFailNum(String.valueOf(getInvestFailNum(pstm, rs, conn, currentDate)));// 申购失败笔数
				id.setInvestFailMoney(String.valueOf(getInvestFailMoney(pstm, rs, conn, currentDate)));// 申购失败金额
				id.setInvestSuccessPersonNum(String.valueOf(getInvestSuccessPersonNum(pstm, rs, conn, currentDate)));// 申购成功人数
				id.setInvestSuccessNum(String.valueOf(getInvestSucessNum(pstm, rs, conn, currentDate)));// 申购成功笔数
				id.setInvestSuccessMoney(String.valueOf(getInvestSuccessMoney(pstm, rs, conn, currentDate)));// 申购成功金额
				id.setInvestPartSuccessNum(String.valueOf(getInvestPartNum(pstm, rs, conn, currentDate)));// 部分申购成功笔数
				id.setInvestPartSuccessMoney(String.valueOf(getInvestPartMoney(pstm, rs, conn, currentDate)));// 部分申购金额
				listInvestData.add(id);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pstm, rs);
		}

		return listInvestData;
	}

	/**
	 * 
	 * Function: 获取总申购人数
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午3:40:43
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public int getInvestTotalPersonNum(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		int total = 0;

		String sql = "select count(1) countNum from ( " + "select distinct uid from ( " + "select uid from qj_fund_db.operat_log where (operate=1 or operate=4) and  opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'" + "union all"
				+ " select uid from qj_cashbao_db.trade_log where (operate=1 or operate=4) and  FROM_UNIXTIME(opDate)  BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'" + ")t)a";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total = Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}
		return total;
	}

	/**
	 * 
	 * Function:申购笔数
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午3:53:59
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int getInvestTotalNum(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		int total = 0;

		// 非组合
		String sql = "select count(1) countNum from qj_fund_db.operat_log where (operate=1 or operate=4)  and sid=0 and opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}

		// 组合
		sql = "select count(1) countNum from( select uid, sid,opDate from qj_fund_db.operat_log where (operate=1 or operate=4)  and sid<>0 and opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59' group by uid,sid,opDate)t";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}

		// 活期宝
		sql = "select count(1) countNum from qj_cashbao_db.trade_log where (operate=1 or operate=4)    and FROM_UNIXTIME(opDate) BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}

		return total;
	}

	/**
	 * 
	 * Function:申购总额
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午4:11:33
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public double getInvestTotalMoney(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		double total = 0;

		String sql = "select ifnull(sum(sum),0)  sum from qj_fund_db.operat_log where (operate=1 or operate=4)   and opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59';";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}

		sql = "select  ifnull(sum(sum),0) sum from qj_cashbao_db.trade_log where (operate=1 or operate=4)   and FROM_UNIXTIME(opDate) BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}
		return new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * Function:申购失败笔数
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午4:19:24
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int getInvestFailPersonNum(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		int total = 0;

		String sql = "select count(1) countNum from ( " + "select distinct uid from ( " + "select uid from qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and  opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'" + "union all"
				+ " select uid from qj_cashbao_db.trade_log where (operate=1 or operate=4) and state=4 and  FROM_UNIXTIME(opDate)  BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'" + ")t)a";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total = Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}
		return total;
	}

	/**
	 * 
	 * Function:申购失败笔数
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午4:24:58
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int getInvestFailNum(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		int total = 0;

		// 非组合
		String sql = "select count(1) countNum from qj_fund_db.operat_log where (operate=1 or operate=4)  and state=4   and sid=0 and opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}

		// 组合
		// sql = "select count(1) countNum from (select uid, sid,opDate from
		// qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and sid<>0 and
		// opDate BETWEEN '"+date+" 00:00:00' and '"+date+" 23:59:59' group by
		// uid,sid,opDate)t";
		sql = "select  uid, sid,opDate ,count(state) stateNum  from qj_fund_db.operat_log where (operate=1 or operate=4)    and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'   group by uid, sid,opDate";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		PreparedStatement pstm1 = null;
		ResultSet rs1 = null;
		while (rs.next()) {
			// total +=
			// Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
			String uid = StringUtil.trimNull(rs.getString("uid"));
			String sid = StringUtil.trimNull(rs.getString("sid"));
			String opDate = StringUtil.trimNull(rs.getString("opDate"));
			String stateNum = StringUtil.trimNull(rs.getString("stateNum"));
			String sql1 = "select count(1) c    from qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and uid='" + uid + "' and sid='" + sid + "' and opDate='" + opDate + "' and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
			pstm1 = conn.prepareStatement(sql1);
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				String c = StringUtil.trimNull(rs1.getString("c"));
				if (c.equals(stateNum)) {
					total++;
				}
			}
			DBUtil.close(null, pstm1, rs1);
		}

		// 活期宝
		sql = "select count(1) countNum from qj_cashbao_db.trade_log where (operate=1 or operate=4)  and state=4  and FROM_UNIXTIME(opDate) BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}

		return total;
	}

	/**
	 * 
	 * Function:申购失败金额
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午5:10:42
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public double getInvestFailMoney(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		double total = 0;

		// 非组合
		String sql = "select ifnull(sum(sum),0) sum from qj_fund_db.operat_log where (operate=1 or operate=4)  and state=4   and sid=0 and opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}

		// 组合
		// sql = "select count(1) countNum from (select uid, sid,opDate from
		// qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and sid<>0 and
		// opDate BETWEEN '"+date+" 00:00:00' and '"+date+" 23:59:59' group by
		// uid,sid,opDate)t";
		sql = "select  uid, sid,opDate ,count(state),ifnull(sum(sum),0) sum ,count(state) stateNum  from qj_fund_db.operat_log where (operate=1 or operate=4)    and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'   group by uid, sid,opDate";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		PreparedStatement pstm1 = null;
		ResultSet rs1 = null;
		while (rs.next()) {
			// total +=
			// Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
			String uid = StringUtil.trimNull(rs.getString("uid"));
			String sid = StringUtil.trimNull(rs.getString("sid"));
			String opDate = StringUtil.trimNull(rs.getString("opDate"));
			String stateNum = StringUtil.trimNull(rs.getString("stateNum"));
			String sum = StringUtil.trimNull(rs.getString("sum"));
			String sql1 = "select count(1) c    from qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and uid='" + uid + "' and sid='" + sid + "' and opDate='" + opDate + "' and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
			pstm1 = conn.prepareStatement(sql1);
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				String c = StringUtil.trimNull(rs1.getString("c"));
				if (c.equals(stateNum)) {
					total += Double.parseDouble(sum);
				}
			}
			DBUtil.close(null, pstm1, rs1);
		}

		// 活期宝
		sql = "select ifnull(sum(sum),0) sum from qj_cashbao_db.trade_log where (operate=1 or operate=4)  and state=4  and FROM_UNIXTIME(opDate) BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}

		return new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * Function:申购成功人数
	 * 
	 * @author TongXin DateTime 2015年8月13日 上午10:27:06
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int getInvestSuccessPersonNum(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		int total = 0;

		String sql = "select count(1) countNum from ( " + "select distinct uid from ( " + "select uid from qj_fund_db.operat_log where (operate=1 or operate=4) and (state=1 or state=3) and  opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'" + "union all"
				+ " select uid from qj_cashbao_db.trade_log where (operate=1 or operate=4) and (state=2 or state=3) and  FROM_UNIXTIME(opDate)  BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'" + ")t)a";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total = Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}
		return total;
	}

	/**
	 * 
	 * Function:申购成功笔数
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午5:31:11
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int getInvestSucessNum(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		int total = 0;

		// 非组合
		String sql = "select count(1) countNum from qj_fund_db.operat_log where (operate=1 or operate=4)  and (state=1 or state=3)   and sid=0 and opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}

		// 组合
		// sql = "select count(1) countNum from (select uid, sid,opDate from
		// qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and sid<>0 and
		// opDate BETWEEN '"+date+" 00:00:00' and '"+date+" 23:59:59' group by
		// uid,sid,opDate)t";
		sql = "select  uid, sid,opDate ,count(state) stateNum  from qj_fund_db.operat_log where (operate=1 or operate=4)    and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'   group by uid, sid,opDate";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		PreparedStatement pstm1 = null;
		ResultSet rs1 = null;
		while (rs.next()) {
			// total +=
			// Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
			String uid = StringUtil.trimNull(rs.getString("uid"));
			String sid = StringUtil.trimNull(rs.getString("sid"));
			String opDate = StringUtil.trimNull(rs.getString("opDate"));
			String stateNum = StringUtil.trimNull(rs.getString("stateNum"));
			String sql1 = "select count(1) c    from qj_fund_db.operat_log where (operate=1 or operate=4) and (state=1 or state=3) and uid='" + uid + "' and sid='" + sid + "' and opDate='" + opDate + "' and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
			pstm1 = conn.prepareStatement(sql1);
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				String c = StringUtil.trimNull(rs1.getString("c"));
				if (c.equals(stateNum)) {
					total++;
				}
			}
			DBUtil.close(null, pstm1, rs1);
		}

		// 活期宝
		sql = "select count(1) countNum from qj_cashbao_db.trade_log where (operate=1 or operate=4)  and (state=2 or state=3)  and FROM_UNIXTIME(opDate) BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
		}

		return total;
	}

	/**
	 * 
	 * Function:申购成功金额
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午5:36:17
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public double getInvestSuccessMoney(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		double total = 0;

		// 非组合
		String sql = "select ifnull(sum(sum),0) sum from qj_fund_db.operat_log where (operate=1 or operate=4)  and (state=1 or state=3)   and sid=0 and opDate BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}

		// 组合
		// sql = "select count(1) countNum from (select uid, sid,opDate from
		// qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and sid<>0 and
		// opDate BETWEEN '"+date+" 00:00:00' and '"+date+" 23:59:59' group by
		// uid,sid,opDate)t";
		sql = "select  uid, sid,opDate ,count(state),ifnull(sum(sum),0) sum ,count(state) stateNum  from qj_fund_db.operat_log where (operate=1 or operate=4)    and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'   group by uid, sid,opDate";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		PreparedStatement pstm1 = null;
		ResultSet rs1 = null;
		while (rs.next()) {
			// total +=
			// Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
			String uid = StringUtil.trimNull(rs.getString("uid"));
			String sid = StringUtil.trimNull(rs.getString("sid"));
			String opDate = StringUtil.trimNull(rs.getString("opDate"));
			String stateNum = StringUtil.trimNull(rs.getString("stateNum"));
			String sum = StringUtil.trimNull(rs.getString("sum"));
			String sql1 = "select count(1) c    from qj_fund_db.operat_log where (operate=1 or operate=4) and (state=1 or state=3) and uid='" + uid + "' and sid='" + sid + "' and opDate='" + opDate + "' and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
			pstm1 = conn.prepareStatement(sql1);
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				String c = StringUtil.trimNull(rs1.getString("c"));
				if (c.equals(stateNum)) {
					total += Double.parseDouble(sum);
				}
			}
			DBUtil.close(null, pstm1, rs1);
		}

		// 活期宝
		sql = "select ifnull(sum(sum),0) sum from qj_cashbao_db.trade_log where (operate=1 or operate=4)  and (state=2 or state=3)  and FROM_UNIXTIME(opDate) BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			total += Double.parseDouble(StringUtil.trimNull(rs.getString("sum")));
		}

		return new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * Function:部分成功笔数
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午5:39:08
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public int getInvestPartNum(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		int total = 0;

		// 组合
		// sql = "select count(1) countNum from (select uid, sid,opDate from
		// qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and sid<>0 and
		// opDate BETWEEN '"+date+" 00:00:00' and '"+date+" 23:59:59' group by
		// uid,sid,opDate)t";
		String sql = "select  uid, sid,opDate ,count(state) stateNum  from qj_fund_db.operat_log where (operate=1 or operate=4)    and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'   group by uid, sid,opDate";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		PreparedStatement pstm1 = null;
		ResultSet rs1 = null;
		while (rs.next()) {
			// total +=
			// Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
			String uid = StringUtil.trimNull(rs.getString("uid"));
			String sid = StringUtil.trimNull(rs.getString("sid"));
			String opDate = StringUtil.trimNull(rs.getString("opDate"));
			String stateNum = StringUtil.trimNull(rs.getString("stateNum"));
			String sql1 = "select count(1) c    from qj_fund_db.operat_log where (operate=1 or operate=4) and (state=1 or state=3) and uid='" + uid + "' and sid='" + sid + "' and opDate='" + opDate + "' and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
			pstm1 = conn.prepareStatement(sql1);
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				String c = StringUtil.trimNull(rs1.getString("c"));
				if (!c.equals(stateNum) && Integer.parseInt(c) > 0) {
					total++;
				}
			}
			DBUtil.close(null, pstm1, rs1);
		}

		return total;
	}

	/**
	 * 
	 * Function:部分成功金额
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午5:46:53
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public double getInvestPartMoney(PreparedStatement pstm, ResultSet rs, Connection conn, String date) throws SQLException {
		double total = 0;

		// 组合
		// sql = "select count(1) countNum from (select uid, sid,opDate from
		// qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and sid<>0 and
		// opDate BETWEEN '"+date+" 00:00:00' and '"+date+" 23:59:59' group by
		// uid,sid,opDate)t";
		String sql = "select  uid, sid,opDate ,count(state),ifnull(sum(sum),0) sum ,count(state) stateNum  from qj_fund_db.operat_log where (operate=1 or operate=4)    and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'   group by uid, sid,opDate";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		PreparedStatement pstm1 = null;
		ResultSet rs1 = null;
		while (rs.next()) {
			// total +=
			// Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
			String uid = StringUtil.trimNull(rs.getString("uid"));
			String sid = StringUtil.trimNull(rs.getString("sid"));
			String opDate = StringUtil.trimNull(rs.getString("opDate"));
			String stateNum = StringUtil.trimNull(rs.getString("stateNum"));
			String sum = StringUtil.trimNull(rs.getString("sum"));
			String sql1 = "select count(1) c ,ifnull(sum(sum),0) sum1   from qj_fund_db.operat_log where (operate=1 or operate=4) and (state=1 or state=3) and uid='" + uid + "' and sid='" + sid + "' and opDate='" + opDate + "' and sid<>0 and opDate    BETWEEN '" + date + " 00:00:00' and '" + date + " 23:59:59'";
			pstm1 = conn.prepareStatement(sql1);
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				String c = StringUtil.trimNull(rs1.getString("c"));
				String sum1 = StringUtil.trimNull(rs1.getString("sum1"));
				if (!c.equals(stateNum) && Integer.parseInt(c) > 0) {
					total += Double.parseDouble(sum1);
				}
			}
			DBUtil.close(null, pstm1, rs1);
		}

		return new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] args) {

	}

	/*
	 * (非 Javadoc) <p>Title: getFailList</p> <p>Description: </p>
	 * 
	 * @param fail
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getFailList(com.forestry.model.sys.Fail)
	 */
	@Override
	public List<Fail> getFailList(Fail fail) {
		// TODO Auto-generated method stub
		String sql = "select opdate as date,uid,name,mobile,fund,invest,bank,reason from qj_qjs_db.day_fail where 1=1 ";
		if (!"".equals(StringUtil.trimNull(fail.getSdate()))) {
			sql += " and opDate >='" + fail.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(fail.getEdate()))) {
			sql += " and opDate <='" + fail.getEdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(fail.get$like_mobile()))) {
			sql += " and mobile like'%" + fail.get$like_mobile() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fail.get$like_uid()))) {
			sql += " and uid like'%" + fail.get$like_uid() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fail.get$like_name()))) {
			sql += " and name like'%" + fail.get$like_name() + "%'";
		}

		String key = fail.getSortedConditions().keySet().iterator().next();
		String value = fail.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + fail.getFirstResult() + "," + fail.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Fail> listFails = new ArrayList<Fail>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Fail op = new Fail();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setUid(StringUtil.trimNull(o[1]));
			op.setName(StringUtil.trimNull(o[2]));
			op.setMobile(StringUtil.trimNull(o[3]));
			op.setFund(StringUtil.trimNull(o[4]));
			op.setInvest(StringUtil.trimNull(o[5]));
			op.setBank(StringUtil.trimNull(o[6]));
			op.setReason(StringUtil.trimNull(o[7]));
			listFails.add(op);
		}
		return listFails;
	}

	/*
	 * (非 Javadoc) <p>Title: getFailListCount</p> <p>Description: </p>
	 * 
	 * @param fail
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getFailListCount(com.forestry.model.sys.
	 * Fail)
	 */
	@Override
	public long getFailListCount(Fail fail) {
		// TODO Auto-generated method stub
		String sql = "select count(1) from (select opdate as date,uid,name,mobile,fund,invest,bank,reason from qj_qjs_db.day_fail where 1=1 ";
		if (!"".equals(StringUtil.trimNull(fail.getSdate()))) {
			sql += " and opDate >='" + fail.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(fail.getEdate()))) {
			sql += " and opDate <='" + fail.getEdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(fail.get$like_mobile()))) {
			sql += " and mobile like'%" + fail.get$like_mobile() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fail.get$like_uid()))) {
			sql += " and uid like'%" + fail.get$like_uid() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(fail.get$like_name()))) {
			sql += " and name like'%" + fail.get$like_name() + "%'";
		}

		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	@Override
	public void statistic_day_fail() {
		// TODO Auto-generated method stub
		try {

			List<Fail> listFails = caculateFail(DateUtil.currentTimeMinus(1));
			saveDay_failToDb(listFails);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	private List<Fail> caculateFail(String sdate) throws IOException {

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Fail> listFailData = new ArrayList<Fail>();
		try {
			conn = DBUtil.getConnection();
			List<Fail> listCasFailReasons = getCashBaoFailReason(pstm, rs, conn, sdate);// 活期宝
			List<Fail> listSingleFundReasons = getSingleFundFailReason(pstm, rs, conn, sdate);// 单只基金
			List<Fail> listGroupFundReasons = getGroupFundFailReason(pstm, rs, conn, sdate); // 组合基金
			listFailData.addAll(listCasFailReasons);
			listFailData.addAll(listSingleFundReasons);
			listFailData.addAll(listGroupFundReasons);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pstm, rs);
		}

		return listFailData;
	}

	/**
	 * 
	 * Function: 获取活期宝失败
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午3:40:43
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<Fail> getCashBaoFailReason(PreparedStatement pstm, ResultSet rs, Connection conn, String sdate) throws SQLException {
		List<Fail> listInvestData = new ArrayList<Fail>();

		String sql = "select u.name,u.mobile, FROM_UNIXTIME(tl.opDate) as opDate,tl.uid,'活期宝' as fund,sum as invest,bank, remark as reason from qj_cashbao_db.trade_log tl  "
				+ "   left join qj_user_db.`user` u on(tl.uid=u.uid)  where (tl.operate=1 or tl.operate=4)  and tl.state=4  and FROM_UNIXTIME(tl.opDate) BETWEEN '" + sdate + " 00:00:00' and '" + sdate + " 23:59:59';";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			Fail fr = new Fail();
			if (listExcluded.contains(rs.getString("uid"))) {
				continue;
			}
			fr.setDate(StringUtil.trimNull(rs.getString("opDate")));
			fr.setUid(StringUtil.trimNull(rs.getString("uid")));
			fr.setFund(StringUtil.trimNull(rs.getString("fund")));
			fr.setInvest(StringUtil.trimNull(rs.getString("invest")));
			fr.setBank(StringUtil.trimNull(rs.getString("bank")));
			fr.setReason(StringUtil.trimNull(rs.getString("reason")));
			fr.setName(StringUtil.trimNull(rs.getString("name")));
			fr.setMobile(StringUtil.trimNull(rs.getString("mobile")));
			listInvestData.add(fr);
		}
		return listInvestData;
	}

	/**
	 * 
	 * Function:获取单支基金失败
	 * 
	 * @author TongXin DateTime 2015年8月11日 上午11:39:16
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param sdate
	 * @param edate
	 * @return
	 * @throws SQLException
	 */
	public List<Fail> getSingleFundFailReason(PreparedStatement pstm, ResultSet rs, Connection conn, String sdate) throws SQLException {
		List<Fail> listInvestData = new ArrayList<Fail>();

		String sql = " select u.name,u.mobile,ol.opdate,ol.uid,ol.abbrev as fund,ol.sum as invest,ol.bank,ol.reason From qj_fund_db.operat_log ol left join qj_user_db.`user` u on(ol.uid=u.uid) where ol.operate =1 and ol.state=4 and ol.sid=0 and ol.opDate BETWEEN '" + sdate + " 00:00:00' and '"
				+ sdate + " 23:59:59';";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		while (rs.next()) {
			Fail fr = new Fail();
			if (listExcluded.contains(rs.getString("uid"))) {
				continue;
			}
			fr.setDate(StringUtil.trimNull(rs.getString("opDate")));
			fr.setUid(StringUtil.trimNull(rs.getString("uid")));
			fr.setFund(StringUtil.trimNull(rs.getString("fund")));
			fr.setInvest(StringUtil.trimNull(rs.getString("invest")));
			fr.setBank(StringUtil.trimNull(rs.getString("bank")));
			fr.setReason(StringUtil.trimNull(rs.getString("reason")));
			fr.setName(StringUtil.trimNull(rs.getString("name")));
			fr.setMobile(StringUtil.trimNull(rs.getString("mobile")));
			listInvestData.add(fr);
		}
		return listInvestData;
	}

	/**
	 * 
	 * Function:组合申购失败
	 * 
	 * @author TongXin DateTime 2015年8月10日 下午5:10:42
	 * @param pstm
	 * @param rs
	 * @param conn
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public List<Fail> getGroupFundFailReason(PreparedStatement pstm, ResultSet rs, Connection conn, String sdate) throws SQLException {
		List<Fail> listInvestData = new ArrayList<Fail>();

		// 组合
		// sql = "select count(1) countNum from (select uid, sid,opDate from
		// qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and sid<>0 and
		// opDate BETWEEN '"+date+" 00:00:00' and '"+date+" 23:59:59' group by
		// uid,sid,opDate)t";
		String sql = "select  ol.uid,u.name,u.mobile, sid,opDate ,ifnull(sum(sum),0) sum ,count(ol.state) stateNum ,bank from qj_fund_db.operat_log ol " + " left join qj_user_db.`user` u on (ol.uid =u.uid)" + " where (operate=1 or operate=4)    and sid<>0 and opDate    BETWEEN '" + sdate + " 00:00:00' and '"
				+ sdate + " 23:59:59'   group by uid, sid,bank,opDate,name,mobile";
		pstm = conn.prepareStatement(sql);
		rs = pstm.executeQuery();
		PreparedStatement pstm1 = null;
		ResultSet rs1 = null;
		while (rs.next()) {
			if (listExcluded.contains(rs.getString("uid"))) {
				continue;
			}
			// total +=
			// Integer.parseInt(StringUtil.trimNull(rs.getString("countNum")));
			String uid = StringUtil.trimNull(rs.getString("uid"));
			String sid = StringUtil.trimNull(rs.getString("sid"));
			String opDate = StringUtil.trimNull(rs.getString("opDate"));
			String stateNum = StringUtil.trimNull(rs.getString("stateNum"));
			String sum = StringUtil.trimNull(rs.getString("sum"));
			String bank = StringUtil.trimNull(rs.getString("bank"));
			String name = StringUtil.trimNull(rs.getString("name"));
			String mobile = StringUtil.trimNull(rs.getString("mobile"));
			String sql1 = "select count(1) c    from qj_fund_db.operat_log where (operate=1 or operate=4) and state=4 and uid='" + uid + "' and sid='" + sid + "' and opDate='" + opDate + "' and sid<>0 and opDate    BETWEEN '" + sdate + " 00:00:00' and '" + sdate + " 23:59:59'";
			pstm1 = conn.prepareStatement(sql1);
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				String c = StringUtil.trimNull(rs1.getString("c"));
				if (c.equals(stateNum)) {
					String sql2 = " select distinct ol.reason,fs.name as fundname from   qj_fund_db.finance_schema  fs " + " left join qj_fund_db.operat_log ol on(fs.sid=ol.sid) where fs.sid ='" + sid + "'";
					PreparedStatement pstm2 = conn.prepareStatement(sql2);
					ResultSet rs2 = pstm2.executeQuery();
					String fundname = null, reason = null;
					while (rs2.next()) {
						fundname = StringUtil.trimNull(rs2.getString("fundname"));
						reason = StringUtil.trimNull(rs2.getString("reason"));
					}
					Fail fr = new Fail();
					fr.setDate(opDate);
					fr.setBank(bank);
					fr.setReason(reason);
					fr.setFund(fundname);
					fr.setInvest(sum);
					fr.setUid(uid);
					fr.setName(name);
					fr.setMobile(mobile);
					listInvestData.add(fr);
					DBUtil.close(null, pstm2, rs2);
				}
			}
			DBUtil.close(null, pstm1, rs1);
		}

		return listInvestData;
		// return new BigDecimal(total).setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * Function:
	 * 
	 * @author TongXin DateTime 2015年8月17日 上午10:15:28
	 * @param list
	 */
	public void saveDay_failToDb(List<Fail> list) {
		try {
			if (list == null) {
				return;
			}
			int m = 500;// 批量处理条数
			StringBuffer sql = new StringBuffer();
			long c1 = Calendar.getInstance().getTimeInMillis();
			sql.append(" replace into qj_qjs_db.day_fail(opdate,uid,name,mobile,fund,invest,bank,reason) values");
			for (int i = 0; i < list.size(); i++) {
				Fail op = list.get(i);
				sql.append("('" + op.getDate() + "','");
				sql.append(op.getUid() + "','");
				sql.append(op.getName() + "','");
				sql.append(op.getMobile() + "','");
				sql.append(op.getFund() + "','");
				sql.append(op.getInvest() + "','");
				sql.append(op.getBank() + "','");
				sql.append(op.getReason());
				if (i % m == 0 || i == (list.size() - 1)) {
					sql.append("');");
					DBUtil.dbInsert(sql.toString());
					sql.setLength(0);
					sql.append(" replace into qj_qjs_db.day_fail(opdate,uid,name,mobile,fund,invest,bank,reason) values");
				} else {
					sql.append("'),");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelList</p> <p>渠道统计</p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelList(com.forestry.model.sys.
	 * Channel)
	 */
	@Override
	public List<Channel> getChannelList(Channel channel) {
		// TODO Auto-generated method stub

		String sql = "select s_date as  date,platform,cl.name as channel,reg_num,bound_num,new_bound_num,user_num,money_num, case user_num when 0 then  0 else money_num/user_num  end  as per_money_num ,new_user_num,new_money_num, case new_user_num when 0 then  0 else new_money_num/new_user_num  end  as per_new_money_num  from qj_statis_db.s_date_channel c "
			       + " left join qj_activ_db.channel_list cl on(c.channel=cl.channel)"
				+ "where 1=1 ";
		if (!"".equals(StringUtil.trimNull(channel.getSdate()))) {
			sql += " and s_date >='" + channel.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.getEdate()))) {
			sql += " and s_date <='" + channel.getEdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_channel()))) {
			sql += " and cl.name like '%" + channel.get$like_channel() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_platform()))) {
			sql += " and platform like '%" + channel.get$like_platform() + "%'";
		}

		String key = channel.getSortedConditions().keySet().iterator().next();
		String value = channel.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + channel.getFirstResult() + "," + channel.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Channel> listOperates = new ArrayList<Channel>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Channel op = new Channel();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setPlatform(StringUtil.trimNull(o[1]));
			op.setChannel(StringUtil.trimNull(o[2]));
			op.setReg_num(StringUtil.trimNull(o[3]));
			op.setBound_num(StringUtil.trimNull(o[4]));
			op.setNew_bound_num(StringUtil.trimNull(o[5]));
			op.setUser_num(StringUtil.trimNull(o[6]));
			op.setMoney_num(StringUtil.trimNull(o[7]));
			op.setPer_money_num(StringUtil.trimNull(o[8]));
			op.setNew_user_num(StringUtil.trimNull(o[9]));
			op.setNew_money_num(StringUtil.trimNull(o[10]));
			op.setPer_new_money_num(StringUtil.trimNull(o[11]));
			listOperates.add(op);
		}
		return listOperates;
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelListCount</p> <p>Description: </p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelListCount(com.forestry.model.
	 * sys.Channel)
	 */
	@Override
	public long getChannelListCount(Channel channel) {
		// TODO Auto-generated method stub
		String sql = "select count(1) from (select s_date as  date,platform,cl.name as channel,reg_num,bound_num,new_bound_num,user_num,money_num,new_user_num,new_money_num from qj_statis_db.s_date_channel c"
				 + " left join qj_activ_db.channel_list cl on(c.channel=cl.channel)"
				+ "where 1=1 ";

		if (!"".equals(StringUtil.trimNull(channel.getSdate()))) {
			sql += " and s_date >='" + channel.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.getEdate()))) {
			sql += " and s_date <='" + channel.getEdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_channel()))) {
			sql += " and cl.name like '%" + channel.get$like_channel() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_platform()))) {
			sql += " and platform like '%" + channel.get$like_platform() + "%'";
		}

		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	/*
	 * 
	 * 
	 * select s_date,ifnull(sum(reg_num),0) as reg_num, ifnull(sum(bound_num),0)
	 * as bound_num, ifnull(sum(user_num),0) as user_num,
	 * ifnull(sum(first_user_num),0) as first_user_num, case when
	 * ifnull(sum(user_num),0)=0 then 0 else concat(round(
	 * ifnull(sum(new_user_num)) /ifnull(sum(user_num)) *100,2) ,'%') end as
	 * new_user_rate, ifnull(sum(new_money_num),0) as new_money_num,
	 * ifnull(sum(money_num),0) as money_num from
	 * qj_statis_db.s_date_channel_timely where 1=1 and platform='' and
	 * channel='' group by s_date,platform,channel
	 */
	/*
	 * (非 Javadoc) <p>Title: getChannelList</p> <p>渠道统计</p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelList(com.forestry.model.sys.
	 * Channel)
	 */
	@Override
	public List<Channel2> getChannel2List(Channel2 channel) {
		// TODO Auto-generated method stub

		String sql = "select s_date as date,ifnull(sum(reg_num),0) as reg_num, " + "              ifnull(sum(bound_num),0) as bound_num," + "              ifnull(sum(user_num),0) as user_num," + "              ifnull(sum(first_user_num),0) as first_user_num,"
				+ "              case when ifnull(sum(user_num),0)=0 then 0 else concat(round( ifnull(sum(new_user_num),0) / sum(user_num) *100,2) ,'%')  end as new_user_rate,  " + "              ifnull(sum(new_money_num),0) as new_money_num,"
				+ "              ifnull(sum(money_num),0) as money_num ,ifnull(sum(new_user_num),0) as new_user_num" + "              from qj_statis_db.s_date_channel_timely " + "              where 1=1   ";

		if (!"".equals(StringUtil.trimNull(channel.getSdate()))) {
			sql += " and s_date >='" + channel.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.getEdate()))) {
			sql += " and s_date <='" + channel.getEdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.get$last()))) {
			sql += " and s_date >='" + DateUtil.currentTimeMinus(Integer.parseInt(channel.get$last())) + "' and s_date <= '" + DateUtil.currentMonthMinus(0) + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_channel2()))) {
			if("qianjing".equals(channel.get$like_channel2())){
				sql += " and channel not in  (select pname from qj_user_db.partner  )";
			}else{
				sql += " and channel ='" + channel.get$like_channel2() + "'";
			}
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_channel()))) {
			if("qianjing".equals(channel.get$like_channel())){
				sql += " and channel not in  (select pname from qj_user_db.partner  )";
			}else {
			sql += "  and channel = '" + channel.get$like_channel() + "'";
			}
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_platform()))) {
			sql += " and platform = '" + channel.get$like_platform() + "'";
		}

		sql += "     group by s_date";

		String key = channel.getSortedConditions().keySet().iterator().next();
		String value = channel.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + channel.getFirstResult() + "," + channel.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Channel2> listOperates = new ArrayList<Channel2>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Channel2 op = new Channel2();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setReg_num(StringUtil.trimNull(o[1]));
			op.setBound_num(StringUtil.trimNull(o[2]));
			op.setUser_num(StringUtil.trimNull(o[3]));
			op.setFirst_user_num(StringUtil.trimNull(o[4]));
			op.setNew_user_rate(StringUtil.trimNull(o[5]));
			op.setNew_money_num(StringUtil.trimNull(o[6]));
			op.setMoney_num(StringUtil.trimNull(o[7]));
			op.setNew_user_num(StringUtil.trimNull(o[8]));
			listOperates.add(op);
		}
		return listOperates;
	}

	@Override
	public List<Channel2> getChannel2List2(Channel2 channel) {
		// TODO Auto-generated method stub
		String sdate = DateUtil.currentTimeMinus(7);
		String edate = DateUtil.currentTimeMinus(0);
		String sql =        "select * From (                                                                 "
				+"select s_date as date,                                                          "
				+"ifnull(sum(reg_num),0) as reg_num,                                              "
				+"   ifnull(sum(bound_num),0) as bound_num,                                       "
				+"     ifnull(sum(user_num),0) as user_num,                                       "
				+"   ifnull(sum(first_user_num),0) as first_user_num,                             "
				+"     case when ifnull(sum(user_num),0)=0                                        "
				+"then 0                                                                          "
				+"else concat(round( ifnull(sum(new_user_num),0) / sum(user_num) *100,2) ,'%')    "
				+"  end as new_user_rate,                                                         "
				+"     ifnull(sum(new_money_num),0) as new_money_num,                             "
				+"     ifnull(sum(money_num),0) as money_num ,                                    "
				+"ifnull(sum(new_user_num),0) as new_user_num    ,                                "
				+"  'qianjing' as 'parter'                                                           "
				+"   from qj_statis_db.s_date_channel_timely                                      "
				+"      where 1=1                                                                 "
				+" and s_date >='"+sdate+"' and s_date <='"+edate+"'                              "
				+"   and channel not  in  (select pname from qj_user_db.partner  )                   "
				+"                                                                                "
				+"   group by s_date                                                     "
				+"                                                                                "
				+"union all                                                                       "
				+"                                                                                "
				+"select s_date as date,                                                          "
				+"ifnull(sum(reg_num),0) as reg_num,                                              "
				+"   ifnull(sum(bound_num),0) as bound_num,                                       "
				+"     ifnull(sum(user_num),0) as user_num,                                       "
				+"   ifnull(sum(first_user_num),0) as first_user_num,                             "
				+"     case when ifnull(sum(user_num),0)=0                                        "
				+"then 0                                                                          "
				+"else concat(round( ifnull(sum(new_user_num),0) / sum(user_num) *100,2) ,'%')    "
				+"  end as new_user_rate,                                                         "
				+"     ifnull(sum(new_money_num),0) as new_money_num,                             "
				+"     ifnull(sum(money_num),0) as money_num ,                                    "
				+"ifnull(sum(new_user_num),0) as new_user_num    ,                                "
				+"  channel as 'parter'                                                        "
				+"   from qj_statis_db.s_date_channel_timely                                      "
				+"      where 1=1                                                                 "
				+" and s_date >='"+sdate+"' and s_date <='"+edate+"'                              "
				+"   and channel   in  (select pname from qj_user_db.partner)                     "
				+"                                                                                "
				+"   group by s_date  ,channel                                                           "
				+")t                                                                              "
				+" order by date DESC limit 0,100                                                  ";     

	 
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Channel2> listOperates = new ArrayList<Channel2>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Channel2 op = new Channel2();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setReg_num(StringUtil.trimNull(o[1]));
			op.setBound_num(StringUtil.trimNull(o[2]));
			op.setUser_num(StringUtil.trimNull(o[3]));
			op.setFirst_user_num(StringUtil.trimNull(o[4]));
			op.setNew_user_rate(StringUtil.trimNull(o[5]));
			op.setNew_money_num(StringUtil.trimNull(o[6]));
			op.setMoney_num(StringUtil.trimNull(o[7]));
			op.setNew_user_num(StringUtil.trimNull(o[8]));
			op.setPartner(StringUtil.trimNull(o[9]));
			listOperates.add(op);
		}
		return listOperates;
	}
	
	
	
	@Override
	public List<Channel2> getChannel2NumGraph(Channel2 channel) {
		// TODO Auto-generated method stub

		String sql = "select s_date as date,ifnull(sum(reg_num),0) as reg_num, " + "              ifnull(sum(bound_num),0) as bound_num," + "              ifnull(sum(user_num),0) as user_num," + "              ifnull(sum(first_user_num),0) as first_user_num,"
				+ "              case when ifnull(sum(user_num),0)=0 then 0 else concat(round( ifnull(sum(new_user_num),0) / sum(user_num) *100,2) ,'%')  end as new_user_rate,  " + "              ifnull(sum(new_money_num),0) as new_money_num,"
				+ "              ifnull(sum(money_num),0) as money_num " + "              from qj_statis_db.s_date_channel_timely " + "              where 1=1   ";

		sql += " and s_date >='" + DateUtil.currentTimeMinus(2) + "'";

		sql += " and s_date <='" + DateUtil.currentTimeMinus(0) + "'";

		sql += "     group by s_date";

		sql += "     order by " + "date" + " " + "asc"  ;
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Channel2> listOperates = new ArrayList<Channel2>();
		  NumberFormat nt = NumberFormat.getPercentInstance();
		  nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Channel2 op = new Channel2();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setReg_num(StringUtil.trimNull(o[1]));
			op.setBound_num(StringUtil.trimNull(o[2]));
			op.setUser_num(StringUtil.trimNull(o[3]));
			op.setFirst_user_num(StringUtil.trimNull(o[4]));
			op.setNew_user_rate(StringUtil.trimNull(o[5]));
			op.setNew_money_num(StringUtil.trimNull(o[6]));
			op.setMoney_num(StringUtil.trimNull(o[7]));
			
			if (i >= 1) {
				if (Double.parseDouble(listOperates.get(i-1).getReg_num()) != 0) {
					double today = Double.parseDouble(String.valueOf(o[1]));
					double yesterday = Double.parseDouble(listOperates.get(i-1).getReg_num());
					op.setReg_num_ration(nt.format((today - yesterday) / yesterday));
				} else {
					op.setReg_num_ration("0.00%");
				}
				if (Double.parseDouble(listOperates.get(i-1).getBound_num()) != 0) {
					double today = Double.parseDouble(String.valueOf(o[2]));
					double yesterday = Double.parseDouble(listOperates.get(i-1).getBound_num());
					op.setBound_num_ration(nt.format((today - yesterday) / yesterday));
				} else {
					op.setBound_num_ration("0.00%");
				}
				if (Double.parseDouble(listOperates.get(i-1).getUser_num()) != 0) {
					double today = Double.parseDouble(String.valueOf(o[3]));
					double yesterday = Double.parseDouble(listOperates.get(i-1).getUser_num());
					op.setUser_num_ration(nt.format((today - yesterday) / yesterday));
				} else {
					op.setUser_num_ration("0.00%");
				}
				if (Double.parseDouble(listOperates.get(i-1).getFirst_user_num()) != 0) {
					double today = Double.parseDouble(String.valueOf(o[4]));
					double yesterday = Double.parseDouble(listOperates.get(i-1).getFirst_user_num());
					op.setFirst_user_num_ration(nt.format((today - yesterday) / yesterday));
				} else {
					op.setFirst_user_num_ration("0.00%");
				}

			}
			
			listOperates.add(op);
		}
		return listOperates;
	}
	
	
	@Override
	public List<Channel2> getChannel2RateGraph(Channel2 channel) {
		// TODO Auto-generated method stub

		String sql = "select s_date as date,ifnull(sum(reg_num),0) as reg_num, " + "              ifnull(sum(bound_num),0) as bound_num," + "              ifnull(sum(user_num),0) as user_num," + "              ifnull(sum(first_user_num),0) as first_user_num,"
				+ "              case when ifnull(sum(user_num),0)=0 then 0 else  round( ifnull(sum(new_user_num),0) / sum(user_num) *100,2)   end as new_user_rate,  " + "              ifnull(sum(new_money_num),0) as new_money_num,"
				+ "              ifnull(sum(money_num),0) as money_num,case when ifnull(sum(user_num),0)=0 then 0 else   ifnull(sum(new_user_num),0) / sum(user_num)    end as new_user_rate_ration " + "              from qj_statis_db.s_date_channel_timely " + "              where 1=1   ";

		sql += " and s_date >='" + DateUtil.currentTimeMinus(7) + "'";

		sql += " and s_date <='" + DateUtil.currentTimeMinus(0) + "'";

		sql += "     group by s_date";

		sql += "     order by " + "date" + " " + "asc"  ;
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Channel2> listOperates = new ArrayList<Channel2>();
		  NumberFormat nt = NumberFormat.getPercentInstance();
		  nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Channel2 op = new Channel2();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setReg_num(StringUtil.trimNull(o[1]));
			op.setBound_num(StringUtil.trimNull(o[2]));
			op.setUser_num(StringUtil.trimNull(o[3]));
			op.setFirst_user_num(StringUtil.trimNull(o[4]));
			op.setNew_user_rate(StringUtil.trimNull(o[5]));
			op.setNew_money_num(StringUtil.trimNull(o[6]));
			op.setMoney_num(StringUtil.trimNull(o[7]));
			op.setNew_user_rate1(StringUtil.trimNull(o[8]));
			if (i >= 1) {
				if (Double.parseDouble(listOperates.get(i-1).getNew_user_rate1()) != 0) {
					double today = Double.parseDouble(String.valueOf(o[8]));
					double yesterday = Double.parseDouble(listOperates.get(i-1).getNew_user_rate1());
					op.setNew_user_rate_ration(nt.format((today - yesterday) / yesterday));
				} else {
					op.setNew_user_rate_ration("0.00%");
				}
			}
			
			listOperates.add(op);
		}
		return listOperates;
	}
	
	
	@Override
	public List<Channel2> getChannel2MoneyGraph(Channel2 channel) {
		// TODO Auto-generated method stub

		String sql = "select s_date as date,ifnull(sum(reg_num),0) as reg_num, " + "              ifnull(sum(bound_num),0) as bound_num," + "              ifnull(sum(user_num),0) as user_num," + "              ifnull(sum(first_user_num),0) as first_user_num,"
				+ "              case when ifnull(sum(user_num),0)=0 then 0 else  round( ifnull(sum(new_user_num),0) / sum(user_num) *100,2)   end as new_user_rate,  " + "              ifnull(sum(new_money_num),0) as new_money_num,"
				+ "              ifnull(sum(money_num),0) as money_num,case when ifnull(sum(user_num),0)=0 then 0 else   ifnull(sum(new_user_num),0) / sum(user_num)    end as new_user_rate_ration " + "              from qj_statis_db.s_date_channel_timely " + "              where 1=1   ";

		sql += " and s_date >='" + DateUtil.currentTimeMinus(7) + "'";

		sql += " and s_date <='" + DateUtil.currentTimeMinus(0) + "'";

		sql += "     group by s_date";

		sql += "     order by " + "date" + " " + "asc"  ;
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Channel2> listOperates = new ArrayList<Channel2>();
		  NumberFormat nt = NumberFormat.getPercentInstance();
		  nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Channel2 op = new Channel2();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setReg_num(StringUtil.trimNull(o[1]));
			op.setBound_num(StringUtil.trimNull(o[2]));
			op.setUser_num(StringUtil.trimNull(o[3]));
			op.setFirst_user_num(StringUtil.trimNull(o[4]));
			op.setNew_user_rate(StringUtil.trimNull(o[5]));
			op.setNew_money_num(StringUtil.trimNull(o[6]));
			op.setMoney_num(StringUtil.trimNull(o[7]));
			op.setNew_user_rate1(StringUtil.trimNull(o[8]));
			if (i >= 1) {
				if (Double.parseDouble(listOperates.get(i-1).getNew_money_num()) != 0) {
					double today = Double.parseDouble(String.valueOf(o[6]));
					double yesterday = Double.parseDouble(listOperates.get(i-1).getNew_money_num());
					op.setNew_money_num_ration(nt.format((today - yesterday) / yesterday));
				} else {
					op.setNew_money_num_ration("0.00%");
				}
			}
			
			listOperates.add(op);
		}
		return listOperates;
	}
	
	
	/*
	 * (非 Javadoc) <p>Title: getChannelListCount</p> <p>Description: </p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelListCount(com.forestry.model.
	 * sys.Channel)
	 */
	@Override
	public long getChannel2ListCount(Channel2 channel) {
		// TODO Auto-generated method stub
		String sql = "select count(1) from (select s_date as date, ifnull(sum(reg_num),0) as reg_num, " + "              ifnull(sum(bound_num),0) as bound_num," + "              ifnull(sum(user_num),0) as user_num," + "              ifnull(sum(first_user_num),0) as first_user_num,"
				+ "              case when ifnull(sum(user_num),0)=0 then 0 else concat(round( ifnull(sum(new_user_num),0) / sum(user_num) *100,2) ,'%')  end as new_user_rate,  " + "              ifnull(sum(new_money_num),0) as new_money_num,"
				+ "              ifnull(sum(money_num),0) as money_num " + "              from qj_statis_db.s_date_channel_timely " + "              where 1=1   ";

		if (!"".equals(StringUtil.trimNull(channel.getSdate()))) {
			sql += " and s_date >='" + channel.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.getEdate()))) {
			sql += " and s_date <='" + channel.getEdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.get$last()))) {
			sql += " and s_date >='" + DateUtil.currentTimeMinus(Integer.parseInt(channel.get$last())) + "' and s_date <= '" + DateUtil.currentMonthMinus(0) + "'";
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_channel2()))) {
			if("qianjing".equals(channel.get$like_channel2())){
				sql += " and channel not in  (select pname from qj_user_db.partner  )";
			}else{
				sql += " and channel ='" + channel.get$like_channel2() + "'";
			}
		}

		if (!"".equals(StringUtil.trimNull(channel.get$like_channel()))) {
			if("qianjing".equals(channel.get$like_channel())){
				sql += " and channel not in  (select pname from qj_user_db.partner  )";
			}else {
			sql += "  and channel = '" + channel.get$like_channel() + "'";
			}
		}
		if (!"".equals(StringUtil.trimNull(channel.get$like_platform()))) {
			sql += " and platform = '" + channel.get$like_platform() + "'";
		}

		sql += "     group by s_date )t";

		 

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	
	@Override
	public List<ChannelType> getChannelTypeList(ChannelType channelType) {
		// TODO Auto-generated method stub
		String sql = "select channel as `key`,name as `value` from qj_activ_db.channel_list ";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<ChannelType> listOperates = new ArrayList<ChannelType>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			ChannelType op = new ChannelType();
			Object[] o = (Object[]) list.get(i);
			op.setKey(StringUtil.trimNull(o[0]));
			op.setValue(StringUtil.trimNull(o[1]));
			listOperates.add(op);
		}
		String[][] s ={{"baigujing","minxindai","jintaipingyang","yinlaicaifu","stockradar","renrendai","qianjing"},{"白骨精","民信贷","金太平洋","银来财富","股票雷达","人人贷","钱景"}};
		
		for(int i=0;i<s[0].length;i++){
			ChannelType ct = new ChannelType();
			ct.setKey(s[0][i]);
			ct.setValue(s[1][i]);
			listOperates.add(ct);
		}
		
		ChannelType op = new ChannelType();
		op.setKey("");
		op.setValue("全部");
		listOperates.add(op);
		 
		return listOperates;
	}
	
	/*
	 * (非 Javadoc) <p>Title: getChannelList</p> <p>渠道统计</p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelList(com.forestry.model.sys.
	 * Channel)
	 */
	@Override
	public List<TradeUser> getTradeUserList(TradeUser tradeUser) {
		// TODO Auto-generated method stub

		String sql =  "select t.*,sd2.`value` from ( "
				+"select uid,name,reg_time,boundT,first_time,first_money,sd1.value as first_stype,first_platform,first_channel,second_time,second_money ,second_stype "
				+"from  qj_statis_db.s_trade_user  tu  "
				+"left join qj_statis_db.s_dictionary sd1 on(tu.first_stype=sd1.key) "
				+"and sd1.type='schema_type' "
				+")t  "
				+"left join qj_statis_db.s_dictionary sd2 on(t .second_stype=sd2.key)  "
				+"where 1=1 ";
		if (!"".equals(StringUtil.trimNull(tradeUser.getSdate()))) {
			sql += " and t.reg_time >='" + tradeUser.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(tradeUser.getEdate()))) {
			sql += " and t.reg_time <='" + tradeUser.getEdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(tradeUser.get$like_name()))) {
			sql += " and t.name like '%" + tradeUser.get$like_name() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(tradeUser.get$like_uid()))) {
			sql += " and t.uid like '%" + tradeUser.get$like_uid() + "%'";
		}

		String key = tradeUser.getSortedConditions().keySet().iterator().next();
		String value = tradeUser.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + tradeUser.getFirstResult() + "," + tradeUser.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<TradeUser> listOperates = new ArrayList<TradeUser>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			TradeUser op = new TradeUser();
			Object[] o = (Object[]) list.get(i);
			op.setUid(StringUtil.trimNull(o[0]));
			op.setName(StringUtil.trimNull(o[1]));
			op.setReg_time(StringUtil.trimNull(o[2]));
			op.setBoundT(StringUtil.trimNull(o[3]));
			op.setFirst_time(StringUtil.trimNull(o[4]));
			op.setFirst_money(StringUtil.trimNull(o[5]));
			op.setFirst_stype(StringUtil.trimNull(o[6]));
			op.setFirst_platform(StringUtil.trimNull(o[7]));
			op.setFirst_channel(StringUtil.trimNull(o[8]));
			op.setSecond_time(StringUtil.trimNull(o[9]));
			op.setSecond_money(StringUtil.trimNull(o[10]));
			op.setSecond_stype(StringUtil.trimNull(o[12]));
			listOperates.add(op);
		}
		return listOperates;
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelListCount</p> <p>Description: </p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelListCount(com.forestry.model.
	 * sys.Channel)
	 */
	@Override
	public long getTradeUserListCount(TradeUser tradeUser) {
		// TODO Auto-generated method stub
		String sql = "select count(1) From (select t.*,sd2.`value` from ( "
				+"select uid,name,reg_time,boundT,first_time,first_money,sd1.value as first_stype,first_platform,first_channel,second_time,second_money ,second_stype "
				+"from  qj_statis_db.s_trade_user  tu  "
				+"left join qj_statis_db.s_dictionary sd1 on(tu.first_stype=sd1.key) "
				+"and sd1.type='schema_type' "
				+")t  "
				+"left join qj_statis_db.s_dictionary sd2 on(t .second_stype=sd2.key)  "
				+"where 1=1 ";
		if (!"".equals(StringUtil.trimNull(tradeUser.getSdate()))) {
			sql += " and t.reg_time >='" + tradeUser.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(tradeUser.getEdate()))) {
			sql += " and t.reg_time <='" + tradeUser.getEdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(tradeUser.get$like_name()))) {
			sql += " and t.name like '%" + tradeUser.get$like_name() + "%'";
		}

		if (!"".equals(StringUtil.trimNull(tradeUser.get$like_uid()))) {
			sql += " and t.uid like '%" + tradeUser.get$like_uid() + "%'";
		}

		sql += "    )t1";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelList</p> <p>渠道统计</p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelList(com.forestry.model.sys.
	 * Channel)
	 */
	@Override
	public List<NewOld> getNewOldList(NewOld newOld) {
		// TODO Auto-generated method stub

		String sql = "select * From ( select    CONCAT(week_start,'_',week_end) as date,total_user_num as total_num,sum(new_user_num) as new_num, " + " max(case stype when  'cashbao' then new_user_num  else 0 end) as cashbao_num, "
				+ " max(case stype when  'single' then new_user_num else 0 end) as single_num ," + " max(case stype when  'jinqu' then new_user_num else 0 end) as jinqu_num ," + " max(case stype when  'baoshou' then new_user_num else 0 end) as baoshou_num ,"
				+ " max(case stype when  'wenjian' then new_user_num else 0 end) as wenjian_num ," + " max(case stype when  'licai' then new_user_num else 0 end) as licai_num, " + " max(case stype when  'licai2' then new_user_num else 0 end) as licai2_num,"
				+ " max(case stype when  'yanglao' then new_user_num else 0 end) as yanglao_num," + " max(case stype when  'maifang' then new_user_num else 0 end) as maifang_num, " + " max(case stype when  'zinv' then new_user_num else 0 end) as zinv_num, "
				+ " max(case stype when  'jiehun' then new_user_num else 0 end) as jiehun_num," + " max(case stype when  'duanqi' then new_user_num else 0 end) as duanqi_num " + " from qj_statis_db.s_user_num sun " + " where 1=1 ";

		sql += " group by week_end,week_start )t where 1=1 ";

		if (!"".equals(StringUtil.trimNull(newOld.getSdate()))) {
			sql += " and date >='" + newOld.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(newOld.getEdate()))) {
			sql += " and date<='" + newOld.getEdate() + "'";
		}

		String key = newOld.getSortedConditions().keySet().iterator().next();
		String value = newOld.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + newOld.getFirstResult() + "," + newOld.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<NewOld> listOperates = new ArrayList<NewOld>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			NewOld op = new NewOld();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setTotal_num(StringUtil.trimNull(o[1]));
			op.setNew_num(StringUtil.trimNull(o[2]));
			op.setCashbao_num(StringUtil.trimNull(o[3]));
			op.setSingle_num(StringUtil.trimNull(o[4]));
			op.setJinqu_num(StringUtil.trimNull(o[5]));
			op.setBaoshou_num(StringUtil.trimNull(o[6]));
			op.setWenjian_num(StringUtil.trimNull(o[7]));
			op.setLicai_num(StringUtil.trimNull(o[8]));
			op.setLicai2_num(StringUtil.trimNull(o[9]));
			op.setYanglao_num(StringUtil.trimNull(o[10]));
			op.setMaifang_num(StringUtil.trimNull(o[11]));
			op.setZinv_num(StringUtil.trimNull(o[12]));
			op.setJiehun_num(StringUtil.trimNull(o[13]));
			op.setDuanqi_num(StringUtil.trimNull(o[14]));
			listOperates.add(op);
		}
		return listOperates;
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelListCount</p> <p>Description: </p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelListCount(com.forestry.model.
	 * sys.Channel)
	 */
	@Override
	public long getNewOldListCount(NewOld newOld) {
		// TODO Auto-generated method stub
		String sql = "select count(1) from (select * From ( select    CONCAT(week_start,'_',week_end) as date,total_user_num as total_num,sum(new_user_num) as new_num, " + " max(case stype when  'cashbao' then new_user_num  else 0 end) as cashbao_num, "
				+ " max(case stype when  'single' then new_user_num else 0 end) as single_num ," + " max(case stype when  'jinqu' then new_user_num else 0 end) as jinqu_num ," + " max(case stype when  'baoshou' then new_user_num else 0 end) as baoshou_num ,"
				+ " max(case stype when  'wenjian' then new_user_num else 0 end) as wenjian_num ," + " max(case stype when  'licai' then new_user_num else 0 end) as licai_num, " + " max(case stype when  'licai2' then new_user_num else 0 end) as licai2_num,"
				+ " max(case stype when  'yanglao' then new_user_num else 0 end) as yanglao_num," + " max(case stype when  'maifang' then new_user_num else 0 end) as maifang_num, " + " max(case stype when  'zinv' then new_user_num else 0 end) as zinv_num, "
				+ " max(case stype when  'jiehun' then new_user_num else 0 end) as jiehun_num," + " max(case stype when  'duanqi' then new_user_num else 0 end) as duanqi_num " + " from qj_statis_db.s_user_num sun " + " where 1=1 ";

		sql += " group by week_end,week_start )a where 1=1 ";

		if (!"".equals(StringUtil.trimNull(newOld.getSdate()))) {
			sql += " and date >='" + newOld.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(newOld.getEdate()))) {
			sql += " and date<='" + newOld.getEdate() + "'";
		}

		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelList</p> <p>渠道统计</p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelList(com.forestry.model.sys.
	 * Channel)
	 */
	@Override
	public List<Money> getMoneyList(Money money) {
		// TODO Auto-generated method stub

		String sql = " select * From ( " + " select  s_date date," + " max(case schema_type when  'cashbao' then money_num  else 0 end) as cashbao_num," + " max(case schema_type when  'single' then money_num else 0 end) as single_num ,  "
				+ " max(case schema_type when  'jinqu' then money_num else 0 end) as jinqu_num ,    " + " max(case schema_type when  'baoshou' then money_num else 0 end) as baoshou_num ," + " max(case schema_type when  'wenjian' then money_num else 0 end) as wenjian_num ,"
				+ " max(case schema_type when  'licai' then money_num else 0 end) as licai_num,     " + " max(case schema_type when  'licai2' then money_num else 0 end) as licai2_num,  " + " max(case schema_type when  'yanglao' then money_num else 0 end) as yanglao_num, "
				+ " max(case schema_type when  'maifang' then money_num else 0 end) as maifang_num, " + " max(case schema_type when  'zinv' then money_num else 0 end) as zinv_num,       " + " max(case schema_type when  'jiehun' then money_num else 0 end) as jiehun_num,   "
				+ " max(case schema_type when  'duanqi' then money_num else 0 end) as duanqi_num    " + " from qj_statis_db.s_date_stype " + " group by s_date  " + " ) t where 1=1 ";

		if (!"".equals(StringUtil.trimNull(money.getSdate()))) {
			sql += " and date >='" + money.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(money.getEdate()))) {
			sql += " and date<='" + money.getEdate() + "'";
		}

		String key = money.getSortedConditions().keySet().iterator().next();
		String value = money.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + money.getFirstResult() + "," + money.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Money> listOperates = new ArrayList<Money>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Money op = new Money();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setCashbao_num(StringUtil.trimNull(o[1]));
			op.setSingle_num(StringUtil.trimNull(o[2]));
			op.setJinqu_num(StringUtil.trimNull(o[3]));
			op.setBaoshou_num(StringUtil.trimNull(o[4]));
			op.setWenjian_num(StringUtil.trimNull(o[5]));
			op.setLicai_num(StringUtil.trimNull(o[6]));
			op.setLicai2_num(StringUtil.trimNull(o[7]));
			op.setYanglao_num(StringUtil.trimNull(o[8]));
			op.setMaifang_num(StringUtil.trimNull(o[9]));
			op.setZinv_num(StringUtil.trimNull(o[10]));
			op.setJiehun_num(StringUtil.trimNull(o[11]));
			op.setDuanqi_num(StringUtil.trimNull(o[12]));
			listOperates.add(op);
		}
		return listOperates;
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelListCount</p> <p>Description: </p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelListCount(com.forestry.model.
	 * sys.Channel)
	 */
	@Override
	public long getMoneyListCount(Money money) {
		// TODO Auto-generated method stub
		String sql = "select count(1) From ( select * From ( " + " select  s_date date," + " max(case schema_type when  'cashbao' then money_num  else 0 end) as cashbao_num," + " max(case schema_type when  'single' then money_num else 0 end) as single_num ,  "
				+ " max(case schema_type when  'jinqu' then money_num else 0 end) as jinqu_num ,    " + " max(case schema_type when  'baoshou' then money_num else 0 end) as baoshou_num ," + " max(case schema_type when  'wenjian' then money_num else 0 end) as wenjian_num ,"
				+ " max(case schema_type when  'licai' then money_num else 0 end) as licai_num,     " + " max(case schema_type when  'licai2' then money_num else 0 end) as licai2_num_,  " + " max(case schema_type when  'yanglao' then money_num else 0 end) as yanglao_num, "
				+ " max(case schema_type when  'maifang' then money_num else 0 end) as maifang_num, " + " max(case schema_type when  'zinv' then money_num else 0 end) as zinv_num,       " + " max(case schema_type when  'jiehun' then money_num else 0 end) as jiehun_num,   "
				+ " max(case schema_type when  'duanqi' then money_num else 0 end) as duanqi_num    " + " from qj_statis_db.s_date_stype                                                  " + "                                                                                 "
				+ " group by s_date                                                                 " + " ) t where 1=1                                                ";

		if (!"".equals(StringUtil.trimNull(money.getSdate()))) {
			sql += " and date >='" + money.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(money.getEdate()))) {
			sql += " and date<='" + money.getEdate() + "'";
		}

		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	/**
	 * 统计保有量
	 */
	@Override
	public void statistic_day_inventory() {
		// TODO Auto-generated method stub
		Connection conn = null;
	 	PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Operate> listInvestData = new ArrayList<Operate>();
		try {
			conn = DBUtil.getConnection();
				Operate id = new Operate();
				String currentDate = DateUtil.currentTimeMinus(1);
				//System.out.println(currentDate);
			 String sql = " replace into qj_qjs_db.day_inventory(date,fdcode,fdname,partner,inventory) "
					 +" select '"+currentDate+"', t.fdcode,ff.`name`,t.partner,t.inventory from ("
					 +" select  a.fdcode,a.fdname ,ifnull(p.pname,'qianjing') as partner,a.inventory from ( "
					 +" select fdcode ,fdname,partner,  round(sum(inventory),2)     as inventory from ("
					 +" select fdcode,abbrev as fdname, partner ,ifnull(sum(shares*nav ),0) as inventory  from qj_fund_db.assets  group by fdcode ,partner  "
					 +" union all"
					 +" select fdcode,abbrev as fdname, 0 as partner ,ifnull(sum(shares*1 ),0) as inventory  from qj_cashbao_db.assets  group by fdcode  "
					 +" )t group by fdcode,fdname,partner"
					 +" )a left join qj_user_db.partner p on (a.partner= p.pno)) t"
					 +" left join qj_fund_db.fund_info ff on (t.fdcode=ff.fdcode) ";
			 getSession().createSQLQuery(sql).executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pstm, rs);
		} 

	}
	
	
	@Override
	public List<Inventory> getInventoryList(Inventory inventory) {
		// TODO Auto-generated method stub
		
		int day=DateUtil.getDayCount("1970-01-01", DateUtil.currentMonthMinus(0));;
		if(!"".equals(StringUtil.trimNull(inventory.getSdate()))&&!"".equals(StringUtil.trimNull(inventory.getEdate()))){
			 day = DateUtil.getDayCount(inventory.getSdate(), inventory.getEdate());
		}
		
		String sql  =" select  fdcode,fdname,"
						+" case when partner  = 'baigujing' then '白骨精'  "   
						+"      when partner= 'qianjing' then '钱景'   "
						+"      when partner= 'minxindai' then '民信贷'  "
						+"      when partner= 'yinlaicaifu' then '银来财富'  "
						+"      when partner= 'jintaipingyang' then '金太平洋' "
						+"      when partner= 'stockradar' then '股票雷达'   "
						+" end as partner "
						+" ,round(ifnull(sum(inventory),0)/"+day+",2) as inventory ,round(ifnull(sum(inventory),0),2) as sum From qj_qjs_db.day_inventory where "
						+ " 1=1 ";

		if (!"".equals(StringUtil.trimNull(inventory.getSdate()))) {
			sql += " and date >='" + inventory.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(inventory.getEdate()))) {
			sql += " and date<='" + inventory.getEdate() + "'";
		}
		
		
		if (!"".equals(StringUtil.trimNull(inventory.get$like_platform()))) {
			sql += " and partner = '" + inventory.get$like_platform() + "'";
		}

		if (!"".equals(StringUtil.trimNull(inventory.get$like_fdCode()))) {
			sql += " and fdcode = '" + inventory.get$like_fdCode() + "'";
		}
		
		sql+=" group by fdcode,fdname,partner";
		String key = inventory.getSortedConditions().keySet().iterator().next();
		String value = inventory.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + inventory.getFirstResult() + "," + inventory.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Inventory> listOperates = new ArrayList<Inventory>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Inventory op = new Inventory();
			Object[] o = (Object[]) list.get(i);
			op.setFdCode(StringUtil.trimNull(o[0]));
			op.setFdName(StringUtil.trimNull(o[1]));
			op.setPartner(StringUtil.trimNull(o[2]));
			op.setInventory(StringUtil.trimNull(o[3]));
			op.setSum(StringUtil.trimNull(o[4]));
			listOperates.add(op);
		}
		return listOperates;
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelListCount</p> <p>Description: </p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelListCount(com.forestry.model.
	 * sys.Channel)
	 */
	@Override
	public long getInventoryListCount(Inventory inventory) {
		// TODO Auto-generated method stub
		int day=DateUtil.getDayCount("1970-01-01", DateUtil.currentMonthMinus(0));;
		if(!"".equals(StringUtil.trimNull(inventory.getSdate()))&&!"".equals(StringUtil.trimNull(inventory.getEdate()))){
			 day = DateUtil.getDayCount(inventory.getSdate(), inventory.getEdate());
		}
		
		String sql  =" select count(1) From (select  fdcode,fdname,"
						+" case when partner  = 'baigujing' then '白骨精'  "   
						+"      when partner= 'qianjing' then '钱景'   "
						+"      when partner= 'minxindai' then '民信贷'  "
						+"      when partner= 'yinlaicaifu' then '银来财富'  "
						+"      when partner= 'jintaipingyang' then '金太平洋' "
						+"      when partner= 'stockradar' then '股票雷达'   "
						+" end as partner "
						+" ,round(ifnull(sum(inventory),0)/"+day+",2) as inventory  From qj_qjs_db.day_inventory where "
						+ " 1=1 ";

		if (!"".equals(StringUtil.trimNull(inventory.getSdate()))) {
			sql += " and date >='" + inventory.getSdate() + "'";
		}

		if (!"".equals(StringUtil.trimNull(inventory.getEdate()))) {
			sql += " and date<='" + inventory.getEdate() + "'";
		}
		
		
		if (!"".equals(StringUtil.trimNull(inventory.get$like_platform()))) {
			sql += " and partner = '" + inventory.get$like_platform() + "'";
		}

		if (!"".equals(StringUtil.trimNull(inventory.get$like_fdCode()))) {
			sql += " and fdcode ='" + inventory.get$like_fdCode() + "'";
		}
		
		sql+=" group by fdcode,fdname,partner";

		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}
	
	
	
	@Override
	public List<Inventory> getInventoryDetailList(Inventory inventory) {
		// TODO Auto-generated method stub
		String sql = " select date,round(inventory,2) as inventory From qj_qjs_db.day_inventory where 1=1 ";
		if (!"".equals(StringUtil.trimNull(inventory.get$like_platform()))) {
			sql += " and partner = '" + inventory.get$like_platform() + "'";
		}

		if (!"".equals(StringUtil.trimNull(inventory.get$like_fdCode()))) {
			sql += " and fdcode = '" + inventory.get$like_fdCode() + "'";
		}

		String key = inventory.getSortedConditions().keySet().iterator().next();
		String value = inventory.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + inventory.getFirstResult() + "," + inventory.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Inventory> listOperates = new ArrayList<Inventory>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Inventory op = new Inventory();
			Object[] o = (Object[]) list.get(i);
			op.setDate(StringUtil.trimNull(o[0]));
			op.setInventory(StringUtil.trimNull(o[1]));
			listOperates.add(op);
		}
		return listOperates;
	}

	/*
	 * (非 Javadoc) <p>Title: getChannelListCount</p> <p>Description: </p>
	 * 
	 * @param channel
	 * 
	 * @return
	 * 
	 * @see
	 * com.forestry.dao.sys.OperateDao#getChannelListCount(com.forestry.model.
	 * sys.Channel)
	 */
	@Override
	public long getInventoryDetailListCount(Inventory inventory) {
		// TODO Auto-generated method stub
		String sql = " select count(1) from (select date,inventory From qj_qjs_db.day_inventory where 1=1 ";

		if (!"".equals(StringUtil.trimNull(inventory.get$like_platform()))) {
			sql += " and partner = '" + inventory.get$like_platform() + "'";
		}

		if (!"".equals(StringUtil.trimNull(inventory.get$like_fdCode()))) {
			sql += " and fdcode = '" + inventory.get$like_fdCode() + "'";
		}

		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}
	
	@Override
	public List<Inventory> ExportInventoryList(Inventory inventory) {
		// TODO Auto-generated method stub

		String sql = " select    fdcode,fdname "; 
	
	

		List<String> everyDay = DateUtil.getMonth(inventory.getSdate(), inventory.getEdate());
        
		for(int i=0;i<everyDay.size();i++){
			String day=everyDay.get(i);
			sql +=", max(case date when  '"+day+"' then inventory  else 0 end) as  '"+day+"' ";
		}
		sql+= " from qj_qjs_db.day_inventory where 1=1  ";
		
		if (!"".equals(StringUtil.trimNull(inventory.get$like_platform()))) {
			sql += " and partner = '" + inventory.get$like_platform() + "'";
		}
 
		if (!"".equals(StringUtil.trimNull(inventory.get$like_fdCode()))) {
			sql += " and fdcode ='" + inventory.get$like_fdCode() + "'";
		}
		
		sql += " group by fdcode,fdname";
		String key = inventory.getSortedConditions().keySet().iterator().next();
		String value = inventory.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + inventory.getFirstResult() + "," + inventory.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<Inventory> listOperates = new ArrayList<Inventory>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Inventory op = new Inventory();
			Object[] o = (Object[]) list.get(i);
			op.setFdCode(StringUtil.trimNull(o[0]));
			op.setFdName(StringUtil.trimNull(o[1]));
			List<String> day= new ArrayList<String>();
			List<String> dayInventory = new ArrayList<String>();
			for(int j=0;j<everyDay.size();j++){
				String d = everyDay.get(j);
				day.add(d);
				dayInventory.add(StringUtil.trimNull(o[j+2]));
			}
			op.setDay(day);
			op.setDayInventory(dayInventory);
			listOperates.add(op);
		}
		return listOperates;
	}
	
	  
	
	public String getPartner(String platform) {
		String partner = "0";

		if ("qianjing".equals(platform)) {
			partner = "0";
		} else {
			String sql = "select pno from qj_user_db.partner where pname='" + platform + "'";
			Query query = getSession().createSQLQuery(sql);
			List list = query.list();
			if(list.size()!=0){
				partner = StringUtil.trimNull(list.get(0));
			}
		}
		return partner;
	}
	
	@Override
	public float getSettleRate(){
		float settleRate=1;
		String sql = "select id,title,rate from qj_qjs_db.rate_config  order by id asc";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
	 
	 
		float cargoRate=0;
		float nonCargoRate=0;
		float nonCargoSupRate=0;
		float accessAuthRate=0;
		 
		cargoRate = Float.parseFloat( StringUtil.trimNull(((Object[]) list.get(0))[2]))/100;
		nonCargoRate = Float.parseFloat(StringUtil.trimNull( ((Object[]) list.get(1))[2]))/100;
		nonCargoSupRate = Float.parseFloat(StringUtil.trimNull(((Object[]) list.get(2))[2]))/100;
		accessAuthRate = Float.parseFloat(StringUtil.trimNull(((Object[]) list.get(3))[2]));
		settleRate=Float.parseFloat(StringUtil.trimNull(((Object[]) list.get(4))[2]))/100;
	 
		
		return settleRate;
	}
	
	
	@Override
	public List<Settle> getSettleList(Settle settle) {
		// TODO Auto-generated method stub

		int day=DateUtil.getDayCount("1970-01-01", DateUtil.currentMonthMinus(0));;
		if(!"".equals(StringUtil.trimNull(settle.getSdate()))&&!"".equals(StringUtil.trimNull(settle.getEdate()))){
			 day = DateUtil.getDayCount(settle.getSdate(), settle.getEdate());
		}
		
		String sql = "select id,title,rate from qj_qjs_db.rate_config  order by id asc";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		float cargoRate=0;
		float nonCargoRate=0;
		float nonCargoSupRate=0;
	 
		cargoRate = Float.parseFloat( StringUtil.trimNull(((Object[]) list.get(0))[2]))/100;
		nonCargoRate = Float.parseFloat(StringUtil.trimNull( ((Object[]) list.get(1))[2]))/100;
		nonCargoSupRate = Float.parseFloat(StringUtil.trimNull(((Object[]) list.get(2))[2]))/100;
		
		settle.set$like_platform1(getPartner(settle.get$like_platform()));
		
		
		 if("1".equals(settle.get$like_type())){
			 sql=" select fdcode,fdname,round(ifnull(inventory,0)/"+day+",2),ifnull(inventory,0),ifnull(suml6,0),ifnull(counts6,0),ifnull(invest,0),ifnull(countl5w,0),ifnull(counts5w,0),ifnull(fees,0),ifnull(sale,0),ifnull(manage,0),ifnull(pay,0),ifnull(supfee,0),                                         "
					 +" ifnull(round(sale+manage-pay-supfee,2),0) from (                         "
					 +" select `data`.fdcode ,data.fdname ,`data`.inventory as inventory,  "
					 + "t4.suml6  as suml6, t5.counts6  as counts6,                                                    "
					 +"  t1.invest  as invest,ifnull(t2.countl5w,0) as countl5w ,ifnull(t3.counts5w,0) as counts5w , 0 as fees,                  "
					 +" round(ifnull(inventory*fr.sellrate/100/365,0),2) as sale,round(ifnull(inventory*fi.manageFee/100/365*fr.managerate/100,0),2) as manage,"
					 +"  round(ifnull(t4.suml6 *"+cargoRate+",0)+t5.counts6*2,2) as pay,ifnull(counts5w*2,0)+ifnull(countl5w*5.5,0) as supFee from                                "
					 +" (select fdcode,fdname ,sum(inventory) as 'inventory',partner from qj_qjs_db.day_inventory                              "
					 +" where partner = '"+settle.get$like_platform()+"' and date BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59' group by fdcode, partner)data          "
					 +" left join ( select fdcode,ifnull(sum(sum),0) as 'invest' from  qj_fund_db.operat_log                                             "
					 +" where opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                     "
					 +" and (operate=1 or operate=4)       "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                                                                    
					 +" and state in (1,3)                                                                                                     "
					 +" group by fdcode,partner) t1 on (t1.fdcode=data.fdcode)                                                                         "
					 +" left join                                                                                                              "
					 +" ( select fdcode,count(1) as countl5w   from  qj_fund_db.operat_log                                                     "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                    "
					 +" and  operate=2                                                                                                         "
					 +" and  state in (2,3)   "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"
					 +" and  sum >50000                                                                                                        "
					 +" group by fdcode,partner                                                                                                        "
					 +" )t2 on(`data`.fdcode=t2.fdcode)                                                                                        "
					 +" left join                                                                                                              "
					 +" ( select fdcode,count(1) as counts5w   from  qj_fund_db.operat_log                                                     "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                    "
					 +" and  operate=2                                                                                                         "
					 +" and  state in (2,3)                                                                                                    "
					 +" and  sum <=50000           "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                            
					 +" group by fdcode ,partner                                                                                                              "
					 +")t3 on(`data`.fdcode=t3.fdcode)  "
					 +" left join                                                                                                              "
					 +" ( select fdcode,ifnull(sum(sum),0) as suml6   from  qj_fund_db.operat_log                                                     "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                    "
					 +" and  (operate=1 or operate=4)                                                                                                         "
					 +" and  state in (1,3)                                                                                                    "
					 +" and  sum >=6666.67           "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                            
					 +" group by fdcode ,partner                                                                                                              "
					 +")t4 on(`data`.fdcode=t4.fdcode) "
					 +" left join                                                                                                              "
					 +" ( select fdcode,count(1) as counts6   from  qj_fund_db.operat_log                                                     "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                    "
					 +" and  (operate=1 or operate=4)                                                                                                         "
					 +" and  state in (1,3)                                                                                                    "
					 +" and  sum <6666.67          "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                            
					 +" group by fdcode ,partner                                                                                                              "
					 +")t5 on(`data`.fdcode=t5.fdcode)                                                                                              "
					 +" left  join qj_qjs_db.fund_rate fr on(`data`.fdcode=fr.fdcode)                                                          "
					 +" left join qj_fund_db.fund_info fi on (`data`.fdcode=fi.fdcode)                                                         "
					 +"  where fr.type=1                                                                                                       "
					 +"  and fi.type=3                                                                                                         "
					 +")s                                                                                                                      ";
		 }else{
			 sql = " select fdcode,fdname,round(ifnull(inventory,0)/"+day+",2),ifnull(inventory,0),ifnull(suml6,0),ifnull(counts6,0),ifnull(invest,0),ifnull(countl5w,0),ifnull(counts5w,0),ifnull(fees,0),ifnull(sale,0),ifnull(manage,0),ifnull(pay,0),ifnull(supfee,0),                                     "
					 +"  ifnull(round(sale+fees+manage-pay-supfee,2),0)  from (                      "
					 +" select `data`.fdcode ,data.fdname ,`data`.inventory as inventory,    "
					 + "t4.suml6 as suml6,t5.counts6 as counts6,  "
					 +" t1.invest as invest,ifnull(t2.countl5w,0) as countl5w ,ifnull(t3.counts5w,0) as counts5w ,           "
					 + "round(ifnull(t1.fees,0),2) as fees,     "
					 + "round(ifnull(inventory*fr.sellrate/100/365,0),2) as sale ,"
					 +" round(ifnull(inventory*fi.manageFee/100*fr.managerate/100,0)/"+365+",2) as manage,     "
					 +"  round(ifnull(t1.invest,0)*"+nonCargoRate+",2) as pay,ifnull(counts5w*2,0)+ifnull(countl5w*5.5,0)+t1.invest*"+nonCargoSupRate+" as supFee   from              "
					 +" (select fdcode,fdname ,sum(inventory) as 'inventory',partner from qj_qjs_db.day_inventory                           "
					 +"  where partner = '"+settle.get$like_platform()+"' and date BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59' group by fdcode, partner)data      "
					 +" left join ( select fdcode,ifnull(sum(sum),0) as 'invest' ,sum(fee) as fees from  qj_fund_db.operat_log                                          "
					 +" where opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                  "
					 +" and (operate=1 or operate=4)         "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                                                               
					 +" and state in (1,3)                                                                                                  "
					 +" group by fdcode,partner ) t1 on (t1.fdcode=data.fdcode)                                                                      "
					 +" left join                                                                                                           "
					 +"  ( select fdcode,count(1) as countl5w   from  qj_fund_db.operat_log                                                 "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                 "
					 +" and  operate=2                                                                                                      "
					 +" and  state in (2,3)                                                                                                 "
					 +" and  sum >50000     "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                                                                 
					 +" group by fdcode  ,partner                                                                                                          "
					 +" )t2 on(`data`.fdcode=t2.fdcode)                                                                                     "
					 +" left join                                                                                                           "
					 +"  ( select fdcode,count(1) as counts5w   from  qj_fund_db.operat_log                                                 "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                 "
					 +" and  operate=2                                                                                                      "
					 +" and  state in (2,3)                                                                                                 "
					 +" and  sum <=50000   "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                                                                  
					 +" group by fdcode,partner                                                                                                            "
					 +" )t3 on(`data`.fdcode=t3.fdcode)   "
					 +" left join                                                                                                              "
					 +" ( select fdcode,ifnull(sum(sum),0) as suml6   from  qj_fund_db.operat_log                                                     "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                    "
					 +" and  (operate=1 or operate=4)                                                                                                         "
					 +" and  state in (1,3)                                                                                                    "
					 +" and  sum >=6666.67           "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                            
					 +" group by fdcode ,partner                                                                                                              "
					 +")t4 on(`data`.fdcode=t4.fdcode) "
					 +" left join                                                                                                              "
					 +" ( select fdcode,count(1) as counts6   from  qj_fund_db.operat_log                                                     "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                    "
					 +" and  (operate=1 or operate=4)                                                                                                         "
					 +" and  state in (1,3)                                                                                                    "
					 +" and  sum <6666.67          "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                            
					 +" group by fdcode ,partner                                                                                                              "
					 +")t5 on(`data`.fdcode=t5.fdcode)        "
					 +"  left  join qj_qjs_db.fund_rate fr on(`data`.fdcode=fr.fdcode)                                                      "
					 +" left join qj_fund_db.fund_info fi on (`data`.fdcode=fi.fdcode)                                                      "
					 +"   where fr.type=0                                                                                                   "
					 +"   and fi.type<>3                                                                                                    "
					 +" )s                                                                                                                  ";
		 }
		 
		if (!"".equals(StringUtil.trimNull(settle.get$like_fdCode()))) {
			sql += " where fdcode = '" + settle.get$like_fdCode() + "'";
		}
		 

		String key = settle.getSortedConditions().keySet().iterator().next();
		String value = settle.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + settle.getFirstResult() + "," + settle.getMaxResults();
		//System.out.println(sql);  
		query = getSession().createSQLQuery(sql);
		  list = query.list();
		List<Settle> listOperates = new ArrayList<Settle>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Settle op = new Settle();
			Object[] o = (Object[]) list.get(i);
			op.setFdCode(StringUtil.trimNull(o[0]));
			op.setFdName(StringUtil.trimNull(o[1]));
			op.setDayInventory(StringUtil.trimNull(o[2]));
			op.setInventory(StringUtil.trimNull(o[3]));
			op.setSuml6(StringUtil.trimNull(o[4]));
			op.setCounts6(StringUtil.trimNull(o[5]));
			op.setInvest(StringUtil.trimNull(o[6]));
			op.setRedempl5w(StringUtil.trimNull(o[7]));
			op.setRedemps5w(StringUtil.trimNull(o[8]));
			op.setFees(StringUtil.trimNull(o[9]));
			op.setSaleFee(StringUtil.trimNull(o[10]));
			op.setManageFee(StringUtil.trimNull(o[11]));
			op.setPayCost(StringUtil.trimNull(o[12]));
			op.setSupFee(StringUtil.trimNull(o[13]));
			op.setTechFee(StringUtil.trimNull(o[14]));
			listOperates.add(op);
		}
		return listOperates;
	} 
	
	@Override
	public long getSettleListCount(Settle settle) {
		// TODO Auto-generated method stub
		int day=DateUtil.getDayCount("1970-01-01", DateUtil.currentMonthMinus(0));;
		if(!"".equals(StringUtil.trimNull(settle.getSdate()))&&!"".equals(StringUtil.trimNull(settle.getEdate()))){
			 day = DateUtil.getDayCount(settle.getSdate(), settle.getEdate());
		}
		
		String sql = "select id,title,rate from qj_qjs_db.rate_config  order by id asc";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		float cargoRate=0;
		float nonCargoRate=0;
		float nonCargoSupRate=0;
	 
		cargoRate = Float.parseFloat( StringUtil.trimNull( ((Object[]) list.get(0))[2]))/100;
		nonCargoRate = Float.parseFloat( StringUtil.trimNull(((Object[]) list.get(1))[2]))/100;
		nonCargoSupRate = Float.parseFloat( StringUtil.trimNull(((Object[]) list.get(2))[2]))/100;
		 
		settle.set$like_platform1(getPartner(settle.get$like_platform()));
		
		 if("1".equals(settle.get$like_type())){
			 sql=" select count(1) from (select fdcode,fdname,round(inventory/"+day+",6),inventory,invest,countl5w,counts5w,sale,manage,pay,supfee,                                         "
					 +" case when (sale+manage-pay-supfee)*0.7 <0 then (sale+manage-pay-supfee)*0.7 else  (sale+manage-pay-supfee)*0.7 end from (                         "
					 +" select `data`.fdcode ,data.fdname ,`data`.inventory as inventory,                                                      "
					 +" ifnull(t1.invest,0) as invest,ifnull(t2.countl5w,0) as countl5w ,ifnull(t3.counts5w,0) as counts5w ,                   "
					 +" round(ifnull(inventory*fr.sellrate/100/365,0),6) as sale,round(ifnull(inventory*fi.manageFee/100/365*fr.managerate/100,0),6) as manage,"
					 +" ifnull(inventory,0)*"+cargoRate+"/365  as pay,ifnull(counts5w*2,0)+ifnull(countl5w*5.5,0) as supFee from                                "
					 +" (select fdcode,fdname ,sum(inventory) as 'inventory',partner from qj_qjs_db.day_inventory                              "
					 +" where partner = '"+settle.get$like_platform()+"' and date BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59' group by fdcode, partner)data          "
					 +" left join ( select fdcode,sum(sum) as 'invest' from  qj_fund_db.operat_log                                             "
					 +" where opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                     "
					 +" and (operate=1 or operate=4)       "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                                                                    
					 +" and state in (1,3)                                                                                                     "
					 +" group by fdcode,partner) t1 on (t1.fdcode=data.fdcode)                                                                         "
					 +" left join                                                                                                              "
					 +" ( select fdcode,count(1) as countl5w   from  qj_fund_db.operat_log                                                     "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                    "
					 +" and  operate=2                                                                                                         "
					 +" and  state in (2,3)   "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"
					 +" and  sum >50000                                                                                                        "
					 +" group by fdcode,partner                                                                                                        "
					 +" )t2 on(`data`.fdcode=t2.fdcode)                                                                                        "
					 +" left join                                                                                                              "
					 +" ( select fdcode,count(1) as counts5w   from  qj_fund_db.operat_log                                                     "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                    "
					 +" and  operate=2                                                                                                         "
					 +" and  state in (2,3)                                                                                                    "
					 +" and  sum <=50000           "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                            
					 +" group by fdcode ,partner                                                                                                              "
					 +")t3 on(`data`.fdcode=t3.fdcode)                                                                                         "
					 +" left  join qj_qjs_db.fund_rate fr on(`data`.fdcode=fr.fdcode)                                                          "
					 +" left join qj_fund_db.fund_info fi on (`data`.fdcode=fi.fdcode)                                                         "
					 +"  where fr.type=1                                                                                                       "
					 +"  and fi.type=3                                                                                                         "
					 +")s                                                                                                                      ";
		 }else{
			 sql = "select count(1) from ( select fdcode,fdname,round(inventory/"+day+",6),inventory,invest,countl5w,counts5w,sale,manage,pay,supfee,                                     "
					 +" case when (sale+manage-pay-supfee)*0.7 <0 then  (sale+manage-pay-supfee)*0.7 else  (sale+manage-pay-supfee)*0.7 end from (                      "
					 +" select `data`.fdcode ,data.fdname ,`data`.inventory as inventory,                                                   "
					 +" ifnull(t1.invest,0) as invest,ifnull(t2.countl5w,0) as countl5w ,ifnull(t3.counts5w,0) as counts5w ,                "
					 +" round(ifnull(t1.fees,0),6) as sale,round(ifnull(inventory*fi.manageFee/100*fr.managerate/100,0)/"+365+",6) as manage,     "
					 +"  ifnull(t1.invest,0)*"+nonCargoRate+" as pay,ifnull(counts5w*2,0)+ifnull(countl5w*5.5,0)+t1.invest*"+nonCargoSupRate+" as supFee   from              "
					 +" (select fdcode,fdname ,sum(inventory) as 'inventory',partner from qj_qjs_db.day_inventory                           "
					 +"  where partner = '"+settle.get$like_platform()+"' and date BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59' group by fdcode, partner)data      "
					 +" left join ( select fdcode,sum(sum) as 'invest' ,sum(fee) as fees from  qj_fund_db.operat_log                                          "
					 +" where opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                  "
					 +" and (operate=1 or operate=4)         "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                                                               
					 +" and state in (1,3)                                                                                                  "
					 +" group by fdcode,partner ) t1 on (t1.fdcode=data.fdcode)                                                                      "
					 +" left join                                                                                                           "
					 +"  ( select fdcode,count(1) as countl5w   from  qj_fund_db.operat_log                                                 "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                 "
					 +" and  operate=2                                                                                                      "
					 +" and  state in (2,3)                                                                                                 "
					 +" and  sum >50000     "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                                                                 
					 +" group by fdcode  ,partner                                                                                                          "
					 +" )t2 on(`data`.fdcode=t2.fdcode)                                                                                     "
					 +" left join                                                                                                           "
					 +"  ( select fdcode,count(1) as counts5w   from  qj_fund_db.operat_log                                                 "
					 +" where  opDate BETWEEN '"+settle.getSdate()+" 00:00:00' and '"+settle.getEdate()+" 23:59:59'                                                                 "
					 +" and  operate=2                                                                                                      "
					 +" and  state in (2,3)                                                                                                 "
					 +" and  sum <=50000   "
					 + "and  partner=  '"+settle.get$like_platform1()+"'"                                                                                                  
					 +" group by fdcode,partner                                                                                                            "
					 +" )t3 on(`data`.fdcode=t3.fdcode)                                                                                     "
					 +"  left  join qj_qjs_db.fund_rate fr on(`data`.fdcode=fr.fdcode)                                                      "
					 +" left join qj_fund_db.fund_info fi on (`data`.fdcode=fi.fdcode)                                                      "
					 +"   where fr.type=0                                                                                                   "
					 +"   and fi.type<>3                                                                                                    "
					 +" )s                                                                                                                  ";
		 }
		 
		if (!"".equals(StringUtil.trimNull(settle.get$like_fdCode()))) {
			sql += " where fdcode = '" + settle.get$like_fdCode() + "'";
		}
		 
		sql += "    )t";

		  query = getSession().createSQLQuery(sql);
		  list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}
	
	@Override
	public List<Settle> getAcessFeeList(Settle settle) {
		// TODO Auto-generated method stub

		int day=DateUtil.getDayCount("1970-01-01", DateUtil.currentMonthMinus(0));;
		if(!"".equals(StringUtil.trimNull(settle.getSdate()))&&!"".equals(StringUtil.trimNull(settle.getEdate()))){
			 day = DateUtil.getDayCount(settle.getSdate(), settle.getEdate());
		}
		
		String sql = "select id,title,rate from qj_qjs_db.rate_config  order by id asc";
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		float cargoRate=0;
		float nonCargoRate=0;
		float nonCargoSupRate=0;
		float accessAuthRate=0;
		
		cargoRate = Float.parseFloat( StringUtil.trimNull(((Object[]) list.get(0))[2]))/100;
		nonCargoRate = Float.parseFloat(StringUtil.trimNull( ((Object[]) list.get(1))[2]))/100;
		nonCargoSupRate = Float.parseFloat(StringUtil.trimNull(((Object[]) list.get(2))[2]))/100;
		accessAuthRate = Float.parseFloat(StringUtil.trimNull(((Object[]) list.get(3))[2]));
		
		//settle.set$like_platform1(getPartner(settle.get$like_platform()));
		
		if("qianjing".equals(settle.get$like_platform())){
			sql="select ifnull(sum(bound_num),0) as num,ifnull(sum(bound_num),0) *"+accessAuthRate+" from qj_statis_db.s_date_channel_timely where channel not in (select pname from qj_user_db.partner) and  s_date BETWEEN '"+settle.getSdate()+"' and '"+settle.getEdate()+"'";
		}else{
			sql="select ifnull(sum(bound_num),0) as num,ifnull(sum(bound_num),0) *"+accessAuthRate+" from qj_statis_db.s_date_channel_timely where channel ='"+settle.get$like_platform()+"' and  s_date BETWEEN '"+settle.getSdate()+"' and '"+settle.getEdate()+"'";
		}

		String key = settle.getSortedConditions().keySet().iterator().next();
		String value = settle.getSortedConditions().values().iterator().next();
		//sql += "     order by " + key + " " + value + " limit " + settle.getFirstResult() + "," + settle.getMaxResults();
		//System.out.println(sql);  
		query = getSession().createSQLQuery(sql);
		  list = query.list();
		List<Settle> listOperates = new ArrayList<Settle>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			Settle op = new Settle();
			Object[] o = (Object[]) list.get(i);
			op.setBoundNum(StringUtil.trimNull(o[0]));
			op.setAccessAuthFee(StringUtil.trimNull(o[1]));
			listOperates.add(op);
		}
		return listOperates;
	} 

}
