package com.fx.commons.utils.other;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fx.commons.hiberantedao.pagingcom.Filtration;


/**
 * 文件工具类
 * 
 */
public class UtilFile {
	/**
	 * 文件存放根目录 /yjbsFile
	 */
	public static String MFILEROOT = "/yjbsFile";
	/**
	 * 用户头像保存地址
	 */
	public static String USER_HEAD_IMG_FILEURL = "/yjbsFile/headImg";
	/**
	 * 后台管理文件保存地址
	 */
	public static String MFILEURL = "/yjbsFile/backImg";
	/**
	 * 单位管理文件保存地址
	 */
	public static String COMPANYFILE = "C://yjbsFile/companyImg";
	/**
	 * 单位管理文档保存地址
	 */
	public static String COMPANYEXCEL = "C://yjbsFile/companyExcel";
	/**
	 * JXL临时文件默认位置
	 */
	public static String JXL_TEMP_DIR = "/";
	
	/**
	 * 广告图片保存地址 F:/ad_images/ad_imgs
	 */
	public static String AD_IMG_FILE_URL = "C:/ad_images/ad_imgs";
	
	/**
	 * 景点说明图片-存储目录
	 */
	public static String JD_FILE_ROOT_DIR = MFILEROOT+"/jd_imgs";
	
	/** 记账报销-文件-路径：/yjbsFile/jzbx */
	public static String JZBX_FILE_PATH = MFILEROOT+"/jzbx";
	
	/**
	 * 上传文件消息变量
	 */
	public static String MSGSTR = "";
/////////////////////导出数据封装方法--begin///////////////////////////
	/**
	 * 根据查询类型获取类型对应字符串
	 * @author qfc
	 * 2016-9-19
	 * @param matchType 查询类型
	 * @return 在sql中对应字符串 eg:LE -- <=
	 */
	public static String getMatchTypeStr(String matchType) {
		if ("EQ".equals(matchType)) {
			return "=";
		} else if ("NE".equals(matchType)) {
			return "<>";
		} else if ("LIKE".equals(matchType) || "LIKE_".equals(matchType)) {
			return "like";
		} else if ("IN".equals(matchType)) {
			return "in";
		} else if ("LT".equals(matchType)) {
			return "<";
		} else if ("LE".equals(matchType)) {
			return "<=";
		} else if ("GT".equals(matchType)) {
			return ">";
		} else if ("GE".equals(matchType)) {
			return ">=";
		}else if("ISNULL".equals(matchType)){
			return "is null";
		}else if("ISNOTNULL".equals(matchType)){
			return "is not null";
		}else{
			return "";
		}
	}
	/**
	 * 获取hql条件拼接字符串
	 * @author qfc 2016-1-11
	 * @param filtrations 条件封装集合
	 * @return 如：and name like ? and age > ?
	 */
	public static String getHqlFiltStr(List<Filtration> filtrations) {
		String hqlStr = "";//返回字符串变量
		for (int i = 0; i < filtrations.size(); i++) {
			String[] pars = filtrations.get(i).getFieldNames();
			if(pars.length > 1){
				hqlStr += " and (";
				for(int j = 0; j < pars.length; j++){
					hqlStr += pars[j]+ " ";
					if("ISNULL".equals(filtrations.get(i).getMatchType().toString())){
						hqlStr += "is null";//eg:name is null
					}else if("ISNOTNULL".equals(filtrations.get(i).getMatchType().toString())){
						hqlStr += "is not null";//eg:name is not null
					}else{
						hqlStr += getMatchTypeStr(filtrations.get(i).getMatchType().toString()) + " ?";//eg:name = ?
					}
					if((j+1) < pars.length){
						hqlStr += " or ";
					}
				}
				hqlStr += ")";
			}else{
				hqlStr += " and "+ filtrations.get(i).getFieldName() + " ";
				if("ISNULL".equals(filtrations.get(i).getMatchType().toString())){
					hqlStr += "is null";//eg:name is null
				}else if("ISNOTNULL".equals(filtrations.get(i).getMatchType().toString())){
					hqlStr += "is not null";//eg:name is not null
				}else{
					hqlStr += getMatchTypeStr(filtrations.get(i).getMatchType().toString()) + " ? ";//eg:name = ?
				}
			}
		}
		return hqlStr;
	}
	/**
	 *  怎么定义
	 *  Object[] sys_t = new Object[meinfo.size()];//定义类型的数组
	 *	for(int i = 0; i < meinfo.size(); i++){
	 *		sys_t[i] = meinfo.get(i).getMbName().trim();
	　*	}
	 *	filtrations.add(new Filtration(MatchType.IN, sys_t, "mbName"));//必须属于当前商家会员的提现处理记录
	 *
	 *  查询导出数据
	 *	String hqlExcel = " from DrawCashList where 1=1 "+Util.getHqlFiltStr2(filtrations)+" order by atime desc";
	 *	List<DrawCashList> drawMoneyList = drawCashService.findListIns2(hqlExcel, Util.getHqlFiltVal(filtrations));
	 * 
	*/
	public static String getHqlFiltStr2(List<Filtration> filtrations) {
		String hqlStr = "";
		for (int i = 0; i < filtrations.size(); i++) {
			hqlStr += " and " + filtrations.get(i).getFieldName() + " ";
			if("IN".equals(filtrations.get(i).getMatchType().toString())){
				hqlStr += "in (:v"+i+")";//eg: and name in (:v0)
			}else if("ISNULL".equals(filtrations.get(i).getMatchType().toString())){
				hqlStr += "is null";//eg: and name is null
			}else if("ISNOTNULL".equals(filtrations.get(i).getMatchType().toString())){
				hqlStr += "is not null";//eg: and name is not null
			}else{
				//eg: and name = :v0
				hqlStr += getMatchTypeStr(filtrations.get(i).getMatchType().toString()) + " :v"+i+" ";
			}
		}
		return hqlStr;
	}
	/**
	 * 获取hql条件值
	 * 
	 * @author qfc 2016-1-11
	 * @param filtrations
	 *            条件封装集合
	 * @return object...的值
	 */
	public static Object[] getHqlFiltVal(List<Filtration> filtrations) {
		List<Object> objs = new ArrayList<Object>();
		for (int i = 0; i < filtrations.size(); i++) {
			String[] pars = filtrations.get(i).getFieldNames();
			if ("LIKE".equals(filtrations.get(i).getMatchType().toString())) {
				for(int j = 0; j < pars.length; j++){
					objs.add("%" + filtrations.get(i).getFieldValue() + "%");
				}
			} else {
				for(int j = 0; j < pars.length; j++){
					objs.add(filtrations.get(i).getFieldValue());
				}
			}
		}
		Object[] vals = (Object[])objs.toArray(new Object[objs.size()]);
		return vals;
	}
/////////////////////导出数据封装方法--end///////////////////////////
	/**
	 * 判断文件夹是否存在，不存在创建
	 * 
	 * @author qfc 2015-12-14
	 * @param path
	 *            文件夹路径 D:/a/b/c/d
	 * @return 返回false（true）表示文件夹不存在（存在）；如果不存在，文件夹会被创建
	 */
	public static boolean creatFolder(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			file.mkdirs();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断文件是否存在，不存在创建
	 * 
	 * @author qfc 2015-12-14
	 * @param path
	 *            文件夹路径 D:/a/b/c/d/1.txt（该文件所在的文件夹路径必须存在）
	 * @return 返回false（true）表示文件不存在（存在）；如果不存在，文件夹会被创建
	 */
	public static boolean creatFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		} else {
			return true;
		}
	}

	/**
	 * bytes ---> KB
	 * 
	 * @author qfc
	 * @date 2015-7-24
	 */
	public static String getFileKB(long byteFile) {
		if (byteFile == 0)
			return "0KB";
		long kb = 1024;
		return "" + byteFile / kb + "KB";
	}

	/**
	 * bytes ---> MB
	 * 
	 * @author qfc
	 * @date 2015-7-24
	 */
	public static String getFileMB(long byteFile) {
		if (byteFile == 0)
			return "0MB";
		long mb = 1024 * 1024;
		return "" + byteFile / mb + "MB";
	}

	/**
	 * 文件下载到浏览器（下载的文件如果是中文名不会乱码，适用于火狐、谷歌、IE浏览器）
	 * @param fName 文件名
	 * @param path 下载路径
	 * @param response
	 */
	public static boolean downloadFile(String fName, String path,
			HttpServletResponse response) {
		// path是指欲下载的文件的路径。
		File file = new File(path);
		if (file.exists()) {
			try {
				// 取得文件名。
				// String filename = file.getName();
				String filename = fName;
				// 以流的形式下载文件。
				InputStream fis = new BufferedInputStream(new FileInputStream(
						file.getPath()));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				// 清空response
				response.reset();
				// 设置response的Header,文件名乱码问题
				/*response.addHeader(
						"Content-Disposition",
						"attachment;filename="
								+ URLEncoder.encode(new String(filename.getBytes()), "UTF-8"));*/
				response.addHeader(
						"Content-Disposition",
						"attachment;filename="
								+ new String(filename.getBytes("GB2312"),"ISO-8859-1"));
				response.addHeader("Content-Length", "" + file.length());
				OutputStream toClient = new BufferedOutputStream(
						response.getOutputStream());
				response.setContentType("application/octet-stream;charset=utf8");
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 处理时间字符串，获取月和天
	 * 
	 * @param str
	 *            格式：2014-12-03/2014-12-03 20:09:33
	 * @return 12-03
	 */
	public static String getMouthAndDay(String str) {
		str = str.trim();
		return str.substring(5, 10);
	}

	/**
	 * 获取文件绝对路径
	 * 
	 * @param dir
	 *            欲获取路径的文件名称
	 * @return 文件的绝对路径
	 */
	public static String getContextPath(HttpServletRequest request, String dir) {
		return request.getSession().getServletContext().getRealPath(dir);
	}

	/**
	 * 使用纯java获取文件的绝对路径
	 * 
	 * @param dir
	 *            欲获取路径的文件名称
	 * @return 文件的绝对路径
	 */
	public static String getContextPathByJava(String dir) {
		return Thread.currentThread().getContextClassLoader().getResource("/")
				.getPath().replace("WEB-INF/classes/", dir);
	}

	/**
	 * 根据当前时间重命名文件名称
	 * 
	 * @return 唯一的文件名
	 */
	public static String getFileNameByTime(Date date, String oldFName) {
		return oldFName + DateUtils.DateToStr(DateUtils.yyyyMMddHHmmss, date);
	}

	/**
	 * 处理数据为空的数据写入
	 * 
	 * @param mName
	 *            欲写入的数据
	 * @return 正确的数据
	 */
	public static String getStrByNull(Object str) {
		String type = "";

		// 判断传入数据的类型
		if (str instanceof String) {
			// 将数据转换类型
			String newStr = String.valueOf(str);
			if (newStr == null || newStr.isEmpty() || "".equals(newStr)) {
				type = "无";
			} else {
				type = newStr;
			}
		} else if (str instanceof Double) {
			Double newStr = Double.valueOf(str.toString());
			if (newStr == null || newStr == 0.0d || newStr == 0) {
				type = "0.0";
			} else {
				type = newStr.toString();
			}
		} else if (str instanceof Integer) {
			Integer newStr = Integer.valueOf(str.toString());
			if (newStr == null || newStr == 0) {
				type = "0";
			} else {
				type = newStr.toString();
			}
		}

		return type;
	}

	/**
	 * 判断字符串是什么编码
	 * 
	 * @author qfc 2015-7-27
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}

	/**
	 * 字符串编码转换的实现方法
	 * 
	 * @param str
	 *            待转换编码的字符串
	 * @param oldCharset
	 *            原编码
	 * @param newCharset
	 *            目标编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(String str, String oldCharset,
			String newCharset) throws UnsupportedEncodingException {
		if (str != null) {
			// 用旧的字符编码解码字符串。解码可能会出现异常。
			byte[] bs = str.getBytes(oldCharset);
			// 用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}

	/**
	 * 字符串编码转换的实现方法
	 * 
	 * @param str
	 *            待转换编码的字符串
	 * @param newCharset
	 *            目标编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset1(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// 用默认字符编码解码字符串。
			byte[] bs = str.getBytes();
			// 用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}

	// //////////////////////导出数据转换类型--s///////////////////////////////
	/**
	 * 将null转换成“”
	 * 
	 * @author qfc 2016-1-11
	 * @param mName
	 *            欲处理字符串
	 * @return ""
	 */
	public static String lostEmpty(String mName) {
		if (StringUtils.isEmpty(mName)) {
			return "";
		} else {
			return mName;
		}
	}
	/**
	 * 根据状态判断已收金额
	 * @author xx 20190606
	 * @param isConfirm
	 * @param orderHandSelPrice
	 */
	public static double alGath(int isConfirm,double orderHandSelPrice) {
		if(isConfirm==0){//未确认
			return 0;
		}
		return orderHandSelPrice;
	}
	/**
	 * 根据金额返回空或实际值
	 * @author xx 20200214
	 * @param price 金额
	 */
	public static String isGTZero(double price) {
		if(0==price){
			return "";
		}
		return price+"";
	}
	/**
	 * 根据状态判断应收金额
	 * @author xx 20180615
	 * @param isConfirm
	 * @param orderPrice
	 * @param orderHandSelPrice
	 */
	public static double noGath(int isConfirm,double orderPrice,double orderHandSelPrice) {
		if(isConfirm==0){//未确认
			return orderPrice;
		}
		if(orderHandSelPrice<0 || MathUtils.sub(orderPrice, orderHandSelPrice, 2)<0) return 0;//已收为负数或已收大于总价说明已收完
		return MathUtils.sub(orderPrice, orderHandSelPrice, 2);
	}
	// 获取指定格式的时间字符串
	public static String getTimeStr(Date t) {
		return DateUtils.DateToStr("yyyy-MM-dd HH:mm:ss", t);
	}
	// 获取付款方式
	public static String getPayWay(int state) {
		if(state==0){
			return "记账";
		}
		return "现金";
	}
	// //////////////////////导出数据转换类型--e///////////////////////////////
}