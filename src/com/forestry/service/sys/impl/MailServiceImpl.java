package com.forestry.service.sys.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.forestry.dao.sys.AttachmentDao;
import com.forestry.dao.sys.ForestryDao;
import com.forestry.dao.sys.ForestryTypeDao;
import com.forestry.dao.sys.OperateDao;
import com.forestry.dao.sys.ProfitDao;
import com.forestry.model.sys.Attachment;
import com.forestry.model.sys.Channel;
import com.forestry.model.sys.Fail;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.Mail;
import com.forestry.model.sys.MailContent;
import com.forestry.model.sys.Money;
import com.forestry.model.sys.NewOld;
import com.forestry.model.sys.Operate;
import com.forestry.model.sys.Profit;
import com.forestry.model.sys.TradeUser;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.MailService;
import com.forestry.service.sys.OperateService;
import com.forestry.service.sys.ProfitService;

import core.service.BaseService;
import core.util.Config;
import core.util.DateUtil;
import core.util.MailUtil;

/**
 * 
 * Class Name: ProfitServiceImpl.java Function: Modifications:
 * 
 * @author TongXin DateTime 2015年7月13日 下午3:23:50
 * @version 1.0
 */
@Service
public class MailServiceImpl implements MailService {

	static Logger logger = Logger.getLogger(MailServiceImpl.class.getName());
	@Resource
	private OperateDao operateDao;

	@Resource
	private OperateService operateService;

	@Override
	public void sendMail() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		// map.put("#{loginName}", "tongxin");
		String _token = UUID.randomUUID().toString();
		MailContent content = new MailContent();
		content.setContentMap(map);
		Mail mailModel = Mail.MailCF;
		mailModel.subject = "日常统计" + DateUtil.currentTime();
		mailModel.setContent(content);
		List<File> listFile = new ArrayList<File>();
		try {
			listFile.add(getInvestFile());
			listFile.add(getFailtFile());
			listFile.add(getChannelFile());
			listFile.add(getTradeUserFile());
			listFile.add(getNewOldFile());
			listFile.add(getMoneyFile());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return;
		}
		MailUtil.send(mailModel, listFile);
	}

	/**
	 * 
	 * Function:申购数据
	 * 
	 * @author TongXin DateTime 2015年8月14日 下午4:12:10
	 * @return
	 * @throws Exception
	 */
	public File getInvestFile() throws Exception {

		Integer firstResult = 0;
		Integer maxResults = 10000;

		String sortedObject = "date";
		String sortedValue = "desc";

		Operate operate = new Operate();

		String sdate = DateUtil.currentMonthMinus(1);
		String edate = DateUtil.currentTimeMinus(0);

		operate.setSdate(sdate);
		operate.setEdate(edate);

		String path = MailServiceImpl.class.getClassLoader().getResource("/").getPath() + "/invest/invest" + sdate + "_" + edate + ".xls";

		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);

		List<Operate> listOperate = operateService.getOperateList(operate);
		writeXLS(listOperate, "invest" + sdate + "_" + edate, path);

		File file = new File(path);
		return file;
	}

	private static void writeXLS(List<Operate> list, String name, String path) throws Exception {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(name);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("申购人数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("申购笔数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 4);
		cell.setCellValue("申购失败人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 5);
		cell.setCellValue("申购失败笔数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 6);
		cell.setCellValue("申购失败金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 7);
		cell.setCellValue("申购成功人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 8);
		cell.setCellValue("申购成功笔数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 9);
		cell.setCellValue("申购成功金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 10);
		cell.setCellValue("部分成功笔数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 11);
		cell.setCellValue("部分成功金额");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			Operate data = list.get(i);
			row = sheet.createRow((int) i + 1);

			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(data.getDate());
			row.createCell((short) 1).setCellValue(data.getInvestTotalPersonNum());
			row.createCell((short) 2).setCellValue(data.getInvestTotalNum());
			row.createCell((short) 3).setCellValue(data.getInvestTotalMoney());
			row.createCell((short) 4).setCellValue(data.getInvestFailPersonNum());
			row.createCell((short) 5).setCellValue(data.getInvestFailNum());
			row.createCell((short) 6).setCellValue(data.getInvestFailMoney());
			row.createCell((short) 7).setCellValue(data.getInvestSuccessPersonNum());
			row.createCell((short) 8).setCellValue(data.getInvestSuccessNum());
			row.createCell((short) 9).setCellValue(data.getInvestSuccessMoney());
			row.createCell((short) 10).setCellValue(data.getInvestPartSuccessNum());
			row.createCell((short) 11).setCellValue(data.getInvestPartSuccessMoney());

		}
		// 第六步，将文件存到指定位置

		FileOutputStream fout = new FileOutputStream(path);
		wb.write(fout);
		fout.close();

	}

	/**
	 * 
	 * Function:申购数据
	 * 
	 * @author TongXin DateTime 2015年8月14日 下午4:12:10
	 * @return
	 * @throws Exception
	 */
	public File getFailtFile() throws Exception {

		Integer firstResult = 0;
		Integer maxResults = 10000;

		String sortedObject = "date";
		String sortedValue = "desc";

		Fail fail = new Fail();

		String sdate = DateUtil.currentTimeMinus(7);
		String edate = DateUtil.currentTimeMinus(0);

		fail.setSdate(sdate);
		fail.setEdate(edate);

		String path = MailServiceImpl.class.getClassLoader().getResource("/").getPath() + "/invest/fail" + sdate + "_" + edate + ".xls";

		fail.setFirstResult(firstResult);
		fail.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		fail.setSortedConditions(sortedCondition);

		List<Fail> listFail = operateService.getFailList(fail);
		writeXLS_fail(listFail, "fail" + sdate + "_" + edate, path);

		File file = new File(path);
		return file;
	}

	private static void writeXLS_fail(List<Fail> list, String name, String path) throws Exception {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(name);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("UID");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("电话号码");
		cell.setCellStyle(style);

		cell = row.createCell((short) 4);
		cell.setCellValue("产品名称");
		cell.setCellStyle(style);

		cell = row.createCell((short) 5);
		cell.setCellValue("投资额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 6);
		cell.setCellValue("开户行");
		cell.setCellStyle(style);

		cell = row.createCell((short) 7);
		cell.setCellValue("失败原因");
		cell.setCellStyle(style);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			Fail data = list.get(i);
			row = sheet.createRow((int) i + 1);

			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(data.getDate());
			row.createCell((short) 1).setCellValue(data.getUid());
			row.createCell((short) 2).setCellValue(data.getName());
			row.createCell((short) 3).setCellValue(data.getMobile());
			row.createCell((short) 4).setCellValue(data.getFund());
			row.createCell((short) 5).setCellValue(data.getInvest());
			row.createCell((short) 6).setCellValue(data.getBank());
			row.createCell((short) 7).setCellValue(data.getReason());
		}
		// 第六步，将文件存到指定位置

		FileOutputStream fout = new FileOutputStream(path);
		wb.write(fout);
		fout.close();

	}

	public File getChannelFile() throws Exception {

		Integer firstResult = 0;
		Integer maxResults = 10000;

		String sortedObject = "date";
		String sortedValue = "desc";

		Channel operate = new Channel();

		String sdate = DateUtil.currentMonthMinus(1);
		String edate = DateUtil.currentTimeMinus(0);

		operate.setSdate(sdate);
		operate.setEdate(edate);

		String path = MailServiceImpl.class.getClassLoader().getResource("/").getPath() + "/invest/channel" + sdate + "_" + edate + ".xls";

		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);

		List<Channel> listOperate = operateService.getChannelList(operate);
		writeXLS_Channel(listOperate, "channel" + sdate + "_" + edate, path);

		File file = new File(path);
		return file;
	}

	private static void writeXLS_Channel(List<Channel> list, String name, String path) throws Exception {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(name);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("平台");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("渠道名称");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("注册人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 4);
		cell.setCellValue("绑卡人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 5);
		cell.setCellValue("新绑卡人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 6);
		cell.setCellValue("申购用户数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 7);
		cell.setCellValue("申购总金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 8);
		cell.setCellValue("人均投资金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 9);
		cell.setCellValue("新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 10);
		cell.setCellValue("新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 11);
		cell.setCellValue("新用户人均投资金额");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			Channel data = list.get(i);
			row = sheet.createRow((int) i + 1);

			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(data.getDate());
			row.createCell((short) 1).setCellValue(data.getPlatform());
			row.createCell((short) 2).setCellValue(data.getChannel());
			row.createCell((short) 3).setCellValue(data.getReg_num());
			row.createCell((short) 4).setCellValue(data.getBound_num());
			row.createCell((short) 5).setCellValue(data.getNew_bound_num());
			row.createCell((short) 6).setCellValue(data.getUser_num());
			row.createCell((short) 7).setCellValue(data.getMoney_num());
			row.createCell((short) 8).setCellValue(data.getPer_money_num());
			row.createCell((short) 9).setCellValue(data.getNew_user_num());
			row.createCell((short) 10).setCellValue(data.getNew_money_num());
			row.createCell((short) 11).setCellValue(data.getPer_new_money_num());

		}
		// 第六步，将文件存到指定位置

		FileOutputStream fout = new FileOutputStream(path);
		wb.write(fout);
		fout.close();

	}

	/**
	 * 
	 * Function:交易明细
	 * 
	 * @author TongXin DateTime 2015年8月27日 上午10:46:40
	 * @return
	 * @throws Exception
	 */
	public File getTradeUserFile() throws Exception {

		Integer firstResult = 0;
		Integer maxResults = 10000;

		String sortedObject = "reg_time";
		String sortedValue = "desc";

		TradeUser operate = new TradeUser();

		String sdate = DateUtil.currentMonthMinus(1);
		String edate = DateUtil.currentTimeMinus(0);

		operate.setSdate(sdate);
		operate.setEdate(edate);

		String path = MailServiceImpl.class.getClassLoader().getResource("/").getPath() + "/invest/tradeUser" + sdate + "_" + edate + ".xls";

		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);

		List<TradeUser> listOperate = operateService.getTradeUserlList(operate);
		writeXLS_TradeUser(listOperate, "tradeUser" + sdate + "_" + edate, path);

		File file = new File(path);
		return file;
	}

	private static void writeXLS_TradeUser(List<TradeUser> list, String name, String path) throws Exception {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(name);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("用户uid");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("注册时间");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("绑卡时间");
		cell.setCellStyle(style);

		cell = row.createCell((short) 4);
		cell.setCellValue("首次投资时间");
		cell.setCellStyle(style);

		cell = row.createCell((short) 5);
		cell.setCellValue("首次投资金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 6);
		cell.setCellValue("首次投资购买产品名称");
		cell.setCellStyle(style);

		cell = row.createCell((short) 7);
		cell.setCellValue("首次投资平台");
		cell.setCellStyle(style);

		cell = row.createCell((short) 8);
		cell.setCellValue("首次投资渠道");
		cell.setCellStyle(style);

		cell = row.createCell((short) 9);
		cell.setCellValue("二次投资时间");
		cell.setCellStyle(style);

		cell = row.createCell((short) 10);
		cell.setCellValue("二次投资金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 11);
		cell.setCellValue("二次投资产品名称");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			TradeUser data = list.get(i);
			row = sheet.createRow((int) i + 1);

			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(data.getUid());
			row.createCell((short) 1).setCellValue(data.getName());
			row.createCell((short) 2).setCellValue(data.getReg_time());
			row.createCell((short) 3).setCellValue(data.getBoundT());
			row.createCell((short) 4).setCellValue(data.getFirst_time());
			row.createCell((short) 5).setCellValue(data.getFirst_money());
			row.createCell((short) 6).setCellValue(data.getFirst_stype());
			row.createCell((short) 7).setCellValue(data.getFirst_platform());
			row.createCell((short) 8).setCellValue(data.getFirst_channel());
			row.createCell((short) 9).setCellValue(data.getSecond_time());
			row.createCell((short) 10).setCellValue(data.getSecond_money());
			row.createCell((short) 11).setCellValue(data.getSecond_stype());

		}
		// 第六步，将文件存到指定位置

		FileOutputStream fout = new FileOutputStream(path);
		wb.write(fout);
		fout.close();

	}

	/**
	 * 
	 * Function:新老用户
	 * 
	 * @author TongXin DateTime 2015年8月27日 上午10:46:40
	 * @return
	 * @throws Exception
	 */
	public File getNewOldFile() throws Exception {

		Integer firstResult = 0;
		Integer maxResults = 10000;

		String sortedObject = "date";
		String sortedValue = "desc";

		NewOld operate = new NewOld();

		String sdate = DateUtil.currentMonthMinus(1);
		String edate = DateUtil.currentTimeMinus(0);

		operate.setSdate(sdate);
		operate.setEdate(edate);

		String path = MailServiceImpl.class.getClassLoader().getResource("/").getPath() + "/invest/newOld" + sdate + "_" + edate + ".xls";

		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);

		List<NewOld> listOperate = operateService.getNewOldlList(operate);
		writeXLS_NewOld(listOperate, "newOld" + sdate + "_" + edate, path);

		File file = new File(path);
		return file;
	}

	private static void writeXLS_NewOld(List<NewOld> list, String name, String path) throws Exception {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(name);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("日期");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("申购用户数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("新用户申购数");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("活期宝新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 4);
		cell.setCellValue("单只基金新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 5);
		cell.setCellValue("进取型新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 6);
		cell.setCellValue("保守型新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 7);
		cell.setCellValue("稳健型新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 8);
		cell.setCellValue("理财增值型新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 9);
		cell.setCellValue("理财增值型2(新20档)新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 10);
		cell.setCellValue("退休养老型新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 11);
		cell.setCellValue("买房置业型新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 12);
		cell.setCellValue("子女教育型新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 13);
		cell.setCellValue("结婚生子型新用户申购人数");
		cell.setCellStyle(style);

		cell = row.createCell((short) 14);
		cell.setCellValue("短期理财型新用户申购人数");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			NewOld data = list.get(i);
			row = sheet.createRow((int) i + 1);

			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(data.getDate());
			row.createCell((short) 1).setCellValue(data.getTotal_num());
			row.createCell((short) 2).setCellValue(data.getNew_num());
			row.createCell((short) 3).setCellValue(data.getCashbao_num());
			row.createCell((short) 4).setCellValue(data.getSingle_num());
			row.createCell((short) 5).setCellValue(data.getJinqu_num());
			row.createCell((short) 6).setCellValue(data.getBaoshou_num());
			row.createCell((short) 7).setCellValue(data.getWenjian_num());
			row.createCell((short) 8).setCellValue(data.getLicai_num());
			row.createCell((short) 9).setCellValue(data.getLicai2_num());
			row.createCell((short) 10).setCellValue(data.getYanglao_num());
			row.createCell((short) 11).setCellValue(data.getMaifang_num());
			row.createCell((short) 12).setCellValue(data.getZinv_num());
			row.createCell((short) 13).setCellValue(data.getJiehun_num());
			row.createCell((short) 14).setCellValue(data.getDuanqi_num());

		}
		// 第六步，将文件存到指定位置

		FileOutputStream fout = new FileOutputStream(path);
		wb.write(fout);
		fout.close();

	}

	/**
	 * 
	 * Function:申购金额
	 * 
	 * @author TongXin DateTime 2015年8月27日 上午10:46:40
	 * @return
	 * @throws Exception
	 */
	public File getMoneyFile() throws Exception {

		Integer firstResult = 0;
		Integer maxResults = 10000;

		String sortedObject = "date";
		String sortedValue = "desc";

		Money operate = new Money();

		String sdate = DateUtil.currentMonthMinus(1);
		String edate = DateUtil.currentTimeMinus(0);

		operate.setSdate(sdate);
		operate.setEdate(edate);

		String path = MailServiceImpl.class.getClassLoader().getResource("/").getPath() + "/invest/money" + sdate + "_" + edate + ".xls";

		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);

		List<Money> listOperate = operateService.getMoneylList(operate);
		writeXLS_Money(listOperate, "money" + sdate + "_" + edate, path);

		File file = new File(path);
		return file;
	}

	private static void writeXLS_Money(List<Money> list, String name, String path) throws Exception {

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(name);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("日期");
		cell.setCellStyle(style);
	 
		cell = row.createCell((short) 1);
		cell.setCellValue("活期宝新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 2);
		cell.setCellValue("单只基金新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 3);
		cell.setCellValue("进取型新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 4);
		cell.setCellValue("保守型新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 5);
		cell.setCellValue("稳健型新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 6);
		cell.setCellValue("理财增值型新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 7);
		cell.setCellValue("理财增值型2(新20档)新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 8);
		cell.setCellValue("退休养老型新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 9);
		cell.setCellValue("买房置业型新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 10);
		cell.setCellValue("子女教育型新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 11);
		cell.setCellValue("结婚生子型新用户申购金额");
		cell.setCellStyle(style);

		cell = row.createCell((short) 12);
		cell.setCellValue("短期理财型新用户申购金额");
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，

		for (int i = 0; i < list.size(); i++) {

			Money data = list.get(i);
			row = sheet.createRow((int) i + 1);

			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(data.getDate());
			row.createCell((short) 1).setCellValue(data.getCashbao_num());
			row.createCell((short) 2).setCellValue(data.getSingle_num());
			row.createCell((short) 3).setCellValue(data.getJinqu_num());
			row.createCell((short) 4).setCellValue(data.getBaoshou_num());
			row.createCell((short) 5).setCellValue(data.getWenjian_num());
			row.createCell((short) 6).setCellValue(data.getLicai_num());
			row.createCell((short) 7).setCellValue(data.getLicai2_num());
			row.createCell((short) 8).setCellValue(data.getYanglao_num());
			row.createCell((short) 9).setCellValue(data.getMaifang_num());
			row.createCell((short) 10).setCellValue(data.getZinv_num());
			row.createCell((short) 11).setCellValue(data.getJiehun_num());
			row.createCell((short) 12).setCellValue(data.getDuanqi_num());

		}
		// 第六步，将文件存到指定位置

		FileOutputStream fout = new FileOutputStream(path);
		wb.write(fout);
		fout.close();

	}

}
