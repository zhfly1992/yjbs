package com.fx.commons.utils.other;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class MyStringUtils{
	
	private final static String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser","Apache-HttpClient/UNAVAILABLE" };
	/**
	 * 中信接口返回状态代码
	 */
	public final static String [] returnStatus=new String[]{"AAAAAAA","AAAAAAB","AAAAAAC","AAAAAAD","AAAAAAE","AAAAAAF"};
	
	/**
	* 判断User-Agent 是不是来自于手机
	* @param ua
	* @return
	*/
	public static boolean checkAgentIsMobile(String ua) {
		boolean flag = false;
		if (!ua.contains("Windows NT")|| (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
			// 排除 苹果桌面系统
			if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
				for (String item : agent) {
					if (ua.contains(item)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
	/**
	 * 手机号替换
	 */
	public static String replaceFiled(String phone){
		if(phone.length()==11){
			phone=phone.substring(0,3)+"****"+phone.substring(7,phone.length());
		}
		return phone;
	}
	/**
	 * 微信获取文件扩展名
	 */
	public static String getFileEndWith(String type){
		if ("image/jpeg".equals(type))
			type = ".jpg";
        else if ("audio/mpeg".equals(type))
        	type = ".mp3";
        else if ("audio/amr".equals(type))
        	type = ".amr";
        else if ("video/mp4".equals(type))
        	type = ".mp4";
        else if ("video/mpeg4".equals(type))
        	type = ".mp4";
		return type;
	}
	/**
	 * ANSI转化为utf-8编码
	 * @param str 
	 * @return
	 */
	public static String ansi2utf(String str){
		
	    return str;   
	}
	/**
	 * GB2312转化为utf-8编码
	 * @param str
	 * @return
	 */
	public static String gb2utf(String str)
	{
		String result = StringUtils.stripToEmpty(str);
		try
		{
			result = new String(result.getBytes("GB2312"), "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * iso8859-1转化为utf-8编码
	 * @param str
	 * @return
	 */
	public static String iso2utf(String str)
	{
		String result = StringUtils.stripToEmpty(str);
		try{
			String en = EncodeUtils.getEncode(str);
			if(!"UTF-8".equals(en)) {
				result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
			}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 页面提交转utf-8
	 * @param str
	 * @return
	 */
	public static String decode(String str){
		try {
			if(StringUtils.isNotEmpty(str)){
				str=URLDecoder.decode(str , "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 得到0-9之间的随机字符
	* @author 作者 qfc 
	* @version 创建时间：2017-8-7 下午10:28:27 
	* @param n 随机字符个数
	* @return 0-9之间的指定个数的随机字符
	 */
	public static String randomStr(int n) {
        String codeContext = "1234567890";
        String str2 = "";
        int len = codeContext.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            str2 = str2 + codeContext.charAt((int) r);
        }
        return str2;
    }
	/**
	 * 对用户卡号进行处理
	 */
	public static String getCard(String str){
		String card=str.substring(0,12);
		int lastSix=Integer.parseInt(str.substring(12,str.length()));
	    String last=six(lastSix);
		return card+last;
	}
	/**
	 * 对用户卡号最后6位进行处理,有4的不要
	 */
	public static String six(int num){
		num++;
		String str=num+"";
		while(str.contains("4")){	
			num++;	
			str=num+"";
		}
		return num+"";
	}
	/**
	 * 对用户卡号3-12位中的任意两位进行处理,有4的不要,0或者1结尾的也不要
	 */
	public static String getTwo(String before){
		if(before.substring(0, 1).equals("0")){
			before=before.substring(1,before.length());
		}
		int after=Integer.parseInt(before);
		after++;
		String str=after+"";
		while(str.contains("4")||(str.endsWith("0"))||str.endsWith("1")){
			after++;
			str=after+"";
		}
		if(str.length()==1){
			str="0"+str;
		}
		return str;
	}
	/**
	 * 根据身份证号确定用户生日
	 */
	public static String getBirthDateFromCard(String cardNumber){
		String card = cardNumber.trim();
		String year;  //年
		String month; //月
		String day;   //日
		if (card.length()==18){ //处理18位身份证
		    year=card.substring(6,10);
		    month=card.substring(10,12);
		    day=card.substring(12,14);
		}else{ //处理非18位身份证
		    year=card.substring(6,8);
		    month=card.substring(8,10);
		    day=card.substring(10,12);
		    year="19"+year;        
	    }
		if (month.length()==1){
		    month="0"+month; //补足两位
		}
		if (day.length()==1){
		    day="0"+day; //补足两位
		}
		return month+"-"+day;
	}
	/** 拼接车辆的车队编号 */  
    public static String joinNo(String obj,String unitNum) {  
        if(StringUtils.isNotEmpty(obj)){
        	if(obj.contains(unitNum)){
        		return obj;
        	}else{
        		return obj+","+unitNum;
        	}
        }
        return unitNum;
    } 
    /**获取商品编号*/
    public static String getComNumber(String number,List<String> list){
		//先升序排序
	    Collections.sort(list, new Comparator<Object>() {
	      @Override
	      public int compare(Object o1, Object o2) {
	        return new Double((String) o1).compareTo(new Double((String) o2));
	      }
	    });    
	    for(int i=0;i<list.size();i++){
	    	if(i+1!=Integer.valueOf(list.get(i))){
				number=i+1+"";
				break;
			}
			if(i+1==list.size()){
				number=list.size()+1+"";
			}
		}
	    if(number.length()==1){
	    	number="0"+number;
	    }
	    return number;
	}
    /**
     * 获取编号size+1
     */
    public static String getSizeCount(String size){
    	if(size.length()==1){
    		size="000"+size;
    	}else if(size.length()==2){
    		size="00"+size;
    	}else if(size.length()==3){
    		size="0"+size;
    	}
    	return size;
    }
    /**
     * 返回交易状态
     */
    public static String replaceTraStu(int state){
    	if(state==0){
    		return "收入";
    	}
    	return "支出";
    }
    /**
     * 获取订单最后4位数
     */
    public static String getLastFourNum(String orderNum){
    	if(StringUtils.isNotEmpty(orderNum)){
    		return orderNum.substring(orderNum.length()-4, orderNum.length());
    	}
    	return "";
    }
    
    /**
     * 获取
     * @author qfc
     * 2018-9-4
     * @return 去除地点的城市
     */
    public static String getPointAddr(String p){
    	String addr = "";
    	
    	if(StringUtils.isNotBlank(p)){
    		if(p.lastIndexOf("-") != -1){// 是微信地图选择的地址
        		addr = p.substring(0, p.lastIndexOf("-"));
        	}else if(p.indexOf(" ") != -1){// 是pc地图选择的地址
        		addr = p.substring(p.lastIndexOf(" "));
        	}else{
        		addr = p; 
        	}
    	}
    	
    	return addr;
    }
    /**
     * @author xx
     * 20190517
     * @return 取消订单,添加,更换驾驶员,重新派单,主订单值改变
     */
    public static String getReplace(String total,String cancelEach,String newEach){
    	if(StringUtils.isNotBlank(total)){
    		String disNum="";//默认全部清空
    		String [] count=total.split("/");
    		for (int i = 0; i < count.length; i++) {
    			if(StringUtils.isNotBlank(cancelEach) && !count[i].contains(cancelEach) && !cancelEach.contains(count[i])){
    				if(StringUtils.isNotEmpty(disNum)){
    					disNum=disNum+"/"+count[i];
    				}else{
    					disNum=count[i];
    				}
    			}
    		}
        	if(disNum==""){//取消完
        		if(StringUtils.isNotBlank(newEach)){//新增一个
        			return newEach;
        		}
        		return null;
        	}else{//没取消完
        		if(StringUtils.isNotBlank(newEach)){//新增一个
        			return disNum+"/"+newEach;
        		}
        		return disNum;
        	}
    	}
    	return newEach;
    }
    /**
     * 将字符串用逗号分隔后用单引号包裹
     * @date 20191204
     * @param xx 
     * @param str
     * @return
     */
    public static String spilt(String str) {
	  StringBuffer sb = new StringBuffer();
	  String[] temp = str.split(",");
	  for (int i = 0; i < temp.length; i++) {
	   if (!"".equals(temp[i]) && temp[i] != null)
	    sb.append("'" + temp[i] + "',");
	  }
	  String result = sb.toString();
	  String tp = result.substring(result.length() - 1, result.length());
	  if (",".equals(tp))
	   return result.substring(0, result.length() - 1);
	  else
	   return result;
    }
    /**
     * 将字符串用逗号分隔后用单引号包裹
     * @date 20200518
     * @param xx 
     * @param plateNum
     * @return
     */
  	public static String getLastNum(String plateNum){
  		String lastNum="";
  		for(int i=plateNum.length()-1;i>=0;i--){
  			if (Character.isDigit(plateNum.charAt(i))){
  				lastNum=String.valueOf(plateNum.charAt(i));
  				break;
  			}
  		}
  		return lastNum;
  	}
    
    public static void main(String[] args) {
		String str = "璇锋眰鎴愬姛";
		System.out.println(iso2utf(str));
		System.out.println(gb2utf(str));
	}
    
}
