package com.forestry.controller.sys;

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

import com.forestry.core.ForestryBaseController;
import com.forestry.model.sys.ErrorLog;
import com.forestry.model.sys.Forestry;
import com.forestry.service.sys.ErrorLogService;

import core.extjs.ListView;
import core.support.QueryResult;

@Controller
@RequestMapping("/error/log")
public class ErrorLogController extends ForestryBaseController<ErrorLog> {
	@Resource
	private ErrorLogService logService;
	
	@RequestMapping(value = "/getPHPLogs", method = { RequestMethod.POST, RequestMethod.GET })
	public void getPHPLogs(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		ErrorLog log = new ErrorLog();
		String uid = request.getParameter("uid");
		if (StringUtils.isNotBlank(uid)) {
			log.setUid(uid);
		}
		String mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank(mobile)) {
			log.setMobile(mobile);
		}
		/*
		String card=request.getParameter("card");
		if(StringUtils.isNotBlank(card)){
			log.setCard(card);
		}
		*/
		String ecode=request.getParameter("ecode");
		if(StringUtils.isNotBlank(ecode)){
			log.setEcode(ecode);
		}
		
		log.setFirstResult(firstResult);
		log.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		log.setSortedConditions(sortedCondition);
		ListView<ErrorLog> logListView = new ListView<ErrorLog>();
		logListView.setData(logService.getLogs(log));
		logListView.setTotalRecord(logService.getLogsCount(log));
		writeJSON(response, logListView);
	}
	
	@RequestMapping(value = "/getDBLogs", method = { RequestMethod.POST, RequestMethod.GET })
	public void getDBLogs(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		ErrorLog log = new ErrorLog();
		String uid = request.getParameter("uid");
		if (StringUtils.isNotBlank(uid)) {
			log.setUid(uid);
		}
		String mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank(mobile)) {
			log.setMobile(mobile);
		}
		/*
		String card=request.getParameter("card");
		if(StringUtils.isNotBlank(card)){
			log.setCard(card);
		}
		*/
		String ecode=request.getParameter("ecode");
		if(StringUtils.isNotBlank(ecode)){
			log.setEcode(ecode.toUpperCase());
		}
		log.setType("db");
		log.setFirstResult(firstResult);
		log.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		log.setSortedConditions(sortedCondition);
		ListView<ErrorLog> logListView = new ListView<ErrorLog>();
		logListView.setData(logService.getLogs(log));
		logListView.setTotalRecord(logService.getLogsCount(log));
		writeJSON(response, logListView);
	}
}
