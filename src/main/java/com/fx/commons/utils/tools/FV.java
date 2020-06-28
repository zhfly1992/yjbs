package com.fx.commons.utils.tools;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 表单验证工具类
 */
public class FV {
	/** 日志记录 */
	public static Logger logger = LogManager.getLogger(FV.class.getName());
	
	public static void main(String[] args) {
		
		String str = null;
		boolean f = Boolean.parseBoolean(str);
		System.out.println(f);
	}
	
	/**
	 * 验证-字符串是否是-Boolean类型
	 * @param str 验证的字符串
	 * @return true-是；false-不是；
	 */
	public static boolean isBoolean(String str){
		try{
			if(str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false"))return true;
			return false;
	    }catch(Exception e){
	      return false;
	    }
	}
	
	/**
	 * 验证-字符串是否是-Long类型
	 * @param str 验证的字符串
	 * @return true-是；false-不是；
	 */
	public static boolean isLong(String str){
	    try{
	        Long.parseLong(str);
	       
	        return true;
	    }catch(Exception e){
	    	return false;
	    }
	}
	/**
	 * 验证-字符串是否是-Date类型
	 * @param dateStr 验证的字符串
	 * @return true-是；false-不是；
	 */
	public static boolean isDate(String dateStr){
		logger.info("传入时间字符串：dateStr="+dateStr);
		if(StringUtils.isNotEmpty(dateStr) && !dateStr.contains("NaN")){
			try {
				String[] time = dateStr.split(" ");// 获取日期的时间
				if(time.length == 2){// 说明有时间
					String[] t = time[1].split(":");// 获取时、分、秒
					if(t.length == 1){// 只有小时
						dateStr = dateStr + ":00:00";// 拼接分、秒
						
						logger.info("传入时间字符串，只有小时，拼接分、秒");
					}else if(t.length == 2){// 只有小时、分
						dateStr = dateStr + ":00";// 拼接秒
						
						logger.info("传入时间字符串，只有小时、分，拼接秒");
					}else{
						logger.info("传入时间字符串符合格式");
					}
				}else{
					dateStr = dateStr + " 00:00:00";// 拼接时、分、秒
					
					logger.info("传入时间字符串，无时间，拼接时、分、秒");
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				simpleDateFormat.parse(dateStr);
				
				logger.info("成功：将时间字符串转换成yyyy-MM-dd HH:mm:ss格式的时间对象");
				
				return true;
			} catch (Exception e) {
				logger.info("异常：将时间字符串转换成yyyy-MM-dd HH:mm:ss格式的时间对象");
			}
		}
		
		return false;
	}
	
	/**
	 * 获取-字符串字节长度（ISO8859-1，因为此编码一个字符长度为1，无论中英文）
	 * @param str 字符串
	 * @param charset 编码，默认ISO8859-1
	 * @return 字符串长度，异常时为-1
	 */
	public static int getByteLen(String str, String charset){
		try {
			if(StringUtils.isEmpty(charset)) charset = "ISO8859-1";
			return str.getBytes(charset).length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}
	}
	/**
	 * 验证-值是否是指定枚举中的值
	 * @param clazz 枚举类
	 * @param en 枚举字符串值
	 * @return true-是；false-不是；
	 */
	public static boolean isOfEnum(Class<?> clazz, String en){
		Enum<?>[] ens = (Enum<?>[])clazz.getEnumConstants();
		for(Enum<?> e : ens){  
            if(e.name().equals(en)){  
                return true;  
            }
        }  
        return false;  
	}
	/**
	 * 验证-字符串是否包含字母
	 * @param str 验证的字符串
	 * @return true-包含；false-不包含；
	 */
	public static boolean isConLetter(String str){
		return myMatch(".*[a-zA-Z]+.*", str);
	}
	
	/**
	 * 验证-字符串是否包含数字
	 * @param str 验证的字符串
	 * @return true-包含；false-不包含；
	 */
	public static boolean isConNumber(String str){
		return myMatch(".*[0-9]+.*", str);
	}
	/**
	 * 匹配-手机号-失败-提示：手机号由11位纯数字组成
	 */
	public static String match_phone_false_msg = "手机号由11位纯数字组成";
	/**
	 * 手机号-格式验证
	 * @param str 手机号字符串
	 * @return true-格式正确；false-格式错误；
	 */
	public static boolean isPhone(String str) { 
		return myMatch("^1[3|4|5|6|7|8|9]\\d{9}$", str);
	}
	/**
	 * 匹配-座机号-失败-提示：座机号由区号+具体号码的纯数字组成
	 */
	public static String match_homephone_false_msg = "座机号由“区号-具体号码”数字组成，如：028-86999999";
	/**
	 * 座机（家庭电话）-格式验证
	 * @param str 座机号字符串
	 * @return true-格式正确；false-格式错误；
	 */
	public static boolean isHomePhone(String str) { 
		return myMatch("^0\\d{2}-\\d{7,8}$", str);
	}
	/**
	 * 判断是否是数字（整数/小数（不限位数），包含0）
	 * @param str 传入字符串
	 * @return true-是数字；false-不是数字；
	 */
	public static boolean isDouble(String str) {
		if(str.trim().length() > 11) {
			return false;
		}else {
			return myMatch("^([1-9]([0-9]?)+[\\.]?[0-9]+)|[0-9]|([0]\\.[0-9]*)$", str);
		}
	}
	/**
	 * 判断是否为-整数（正整数、负整数、0）
	 * @param str 传入的字符串
	 * @return true-是整数；false-不是整数；
	 */
	public static boolean isInteger(String str) {
		if(str.trim().length() > 11) {
			return false;
		}else {
			return myMatch("^[-\\+]?[\\d]*$", str);
		}
	}
	/**
	 * 判断是否为-正整数（不含0）
	 * @param str 传入的字符串
	 * @return true-是正整数；false-不是正整数；
	 */
	public static boolean isPosInteger(String str) {
		if(str.trim().length() > 11) {
			return false;
		}else {
			return myMatch("^[0-9]*[1-9][0-9]*$", str);
		}
	}
	/**
	 * 判断是否为-负整数
	 * @param str 传入的字符串
	 * @return true-是负整数；false-不是负整数；
	 */
	public static boolean isNegInteger(String str) {
		if(str.trim().length() > 11) {
			return false;
		}else {
			return myMatch("^-[0-9]*[1-9][0-9]*$", str);
		}
	}
	
	/**
	 * 匹配-登录密码-失败-提示：密码由6~18位的英文字母、数字、点、下划线、减号组成
	 */
	public static String match_lpass_false_msg = "密码由6~18位的英文字母、数字、点、下划线、减号组成";
	/**
	 * 判断是否为符合登录密码格式
	 * @param str 传入的字符串
	 * @return true-格式正确；false-格式错误；
	 */
	public static boolean isLPass(String str) {
		 return myMatch("^[\\.A-Wa-z0-9_-]{6,18}$", str);
	}
	
	/**
	 * 匹配-支付密码-失败-提示：支付密码由6位纯数字组成
	 */
	public static String match_ppass_false_msg = "支付密码由6位纯数字组成";
	/**
	 * 判断是否为符合支付密码格式
	 * @param str 传入的字符串
	 * @return true-格式正确；false-格式错误；
	 */
	public static boolean isPPass(String str) {
		 return myMatch("^[0-9]{6}$", str);
	}
	
	
	/**
	 * 正则验证指定字符串
	 * @author qfc
	 * 2018-8-29
	 * @param reg 正则表达式，eg：^[-\\+]?[\\d]*$
	 * @param str 验证的字符串
	 * @return true：匹配成功；false：匹配失败；
	 */
	public static boolean myMatch(String reg, String str){
		Pattern pattern = Pattern.compile(reg);  
        return pattern.matcher(str).matches();
	}
}
