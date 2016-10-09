package com.forestry.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.forestry.model.sys.param.AppLogParameter;
import com.forestry.model.sys.param.ChannelParameter;
import com.forestry.model.sys.param.FailParameter;
import com.forestry.model.sys.param.ForestryParameter;
import com.forestry.model.sys.param.OperateParameter;
import com.forestry.model.sys.param.ProfitParameter;
import com.forestry.model.sys.param.TradeUserParameter;
import com.google.common.base.Objects;

import core.extjs.DateTimeSerializer;

 
/**
 * 
 *  Class Name: TradeUser.java
 *  Function:
 *  Modifications:   
 *  @author TongXin  DateTime 2015年8月26日 下午1:59:22    
 *  @version 1.0
 */
@Cache(region = "all", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppLog extends AppLogParameter {

	private static final long serialVersionUID = -1659957832585685551L;
	 
	private String user_id;
	private String mobile;
	private String version;
	private String system_level;
	private String system_version;
	private String model;
	private String channel;
	private String network_type;
	private String time;
	private String error_url;
	private String error_url_params;
	private String error_json;
	private String error_detail;
	private String sys_time;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSystem_level() {
		return system_level;
	}
	public void setSystem_level(String system_level) {
		this.system_level = system_level;
	}
	public String getSystem_version() {
		return system_version;
	}
	public void setSystem_version(String system_version) {
		this.system_version = system_version;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getNetwork_type() {
		return network_type;
	}
	public void setNetwork_type(String network_type) {
		this.network_type = network_type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getError_url() {
		return error_url;
	}
	public void setError_url(String error_url) {
		this.error_url = error_url;
	}
	public String getError_url_params() {
		return error_url_params;
	}
	public void setError_url_params(String error_url_params) {
		this.error_url_params = error_url_params;
	}
	public String getError_json() {
		return error_json;
	}
	public void setError_json(String error_json) {
		this.error_json = error_json;
	}
	public String getError_detail() {
		return error_detail;
	}
	public void setError_detail(String error_detail) {
		this.error_detail = error_detail;
	}
	public String getSys_time() {
		return sys_time;
	}
	public void setSys_time(String sys_time) {
		this.sys_time = sys_time;
	}
 
}
