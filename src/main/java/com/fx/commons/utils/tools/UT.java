package com.fx.commons.utils.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.commons.utils.clazz.WxUserInfo;
import com.fx.commons.utils.enums.PointType;
import com.fx.commons.utils.enums.Sex;
import com.fx.commons.utils.other.DateUtils;
import com.fx.commons.utils.other.GetMacAddress;
import com.fx.commons.utils.other.HttpReqMeth;
import com.fx.commons.utils.other.IPUtil;
import com.fx.commons.utils.other.MathUtils;
import com.fx.commons.utils.other.MyStringUtils;
import com.fx.commons.utils.other.WeiXinUtil;
import com.fx.commons.utils.wx.service.WXPrepay;
import com.fx.entity.company.PublicDataSet;
import com.fx.entity.order.BcOrderParam;
import com.fx.entity.order.CompanyOrderTemp;
import com.fx.entity.order.MapPoint;
import com.fx.entity.order.RouteLineInfo;
import com.fx.entity.order.RouteMapPoint;

/**
 * 工具类
 *
 */
public class UT {
	/** 日志记录 */
	public static Logger log = LogManager.getLogger(UT.class.getName());
	
	
	public static void main(String[] args) {
		
	}
	
	/**
	 * 移动端请求Agents
	 */
	public final static String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
			"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
			"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
			"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
			"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
			"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
			"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
			"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
			"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
			"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
			"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
			"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
			"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
			"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
			"Googlebot-Mobile" };
	
	/**
	 * 判断访问浏览器类型-手机/PC
	 * @param request
	 * @return 空-pc；不为空-设备名；
	 */
	public static String getMoblie(HttpServletRequest request) {
		String isMoblie = "";
		
		if (request.getHeader("User-Agent") != null) {
			for (String mobileAgent : mobileAgents) {
				if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = mobileAgent;
					break;
				}
			}
		}
		
		return isMoblie;
	}
	
	/**
	 * 判断访问浏览器类型-手机/PC
	 * @param request
	 * @return true-是手机浏览器，false-PC浏览器
	 */
	public static boolean isMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		
		if (request.getHeader("User-Agent") != null) {
			//System.out.println(request.getHeader("User-Agent"));
			for (String mobileAgent : mobileAgents) {
				if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}
	/**
	 * 获取请求方式-手机/PC
	 * @param request
	 * @return true-是手机浏览器，false-PC浏览器
	 */
	public static String getReqWay(HttpServletRequest request) {
		String isMoblie = "";
		
		String header = request.getHeader("User-Agent");
		if (StringUtils.isNotBlank(header)) {
			for (String mobileAgent : mobileAgents) {
				if (header.toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = mobileAgent;
					break;
				}
			}
		}
		
		if(StringUtils.isEmpty(isMoblie)){
			isMoblie = header;
		}
		
		return isMoblie;
	}
	
	/**
	 * 获取请求User-Agent
	 * @param request
	 * @return User-Agent字符串
	 */
	public static String getReqUA(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}
	
	/**
	 * 获取-请求地址
	 */
	public static String getReqUrl(HttpServletRequest request){
		return request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo()); 
	}
	
	/**
	 * 获取用户登录的电脑IP
	 * @param request request
	 * @return 客户端ip地址（可以穿透代理）
	 */
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
	/**
	 * 获取用户Ip（代理一样可以）
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }
	
	/**
	 * 获取设备的唯一mac地址
	 * @param ip 用户ip
	 */
	public static String getMAC(String ip){
		return GetMacAddress.getMacAddress(ip);
	}
	/**
	 * 根据ip获取城市
	 * @param ip 用户ip
	 */
	public static String getAddrById(String ip){
		return HttpReqMeth.getAddressByIp(ip);
	}
	
	
	/**
	 * 获取-微信支付调起参数
	 * @param openid 		支付用户微信openid
	 * @param orderPayNum 	支付订单编号（每次都不能重复）
	 * @param body 			微信支付主体说明
	 * @param payMoney 		支付金额
	 * @return map{code: 结果状态码, msg: 结果状态码说明, wxdata: 数据}
	 */
	public static Map<String, Object> getWxPayCallParams(HttpServletRequest request, String openid, String orderPayNum, 
		String body, double payMoney) {
		String logtxt = U.log(log, "正在进行【"+body+"】调起微信支付");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(openid)) {
					fg = U.setPutFalse(map, "[支付用户openid]不能为空");
				}else {
					openid = openid.trim();
				}
				
				U.log(log, "[支付用户openid] openid="+openid);
			}
			
			if(fg) {
				if(StringUtils.isEmpty(orderPayNum)) {
					fg = U.setPutFalse(map, "[支付订单编号]不能为空");
				}else {
					orderPayNum = orderPayNum.trim();
				}
				
				U.log(log, "[支付订单编号] orderPayNum="+orderPayNum);
			}
			
			if(fg) {
				if(StringUtils.isEmpty(body)) {
					U.log(log, "[微信支付标题说明]为空");
					
					body = "俭约旅行-微信支付";
				}else {
					body = body.trim();
					if(body.length() > 20) {
						fg = U.setPutFalse(map, "[微信支付标题说明]不能超过20个字");
					}
					
					U.log(log, "[微信支付标题说明] body="+body);
				}
			}
			
			// 测试所需
//			if(payMoney == 398) payMoney = 0.01;
			
			if(fg) {
				WXPrepay prePay = new WXPrepay();
				prePay.setBody(body);							// 商品主体说明
//				prePay.setAppid(KcbConfig.appid);				// 商户appid
//				prePay.setMch_id(KcbConfig.wechatPartner);		
//				prePay.setPartnerKey(KcbConfig.wechatKey);		// 商户key
//				prePay.setNotify_url(KcbConfig.wx_notify_url); 	// 支付成功回调地址
				// 防止商户订单号重复发生支付错误
	    		String newNum = orderPayNum+"_"+UT.getUniqueTime();
	    		U.log(log, "【**为防止重复，重新生成新的商户订单号后缀："+newNum+"**】");
				prePay.setOut_trade_no(newNum);					// 此支付订单编号不能重复使用
				prePay.setSpbill_create_ip(IPUtil.getIpAddr(request)); 	// 终端IP
				//prePay.setTotal_fee("1");						// 测试为1分
				prePay.setTotal_fee((int)(payMoney*100)+"");	// 支付金额
				prePay.setTrade_type("JSAPI");					// 微信支付类型
				prePay.setOpen_id(openid);						// 支付用户微信openid
				String prepayid = prePay.submitXmlGetPrepayId();// 获取预支付订单号
				U.log(log, "获取的预支付订单号是：" + prepayid);
				// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
				String jsParam = ""; //WXPay.createPackageValue(KcbConfig.appid, KcbConfig.wechatKey, prepayid);
//				U.log(log, "生成的微信调起JS参数为：" + jsParam);
				
				// 转换成对象
				ObjectMapper mapper = new ObjectMapper();
				JsonNode wxdata = mapper.readTree(jsParam);
				map.put("wxdata", wxdata);
				
				U.setPut(map, 1, "获取-微信支付调起参数-成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * 获取-微信用户信息
	 * @param token 授权token
	 * @param openId 用户微信id
	 * @return 用户信息对象
	 */
	public static WxUserInfo getWxUserInfo(String token, String openId) {
		String logtxt = U.log(log, "获取-微信用户信息");
		
		WxUserInfo wxu = null;
		
		try {
			U.log(log, "授权token："+token);
			U.log(log, "用户openId："+openId);
			
			// 获取用户信息
			// 获取access_token(scope=snsapi_userinfo)
    		JsonNode node = WeiXinUtil.getUserInfo(token, openId);
			
    		// 用户微信昵称
    		String nickname = U.Cq(node, "nickname");
    		U.log(log, "【"+nickname+"】昵称处理前");
    		nickname = nickname.replaceAll("[^0-9A-Za-z\\u4e00-\\u9fa5]", "");// 过滤其他符号，只剩下中文
    		U.log(log, "【"+nickname+"】昵称处理后");
    		
    		// 用户微信性别
    		String sex = U.Cq(node, "sex");
    		if("1".equals(sex)){// 男
    			sex = Sex.MALE.name();
    		}else if("2".equals(sex)){// 女
    			sex = Sex.FEMALE.name();
    		}else{// 其他
    			sex = Sex.OTHER.name();
    		}
    		
    		wxu = new WxUserInfo();
    		wxu.setOpenid(openId);
    		wxu.setNickname(nickname);
    		wxu.setHeadimgurl(U.Cq(node, "headimgurl"));
    		wxu.setSex(sex);
    		wxu.setCountry(U.Cq(node, "country"));
    		wxu.setProvince(U.Cq(node, "province"));
    		wxu.setCity(U.Cq(node, "city"));
    		wxu.setLanguage(U.Cq(node, "language"));
    		wxu.setPrivilege(U.Cq(node, "privilege"));
    		
    		U.log(log, "用户微信信息："+U.toJsonStr(wxu));
		} catch (Exception e) {
			U.log(log, logtxt, e);
			e.printStackTrace();
		}
		
		return wxu;
	}
	
	/**
	 * 系统生成唯一时间戳
	 */
	public static String getUniqueTime() {
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ""+System.currentTimeMillis();
    }
	
	/**
	 * 唯一的时间戳
	 * @return 时间戳字符串
	 */
	public static String createTime() {
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis()+"";
    }
	
	/**
	 * 系统生成唯一【系统编号】 
	 * @return S+时间戳
	 */
	public static String createSysNo() {
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "S"+System.currentTimeMillis();
    }
	
	/**
	 * 系统生成唯一【系统用户编号】 
	 * @return S+时间戳
	 */
	public static String createSysUname() {
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "SU"+System.currentTimeMillis();
    }
	
	/**
	 * 系统生成【用户名】
	* @return U+时间戳
	 */
	public static String createUname() {
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "U"+System.currentTimeMillis();
    }
	
	/**
	 * 系统生成【单位编号】
	* @return UT+时间戳
	 */
	public static String createUtilNum() {
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "UT"+System.currentTimeMillis();
    }
	
	/**
	 * 系统自动生成【订单支付编号】
	 * @return P+时间戳
	 */
	public static String creatOrderPayNum(){
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		long time = System.currentTimeMillis();
        return "P"+time;
	}
	/**
	 * 系统自动生成【主订单编号】 M...
	 * @param orderType 订单类型 1-单程接送；2-往返包车；
	 * @return
	 */
	public static String creatMainOrderNum(int orderType){
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		long time = System.currentTimeMillis();
        return "M"+orderType+time;
	}
	/**
	 * 系统自动生成【订单编号】 O+订单类型+时间（精确到毫秒）
	 * @param orderType 订单类型 1-单程接送；2-往返包车；
	 * @param date 时间
	 * @return
	 */
	public static String creatOrderNum(int orderType, Date date){
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		String time = DateUtils.DateToStr("yyyyMMddHHmmssSSS", date);
        return "O"+orderType+""+time;
	}
	/**
	 * 系统自动生成【提现申请编号】
	 * @return SQ+时间戳
	 */
	public static String creatWithDrawCashNum(){
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		long time = System.currentTimeMillis();
        return "SQ"+time;
	}
	
	/**
	 * 系统自动生成【优惠券编号】
	 * @return YH+时间戳
	 */
	public static String creatCouponNum(){
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		long time = System.currentTimeMillis();
        return "YH"+time;
	}  
	/**
	 * 系统自动生成【凭证编号】
	 * @param uname 员工账号
	 * @param sortNum已有最大编号
	 */
	public static String creatReimVoucher(String uname,int sortNum){
		return uname+"V"+DateUtils.getyyyyMMdd(new Date())+MyStringUtils.getSizeCount(sortNum+1+"");
	} 
	/**
	 * 系统自动生成【操作编号】
	 */
	public static String creatOperMark(){
		try {
            Thread.sleep(2);// 让程序等待2毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		long time = System.currentTimeMillis();
        return "CZ"+time;
	} 
	/**
	 * 获取-项目中指定路径的文件
	 * @param clazz 	获取文件类
	 * @param filepath 	文件路径 eg: /config/data/poi_data.txt
	 * @return 文件对象，不存在则为空
	 */
	public static File getFile(Class<?> clazz, String filepath) {
		String path = clazz.getResource(filepath).getPath();
		File file = new File(path);
		if(file.exists()) {
			return file;
		}else {
			return null;
		}
	}
	
	/**
	 * 处理-航班升降/火车车次查询结果数据
	 * @param jnode 		查询数据json对象
	 * @param travelWay 	出行方式：1-飞机；2-火车；
	 * @return map{code: 结果状态码, msg: 结果状态码说明, data: 数据}
	 */
	public static Map<String, Object> getNumInfo(JsonNode json, int travelWay) {
		String logtxt = U.log(log, "处理-航班查询结果数据");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		Map<String, Object> res = new HashMap<String, Object>();
		
		try {
			if(fg) {
				if(json == null) {
					fg = U.setPutFalse(map, "获取错误");
				}else {
					U.log(log, "json结果："+json.toString());
				}
			}
			
			// 飞机升降查询结果
			if(fg && travelWay == 1) {
				if(fg) {
					String error_code = U.Cq(json, "code");
					if(!StringUtils.equals("10000", error_code)){
						fg = U.setPutFalse(map, "获取数据失败");
					}
				}
				
				JsonNode result = json.get("result");
				if(fg) {
					if(result == null) {
						fg = U.setPutFalse(map, "获取数据结果为空");
					}else if(result.get("topic") != null && "device error".equals(U.Cq(result, "topic"))) {
						U.log(log, U.Cq(result, "message"));
						
						fg = U.setPutFalse(map, "请检查[航班号是否正确]后再次请求");
					}
				}
				
				List<JsonNode> ja = null;
				if(fg) {
					ja = result.get("output").findValues("result");
					if(ja.size() == 0) {
						fg = U.setPutFalse(map, "数据列表为空");
					}
				}
				
				if(fg) {
					res.put("travelway", travelWay); 					// 出行方式
					
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < ja.size(); i++) {
						Map<String, Object> jo = new HashMap<String, Object>();
						
						JsonNode jb = ja.get(i);
						
						jo.put("no", i+1); 								// 编号
						jo.put("flightNo", jb.get("flightNo")); 		// 航班号
						jo.put("rate", jb.get("rate")); 				// 准点率
						jo.put("depCity", jb.get("depCity")); 			// 出发城市
						jo.put("depPort", jb.get("depPort")); 			// 出发机场
						jo.put("depTerminal", jb.get("depTerminal")); 	// 出发航站楼
						String depScheduled = jb.get("depScheduled").toString().replace("T", " ").replace(":00Z", "");
						jo.put("depScheduled", depScheduled); 			// 出发时间
						
						jo.put("arrCity", jb.get("arrCity")); 			// 到达城市
						jo.put("arrPort", jb.get("arrPort")); 			// 到达机场
						jo.put("arrTerminal", jb.get("arrTerminal")); 	// 到达航站楼
						String arrScheduled = jb.get("arrScheduled").toString().replace("T", " ").replace(":00Z", "");
						jo.put("arrScheduled", arrScheduled); 			// 到达时间
						
						list.add(jo);
					}
					
					res.put("list", list);
					
					U.setPut(map, 1, "获取数据列表成功");
				}
				
			}
			
			if(fg && travelWay == 2) {// 火车车次查询结果
				if(fg) {
					String error_code = U.Cq(json, "code");
					if(!StringUtils.equals("10000", error_code)){
						fg = U.setPutFalse(map, "获取数据失败");
					}
				}
				
				JsonNode result = json.get("result");
				if(fg) {
					if(result == null) {
						fg = U.setPutFalse(map, "获取数据结果为空");
					}else if(result.get("status") != null && "203".equals(result.get("status").toString())) {
						U.log(log, U.Cq(result, "msg"));
						
						fg = U.setPutFalse(map, "请检查[车次号是否正确]后再次请求");
					}
				}
				
				JsonNode jop = null;
				if(fg) {
					String jopStr = U.Cq(result, "result");
					if(StringUtils.isEmpty(jopStr)) {
						fg = U.setPutFalse(map, "获取[车次号]基本数据为空");
					}else {
						jop = result.get("result");
						if(jop == null) {
							fg = U.setPutFalse(map, "获取[车次号]基本数据为空");
						}
					}
				}
				
				List<JsonNode> ja = null;
				if(fg) {
					ja = jop.findValues("list");
					if(ja.size() == 0) {
						fg = U.setPutFalse(map, "站点数据列表为空");
					}
				}
				
				if(fg) {
					res.put("travelway", travelWay); 					// 出行方式
					res.put("trainno", jop.get("trainno")); 			// 车次号
					res.put("date", jop.get("date")); 					// 发车日期
					res.put("typename", jop.get("typename")); 			// 车辆类型
					res.put("startstation", jop.get("startstation")); 	// 起点站
					res.put("endstation", jop.get("endstation")); 		// 终点站
					
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < ja.size(); i++) {
						Map<String, Object> jo = new HashMap<String, Object>();
						
						JsonNode jb = ja.get(i);
						
						jo.put("sequenceno", jb.get("sequenceno")); 		// 停靠站编号
						jo.put("station", jb.get("station")); 				// 站名
						String arrivaltime = jb.get("arrivaltime").toString();
						if(arrivaltime.indexOf(":") == -1) arrivaltime = "";
						jo.put("arrivaltime", arrivaltime); 				// 进站时间
						jo.put("departuretime", jb.get("departuretime")); 	// 发车时间
						jo.put("stoptime", jb.get("stoptime")); 			// 停留时间（分钟）
						jo.put("costtime", jb.get("costtime")); 			// 用时（分钟）
						jo.put("distance", jb.get("distance")); 			// 距离（公里）
						jo.put("day", jb.get("day")); 						// 天数
						
						list.add(jo);
						
						if(i == ja.size() - 1) {// 最后一个，保存总行程耗时
							res.put("costtimetxt", jb.get("costtimetxt"));
						}
					}
					
					res.put("list", list);
					
					U.setPut(map, 1, "获取数据列表成功");
				}
				
			}
			
			map.put("data", res);
		} catch (Exception e) {
			U.log(log, logtxt, e);
		}
		
		return map;
	}
	
	/**
	 * 根据编号获取-出行方式：1-飞机；2-火车；0-不知；
	 * @param num 航班号/车次号
	 * @return 订单类型
	 */
	public static int getTravelWayByNum(String num) {
		String logtxt = "验证传入的字符串是航班/车次号";
		
		boolean fg = true;
		int res = 0;
		
		try {
			if(fg) {
				if(StringUtils.isEmpty(num)) {
					fg = U.logFalse(log, "[航班/车次号]不能为空");
				}else {
					num = num.trim();
					if(num.length() < 2) {
						fg = U.logFalse(log, "[航班/车次号]组成至少2个字符");
					}
					
					U.log(log, "[航班/车次号] num="+num);
				}
			}
			
			if(fg) {
				// 编号必须包含字母
				boolean is = FV.isConLetter(num);
				if(!is) {
					fg = U.logFalse(log, "[航班/车次号]全是数字");
				}else {
					// 获取前2位字符
					String str2 = num.substring(0, 2);
					if(!FV.isConNumber(str2)) {
						U.log(log, "前2位字符全为字母，一定是航班号");
						
						res = 1;
					}else {
						U.log(log, "前2位字符至少有一个数字，则再验证第1位字符");
						
						String str1 = str2.substring(0, 1);
						if(FV.isConNumber(str1)) {
							U.log(log, "前1位字符为数字，一定是航班号");
							
							res = 1;
						}else {
							U.log(log, "前1位字符为字母，一定是车次号");
							
							res = 2;
						}
					}
				}
			}
			
		} catch (Exception e) {
			U.log(log, logtxt, e);
		}
		
		return res;
	}
	
	/**
	 * 过滤字符串中的html标签
	 * @param htmlcontent 传入包含html标签的字符串
	 * @return 过滤后的字符串
	 */
	public static String delHtml(String htmlcontent){
		String txtcontent = htmlcontent;
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	    String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	    String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	    String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
	    if(txtcontent==null||txtcontent.trim().equals("")) return "";
	    Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
	    Matcher m_script = p_script.matcher(txtcontent);
	    if(null!=m_script)
	    txtcontent = m_script.replaceAll(""); // 过滤script标签

	    Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
	    Matcher m_style = p_style.matcher(txtcontent);
	    if(null!=m_style)
	    txtcontent = m_style.replaceAll(""); // 过滤style标签

	    Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
	    Matcher m_html = p_html.matcher(txtcontent);
	    if(null!=m_html)
	    txtcontent = m_html.replaceAll(""); // 过滤html标签

	    Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
	    Matcher m_space = p_space.matcher(txtcontent);
	    if(null!=m_space)
	    txtcontent = m_space.replaceAll(""); // 过滤空格回车标签
		
		return txtcontent.replace("&nbsp;", "");//返回处理后的字符串
	}
	
	/** 
     * 计算地球上任意两点(经纬度)距离   
     * 与百度地图测距最相近，差距小于1米
     * @param long1 起点-经度 
     * @param lat1 起点-纬度 
     * @param long2 终点-经度 
     * @param lat2 终点-纬度 
     * @return 返回距离 单位：米 
     */  
    public static double getDistance(double long1, double lat1, double long2, double lat2) {  
        double a, b, R;  
        R = 6371004; // 地球半径  6371.004 6378137
        lat1 = lat1 * Math.PI / 180.0;  
        lat2 = lat2 * Math.PI / 180.0;  
        a = lat1 - lat2;  
        b = (long1 - long2) * Math.PI / 180.0;  
        double d;  
        double sa2, sb2;  
        sa2 = Math.sin(a / 2.0);  
        sb2 = Math.sin(b / 2.0);  
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)  
                        * Math.cos(lat2) * sb2 * sb2));  
        return d;  
    }
    
    /**
	 * 判断预约时间是否在加班时间之内
	 * @param overTime 	加班起止时间段，如：22:00=23:30 || 23:00=06:00
	 * @param gotime 	订单预约时间：如：2017-10-01 00:00:00
	 * @return true-包括，false-不包括
	 */
	public static boolean isIncludeDate(String overTime, Object gotime){
		boolean isInclude = false;// 默认不包括
		
		try {
			String reserveOrderTime = "";
			if(gotime instanceof Date){
				reserveOrderTime = DateUtils.DateToStr((Date)gotime);
			}else{
				reserveOrderTime = gotime.toString();
			}
			
			long rot = DateUtils.strToDate(reserveOrderTime).getTime();// 预约时间毫秒数：2017-10-01 00:00:00
			String ydd = reserveOrderTime.split(" ")[0];// 预约时间年月日 eg：2017-10-01
			
			long ot_s = DateUtils.strToDate(ydd+" "+overTime.split("=")[0]+":00").getTime();// 加班开始时间毫秒数:2017-10-01 22:00:00 || 2017-10-01 23:00:00
			long ot_e = DateUtils.strToDate(ydd+" "+overTime.split("=")[1]+":00").getTime();// 加班结束时间毫秒数:2017-10-01 23:00:00 || 2017-10-02 06:00:00
			
			if(ot_s > ot_e){// 如果开始时间大于结束时间，说明结束时间需加一天，表示是第二天的时间
				ot_e += 1000*60*60*24;  // 2017-10-02 06:00:00
			}
			
			// 如果预约时间在加班时间之内 
			if(rot >= ot_s && rot <= ot_e){// 判断当天
				isInclude = true;
			}else{// 判断昨天
				rot += 1000*60*60*24;// 预约时间加1天，再判断：因为有可能预约时间为0点 eg:2017-10-01 00:00:00
				if(rot >= ot_s && rot <= ot_e){
					isInclude = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isInclude;
	}
    
    /**
	 * 获取节假日价格
	* @param pds 车队接送机-假日价格设置
	* @param st 用户下单出行时间
	* @param cs 选择的车辆座位数
	* @param op 订单价格
	* @return 处理后的价格
	 */
	public static double getFeastdayPrice(PublicDataSet pds, Date st, int cs, double op){
		boolean fg = true;
		
		double m = op;// 赋值原价
		
		Date fdSt = null;// 节假日开始时间
		Date fdEt = null;// 节假日结束时间
		String[] cps = new String[]{};// 座位金额设置
		if(fg){
			// 存在假日价格设置数据，且设置了起止时间段、座位数对应金额、数据是开启的
			if(pds != null){
				if(pds.getIsOpen() == 1){
					fdSt = pds.getStartTime();
					fdEt = pds.getEndTime();
					cps = pds.getCusSmsSet().split("&");
				}else{
					fg = false;
					
					U.log(log, "车队[未开启]假日价格设置");
				}
			}else{
				U.log(log, "车队[未设置]假日价格，则使用写死的设置");
				
				fdSt = DateUtils.std_st("2019-02-01");
				fdEt = DateUtils.std_et("2019-02-10");
				cps = new String[]{"7=30", "14=50", "17=50", "23=50", "39=100", "49=150"};
			}
		}
		
		if(fg){
			// 如果订单出行时间在节假日之间，则处理价格
			if(st.getTime() >= fdSt.getTime() && st.getTime() <= fdEt.getTime()){
				for(int i = 0; i < cps.length; i++){
					if(cs == Integer.parseInt(cps[i].split("=")[0])){
						m = op + MathUtils.mul(op, MathUtils.div(Double.parseDouble(cps[i].split("=")[1]), 100, 2), 2) ;
						break;
					}
				}
			}
		}
		
		return m;
	}
	
	/**
	 * 设置-旅游包车-行程数据
	 * @param bop 		旅游包车-临时订单参数对象
	 * @param routeMps 	行程地图地点列表
	 * @param rli		行程线路信息
	 */
	public static void setLybcOrderRouteDat(BcOrderParam bop, List<RouteMapPoint> routeMps, RouteLineInfo rli) {
		String [] sarea=bop.getSpoint().split("=")[2].split("-");
		MapPoint sp = new MapPoint();
		sp.setAddress(bop.getSpoint().split("=")[0]);
		sp.setLngLat(bop.getSpoint().split("=")[1]);
		sp.setCounty(sarea[2]);
		sp.setCity(sarea[0]+"-"+sarea[1]);
		sp.setLng(Double.parseDouble(sp.getLngLat().split(",")[0]));
		sp.setLat(Double.parseDouble(sp.getLngLat().split(",")[1]));
		U.log(log, "保存-[地图起点]数据-完成，并未保存至数据库");
		
		RouteMapPoint rmp_sp = new RouteMapPoint();
		rmp_sp.setMapPoint(sp);
		rmp_sp.setSortNo(1);
		rmp_sp.setPtype(PointType.UP_POINT);
		routeMps.add(rmp_sp);
		U.log(log, "保存-[行程起点]数据-完成，并未保存至数据库");
		
		String [] earea=bop.getEpoint().split("=")[2].split("-");
		MapPoint ep = new MapPoint();
		ep.setAddress(bop.getEpoint().split("=")[0]);
		ep.setLngLat(bop.getEpoint().split("=")[1]);
		ep.setCounty(earea[2]);
		ep.setCity(earea[0]+"-"+earea[1]);
		ep.setLng(Double.parseDouble(ep.getLngLat().split(",")[0]));
		ep.setLat(Double.parseDouble(ep.getLngLat().split(",")[1]));
		U.log(log, "保存-[地图终点]数据-完成，并未保存至数据库");
		
		RouteMapPoint rmp_ep = new RouteMapPoint();
		rmp_ep.setMapPoint(ep);
		rmp_ep.setSortNo(2);
		rmp_ep.setPtype(PointType.DOWN_POINT);
		routeMps.add(rmp_ep);
		U.log(log, "保存-[行程终点]数据-完成，并未保存至数据库");
		
		
		rli = new RouteLineInfo();
		int dayNum = Integer.parseInt(DateUtils.getDaysOfTowDiffDate(bop.getStime(), bop.getEtime())+"");
		rli.setDayNum(dayNum);
		rli.setDistance(bop.getDistance());
		rli.setRouteTime((int)Math.ceil(bop.getRouteTime()));
		U.log(log, "保存-[行程线路信息]数据-完成，并未保存至数据库");
		
	}
	
	/**
	 * 单位设置-旅游包车-行程数据
	 * @param bop 		旅游包车-临时订单参数对象
	 * @param routeMps 	行程地图地点列表
	 * @param rli		行程线路信息
	 */
	public static void setLybcOrderRouteCompany(CompanyOrderTemp cot, List<RouteMapPoint> routeMps, RouteLineInfo rli) {
		String [] sarea=cot.getSpoint().split("=")[2].split("-");
		MapPoint sp = new MapPoint();
		sp.setAddress(cot.getSpoint().split("=")[0]);
		sp.setLngLat(cot.getSpoint().split("=")[1]);
		sp.setCounty(sarea[2]);
		sp.setCity(sarea[0]+"-"+sarea[1]);
		sp.setLng(Double.parseDouble(sp.getLngLat().split(",")[0]));
		sp.setLat(Double.parseDouble(sp.getLngLat().split(",")[1]));
		U.log(log, "保存-[地图起点]数据-完成，并未保存至数据库");
		
		RouteMapPoint rmp_sp = new RouteMapPoint();
		rmp_sp.setMapPoint(sp);
		rmp_sp.setSortNo(1);
		rmp_sp.setPtype(PointType.UP_POINT);
		routeMps.add(rmp_sp);
		U.log(log, "保存-[行程起点]数据-完成，并未保存至数据库");
		
		String [] earea=cot.getEpoint().split("=")[2].split("-");
		MapPoint ep = new MapPoint();
		ep.setAddress(cot.getEpoint().split("=")[0]);
		ep.setLngLat(cot.getEpoint().split("=")[1]);
		ep.setCounty(earea[2]);
		ep.setCity(earea[0]+"-"+earea[1]);
		ep.setLng(Double.parseDouble(ep.getLngLat().split(",")[0]));
		ep.setLat(Double.parseDouble(ep.getLngLat().split(",")[1]));
		U.log(log, "保存-[地图终点]数据-完成，并未保存至数据库");
		
		RouteMapPoint rmp_ep = new RouteMapPoint();
		rmp_ep.setMapPoint(ep);
		rmp_ep.setSortNo(2);
		rmp_ep.setPtype(PointType.DOWN_POINT);
		routeMps.add(rmp_ep);
		U.log(log, "保存-[行程终点]数据-完成，并未保存至数据库");
		
		if (StringUtils.isNotEmpty(cot.getWpoints())) {
			String[] wps = cot.getWpoints().split(";");
			String [] warea=null;
			for (int i = 0; i < wps.length; i++) {
				warea=wps[i].split("=")[2].split("-");
				MapPoint wp = new MapPoint();
				wp.setAddress(wps[i].split("=")[0]);
				wp.setLngLat(wps[i].split("=")[1]);
				wp.setCounty(warea[2]);
				wp.setCity(warea[0]+"-"+warea[1]);
				wp.setLng(Double.parseDouble(wp.getLngLat().split(",")[0]));
				wp.setLat(Double.parseDouble(wp.getLngLat().split(",")[1]));
				U.log(log, "保存-[途径地]数据-完成，并未保存至数据库");
				
				RouteMapPoint rmp_wp = new RouteMapPoint();
				rmp_wp.setMapPoint(wp);
				rmp_wp.setSortNo(i+3);
				rmp_wp.setPtype(PointType.WAY_POINT);
				routeMps.add(rmp_wp);
				U.log(log, "保存-[行程途径地]数据-完成，并未保存至数据库");
			}
		}
		int dayNum = Integer.parseInt(DateUtils.getDaysOfTowDiffDate(cot.getStime(), cot.getEtime())+"");
		rli.setDayNum(dayNum);
		rli.setDistance(cot.getDistance());
		rli.setRouteTime((int)Math.ceil(cot.getRouteTime()));
		U.log(log, "保存-[行程线路信息]数据-完成，并未保存至数据库");
		
	}
}
