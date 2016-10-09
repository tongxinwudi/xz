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
import com.forestry.model.sys.Member_week_ration;
import com.forestry.model.sys.MonthInvestGraph;
import com.forestry.model.sys.Profit;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.ForestryTypeService;
import com.forestry.service.sys.MemberService;
import com.forestry.service.sys.ProfitService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;
import core.util.ForestryUtils;

/**
 * 
 *  Class Name: ProfitController.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年7月13日 下午2:09:52    
 *  @version 1.0
 */
@Controller
@RequestMapping("/sys/member")
public class MemberController extends ForestryBaseController<Forestry> {

	private static final Logger log = Logger.getLogger(MemberController.class);
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private MemberService memberService;
 
 
	@RequestMapping(value = "/getMemberRationList", method = { RequestMethod.POST, RequestMethod.GET })
	public void getForestry(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		Member_week_ration member = new Member_week_ration();
		String sdate = request.getParameter("sdate");
		if (StringUtils.isNotBlank(sdate)) {
			member.setSdate(sdate);
		}
		String edate = request.getParameter("edate");
		if (StringUtils.isNotBlank(edate)) {
			member.setEdate(edate);
		}
		
	 
		
		//System.out.println("name="+name);
	 
		member.setFirstResult(firstResult);
		member.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		member.setSortedConditions(sortedCondition);
		
		 
		ListView<Member_week_ration> memberList = new ListView<Member_week_ration>();
		memberList.setData(memberService.getMemberRationList(member));
		memberList.setTotalRecord(memberService.getMemberRationListCount(member));
		writeJSON(response, memberList);
	}

 

}
