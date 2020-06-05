package com.fx.commons.utils.wx.templatemsg;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.tools.U;

/**
 * 发送模板消息
 * @author qfc
 * @2018-5-16
 */
public class SendTemplateMsg {
	/** 日志记录 */
	public static Logger log = LogManager.getLogger(SendTemplateMsg.class.getName());
	public static TemplateInterface wxtemplate = new TemplateInterfaceImpl();
	
	
	/**
	 * 标题：登录成功通知 [T001]
	 * @param ps 			模板内容参数数组
	 * @param accessToken 	微信授权accessToken
	 * @param wxid 			接收消息用户微信id
	 * @return 微信模板消息结果
	 */
	public static TemplateMsgResult loginSuccessOfWxMsg(Item[] ps, String accessToken, String wxid) {
		String logtxt = U.log(log, "发送[登录成功通知]微信模板消息");
		
		TemplateMsgResult tmr = null;
		try {
			TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
			params.put(ps[0].getOther().toString(), WechatTemplateMsg.item(ps[0].getId(), ps[0].getText()));
			params.put(ps[1].getOther().toString(), WechatTemplateMsg.item(ps[1].getId(), ps[1].getText()));
			params.put(ps[2].getOther().toString(), WechatTemplateMsg.item(ps[2].getId(), ps[2].getText()));
			params.put(ps[3].getOther().toString(), WechatTemplateMsg.item(ps[3].getId(), ps[3].getText()));
			
			WechatTemplateMsg wtm = new WechatTemplateMsg();
			wtm.setTouser(wxid);
			wtm.setTemplate_id(ps[4].getOther().toString());// 微信模板消息模板id
			wtm.setUrl(ps[4].getId());
			wtm.setData(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", wtm);
			String data = U.toJsonStr(map);
			U.log(log, "模板消息json数据包："+data);
			tmr = wxtemplate.sendTemplate(accessToken, data);
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return tmr;
	}
	
	/**
	 * 标题：订单确认通知 [T002]
	 * @param ps 			模板内容参数数组
	 * @param accessToken 	微信授权accessToken
	 * @param wxid 			接收消息用户微信id
	 * @return 微信模板消息结果
	 */
	public static TemplateMsgResult jdOfWxmsg(Item[] ps, String accessToken, String wxid){
		String logtxt = U.log(log, "发送[订单确认通知]微信模板消息");
		
		TemplateMsgResult tmr = null;
		try {
			TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
			params.put(ps[0].getOther().toString(), WechatTemplateMsg.item(ps[0].getId(), ps[0].getText()));
			params.put(ps[1].getOther().toString(), WechatTemplateMsg.item(ps[1].getId(), ps[1].getText()));
			params.put(ps[2].getOther().toString(), WechatTemplateMsg.item(ps[2].getId(), ps[2].getText()));
			params.put(ps[3].getOther().toString(), WechatTemplateMsg.item(ps[3].getId(), ps[3].getText()));
			params.put(ps[4].getOther().toString(), WechatTemplateMsg.item(ps[4].getId(), ps[4].getText()));
			params.put(ps[5].getOther().toString(), WechatTemplateMsg.item(ps[5].getId(), ps[5].getText()));
			
			WechatTemplateMsg wtm = new WechatTemplateMsg();
			wtm.setTouser(wxid);
			wtm.setTemplate_id(ps[6].getOther().toString());// 微信模板消息模板id
			wtm.setUrl(ps[6].getId());
			wtm.setData(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", wtm);
			String data = U.toJsonStr(map);
			U.log(log, "模板消息json数据包："+data);
			tmr = wxtemplate.sendTemplate(accessToken, data);
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return tmr;
	}
	
	/**
	 * 标题：新订单通知 [T003]
	 * @param ps 			模板内容参数数组
	 * @param accessToken 	微信授权accessToken
	 * @param wxid 			接收消息用户微信id
	 * @return 微信模板消息结果
	 */
	public static TemplateMsgResult driverOfWxmsg(Item[] ps, String accessToken, String wxid){
		String logtxt = U.log(log, "发送[新订单通知]微信模板消息");
		
		TemplateMsgResult tmr = null;
		try {
			TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
			params.put(ps[0].getOther().toString(), WechatTemplateMsg.item(ps[0].getId(), ps[0].getText()));
			params.put(ps[1].getOther().toString(), WechatTemplateMsg.item(ps[1].getId(), ps[1].getText()));
			params.put(ps[2].getOther().toString(), WechatTemplateMsg.item(ps[2].getId(), ps[2].getText()));
			params.put(ps[3].getOther().toString(), WechatTemplateMsg.item(ps[3].getId(), ps[3].getText()));
			params.put(ps[4].getOther().toString(), WechatTemplateMsg.item(ps[4].getId(), ps[4].getText()));
			params.put(ps[5].getOther().toString(), WechatTemplateMsg.item(ps[5].getId(), ps[5].getText()));
			params.put(ps[6].getOther().toString(), WechatTemplateMsg.item(ps[6].getId(), ps[6].getText()));
			
			WechatTemplateMsg wtm = new WechatTemplateMsg();
			wtm.setTouser(wxid);
			wtm.setTemplate_id(ps[7].getOther().toString());// 微信模板消息模板id
			wtm.setUrl(ps[7].getId());
			wtm.setData(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", wtm);
			String data = U.toJsonStr(map);
			U.log(log, "模板消息json数据包："+data);
			tmr = wxtemplate.sendTemplate(accessToken, data);
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return tmr;
	}
	
	/**
	 * 标题：车辆安排提醒 [T004]
	 * @param ps 			模板内容参数数组
	 * @param accessToken 	微信授权accessToken
	 * @param wxid 			接收消息用户微信id
	 * @return 微信模板消息结果
	 */
	public static TemplateMsgResult useCarOfWxmsg(Item[] ps, String accessToken, String wxid){
		String logtxt = U.log(log, "发送[车辆安排提醒]微信模板消息");
		
		TemplateMsgResult tmr = null;
		try {
			TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
			params.put(ps[0].getOther().toString(), WechatTemplateMsg.item(ps[0].getId(), ps[0].getText()));
			params.put(ps[1].getOther().toString(), WechatTemplateMsg.item(ps[1].getId(), ps[1].getText()));
			params.put(ps[2].getOther().toString(), WechatTemplateMsg.item(ps[2].getId(), ps[2].getText()));
			params.put(ps[3].getOther().toString(), WechatTemplateMsg.item(ps[3].getId(), ps[3].getText()));
			params.put(ps[4].getOther().toString(), WechatTemplateMsg.item(ps[4].getId(), ps[4].getText()));
			params.put(ps[5].getOther().toString(), WechatTemplateMsg.item(ps[5].getId(), ps[5].getText()));
			params.put(ps[6].getOther().toString(), WechatTemplateMsg.item(ps[6].getId(), ps[6].getText()));
			
			WechatTemplateMsg wtm = new WechatTemplateMsg();
			wtm.setTouser(wxid);
			wtm.setTemplate_id(ps[7].getOther().toString());// 微信模板消息模板id
			wtm.setUrl(ps[7].getId());
			wtm.setData(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", wtm);
			String data = U.toJsonStr(map);
			U.log(log, "模板消息json数据包："+data);
			tmr = wxtemplate.sendTemplate(accessToken, data);
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return tmr;
	}
	
	/**
	 * 标题：订单取消通知 [T005]
	 * @param ps 			模板内容参数数组
	 * @param accessToken 	微信授权accessToken
	 * @param wxid 			接收消息用户微信id
	 * @return 微信模板消息结果
	 */
	public static TemplateMsgResult cancelOrderOfWxmsg(Item[] ps, String accessToken, String wxid){
		String logtxt = U.log(log, "发送[订单取消通知]微信模板消息");
		
		TemplateMsgResult tmr = null;
		try {
			TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
			params.put(ps[0].getOther().toString(), WechatTemplateMsg.item(ps[0].getId(), ps[0].getText()));
			params.put(ps[1].getOther().toString(), WechatTemplateMsg.item(ps[1].getId(), ps[1].getText()));
			params.put(ps[2].getOther().toString(), WechatTemplateMsg.item(ps[2].getId(), ps[2].getText()));
			params.put(ps[3].getOther().toString(), WechatTemplateMsg.item(ps[3].getId(), ps[3].getText()));
			
			WechatTemplateMsg wtm = new WechatTemplateMsg();
			wtm.setTouser(wxid);
			wtm.setTemplate_id(ps[4].getOther().toString());// 微信模板消息模板id
			wtm.setUrl(ps[4].getId());
			wtm.setData(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", wtm);
			String data = U.toJsonStr(map);
			U.log(log, "模板消息json数据包："+data);
			tmr = wxtemplate.sendTemplate(accessToken, data);
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return tmr;
	}

	/**
	 * 标题：预约修改提醒 [T006]
	 * @param ps 			模板内容参数数组
	 * @param accessToken 	微信授权accessToken
	 * @param wxid 			接收消息用户微信id
	 * @return 微信模板消息结果
	 */
	public static TemplateMsgResult changeOfWxmsg(Item[] ps, String accessToken, String wxid) {
		String logtxt = U.log(log, "发送[预约修改提醒]微信模板消息");
		
		TemplateMsgResult tmr = null;
		try {
			TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
			params.put(ps[0].getOther().toString(), WechatTemplateMsg.item(ps[0].getId(), ps[0].getText()));
			params.put(ps[1].getOther().toString(), WechatTemplateMsg.item(ps[1].getId(), ps[1].getText()));
			params.put(ps[2].getOther().toString(), WechatTemplateMsg.item(ps[2].getId(), ps[2].getText()));
			params.put(ps[3].getOther().toString(), WechatTemplateMsg.item(ps[3].getId(), ps[3].getText()));
			
			WechatTemplateMsg wtm = new WechatTemplateMsg();
			wtm.setTouser(wxid);
			wtm.setTemplate_id(ps[4].getOther().toString());// 微信模板消息模板id
			wtm.setUrl(ps[4].getId());
			wtm.setData(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", wtm);
			String data = U.toJsonStr(map);
			U.log(log, "模板消息json数据包："+data);
			tmr = wxtemplate.sendTemplate(accessToken, data);
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return tmr;
	}
	
	/**
	 * 标题：订单待审核通知 [T007]
	 * @param ps 			模板内容参数数组
	 * @param accessToken 	微信授权accessToken
	 * @param wxid 			接收消息用户微信id
	 * @return 微信模板消息结果
	 */
	public static TemplateMsgResult orderWaitforCheckOfWxmsg(Item[] ps, String accessToken, String wxid) {
		String logtxt = U.log(log, "发送[订单待审核通知]微信模板消息");
		
		TemplateMsgResult tmr = null;
		try {
			TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
			params.put(ps[0].getOther().toString(), WechatTemplateMsg.item(ps[0].getId(), ps[0].getText()));
			params.put(ps[1].getOther().toString(), WechatTemplateMsg.item(ps[1].getId(), ps[1].getText()));
			params.put(ps[2].getOther().toString(), WechatTemplateMsg.item(ps[2].getId(), ps[2].getText()));
			params.put(ps[3].getOther().toString(), WechatTemplateMsg.item(ps[3].getId(), ps[3].getText()));
			params.put(ps[4].getOther().toString(), WechatTemplateMsg.item(ps[4].getId(), ps[4].getText()));
			params.put(ps[5].getOther().toString(), WechatTemplateMsg.item(ps[5].getId(), ps[5].getText()));
			
			WechatTemplateMsg wtm = new WechatTemplateMsg();
			wtm.setTouser(wxid);
			wtm.setTemplate_id(ps[6].getOther().toString());// 微信模板消息模板id
			wtm.setUrl(ps[6].getId());
			wtm.setData(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", wtm);
			String data = U.toJsonStr(map);
			U.log(log, "模板消息json数据包："+data);
			tmr = wxtemplate.sendTemplate(accessToken, data);
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return tmr;
	}
	
	/**
	 * 标题：车队财务下账通知 [T008]
	 * @param ps 			模板内容参数数组
	 * @param accessToken 	微信授权accessToken
	 * @param wxid 			接收消息用户微信id
	 * @return 微信模板消息结果
	 */
	public static TemplateMsgResult financeReimburseOfWxmsg(Item[] ps, String accessToken, String wxid){
		String logtxt = U.log(log, "发送[车队财务下账通知]微信模板消息");
		
		TemplateMsgResult tmr = null;
		try {
			TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
			params.put(ps[0].getOther().toString(), WechatTemplateMsg.item(ps[0].getId(), ps[0].getText()));
			params.put(ps[1].getOther().toString(), WechatTemplateMsg.item(ps[1].getId(), ps[1].getText()));
			params.put(ps[2].getOther().toString(), WechatTemplateMsg.item(ps[2].getId(), ps[2].getText()));
			params.put(ps[3].getOther().toString(), WechatTemplateMsg.item(ps[3].getId(), ps[3].getText()));
			params.put(ps[4].getOther().toString(), WechatTemplateMsg.item(ps[4].getId(), ps[4].getText()));
			params.put(ps[5].getOther().toString(), WechatTemplateMsg.item(ps[5].getId(), ps[5].getText()));
			
			WechatTemplateMsg wtm = new WechatTemplateMsg();
			wtm.setTouser(wxid);
			wtm.setTemplate_id(ps[6].getOther().toString());// 微信模板消息模板id
			wtm.setUrl(ps[6].getId());
			wtm.setData(params);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", wtm);
			String data = U.toJsonStr(map);
			U.log(log, "模板消息json数据包："+data);
			tmr = wxtemplate.sendTemplate(accessToken, data);
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return tmr;
	}
	
	
}
