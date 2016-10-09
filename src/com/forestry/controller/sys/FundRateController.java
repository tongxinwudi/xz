/**
 * 
 */
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
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
import com.forestry.model.sys.ErrorLog;
import com.forestry.model.sys.Forestry;
import com.forestry.model.sys.ForestryType;
import com.forestry.model.sys.FundRate;
import com.forestry.model.sys.OperatLog;
import com.forestry.model.sys.RateConfig;
import com.forestry.service.sys.FundInvestService;
import com.forestry.service.sys.FundRateService;
import com.forestry.service.sys.RateConfigService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;
import core.util.ForestryUtils;

/**
 * @author Steve
 *
 */
@Controller
@RequestMapping("/operate/config")
public class FundRateController extends ForestryBaseController<FundRate> {
	@Resource
	private FundRateService frService;
	@Resource
	private RateConfigService rcService;
	@Resource
	private FundInvestService fiService;
	
	@RequestMapping(value = "/getFundRate", method = { RequestMethod.POST, RequestMethod.GET })
	public void getFundRate(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		
		FundRate model = new FundRate();
		String fdcode = request.getParameter("fdcode");
		if (StringUtils.isNotBlank(fdcode)) {
			model.setFdcode(fdcode);
		}
		String name = request.getParameter("name");
		if (StringUtils.isNotBlank(name)) {
			model.setName(name);
		}
		
		String type=request.getParameter("type");
		if(StringUtils.isNotBlank(type))
			model.setType(type);
		
		model.setFirstResult(firstResult);
		model.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		model.setSortedConditions(sortedCondition);
		ListView<FundRate> listView = new ListView<FundRate>();
		listView.setData(frService.getFundRate(model));
		listView.setTotalRecord(frService.getFundRateCount(model));
		writeJSON(response, listView);
	}
	
	@RequestMapping(value = "/importFundRate", method = RequestMethod.POST)
	public void importFundRate(@RequestParam(value = "importedFile", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RequestContext requestContext = new RequestContext(request);
		JSONObject json = new JSONObject();
		if (!file.isEmpty()) {
			if (file.getSize() > 2097152) {
				json.put("msg", requestContext.getMessage("g_fileTooLarge"));
			} else {
				try {
					String originalFilename = file.getOriginalFilename();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
					String fileName = sdf.format(new Date()) + ForestryUtils.getRandomString(3) + originalFilename.substring(originalFilename.lastIndexOf("."));
					File filePath = new File(getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "/static/download/attachment/" + DateFormatUtils.format(new Date(), "yyyyMM")));
					if (!filePath.exists()) {
						filePath.mkdirs();
					}
					String serverFile = filePath.getAbsolutePath() + "\\" + fileName;
					file.transferTo(new File(serverFile));

					String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
					if (!fileType.equalsIgnoreCase("xls") && !fileType.equalsIgnoreCase("xlsx")) {
						json.put("success", false);
						json.put("msg", requestContext.getMessage("g_notValidExcel"));
						writeJSON(response, json.toString());
						return;
					}
					int count = 0;
					StringBuilder stringBuilder = new StringBuilder();
					InputStream xls = new FileInputStream(serverFile);
					Workbook wb = null;
					Sheet sheet = null;
					Row currentRow = null;
					Row headRow = null;
					Cell currentCell = null;
					if (fileType.equals("xls")) {
						wb = new HSSFWorkbook(xls);
					} else if (fileType.equals("xlsx")) {
						wb = new XSSFWorkbook(xls);
					}
					sheet = wb.getSheetAt(0);// excel中至少会存在一个sheet页
					int rowNum = sheet.getPhysicalNumberOfRows();// 物理有效行数
					Object[] rowValues = null;// excel中一行树木信息
					List<Object[]> models = new ArrayList<Object[]>();// excel中全部树木信息
					if (rowNum > 1) {
						headRow = sheet.getRow(0);
						columns: for (int i = 1; i < rowNum; i++) {
							currentRow = sheet.getRow(i);
							if (currentRow != null) {
								rowValues = new Object[5];
								// int cellNum = currentRow.getLastCellNum();// 总单元格数目
								for (short j = 0; j < 5; j++) {
									try {
										currentCell = currentRow.getCell(j);
										Object obj = null;
										if (currentCell == null) {
											obj = "";
										} else {
											switch (currentCell.getCellType()) {
												case Cell.CELL_TYPE_BLANK:
													obj = "";
													break;
												case Cell.CELL_TYPE_STRING:
													obj = currentCell.getRichStringCellValue();
													break;
												case Cell.CELL_TYPE_NUMERIC:
													if(currentCell.equals("")){
														obj="0";
													}else if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
														double d = currentCell.getNumericCellValue();
														Date date = HSSFDateUtil.getJavaDate(d);
														obj = sdfDate.format(date);
													} else {
														NumberFormat nf = NumberFormat.getInstance();
														nf.setGroupingUsed(false);//true时的格式：1,234,567,890
														//obj = nf.format(currentCell.getNumericCellValue());
														
														 DecimalFormat df = new DecimalFormat("0.000000");//格式化小数   
														obj = df.format(currentCell.getNumericCellValue()) ;//返回的是String类型
													}
													break;
												default:
													obj = "";
													break;
											}
										}
										String cellVal = obj.toString();
										rowValues[j] = cellVal;
									} catch (IllegalStateException e) {
										rowValues = null;
										stringBuilder.append("第" + i + "行," + headRow.getCell(j).getRichStringCellValue() + "列输入了非法值，未导入成功！");
										continue columns;
									} catch (Exception e) {
										rowValues = null;
										stringBuilder.append(e.getMessage());
										continue columns;
									}
								}
								if (rowValues != null) {
									models.add(rowValues);
								}
							}
						}
						boolean isFund=true;
//						if(!headRow.getCell(2).getRichStringCellValue().getString().trim().equals("销售服务费率")){
//							if((headRow.getCell(3)==null)||(headRow.getCell(3)!=null&&!headRow.getCell(3).getRichStringCellValue().getString().trim().equals("销售服务费率")))
//								isFund=false;
//						}
						for (int i = 0; i < models.size(); i++) {
							if (StringUtils.isBlank(models.get(i)[0].toString())) {
								stringBuilder.append("第" + (i + 1) + "行记录的基金代码为空值，导入失败。");
								continue;
							}
							FundRate fr=frService.getFundRateByCode(models.get(i)[0].toString());
							if(fr==null){
								fr=new FundRate();
								fr.setFdcode(models.get(i)[0].toString());
								fr.setName(models.get(i)[1].toString());
								//if(isFund){
									fr.setSellrate(Double.parseDouble(models.get(i)[2].toString().isEmpty()?"0":models.get(i)[2].toString())*100);
									fr.setManagerate(Double.parseDouble(models.get(i)[3].toString().isEmpty()?"0":models.get(i)[3].toString())*100);
									fr.setType( models.get(i)[4].toString() );
								//}
								//else{
									//fr.setManagerate(Double.parseDouble(models.get(i)[2].toString().isEmpty()?"0":models.get(i)[2].toString())*100);
									//fr.setType("0");
								//}
									 
								frService.persist(fr);
							}else{
								fr.setName(models.get(i)[1].toString());
								//if(isFund){
									fr.setSellrate(Double.parseDouble(models.get(i)[2].toString().isEmpty()?"0":models.get(i)[2].toString())*100);
									fr.setManagerate(Double.parseDouble(models.get(i)[3].toString().isEmpty()?"0":models.get(i)[3].toString())*100);
									fr.setType( models.get(i)[4].toString() );
								//}
								//else{
									//fr.setManagerate(Double.parseDouble(models.get(i)[2].toString().isEmpty()?"0":models.get(i)[2].toString())*100);
									//fr.setType("0");
								//}
								frService.update(fr);
								 
							}
							count++;
						}

						json.put("success", true);
						json.put("msg", count + "条记录导入完成。" + stringBuilder.toString());
					} else if (rowNum <= 1) {// 表示模版中只存在头部信息
						json.put("success", false);
						json.put("msg", "Excel表格中没有需要导入的内容！");
						writeJSON(response, json.toString());
						return;
					}
					//List<Forestry> list = objectToForestry(models);// Object-->Forestry
					
				} catch (Exception e) {
					e.printStackTrace();
					json.put("success", false);
					json.put("msg", e.getMessage());
					writeJSON(response, json.toString());
				}
			}
		} else {
			json.put("success", false);
			json.put("msg", "文件不存在！");
		}
		writeJSON(response, json.toString());
	}
	
	@RequestMapping("/downloadMoneyFundRateFile")
	public ResponseEntity<byte[]> downloadMoneyFundRateFile() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "template_money.xlsx");
		File filePath = new File(getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "/static/download/attachment/template_money.xlsx"));
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(filePath), headers, HttpStatus.CREATED);
	}
	
	@RequestMapping("/downloadNotMoneyFundRateFile")
	public ResponseEntity<byte[]> downloadNotMoneyFundRateFile() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "template_notmoney.xlsx");
		File filePath = new File(getClass().getClassLoader().getResource("/").getPath().replace("/WEB-INF/classes/", "/static/download/attachment/template_notmoney.xlsx"));
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(filePath), headers, HttpStatus.CREATED);
	}
	
	@RequestMapping("/deleteFundRate")
	public void deleteFundRate(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		boolean flag = frService.deleteByPK(ids);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}

	@Override
	@RequestMapping(value = "/saveFundRate", method = { RequestMethod.POST, RequestMethod.GET })
	public void doSave(FundRate entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		FundRate fundRate = frService.getByProerties("fdcode", entity.getFdcode());
		if (null != fundRate && null == entity.getId()) {
			parameter.setSuccess(false);
		} else {
			if (CMD_EDIT.equals(parameter.getCmd())) {
				frService.update(entity);
			} else if (CMD_NEW.equals(parameter.getCmd())) {
				frService.persist(entity);
			}
			parameter.setCmd(CMD_EDIT);
			parameter.setSuccess(true);
		}
		writeJSON(response, parameter);
	}
	
	@RequestMapping(value = "/getAllRateConfig", method = { RequestMethod.POST, RequestMethod.GET })
	public void getAllRateConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		ListView<RateConfig> listView = new ListView<RateConfig>();
		listView.setData(rcService.getAllRateConfig());
		//listView.setTotalRecord(rcService.getAllRateConfigCount());
		writeJSON(response, listView);
	}
	
	@RequestMapping("/deleteRateConfig")
	public void deleteRateConfig(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids") Long[] ids) throws IOException {
		boolean flag = rcService.deleteByPK(ids);
		if (flag) {
			writeJSON(response, "{success:true}");
		} else {
			writeJSON(response, "{success:false}");
		}
	}

	@RequestMapping(value = "/saveRateConfig", method = { RequestMethod.POST, RequestMethod.GET })
	public void saveRateConfig(RateConfig entity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ExtJSBaseParameter parameter = ((ExtJSBaseParameter) entity);
		RateConfig rate = rcService.getByProerties("id", entity.getId());
		if (null != rate && null == entity.getId()) {
			parameter.setSuccess(false);
		} else {
			if (CMD_EDIT.equals(parameter.getCmd())) {
				rcService.update(entity);
			} else if (CMD_NEW.equals(parameter.getCmd())) {
				rcService.persist(entity);
			}
			parameter.setCmd(CMD_EDIT);
			parameter.setSuccess(true);
		}
		writeJSON(response, parameter);
	}
	
	@RequestMapping(value="/getFundInvestSum",method={RequestMethod.POST,RequestMethod.GET})
	public void getFundInvestSum(OperatLog op,HttpServletRequest request,HttpServletResponse response) throws IOException{
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
		op.setFirstResult(firstResult);
		op.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		op.setSortedConditions(sortedCondition);
		ListView<OperatLog> listView = new ListView<OperatLog>();
		listView.setData(fiService.fundInvestSum(op,true));
		listView.setTotalRecord(fiService.fundInvestSumCount(op));
		writeJSON(response, listView);
	}
	
	@RequestMapping(value="/getFundInvestEveryday", method={RequestMethod.POST,RequestMethod.GET})
	public void getFundInvestEveryday(OperatLog op,HttpServletRequest request,HttpServletResponse response) throws IOException{
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
		op.setFirstResult(firstResult);
		op.setMaxResults(maxResults);
		Map<String, String> sortedCondition = new HashMap<String, String>();
		sortedCondition.put(sortedObject, sortedValue);
		op.setSortedConditions(sortedCondition);
		ListView<OperatLog> listView = new ListView<OperatLog>();
		listView.setData(fiService.fundInvestEveryday(op,true));
		listView.setTotalRecord(fiService.fundInvestEverydayCount(op));
		writeJSON(response, listView);
	}
	
	@RequestMapping(value="/exportFundInvestSum",method={RequestMethod.POST,RequestMethod.GET})
	public void exportFundInvestSum(OperatLog op,HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<OperatLog> list=fiService.fundInvestSum(op,false);		
		//创建一个新的Excel
		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("统计信息");
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		cell0.setCellValue("基金代码");
		cell1.setCellValue("基金名称");
		cell2.setCellValue("时间");
		cell3.setCellValue("投资总额");
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
		for (int i = 0; i < list.size(); i++) {
			OperatLog model = list.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell0.setCellValue(model.getFdcode());
			cell1.setCellValue(model.getAbbrev());
			cell2.setCellValue(sdf.format(model.getOpDate()));
			cell3.setCellValue(model.getSum().toString());
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
		}
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			response.addHeader("Content-Disposition", "attachment;filename=fundInvest.xls");
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/exportFundInvestEveryday", method={RequestMethod.POST,RequestMethod.GET})
	public void exportFundInvestEveryday(OperatLog op,HttpServletRequest request,HttpServletResponse response) throws IOException{
		try {
		List<OperatLog> list=fiService.fundInvestEveryday(op,false);
		//创建一个新的Excel
		HSSFWorkbook workBook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workBook.createSheet("统计信息");
		//设置第一行为Header
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell0 = row.createCell(0);
		HSSFCell cell1 = row.createCell(1);
		HSSFCell cell2 = row.createCell(2);
		HSSFCell cell3 = row.createCell(3);
		cell0.setCellValue("基金代码");
		cell1.setCellValue("基金名称");
		cell2.setCellValue("时间");
		cell3.setCellValue("投资总额");
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
		for (int i = 0; i < list.size(); i++) {
			OperatLog model = list.get(i);
			row = sheet.createRow(i + 1);
			cell0 = row.createCell(0);
			cell1 = row.createCell(1);
			cell2 = row.createCell(2);
			cell3 = row.createCell(3);
			cell0.setCellValue(model.getFdcode());
			cell1.setCellValue(model.getAbbrev());
			cell2.setCellValue(sdf.format(model.getOpDate()));
			cell3.setCellValue(model.getSum().toString());
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
		}
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		
			response.addHeader("Content-Disposition", "attachment;filename=fundInvestDetail.xls");
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
