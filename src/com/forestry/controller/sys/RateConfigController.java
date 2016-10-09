/**
 * 
 */
package com.forestry.controller.sys;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.forestry.core.ForestryBaseController;
import com.forestry.model.sys.FundRate;
import com.forestry.model.sys.RateConfig;
import com.forestry.service.sys.RateConfigService;

import core.extjs.ExtJSBaseParameter;
import core.extjs.ListView;

/**
 * @author Steve
 *
 */
@Controller
@RequestMapping("/operate/rateconfig")
public class RateConfigController extends ForestryBaseController<RateConfig> {
	@Resource
	private RateConfigService rcService;
	
	@RequestMapping(value = "/getAllRateConfig", method = { RequestMethod.POST, RequestMethod.GET })
	public void getAllRateConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		ListView<RateConfig> listView = new ListView<RateConfig>();
		listView.setData(rcService.getAllRateConfig());
		listView.setTotalRecord(rcService.getAllRateConfigCount());
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
}
