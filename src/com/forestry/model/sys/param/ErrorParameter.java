package com.forestry.model.sys.param;

import core.extjs.ExtJSBaseParameter;

public class ErrorParameter extends ExtJSBaseParameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5780667230789779359L;
	
	private String start;
	private String end;
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
}
