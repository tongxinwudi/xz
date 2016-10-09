package com.forestry.controller.sys;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forestry.core.ForestryBaseController;
import com.forestry.model.sys.AppLog;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.ErrorCommonInfo;
import com.forestry.model.sys.ErrorCountView;
import com.forestry.model.sys.Member_week_ration;
import com.forestry.model.sys.OperatLog;
import com.forestry.model.sys.Profit;
import com.forestry.service.sys.ErrorLogService;
import com.forestry.service.sys.ErrorStatisticsService;

import core.extjs.ListView;

@Controller
@RequestMapping("/sys/error")
public class ErrorController extends ForestryBaseController<OperatLog> {
	@Resource
	private ErrorStatisticsService errorService;
	@Resource
	ErrorLogService errorLog;
	
	@RequestMapping(value = "/getCountGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getCountGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ErrorCommonInfo common=new ErrorCommonInfo();
		String mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank(mobile)) {
		    common.setMobile(mobile);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			common.setName(name);
		}
		String start = request.getParameter("queryStart");
		if (StringUtils.isNotBlank(start)) {
			common.setStart(start);
		}
		String end = request.getParameter("queryEnd");
		if (StringUtils.isNotBlank(end)) {
			common.setEnd(end);
		}
		List<ErrorCountView> list=errorService.getErrorCountPage(common);
		writeJSON(response, list);
	}
	
	@RequestMapping(value = "/getCountMonth", method = { RequestMethod.POST, RequestMethod.GET })
	public void getCountMonth(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ErrorCommonInfo common=new ErrorCommonInfo();
		String mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank(mobile)) {
		    common.setMobile(mobile);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			common.setName(name);
		}
		String start = request.getParameter("queryStart");
		if (StringUtils.isNotBlank(start)) {
			common.setStart(start);
		}
		String end = request.getParameter("queryEnd");
		System.out.println("end="+end);
		if (StringUtils.isNotBlank(end)) {
			common.setEnd(end);
		}
		//List list=errorService.getErrorCountMonthPage(common);
		String l=errorService.getErrorCountMonthPage(common);
		writeJSON(response, l);
	}
	
	@RequestMapping(value = "/getErrorList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getErrorList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		ErrorCommonInfo common=new ErrorCommonInfo();
		common.setFirstResult(firstResult);
		common.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		common.setSortedConditions(sortedCondition);
		
		String mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank(mobile)) {
		    common.setMobile(mobile);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			common.setName(name);
		}
		String start = request.getParameter("queryStart");
		if (StringUtils.isNotBlank(start)) {
			common.setStart(start);
		}
		String end = request.getParameter("queryEnd");
		if (StringUtils.isNotBlank(end)) {
			common.setEnd(end);
		}
		List<ErrorCommonInfo> list=new ArrayList<ErrorCommonInfo>();
		long total=0;
		if(StringUtils.isNotBlank(request.getParameter("type"))){
			int type=Integer.parseInt(request.getParameter("type"));
			switch(type){
				case 1:
					list=errorService.getFundErrorList(common);
					total=errorService.getFundErrorCount(common);
					break;
				case 2:
					list=errorService.getCashbaoErrorList(common);
					total=errorService.getCashbaoErrorCount(common);
					break;
				case 3:
					list=errorService.getUserErrorList(common);
					total=errorService.getUserErrorCount(common);
					break;
			}
		}else{
			list=errorService.getFundErrorList(common);
			total=errorService.getFundErrorCount(common);
		}
		ListView<ErrorCommonInfo> profitListView = new ListView<ErrorCommonInfo>();
		profitListView.setData(list);
		profitListView.setTotalRecord(total);
		
		writeJSON(response,profitListView);
	}
	
	
	@RequestMapping(value = "/getAppLogList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getAppLogList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		AppLog appLog = new AppLog();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			appLog.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			appLog.setEdate(edate);
		}
		
		String mobile = request.getParameter("$like_mobile");
		if (StringUtils.isNotBlank(mobile)) {
			appLog.set$like_mobile(mobile);
		}
		
		String type = request.getParameter("$like_model");
		if (StringUtils.isNotBlank(type)) {
			appLog.set$like_model(type);
		}
		 
		String version = request.getParameter("$like_version");
		if (StringUtils.isNotBlank(version)) {
			appLog.set$like_version(version);
		}
	 
		appLog.setFirstResult(firstResult);
		appLog.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		appLog.setSortedConditions(sortedCondition);
		
		 
		ListView<AppLog> appLogList = new ListView<AppLog>();
		appLogList.setData(errorLog.getAppLogList(appLog));
		appLogList.setTotalRecord(errorLog.getAppLogListCount(appLog));
		writeJSON(response, appLogList);
	}

	@RequestMapping(value = "/getOsTypeList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getOsTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<ChannelType> channelTypeList = errorLog.getOsTypeList(null);
		writeJSON(response, channelTypeList);
	}
}
