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
import com.forestry.model.sys.PersonalInfo;
import com.forestry.model.sys.Profit;
import com.forestry.service.sys.ForestryService;
import com.forestry.service.sys.ForestryTypeService;
import com.forestry.service.sys.MemberService;
import com.forestry.service.sys.PersonalService;
import com.forestry.service.sys.ProfitService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.support.QueryResult;
import core.util.ForestryUtils;

/**
 * 
 *  Class Name: PersonalController.java
 *  Function:个人信息
 *  Modifications:   
 *  @author TongXin  DateTime 2015年9月19日 上午10:07:09    
 *  @version 1.0
 */
@Controller
@RequestMapping("/sys/personal")
public class PersonalController extends ForestryBaseController<Forestry> {

	private static final Logger log = Logger.getLogger(PersonalController.class);
	private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private PersonalService personalService;
 
 
	@RequestMapping(value = "/getPersonalInfoList", method = { RequestMethod.POST, RequestMethod.GET })
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
		PersonalInfo personal = new PersonalInfo();
		String $like_code = request.getParameter("code");
		if (StringUtils.isNotBlank($like_code)) {
			personal.set$like_code($like_code);
		}
		
		String $like_identity = request.getParameter("identity");
		if (StringUtils.isNotBlank($like_identity)) {
			personal.set$like_identity($like_identity);
		}
		 
		String $like_mobile = request.getParameter("mobile");
		if (StringUtils.isNotBlank($like_mobile)) {
			personal.set$like_mobile($like_mobile);
		}
		
		String $like_name = request.getParameter("name");
		if (StringUtils.isNotBlank($like_name)) {
			personal.set$like_name($like_name);
		}
		
		String $like_uid = request.getParameter("uid");
		if (StringUtils.isNotBlank($like_uid)) {
			personal.set$like_uid($like_uid);;
		}
		
		String $lile_boundCode = request.getParameter("boundCode");
		if (StringUtils.isNotBlank($lile_boundCode)) {
			personal.set$lile_boundCode($lile_boundCode);
		}
		
		String s_date_reg = request.getParameter("s_date_reg");
		if (StringUtils.isNotBlank(s_date_reg)) {
			personal.setS_date_reg(s_date_reg); 
		}
		
		String e_date_reg = request.getParameter("e_date_reg");
		if (StringUtils.isNotBlank(e_date_reg)) {
			personal.setE_date_reg(e_date_reg); 
		}
		
		String s_date_bound = request.getParameter("s_date_bound");
		if (StringUtils.isNotBlank(s_date_bound)) {
			personal.setS_date_bound(s_date_bound); 
		}
		
		String e_date_bound = request.getParameter("e_date_bound");
		if (StringUtils.isNotBlank(e_date_bound)) {
			personal.setE_date_bound(e_date_bound); 
		}
		
	 
		personal.setFirstResult(firstResult);
		personal.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		personal.setSortedConditions(sortedCondition);
		
		 
		ListView<PersonalInfo> personaList = new ListView<PersonalInfo>();
		personaList.setData(personalService.getPersonalList(personal));
		personaList.setTotalRecord(personalService.getPersonalListCount(personal));
		writeJSON(response, personaList);
	}

 

}
