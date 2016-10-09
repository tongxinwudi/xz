package com.forestry.model.sys;

import com.forestry.model.sys.param.SystemConfigParameter;

public class SystemConfig extends SystemConfigParameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2889768697393548944L;
	private String fieldname;
	private String fieldvalue;
	
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	public String getFieldvalue() {
		return fieldvalue;
	}
	public void setFieldvalue(String fieldvalue) {
		this.fieldvalue = fieldvalue;
	}
	
	
}
