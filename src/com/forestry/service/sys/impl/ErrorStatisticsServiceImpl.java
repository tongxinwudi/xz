package com.forestry.service.sys.impl;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.forestry.core.Constant;
import com.forestry.dao.sys.FundOperatLogDao;
import com.forestry.dao.sys.impl.CashbaoTradeLogDaoImpl;
import com.forestry.dao.sys.impl.FundOperatLogDaoImpl;
import com.forestry.dao.sys.impl.UserBankAccountDaoImpl;
import com.forestry.model.sys.ErrorCommonInfo;
import com.forestry.model.sys.ErrorCountView;
import com.forestry.service.sys.ErrorStatisticsService;

import core.service.BaseService;

@Service
public class ErrorStatisticsServiceImpl extends BaseService<ErrorCommonInfo>
		implements ErrorStatisticsService {
	@Resource
	private FundOperatLogDaoImpl fundDao;
	@Resource
	private CashbaoTradeLogDaoImpl cashbaoDao;
	@Resource
	private UserBankAccountDaoImpl userDao;

	@Override
	public Long getTotalErrorCount(ErrorCommonInfo model) {
		Long totalCount = fundDao.getErrorCount(model)
				+ cashbaoDao.getErrorCount(model)
				+ userDao.getErrorCount(model);
		return totalCount;
	}

	/**
	 * 返回各类错误总数、比例等信息
	 * 
	 * @return
	 */
	@Override
	public List<ErrorCountView> getErrorCountPage(ErrorCommonInfo model) {
		Long fundTotal = fundDao.getErrorCount(model);
		Long cashTotal = cashbaoDao.getErrorCount(model);
		Long userTotal = userDao.getErrorCount(model);
		Long total = fundTotal + cashTotal + userTotal;
		DecimalFormat df = new DecimalFormat("0.##");

		List<ErrorCountView> list = new ArrayList<ErrorCountView>();
		ErrorCountView page = new ErrorCountView();
		page.setErrorType(1);
		page.setTitle(Constant.ERROR_FUND_NAME);
		page.setCount(fundTotal);
		page.setRatio(df.format(fundTotal / (total * 1.0) * 100));
		list.add(page);

		page = new ErrorCountView();
		page.setErrorType(2);
		page.setTitle(Constant.ERROR_CASHBAO_NAME);
		page.setCount(cashTotal);
		page.setRatio(df.format(cashTotal / (total * 1.0) * 100));
		list.add(page);

		page = new ErrorCountView();
		page.setErrorType(3);
		page.setTitle(Constant.ERROR_USER_NAME);
		page.setCount(userTotal);
		page.setRatio(df.format(userTotal / (total * 1.0) * 100));
		list.add(page);
		return list;
	}

	/**
	 * 按照年月返回各类错误总数、比例等信息
	 * 
	 * @return
	 */
	@Override
	public String getErrorCountMonthPage(ErrorCommonInfo model) {
		//List list=new LinkedList();
		List fList = fundDao.getErrorCountByMonth(model);
		List cashList = cashbaoDao.getErrorCountByMonth(model);
		List userList = userDao.getErrorCountByMonth(model);
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int fIndex = 0; fIndex < fList.size(); fIndex++) {
			Object[] fund = (Object[]) fList.get(fIndex);
			Integer year = (Integer) fund[0];
			Integer month = (Integer) fund[1];
			
			sb.append("{ym:");
			sb.append("\""+year + "/" + month);
			sb.append("\",fund:");
			sb.append((BigInteger) fund[2]);

			for (int cIndex = 0; cIndex < cashList.size(); cIndex++) {
				Object[] cash = (Object[]) cashList.get(cIndex);
				boolean isAdd=false;
				
				if (year == Integer.parseInt(cash[0].toString()) && month == Integer.parseInt(cash[1].toString())) {
					sb.append(",cash:");
					sb.append((BigInteger) cash[2]);
					isAdd=true;
					cashList.remove(cIndex);
					break;
				}
				if(!isAdd&&cIndex==cashList.size()-1){
					sb.append(",cash:");
					sb.append("0");
				}
			}
			
			for (int uIndex = 0; uIndex < userList.size(); uIndex++) {
				Object[] user = (Object[]) userList.get(uIndex);
				boolean isAdd=false;
				
				if (year.equals(user[0]) && month.equals(user[1])) {
					sb.append(",user:");
					sb.append((BigInteger) user[2]);
					isAdd=true;
					userList.remove(uIndex);
					break;
				}
				if(!isAdd&&uIndex==userList.size()){
					sb.append(",user:");
					sb.append("0");
				}
			}
			sb.append("}");
			//list.add(sb);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 获取交易类错误列表
	 * 
	 * @return
	 */
	@Override
	public List<ErrorCommonInfo> getFundErrorList(ErrorCommonInfo model) {
		return fundDao.getAllErrorInfoList(model);
	}

	/**
	 * 获取活期宝类错误列表
	 * 
	 * @return
	 */
	@Override
	public List<ErrorCommonInfo> getCashbaoErrorList(ErrorCommonInfo model) {
		return cashbaoDao.getAllErrorInfoList(model);
	}

	/**
	 * 获取绑卡类错误列表
	 * 
	 * @return
	 */
	@Override
	public List<ErrorCommonInfo> getUserErrorList(ErrorCommonInfo model) {
		return userDao.getAllErrorInfoList(model);
	}

	/**
	 * 获取交易类错误总数
	 * 
	 * @return
	 */
	public Long getFundErrorCount(ErrorCommonInfo model) {
		Long aa = fundDao.getErrorCount(model);
		return aa;
	}

	/**
	 * 获取活期宝类错误总数
	 * 
	 * @return
	 */
	public Long getCashbaoErrorCount(ErrorCommonInfo model) {
		return cashbaoDao.getErrorCount(model);
	}

	/**
	 * 获取绑卡类错误总数
	 * 
	 * @return
	 */
	public Long getUserErrorCount(ErrorCommonInfo model) {
		return userDao.getErrorCount(model);
	}

	/**
	 * 获取所有错误列表
	 */
	@Override
	public List<ErrorCommonInfo> getAllErrorList(ErrorCommonInfo model) {
		List<ErrorCommonInfo> list = new ArrayList<ErrorCommonInfo>();
		list.addAll(getFundErrorList(model));
		list.addAll(getCashbaoErrorList(model));
		list.addAll(getUserErrorList(model));
		long i = 0;
		for (ErrorCommonInfo error : list) {
			error.setId(i);
			i++;
		}
		return list;
	}
}
