package com.forestry.dao.sys;

import java.util.List;

import com.forestry.model.sys.AppLog;
import com.forestry.model.sys.ChannelType;
import com.forestry.model.sys.ErrorLog;

import core.dao.Dao;

public interface ErrorLogDao extends Dao<ErrorLog> {

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
	 * 获取要发送邮件的日志
	 * @param lastId，上次发送邮件的最后日志ID
	 * @return
	 */
	List<ErrorLog> getMailLogs();

	/**
	 * 设置邮件已发送
	 * @param ids
	 */
	void updateIsSend(String ids);

	List<AppLog> getAppLogList(AppLog appLog);

	long getAppLogListCount(AppLog appLog);

	List<ChannelType> getOsTypeList(ChannelType channelType);

}
