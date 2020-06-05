package com.fx.commons.utils.other;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fx.commons.utils.tools.FV;

/**
 * 计算工具类
 */
public class MathUtils {
	/**
	 * 取出一个指定长度大小的随机正整数.
	 * @param length
	 * int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}
	
	/**
	 * 保留指定位数（最大保留小数点后10位数）
	 * @param num 数字字符串
	 * @param len 保留小数点后？位
	 * @return 指定位数的数字字符串
	 */
	public static String saveBit(String num, int len){
		String res = null;
		
		try {
			if(StringUtils.isNotBlank(num)){// 不为空
				if(num.indexOf(".") != -1){// 存在点
					String[] nums = num.split("\\.");
					
					String xs = nums[1];
					if(xs.length() > len){
						xs = xs.substring(0, len);
					}else{
						int l = len - xs.length();// 需要补0的位数
						for(int i = 0; i < l; i++){
							xs += "0";
						}
					}
					
					xs = nums[0] + '.' + xs;
					
					if(FV.isDouble(xs)){// 结果是小数
						res = xs;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("异常：保留指定位数");
		}
		
		return res;
	}
	/**
	 * 返回一个指定范围的随机数保留两位小数.
	 * @param num 基础值
	 * @param max 最大值
	 */
	public static double getRandom(double num,double max) {
		DecimalFormat df =new DecimalFormat("#0.00");
		double random=Double.valueOf(df.format((int)(Math.random()*(max))+Math.random()));
		if(num==0){
			random=Double.valueOf(df.format(Math.random()/10));
			if(random==0.00 || random==0.1){
				random=0.01;
			}
		}
		return random;
	}
	/**
	 * 加法运算
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double add(double d1, double d2, int len) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(Double.toString(d2));
		return round(b1.add(b2).doubleValue(), len);
	}
	/**
	 * 减法运算
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double sub(double d1, double d2, int len) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return round(b1.subtract(b2).doubleValue(), len);
	}
	/**
	 * 乘法运算
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double mul(double d1, double d2, int len) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return round(b1.multiply(b2).doubleValue(), len);
	}
	/**
	 * 除法运算
	 * @param d1
	 * @param d2
	 * @param len
	 * @return
	 */
	public static double div(double d1, double d2, int len) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return round(
				b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue(), len);
	}
	/**
	 * 四舍五入运算
	 * @param d
	 * @param len
	 * @return
	 */
	public static double round(double d, int len) {
		BigDecimal b1 = new BigDecimal(d);
		BigDecimal b2 = new BigDecimal(1);
		// 任何一个数字除以1都是原数字
		// ROUND_HALF_UP是BigDecimal的一个常量，表示进行四舍五入的操作
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * excel数据导入数字处理去掉逗号（200,258,00）
	 */
	public static double acquire(String d1) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setGroupingSeparator(',');
		dfs.setMonetaryDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("###,###.##", dfs);

		Number num = null;
		try {
			num = df.parse(d1);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return num.doubleValue();
	}
	/**
	 * 取两个整数之间的随机整数
	 * @param low
	 * @param high
	 * @return
	 */
	public static int randomInt(int low, int high) {
		int n = ((int) (Math.random() * (high - low))) + low;
		return n;
	}
	/**
	 * 判断是否是数字（整数/小数（不限位数），不包含0，但包含0000，0.000）
	 * @param str 传入字符串
	 * @return true-是数字；false-不是数字；
	 */
	public static boolean isDoubel(String str) { 
	    Pattern pattern = Pattern.compile("^([0-9]+[\\.]?[0-9]+)|[1-9]$");
		return pattern.matcher(str).matches();  
	}
	/**
	 * 判断是否为整数
	 * @param str 传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
	}
	public static void main(String[] args) {
		String str = "103.123";
		System.out.println(saveBit(str, 6));
		System.out.println(randomInt(1,20));
	}
}