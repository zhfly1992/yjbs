package com.fx.dao.cus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.tools.ConfigPs;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.entity.cus.BlackList;
import com.fx.entity.cus.SmsRecord;

@Repository
public class SmsRecordDao extends ZBaseDaoImpl<SmsRecord, Long> {

	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 配置数据-服务 */
	@Autowired
	private ConfigPs configPs;
	/** 用户黑名单-服务 */
	@Autowired
	private BlackListDao blackListDao;
	
	
	/**
	 * 验证-短信发送
	 * @param request 	request
	 * @param response 	response
	 * @param phone 	验证的手机号
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> valSms(HttpServletRequest request, HttpServletResponse response, String phone) {
		String logtxt = U.log(log, "验证-短信发送");
		//=========短信验证码防护方式==========//
		// 1 限制单个IP一段时间内请求验证码的次数；
		// 2 限制一段时间内，对同一手机号码发送验证码的次数（同一用户每小时能接收到的验证码最多2条，24小时内最多3条）
		/****************以下方式暂时不控制********************/
		// 3 获取验证码时，加入图形校验码控制；
		// 4 图形校验码中加入干扰线条，采用问答式图形校验码；
		// 5 定期更新图形校验码的生成机制；
		// 6 定期更新验证码页面的URL；
		// 7 短信商平台加入固定IP限制；
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			if(fg){
				if(StringUtils.isEmpty(phone)){
					fg = U.setPutFalse(map, "发送短信手机号不能为空");
				}else{
					phone = phone.trim();
					if(!FV.isPhone(phone)){
						fg = U.setPutFalse(map, FV.match_phone_false_msg);
					}
					
					U.log(log, "发送短信手机号：phone="+phone);
				}
			}
			
			String ip = UT.getIP(request);
			if(fg){
				if(StringUtils.isEmpty(ip)){
					U.log(log, "发送短信Ip：ip为空");
				}else{
					ip = ip.trim();
					
					U.log(log, "发送短信Ip：ip="+ip);
				}
			}
			
			// 如果是黑名单内的用户或者IP，则不能通过验证
			if(fg){
				BlackList bl = blackListDao.findByField("uname", ip);
				if(bl == null){
					bl = blackListDao.findByField("uname", phone);
				}
				
				if(bl != null){
					fg = U.setPutFalse(map, "当前账号存在异常，请联系管理员");
					
					U.log(log, "用户["+bl.getUname()+"]已被拉入黑名单，原因："+bl.getNote());
				}else{
					U.log(log, "当前访问账号/IP正常");
				}
			}
			
			// 重复发送至少要2分钟后
			if(fg){
				long times = 1000*60*2;
				hql = "from SmsRecord where phone = ?0 order by stime desc";
				List<SmsRecord> srs = findhqlList(hql, phone);
				if(srs.size() > 0){
					SmsRecord sr = srs.get(0);
					if(new Date().getTime() - sr.getStime().getTime() <= times){
						fg = U.setPutFalse(map, "短信发送频繁，请稍后再试");
					}
				}
			}
			
			// 单个IP在1小时内只能请求2次
			if(fg){
				long times = 60*60*1;// 1小时（单位：秒）
				Date st = DateUtils.getPlusSecondsD(new Date(), times);
				Date et = new Date();
				
				hql = "select count(id) from SmsRecord where sip = ?0 and (stime >= ?1 and stime <= ?2)";
				Object obj = findObj(hql, ip, st, et);
				if(obj != null){// 已经发送2次请求
					int count = Integer.parseInt(obj.toString());
					if(count >= 2){
						fg = U.setPutFalse(map, "每小时最多发送2次短信");
						
						U.log(log, "当前IP最近1小时内最多发送2次");
					}else{
						U.log(log, "当前IP最近1小时内还剩"+(2 - count)+"次");
					}
				}else{
					U.log(log, "当前IP最近1小时内未发送过短信");
				}
			}
			
			// 同一用户每小时能接收到的验证码最多2条，24小时内最多3条
			if(fg){
				if(fg){
					long times = 60*60*1;// 1小时（单位：秒）
					Date st = DateUtils.getPlusSecondsD(new Date(), times);
					Date et = new Date();
					
					hql = "select count(id) from SmsRecord where phone = ?0 and (stime >= ?1 and stime <= ?2)";
					Object obj = findObj(hql, phone, st, et);
					if(obj != null){// 已经发送2次请求
						int count = Integer.parseInt(obj.toString());
						if(count >= 2){
							fg = U.setPutFalse(map, "每小时最多发送2次短信");
							
							U.log(log, "当前手机号1小时内最多发送2次");
						}else{
							U.log(log, "当前手机号1小时内还剩"+(2 - count)+"次");
						}
					}else{
						U.log(log, "当前手机号1小时内未发送过短信");
					}
				}
				
				if(fg){
					long times = 60*60*24;// 24小时（单位：秒）
					Date st = DateUtils.getPlusSecondsD(new Date(), times);
					Date et = new Date();
					
					hql = "select count(id) from SmsRecord where phone = ?0 and (stime >= ?1 and stime <= ?2)";
					Object obj = findObj(hql, phone, st, et);
					if(obj != null){// 已经发送3次请求
						int count = Integer.parseInt(obj.toString());
						if(count >= 3){
							fg = U.setPutFalse(map, "24小时内最多发送3次短信");
							
							U.log(log, "当前手机号24小时内最多发送3次");
						}else{
							U.log(log, "当前手机号24小时内还剩"+(3 - count)+"次");
						}
					}else{
						U.log(log, "当前手机号24小时内未发送过短信");
					}
				}
				
				if(fg){
					U.setPut(map, 1, "短信发送验证成功");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 发送短信
	 * @param content  	短信内容（超过40个字，可能按照两条短信费用计算）
	 * @param phones 	1个或多个手机号，多个用英文逗号分隔
	 * @param linkPhone 联系电话（一般指咨询详情等电话）
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> sendSms(String phones, String content, String linkPhone) {
		String logtxt = U.log(log, "短信发送");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(phones)) {
					fg = U.setPutFalse(map, "[短信发送手机号]不能为空");
				}else {
					phones = phones.trim();
					if(phones.indexOf(",") == -1) {// 单个手机号发送
						if(!FV.isPhone(phones)) {
							fg = U.setPutFalse(map, "[短信发送手机号]格式错误");
						}
					}else {// 多个手机号发送
						String parr[] = phones.split(",");
						for (int i = 0; i < parr.length; i++) {
							if(!FV.isPhone(phones)) {
								fg = U.setPutFalse(map, "[第"+(i+1)+"个短信发送手机号]格式错误");
								break;
							}
						}
					}
					
					U.log(log, "[短信发送手机号] phones="+phones);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(content)) {
					fg = U.setPutFalse(map, "[短信发送内容]不能为空");
				}else {
					content = content.trim();
					
					U.log(log, "[短信发送内容] content="+content);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(linkPhone)) {
					U.log(log, "[联系电话]为空，则使用默认电话");
					linkPhone = "02882220858";
				}else {
					linkPhone = linkPhone.trim();
					if(!FV.isPhone(linkPhone)) {
						fg = U.setPutFalse(map, "[联系电话]格式错误");
					}
					
					U.log(log, "[联系电话] linkPhone="+linkPhone);
				}
			}
			
			if(fg) {
				// 处理：发送内容
				content = URLEncoder.encode(content.replaceAll("<br/>", " ")+linkPhone+"【遇见巴士】", "GBK");
			}
			
			if(fg) {
				if(!configPs.isIPpass()) {
					content = DateUtils.DateToStr(new Date())+"："+URLDecoder.decode(content, "GBK");
					fg = U.setPutFalse(map, content);
					
					System.out.println(content);
				}
			}
			
			if(fg) {
				int result = -1;
				URL url = new URL("https://mb345.com/WS/BatchSend.aspx?CorpID=LKSDK0004155&Pwd=ygt2015NEWEBAM@9-10&Mobile="+
					phones+"&Content="+content+"&Cell=&SendTime=");
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String line = in.readLine();
				if(StringUtils.isNotEmpty(line)){
					line = line.split("<")[0];
					result = new Integer(line).intValue();
				}else{
					result = 1;
				}
				
				if(result >= 0) {
					U.setPut(map, 1, "发送短信成功");
				}else {
					U.setPut(map, 0, "发送短信失败");
				}
			}
		}catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 添加-短信发送记录
	 * @param request 	request
	 * @param response 	response
	 * @param phone 	手机号
	 * @param context 	短信内容
	 * @param sState 	结果状态
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> addSmsRecord(HttpServletRequest request, HttpServletResponse response, String phone, 
		String context, int sState) {
		String logtxt = U.log(log, "添加-短信发送记录");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg){
				if(StringUtils.isEmpty(phone)){
					fg = U.setPutFalse(map, "发送短信手机号不能为空");
				}else{
					phone = phone.trim();
					if(!FV.isPhone(phone)){
						fg = U.setPutFalse(map, FV.match_phone_false_msg);
					}
					
					U.log(log, "发送短信手机号：phone="+phone);
				}
			}
			

			if(fg){
				if(StringUtils.isEmpty(context)){
					fg = U.setPutFalse(map, "短信内容不能为空");
				}else{
					context = context.trim();
					
					U.log(log, "短信内容：context="+context);
				}
			}
			
			if(fg){
				SmsRecord sr = new SmsRecord();
				sr.setSip(UT.getIP(request));
				sr.setSway(UT.getReqUA(request));
				sr.setRequrl(UT.getReqUrl(request));
				sr.setPhone(phone);
				sr.setContext(context);
				sr.setsState(sState);
				sr.setStime(new Date());
				save(sr);
				
				U.setPut(map, 1, "添加短信记录成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
