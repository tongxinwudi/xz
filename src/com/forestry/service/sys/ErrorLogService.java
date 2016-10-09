package com.forestry.service.sys;

import java.util.List;

import com.forestry.model.sys.AppLog;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.ErrorLog;

import core.service.Service;

public interface ErrorLogService extends Service<ErrorLog> {
	/**
	 * 通过SSH方式抓取绑卡错误
	 */
	void readLog();
	/**
	 * 返回日志数量
	 * @param log
	 * @return
	 */
	long getLogsCount(ErrorLog log);

	/**
	 * 返回日志列表
	 * @param log
	 * @return
	 */
	List<ErrorLog> getLogs(ErrorLog log);
	/**
	 * 直接读取文件抓取绑卡错误
	 */
	void readLogDirect();
	
	/**
	 * 定时发送邮件
	 */
	void sendErrorMail();
	
	
	List<AppLog> getAppLogList(AppLog appLog);

	long getAppLogListCount(AppLog appLog);
	
	
	List<ChannelType> getOsTypeList(ChannelType channelType);
}
