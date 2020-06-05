package com.fx.commons.utils.other;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 时间处理工具类
 */
public class DateUtils {
	public static Logger logger = LogManager.getLogger(DateUtils.class.getName()); // 日志记录
	
	//时间格式变量
	public static String yyyy_MM_dd = "yyyy-MM-dd";
	public static String yyyyMMdd = "yyyyMMdd";
	public static String ddMMyyyy = "yyyy/MM/dd";
	public static String yyyyMM = "yyyyMM";
	public static String yyyy_MM = "yyyy-MM";
	public static String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public static String yyyyMMddHHmm = "yyyyMMddHHmm";
	public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static String HH_mm_ss = " HH:mm:ss";
	
	
	/**
	 * 判断2个日期是否一样
	 * @param d1 第一个日期
	 * @param d2 第二个日期
	 * @return true-一样；false-不一样
	 */
	public static boolean isDateSame(Date d1, Date d2){
		boolean fg = false;
		
		try {
			String s1 = DateToStr(yyyy_MM_dd, d1);
			String s2 = DateToStr(yyyy_MM_dd, d2);
			if(s1.equals(s2)){
				fg = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fg;
	}
	/**
	 * 获取年月日
	 * @author xx
	 * @date 20200521
	 * @param date 指定时间
	 */
	public static String getyyyyMMdd(Date date){
		 return DateToStr(yyyyMMdd, date);
	}
	/**
	 * 获取操作时间
	 * @author xx
	 * @date 20200508
	 * @param date 时间
	 * @return 格式：2020年02月23日12:45
	 */
	public static String getOperTime(Date date){
		String time = DateUtils.DateToStr("yyyy年MM月dd日HH:mm", date);
		return time;
	}
	/**
	 * 获取日期
	 * @author qfc
	 * 2017-2-24
	 * @param date 时间
	 * @return 格式：2月23日12:45
	 */
	public static String get_MDM_str(Date date){
		String time = DateUtils.DateToStr("yyyy年MM月dd日HH:mm", date);
		//System.out.println(time.subSequence(5, 6));
		if("0".equals(time.subSequence(5, 6))){
			time = time.substring(6);
		}else{
			time = time.substring(5);
		}
		return time;
	}
	/**
	 * 获取指定日期的时分秒
	 * @author xx
	 * @date 20170823
	 * @param date 指定时间
	 */
	public static String getHHmmss(Date date){
		 return DateToStr(HH_mm_ss, date);
	}
	/**
	 * 获取指定时间的时分
	 * @author xx
	 * @date 20170823
	 * @param date 指定时间
	 */
	public static String getHHmm(Date date){
		 String HHmm=DateToStr(yyyy_MM_dd_HH_mm_ss, date);
		 HHmm=HHmm.substring(HHmm.lastIndexOf("-")+4, HHmm.lastIndexOf("-")+9);
		 return HHmm;
	}
	/**
	 * 符合条件的用户名
	 * @author xx
	 * @date 20170823
	 * @param cName 初始用户
	 * @param drivers 待筛选用户
	 * @param yuTime 预约时间
	 * return
	 */
	public static String getCheckName(String cName,String drivers,Date yuTime){
		Date currentHour = strToDate("HH:mm", getHHmm(yuTime));
		String [] count=drivers.split(",");
		String [] s= new String[2];
		Date sHour=null;
		Date eHour=null;
		boolean check=false;
		for (String each : count) {
			s=each.split("\\|");
			if(s.length>1){
				sHour =strToDate("HH:mm", s[1].split("-")[0]+":00");//开始时间
				if(Integer.parseInt(s[1].split("-")[0])<=Integer.parseInt(s[1].split("-")[1])){//开始时间小于结束时间
				    eHour =strToDate("HH:mm:ss", s[1].split("-")[1]+":59:59");//结束时间
				}else{//分成两段时间判断
				    eHour = strToDate("HH:mm:ss","23:59:59");//第1段结束时间
				    Date sHour1 = strToDate("HH:mm","00:00");//第2段开始时间
				    Date eHour1 = strToDate("HH:mm:ss",s[1].split("-")[1]+":59:59");//第2段结束时间
				    if((currentHour.after(sHour1) && currentHour.before(eHour1)) || 
					    DateToStr("HH:mm", sHour1).equals(DateToStr("HH:mm", currentHour)) || 
					    DateToStr("HH:mm", eHour1).equals(DateToStr("HH:mm", currentHour))){
				    	cName=s[0];
				    	check=true;
				    }
				}
			    if(!check && (currentHour.after(sHour) && currentHour.before(eHour)) ||
			    	DateToStr("HH:mm", sHour).equals(DateToStr("HH:mm", currentHour)) || 
			    	DateToStr("HH:mm", eHour).equals(DateToStr("HH:mm", currentHour))){
			    	cName=s[0];
			    }
			}
		}
		return cName;
	}
	/**
	 * 获取当前用车时间内的时间段对应的平均速度求平均值
	 * @author xx
	 * @date 20190419
	 * @param sT 当前用车开始时间
	 * @param eT 当前用车结束时间
	 * @param avgSpeeds 待匹配时间段速度
	 * return
	 */
	public static Double getAvgSpeed(Date sT,Date eT,String avgSpeeds){
		sT = strToDate("HH:mm", getHHmm(sT));
		eT = strToDate("HH:mm", getHHmm(eT));
		double avgSpeed=30;//默认平均速度为30
		String [] speedCounts=avgSpeeds.split(",");
		String [] speedCount= new String[2];
		String [] timeCount= new String[2];
		Date sHour=null;
		Date eHour=null;
		boolean check=false;//默认跨天
		List<Double> avglist=new ArrayList<Double>();
		for (String each : speedCounts) {
			speedCount=each.split("=");
			timeCount=speedCount[0].split("-");
			if(timeCount.length>1){
				sHour =strToDate("HH:mm", timeCount[0]);//开始时间
				if(Integer.parseInt(timeCount[0].split(":")[0])<=Integer.parseInt(timeCount[1].split(":")[0])){//开始时间小于等于结束时间
				    if(timeCount[1].split(":")[1].equals("00")){//整点小时-1,分钟-1:59秒
				    	eHour = strToDate("HH:mm:ss",(Integer.parseInt(timeCount[1].split(":")[0])-1)+":59:59");//第2段结束时间
				    }else if(timeCount[1].split(":")[1].equals("15") || timeCount[1].split(":")[1].equals("30") 
				    		|| timeCount[1].split(":")[1].equals("45")){//非整点分钟-1:59秒
				    	eHour = strToDate("HH:mm:ss",timeCount[1].split(":")[0]+":"+
				    		(Integer.parseInt(timeCount[1].split(":")[1])-1)+":59");//第2段结束时间
				    }else{
				    	eHour = strToDate("HH:mm:ss", timeCount[1]+":59");//结束时间
				    }
				}else{//分成两段时间判断
				    eHour = strToDate("HH:mm:ss","23:59:59");//第1段结束时间
				    Date sHour1 = strToDate("HH:mm","00:00");//第2段开始时间
				    Date eHour1 = strToDate("HH:mm:ss",timeCount[1]+":59");//第2段结束时间
				    if(timeCount[1].split(":")[1].equals("00")){//整点小时-1,分钟-1:59秒
				    	eHour1 = strToDate("HH:mm:ss",(Integer.parseInt(timeCount[1].split(":")[0])-1)+":59:59");//第2段结束时间
				    }else if(timeCount[1].split(":")[1].equals("15") || timeCount[1].split(":")[1].equals("30") 
				    		|| timeCount[1].split(":")[1].equals("45")){//非整点分钟-1:59秒
				    	eHour1 = strToDate("HH:mm:ss",timeCount[1].split(":")[0]+":"+
				    		(Integer.parseInt(timeCount[1].split(":")[1])-1)+":59");//第2段结束时间
				    }
				    if(isNotConflict(sT,eT,sHour,eHour) || isNotConflict(sT,eT,sHour1,eHour1) ||
				    DateToStr("HH:mm", sHour).equals(DateToStr("HH:mm", sT)) || 
				    DateToStr("HH:mm", eHour).equals(DateToStr("HH:mm", eT)) || 
				    DateToStr("HH:mm", sHour1).equals(DateToStr("HH:mm", sT)) || 
				    DateToStr("HH:mm", eHour1).equals(DateToStr("HH:mm", eT))){
				    	avglist.add(Double.valueOf(speedCount[1]));
				    	check=true;
				    }
				}
			    if(!check && (isNotConflict(sT,eT,sHour,eHour)) ||
			    	DateToStr("HH:mm", sHour).equals(DateToStr("HH:mm", sT)) || 
			    	DateToStr("HH:mm", eHour).equals(DateToStr("HH:mm", eT))){
			    	avglist.add(Double.valueOf(speedCount[1]));
			    }
			}
		}
		if(avglist.size()>0){
			avgSpeed=0;//初始化为0
			for (Double each : avglist) {
				avgSpeed+=each;
			}
			avgSpeed = MathUtils.div(avgSpeed, avglist.size(), 0);
		}
		return avgSpeed;
	}
	/**
	 * 获取指定时间的日
	 * @author xx
	 * 20160727
	 * @param date 指定时间
	 */
	public static String getDay(String date){
		 date=date.substring(date.lastIndexOf("-")+1, date.lastIndexOf("-")+3);
		 if(date.startsWith("0")){
			 date=date.substring(1,2);
		 }
		 return date;
	}
	/**
	 * 获取指定时间的月和日
	 * @author xx
	 * @date 20180802
	 * @param date 指定时间
	 */
	public static String getMonthAndDay(String date){
		String month=date.substring(5,7);
		if(month.startsWith("0")){
			month=month.substring(1,2);
		}
		String day=date.substring(date.lastIndexOf("-")+1, date.lastIndexOf("-")+3);
		 if(day.startsWith("0")){
			 day=day.substring(1,2);
		 }
		 return month+"月"+day+"号";
	}
	/**
	 * 获取指定时间的指定天数后的时间
	 * @param date 指定时间
	 * @param day 指定天数
	 * @return 时间(格式：2016年6月14日)
	 */
	public static String getPayLastTime(Date date, int day){
		String lastPayTime = DateUtils.getPlusDays("yyyy-MM-dd HH:mm:ss", date, day+1);//计算最迟付款时间（用车结束时间+天数）
		return getYMD(DateUtils.strToDate("yyyy-MM-dd HH:mm:ss", lastPayTime));
	}
	
	/**
	 * 获取指定日期的年月日
	 * @author qfc
	 * 2016-6-14
	 * @param date 指定日期 Date类型
	 * @return 返回日期(格式：2016年6月14日)
	 */
	public static String getYMD(Date date){
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);//设置指定时间
		int _year = cl.get(Calendar.YEAR);
		int _month = cl.get(Calendar.MONTH)+1;
		int _day = cl.get(Calendar.DAY_OF_MONTH);
		return _year+"年"+_month+"月"+_day+"日";
	}
	
	/**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * 以yyyyMMddHHmmss为格式的当前系统时间
     * 订单号全部由服务器生成，以防遇到服务端和客户端获取到同一个订单号
     */
	public synchronized static String getOrderNum(int type){
		if(type==0){//内部订单
			return "76"+System.currentTimeMillis()+"K";
		}else if(type==1){ //客车帮订单
			return "86"+System.currentTimeMillis()+"K";
		}else if(type==2){ //接送机普通订单
			return "86J"+System.currentTimeMillis()+"K";
		}else if(type==3){//接送机月结订单
			return "86M"+System.currentTimeMillis()+"K";
		}else if(type==4){//退款单号
			return "RE"+System.currentTimeMillis()+"K";
		}else if(type==5){//外部订单支付编号
			return "98"+System.currentTimeMillis()+"K";
		}else if(type==6){//顺风车,中转订单
			return "66"+System.currentTimeMillis()+"K";
		}else if(type==7){//操作标识号
			return "OP"+System.currentTimeMillis();
		}else if(type==8){// 系统购买短信订单编号
			return "BM"+System.currentTimeMillis();
		}else if(type==9){// 车队账本编号
			return "EW"+System.currentTimeMillis();
		}else if(type==10){// 车队转接订单
			return "86T"+System.currentTimeMillis()+"K";
		}else if(type==11){// 代开发票订单
			return "BILL"+System.currentTimeMillis()+"K";
		}else if(type == 12){// 支付订单编号
			return "P"+System.currentTimeMillis();
		}
		return "NO_WAY";
	}
	/**
     * 以yyyyMMddHHmmss为格式的当前系统时间
     */
	public synchronized static String getCurrTime(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(yyyyMMddHHmmss);
		return df.format(date);
	}
	/**
	 * 将毫秒时间转换成标准时间
	 */
	public static String convertTime(long second){
		Date dat=new Date(second*1000);  
	    Calendar cl = Calendar.getInstance();   
		cl.setTime(dat); 
		DateFormat sdf = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);  
		return sdf.format(cl.getTime());
	}
	/**
	 * 得到当前年份
	 */
	public static String getYear(){
		Calendar calTime = Calendar.getInstance();
		return String.valueOf(calTime.get(Calendar.YEAR));
	}
	/**
	 * 得到当前月份
	 */
	public static String getMonthOfYear(){
		Calendar calTime = Calendar.getInstance();
		String month=String.valueOf(calTime.get(Calendar.MONTH)+1);
		if(month.length()==1){
			month="0"+month; //补足两位
		}
		return month;
	}
	/**
	 * 得到明天开始时间
	 */
	public static Date getTommorow(){
		Calendar cal = Calendar.getInstance();  
	    cal.setTime(getStartTimeOfDay());  
	    cal.add(Calendar.DAY_OF_YEAR, +1);  
	    return cal.getTime(); 
	}
	/**
	 * 得到明天的结束时间
	 */
	public static Date getTommorowEnd(){
		Calendar end=Calendar.getInstance();      
		end.setTime(getStartTimeOfDay());  
		end.add(Calendar.DAY_OF_YEAR, +1); 
		end.set(Calendar.HOUR_OF_DAY, 23); 
		end.set(Calendar.MINUTE, 59); 
		end.set(Calendar.SECOND, 59); 
		return end.getTime();
	}
	/**
	 * 得到当前天
	 */
	public static String getDayOfMonth(){
		Calendar bornTime = Calendar.getInstance();
		String day=String.valueOf(bornTime.get(Calendar.DAY_OF_MONTH));//当前月份的天
		if (day.length()==1){
		    day="0"+day; //补足两位
		}
		return day;
	}
	/**
	 * 得到当前月份和天
	 */
	public static String getMonthAndDay() {
		Calendar bornTime = Calendar.getInstance();
		String month = String.valueOf(bornTime.get(Calendar.MONTH) + 1); // 当前月份
		String day = String.valueOf(bornTime.get(Calendar.DAY_OF_MONTH));// 当前月份的天
		if (month.length() == 1) {
			month = "0" + month; // 补足两位
		}
		if (day.length() == 1) {
			day = "0" + day; // 补足两位
		}
		return month + "-" + day;
	}
	/**
	 * 得到当天的开始时间
	 */
	public static Date getStartTimeOfDay(){
		return std_st(DateToStr(yyyy_MM_dd, new Date()));
	}
	/**
	 * 得到当天的结束时间
	 */
	public static Date getEndTimeOfDay(){
		return std_et(DateToStr(yyyy_MM_dd, new Date()));
	}
	/**
	 * 得到昨天的开始时间
	 */
	public static Date getStartTimeOfLastDay(){
		Calendar start=Calendar.getInstance();      
		start.add(Calendar.DATE,-1);  //设置为-1即为昨天  
		start.set(Calendar.HOUR_OF_DAY, 0); 
		start.set(Calendar.MINUTE, 0); 
		start.set(Calendar.SECOND, 0); 
		return start.getTime();
	}
	/**
	 * 得到昨天的结束时间
	 */
	public static Date getEndTimeOfLastDay(){
		Calendar end=Calendar.getInstance();      
		end.add(Calendar.DATE,-1);
		end.set(Calendar.HOUR_OF_DAY, 23); 
		end.set(Calendar.MINUTE, 59); 
		end.set(Calendar.SECOND, 59); 
		return end.getTime();
	}
	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static String getYearFirst(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		return DateToStr(yyyy_MM_dd_HH_mm_ss, calendar.getTime());
	}
	
	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static String getYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);     
		calendar.set(Calendar.MINUTE, 59);     
		calendar.set(Calendar.SECOND, 59);
		return DateToStr(yyyy_MM_dd_HH_mm_ss, calendar.getTime());
	}
	/**
	 * 获取某年某月的第一天
	 * @param year
	 * @param month
	 */
	public static String getFisrtDayOfMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		cal.set(Calendar.HOUR_OF_DAY, 0);     
		cal.set(Calendar.MINUTE, 0);     
		cal.set(Calendar.SECOND, 0); 
		return DateToStr(yyyy_MM_dd_HH_mm_ss, cal.getTime());
	}
	/**
	 * 获取某年某月的最后一天
	 * @param year
	 * @param month
	 */
	public static String getLastDayOfMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		cal.set(Calendar.HOUR_OF_DAY, 23);     
		cal.set(Calendar.MINUTE, 59);     
		cal.set(Calendar.SECOND, 59);
		return DateToStr(yyyy_MM_dd_HH_mm_ss, cal.getTime());
	}
	/**
	 * 得到当月的第一天的开始时间
	 */
	public static Date getFirstDayOfMonth(){
		Calendar c1 = Calendar.getInstance();  //开始时间
        c1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        c1.set(Calendar.HOUR_OF_DAY, 0);     
        c1.set(Calendar.MINUTE, 0);     
        c1.set(Calendar.SECOND, 0); 
        return c1.getTime();
	}
	/**
	 * 得到当月的最后一天的结束时间
	 */
	public static Date getEndDayOfMonth(){
		Calendar c2 = Calendar.getInstance();  //结束时间
        c2.set(Calendar.DAY_OF_MONTH,c2.getActualMaximum(Calendar.DAY_OF_MONTH));//得到当月最后一天 
        c2.set(Calendar.HOUR_OF_DAY, 23);     
        c2.set(Calendar.MINUTE, 59);     
        c2.set(Calendar.SECOND, 59); 
        return c2.getTime();
	}
	/**
	 * 判断指定的日期是否是当月最后一天
	 */
	public static boolean isLastDayOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) == calendar
				.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	/**
     * 获取指定日期上月的最后一天开始时间
     */
    public static Date getLastDayCurrTimeOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,0);//得到上月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);     
        calendar.set(Calendar.MINUTE, 0);     
        calendar.set(Calendar.SECOND, 0); 
        return calendar.getTime();
    }
	/**
     * 判断指定的日期是否是当月第一天
     */
    public static boolean isFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }
    /**
     * 获得指定日期的上月时间,最后一天不能用这个方法获取，需要先判断是否是最后一天
     */
    public static Date getLastMonthCurrDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
        calendar.set(Calendar.HOUR_OF_DAY, 0);     
        calendar.set(Calendar.MINUTE, 0);     
        calendar.set(Calendar.SECOND, 0); 
        return calendar.getTime();
    }
	/**
	 * 得到上一个月的第一天的开始时间
	 */
    public static Date getFirstDayOfLastMonth(){
    	Calendar c3 = Calendar.getInstance();  //开始时间
    	c3.add(Calendar.MONTH, -1);
    	c3.set(Calendar.DAY_OF_MONTH,1);//设置为1号,开始时间即为上月第一天 
    	c3.set(Calendar.HOUR_OF_DAY, 0);     
    	c3.set(Calendar.MINUTE, 0);     
    	c3.set(Calendar.SECOND, 0); 
    	return c3.getTime();
    }
    /**
     * 得到上一个月的最后一天的结束时间
     */
    public static Date getEndDayOfLastMonth(){
    	Calendar c4 = Calendar.getInstance();  //结束时间
    	c4.set(Calendar.DAY_OF_MONTH,0);//得到上月最后一天 
    	c4.set(Calendar.HOUR_OF_DAY, 23);     
   	   	c4.set(Calendar.MINUTE, 59);     
   	   	c4.set(Calendar.SECOND, 59); 
   	   	return c4.getTime();
    }
    /**
     * 得到上一个月的最后一天的当前时间
     */
    public static Date getEndDayCurrTimeOfLastMonth(Date Date){
    	Calendar c4 = Calendar.getInstance();  //结束时间
    	c4.set(Calendar.DAY_OF_MONTH,0);//得到上月最后一天 
    	c4.set(Calendar.HOUR_OF_DAY, 23);     
   	   	c4.set(Calendar.MINUTE, 59);     
   	   	c4.set(Calendar.SECOND, 59); 
   	   	return c4.getTime();
    }
    /**
	 * 判断今天是星期几
	 */
	public static int getDayOfWeek(){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date()); 
		return cal.get(Calendar.DAY_OF_WEEK)-1;
	}
	
	/**
	 * 判断指定时间是星期几
	 */
	public static int getDayOfWeek(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		return cal.get(Calendar.DAY_OF_WEEK)-1;
	}
	/**
	 * 将字符串时间改成Date类型
	 * 
	 * @param format
	 * @param dateStr
	 * @return
	 */
	public static Date strToDate(String format, String dateStr) {
		Date date = null;
		try {
			if(dateStr != null){
				if(StringUtils.isBlank(format)){
					format = DateUtils.yyyy_MM_dd_HH_mm_ss;
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
				date = simpleDateFormat.parse(dateStr);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}
	/**
	 * 将时间字符串转换成yyyy-MM-dd HH:mm:ss格式的时间对象
	 * @param dateStr 时间字符串
	 */
	public static Date strToDate(String dateStr){
//		logger.info("传入时间字符串：dateStr="+dateStr);
		Date date = null;
		if(StringUtils.isNotEmpty(dateStr) && !dateStr.contains("NaN")){
			try {
				String[] time = dateStr.split(" ");// 获取日期的时间
				if(time.length == 2){// 说明有时间
					String[] t = time[1].split(":");// 获取时、分、秒
					if(t.length == 1){// 只有小时
						dateStr = dateStr + ":00:00";// 拼接分、秒
						
//						logger.info("传入时间字符串，只有小时，拼接分、秒");
					}else if(t.length == 2){// 只有小时、分
						dateStr = dateStr + ":00";// 拼接秒
						
//						logger.info("传入时间字符串，只有小时、分，拼接秒");
					}else{
//						logger.info("传入时间字符串符合格式");
					}
				}else{
					dateStr = dateStr + " 00:00:00";// 拼接时、分、秒
					
//					logger.info("传入时间字符串，无时间，拼接时、分、秒");
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
				date = simpleDateFormat.parse(dateStr);
				
//				logger.info("成功：将时间字符串转换成yyyy-MM-dd HH:mm:ss格式的时间对象");
			} catch (Exception e) {
				date = null;
//				logger.info("异常：将时间字符串转换成yyyy-MM-dd HH:mm:ss格式的时间对象");
			}
		}
		return date;
	}
	/**
	 * 将字符串开始时间改成Date类型
	 * 默认:2017-09-06 00:00:00
	 * @param dateStr 开始时间字符串:2017-09-06
	 */
	public static Date std_st(String stimeStr) {
		Date date = null;
		try {
			if(StringUtils.isNotEmpty(stimeStr) && !stimeStr.contains("NaN")){
				String[] st = stimeStr.split(" ");
				if(st.length > 1){// 存在时分秒
					stimeStr = st[0] + " 00:00:00";
				}else{
					stimeStr = stimeStr + " 00:00:00";
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = simpleDateFormat.parse(stimeStr);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 将字符串结束时间改成Date类型
	 * 默认:2017-09-06 23:59:59
	 * @param dateStr 开始时间字符串:2017-09-06
	 */
	public static Date std_et(String etimeStr) {
		Date date = null;
		try {
			if(StringUtils.isNotEmpty(etimeStr) && !etimeStr.contains("NaN")){
				String[] et = etimeStr.split(" ");
				if(et.length > 1){// 存在时分秒
					etimeStr = et[0] + " 23:59:59";
				}else{
					etimeStr = etimeStr + " 23:59:59";
				}
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = simpleDateFormat.parse(etimeStr);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 将Date时间转成字符串
	 * @param format
	 * @param date
	 * @return
	 */
	public static String DateToStr(Date date) {
		if(date!=null){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
			return simpleDateFormat.format(date);
		}
		return null;
	}
	/**
	 * 将Date时间转成字符串
	 * @param format
	 * @param date
	 * @return
	 */
	public static String DateToStr(String format, Date date) {
		if(date != null){
			if(StringUtils.isBlank(format)){
				format = DateUtils.yyyy_MM_dd_HH_mm_ss;
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(date);
		}
		return null;
	}
	/**
	 * 获取2个字符日期的天数差
	 * @param p_startDate
	 * @param p_endDate
	 * @return 天数差
	 */
	public static long getDaysOfTowDiffDate(String p_startDate, String p_endDate) {
		Date l_startDate = DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, p_startDate);
		Date l_endDate = DateUtils.strToDate(DateUtils.yyyy_MM_dd_HH_mm_ss, p_endDate);
		long l_startTime = l_startDate.getTime();
		long l_endTime = l_endDate.getTime();
		long betweenDays = (long) ((l_endTime - l_startTime) / (1000 * 60 * 60 * 24));
		return betweenDays+1;
	}
	/**
	 * 获取2个时间日期的天数差
	 * @param p_startDate
	 * @param p_endDate
	 * @return 天数差
	 */
	public static long getDaysOfTowDiffDate(Date l_startDate, Date l_endDate) {
		long l_startTime = l_startDate.getTime();
		long l_endTime = l_endDate.getTime();
		long betweenDays = (long) ((l_endTime - l_startTime) / (1000 * 60 * 60 * 24));
		return betweenDays;
	}
	/**
	 * 获取2个时间日期的小时差
	 * @param p_startDate
	 * @param p_endDate
	 * @return 天数差
	 */
	public static long getHoursOfTowDiffDate(Date l_startDate, Date l_endDate) {
		long l_startTime = l_startDate.getTime();
		long l_endTime = l_endDate.getTime();
		long betweenHours = (long) ((l_endTime - l_startTime) / (1000 * 60 * 60));
		return betweenHours;
	}
	/**
	 * 获取2个Date型日期的分钟数差值
	 * @param p_startDate
	 * @param p_endDate
	 * @return 分钟数差值
	 */
	public static long getMinutesOfTowDiffDate(Date p_startDate, Date p_endDate) {
		long l_startTime = p_startDate.getTime();
		long l_endTime = p_endDate.getTime();
		long betweenMinutes = (long) ((l_endTime - l_startTime) / (1000 * 60));
		return betweenMinutes;
	}
	/**
	 * 获取2个Date型日期的秒数差值
	 * @param p_startDate
	 * @param p_endDate
	 * @return 秒数差值
	 */
	public static long getSecondsOfTowDiffDate(Date p_startDate, Date p_endDate) {
		long l_startTime = p_startDate.getTime();
		long l_endTime = p_endDate.getTime();
		long betweenSeconds = (long) ((l_endTime - l_startTime) / (1000));
		return betweenSeconds;
	}
	/**
	 * 获取2个时间戳的秒数差值
	 * @param l_startTime
	 * @param l_endTime
	 * @return 秒数差值
	 */
	public static long getSecondsOfTowSJCDate(long l_startTime, long l_endTime) {
		long betweenSeconds = (long) ((l_endTime - l_startTime) / (1000));
		return betweenSeconds;
	}
	/**
	 * 给出日期添加一段天数后的日期
	 * @param dateStr
	 * @param plus
	 * @return
	 */
	public static String getPlusDays(String format, String dateStr, long plus) {
		if(StringUtils.isBlank(format)){
			format = DateUtils.yyyy_MM_dd_HH_mm_ss;
		}
		Date date;
		try {
			date = DateUtils.strToDate(dateStr);
			long time = date.getTime() + (plus * 24 * 60 * 60 * 1000);
			return DateUtils.DateToStr(format, new Date(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 给出日期添加一段天数后的日期
	 * @param dateStr
	 * @param plus
	 * @return String
	 */
	public static String getPlusDays(String format, Date date, long plus) {
		long time = date.getTime() + (plus * 24 * 60 * 60 * 1000);
		if(StringUtils.isBlank(format)){
			format = DateUtils.yyyy_MM_dd_HH_mm_ss;
		}
		return DateUtils.DateToStr(format, new Date(time));
	}
	/**
	 * 给出日期减去一段天数后的日期
	 * @param dateStr
	 * @param plus
	 * @return
	 */
	public static String getPlusDaysD(String format, Date date, long plus) {
		long time = date.getTime() - (plus * 24 * 60 * 60 * 1000);
		if(StringUtils.isBlank(format)){
			format = DateUtils.yyyy_MM_dd_HH_mm_ss;
		}
		return DateUtils.DateToStr(format, new Date(time));
	}
	/**
	 * 给出日期减去一段天数后的日期
	 * @param dateStr
	 * @param plus
	 * @return date
	 */
	public static Date getPlusDaysD(Date date, long plus) {
		long time = date.getTime() - (plus * 24 * 60 * 60 * 1000);
		return new Date(time);
	}
	/**
	 * 给出日期添加一段秒数后的日期
	 * @param dateStr
	 * @param plus
	 * @return string
	 */
	public static String getPlusSeconds(String format, Date date, long plus) {
		long time = date.getTime() + plus * 1000;
		if(StringUtils.isBlank(format)){
			format = DateUtils.yyyy_MM_dd_HH_mm_ss;
		}
		return DateUtils.DateToStr(format, new Date(time));
	}
	/**
	 * 给出日期添加一段秒数后的日期
	 * @param dateStr
	 * @param plus
	 * @return date
	 */
	public static Date getPlusSecondsDate(String format, Date date, long plus) {
		long time = date.getTime() + plus * 1000;
		if(StringUtils.isBlank(format)){
			format = DateUtils.yyyy_MM_dd_HH_mm_ss;
		}
		return new Date(time);
	}
	/**
	 * 给出日期减去一段秒数后的日期
	 * @param dateStr
	 * @param plus
	 * @return
	 */
	public static Date getPlusSecondsD(Date date, long plus) {
		long time = date.getTime() - plus * 1000;
		return new Date(time);
	}
	/**
	 * 判断四个时间是否有冲突
	 * @return true冲突 false不冲突
	 */
	public static boolean isNotConflict(Date use_start, Date use_end,Date rentTime_start, Date rentTime_end){
		boolean isConflict=false;//默认不冲突
		if(use_start!=null && use_end!=null && rentTime_start!=null && rentTime_end!=null){
			if((use_start.before(rentTime_start) && use_end.after(rentTime_start)) || 
				   (use_start.before(rentTime_end) && use_end.after(rentTime_end))     ||
				   (rentTime_start.before(use_start) && rentTime_end.after(use_start)) || 
				   (rentTime_start.before(use_end) && rentTime_end.after(use_end)) || 
				   use_start.equals(rentTime_start) || use_end.equals(rentTime_end)){
					isConflict=true;
			}
		}
		if(use_end==null){ //针对于单程时间
			if(rentTime_end!=null){ //车辆不是单程日程
				if(rentTime_start.before(use_start) && rentTime_end.after(use_start)){
					isConflict=true;
				}
			}
		}
		if(rentTime_end==null){ //单程日程
			if(use_end!=null){ //非单程订单
				if(use_start.before(rentTime_start) && use_end.after(rentTime_start)){
					isConflict=true;
				}
			}
		}
		return isConflict;
	}
	/**
	 * 得到两个时间之间相差的月份
	 * @author xx
	 * @param start :开始时间
	 * @param end  :结束时间
	 * @return
	 */
	public static int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
	}
	
	/**
	 * 获取完整的时间
	* @param obj 传入的时间：可以是Date时间类型，也可以是时间字符串
	* @param end 时间结尾类型：y-年；M-月；d-日；H-小时；m-分钟；s-秒；
	* @param format 时间格式
	* @return 完整的时间字符串
	 */
	public static String getFullTime(Object obj, String end, 
		String format) throws Exception{
		logger.info("获取完整时间>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		String ts = "";
		
		// 未传入时间格式，则默认一个格式
		if(StringUtils.isBlank(format)) format = yyyy_MM_dd_HH_mm_ss;
		
		// 判断传入时间类型，转换成字符串
		if(obj instanceof Date){
			ts = DateUtils.DateToStr((Date)obj);
		}else if(obj instanceof String){
			ts = obj.toString().trim();// 去掉前后空格
			
			// 处理传入时间参数，即时间字符串最后一位是-、" "、:
			if(ts.lastIndexOf("-") == ts.length() - 1 || 
				ts.lastIndexOf(":") == ts.length() - 1 || 
				ts.lastIndexOf(" ") == ts.length() - 1){
				// 去掉最后一个字符
				ts = ts.substring(0, ts.length() - 1);
			}
		}else{
			logger.info("传入的时间参数：类型不是字符串也不是Date类型");
		}
		
		// 将时间字符串，统一变成格式为yyyy-MM-dd HH:mm:ss
		if(StringUtils.isNotBlank(ts)){
			// 将字符串转换成Date类型
			Date res = DateUtils.strToDate(ts);
			// 将Date类型转换成字符串
			ts = DateUtils.DateToStr(format, res);
			
			if("y".equals(end)){
				if(ts.length() > 19){
					ts = ts.substring(0, 5);
				}else{
					ts = ts.substring(0, 4);
				}
			}else if("M".equals(end)){
				if(ts.length() > 19){
					ts = ts.substring(0, 8);
				}else{
					ts = ts.substring(0, 7);
				}
			}else if("d".equals(end)){
				if(ts.length() > 19){
					ts = ts.substring(0, 11);
				}else{
					ts = ts.substring(0, 10);
				}
			}else if("H".equals(end)){
				if(ts.length() > 19){
					ts = ts.substring(0, 15);
				}else{
					ts = ts.substring(0, 13);
				}
			}else if("m".equals(end)){
				if(ts.length() > 19){
					ts = ts.substring(0, 18);
				}else{
					ts = ts.substring(0, 16);
				}
			}
		}
		
		return ts;
	}
	
	/**
	 * 获取-中文天、小时、分钟
	 * @param times 毫秒数
	 * @return i 结果类型：i不为空，则获取单个，eg：4天左右/3小时左右/2分钟左右/1秒
	 */
	public static String getZHDHm(long times, String type){
		String res = null;
		
		try {
			if(times > 0){
				if(StringUtils.isNotBlank(type)){
					String r = null;
					
					if(times > 1000*60*60*24){// 够1天时间
						long day = times / (1000*60*60*24);
						r = day+"天";
					}else if(times > 1000*60*60){// 够1小时
						long hours = times / (1000*60*60);
						r = hours+"小时";
					}else if(times > 1000*60){// 够1分钟
						long min = times / (1000*60);
						r = min+"分钟";
					}else if(times > 1000){// 够1秒钟
						long s = times / 1000;
						r = s+"秒钟";
					}else{// 不够1秒按1秒计算
						r = "1秒";
					}
					
					res = r;
				}else{
					String r = "";
					
					int day = Integer.parseInt((times / (1000*60*60*24))+"");
					if(day > 0){
						r += day+"天";
					}
					
					int hours = Integer.parseInt(((times - 1000*60*60*24*day) / (1000*60*60))+"");
					if(StringUtils.isNotBlank(r)){
						r += hours+"小时";
					}
					
					int min = Integer.parseInt(((times - 1000*60*60*24*day - 1000*60*60*hours) / (1000*60))+"");
					if(StringUtils.isNotBlank(r)){
						r += min+"分钟";
					}
					
					int s = Integer.parseInt(((times - 1000*60*60*24*day - 1000*60*60*hours - 1000*60*min) / 1000)+"");
					if(StringUtils.isNotBlank(r)){
						r += s+"秒";
					}else{
						r += "1秒";
					}
					
					res = r;
				}
			}
		} catch (Exception e) {
			res = null;
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * 将时间字符串加上上午下午等标识
	 * @author qfc
	 * 2018-11-26
	 * @param timeStr 时间字符串
	 */
	public static String getAMPMText(String timeStr){
		if(StringUtils.isNotBlank(timeStr)){
			// 区分凌晨、上午、下午、晚上
			int spaceIndex = timeStr.lastIndexOf(" ");
			if(spaceIndex != -1){// 存在空格
				String str = "";
				int h = Integer.parseInt(timeStr.substring(spaceIndex + 1, spaceIndex + 3));// 时
				if(h >= 0 && h <= 5){// 凌晨 0-5
					str = " 凌晨";
				}else if(h >= 6 && h <= 12){// 上午 6-12
					str = " 上午";
				}else if(h >= 13 && h <= 19){// 下午 13-19
					str = " 下午";
				}else{// 晚上 20-24
					str = " 晚上";
				}
				
				timeStr = timeStr.replace(" ", str);
			}
		}
		
		return timeStr;
	}
	/**
	 * 数字转中文文字读法
	* @param str 传入的字符串
	* @param type 0-数字；1-数量；
	* @return 数字读法：一零一；数量读法：一百零一；
	 */
	public String getNumChinese(String str, int type){
		String res = "";
		
		if(StringUtils.isNotBlank(str)){
			
		}
		
		return res;
	}
	
	/**
	 * 获取时间的中文表示
	* @param obj 传入的时间：可以是Date时间类型，也可以是时间字符串
	* @param end 时间结尾类型：y-年；M-月；d-日；H-小时；m-分钟；s-秒；
	* @return 时间中文表示字符串，例如：二零一八年十一月十一日，上午十一点十一分十一秒
	 */
	public static String getTimeChinese(Object obj, String end) throws Exception{
		String ts = "", text = "";
		
		// 将传入的时间字符串变成完整的时间字符串格式：2018-11-11 11:11:11
		ts = getFullTime(obj, null, null);
		
		String[] numChinese = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
		
		List<String> tsl = new ArrayList<String>(); // 将时间字符串变成单个字符的数组
		tsl.add(ts.substring(0, 4));
		tsl.add(ts.substring(5, 7));
		tsl.add(ts.substring(8, 10));
		tsl.add(ts.substring(11, 13));
		tsl.add(ts.substring(14, 16));
		tsl.add(ts.substring(17, ts.length() - 1));
		// 遍历列表，更改对应数字为中文文字
		for(int i = 0; i < tsl.size(); i++){
			int n = Integer.parseInt(tsl.get(i));
			
			if(i == 0){// 年
				if(n >= 1900){// 读：公元一百一十一年
					
				}else{// 读：一九零零年
					
				}
			}else if(i == 1){// 月
				
			}else if(i == 2){// 日
				
			}else if(i == 3){// 时
				
			}else if(i == 4){// 分
				
			}else{// 秒
				
			}
			
			
			
			
			if(" ".equals(tsl.get(i))){// 日期与时间的分割
				int h = Integer.parseInt(ts.substring(11, 13));// 时
				if(h >= 0 && h <= 5){// 凌晨 0-5
					
				}else if(h >= 6 && h <= 12){// 上午 6-12
					
				}else if(h >= 13 && h <= 19){// 下午 13-19
					
				}else{// 晚上 20-24
					
				}
				
			}else{// 日期/时间
				
			}
			
			String[] is = tsl.get(i).split("");
			
			for(int j = 0; j < is.length; j++){
				int index = Integer.parseInt(is[j]);
				if(i == 0){// 年份直接数字，例如:二零一八
					is[j] = numChinese[index];
				}else{
					if(index == 0){// 表示：01月、01日、01时、01分、01秒
						is[j] = numChinese[index];
					}else if(index == 1){// 表示：11月、11日、11时、11分、11秒
						
					}else{// 表示：21日、21分、21秒
						
					}
				}
			}
			
			
			
		}
		
		
		text = StringUtils.join(tsl, "");
		System.out.println(text);
		
		
		return text;
	}
	//指定日期N个月后的日期
	public static Date getAfterMonth(Date inputDate,int number) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例   
        Date date = inputDate;   
        try{   
            c.setTime(date);//设置日历时间   
            c.add(Calendar.MONTH,number);//在日历的月份上增加N个月
        }catch(Exception e){  
        	e.printStackTrace();
        }  
        return c.getTime();//得到N个月后的日期   
	}
	//指定日期N个月前的日期
	public static Date getBeforeMonth(Date inputDate,int number) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例   
        Date date = inputDate;   
        try{   
            c.setTime(date);//设置日历时间   
            c.add(Calendar.MONTH,-number);//在日历的月份上减去N个月
        }catch(Exception e){  
        	e.printStackTrace();
        }  
        return c.getTime();//得到N个月后的日期   
	}
	/**
     * 功能描述：返回明天的整几点
     * @param hour 小时
     * @return 返回时间
     */
    public static String getTomorrowHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return DateToStr(yyyy_MM_dd_HH_mm_ss,calendar.getTime());
    }
	/**
     * 功能描述：返回小时
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    /**
     * 功能描述：返回第N天的整10点
     * @param hour 小时
     * @return 返回时间
     */
    public static String getTenClock(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return DateToStr(yyyy_MM_dd_HH_mm_ss,calendar.getTime());
    }
    /**
     * 功能描述：给年月日加横杠
     * @author xx
     * @param date 日期
     * @return yyyy-mm-dd
     */
    public static String getDate(String date) {
    	return date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6, 8);
    }
    /**
     * 功能描述：给时分秒加冒号
     * @author xx
     * @param date 日期
     * @return HH:mm:ss
     */
    public static String getHms(String hms) {
    	return hms.substring(0, 2)+":"+hms.substring(2, 4)+":"+hms.substring(4, 6);
    }
	// 测试入口
	public static void main(String[] args) {
		
		long i = getDaysOfTowDiffDate("2020-05-04 19:28:00", "2020-05-04 20:28:00");
		
		System.out.println(i);
	}
}