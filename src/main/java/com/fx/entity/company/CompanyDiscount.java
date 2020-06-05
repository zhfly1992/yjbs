package com.fx.entity.company;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 优惠券领取列表和车队设置都添加到此
 */
@Entity
@Table(name="company_discount")
public class CompanyDiscount implements Serializable,Cloneable {
	private static final long serialVersionUID = -5190052952146131849L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 推荐领取用户名
	 */
	@Column(name="rec_name", columnDefinition="varchar(20) COMMENT '推荐领取用户名'")
	private String recName;
	/**
	 * 领取/添加用户名
	 */
	@Column(name="receipt_name", columnDefinition="varchar(20) COMMENT '领取/添加用户名'")
	private String receiptName;
	/**
	 * 优惠券编号 （优惠券编号[车队编号]）
	 */
	@Column(name="discount_No", columnDefinition="varchar(20) COMMENT '优惠券编号'")
	private String discountNo;
	/**
	 * 优惠券名称
	 */
	@Column(name="discount_name", columnDefinition="varchar(20) COMMENT '优惠券名称'")
	private String discountName;
	/**
	 * 领取有效期开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="provide_start", columnDefinition="datetime COMMENT '领取有效期开始时间'")
	private Date provideStart;
	/**
	 * 领取有效期结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="provide_end", columnDefinition="datetime COMMENT '领取有效期结束时间'")
	private Date provideEnd;
	/**
	 * 使用有效期开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="receipt_start", columnDefinition="datetime COMMENT '使用有效期开始时间'")
	private Date receiptStart;
	/**
	 * 使用有效期结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="receipt_end", columnDefinition="datetime COMMENT '使用有效期结束时间'")
	private Date receiptEnd;
	/**
	 * 领取之后多少天内有效
	 */
	@Column(name="valid_day", columnDefinition="int default 0 COMMENT '领取之后多少天内有效'")
	private int validDay;
	/**
	 * 使用场景 1单程接送  2旅游包车  3顺风车
	 */
	@Column(name="valid_scene", columnDefinition="int default 0 COMMENT '使用场景 1单程接送  2旅游包车  3顺风车'")
	private int validScene;
	/**
	 * 使用车型：5,7,14...
	 */
	@Column(name="valid_seats", columnDefinition="varchar(100) COMMENT '使用车型 5,7,14...'")
	private String validSeats;
	/**
	 * 优惠金额 1固定金额 2随机金额
	 */
	@Column(name="valid_money", columnDefinition="int default 0 COMMENT '优惠金额 1固定金额 2随机金额'")
	private int validMoney;
	/**
	 * 优惠券最低金额
	 */
	@Column(name="low_money", columnDefinition="double(20,2) default 0 COMMENT '优惠券最低金额'")
	private double lowMoney;
	/**
	 * 优惠券最高金额/固定金额
	 */
	@Column(name="high_money", columnDefinition="double(20,2) default 0 COMMENT '优惠券最高金额/固定金额'")
	private double highMoney;
	/**
	 * 使用方式:1全额抵扣 2按订单比例 3按优惠券比例
	 */
	@Column(name="valid_way", columnDefinition="int default 0 COMMENT '使用方式:1全额抵扣 2按订单比例 3按优惠券比例'")
	private int validWay;
	/**
	 * 使用比例
	 */
	@Column(name="use_ratio", columnDefinition="double(20,2) default 0 COMMENT '使用比例'")
	private double useRatio;
	/**
	 * 使用次数:1单次使用 2多次使用
	 */
	@Column(name="single_way", columnDefinition="int default 0 COMMENT '使用次数:1单次使用 2多次使用'")
	private int singleWay;
	/**
	 * 根据使用比例产生的已使用金额
	 */
	@Column(name="use_money", columnDefinition="double(20,2) default 0 COMMENT '根据使用比例产生的已使用金额'")
	private double useMoney;
	/**
	 * 使用客户:1任意客户 2新客户 3下单成功客户  4按客户星级级别
	 */
	@Column(name="valid_customer", columnDefinition="int default 0 COMMENT '使用客户:1任意客户 2新客户 3下单成功客户  4按客户星级级别'")
	private int validCustomer;
	/**
	 * 使用客户=2时代表天数;使用客户=3时代表订单数;使用客户=4时代表客户星级
	 */
	@Column(name="valid_count", columnDefinition="int default 0 COMMENT '使用客户=2时代表天数;使用客户=3时代表订单数'")
	private int validCount;
	/**
	 * 使用客户=3时代表计算单数的开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="success_start", columnDefinition="datetime COMMENT '使用客户=3时代表计算单数的开始时间'")
	private Date successStart;
	/**
	 * 分享推广：1参加分享推广 2不参与分享推广
	 */
	@Column(name="share_popular", columnDefinition="int default 0 COMMENT '分享推广：1参加分享推广 2不参与分享推广'")
	private int sharePopular;	
	/**
	 * 状态：-2使用完成 -1已失效 0未使用 非0和-1代表已使用次数
	 */
	@Column(name="use_state", columnDefinition="int default 0 COMMENT '-1已失效 0未使用 非0和-1代表已使用次数'")
	private int useState;	
	/**
	 * 使用时间根据次数产生多个时间,逗号拼接
	 */
	@Column(name="use_time", columnDefinition="longtext COMMENT '使用时间根据次数产生多个时间,逗号拼接'")
	private String useTime;
	/**
	 * 备注说明
	 */
	@Column(name="note", columnDefinition="longtext COMMENT '备注说明'")
	private String note;
	/**
	 * 下单方式 全部订单：PCALL 前台：PCHOME 后台：PCTEAM 无：NONE
	 */
	@Column(name="add_way", columnDefinition="varchar(20) COMMENT '下单方式'")
	private String addWay;
	/**
	 * 添加/领取时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="add_time", columnDefinition="datetime COMMENT '添加/领取时间'")
	private Date addTime;
	
	
	
	/**  
	 * 获取 id  
	 * @return id
	 */
	public long getId() {
		return id;
	}
	
	/**  
	 * 设置 id  
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**  
	 * 获取 推荐领取用户名  
	 * @return recName
	 */
	public String getRecName() {
		return recName;
	}
	
	/**  
	 * 设置 推荐领取用户名  
	 * @param recName
	 */
	public void setRecName(String recName) {
		this.recName = recName;
	}
	
	/**  
	 * 获取 领取添加用户名  
	 * @return receiptName
	 */
	public String getReceiptName() {
		return receiptName;
	}
	
	/**  
	 * 设置 领取添加用户名  
	 * @param receiptName
	 */
	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}
	
	/**  
	 * 获取 优惠券编号（优惠券编号[车队编号]）  
	 * @return discountNo
	 */
	public String getDiscountNo() {
		return discountNo;
	}
	
	/**  
	 * 设置 优惠券编号（优惠券编号[车队编号]）  
	 * @param discountNo
	 */
	public void setDiscountNo(String discountNo) {
		this.discountNo = discountNo;
	}
	
	/**  
	 * 获取 优惠券名称  
	 * @return discountName
	 */
	public String getDiscountName() {
		return discountName;
	}
	
	/**  
	 * 设置 优惠券名称  
	 * @param discountName
	 */
	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}
	
	/**  
	 * 获取 领取有效期开始时间  
	 * @return provideStart
	 */
	public Date getProvideStart() {
		return provideStart;
	}
	
	/**  
	 * 设置 领取有效期开始时间  
	 * @param provideStart
	 */
	public void setProvideStart(Date provideStart) {
		this.provideStart = provideStart;
	}
	
	/**  
	 * 获取 领取有效期结束时间  
	 * @return provideEnd
	 */
	public Date getProvideEnd() {
		return provideEnd;
	}
	
	/**  
	 * 设置 领取有效期结束时间  
	 * @param provideEnd
	 */
	public void setProvideEnd(Date provideEnd) {
		this.provideEnd = provideEnd;
	}
	
	/**  
	 * 获取 使用有效期开始时间  
	 * @return receiptStart
	 */
	public Date getReceiptStart() {
		return receiptStart;
	}
	
	/**  
	 * 设置 使用有效期开始时间  
	 * @param receiptStart
	 */
	public void setReceiptStart(Date receiptStart) {
		this.receiptStart = receiptStart;
	}
	
	/**  
	 * 获取 使用有效期结束时间  
	 * @return receiptEnd
	 */
	public Date getReceiptEnd() {
		return receiptEnd;
	}
	
	/**  
	 * 设置 使用有效期结束时间  
	 * @param receiptEnd
	 */
	public void setReceiptEnd(Date receiptEnd) {
		this.receiptEnd = receiptEnd;
	}
	
	/**  
	 * 获取 领取之后多少天内有效  
	 * @return validDay
	 */
	public int getValidDay() {
		return validDay;
	}
	
	/**  
	 * 设置 领取之后多少天内有效  
	 * @param validDay
	 */
	public void setValidDay(int validDay) {
		this.validDay = validDay;
	}
	
	/**  
	 * 获取 使用场景1单程接送2旅游包车3顺风车  
	 * @return validScene
	 */
	public int getValidScene() {
		return validScene;
	}
	
	/**  
	 * 设置 使用场景1单程接送2旅游包车3顺风车  
	 * @param validScene
	 */
	public void setValidScene(int validScene) {
		this.validScene = validScene;
	}
	
	/**  
	 * 获取 使用车型：5714...  
	 * @return validSeats
	 */
	public String getValidSeats() {
		return validSeats;
	}
	
	/**  
	 * 设置 使用车型：5714...  
	 * @param validSeats
	 */
	public void setValidSeats(String validSeats) {
		this.validSeats = validSeats;
	}
	
	/**  
	 * 获取 优惠金额1固定金额2随机金额  
	 * @return validMoney
	 */
	public int getValidMoney() {
		return validMoney;
	}
	
	/**  
	 * 设置 优惠金额1固定金额2随机金额  
	 * @param validMoney
	 */
	public void setValidMoney(int validMoney) {
		this.validMoney = validMoney;
	}
	
	/**  
	 * 获取 优惠券最低金额  
	 * @return lowMoney
	 */
	public double getLowMoney() {
		return lowMoney;
	}
	
	/**  
	 * 设置 优惠券最低金额  
	 * @param lowMoney
	 */
	public void setLowMoney(double lowMoney) {
		this.lowMoney = lowMoney;
	}
	
	/**  
	 * 获取 优惠券最高金额固定金额  
	 * @return highMoney
	 */
	public double getHighMoney() {
		return highMoney;
	}
	
	/**  
	 * 设置 优惠券最高金额固定金额  
	 * @param highMoney
	 */
	public void setHighMoney(double highMoney) {
		this.highMoney = highMoney;
	}
	
	/**  
	 * 获取 使用方式:1全额抵扣2按订单比例3按优惠券比例  
	 * @return validWay
	 */
	public int getValidWay() {
		return validWay;
	}
	
	/**  
	 * 设置 使用方式:1全额抵扣2按订单比例3按优惠券比例  
	 * @param validWay
	 */
	public void setValidWay(int validWay) {
		this.validWay = validWay;
	}
	
	/**  
	 * 获取 使用比例  
	 * @return useRatio
	 */
	public double getUseRatio() {
		return useRatio;
	}
	
	/**  
	 * 设置 使用比例  
	 * @param useRatio
	 */
	public void setUseRatio(double useRatio) {
		this.useRatio = useRatio;
	}
	
	/**  
	 * 获取 使用次数:1单次使用2多次使用  
	 * @return singleWay
	 */
	public int getSingleWay() {
		return singleWay;
	}
	
	/**  
	 * 设置 使用次数:1单次使用2多次使用  
	 * @param singleWay
	 */
	public void setSingleWay(int singleWay) {
		this.singleWay = singleWay;
	}
	
	/**  
	 * 获取 根据使用比例产生的已使用金额  
	 * @return useMoney
	 */
	public double getUseMoney() {
		return useMoney;
	}
	
	/**  
	 * 设置 根据使用比例产生的已使用金额  
	 * @param useMoney
	 */
	public void setUseMoney(double useMoney) {
		this.useMoney = useMoney;
	}
	
	/**  
	 * 获取 使用客户:1任意客户2新客户3下单成功客户4按客户星级级别  
	 * @return validCustomer
	 */
	public int getValidCustomer() {
		return validCustomer;
	}
	
	/**  
	 * 设置 使用客户:1任意客户2新客户3下单成功客户4按客户星级级别  
	 * @param validCustomer
	 */
	public void setValidCustomer(int validCustomer) {
		this.validCustomer = validCustomer;
	}
	
	/**  
	 * 获取 使用客户=2时代表天数;使用客户=3时代表订单数;使用客户=4时代表客户星级  
	 * @return validCount
	 */
	public int getValidCount() {
		return validCount;
	}
	
	/**  
	 * 设置 使用客户=2时代表天数;使用客户=3时代表订单数;使用客户=4时代表客户星级  
	 * @param validCount
	 */
	public void setValidCount(int validCount) {
		this.validCount = validCount;
	}
	
	/**  
	 * 获取 使用客户=3时代表计算单数的开始时间  
	 * @return successStart
	 */
	public Date getSuccessStart() {
		return successStart;
	}
	
	/**  
	 * 设置 使用客户=3时代表计算单数的开始时间  
	 * @param successStart
	 */
	public void setSuccessStart(Date successStart) {
		this.successStart = successStart;
	}
	
	/**  
	 * 获取 分享推广：1参加分享推广2不参与分享推广  
	 * @return sharePopular
	 */
	public int getSharePopular() {
		return sharePopular;
	}
	
	/**  
	 * 设置 分享推广：1参加分享推广2不参与分享推广  
	 * @param sharePopular
	 */
	public void setSharePopular(int sharePopular) {
		this.sharePopular = sharePopular;
	}
	
	/**
	 * -2使用完成 -1已失效 0未使用 非0和-1代表已使用次数
	 * @return useState
	 */
	public int getUseState() {
		return useState;
	}
	/**
	 * -2使用完成 -1已失效 0未使用 非0和-1代表已使用次数
	 * @param useState
	 */
	public void setUseState(int useState) {
		this.useState = useState;
	}
	
	/**  
	 * 获取 使用时间根据次数产生多个时间逗号拼接  
	 * @return useTime
	 */
	public String getUseTime() {
		return useTime;
	}
	

	/**  
	 * 设置 使用时间根据次数产生多个时间逗号拼接  
	 * @param useTime
	 */
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	

	/**  
	 * 获取 备注说明  
	 * @return note
	 */
	public String getNote() {
		return note;
	}
	
	/**  
	 * 设置 备注说明  
	 * @param note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/**
	 * 下单方式 全部订单：PCALL 前台：PCHOME 后台：PCTEAM
	 * @return addWay
	 */
	public String getAddWay() {
		return addWay;
	}
	/**
	 * 下单方式 全部订单：PCALL 前台：PCHOME 后台：PCTEAM
	 * @param addWay
	 */
	public void setAddWay(String addWay) {
		this.addWay = addWay;
	}
	/**  
	 * 获取 添加领取时间  
	 * @return addTime
	 */
	public Date getAddTime() {
		return addTime;
	}
	/**  
	 * 设置添加领取时间  
	 * @param addTime 
	 */
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
} 
