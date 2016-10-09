package com.forestry.controller.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.ForestryType;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.FundHold;
import com.forestry.model.sys.Profit;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.ForestryTypeService;
import com.forestry.service.sys.FundService;
import com.forestry.service.sys.ProfitService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;
import core.util.ForestryUtils;

/**
 * 
 * Class Name: ProfitController.java Function: Modifications:
 * 
 * @author TongXin DateTime 2015年7月13日 下午2:09:52
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/fund")
public class FundController extends ForestryBaseController<Forestry> {

	private static final Logger log = Logger.getLogger(FundController.class);
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private FundService fundService;

	@RequestMapping(value = "/getFundLimitGraph", method = {
			RequestMethod.POST, RequestMethod.GET })
	public void getProfitGraph(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Fund fund = new Fund();

		List<Fund> fundListView = fundService.getFundLimitGraphList(fund);
		writeJSON(response, fundListView);
	}

	@RequestMapping(value = "/getFund", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getFund(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(
				request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Fund fund = new Fund();
		String mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank(mobile)) {
			fund.set$like_mobile(mobile);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			fund.set$like_name(name);
		}

		String sinvest = request.getParameter("sinvest");
		if (StringUtils.isNotBlank(sinvest)) {
			fund.set$sinvest(sinvest);
		}

		String einvest = request.getParameter("einvest");
		if (StringUtils.isNotBlank(einvest)) {
			fund.set$einvest(einvest);
		}

		fund.setFirstResult(firstResult);
		fund.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		fund.setSortedConditions(sortedCondition);

		ListView<Fund> fundListView = new ListView<Fund>();
		fundListView.setData(fundService.getFundList(fund));
		fundListView.setTotalRecord(fundService.getFundListCount(fund));
		writeJSON(response, fundListView);
	}

	@RequestMapping(value = "/getFundFrequency", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getFundFrequency(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(
				request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		Fund fund = new Fund();
		String id = request.getParameter("id");
		if (StringUtils.isNotBlank(id)) {
			fund.set$like_id(id);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			fund.set$like_name(name);
		}

		String type = request.getParameter("type");
		if (StringUtils.isNotBlank(type)) {
			fund.set$type(type);
		}

		fund.setFirstResult(firstResult);
		fund.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		fund.setSortedConditions(sortedCondition);

		ListView<Fund> fundListView = new ListView<Fund>();
		fundListView.setData(fundService.getFundFrequencyList(fund));
		fundListView.setTotalRecord(fundService.getFundFrequencyCount(fund));
		writeJSON(response, fundListView);
	}
	
	@RequestMapping(value = "/getFundFrequencyMonth", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getFundFrequencyMonth(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		writeJSON(response, fundService.getFundFrequencyMonth());
	}
	
	@RequestMapping(value = "/getAssemblyFrequencyMonth", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getAssemblyFrequencyMonth(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		writeJSON(response, fundService.getAssemblyFrequencyMonth());
	}
	
	@RequestMapping(value = "/getCashbaoFrequencyMonth", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getCashbaoFrequencyMonth(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		writeJSON(response, fundService.getCashbaoFrequencyMonth());
	}

	@RequestMapping(value = "/getFundHold", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getFundHold(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Integer firstResult = Integer.valueOf(request.getParameter("start"));
		Integer maxResults = Integer.valueOf(request.getParameter("limit"));
		String sortedObject = null;
		String sortedValue = null;
		List<LinkedHashMap<String, Object>> sortedList = mapper.readValue(
				request.getParameter("sort"), List.class);
		for (int i = 0; i < sortedList.size(); i++) {
			Map<String, Object> map = sortedList.get(i);
			sortedObject = (String) map.get("property");
			sortedValue = (String) map.get("direction");
		}
		FundHold fund = new FundHold();
		String fdcode = request.getParameter("fdcode");
		if (StringUtils.isNotBlank(fdcode)) {
			fund.setFdcode(fdcode);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			fund.setName(name);
		}
		String type = request.getParameter("type");
		if (StringUtils.isNotBlank(type)) {
			fund.setType(Integer.parseInt(type));
		}

		fund.setFirstResult(firstResult);
		fund.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		fund.setSortedConditions(sortedCondition);

		ListView<FundHold> fundListView = new ListView<FundHold>();
		fundListView.setData(fundService.getAllFundHoldTime(fund));
		fundListView.setTotalRecord((long) fundService.getAllFundCount(fund));
		writeJSON(response, fundListView);
	}

	@RequestMapping(value = "/getFundHoldGraph", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getFundHoldGraph(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		writeJSON(response, fundService.getAllFundHoldTime(null));
	}

	@RequestMapping(value = "/saveFundHoldTime", method = RequestMethod.GET)
	public void saveFundHoldTime(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		fundService.saveFundHoldTime();
		writeJSON(response, "success!");
	}
	
	@RequestMapping(value = "/getFundLimitLineGraph", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void getFundLimitLineGraph(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		writeJSON(response, fundService.getFundlimtLineGraph(null));
	}

}
