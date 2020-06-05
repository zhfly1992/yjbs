//package com.fx.service.impl.wxdat;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
//import com.fx.commons.hiberantedao.service.BaseServiceImpl;
//import com.fx.commons.utils.clazz.Item;
//import com.fx.commons.utils.other.DateUtils;
//import com.fx.commons.utils.other.IPUtil;
//import com.fx.commons.utils.tools.ConfigPs;
//import com.fx.commons.utils.tools.QC;
//import com.fx.commons.utils.tools.U;
//import com.fx.commons.utils.tools.UT;
//import com.fx.dao.cus.CompanyUserDao;
//import com.fx.dao.cus.CustomerDao;
//import com.fx.dao.wxdat.WxPublicDataDao;
//import com.fx.dao.wxdat.WxTplmsgDataDao;
//import com.fx.entity.cus.CompanyUser;
//import com.fx.entity.cus.Customer;
//import com.fx.entity.cus.WxBaseUser;
//import com.fx.entity.wxdat.WxTplmsgData;
//import com.fx.service.wxdat.WxTplmsgDataService;
//
//@Service
//public class WxTplmsgDataServiceImpl extends BaseServiceImpl<WxTplmsgData, Long> implements WxTplmsgDataService {
//
//	/** 日志记录 */
//	private Logger log = LogManager.getLogger(this.getClass().getName());
//
//	@Autowired
//	private WxTplmsgDataDao wxTplmsgDataDao;
//	@Override
//	public ZBaseDaoImpl<WxTplmsgData, Long> getDao() {
//		return wxTplmsgDataDao;
//	}
//	
//	/** 用户信息-服务 */
//	@Autowired
//	private CustomerDao customerDao;
//	/** 微信公共数据-服务 */
//	@Autowired
//	private WxPublicDataDao wxPublicDataDao;
//	/** 单位用户-服务 */
//	@Autowired
//	private CompanyUserDao companyUserDao;
//	/** 常量-服务 */
//	@Autowired
//	private ConfigPs configPs;
//	
//
//	@Override
//	public WxTplmsgData getWxTplmsgData(String teamNo, String tplNo) {
//		String logtxt = "获取-指定模板编号的微信模板消息对象数据";
//
//		WxTplmsgData wtd = null;
//		String hql = "";
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (StringUtils.isEmpty(teamNo)) {
//					U.log(log, "为空则默认为客车帮车队编号");
//
//					// fg = U.logFalse(log, "[车队编号]不能为空");
//					teamNo = QC.DEF_COMPANY_NUM;// 为空则默认为客车帮车队编号
//				} else {
//					teamNo = teamNo.trim();
//
//					U.log(log, "[车队编号] teamNo=" + teamNo);
//				}
//			}
//
//			if (fg) {
//				if (StringUtils.isEmpty(tplNo)) {
//					fg = U.logFalse(log, "[微信模板消息模板编号]不能为空");
//				} else {
//					tplNo = tplNo.trim();
//
//					U.log(log, "[微信模板消息模板编号] tplNo=" + tplNo);
//				}
//			}
//
//			if (fg) {
//				hql = "from WxTplmsgData where teamNo = ? and tplNo = ?";
//				wtd = wxTplmsgDataDao.findObj(hql, teamNo, tplNo);
//				if (wtd == null) {
////					fg = U.logFalse(log, "指定模板编号模板对象不存在");
//
//					U.log(log, "指定模板编号模板对象不存在，则使用“客车帮”公众号模板消息数据");
//
//					wtd = wxTplmsgDataDao.findObj(hql, QC.DEF_COMPANY_NUM, tplNo);
//				} else {
//					U.log(log, "获取模板对象成功");
//				}
//			}
//		} catch (Exception e) {
//			U.log(log, logtxt, e);
//			e.printStackTrace();
//		}
//
//		return wtd;
//	}
//
////////////////////微信模板消息--begin////////////////
//
//	@Override
//	public Map<String, Object> loginSuccessOfWxMsg(WxTplmsgData wtd, WxBaseUser wxuser) {
//		String logtxt = U.log(log, "登录成功通知-登录用户登录成功后收到通知");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (wtd == null) {
//					U.log(log, "不存在[微信模板消息数据对象]则重新获取");
//
//					wtd = wxTplmsgDataDao.getWxTplmsgData(wxuser.getCompanyNum(), "T001");
//				} else {
//					U.log(log, "已存在[微信模板消息数据对象]");
//				}
//			}
//
//			String teamName = "";
//			if (fg) {
//				if (wxuser == null) {
//					fg = U.setPutFalse(map, "传入微信登录信息不存在");
//				} else {
//					CompanyUser cu = companyUserDao.findByField("unitNum", wxuser.getCompanyNum());
//					if (cu == null) {
//						fg = U.setPutFalse(map, "登录用户所属车队已不存在");
//					} else {
//						teamName = cu.getCompanyName();
//					}
//
//					U.log(log, "登录用户uid：orderNum=" + wxuser.getUname());
//				}
//			}
//
//			String lname = null;
//			if (fg) {
//				Customer cus = customerDao.findByField("baseUserId.uname", wxuser.getUname());
//				if (cus == null) {
//					fg = U.setPutFalse(map, "登录用户不存在");
//				} else {
//					lname = cus.getBaseUserId().getUname();
//				}
//
//				U.log(log, "当前登录用户[" + lname + "]");
//			}
//
//			String wxid = "";
//			if (fg) {
//				wxid = wxuser.getWxid();// 此处获取登录用户绑定的主微信id
//				if (StringUtils.isEmpty(wxid)) {
//					fg = U.setPutFalse(map, "用户[" + lname + "]未关注微信公众号");
//				}
//
//				U.log(log, "消息接收用户：lname=" + lname);
//			}
//
//			String accessToken = "";
//			if (fg) {
//				accessToken = wcgSer.findTokenOrTicket(wtd.getCompanyNum(), 1);
//				if (StringUtils.isEmpty(accessToken)) {
//					fg = U.setPutFalse(map, "微信授权失败");
//				}
//
//				U.log(log, "微信授权：accessToken=" + accessToken);
//			}
//
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				String title = "您好，你的账号[" + lname + "]成功登录[" + teamName + "-" + wxuser.getLgRole().getValue() + "]";
//				if (configPs.isIPpass())
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 登录地址
//				String address = UT.getAddrById(wxuser.getLgIp());
//				if (StringUtils.isEmpty(address))
//					address = "不详";
//				ps.add(new Item(address, "#000000", fs[1]));
//
//				// 登录时间
//				String stime = DateUtils.DateToStr("yyyy-MM-dd HH:mm:ss", wxuser.getLgTime());
//				ps.add(new Item(stime, "#000000", fs[2]));
//
//				// 备注
//				String remark = ""; // "如不是本人登录，点击进入登录密码修改页面";
//				ps.add(new Item(remark, "#000000", fs[3]));
//
//				// 点击链接地址[修改登录密码地址] 只有绑定的微信账号进入的修改密码才会被成功修改，其他微信用户进入不会被成功修改；
//				String linkUrl = ""; // QC.MOBILE_FULL_ROUTE+"upd-login-pass";
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.loginSuccessOfWxMsg(ps.toArray(new Item[ps.size()]),
//						accessToken, wxid);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-登录成功通知-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-登录成功通知-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, logtxt);
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> jdOfWxmsg(WxTplmsgData wtd, CarOrderList order, String mbName) {
//		String logtxt = U.log(log, "通知车队计调（车队后台/乘客添加订单成功后）");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (wtd == null) {
//					U.log(log, "不存在[微信模板消息数据对象]则重新获取");
//
//					wtd = wxTplmsgDataSer.getWxTplmsgData(order.getTeamNo(), "T002");
//				} else {
//					U.log(log, "已存在[微信模板消息数据对象]");
//				}
//			}
//
//			if (fg) {
//				if (order == null) {
//					fg = U.setPutFalse(map, "传入订单对象不存在");
//				} else {
//					U.log(log, "订单编号：orderNum=" + order.getOrderNum());
//				}
//			}
//
//			String wxid = "";
//			if (fg) {
//				if (StringUtils.isEmpty(mbName)) {
//					fg = U.setPutFalse(map, "消息接收用户不能为空");
//				} else {
//					mbName = mbName.trim();
//					wxid = cusSer.takeOpenId(order.getTeamNo(), mbName);
//					if (StringUtils.isEmpty(wxid)) {
//						fg = U.setPutFalse(map, "用户[" + mbName + "]未关注微信公众号");
//					}
//
//					U.log(log, "消息接收用户：mbName=" + mbName);
//				}
//			}
//
//			String accessToken = "";
//			if (fg) {
//				accessToken = wcgSer.findTokenOrTicket(wtd.getTeamNo(), 1);
//				if (StringUtils.isEmpty(accessToken)) {
//					fg = U.setPutFalse(map, "微信授权失败");
//				}
//
//				U.log(log, "微信授权：accessToken=" + accessToken);
//			}
//
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				String title = "您好，你有新的订单需要指派，请及时查看并派车！";
//				if (U.getJdbcIp().contains(QC.TEST_SQL_IP))
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 出发时间
//				String stime = DateUtils.DateToStr("yyyy-MM-dd HH:mm", order.getUseDayStart());
//				ps.add(new Item(stime, "#000000", fs[1]));
//
//				// 行程线路
//				String routeline = "从", fightNum = "";
//				if (StringUtils.isNotBlank(order.getFlightNum())) {
//					if (order.getType() == 1) {// 飞机
//						fightNum = "（航班号：" + order.getFlightNum() + "）";
//					} else {
//						fightNum = "（车次号：" + order.getFlightNum() + "）";
//					}
//				}
//				routeline += MyStringUtils.getPointAddr(order.getDepartPlace());
//				if (order.getIsShuttle() == 0)
//					routeline += fightNum; // 接
//				routeline += order.getIsShuttle() == 0 ? " 接到 " : " 送到 ";
//				routeline += MyStringUtils.getPointAddr(order.getDestination());
//				if (order.getIsShuttle() == 1)
//					routeline += fightNum; // 送
//				ps.add(new Item(routeline, "#000000", fs[2]));
//
//				// 车辆类型
//				String cartype = "";
////	if(StringUtils.isNotBlank(order.getOtherData())){// 接送机
////		DiscountDetail dd = (DiscountDetail)U.toJsonBean(cp.getDisJson(), DiscountDetail.class);
////		
////		Map<String, String> hpMap = UTools.arrStr2map(order.getOtherData());
////		
////		
////		cartype += WebUtil.getCarTypeByNo(Integer.parseInt(hpMap.get("carType")));
////		cartype += hpMap.get("typeCount")+"座（"+order.getCustomers()+" 人）";
////	}else{// 旅游包车
//				cartype += order.getNeedCarNum() + "辆车-" + order.getCarSeats() + "座/辆（" + order.getCustomers() + " 人）";
////	}
//				ps.add(new Item(cartype, "#000000", fs[3]));
//
//				// 订单价格
//				String price = "￥";
//				if (order.getDiscountDetail() != null) {
//					price = order.getDiscountDetail().getPayMoney() + "";
//				} else {
//					price = order.getOrderPrice() + "";
//				}
//				ps.add(new Item(price, "#000000", fs[4]));
//
//				// 备注
//				String remark = "联系方式：";
//				if (StringUtils.isBlank(order.getRealName())) {
//					remark = "匿名";
//				} else {
//					remark = order.getRealName() + "/" + order.getCustomerPhone();
//				}
//				ps.add(new Item(remark, "#000000", fs[5]));
//
//				// 点击链接地址[车队计调-订单-未确认-订单列表]
//				String linkUrl = U.PRO_URL(QC.MOBILE_ROUTE_ID) + "carteam/main/3/0?et=TEAM_JD";
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.jdOfWxmsg(ps.toArray(new Item[ps.size()]), accessToken, wxid);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-订单确认通知-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-订单确认通知-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, logtxt);
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> changeOfWxmsg(WxTplmsgData wtd, JuniorCarOrder order, String mbName) {
//		String logtxt = U.log(log, "变更-消息通知");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (wtd == null) {
//					U.log(log, "不存在[微信模板消息数据对象]则重新获取");
//
//					wtd = wxTplmsgDataSer.getWxTplmsgData(order.getTeamNo(), "T006");
//				} else {
//					U.log(log, "已存在[微信模板消息数据对象]");
//				}
//			}
//
//			if (fg) {
//				if (order == null) {
//					fg = U.setPutFalse(map, "传入订单对象不存在");
//				} else {
//					U.log(log, "订单编号：orderNum=" + order.getOrderNum());
//				}
//			}
//
//			String wxid = "";
//			if (fg) {
//				if (StringUtils.isEmpty(mbName)) {
//					fg = U.setPutFalse(map, "消息接收用户不能为空");
//				} else {
//					mbName = mbName.trim();
//					wxid = cusSer.takeOpenId(order.getTeamNo(), mbName);
//					if (StringUtils.isEmpty(wxid)) {
//						fg = U.setPutFalse(map, "用户[" + mbName + "]未关注微信公众号");
//					}
//
//					U.log(log, "消息接收用户：mbName=" + mbName);
//				}
//			}
//
//			String accessToken = "";
//			if (fg) {
//				accessToken = wcgSer.findTokenOrTicket(wtd.getTeamNo(), 1);
//				if (StringUtils.isEmpty(accessToken)) {
//					fg = U.setPutFalse(map, "微信授权失败");
//				}
//
//				U.log(log, "微信授权：accessToken=" + accessToken);
//			}
//
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				String title = "您好！您有订单已经被计调取消！";
//
//				// 行程线路
//				title += "\r\n行程线路：";
//				String routeline = "从 ";
//				routeline += MyStringUtils.getPointAddr(order.getDepartPlace());
//				routeline += " 到 ";
//				routeline += MyStringUtils.getPointAddr(order.getDestination());
//				title += routeline;
//
//				// 订单编号
//				title += "\r\n订单编号：";
//				String orderNo = order.getOrderNum();
//				title += orderNo;
//				if (U.getJdbcIp().contains(QC.TEST_SQL_IP))
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 变更信息
//				String changeInfo = "订单更换驾驶员";
//				ps.add(new Item(changeInfo, "#000000", fs[1]));
//
//				// 变更时间
//				String stime = DateUtils.DateToStr("yyyy-MM-dd HH:mm", new Date());
//				ps.add(new Item(stime, "#000000", fs[2]));
//
//				// 备注
//				String remark = "点击查看变更后的订单信息";
//				ps.add(new Item(remark, "#000000", fs[3]));
//
//				// 点击链接地址[驾驶员-我接的-订单列表]
//				String linkUrl = "";
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.changeOfWxmsg(ps.toArray(new Item[ps.size()]), accessToken,
//						wxid);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-预约修改提醒-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-预约修改提醒-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, logtxt);
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> driverOfWxmsg(WxTplmsgData wtd, JuniorCarOrder order, String mbName) {
//		U.log(log, "通知驾驶员（计调派单成功后）");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		String hql = "";
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (wtd == null) {
//					U.log(log, "不存在[微信模板消息数据对象]则重新获取");
//
//					wtd = wxTplmsgDataSer.getWxTplmsgData(order.getTeamNo(), "T003");
//				} else {
//					U.log(log, "已存在[微信模板消息数据对象]");
//				}
//			}
//
//			if (fg) {
//				if (order == null) {
//					fg = U.setPutFalse(map, "传入订单对象不存在");
//				} else {
//					U.log(log, "订单编号：orderNum=" + order.getOrderNum());
//				}
//			}
//
//			String wxid = "";
//			String wxidSelina = "";
//			if (fg) {
//				if (StringUtils.isEmpty(mbName)) {
//					fg = U.setPutFalse(map, "消息接收用户不能为空");
//				} else {
//					mbName = mbName.trim();
//					wxid = cusSer.takeOpenId(order.getTeamNo(), mbName);
//					if (StringUtils.isEmpty(wxid)) {
//						fg = U.setPutFalse(map, "用户[" + mbName + "]未关注微信公众号");
//					}
//
//					U.log(log, "消息接收用户：mbName=" + mbName);
//				}
//				if (fg) {
//					if (StringUtils.isNotEmpty(order.getSelinaDriver())) {
//						wxidSelina = cusSer.takeOpenId(order.getTeamNo(), order.getSelinaDriver().split(",")[0]);
//						if (StringUtils.isEmpty(wxidSelina)) {
//							U.log(log, "代班用户[" + order.getSelinaDriver().split(",")[0] + "]未关注微信公众号");
//						}
//					}
//				}
//			}
//
//			String accessToken = "";
//			if (fg) {
//				accessToken = wcgSer.findTokenOrTicket(wtd.getTeamNo(), 1);
//				if (StringUtils.isEmpty(accessToken)) {
//					fg = U.setPutFalse(map, "微信授权失败");
//				}
//
//				U.log(log, "微信授权：accessToken=" + accessToken);
//			}
//
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				String title = "您好，您的新订单信息，请及时点击查看并确认订单和联系客户。";
//				/********* 20190924 xx **********/
//				if (StringUtils.isNotBlank(order.getSelinaDriver())) {// 有代班驾驶员查询代班时间
//					CarList car = carSer.findByField("plateNum", order.getPlateNum());
//					if (car != null) {
//						String selinaDay = DateUtils
//								.getDay(DateUtils.DateToStr(DateUtils.yyyy_MM_dd_HH_mm, car.getSelinaDayStart()));
//						title += "您" + selinaDay + "号的顶班驾驶员为：" + order.getSelinaDriver() + "。";
//						if (StringUtils.isNotBlank(wxidSelina)) {
//							String titleSelina = "您好，您的新订单信息。您" + selinaDay + "号的需要帮：" + order.getMbName() + "顶班。";
//							driverOfSelina(wtd, order, order.getSelinaDriver().split(",")[0], accessToken, wxidSelina,
//									titleSelina);
//						}
//					}
//				}
//				/********* 20190924 xx **********/
//				if (U.getJdbcIp().contains(QC.TEST_SQL_IP))
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 订单号
//				String orderNo = order.getOrderNum();
//				ps.add(new Item(orderNo, "#000000", fs[1]));
//
//				// 线路
//				String routeline = "从 ";
//				routeline += MyStringUtils.getPointAddr(order.getDepartPlace());
//				routeline += " 到 ";
//				routeline += MyStringUtils.getPointAddr(order.getDestination());
//				ps.add(new Item(routeline, "#000000", fs[2]));
//
//				// 出发时间
//				String stime = DateUtils.DateToStr("yyyy-MM-dd HH:mm", order.getUseDayStart());
//				ps.add(new Item(stime, "#000000", fs[3]));
//
//				// 同行人数
//				String cusCount = order.getCarSeats() + " 座（包车）";
//				ps.add(new Item(cusCount, "#000000", fs[4]));
//
//				// 联系方式[地点联系人]
//				String linkWay = "";
//				hql = "from PointLinkman where dispatchNum like ?";
//				List<PointLinkman> plms = plmDao.findhqlList(hql, "%" + orderNo + "%");
//				if (plms.size() > 0) {
//					for (int i = 0; i < plms.size(); i++) {
//						linkWay += plms.get(i).getLinkPhone() + "（" + plms.get(0).getLinkName() + "）";
//						if (i != plms.size() - 1) {
//							linkWay += "/";
//						}
//					}
//				} else {
//					linkWay = "无";
//				}
//				ps.add(new Item(linkWay, "#000000", fs[5]));
//
//				// 说明
//				String explain = "温馨提示：请在保证行车安全的情况下，及时点击确认订单和联系客户。";
//				ps.add(new Item(explain, "#000000", fs[6]));
//
//				// 点击链接地址[驾驶员-我接的-旅游包车-订单列表]
//				String linkUrl = U.PRO_URL(QC.MOBILE_SYSTEM_ID) + "wxAuthority?tno=" + order.getTeamNo()
//						+ "&wr=TEAM_DRIVER&state=125";
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.driverOfWxmsg(ps.toArray(new Item[ps.size()]), accessToken,
//						wxid);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-新订单通知-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-新订单通知-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, "通知驾驶员（计调派单成功后）");
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	public Map<String, Object> driverOfSelina(WxTplmsgData wtd, JuniorCarOrder order, String mbName, String accessToken,
//			String wxidSelina, String title) {
//		U.log(log, "通知代班驾驶员（计调派单成功后）");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		String hql = "";
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				if (U.getJdbcIp().contains(QC.TEST_SQL_IP))
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 订单号
//				String orderNo = order.getOrderNum();
//				ps.add(new Item(orderNo, "#000000", fs[1]));
//
//				// 线路
//				String routeline = "从 ";
//				routeline += MyStringUtils.getPointAddr(order.getDepartPlace());
//				routeline += " 到 ";
//				routeline += MyStringUtils.getPointAddr(order.getDestination());
//				ps.add(new Item(routeline, "#000000", fs[2]));
//
//				// 出发时间
//				String stime = DateUtils.DateToStr("yyyy-MM-dd HH:mm", order.getUseDayStart());
//				ps.add(new Item(stime, "#000000", fs[3]));
//
//				// 同行人数
//				String cusCount = order.getCarSeats() + " 座（包车）";
//				ps.add(new Item(cusCount, "#000000", fs[4]));
//
//				// 联系方式[地点联系人]
//				String linkWay = "";
//				hql = "from PointLinkman where dispatchNum like ?";
//				List<PointLinkman> plms = plmDao.findhqlList(hql, "%" + orderNo + "%");
//				if (plms.size() > 0) {
//					for (int i = 0; i < plms.size(); i++) {
//						linkWay += plms.get(i).getLinkPhone() + "（" + plms.get(0).getLinkName() + "）";
//						if (i != plms.size() - 1) {
//							linkWay += "/";
//						}
//					}
//				} else {
//					linkWay = "无";
//				}
//				ps.add(new Item(linkWay, "#000000", fs[5]));
//
//				// 说明
//				String explain = "温馨提示：请在保证行车安全的情况下，及时点击确认订单和联系客户。";
//				ps.add(new Item(explain, "#000000", fs[6]));
//
//				// 点击链接地址[驾驶员-我接的-旅游包车-订单列表]
//				String linkUrl = U.PRO_URL(QC.MOBILE_SYSTEM_ID) + "wxAuthority?tno=" + order.getTeamNo()
//						+ "&wr=TEAM_DRIVER&state=125";
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.driverOfWxmsg(ps.toArray(new Item[ps.size()]), accessToken,
//						wxidSelina);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-新订单通知-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-新订单通知-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, "通知代班驾驶员（计调派单成功后）");
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> useCarOfWxmsg(WxTplmsgData wtd, JuniorCarOrder order, String mbName) {
//		U.log(log, "通知业务员和旅行社（师傅确认订单成功后）");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (wtd == null) {
//					U.log(log, "不存在[微信模板消息数据对象]则重新获取");
//
//					wtd = wxTplmsgDataSer.getWxTplmsgData(order.getTeamNo(), "T004");
//				} else {
//					U.log(log, "已存在[微信模板消息数据对象]");
//				}
//			}
//
//			if (fg) {
//				if (order == null) {
//					fg = U.setPutFalse(map, "传入订单对象不存在");
//				} else {
//					U.log(log, "订单编号：orderNum=" + order.getOrderNum());
//				}
//			}
//
//			String wxid = "";
//			if (fg) {
//				if (StringUtils.isEmpty(mbName)) {
//					fg = U.setPutFalse(map, "消息接收用户不能为空");
//				} else {
//					mbName = mbName.trim();
//					wxid = cusSer.takeOpenId(order.getTeamNo(), mbName);
//					if (StringUtils.isEmpty(wxid)) {
//						fg = U.setPutFalse(map, "用户[" + mbName + "]未关注微信公众号");
//					}
//
//					U.log(log, "消息接收用户：mbName=" + mbName);
//				}
//			}
//
//			String accessToken = "";
//			if (fg) {
//				accessToken = wcgSer.findTokenOrTicket(wtd.getTeamNo(), 1);
//				if (StringUtils.isEmpty(accessToken)) {
//					fg = U.setPutFalse(map, "微信授权失败");
//				}
//
//				U.log(log, "微信授权：accessToken=" + accessToken);
//			}
//
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				String title = "您好，您的用车需求车队已安排了师傅！";
//				if (U.getJdbcIp().contains(QC.TEST_SQL_IP))
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 行程类型
//				String routeType = order.getCarSeats() + " 座（包车）";
//				ps.add(new Item(routeType, "#000000", fs[1]));
//
//				// 行程明细
//				String routeline = "从 ";
//				routeline += MyStringUtils.getPointAddr(order.getDepartPlace());
//				routeline += " 到 ";
//				routeline += MyStringUtils.getPointAddr(order.getDestination());
//				ps.add(new Item(routeline, "#000000", fs[2]));
//
//				// 出发时间
//				String stime = DateUtils.DateToStr("yyyy-MM-dd HH:mm", order.getUseDayStart());
//				ps.add(new Item(stime, "#000000", fs[3]));
//
//				// 车辆及驾驶员
//				String carInfo = order.getPlateNum() + "、";
//				String driver = order.getDriver();
//				if (StringUtils.isNotBlank(driver)) {
//					carInfo += driver.split(",")[1] + "（" + driver.split(",")[0] + "）";
//				}
//				ps.add(new Item(carInfo, "#000000", fs[4]));
//
//				// 订单号
//				String orderNo = order.getOrderNum();
//				ps.add(new Item(orderNo, "#000000", fs[5]));
//
//				// 备注
//				String explain = order.getVieWay();
//				ps.add(new Item(explain, "#000000", fs[6]));
//
//				// 点击链接地址[驾驶员-我接的-旅游包车-订单列表]
//				String linkUrl = U.PRO_URL(QC.MOBILE_ROUTE_ID) + "carteam/yw-order-detail/" + order.getOrderNum();
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.useCarOfWxmsg(ps.toArray(new Item[ps.size()]), accessToken,
//						wxid);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-车辆安排提醒-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-车辆安排提醒-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, "通知业务员和旅行社（师傅确认订单成功后）");
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> cancelOrderOfWxmsg(WxTplmsgData wtd, JuniorCarOrder order, String mbName) {
//		String logtxt = U.log(log, "取消订单通知-驾驶员（计调取消订单）");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (wtd == null) {
//					U.log(log, "不存在[微信模板消息数据对象]则重新获取");
//
//					wtd = wxTplmsgDataSer.getWxTplmsgData(order.getTeamNo(), "T005");
//				} else {
//					U.log(log, "已存在[微信模板消息数据对象]");
//				}
//			}
//
//			if (fg) {
//				if (order == null) {
//					fg = U.setPutFalse(map, "传入订单对象不存在");
//				} else {
//					U.log(log, "订单编号：orderNum=" + order.getOrderNum());
//				}
//			}
//
//			String wxid = "";
//			if (fg) {
//				if (StringUtils.isEmpty(mbName)) {
//					fg = U.setPutFalse(map, "消息接收用户不能为空");
//				} else {
//					mbName = mbName.trim();
//					wxid = cusSer.takeOpenId(order.getTeamNo(), mbName);
//					if (StringUtils.isEmpty(wxid)) {
//						fg = U.setPutFalse(map, "用户[" + mbName + "]未关注微信公众号");
//					}
//
//					U.log(log, "消息接收用户：mbName=" + mbName);
//				}
//			}
//
//			String accessToken = "";
//			if (fg) {
//				accessToken = wcgSer.findTokenOrTicket(wtd.getTeamNo(), 1);
//				if (StringUtils.isEmpty(accessToken)) {
//					fg = U.setPutFalse(map, "微信授权失败");
//				}
//
//				U.log(log, "微信授权：accessToken=" + accessToken);
//			}
//
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				String title = "您好，您有订单已被计调取消！";
//				// 行程线路
//				title += "\r\n行程线路：";
//				String routeline = "从 ";
//				routeline += MyStringUtils.getPointAddr(order.getDepartPlace());
//				routeline += " 到 ";
//				routeline += MyStringUtils.getPointAddr(order.getDestination());
//				title += routeline;
//				if (U.getJdbcIp().contains(QC.TEST_SQL_IP))
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 订单编号
//				String orderNo = order.getOrderNum();
//				ps.add(new Item(orderNo, "#000000", fs[1]));
//
//				// 取消原因
//				String reason = "暂无";
//				if (StringUtils.isNotBlank(order.getRemarkPos())) {
//					reason = order.getRemarkPos().substring(order.getRemarkPos().lastIndexOf(",") + 1);
//				}
//				ps.add(new Item(reason, "#cccccc", fs[2]));
//
//				// 备注
//				String remark = "取消时间：";
//				remark += DateUtils.DateToStr(new Date());
//				remark += "\r\n点击查看取消订单详情";
//				ps.add(new Item(remark, "#000000", fs[3]));
//
//				// 点击链接地址[驾驶员-我接的-旅游包车-订单列表]
//				String linkUrl = U.PRO_URL(QC.MOBILE_ROUTE_ID) + "cus/cus-order-detail/" + order.getOrderNum();
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.cancelOrderOfWxmsg(ps.toArray(new Item[ps.size()]), accessToken,
//						wxid);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-取消订单通知-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-取消订单通知-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, logtxt);
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> orderWaitforCheckOfWxmsg(WxTplmsgData wtd, ReimburseList obj, String mbName) {
//		String logtxt = U.log(log, "添加记账报销-通知有权限的车队审核人（移动端驾驶员添加记账报销等）");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (wtd == null) {
//					U.log(log, "不存在[微信模板消息数据对象]则重新获取");
//
//					wtd = wxTplmsgDataSer.getWxTplmsgData(obj.getTeamNo(), "T007");
//				} else {
//					U.log(log, "已存在[微信模板消息数据对象]");
//				}
//			}
//
//			if (fg) {
//				if (obj == null) {
//					fg = U.setPutFalse(map, "传入报销对象不存在");
//				} else {
//					U.log(log, "订单编号：orderNum=" + obj.getOrderNum());
//				}
//			}
//
//			String wxid = "";
//			if (fg) {
//				if (StringUtils.isEmpty(mbName)) {
//					fg = U.setPutFalse(map, "消息接收用户不能为空");
//				} else {
//					mbName = mbName.trim();
//					wxid = cusSer.takeOpenId(obj.getTeamNo(), mbName);
//					if (StringUtils.isEmpty(wxid)) {
//						fg = U.setPutFalse(map, "用户[" + mbName + "]未关注微信公众号");
//					}
//
//					U.log(log, "消息接收用户：mbName=" + mbName);
//				}
//			}
//
//			String accessToken = "";
//			if (fg) {
//				accessToken = wcgSer.findTokenOrTicket(wtd.getTeamNo(), 1);
//				if (StringUtils.isEmpty(accessToken)) {
//					fg = U.setPutFalse(map, "微信授权失败");
//				}
//
//				U.log(log, "微信授权：accessToken=" + accessToken);
//			}
//
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				String title = "您有一条报账需要审核！";
//				if (obj.getIsCheck() == 0) {
//
//				} else if (obj.getIsCheck() == 1) {
//					title = "您有一条报账需要复核！";
//				} else if (obj.getIsCheck() == 2) {
//					title = "您有一条报账需要核销！";
//				} else if (obj.getIsCheck() == 3) {
//					title = "您有一条报账报销成功！";
//				}
//				if (U.getJdbcIp().contains(QC.TEST_SQL_IP))
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 订单编号
//				String orderNo = obj.getOrderNum();
//				ps.add(new Item(orderNo, "#000000", fs[1]));
//
//				// 车辆信息
//				String carInfo = obj.getPlateNum();
//				ps.add(new Item(carInfo, "#000000", fs[2]));
//
//				// 申请人
//				String applyUser = obj.getcName() + "-" + obj.getReimName();
//				ps.add(new Item(applyUser, "#000000", fs[3]));
//
//				// 申请时间
//				String atime = DateUtils.DateToStr(obj.getAddTime());
//				ps.add(new Item(atime, "#000000", fs[4]));
//
//				// 备注
//				String remark = "";// "点击进入审核页面";
//				ps.add(new Item(remark, "#000000", fs[5]));
//
//				// 点击链接地址[驾驶员-我接的-旅游包车-订单列表]
//				String linkUrl = "";// U.PRO_URL(QC.MOBILE_ROUTE_ID)+"cus/order-detail/"+obj.getOrderNum();
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.orderWaitforCheckOfWxmsg(ps.toArray(new Item[ps.size()]),
//						accessToken, wxid);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-订单待审核通知-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-订单待审核通知-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, logtxt);
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> financeReimburseOfWxmsg(WxTplmsgData wtd, ReimburseList obj, String mbName) {
//		String logtxt = U.log(log, "车队财务下账-通知");
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		boolean fg = true;
//
//		try {
//			if (fg) {
//				if (wtd == null) {
//					U.log(log, "不存在[微信模板消息数据对象]则重新获取");
//
//					wtd = wxTplmsgDataSer.getWxTplmsgData(obj.getTeamNo(), "T008");
//				} else {
//					U.log(log, "已存在[微信模板消息数据对象]");
//				}
//			}
//
//			if (fg) {
//				if (obj == null) {
//					fg = U.setPutFalse(map, "传入报销对象不存在");
//				} else {
//					U.log(log, "订单编号：orderNum=" + obj.getOrderNum());
//				}
//			}
//
//			String wxid = "";
//			if (fg) {
//				if (StringUtils.isEmpty(mbName)) {
//					fg = U.setPutFalse(map, "消息接收用户不能为空");
//				} else {
//					mbName = mbName.trim();
//					wxid = cusSer.takeOpenId(obj.getTeamNo(), mbName);
//					if (StringUtils.isEmpty(wxid)) {
//						fg = U.setPutFalse(map, "用户[" + mbName + "]未关注微信公众号");
//					}
//
//					U.log(log, "消息接收用户：mbName=" + mbName);
//				}
//			}
//
//			String accessToken = "";
//			if (fg) {
//				accessToken = wcgSer.findTokenOrTicket(wtd.getTeamNo(), 1);
//				if (StringUtils.isEmpty(accessToken)) {
//					fg = U.setPutFalse(map, "微信授权失败");
//				}
//
//				U.log(log, "微信授权：accessToken=" + accessToken);
//			}
//
//			if (fg) {
//				List<Item> ps = new ArrayList<Item>();
//				String[] fs = wtd.getPsArr().split(",");
//
//				// 标题
//				String title = "您有一条车队下账通知！";
//				if (U.getJdbcIp().contains(QC.TEST_SQL_IP))
//					title = "【测试消息，请忽略】" + title;
//				ps.add(new Item(title, "#000000", fs[0]));
//
//				// 客户
//				String cusInfo = obj.getReimName() + "（" + obj.getcName() + "）";
//				ps.add(new Item(cusInfo, "#000000", fs[1]));
//
//				// 下账金额
//				String money = obj.getTotalMoney() + "元";
//				ps.add(new Item(money, "#000000", fs[2]));
//
//				// 业务员
//				String oper = obj.getOperator();
//				ps.add(new Item(oper, "#000000", fs[3]));
//
//				// 时间
//				String atime = DateUtils.DateToStr(obj.getUseDayStart());
//				ps.add(new Item(atime, "#000000", fs[4]));
//
//				// 备注
//				String remark = ""; // "点击进入下账详情页面";
//				ps.add(new Item(remark, "#000000", fs[5]));
//
//				// 点击链接地址
//				String linkUrl = "";// U.PRO_URL(QC.MOBILE_ROUTE_ID)+"cus/order-detail/"+obj.getOrderNum();
//				ps.add(new Item(linkUrl, "#000000", wtd.getTplId()));
//
//				TemplateMsgResult tmr = SendTemplateMsg.financeReimburseOfWxmsg(ps.toArray(new Item[ps.size()]),
//						accessToken, wxid);
//
//				U.setPut(map, tmr.getErrcode(), tmr.getErrmsg());
//			}
//
//			if (fg) {
//				U.log(log, "【发送微信模板消息-车队财务下账通知-成功】");
//			} else {
//				U.log(log, "【发送微信模板消息-车队财务下账通知-失败】");
//			}
//		} catch (Exception e) {
//			U.setPutEx(map, log, e, logtxt);
//			e.printStackTrace();
//		}
//
//		return map;
//	}
//
//	/********** 微信模板消息--end ****************/
//
//}
