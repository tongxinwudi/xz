package com.forestry.controller.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import com.forestry.core.ForestryBaseController;
import com.forestry.model.sys.Channel;
import com.forestry.model.sys.Channel2;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.Fail;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.ForestryType;
import com.forestry.model.sys.Inventory;
import com.forestry.model.sys.Money;
import com.forestry.model.sys.NewOld;
import com.forestry.model.sys.Operate;
import com.forestry.model.sys.Profit;
import com.forestry.model.sys.Settle;
import com.forestry.model.sys.TradeUser;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.ForestryTypeService;
import com.forestry.service.sys.OperateService;
import com.forestry.service.sys.ProfitService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;
import core.util.DateUtil;
import core.util.ForestryUtils;

/**
 * 
 *  Class Name: OperateController.java
 *  Function:运营数据查询
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月12日 下午2:20:05    
 *  @version 1.0
 */
@Controller
@RequestMapping("/sys/operate")
public class OperateController extends ForestryBaseController<Forestry> {

	private static final Logger log = Logger.getLogger(OperateController.class);
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private OperateService operateService;
  
 
	@RequestMapping(value = "/getInvestList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getInvestList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Operate operate = new Operate();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}
		 
	 
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);
		
		 
		ListView<Operate> operateListView = new ListView<Operate>();
		operateListView.setData(operateService.getOperateList(operate));
		operateListView.setTotalRecord(operateService.getOperateListCount(operate));
		writeJSON(response, operateListView);
	}
	
	
	@RequestMapping(value = "/getFailList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getFailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Fail fail = new Fail();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			fail.setSdate(sdate);
		}
		
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			fail.setEdate(edate);
		}
		
		String uid = request.getParameter("uid");
		if (StringUtils.isNotBlank(uid)) {
			fail.set$like_uid(uid);;
		}
		
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			fail.set$like_name(name);;
		}
		
		String mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank(mobile)) {
			fail.set$like_mobile(mobile);;
		}
		
		
		
		fail.setFirstResult(firstResult);
		fail.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		fail.setSortedConditions(sortedCondition);
		
		 
		ListView<Fail> failListView = new ListView<Fail>();
		failListView.setData(operateService.getFailList(fail));
		failListView.setTotalRecord(operateService.getFailListCount(fail));
		writeJSON(response, failListView);
	}
	

	@RequestMapping(value = "/getChannelList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChannelList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Channel channel = new Channel();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			channel.setSdate(sdate);
		}
		
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			channel.setEdate(edate);
		}
		
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			channel.set$like_platform(platform);;
		}
		
		String channels = request.getParameter("channel");
		if (StringUtils.isNotBlank(channels)) {
			channel.set$like_channel(channels);;
		}
		
		channel.setFirstResult(firstResult);
		channel.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		channel.setSortedConditions(sortedCondition);
		
		 
		ListView<Channel> channelListView = new ListView<Channel>();
		channelListView.setData(operateService.getChannelList(channel));
		channelListView.setTotalRecord(operateService.getChannelListCount(channel));
		writeJSON(response, channelListView);
	}
	
	
	@RequestMapping(value = "/getChannel2List", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChannel2List(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Channel2 channel = new Channel2();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			channel.setSdate(sdate);
		}
		
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			channel.setEdate(edate);
		}
		
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			channel.set$like_platform(platform);;
		}
		
		String channels = request.getParameter("channel");
		if (StringUtils.isNotBlank(channels)) {
			channel.set$like_channel(channels);;
		}
		
		String channels2 = request.getParameter("channel2");
		if (StringUtils.isNotBlank(channels2)) {
			channel.set$like_channel2(channels2);;
		}
		
		String last = request.getParameter("last");
		if (StringUtils.isNotBlank(last)) {
			channel.set$last(last);;
		}
		
		channel.setFirstResult(firstResult);
		channel.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		channel.setSortedConditions(sortedCondition);
		
		 
		ListView<Channel2> channelListView = new ListView<Channel2>();
		channelListView.setData(operateService.getChannel2List(channel));
		channelListView.setTotalRecord(operateService.getChannel2ListCount(channel));
		writeJSON(response, channelListView);
	}
	
	
	
	@RequestMapping(value = "/getChannel2List2", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChannel2List2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;//Integer.valueOf(request.getParameter("start"));
		Integer maxResults = 20;//Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
//		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
//		for (int i = 0; i < sortedList.size(); i++) {
//			Map<String, Object> map = sortedList.get(i);
//			sortedObject = (String) map.get("property");
//			sortedValue = (String) map.get("direction");
//		}
		
		sortedObject="s_date";
		sortedValue="desc";
		Channel2 channel = new Channel2();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			channel.setSdate(sdate);
		}
		sdate="2016-01-29";
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			channel.setEdate(edate);
		}
		edate="2016-02-02";
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			channel.set$like_platform(platform);;
		}
		
		String channels = request.getParameter("channel");
		if (StringUtils.isNotBlank(channels)) {
			channel.set$like_channel(channels);;
		}
		channels="qianjing";
	    String channels2 = request.getParameter("channel2");
		if (StringUtils.isNotBlank(channels2)) {
			channel.set$like_channel2(channels2);;
		}
		channels2="qianjing";
		
		String last = request.getParameter("last");
		if (StringUtils.isNotBlank(last)) {
			channel.set$last(last);;
		}
		
		channel.setFirstResult(firstResult);
		channel.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		channel.setSortedConditions(sortedCondition);
		
		 
		ListView<Channel2> channelListView = new ListView<Channel2>();
		channelListView.setData(operateService.getChannel2List2(channel));
		//channelListView.setTotalRecord(operateService.getChannel2ListCount(channel));
		writeJSON(response, channelListView);
	}
	
	@RequestMapping(value = "/getChannel2NumGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChannel2NumGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Channel2> channel2List= operateService.getChannel2NumGraph(null);
		writeJSON(response, channel2List);
	}
	
	@RequestMapping(value = "/getChannel2RateGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChannel2RateGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Channel2> channel2List= operateService.getChannel2RateGraph(null);
		writeJSON(response, channel2List);
	}
	
	@RequestMapping(value = "/getChannel2MoneyGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChannel2MoneyGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Channel2> channel2List= operateService.getChannel2MoneyGraph(null);
		writeJSON(response, channel2List);
	}
	
	@RequestMapping(value = "/getChannelTypeList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChannelTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<ChannelType> channelTypeList = operateService.getChannelTypeList(null);
		writeJSON(response, channelTypeList);
	}
	
	
	@RequestMapping(value = "/getTradeUserList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getTradeUserlList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		TradeUser tradeUser = new TradeUser();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			tradeUser.setSdate(sdate);
		}
		
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			tradeUser.setEdate(edate);
		}
		
		String platform = request.getParameter("uid");
		if (StringUtils.isNotBlank(platform)) {
			tradeUser.set$like_uid(platform);;
		}
		
		String channels = request.getParameter("name");
		if (StringUtils.isNotBlank(channels)) {
			tradeUser.set$like_name(channels);;
		}
		
		tradeUser.setFirstResult(firstResult);
		tradeUser.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		tradeUser.setSortedConditions(sortedCondition);
		
		 
		ListView<TradeUser> tradeUserListView = new ListView<TradeUser>();
		tradeUserListView.setData(operateService.getTradeUserlList(tradeUser));
		tradeUserListView.setTotalRecord(operateService.getTradeUserListCount(tradeUser));
		writeJSON(response, tradeUserListView);
	}
	
	@RequestMapping(value = "/getNewOldList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getNewOldList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		NewOld newOld = new NewOld();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			newOld.setSdate(sdate);
		}
		
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			newOld.setEdate(edate);
		}
		
		 
		
		newOld.setFirstResult(firstResult);
		newOld.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		newOld.setSortedConditions(sortedCondition);
		
		 
		ListView<NewOld> newOldListView = new ListView<NewOld>();
		newOldListView.setData(operateService.getNewOldlList(newOld));
		newOldListView.setTotalRecord(operateService.getNewOldListCount(newOld));
		writeJSON(response, newOldListView);
	}
	
	
	@RequestMapping(value = "/getMoneyList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getMoneyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Money money = new Money();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			money.setSdate(sdate);
		}
		
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			money.setEdate(edate);
		}
		
		 
		
		money.setFirstResult(firstResult);
		money.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		money.setSortedConditions(sortedCondition);
		
		 
		ListView<Money> moneyListView = new ListView<Money>();
		moneyListView.setData(operateService.getMoneylList(money));
		moneyListView.setTotalRecord(operateService.getMoneyListCount(money));
		writeJSON(response, moneyListView);
	}
	
	
	@RequestMapping(value = "/getInventoryList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getInventoryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Inventory inventory = new Inventory();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			inventory.setSdate(sdate);
		}
		
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			inventory.setEdate(edate);
		}
		
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			inventory.set$like_platform(platform);;
		}
		
		String fdcode = request.getParameter("fdCode");
		if (StringUtils.isNotBlank(fdcode)) {
			inventory.set$like_fdCode(fdcode);;
		}
		
		inventory.setFirstResult(firstResult);
		inventory.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		inventory.setSortedConditions(sortedCondition);
		
		 
		ListView<Inventory> InventoryView = new ListView<Inventory>();
		InventoryView.setData(operateService.getInventoryList(inventory));
		InventoryView.setTotalRecord(operateService.getInventoryListCount(inventory));
		writeJSON(response, InventoryView);
	}
	
	@RequestMapping(value = "/getInventoryDetailList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getInventoryDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Inventory inventory = new Inventory();
		 
		
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			inventory.set$like_platform(platform);;
		}
		
		String fdcode = request.getParameter("fdCode");
		if (StringUtils.isNotBlank(fdcode)) {
			inventory.set$like_fdCode(fdcode);;
		}
		
		inventory.setFirstResult(firstResult);
		inventory.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		inventory.setSortedConditions(sortedCondition);
		
		 
		ListView<Inventory> InventoryView = new ListView<Inventory>();
		InventoryView.setData(operateService.getInventoryDetailList(inventory));
		InventoryView.setTotalRecord(operateService.getInventoryDetailListCount(inventory));
		writeJSON(response, InventoryView);
	}
	
	
	@RequestMapping(value = "/getSettleList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSettleList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Settle settle = new Settle();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			settle.setSdate(sdate);
		}
		
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			settle.setEdate(edate);
		}
		
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			settle.set$like_platform(platform);;
		}
		
		String fdcode = request.getParameter("fdCode");
		if (StringUtils.isNotBlank(fdcode)) {
			settle.set$like_fdCode(fdcode);;
		}
		
		String fdType = request.getParameter("fdType");
		if (StringUtils.isNotBlank(fdType)) {
			settle.set$like_type(fdType) ;
		}
		
		settle.setFirstResult(firstResult);
		settle.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		settle.setSortedConditions(sortedCondition);
		
		 
		ListView<Settle> settleView = new ListView<Settle>();
		settleView.setData(operateService.getSettleList(settle));
		settleView.setTotalRecord(operateService.getSettleListCount(settle));
		writeJSON(response, settleView);
	}
	
	
	
	@RequestMapping(value = "/exportInvestList", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportInvestList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
	 
		
		String sortedObject = "date";
		String sortedValue = "desc";
		 
		 
		Operate operate = new Operate();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}
		 
	 
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);
		
		 
	 
	   List<Operate> listOperate= operateService.getOperateList(operate) ;
		 

		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("申购统计"+ sdate +"_" +edate);
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		HSSFCell cell4 = row.createCell(4);
		HSSFCell cell5 = row.createCell(5);
		HSSFCell cell6 = row.createCell(6);
		HSSFCell cell7 = row.createCell(7);
		HSSFCell cell8 = row.createCell(8);
		HSSFCell cell9 = row.createCell(9);
		HSSFCell cell10 = row.createCell(10);
		HSSFCell cell11 = row.createCell(11);
		
		cell0.setCellValue("日期");
		cell1.setCellValue("申购总人数");
		cell2.setCellValue("申购总笔数");
		cell3.setCellValue("申购总金额");
		cell4.setCellValue("申购失败人数");
		cell5.setCellValue("申购失败笔数");
		cell6.setCellValue("申购失败金额");
		cell7.setCellValue("申购成功人数");
		cell8.setCellValue("申购成功笔数");
		cell9.setCellValue("申购成功金额");
		cell10.setCellValue("部分申购成功笔数");
		cell11.setCellValue("部分申购成功金额");
 
		 
	 
		for (int i = 0; i < listOperate.size(); i++) {
			Operate op = listOperate.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);
			cell5 = row.createCell(5);
			cell6 = row.createCell(6);
			cell7 = row.createCell(7);
			cell8 = row.createCell(8);
			cell9 = row.createCell(9);
			cell10 = row.createCell(10);
			cell11= row.createCell(11);
		 
			cell0.setCellValue(op.getDate());
			cell1.setCellValue(op.getInvestTotalPersonNum());
			cell2.setCellValue(op.getInvestTotalNum());
			cell3.setCellValue(op.getInvestTotalMoney());
			cell4.setCellValue(op.getInvestFailPersonNum());
			cell5.setCellValue(op.getInvestFailNum());
			cell6.setCellValue(op.getInvestFailMoney());
			cell7.setCellValue(op.getInvestSuccessPersonNum());
			cell8.setCellValue(op.getInvestSuccessNum());
			cell9.setCellValue(op.getInvestSuccessMoney());
			cell10.setCellValue(op.getInvestPartSuccessNum());
			cell11.setCellValue(op.getInvestPartSuccessMoney());
		 
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 6000);
			sheet.setColumnWidth(11, 6000);
		}
		
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name= "invest_" +sdate +"_" +edate+".xls";
			response.addHeader("Content-Disposition", "attachment;filename="+name);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
 
	@RequestMapping(value = "/exportChannelList", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportChannelList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
	 
		
		String sortedObject = "date";
		String sortedValue = "desc";
		 
		 
		Channel operate = new Channel();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			operate.set$like_platform(platform);
		}
	 
		String channel = request.getParameter("channel");
		if (StringUtils.isNotBlank(channel)) {
			operate.set$like_channel(channel);
		}
		
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);
		
		 
	 
	   List<Channel> listOperate= operateService.getChannelList(operate) ;
		 

		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("申购统计"+ sdate +"_" +edate);
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		HSSFCell cell4 = row.createCell(4);
		HSSFCell cell5 = row.createCell(5);
		HSSFCell cell6 = row.createCell(6);
		HSSFCell cell7 = row.createCell(7);
		HSSFCell cell8 = row.createCell(8);
		HSSFCell cell9 = row.createCell(9);
		HSSFCell cell10 = row.createCell(10);
		HSSFCell cell11 = row.createCell(11);
		
		cell0.setCellValue("日期");
		cell1.setCellValue("平台");
		cell2.setCellValue("渠道名称");
		cell3.setCellValue("注册人数");
		cell4.setCellValue("绑卡人数");
		cell5.setCellValue("新绑卡人数");
		cell6.setCellValue("申购用户数");
		cell7.setCellValue("申购总金额");
		cell8.setCellValue("人均投资金额");
		cell9.setCellValue("新用户申购人数");
		cell10.setCellValue("新用户申购金额");
		cell11.setCellValue("新用户人均投资金额");
 
		 
	 
		for (int i = 0; i < listOperate.size(); i++) {
			Channel op = listOperate.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);
			cell5 = row.createCell(5);
			cell6 = row.createCell(6);
			cell7 = row.createCell(7);
			cell8 = row.createCell(8);
			cell9 = row.createCell(9);
			cell10 = row.createCell(10);
			cell11= row.createCell(11);
		 
			cell0.setCellValue(op.getDate());
			cell1.setCellValue(op.getPlatform());
			cell2.setCellValue(op.getChannel());
			cell3.setCellValue(op.getReg_num());
			cell4.setCellValue(op.getBound_num());
			cell5.setCellValue(op.getNew_bound_num());
			cell6.setCellValue(op.getUser_num());
			cell7.setCellValue(op.getMoney_num());
			cell8.setCellValue(op.getPer_money_num());
			cell9.setCellValue(op.getNew_user_num());
			cell10.setCellValue(op.getNew_money_num());
			cell11.setCellValue(op.getPer_new_money_num());
		 
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 6000);
			sheet.setColumnWidth(11, 6000);
		}
		
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name= "channel_" +sdate +"_" +edate+".xls";
			response.addHeader("Content-Disposition", "attachment;filename="+name);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/exportChannel2List", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportChannel2List(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
	 
		
		String sortedObject = "date";
		String sortedValue = "desc";
		 
		 
		Channel2 operate = new Channel2();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}
		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			operate.set$like_platform(platform);
		}
	 
		String channel = request.getParameter("channel");
		if (StringUtils.isNotBlank(channel)) {
			operate.set$like_channel(channel);
		}
		
		String channel2 = request.getParameter("channel2");
		if (StringUtils.isNotBlank(channel2)) {
			operate.set$like_channel(channel2);
		}
		
		String last = request.getParameter("last");
		if (StringUtils.isNotBlank(last)) {
			operate.set$last(last);
		}
		
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);
		
		 
	 
	   List<Channel2> listOperate= operateService.getChannel2List(operate) ;
		 

		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("渠道统计2"+ sdate +"_" +edate);
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		HSSFCell cell4 = row.createCell(4);
		HSSFCell cell5 = row.createCell(5);
		HSSFCell cell6 = row.createCell(6);
		HSSFCell cell7 = row.createCell(7);
		HSSFCell cell8 = row.createCell(8);
	 
		cell0.setCellValue("日期");
		cell1.setCellValue("注册人数");
		cell2.setCellValue("绑卡人数");
		cell3.setCellValue("申购用户数");
		cell4.setCellValue("首申用户数");
		cell5.setCellValue("新申购用户数");
		cell6.setCellValue("新用户申购占比");
		cell7.setCellValue("新用户申购金额");
		cell8.setCellValue("申购总金额");
	 
		 
	 
		for (int i = 0; i < listOperate.size(); i++) {
			Channel2 op = listOperate.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);
			cell5 = row.createCell(5);
			cell6 = row.createCell(6);
			cell7 = row.createCell(7);
			cell8 = row.createCell(8);
		 
		 
			cell0.setCellValue(op.getDate());
			cell1.setCellValue(op.getReg_num());
			cell2.setCellValue(op.getBound_num());
			cell3.setCellValue(op.getUser_num());
			cell4.setCellValue(op.getFirst_user_num());
			cell5.setCellValue(op.getNew_user_num());
			cell6.setCellValue(op.getNew_user_rate());
			cell7.setCellValue(op.getNew_money_num());
			cell8.setCellValue(op.getMoney_num());
		 
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
		 
		}
		
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name= "channel2_" +sdate +"_" +edate+".xls";
			response.addHeader("Content-Disposition", "attachment;filename="+name);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@RequestMapping(value = "/exportFailList", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportFailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
	 
		
		String sortedObject = "date";
		String sortedValue = "desc";
		 
		 
		Fail fail = new Fail();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			fail.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			fail.setEdate(edate);
		}
		String uid = request.getParameter("uid");
		if (StringUtils.isNotBlank(uid)) {
			fail.set$like_uid(uid);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			fail.set$like_name(name);
		}
		String mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank(mobile)) {
			fail.set$like_mobile(mobile);
		}
		
		fail.setFirstResult(firstResult);
		fail.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		fail.setSortedConditions(sortedCondition);
		
		 
	 
	   List<Fail> listFail= operateService.getFailList(fail) ;
		 

		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("fail"+ sdate +"_" +edate);
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		HSSFCell cell4 = row.createCell(4);
		HSSFCell cell5 = row.createCell(5);
		HSSFCell cell6 = row.createCell(6);
	 
		
		cell0.setCellValue("日期");
		cell1.setCellValue("UID");
		cell2.setCellValue("姓名");
		cell3.setCellValue("手机");
		cell4.setCellValue("申购产品");
		cell5.setCellValue("绑卡银行");
		cell6.setCellValue("失败原因");
	 
		 
	 
		for (int i = 0; i < listFail.size(); i++) {
			Fail op = listFail.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);
			cell5 = row.createCell(5);
			cell6 = row.createCell(6);
			 
		 
			cell0.setCellValue(op.getDate());
			cell1.setCellValue(op.getUid());
			cell2.setCellValue(op.getName());
			cell3.setCellValue(op.getMobile());
			cell4.setCellValue(op.getFund());
			cell5.setCellValue(op.getBank());
			cell6.setCellValue(op.getReason());
			 
		 
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			 
		}
		
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name1= "fail_" +sdate +"_" +edate+".xls";
			response.addHeader("Content-Disposition", "attachment;filename="+name1);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
 
	@RequestMapping(value = "/exportTradeUserList", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportTradeUserList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
	 
		
		String sortedObject = "reg_time";
		String sortedValue = "desc";
		 
		 
		TradeUser operate = new TradeUser();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}
		String uid = request.getParameter("uid");
		if (StringUtils.isNotBlank(uid)) {
			operate.set$like_uid(uid);
		}
	 
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			operate.set$like_name(name);
		}
		
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);
		
		 
	 
	   List<TradeUser> listOperate= operateService.getTradeUserlList(operate) ;
		 

		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("tradeUser"+ sdate +"_" +edate);
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		HSSFCell cell4 = row.createCell(4);
		HSSFCell cell5 = row.createCell(5);
		HSSFCell cell6 = row.createCell(6);
		HSSFCell cell7 = row.createCell(7);
		HSSFCell cell8 = row.createCell(8);
		HSSFCell cell9 = row.createCell(9);
		HSSFCell cell10 = row.createCell(10);
		HSSFCell cell11 = row.createCell(11);
		
		cell0.setCellValue("用户uid");
		cell1.setCellValue("姓名");
		cell2.setCellValue("注册时间");
		cell3.setCellValue("绑卡时间");
		cell4.setCellValue("首次投资时间");
		cell5.setCellValue("首次投资金额");
		cell6.setCellValue("首次投资购买产品名称");
		cell7.setCellValue("首次投资平台");
		cell8.setCellValue("首次投资渠道");
		cell9.setCellValue("二次投资时间");
		cell10.setCellValue("二次投资金额");
		cell11.setCellValue("二次投资购买产品名称");
 
		 
	 
		for (int i = 0; i < listOperate.size(); i++) {
			TradeUser op = listOperate.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);
			cell5 = row.createCell(5);
			cell6 = row.createCell(6);
			cell7 = row.createCell(7);
			cell8 = row.createCell(8);
			cell9 = row.createCell(9);
			cell10 = row.createCell(10);
			cell11= row.createCell(11);
		 
			cell0.setCellValue(op.getUid());
			cell1.setCellValue(op.getName());
			cell2.setCellValue(op.getReg_time());
			cell3.setCellValue(op.getBoundT());
			cell4.setCellValue(op.getFirst_time());
			cell5.setCellValue(op.getFirst_money());
			cell6.setCellValue(op.getFirst_stype());
			cell7.setCellValue(op.getFirst_platform());
			cell8.setCellValue(op.getFirst_channel());
			cell9.setCellValue(op.getSecond_time());
			cell10.setCellValue(op.getSecond_money());
			cell11.setCellValue(op.getSecond_stype());
		 
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 6000);
			sheet.setColumnWidth(11, 6000);
		}
		
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name1= "tradeUser_" +sdate +"_" +edate+".xls";
			response.addHeader("Content-Disposition", "attachment;filename="+name1);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@RequestMapping(value = "/exportNewOldList", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportNewOldList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
	 
		
		String sortedObject = "date";
		String sortedValue = "desc";
		 
		 
		NewOld operate = new NewOld();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}
		 
		
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);
		
		 
	 
	   List<NewOld> listOperate= operateService.getNewOldlList(operate) ;
		 

		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("newOld"+ sdate +"_" +edate);
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		HSSFCell cell4 = row.createCell(4);
		HSSFCell cell5 = row.createCell(5);
		HSSFCell cell6 = row.createCell(6);
		HSSFCell cell7 = row.createCell(7);
		HSSFCell cell8 = row.createCell(8);
		HSSFCell cell9 = row.createCell(9);
		HSSFCell cell10 = row.createCell(10);
		HSSFCell cell11 = row.createCell(11);
		HSSFCell cell12 = row.createCell(12);	 
		HSSFCell cell13 = row.createCell(13);
		HSSFCell cell14 = row.createCell(14);
		
		cell0.setCellValue("日期");
		cell1.setCellValue("申购用户数");
		cell2.setCellValue("新用户申购数");
		cell3.setCellValue("活期宝新用户申购人数");
		cell4.setCellValue("单只基金新用户申购人数");
		cell5.setCellValue("进取型新用户申购人数");
		cell6.setCellValue("保守型新用户申购人数");
		cell7.setCellValue("稳健型新用户申购人数");
		cell8.setCellValue("理财增值型新用户申购人数");
		cell9.setCellValue("理财增值型2(新20档)新用户申购人数");
		cell10.setCellValue("养老型新用户申购人数");
		cell11.setCellValue("买房置业型新用户申购人数");
		cell12.setCellValue("子女教育型新用户申购人数");
		cell13.setCellValue("结婚生子型新用户申购人数");
		cell14.setCellValue("短期理财型新用户申购人数");
		 
	 
		for (int i = 0; i < listOperate.size(); i++) {
			NewOld op = listOperate.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);
			cell5 = row.createCell(5);
			cell6 = row.createCell(6);
			cell7 = row.createCell(7);
			cell8 = row.createCell(8);
			cell9 = row.createCell(9);
			cell10 = row.createCell(10);
			cell11= row.createCell(11);
			cell12= row.createCell(12);
			cell13= row.createCell(13);
			cell14= row.createCell(14);
		 
			cell0.setCellValue(op.getDate());
			cell1.setCellValue(op.getTotal_num());
			cell2.setCellValue(op.getNew_num());
			cell3.setCellValue(op.getCashbao_num());
			cell4.setCellValue(op.getSingle_num());
			cell5.setCellValue(op.getJinqu_num());
			cell6.setCellValue(op.getBaoshou_num());
			cell7.setCellValue(op.getWenjian_num());
			cell8.setCellValue(op.getLicai_num());
			cell9.setCellValue(op.getLicai2_num());
			cell10.setCellValue(op.getYanglao_num());
			cell11.setCellValue(op.getMaifang_num());
			cell12.setCellValue(op.getZinv_num());
			cell13.setCellValue(op.getJiehun_num());
			cell14.setCellValue(op.getDuanqi_num());
		 
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 6000);
			sheet.setColumnWidth(11, 6000);
			sheet.setColumnWidth(12, 6000);
			sheet.setColumnWidth(13, 6000);
			sheet.setColumnWidth(14, 6000);
		}
		
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name1= "newOld_" +sdate +"_" +edate+".xls";
			response.addHeader("Content-Disposition", "attachment;filename="+name1);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/exportMoneyList", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportMoneyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
	 
		
		String sortedObject = "date";
		String sortedValue = "desc";
		 
		 
		Money operate = new Money();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}
		 
		
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);
		
		 
	 
	   List<Money> listOperate= operateService.getMoneylList(operate) ;
		 

		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("money"+ sdate +"_" +edate);
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		HSSFCell cell4 = row.createCell(4);
		HSSFCell cell5 = row.createCell(5);
		HSSFCell cell6 = row.createCell(6);
		HSSFCell cell7 = row.createCell(7);
		HSSFCell cell8 = row.createCell(8);
		HSSFCell cell9 = row.createCell(9);
		HSSFCell cell10 = row.createCell(10);
		HSSFCell cell11 = row.createCell(11);
		HSSFCell cell12 = row.createCell(12);	 
 
		
		cell0.setCellValue("日期");
		cell1.setCellValue("活期宝新用户申购金额");
		cell2.setCellValue("单只基金新用户申购金额");
		cell3.setCellValue("进取型新用户申购金额");
		cell4.setCellValue("保守型新用户申购金额");
		cell5.setCellValue("稳健型新用户申购金额");
		cell6.setCellValue("理财增值型新用户申购金额");
		cell7.setCellValue("理财增值型2(新20档)新用户申购金额");
		cell8.setCellValue("养老型新用户申购金额");
		cell9.setCellValue("买房置业型新用户申购金额");
		cell10.setCellValue("子女教育型新用户申购金额");
		cell11.setCellValue("结婚生子型新用户申购金额");
		cell12.setCellValue("短期理财型新用户申购金额");
		 
	 
		for (int i = 0; i < listOperate.size(); i++) {
			Money op = listOperate.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);
			cell5 = row.createCell(5);
			cell6 = row.createCell(6);
			cell7 = row.createCell(7);
			cell8 = row.createCell(8);
			cell9 = row.createCell(9);
			cell10 = row.createCell(10);
			cell11= row.createCell(11);
			cell12= row.createCell(12);
			 
		 
			cell0.setCellValue(op.getDate());
			cell1.setCellValue(op.getCashbao_num());
			cell2.setCellValue(op.getSingle_num());
			cell3.setCellValue(op.getJinqu_num());
			cell4.setCellValue(op.getBaoshou_num());
			cell5.setCellValue(op.getWenjian_num());
			cell6.setCellValue(op.getLicai_num());
			cell7.setCellValue(op.getLicai2_num());
			cell8.setCellValue(op.getYanglao_num());
			cell9.setCellValue(op.getMaifang_num());
			cell10.setCellValue(op.getZinv_num());
			 
		 
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 6000);
			sheet.setColumnWidth(11, 6000);
			sheet.setColumnWidth(12, 6000);
			 
		}
		
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name1= "money_" +sdate +"_" +edate+".xls";
			response.addHeader("Content-Disposition", "attachment;filename="+name1);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	@RequestMapping(value = "/exportInventoryList", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportInventoryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));

		String sortedObject = "date";
		String sortedValue = "desc";

		Inventory operate = new Inventory();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}

		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			operate.set$like_platform(platform);
		}
		String fdcode = request.getParameter("fdCode");
		if (StringUtils.isNotBlank(fdcode)) {
			operate.set$like_fdCode(fdcode);;
		}

	 
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);

		List<Inventory> listOperate = operateService.exportInventoryList(operate);
		List<String> day = DateUtil.getMonth(sdate, edate);

		HSSFWorkbook workBook = new HSSFWorkbook();
		// 创建sheet页
		HSSFSheet sheet = workBook.createSheet(platform+ sdate + "_" + edate);
		// 设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);

		List<HSSFCell> listCell = new ArrayList<HSSFCell>();
		for (int i = 0; i < day.size(); i++) {
			HSSFCell cell = row.createCell(i + 2);
			cell.setCellValue(day.get(i));
			listCell.add(cell);
		}

		cell0.setCellValue("基金名称");
		cell1.setCellValue("基金代码");

		for (int i = 0; i < listOperate.size(); i++) {
			Inventory op = listOperate.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);

			for (int j = 0; j < listCell.size(); j++) {
				HSSFCell c = listCell.get(j);
				c = row.createCell(j + 2);
				c.setCellValue(op.getDayInventory().get(j));
				sheet.setColumnWidth(j + 2, 6000);
			}

			cell0.setCellValue(op.getFdName());
			cell1.setCellValue(op.getFdCode());

			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);

		}

		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name1 = "Inventory_"+platform + sdate + "_" + edate + ".xls";
			response.addHeader("Content-Disposition", "attachment;filename=" + name1);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

	@RequestMapping(value = "/exportSettleList", method = { RequestMethod.POST, RequestMethod.GET })
	public void exportSettleList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer firstResult = 0;
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));

		String sortedObject = "fdcode";
		String sortedValue = "desc";

		Settle operate = new Settle();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			operate.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			operate.setEdate(edate);
		}

		String platform = request.getParameter("platform");
		if (StringUtils.isNotBlank(platform)) {
			operate.set$like_platform(platform);
		}
		
		String platformName = request.getParameter("platformName");
		if (StringUtils.isNotBlank(platformName)) {
			operate.setPlatforomName(platformName);
		}
		String fdcode = request.getParameter("fdCode");
		if (StringUtils.isNotBlank(fdcode)) {
			operate.set$like_fdCode(fdcode);;
		}

		String fdType = request.getParameter("fdType");
		if (StringUtils.isNotBlank(fdType)) {
			operate.set$like_type(fdType) ;
		}
	 
		operate.setFirstResult(firstResult);
		operate.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		operate.setSortedConditions(sortedCondition);
		HSSFWorkbook workBook = new HSSFWorkbook();
		
		
		float cargoSum =0;
		float nonCargoSum=0;
		operate.set$like_type("1");
		List<Settle> listOperate = operateService.getSettleList( operate );
		
		//计算货基技术服务费基数
		for(int i=0;i<listOperate.size();i++){
			Settle s = listOperate.get(i);
			cargoSum+=Float.parseFloat(s.getTechFee());
		}
		String name ="货基";
		createSheetForCargo(listOperate,workBook,name);
		
		operate.set$like_type("0");
		listOperate = operateService.getSettleList( operate );
		
		//计算货基技术服务费基数
		for(int i=0;i<listOperate.size();i++){
			Settle s = listOperate.get(i);
			nonCargoSum+=Float.parseFloat(s.getTechFee());
		}			
		name ="非货基";
		createSheetForCargo(listOperate,workBook,name);
		
		name="认证费用";
		listOperate=operateService.getAcessFeeList(operate);
		createSheetForAccessAuthCost(listOperate,workBook,name);
		
		name ="总计";
	   	
	    HSSFCellStyle cellStyle = workBook.createCellStyle();   
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框    
		HSSFSheet sheet = workBook.createSheet(name);
		HSSFRow row = sheet.createRow(0);
		
		float settleRate= operateService.getSettleRate();
		 DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
		 
		
		
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		cell0.setCellStyle(cellStyle);
		cell1.setCellStyle(cellStyle);
		cell2.setCellStyle(cellStyle);
		cell3.setCellStyle(cellStyle);
		
		cell0.setCellValue("费用项目");
		cell1.setCellValue("销售商");
		cell2.setCellValue("结算比例");
		cell3.setCellValue("金额(元)");
		
		HSSFRow row1 = sheet.createRow(1);
		HSSFCell cell10 = row1.createCell(0);
		HSSFCell cell11 = row1.createCell(1);
		HSSFCell cell12 = row1.createCell(2);
		HSSFCell cell13 = row1.createCell(3);
		
		
		cell10.setCellStyle(cellStyle);
		cell11.setCellStyle(cellStyle);
		cell12.setCellStyle(cellStyle);
		cell13.setCellStyle(cellStyle);
		
		
		cell10.setCellValue("货基技术服务费基数");
		cell11.setCellValue(operate.getPlatforomName());
		cell12.setCellValue(operateService.getSettleRate()*100+"%");
		cell13.setCellValue(df.format(cargoSum));
		
		HSSFRow row2 = sheet.createRow(2);
		HSSFCell cell20 = row2.createCell(0);
		HSSFCell cell21 = row2.createCell(1);
		HSSFCell cell22 = row2.createCell(2);
		HSSFCell cell23 = row2.createCell(3);
		
		cell20.setCellStyle(cellStyle);
		cell21.setCellStyle(cellStyle);
		cell22.setCellStyle(cellStyle);
		cell23.setCellStyle(cellStyle);
		
		cell20.setCellValue("非货基技术服务费基数");
		cell21.setCellValue(operate.getPlatforomName());
		cell22.setCellValue(operateService.getSettleRate()*100+"%");
		cell23.setCellValue(df.format(nonCargoSum));
		 
		HSSFRow row3 = sheet.createRow(3);
		HSSFCell cell30 = row3.createCell(0);
		HSSFCell cell31 = row3.createCell(1);
		HSSFCell cell32 = row3.createCell(2);
		HSSFCell cell33 = row3.createCell(3);
		
		cell30.setCellStyle(cellStyle);
		cell31.setCellStyle(cellStyle);
		cell32.setCellStyle(cellStyle);
		cell33.setCellStyle(cellStyle);
		
		float accessAuthRate = Float.parseFloat(operateService.getAcessFeeList(operate).get(0).getAccessAuthFee());
		cell30.setCellValue("认证费用");
		cell31.setCellValue(operate.getPlatforomName());
		cell32.setCellValue(operateService.getSettleRate()*100+"%");
		cell33.setCellValue(df.format(-accessAuthRate));
		
		
		

 		HSSFRow row4 = sheet.createRow(4);
	    CellRangeAddress cra=new CellRangeAddress(4, 4, 0, 2);        
        
	    RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, cra, sheet, workBook);
	    RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, cra, sheet, workBook);
	    RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, cra, sheet, workBook);
	    RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, cra, sheet, workBook);
	    
        //在sheet里增加合并单元格  
        sheet.addMergedRegion(cra);  
 
 		HSSFCell cell40 = row4.createCell(0);
 		HSSFCell cell41 = row4.createCell(3);
 		
 		cell40.setCellStyle(cellStyle);
		cell41.setCellStyle(cellStyle);
	 
 		
 		float sum = (nonCargoSum+cargoSum-accessAuthRate)*settleRate;
 		cell40.setCellValue("总计");
		cell41.setCellValue(df.format(sum));
 		
		
		
		
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 2000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 4000);
		 
		
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			String name1= "settle_"+operate.get$like_platform()+"_" +sdate +"_" +edate+".xls";
			response.addHeader("Content-Disposition", "attachment;filename="+name1);
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//货基和非货基计算费率
	public void createSheetForCargo(List<Settle> listOperate,HSSFWorkbook workBook,String name){
		//创建sheet货基
		//HSSFSheet sheet = workBook.createSheet("cargoRate_"+ sdate +"_" +edate);
		HSSFSheet sheet = workBook.createSheet(name);
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		HSSFCell cell4 = row.createCell(4);
		HSSFCell cell5 = row.createCell(5);
		HSSFCell cell6 = row.createCell(6);
		HSSFCell cell7 = row.createCell(7);
		HSSFCell cell8 = row.createCell(8);
		HSSFCell cell9 = row.createCell(9);
		HSSFCell cell10 = row.createCell(10);
		HSSFCell cell11 = row.createCell(11);
		HSSFCell cell12 = row.createCell(12);
		HSSFCell cell13 = row.createCell(13);
		HSSFCell cell14 = row.createCell(14);
 
		
		cell0.setCellValue("基金代码");
		cell1.setCellValue("基金名称");
		cell2.setCellValue("日均保有量(元)");
		cell3.setCellValue("总保有量(元)");
		cell4.setCellValue("累计申购额>=6666.67元");
		cell5.setCellValue("申购笔数<6666.67元");
		cell6.setCellValue("累计申购额(元)");
		cell7.setCellValue("赎回笔数(小于5万元)");
		cell8.setCellValue("赎回笔数(大于等于5万元)");
		cell9.setCellValue("认/收购手续费(元)");
		cell10.setCellValue("销售服务费(元)");
		cell11.setCellValue("管理费分成(元)");
		cell12.setCellValue("支付成本(元)");
		cell13.setCellValue("监督成本(元)");
		cell14.setCellValue("技术服务费(元)");
	 
		 
	 
		for (int i = 0; i < listOperate.size(); i++) {
			Settle op = listOperate.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell4 = row.createCell(4);
			cell5 = row.createCell(5);
			cell6 = row.createCell(6);
			cell7 = row.createCell(7);
			cell8 = row.createCell(8);
			cell9 = row.createCell(9);
			cell10 = row.createCell(10);
			cell11 = row.createCell(11);
			cell12 = row.createCell(12);
			cell13 = row.createCell(13);
			cell14 = row.createCell(14);
			 
		 
			cell0.setCellValue(op.getFdCode());
			cell1.setCellValue(op.getFdName());
			cell2.setCellValue(op.getDayInventory());
			cell3.setCellValue(op.getInventory());
			cell4.setCellValue(op.getSuml6());
			cell5.setCellValue(op.getCounts6());
			cell6.setCellValue(op.getInvest());
			cell7.setCellValue(op.getRedemps5w());
			cell8.setCellValue(op.getRedempl5w());
			cell9.setCellValue(op.getFees());
			cell10.setCellValue(op.getSaleFee());
			cell11.setCellValue(op.getManageFee());
			cell12.setCellValue(op.getPayCost());
			cell13.setCellValue(op.getSupFee());
			cell14.setCellValue(op.getTechFee());
		 
			 
		 
			
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 6000);
			sheet.setColumnWidth(11, 6000);
			sheet.setColumnWidth(12, 6000);
			sheet.setColumnWidth(13, 6000);
			sheet.setColumnWidth(14, 6000);
		}
		
		HSSFRow rowSum = sheet.createRow(listOperate.size()+1);
	    CellRangeAddress cra=new CellRangeAddress(listOperate.size()+1, listOperate.size()+1, 0, 13);        
        
	    
		// 在sheet里增加合并单元格
		sheet.addMergedRegion(cra);

		HSSFCell cellsum0 = rowSum.createCell(0);
		HSSFCell cellsum1 = rowSum.createCell(14);

		cellsum0.setCellValue("总计");

		float sum = 0;
		for (int i = 0; i < listOperate.size(); i++) {
			Settle s = listOperate.get(i);
			sum += Float.parseFloat(s.getTechFee());
		}
		DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
		cellsum1.setCellValue(df.format(sum));
		
	}
	
	
	
	//货基和非货基计算费率
		public void createSheetForAccessAuthCost(List<Settle> listOperate,HSSFWorkbook workBook,String name){
			//创建sheet货基
			//HSSFSheet sheet = workBook.createSheet("cargoRate_"+ sdate +"_" +edate);
			HSSFSheet sheet = workBook.createSheet(name);
			//设置第一行为Header
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell0 = row.createCell(0);
			HSSFCell cell1 = row.createCell(1);
			
			cell0.setCellValue("绑卡成功人数");
			cell1.setCellValue("认证费用");
		 
			for (int i = 0; i < listOperate.size(); i++) {
				Settle op = listOperate.get(i);
				row = sheet.createRow(i + 1);
				cell0 = row.createCell(0);
				cell1 = row.createCell(1);
			 
				cell0.setCellValue(op.getBoundNum());
				cell1.setCellValue(op.getAccessAuthFee());
				
				sheet.setColumnWidth(0, 6000);
				sheet.setColumnWidth(1, 6000);
			 
				 
			}
		}
	

}
