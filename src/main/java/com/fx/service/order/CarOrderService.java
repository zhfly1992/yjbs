package com.fx.service.order;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.CusRole;
import com.fx.commons.utils.enums.OrderSource;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.CompanyVehicle;
import com.fx.entity.company.Staff;
import com.fx.entity.cus.BaseUser;
import com.fx.entity.cus.CompanyUser;
import com.fx.entity.cus.Customer;
import com.fx.entity.order.CarOrder;

public interface CarOrderService extends BaseService<CarOrder, Long> {

	/**
	 * 添加-单程接送-订单
	 * 
	 * @param reqsrc
	 *            数据来源
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @param lcus
	 *            登录用户
	 * @param ordersrc
	 *            订单来源
	 * @param selCarId
	 *            选中的临时车辆价格对象id
	 * @param linkPhone
	 *            联系人电话
	 * @param upCarCount
	 *            上车人数
	 * @param note
	 *            备注 ge：有一个大拉箱；
	 * @param couponId
	 *            选中的优惠券id
	 * @return map{code: 结果状态码, msg: 结果状态码说明, id: 订单id, orderNum: 订单编号}
	 */
	public Map<String, Object> addOnewayTransferOrder(ReqSrc reqsrc, HttpServletRequest request,
			HttpServletResponse response, Customer lcus, OrderSource ordersrc, String selCarId, String linkPhone,
			String upCarCount, String note, String couponId);



	/**
	 * 
	 * @Description:业务付款列表(外调的已经确认付款价格的子订单)
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 *            搜索参数
	 * @param unitNum
	 * @return
	 * @author :zh
	 * @version 2020年4月30日
	 */
	public Map<String, Object> getCarOrderListForPayment(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject,String unitNum);



	/**
	 * 
	 * @Description:订单外调
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月2日
	 */
	public Map<String, Object> setExternal(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,JSONObject jsonObject);



	/**
	 * 
	 * @Description:取消订单外调
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月29日
	 */
	public Map<String, Object> cancelExternal(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,JSONObject jsonObject);



	/**
	 * 
	 * @Description:锁定订单外调
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月29日
	 */
	public Map<String, Object> lockExternal(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject,String externalLockMan);



	/**
	 * 
	 * @Description:解锁订单外调 
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月29日
	 */
	public Map<String, Object> unlockExternal(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject,String externalUnLockMan);



	/**
	 * 
	 * @Description:删除订单
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 *            idList  子订单id数组
	 *            mainOrderNum 主订单编号
	 * @return
	 * @author :zh
	 * @version 2020年5月3日
	 */
	public Map<String, Object> deleteOrder(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);



	/**
	 * 
	 * @Description:通过id获取订单
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 *            包含订单id(id)
	 * @return
	 * @author :zh
	 * @version 2020年5月2日
	 */
	public Map<String, Object> getCarOrderById(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);



	/**
	 * 
	 * @Description:撤销派车
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月2日
	 */
	public Map<String, Object> cancelDisCar(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);



	/**
	 * 
	 * @Description:修改订单
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月3日
	 */
	public Map<String, Object> updateOrder(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);


	/**
	 * 
	 * @Description:根据mainOrderNum查询所有子订单
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 *            包含mainOrderNum
	 * @return
	 * @author :zh
	 * @version 2020年5月2日
	 */
	public Map<String, Object> getAllCarOrderByMainOrderNum(ReqSrc reqsrc, HttpServletResponse response,
			HttpServletRequest request, JSONObject jsonObject);



	/**
	 * 添加-旅游包车-行程订单
	 * 
	 * @author qfc
	 * @param reqsrc
	 *            请求来源
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @param teamNo
	 *            车队编号
	 * @param lcus
	 *            登录用户
	 * @param id
	 *            临时车辆价格对象id
	 * @param isBill
	 *            是否开发票 0-不开发票；1-开发票；
	 * @return map{code: 结果状态码, msg: 结果状态信息, mainOrderNum: 旅游包车主订单编号}
	 */
	public Map<String, Object> addLybcOrder(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response,
			String teamNo, Customer lcus, OrderSource orderSource, String id, String isBill);



	/**
	 * 单位添加-往返-行程订单
	 * 
	 * @author xx
	 * @version 20200514
	 * @param reqsrc
	 *            请求来源
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @param compnayName
	 *            单位名称
	 * @param unitNum
	 *            单位编号
	 * @param companyCusId
	 *            客户id
	 * @param serviceMan
	 *            业务员
	 * @param dutyService
	 *            业务负责人
	 * @param routeLink
	 *            乘车联系人
	 * @param mainOrderNum
	 *            主订单编号
	 * @return map{code: 结果状态码, msg: 结果状态信息, mainOrderNum: 旅游包车主订单编号}
	 */
	public Map<String, Object> addCompanyLybcOrder(ReqSrc reqsrc, HttpServletRequest request,
			HttpServletResponse response, String compnayName, String unitNum, String companyCusId, String serviceMan,
			String dutyService, String routeLink, String mainOrderNum);



	/**
	 * 单位添加-单程接送-订单
	 * 
	 * @author xx
	 * @version 20200513
	 * @param reqsrc
	 *            数据来源
	 * @param request
	 *            request
	 * @param response
	 *            response
	 * @param jsonObject
	 *            前端请求参数
	 * @param companyName
	 *            单位名称
	 * @param unitNum
	 *            单位编号
	 * @return map{code: 结果状态码, msg: 结果状态码说明}
	 */
	public Map<String, Object> addCompanyOnewayTransferOrder(ReqSrc reqsrc, HttpServletRequest request,
			HttpServletResponse response, JSONObject jsonObject, String companyName, String unitNum);
	


	/**
	 * 
	 * @Description:确认付款价格
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @param customer
	 * @return
	 * @author :zh
	 * @version 2020年5月13日
	 */
	public Map<String, Object> confirmPayment(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject,Staff staff);
	
	/**
	 * 业务付款
	 * @author xx
	 * @date 20200602
	 * @param reqsrc 			请求来源
	 * @param request 			request
	 * @param unitNum 		单位编号
	 * @param staff			当前员工
	 * @param ids 付款订单id,多个逗号拼接
	 * @param payMoney 付款金额
	 * @param payRemark 摘要
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> servicePay(ReqSrc reqsrc, HttpServletRequest request,
			String unitNum, Staff staff, String ids,String payMoney, String payRemark);



	/**
	 * 根据车牌号和时间找到对应订单
	 * 
	 * @author xx
	 * @param date
	 *            20200525
	 * @param reqsrc
	 *            请求来源
	 * @param request
	 * @param plateNum
	 *            车牌号
	 * @param inTime
	 *            进站时间
	 * @param ouTime
	 *            出站时间
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], data[数据对象]}
	 */
	public Map<String, Object> findOrderByPlateTime(ReqSrc reqsrc, HttpServletRequest request, String plateNum,
			String inTime, String ouTime);

	/**
	  *  人工派单-获取最符合条件的车辆
	 * @author xx
	 * @date 2020061
	 * @param sendOrderNum 派单订单号
	 * @param seats 	座位数
	 * @param force 	强制查询
   * @param runArea 	运营区域
   * @param plateNum 	车牌号
   * @param selfOwned 0自营 1挂靠
   * @param avgSpeed 平均速度
   * @param notContainPn 不包含的车牌号
	 * @return map
	 */
	public Map<String, Object> handleSendCar(ReqSrc reqsrc,String sendOrderNum,String seats,String force,
			String runArea,String plateNum,String selfOwned,String avgSpeed,String notContainPn);

	/**
	 * 智能派单获取车辆
	 * 
	 * @author xx
	 * @date 20200518
	 * @param reqsrc
	 * @param response
	 * @param request
	 *            request
	 * @param sendOrderNum
	 *            派车订单号
	 * @param firstCar
	 *            优先顺序 1自营优先 2挂靠优先
	 * @param seats
	 *            座位数
	 * @param notContainPn
	 *            不包含的车牌号
	 * @param sendPlate
	 *            选中的车牌号
	 * @param sendModel 0淡季模式 1旺季模式
	 * @return map
	 */
	public Map<String, Object> smartSendOrder(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String sendOrderNum, String firstCar, String seats, String notContainPn, String sendPlate,String sendModel);



	/**
	 * 智能派单前端选择确认派单
	 * 
	 * @author xx
	 * @date 20200519
	 * @param reqsrc
	 * @param response
	 * @param request
	 *            request
	 * @param cancelNum
	 *            取消订单号
	 * @param colist
	 *            派车订单
	 * @param car
	 *            派单车辆
	 * @param sendOrderNum
	 *            派单订单号
	 * @param sendPlateNum
	 *            派单车牌号
	 * @return map
	 */
	public Map<String, Object> confirmSendSmart(ReqSrc reqsrc, Map<String, Object> sendMap, String cancelNum,
			String sendOrderNum, String sendPlateNum, List<CarOrder> colist, CompanyVehicle car);



	/**
	 * 	人工派单线下车辆
	 * @author xx
	 * @date 20200529
	 * @param reqsrc
	 * @param response
	 * @param request 	request
	 * @param sendOrderNum  派单订单号
	 * @param suppCar       供车方单位名称
	 * @param suppCarHead   供车方业务负责人
	 * @param sendPlateNum  派单车牌号
	 * @param driverInfo    线下师傅信息
	 * @param sendPrice     派单价格
	 * @param driverGath    师傅现收
	 * @return map
	 */
	public Map<String, Object> confirmSendUnder(ReqSrc reqsrc,String sendOrderNum,String suppCar,String suppCarHead,
			String sendPlateNum,String driverInfo,String sendPrice,String driverGath);
	
	/**
	 * 
	 * @Description:确认用车
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年5月19日
	 */
	public Map<String, Object> confirmUseCar(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);
	
	/**
	 * 
	 * @Description:取消确认付款
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param jsonObject
	 * @return
	 * @author :zh
	 * @version 2020年6月2日
	 */
	public Map<String, Object> cancelConfirmPayment(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			JSONObject jsonObject);

	/**
	 * 获取-车队驾驶员-订单列表
	 * @param reqsrc 	请求来源
	 * @param buser 	登录用户基类
	 * @param comUser 	登录单位对象
	 * @param page 		页码
	 * @param rows 		页大小
	 * @param stime 	出行开始时间
	 * @param etime 	出行结束时间
	 * @param isTrip 	出行类型：1-未出行；0-已出行；
	 * @return map{code: 结果状态码, msg: 结果状态码说明,data: 数据列表，count: 数据总条数}
	 */
	public Map<String, Object> findDriverOrderList(ReqSrc reqsrc, BaseUser buser, CompanyUser comUser,
		String page, String rows, String stime, String etime, String isTrip);
	
	/**
	 * 
	 * @Description:
	 * @param reqsrc
	 * @param response
	 * @param request
	 * @param orderId  子订单id
	 * @return
	 * @author :zh
	 * @version 2020年6月3日
	 */
	public Map<String, Object> JLComfirm(ReqSrc reqsrc, HttpServletResponse response, HttpServletRequest request,
			String carOrderId);



	/**
	 * 驾驶员-确认订单
	 * @param reqsrc 		请求来源
	 * @param role 			用户角色 
	 * @param request 		request
	 * @param response 		response
	 * @param orderNum 		订单编号
	 * @param isAgree 		1-同意; 2-拒绝;
	 * @param reason 		拒绝理由，100中文字符
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> driverCofmOrder(ReqSrc reqsrc, CusRole role, HttpServletRequest request, 
		HttpServletResponse response, String orderNum, String isAgree, String reason);

	/**
	 * 驾驶员-确认订单出行
	 * @param reqsrc 			请求来源
	 * @param request 			request
	 * @param response 			response
	 * @param orderNum 			派车-订单编号
	 * @param lnglat 			驾驶员确认出行-坐标：103.123456|30.123456
	 * @param isArr				是否到达[1-已到达出行地点；2-已到达完团地点；3-未到达；]
	 * @param isUpCar			乘客是否上车：1-已上车；0-未上车；（点击时使用）
	 * @param isToDP			是否到下车点：1-已到下车点；0-还在走行程；（点击时使用）
	 * @param okBack			确认直接回程完团：1-确认；否则-点错了；（点击时使用）
	 * 
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> updCofmOrderGo(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String orderNum, String lnglat, String isArr, String isUpCar, String isToDP, String okBack);
	
	/**
	 * 驾驶员-确认-完团（乘客下车）
	 * @param reqsrc 			请求来源
	 * @param request 			request
	 * @param response 			response
	 * @param orderNum 			派车-订单编号
	 * @param dayId				天数行程id
	 * @param lnglat 			驾驶员确认出行-坐标：103.123456|30.123456
	 * @param isArr				是否到达出行地点
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> driverCofmDownCar(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String orderNum,String dayId, String lnglat, String isArr);
	
	/**
	 * 获取-子订单列表
	 * @param reqsrc 	请求来源
	 * @param lunitNum	登录单位编号
	 * @param luname 	登录用户名
	 * @param mid 		主订单id
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据}
	 */
	public Map<String, Object> findXcjzOrderList(ReqSrc reqsrc, String lunitNum, String luname, String mid);

	/**
	 * 验证-驾驶员位置是否是订单出发点/完团地点
	 * @param reqsrc 			请求来源
	 * @param request 			request
	 * @param response 			response
	 * @param orderNum 			派车-订单编号
	 * @param lnglat 			驾驶员确认出行-坐标：103.123456|30.123456
	 * @return map{code: 结果状态码, msg: 结果状态说明}
	 */
	public Map<String, Object> valCofmGoOrDownCar(ReqSrc reqsrc, HttpServletRequest request, HttpServletResponse response, 
		String orderNum, String lnglat);
	
}
