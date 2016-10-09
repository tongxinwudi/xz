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
import com.forestry.dao.sys.MemberDao;
import com.forestry.dao.sys.OperateDao;
import com.forestry.dao.sys.PersonalDao;
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
import com.forestry.model.sys.PersonalInfo;
import com.forestry.model.sys.PersonalInvest;
import com.forestry.model.sys.Profit;
import com.forestry.model.sys.TradeUser;
import com.forestry.model.sys.param.TradeUserParameter;

 
import core.dao.BaseDao;
import core.util.Arith;
import core.util.DBUtil;
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
public class PersonalDaoImpl extends BaseDao<PersonalInfo>implements PersonalDao {

	public PersonalDaoImpl() {
		super(PersonalInfo.class);
	}

	private static final Logger log = Logger.getLogger(PersonalDaoImpl.class);

 

 

	  
	/**
	 * 查询个人信息
	 */
	@Override
	public List<PersonalInfo> getPersonalList(PersonalInfo personalinfo) {
		// TODO Auto-generated method stub

		String sql =" select u.uid,u.name,u.mobile,u.identity,SUBSTR(u.uid,3,8) as regDate ,ba.boundT as boundDate,b.bank,b.city,b.province from qj_user_db.`user` u  " 
				+" left join qj_user_db.bank_account ba on(u.uid=ba.uid) " 
				+" left join qj_user_db.banks b on(ba.brach=b.brach) "
				+" where 1=1  " ;

		if (!"".equals(StringUtil.trimNull(personalinfo.getS_date_reg()))) {
			sql += " and SUBSTR(u.uid,3,8) >='" +personalinfo.getS_date_reg() + "'";
		}

		if (!"".equals(StringUtil.trimNull(personalinfo.getE_date_reg()))) {
			sql += " and SUBSTR(u.uid,3,8) <='" +personalinfo.getE_date_reg() + "'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.getS_date_bound()))) {
			sql += " and ba.boundT >='" +personalinfo.getS_date_bound() + " 00:00:00'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.getE_date_bound()))) {
			sql += " and ba.boundT <='" +personalinfo.getE_date_bound() + " 23:59:59'";
		}

		if (!"".equals(StringUtil.trimNull(personalinfo.getUid()))) {
			sql += " and u.uid ='" +personalinfo.getUid() + "'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.get$like_name()))) {
			sql += " and u.name ='" +personalinfo.get$like_name() + "'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.get$like_mobile()))) {
			sql += " and u.mobile ='" +personalinfo.get$like_mobile()+ "'";
		}

		if (!"".equals(StringUtil.trimNull(personalinfo.getIdentity()))) {
			sql += " and u.identity ='" +personalinfo.getIdentity() + "'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.get$like_code()))) {
			sql = " select u.uid,u.name,u.mobile,u.identity,SUBSTR(u.uid,3,8) as regDate ,ba.boundT as boundDate,b.bank,b.city,b.province from qj_user_db.`user` u " 
					+" left join qj_user_db.bank_account ba on(u.uid=ba.uid) "
					+" left join qj_user_db.banks b on(ba.brach=b.brach) "
					+" left join qj_user_db.invite_basic ib on (ib.uid =u.uid)" 
					+" where 1=1 " 
					+" and ib.code='" + personalinfo.get$like_code()+"'" ;
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.get$lile_boundCode()))) {
			sql = " select u.uid,u.name,u.mobile,u.identity,SUBSTR(u.uid,3,8) as regDate ,ba.boundT as boundDate,b.bank,b.city,b.province from qj_user_db.`user` u " 
					+" left join qj_user_db.bank_account ba on(u.uid=ba.uid) " 
					+" left join qj_user_db.banks b on(ba.brach=b.brach) "
					+" left join qj_user_db.user_extinfo ue on (ue.uid =u.uid)" 
					+" where 1=1 " 
					+" and ue.boundCode='" + personalinfo.get$lile_boundCode()+"'" ;
		}
		
		String key = personalinfo.getSortedConditions().keySet().iterator().next();
		String value = personalinfo.getSortedConditions().values().iterator().next();
		sql += "     order by " + key + " " + value + " limit " + personalinfo.getFirstResult() + "," + personalinfo.getMaxResults();
		Query query = getSession().createSQLQuery(sql);
		List list = query.list();
		List<PersonalInfo> listOperates = new ArrayList<PersonalInfo>();
		// NumberFormat nt = NumberFormat.getPercentInstance();
		// nt.setMinimumFractionDigits(2);
		for (int i = 0; i < list.size(); i++) {
			PersonalInfo op = new PersonalInfo();
			Object[] o = (Object[]) list.get(i);
			op.setUid(StringUtil.trimNull(o[0]));
			op.setName(StringUtil.trimNull(o[1]));
			op.setMobile(StringUtil.trimNull(o[2]));
			op.setIdentity(StringUtil.trimNull(o[3]));
			op.setRegDate(StringUtil.trimNull(o[4]));
			op.setBoundDate(StringUtil.trimNull(o[5]));
			op.setBank(StringUtil.trimNull(o[6]));
			op.setCity(StringUtil.trimNull(o[7]));
			op.setProvince(StringUtil.trimNull(o[8]));
			listOperates.add(op);
		}
		return listOperates;
	} 
	
	@Override
	public long getPersonalListCount(PersonalInfo personalinfo) {
		// TODO Auto-generated method stub
	 
		String sql ="select count(1) from ( select u.uid,u.name,u.mobile,u.identity,SUBSTR(u.uid,3,8) as regDate ,ba.boundT as boundDate from qj_user_db.`user` u " 
				+" left join qj_user_db.bank_account ba on(u.uid=ba.uid) " 
				+" left join qj_user_db.banks b on(ba.brach=b.brach) "
				+" where 1=1  " ;

		if (!"".equals(StringUtil.trimNull(personalinfo.getS_date_reg()))) {
			sql += " and SUBSTR(u.uid,3,8) >='" +personalinfo.getS_date_reg() + "'";
		}

		if (!"".equals(StringUtil.trimNull(personalinfo.getE_date_reg()))) {
			sql += " and SUBSTR(u.uid,3,8) <='" +personalinfo.getE_date_reg() + "'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.getS_date_bound()))) {
			sql += " and ba.boundT >='" +personalinfo.getS_date_bound() + " 00:00:00'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.getE_date_bound()))) {
			sql += " and ba.boundT <='" +personalinfo.getE_date_bound() + " 23:59:59'";
		}

		if (!"".equals(StringUtil.trimNull(personalinfo.getUid()))) {
			sql += " and u.uid ='" +personalinfo.getUid() + "'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.get$like_name()))) {
			sql += " and u.name ='" +personalinfo.get$like_name() + "'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.get$like_mobile()))) {
			sql += " and u.mobile ='" +personalinfo.get$like_mobile() + "'";
		}

		if (!"".equals(StringUtil.trimNull(personalinfo.getIdentity()))) {
			sql += " and u.identity ='" +personalinfo.getIdentity() + "'";
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.get$like_code()))) {
			sql = "select count(1) from ( select u.uid,u.name,u.mobile,u.identity,SUBSTR(u.uid,3,8) as regDate ,ba.boundT as boundDate from qj_user_db.`user` u " 
					+" left join qj_user_db.bank_account ba on(u.uid=ba.uid) " 
					+" left join qj_user_db.banks b on(ba.brach=b.brach) "
					+" left join qj_user_db.invite_basic ib on (ib.uid =u.uid)" 
					+" where 1=1 " 
					+" and ib.code='" + personalinfo.get$like_code()+"'" ;
		}
		
		if (!"".equals(StringUtil.trimNull(personalinfo.get$lile_boundCode()))) {
			sql = " select count(1) from (select u.uid,u.name,u.mobile,u.identity,SUBSTR(u.uid,3,8) as regDate ,ba.boundT as boundDate from qj_user_db.`user` u " 
					+" left join qj_user_db.bank_account ba on(u.uid=ba.uid) " 
					+" left join qj_user_db.banks b on(ba.brach=b.brach) "
					+" left join qj_user_db.user_extinfo ue on (ue.uid =u.uid)" 
					+" where 1=1 " 
					+" and ue.boundCode='" + personalinfo.get$lile_boundCode()+"'" ;
		}


		sql += "    )t";

		Query query = getSession().createSQLQuery(sql);
		List list = query.list();

		String r = StringUtil.trimNull(list.get(0));

		return Integer.parseInt(r);
	}

	/**
	 * 统计个人投资信息
	 */
	@Override
	public void statistic_day_personal_invest() {
		// TODO Auto-generated method stub
		//String sdate = DateUtil.currentTimeMinus(14);
		//String edate = DateUtil.currentTimeMinus(0);
		String sdate ="2015-09-22 00:00:00";
		String edate ="2015-09-20 23:59:59";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<PersonalInvest> returnList = new ArrayList<PersonalInvest>();
		try {
			
			conn = DBUtil.getConnection();
			String sql =" select * From (                                                                                                                                     "
					+" select t.uid,u.`name`, u.mobile,u.identity,SUBSTR(u.uid ,3,8) as regDate,ba.boundT as boundDate,cl.name as channel,t.invest,t.fund,t.opDate from ( "
					+" select uid, sum as invest,'单只基金' as fund,opDate from qj_fund_db.operat_log ol1                                                                 "
					+"  where (ol1.operate=1 or ol1.operate=4)                                                                                                                              "
					+"  and (ol1.state=1 or ol1.state=3)                                                                                                                  "
					+"  and ol1.sid=0                                                                                                                                     "
					+"  union all                                                                                                                                         "
					+" select ol2.uid,sum(sum)  as invest,fs.`name` as fund,ol2.opDate  from qj_fund_db.operat_log ol2                                                    "
					+" left join qj_fund_db.finance_schema fs on(fs.sid = ol2.sid)                                                                                        "
					+" where  ol2.sid<>0                                                                                                                                  "
					+" and (ol2.operate=1 or ol2.operate=4) and (ol2.state=1 or ol2.state=3)                                                                                                 "
					+" group by uid,opDate                                                                                                                                "
					+" union all                                                                                                                                          "
					+" select  tl.uid,tl.sum as invest ,'活期宝' as fund ,FROM_UNIXTIME(opDate) as  opdate   From  qj_cashbao_db.trade_log tl                             "
					+" where (tl.operate=1 or tl.operate=4)                                                                                                                                 "
					+" and   (tl.state=2 or tl.state=3)                                                                                                                   "
					+" ) t                                                                                                                                                "
					+" left join qj_user_db.`user` u on(u.uid=t.uid)                                                                                                      "
					+" left join qj_user_db.bank_account ba on(u.uid=ba.uid)                                                                                              "
					+" left join qj_user_db.user_extinfo ue on(u.uid = ue.uid)                                                                                            "
					+" left join qj_activ_db.channel_list cl on(ue.channel=cl.channel)                                                                                    "
					+" where ba.state=1                                                                                                                                   "
					+"  )t1 where opdate BETWEEN '"+sdate+"' and '"+edate+"'  order by opDate desc                                                    ";
			pstm = conn.prepareStatement(sql);
			rs=pstm.executeQuery();
			
			while(rs.next()){
				PersonalInvest pi = new PersonalInvest();
				  pi.setUid(StringUtil.trimNull(rs.getString("uid")));
				  pi.setName(StringUtil.trimNull(rs.getString("name")));
				  pi.setMobile(StringUtil.trimNull(rs.getString("mobile")));
				  pi.setIdentity((StringUtil.trimNull(rs.getString("identity"))));
				  pi.setRegDate((StringUtil.trimNull(rs.getString("regDate"))));
				  pi.setBoundDate((StringUtil.trimNull(rs.getString("boundDate"))));
				  pi.setChannel((StringUtil.trimNull(rs.getString("channel"))));
				  pi.setInvest(StringUtil.trimNull(rs.getString("invest")));
				  pi.setFund(StringUtil.trimNull(rs.getString("fund")));
				  pi.setOpDate((StringUtil.trimNull(rs.getString("opDate"))));
				  String sql1= "select  t.invest,t.fund,t.opDate from (                                                                                    "
						  +"select uid, sum as invest,'单只基金' as fund,opDate from qj_fund_db.operat_log ol1                                         "
						  +" where (ol1.operate=1 or ol1.operate=4)                                                                                                       "
						  +" and ol1.sid=0                                                                                                             "
						  +" union all                                                                                                                 "
						  +"select ol2.uid,sum(sum)  as invest,fs.`name` as fund,ol2.opDate  from qj_fund_db.operat_log ol2                            "
						  +"left join qj_fund_db.finance_schema fs on(fs.sid = ol2.sid)                                                                "
						  +"where  ol2.sid<>0                                                                                                          "
						  +"and (ol2.operate=1 or ol2.operate=4)                                                                                                          "
						  +"group by uid,opDate                                                                                                        "
						  +"union all                                                                                                                  "
						  +"select  tl.uid,tl.sum as invest ,'活期宝' as fund ,FROM_UNIXTIME(opDate) as  opdate   From  qj_cashbao_db.trade_log tl     "
						  +"where (tl.operate=1 or tl.operate=4)                                                                                                         "
						  +" )t                                                                                                                        "
						  +" where  t.uid = '" + pi.getUid()+"'  order by opDate asc limit 1                                                           " ;
				  PreparedStatement pstm1 = conn.prepareStatement(sql1);
				  ResultSet rs1=pstm1.executeQuery();
				  
				  while(rs1.next()){
					  pi.setFirst_fund(((StringUtil.trimNull(rs1.getString("fund")))));
					  pi.setFirst_invest((((StringUtil.trimNull(rs1.getString("invest"))))));
					  pi.setFirst_opDate((((StringUtil.trimNull(rs1.getString("opDate"))))));
				  }
				  
				  returnList.add(pi);
				  System.out.println(returnList.size());
				  DBUtil.close(null, pstm1, rs1);
			}
			savePersonal_investToDb(returnList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(conn, pstm, rs);
		}
	}

	/**
	 * 
	 *  Function:存入数据库
	 *  @author TongXin  DateTime 2015年9月21日 下午7:10:39
	 *  @param list
	 */
	public void savePersonal_investToDb(List<PersonalInvest> list) {
		try {
			if (list == null) {
				return;
			}
			 
			int m = 500;// 批量处理条数
			StringBuffer sql = new StringBuffer();
			sql.append(  "replace into qj_qjs_db.day_personal_invest(uid,name,mobile,identity,regDate,boundDate,channel,first_opDate,first_invest,first_fund,opDate,invest,fund) values");
			//long c1 = Calendar.getInstance().getTimeInMillis();
		//	sql.append( sql);
			for (int i = 0; i < list.size(); i++) {
				PersonalInvest op = list.get(i);
				sql.append("('" + op.getUid() + "','");
				sql.append(op.getName() + "','");
				sql.append(op.getMobile() + "','");
				sql.append(op.getIdentity() + "','");
				sql.append(op.getRegDate() + "','");
				sql.append(op.getBoundDate() + "','");
				sql.append(op.getChannel() + "','");
				sql.append(op.getFirst_opDate() + "','");
				sql.append(op.getFirst_invest() + "','");
				sql.append(op.getFirst_fund() + "','");
				sql.append(op.getOpDate() + "','");
				sql.append(op.getInvest() + "','");
				sql.append(op.getFund());
				if (i % m == 0 || i == (list.size() - 1)) {
					sql.append("');");
					DBUtil.dbInsert(sql.toString());
					sql.setLength(0);
					sql.append(  "replace into qj_qjs_db.day_personal_invest(uid,name,mobile,identity,regDate,boundDate,channel,first_opDate,first_invest,first_fund,opDate,invest,fund) values");
				} else {
					sql.append("'),");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	 
 
}
