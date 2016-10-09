package core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

 

 

 

/**
 * 时间处理工具类
 * 
 * @author lidl
 * 
 */
public class DateUtil    
{
	/**
	 * 
	 * @return 返回当前时间减3周后的时间
	 */
	public static String lowDateTimeStr()
	{
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -21);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(c.getTime());
	}

	/**
	 * 
	 * @return 返回最后的发布时间
	 */
	public static String lastPTimeStr()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(DateUtil.lastPtime().getTime());
	}

	/**
	 * 根据calendar 日期 ，加减3分钟 返回 calendar数组，index 0:减3分钟后的calendar,index 1:加3分钟后的calendar type: calendar[]
	 * 
	 * @param datetime
	 * @return
	 */
	public static Calendar[] timeArea(Calendar datetime)
	{
		if (null == datetime)
		{
			//modify by lidl:修改取当前服务器时间 为 取最新的发布时间
			//datetime = Calendar.getInstance();
			datetime = DateUtil.lastPtime();
		}
		Calendar begin = (Calendar) datetime.clone();
		begin.add(Calendar.MINUTE, -20);
		Calendar end = (Calendar) datetime.clone();
		end.add(Calendar.MINUTE, 5);
		return new Calendar[] { begin, end };
	}
	
	public static Calendar lastPtime(){
		Calendar c=Calendar.getInstance();
		int min=c.get(Calendar.MINUTE);
		int minSet=min%5;
		if(minSet==0){
			return c;
		}else{
			c.set(Calendar.MINUTE, minSet>=3?min/5*5+3:min/5*5);
			c.set(Calendar.SECOND, 0);
			return c;
		}
	}
	public static String datetimeStr(Calendar c)
	{
		 if(c==null)return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(c.getTime());
	}
	
	public static String datetimeStr(  )
	{
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Calendar.getInstance().getTime());
	}
	
	public static String currentTime(  )
	{
	    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}
	
	
	public static String datetimeStr1(Calendar c)
	{
		 if(c==null)return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
	}
	
	public static String getDateTimeStr(String foramt)
	{
	    return new SimpleDateFormat(foramt).format(Calendar.getInstance().getTime());
	}
	
	public static String dateToString(Date date){
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSSSS");
		if(date==null){
			return df.format(new Date());
		}else{
			return df.format(date);
		}
	}
	
	/**
	 * Calendar字符串转时间
	 * @param str
	 * @return
	 */
	public static Calendar strToCalendar(String str){
		Calendar dayc = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dayc.setTime(date);
		return dayc;
	}
	
	/**
	 * 字符串转Date
	 * @param str
	 * @return
	 */
	public static Date strToDate(String str){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date stringToDate(String string){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			if(string!=null){
				date = df.parse(string);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Calendar strToCalendarWithoutDD(String str){
		Calendar dayc = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dayc.setTime(date);
		return dayc;
	}

	public static Calendar strToCalendarWithoutDD1(String str){
		Calendar dayc = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dayc.setTime(date);
		return dayc;
	}
	
	public static String getScaleTime(Calendar c )
	{
	    return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}
	
	
	public static String getScaleTimeWithoutdd(Calendar c )
	{
	    return new SimpleDateFormat("yyyy-MM").format(c.getTime());
	}
	
	
	public static  List<String> getMonth(String sdate,String edate){
		List<String> list= new ArrayList<String>();
	 
		Calendar c_begin =DateUtil.strToCalendarWithoutDD(sdate);
		Calendar c_end = DateUtil.strToCalendarWithoutDD(edate);
		
		if(c_end.before(c_begin)){
			return list;
		}
		
		list.add(sdate);
		while (c_begin.before(c_end)) {
			
			
			c_begin.add(Calendar.DATE, 1);
			list.add(DateUtil.getScaleTime(c_begin));
		}
		 
		 return list;
	}
	
	
	public static  List<String> getMonthWithoutdd(String sdate,String edate){
		List<String> list= new ArrayList<String>();
	 
		Calendar c_begin =DateUtil.strToCalendarWithoutDD1(sdate);
		Calendar c_end = DateUtil.strToCalendarWithoutDD1(edate);
		
		if(c_end.before(c_begin)){
			return list;
		}
		
		list.add(sdate);
		while (c_begin.before(c_end)) {
			
			
			c_begin.add(Calendar.MONTH, 1);
			list.add(DateUtil.getScaleTimeWithoutdd(c_begin));
		}
		 
		 return list;
	}
	
	public static String currentTimeMinus(int count)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) - count);// 让日期加1
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar
				.getTime());
	}
	
	
	public static String currentMonthMinus(int count)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH,
				calendar.get(Calendar.MONTH) - count);// 让日期加1
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar
				.getTime());
	}
	
	public static String currentMonthMinusWithoutdd(int count)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH,
				calendar.get(Calendar.MONTH) - count);// 让日期加1
		return new SimpleDateFormat("yyyy-MM").format(calendar
				.getTime());
	}
	
	public static void main(String[] args){
		 
		getMonth("2015-10-01", "2015-10-07");
		 
		System.out.println(getDayCount("2015-09-130", "2015-10-01"));
	}
	
	/**
	 * 
	 *  Function:
	 *  @author TongXin  DateTime 2015年9月30日 下午3:18:39
	 *  @param sdate
	 *  @param edate
	 *  @return
	 */
	public static int getDayCount(String sdate, String edate) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(strToDate(sdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(strToDate(edate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days)) + 1;
	}
	
}
