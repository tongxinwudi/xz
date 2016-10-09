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

import net.sf.json.JSON;
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
import com.forestry.model.sys.Choice;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.ForestryType;
import com.forestry.model.sys.Fund;
import com.forestry.model.sys.Profit;
import com.forestry.service.sys.ChoiceService;
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
@RequestMapping("/sys/choice")
public class ChoiceController extends ForestryBaseController<Forestry> {

	private static final Logger log = Logger.getLogger(ChoiceController.class);
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private ChoiceService choiceService;

	@RequestMapping(value = "/getChoiceSelf", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChoiceSelf(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		Choice choice = new Choice();
		String id = request.getParameter("id");
		if (StringUtils.isNotBlank(id)) {
			choice.set$like_id(id);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			choice.set$like_name(name);
		}

		String type = request.getParameter("type");
		if (StringUtils.isNotBlank(type)) {
			choice.set$type(type);
		}

		choice.setFirstResult(firstResult);
		choice.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		choice.setSortedConditions(sortedCondition);

		ListView<Choice> choiceListView = new ListView<Choice>();
		choiceListView.setData(choiceService.getChoiceSelfList(choice));
		choiceListView.setTotalRecord(choiceService.getChoiceSelfCount(choice));
		writeJSON(response, choiceListView);
	}

	@RequestMapping(value = "/getChoiceSelfGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChoiceSelfGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Choice choice = new Choice();

		List choiceView = choiceService.getChoiceSelfGraphList(choice);
		writeJSON(response, choiceView);
	}
	
	@RequestMapping(value = "/getChoiceRedempSelfGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getChoiceRedempSelfGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Choice choice = new Choice();

		List choiceView = choiceService.getChoiceRedempSelfList(choice);
		writeJSON(response, choiceView);
	}
	
	
	@RequestMapping(value = "/getSelfMonthSumlineGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSelfMonthSumlineGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Choice choice = new Choice();

		List choiceView = choiceService.getMonthSumLineGraph(choice);
		writeJSON(response, choiceView);
	}
	
	@RequestMapping(value = "/getSelfMonthRedemplineGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSelfMonthRedemplineGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Choice choice = new Choice();

		List choiceView = choiceService.getMonthRedempLineGraph(choice);
		writeJSON(response, choiceView);
	}
	
	@RequestMapping(value = "/getSchemaSexGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSchemaSexGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int type = 1;
		if (StringUtils.isNotBlank(request.getParameter("type"))) {
			type=Integer.parseInt(request.getParameter("type"));
		}
		writeJSON(response, choiceService.getSchemaUserCountBySex(type));
	}

	@RequestMapping(value = "/getSchemaAgeGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSchemaAgeGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int type = 1;
		if (StringUtils.isNotBlank(request.getParameter("type"))) {
			type=Integer.parseInt(request.getParameter("type"));
		}
		writeJSON(response, choiceService.getSchemaUserCountByAge(type));
	}
	
	@RequestMapping(value="/getSchemaMonthDistribute", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSchemaMonthDistribute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int type = 1;
		if (StringUtils.isNotBlank(request.getParameter("type"))) {
			type=Integer.parseInt(request.getParameter("type"));
		}
		
		writeJSON(response, choiceService.getSchemaMonthDistribute(type));
	}
	
	@RequestMapping(value = "/getSchemaAgeSumGraph", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSchemaAgeSumGraph(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int type = 1;
		if (StringUtils.isNotBlank(request.getParameter("type"))) {
			type=Integer.parseInt(request.getParameter("type"));
		}
		writeJSON(response, choiceService.getSchemaSumByAge(type));
	}
	
	@RequestMapping(value="/getSchemaMonthSumDistribute", method = { RequestMethod.POST, RequestMethod.GET })
	public void getSchemaMonthSumDistribute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int type = 1;
		if (StringUtils.isNotBlank(request.getParameter("type"))) {
			type=Integer.parseInt(request.getParameter("type"));
		}
		
		writeJSON(response, choiceService.getSchemaMonthSumDistribute(type));
	}
}
