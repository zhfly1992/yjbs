package com.fx.service.impl.cus;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.clazz.Item;
import com.fx.commons.utils.clazz.MapRes;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;
import com.fx.commons.utils.tools.UT;
import com.fx.dao.cus.SmsRecordDao;
import com.fx.entity.cus.SmsRecord;
import com.fx.service.cus.SmsRecordService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class SmsRecordServiceImpl extends BaseServiceImpl<SmsRecord, Long> implements SmsRecordService {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass());
	
	/** 用户基类-数据源 */
	@Autowired
	private SmsRecordDao smsRecordDao;
	@Override
	public ZBaseDaoImpl<SmsRecord, Long> getDao() {
		return smsRecordDao;
	}
	
	/** 缓存-服务 */
	@Autowired
	private RedisUtil redis;
	
	@Override
	public Map<String, Object> sendSms(String phones, String content, String linkPhone) {
		return smsRecordDao.sendSms(phones, content, linkPhone);
	}
	
	@Override
	public Map<String, Object> valSms(HttpServletRequest request, HttpServletResponse response, String phone) {
		return smsRecordDao.valSms(request, response, phone);
	}
	
	@Override
	public Map<String, Object> addSmsRecord(HttpServletRequest request, HttpServletResponse response, String phone, 
		String context, int sState) {
		return smsRecordDao.addSmsRecord(request, response, phone, context, sState);
	}
	
	@Override
	public Map<String, Object> sendSmsCode(HttpServletRequest request, HttpServletResponse response, String phone) {
		String logtxt = U.log(log, "发送-短信验证码");
		
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
				long times = 1000*60*2;// 重复发送短信时间差不小于2分钟
				Object obj = redis.get(phone);
				// 存在缓存，且是保存在Item中
				if(obj != null && obj instanceof Item){
					Item item = (Item)obj;
					if(item != null){
						Date st = new Date(Long.parseLong(item.getOther().toString()));
						if(new Date().getTime() - st.getTime() < times){
							fg = U.setPutFalse(map, "短信发送频繁，请稍后发送");
						}
					}
				}
			}
			
			// 验证
			if(fg){
				map = valSms(request, response, phone);
				fg = U.setPutFg(map);
			}
			
			if(fg){
				String capText = U.buildRandom(6)+"";// 获取6位随机正整数验证码
				String text = "请输入验证码"+capText+"完成手机验证（"+QC.SMS_CODE_SAVE_TIME+"分钟内有效）。如非本人操作请忽略。详询：";
				MapRes mr = U.mapRes(smsRecordDao.sendSms(phone, text, ""));
				
				// 添加-短信发送记录
				map = addSmsRecord(request, response, phone, text, mr.getCode());
				
				if(mr.getCode() >= 0){
					// 传入短信内容 验证码 电话并将验证码存入缓存
					UT.setRedis(redis, capText, phone, QC.SMS_CODE_SAVE_TIME);
					
					U.setPut(map, 1, "短信发送成功，请注意查收");
				}else{
					U.setPut(map, 0, "短信发送失败，请重新发送");
				}
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
