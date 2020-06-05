package com.fx.commons.utils.wx.templatemsg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.utils.other.HttpRequest;
import com.fx.commons.utils.tools.ConfigPs;
import com.fx.commons.utils.tools.QC;
import com.fx.commons.utils.tools.U;

/**
 * 微信模板消息接口实现类
 */
public class TemplateInterfaceImpl implements TemplateInterface {
	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/** 常量-服务 */
	@Autowired
	private ConfigPs configPs;
	
	@Override
	public TemplateMsgResult sendTemplate(String accessToken, String params) {
		TemplateMsgResult tmr = new TemplateMsgResult();
		try {
			if(configPs.isIPpass()){
				String url = QC.SEND_TEMPLATE_MESSAGE + "?access_token="+accessToken;
				String json = HttpRequest.sendPost(url, params, null);
				U.log(log, "发送微信模板消息结果："+json);
				ObjectMapper mapper = new ObjectMapper();
				
				JsonNode jn = mapper.readTree(json);
				
				String errcode = U.Cq(jn, "errcode");
				if("0".equals(errcode)){// 发送成功
					tmr.setErrcode(Integer.parseInt(errcode));
					tmr.setErrmsg(U.Cq(jn, "errmsg"));
					tmr.setMsgid(U.Cq(jn, "msgid"));
				}else{
					if("43004".equals(errcode)){
						//System.out.println("43004-需要接收者关注微信公众号："+params);
						U.log(log, "43004-需要接收者关注微信公众号："+params);
					}else{
						tmr.setErrcode(0);
						tmr.setErrmsg("失败："+U.Cq(jn, "errmsg"));
						tmr.setMsgid("0");
						//System.out.println(jo.toString());
						U.log(log, "微信模板失败原因："+jn.toString());
					}
				}
			}else{
				U.log(log, "[访问ip受限]");
				
				tmr.setErrcode(0);
				tmr.setErrmsg("ok");
				tmr.setMsgid("952712138");
			}
		} catch (Exception e) {
			tmr.setErrcode(-1);
			tmr.setErrmsg("异常："+e.getMessage());
			U.log(log, "异常：发送微信模板消息", e);
			e.printStackTrace();
		}
		
		return tmr;
	}

}
