package com.fx.entity.cus;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fx.commons.utils.enums.ShStatus;

/**
 * 钱包余额信息
 * @author qfc
 * @Date 20200426
 */
@Entity
@Table(name="cus_wallet")
public class CusWallet{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * 操作账户 唯一
	 */
	@Column(name="c_name", nullable=false, unique=true, columnDefinition="varchar(50) COMMENT '操作账户 唯一'")
	private String cName;
	/**
	 * 账户余额
	 */
	@Column(name="cash_balance", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '账户余额'")
	private double cashBalance;
	/**
	 * 红包/积分/消费金/帮币
	 */
	@Column(name="present_balance", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '红包/积分/消费金/帮币'")
	private double presentBalance;
	/**
	 * 押金
	 */
	@Column(name="cash_pledge", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '押金'")
	private double cashPledge;
	/**
	 * 未完团定金
	 */
	@Column(name="not_finish", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '未完团定金'")
	private double notFinish;
	/**
	 * 提成金额
	 */
	@Column(name="royalty", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '提成金额'")
	private double royalty;
	/**
	 * 分享解冻后消费余额 20191118新增
	 */
	@Column(name="share_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '分享解冻后消费余额'")
	private double shareMoney;
	/**
	 * 用车方包年/包月用户有效开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lookStimeAgent;
	/**
	 * 用车方包年/包月用户有效结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lookEtimeAgent;
	/**
	 * 出租方包年/包月有效开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lookStimeDriver;
	/**
	 * 出租方包年/包月有效结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lookEtimeDriver;
	/**
	 * 累计消费金额
	 */
	@Column(name="consume_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '累计消费金额'")
	private double consumeMoney;
	/**
	 * 累计分红金额
	 */
	@Column(name="dividend_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '累计分红金额'")
	private double dividendMoney;
	/**
	 * 除去分红权后已分红金额
	 */
	@Column(name="residual_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '除去分红权后已分红金额'")
	private double  residualMoney;
	/**
	 * 分红权
	 */
	@Column(name="dividend", nullable=false, columnDefinition="int(11) default '0' COMMENT '分红权'")
	private int dividend;
	/**
	 * 推荐奖励金额
	 */
	@Column(name="recom_money", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '推荐奖励金额'")
	private double recomMoney;
	/**
	 * 团上现收金额（师傅应付给车队）
	 */
	@Column(name="route_cash", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '团上现收金额（师傅应付给车队）'")
	private double routeCash;
	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date atime;
	
	/****移动端-新增**************************/
	/**
	 * 显示/隐藏-金额，默认：显示=SHOW_STA
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="sh_sta", nullable=false, columnDefinition="varchar(20) default 'SHOW_STA' COMMENT '显示/隐藏-金额'")
	private ShStatus shSta = ShStatus.SHOW_STA;
	/**
	 * 驾驶员完团-总额
	 */
	@Column(name="fin_total", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '驾驶员完团-总额'")
	private double finTotal;
	/**
	 * 驾驶员完团-余额
	 */
	@Column(name="fin_balance", nullable=false, columnDefinition="double(20,2) default 0 COMMENT '驾驶员完团-余额'")
	private double finBalance;
	
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
	 * 获取 操作账户唯一  
	 * @return cName  
	 */
	public String getcName() {
		return cName;
	}
	/**  
	 * 设置 操作账户唯一  
	 * @param cName
	 */
	public void setcName(String cName) {
		this.cName = cName;
	}
	/**  
	 * 获取 账户余额  
	 * @return cashBalance  
	 */
	public double getCashBalance() {
		return cashBalance;
	}
	/**  
	 * 设置 账户余额  
	 * @param cashBalance
	 */
	public void setCashBalance(double cashBalance) {
		this.cashBalance = cashBalance;
	}
	/**  
	 * 获取 红包积分消费金帮币  
	 * @return presentBalance  
	 */
	public double getPresentBalance() {
		return presentBalance;
	}
	/**  
	 * 设置 红包积分消费金帮币  
	 * @param presentBalance
	 */
	public void setPresentBalance(double presentBalance) {
		this.presentBalance = presentBalance;
	}
	/**  
	 * 获取 押金  
	 * @return cashPledge  
	 */
	public double getCashPledge() {
		return cashPledge;
	}
	/**  
	 * 设置 押金  
	 * @param cashPledge
	 */
	public void setCashPledge(double cashPledge) {
		this.cashPledge = cashPledge;
	}
	/**  
	 * 获取 未完团定金  
	 * @return notFinish  
	 */
	public double getNotFinish() {
		return notFinish;
	}
	/**  
	 * 设置 未完团定金  
	 * @param notFinish
	 */
	public void setNotFinish(double notFinish) {
		this.notFinish = notFinish;
	}
	/**  
	 * 获取 提成金额  
	 * @return royalty  
	 */
	public double getRoyalty() {
		return royalty;
	}
	/**  
	 * 设置 提成金额  
	 * @param royalty
	 */
	public void setRoyalty(double royalty) {
		this.royalty = royalty;
	}
	/**  
	 * 获取 用车方包年包月用户有效开始时间  
	 * @return lookStimeAgent  
	 */
	public Date getLookStimeAgent() {
		return lookStimeAgent;
	}
	/**  
	 * 设置 用车方包年包月用户有效开始时间  
	 * @param lookStimeAgent
	 */
	public void setLookStimeAgent(Date lookStimeAgent) {
		this.lookStimeAgent = lookStimeAgent;
	}
	/**  
	 * 获取 用车方包年包月用户有效结束时间  
	 * @return lookEtimeAgent  
	 */
	public Date getLookEtimeAgent() {
		return lookEtimeAgent;
	}
	/**  
	 * 设置 用车方包年包月用户有效结束时间  
	 * @param lookEtimeAgent
	 */
	public void setLookEtimeAgent(Date lookEtimeAgent) {
		this.lookEtimeAgent = lookEtimeAgent;
	}
	/**  
	 * 获取 出租方包年包月有效开始时间  
	 * @return lookStimeDriver  
	 */
	public Date getLookStimeDriver() {
		return lookStimeDriver;
	}
	/**  
	 * 设置 出租方包年包月有效开始时间  
	 * @param lookStimeDriver
	 */
	public void setLookStimeDriver(Date lookStimeDriver) {
		this.lookStimeDriver = lookStimeDriver;
	}
	/**  
	 * 获取 出租方包年包月有效结束时间  
	 * @return lookEtimeDriver  
	 */
	public Date getLookEtimeDriver() {
		return lookEtimeDriver;
	}
	/**  
	 * 设置 出租方包年包月有效结束时间  
	 * @param lookEtimeDriver
	 */
	public void setLookEtimeDriver(Date lookEtimeDriver) {
		this.lookEtimeDriver = lookEtimeDriver;
	}
	/**  
	 * 获取 累计消费金额  
	 * @return consumeMoney  
	 */
	public double getConsumeMoney() {
		return consumeMoney;
	}
	/**  
	 * 设置 累计消费金额  
	 * @param consumeMoney
	 */
	public void setConsumeMoney(double consumeMoney) {
		this.consumeMoney = consumeMoney;
	}
	/**  
	 * 获取 累计分红金额  
	 * @return dividendMoney  
	 */
	public double getDividendMoney() {
		return dividendMoney;
	}
	/**  
	 * 设置 累计分红金额  
	 * @param dividendMoney
	 */
	public void setDividendMoney(double dividendMoney) {
		this.dividendMoney = dividendMoney;
	}
	/**  
	 * 获取 除去分红权后已分红金额  
	 * @return residualMoney  
	 */
	public double getResidualMoney() {
		return residualMoney;
	}
	/**  
	 * 设置 除去分红权后已分红金额  
	 * @param residualMoney
	 */
	public void setResidualMoney(double residualMoney) {
		this.residualMoney = residualMoney;
	}
	/**  
	 * 获取 分红权  
	 * @return dividend  
	 */
	public int getDividend() {
		return dividend;
	}
	/**  
	 * 设置 分红权  
	 * @param dividend
	 */
	public void setDividend(int dividend) {
		this.dividend = dividend;
	}
	/**  
	 * 获取 推荐奖励金额  
	 * @return recomMoney  
	 */
	public double getRecomMoney() {
		return recomMoney;
	}
	/**  
	 * 设置 推荐奖励金额  
	 * @param recomMoney
	 */
	public void setRecomMoney(double recomMoney) {
		this.recomMoney = recomMoney;
	}
	/**  
	 * 获取 创建时间  
	 * @return atime  
	 */
	public Date getAtime() {
		return atime;
	}
	/**  
	 * 设置 创建时间  
	 * @param atime
	 */
	public void setAtime(Date atime) {
		this.atime = atime;
	}
	public double getRouteCash() {
		return routeCash;
	}
	public void setRouteCash(double routeCash) {
		this.routeCash = routeCash;
	}
	/**  
	 * 获取 显示/隐藏-金额
	 * @return shSta  
	 */
	public ShStatus getShSta() {
		return shSta;
	}
	/**  
	 * 设置 显示/隐藏-金额
	 * @param shSta
	 */
	public void setShSta(ShStatus shSta) {
		this.shSta = shSta;
	}
	/**
	 * 获取 驾驶员完团-总额
	 * @return finTotal
	 */
	public double getFinTotal() {
		return finTotal;
	}
	/**
	 * 设置 驾驶员完团-总额
	 * @param finTotal
	 */
	public void setFinTotal(double finTotal) {
		this.finTotal = finTotal;
	}
	/**
	 * 获取 驾驶员完团-余额
	 * @return finBalance
	 */
	public double getFinBalance() {
		return finBalance;
	}
	/**
	 * 设置 驾驶员完团-余额
	 * @param finBalance
	 */
	public void setFinBalance(double finBalance) {
		this.finBalance = finBalance;
	}
	public double getShareMoney() {
		return shareMoney;
	}
	public void setShareMoney(double shareMoney) {
		this.shareMoney = shareMoney;
	}
	
}