package com.fx.commons.utils.tools;

/**
 * 常量-类
 * @author yigou
 *
 */
public class QC {

	/**
	 * 正式-项目域名 http://51ekc.cn
	 */
	public static String PRO_URL = "http://51ekc.cn";
//	public static String PRO_URL = "http://192.168.1.4";
	/**
	 * 正式-项目数据库连接ip 121.37.161.108
	 */
	public final static String SQL_IP = "121.37.161.108";
	/**
	 * 正式-cookie保存domain .51ekc.cn
	 */
	public static String COOKIE_DOMAIN = ".51ekc.cn";
	/**
	 * 正式-网站备案号
	 */
	public static String RECORD_NUMBER = "蜀ICP备16026496号";
	
	/**
	 * 移动端-公众号-客户-访问项目名 /yjbs-cus
	 */
	public static String MOBILE_CUS_URL = "/yjbs-cus";
	
	/**
	 * 移动端-公众号-驾驶员-访问项目名 /yjbs-driver
	 */
	public static String MOBILE_DRIVER_URL = "/yjbs-driver";
	
	
	/**
	 * 测试-项目域名 http://cba360.cn
	 */
	public static String TEST_PRO_URL = "http://cba360.cn";
	/**
	 * 测试-项目数据库连接ip 120.78.78.179
	 */
	public final static String TEST_SQL_IP = "120.78.78.179";
	/**
	 * 测试-cookie保存domain .cba360.cn
	 */
	public static String TEST_COOKIE_DOMAIN = ".cba360.cn";
	/**
	 * 测试-网站备案号
	 */
	public static String TEST_RECORD_NUMBER = "蜀ICP备15020382号";
	
//	/** 管理员登录用户唯一uuid：buuid */
//	public static final String L_BACK_UUID = "uuid";
	/** 登录用户唯一uuid：uuid */
	public static final String UUID = "uuid";
	/** 登录用户 L_USER */
	public static final String L_USER = "L_USER";
	/** 登录管理员用户 L_BACK_USER */
	public static final String L_BACK_USER = "L_BACK_USER";
	/** 登录员工 L_STAFF */
	public static final String L_STAFF = "L_STAFF";
	/** 登录用户角色 L_ROLE */
	public static final String L_ROLE = "L_ROLE";
	/** 用户登录时间 L_TIME */
	public static final String L_TIME = "L_TIME";
	/**当前登录用户绑定的微信信息 */
	public static final String L_WX = "L_WX";
	/** 登录车队 L_TEAM */
	public static final String L_COMPANY = "L_COMPANY";
	/** 图片验证码 IMG_CODE */
	public static final String IMG_CODE = "IMG_CODE";
	/** 图片验证码长度 4 */
	public static final int IMG_CODE_LEN = 4;
	
	/** 短信验证码保存时间（分钟）10 */
	public static final int SMS_CODE_SAVE_TIME = 10;
	
    
    /** 用户默认[明文]登录密码 */
    public static final String DEF_LOGIN_PASS = "123456";
    /** 用户默认[明文]支付密码 */
    public static final String DEF_PAY_PASS = "654321";
	
	/** 加密方式 MD5 */
    public static final String ALGORITHNAME = "MD5";
    /** 加密次数 10 */
    public static final int HASHNUM = 10;
    
    /**
	 * 驾驶员-获得完团金额的比例：1 - 0.15
	 */
	public static double DRIVER_GET_MONEY_PROP = 1 - 0.15;
    
	/**
	 * 地点范围：2000米
	 */
	public static int POINT_RANGE = 2 * 1000;
	
	/**
	 * 过滤字段数组：fit_fields
	 */
	public static final String FIT_FIELDS = "fit_fields";
	
	/** 默认[单位编号-飞翔车队] */
    public static final String DEF_COMPANY_NUM = "8112010001";
    
    
    /** 默认飞翔公众号 appid */
	public static final String DEF_APPID = "wx642a5433d3df7f96";
	/** 默认飞翔公众号 secret */
	public static final String DEF_SECRET = "51c7b1d93203936896d540efe7c4091d";
	
	
	/** 高德地图服务接口key */
	public static final String AMAP_WEB_SER_AK = "72aa0453adf43e4165ea3dfcaffda19d";
	
	/** 高德地图服务接口key */
	public static final String AMAP_WEB_JS_AK = "533e81b8ae97b2caae38b47e7fb001d9";
	
	/** 航班查询 api密钥key */
	public static String FLIGHT_SEARCH_API_KEY = "32f3ab742120589f83fee10f6d634160";
	
	/** 地图web api密钥key */
	public static String MAP_WEB_API_KEY = "72aa0453adf43e4165ea3dfcaffda19d";
	
	/**
	 * 中信接口返回状态代码
	 */
	public final static String [] returnStatus = new String[]{"AAAAAAA", "AAAAAAB", "AAAAAAC", "AAAAAAD", "AAAAAAE", "AAAAAAF"};
	
	
	/**
	 * 微信-发送模板消息url https://api.weixin.qq.com/cgi-bin/message/template/send 
	 */
	public static final String SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send"; 
	
	
	/**
	 * 请求来源不存在错误消息：请求来源不存在
	 */
	public static String ERROR_REQ_SRC_MSG = "请求来源不存在";
	
	/**
	 * 发生异常出错提示消息：抱歉，出错了，请刷新再试
	 */
	public static String ERRORS_MSG = "抱歉，出错了，请刷新再试";
	
	/**
	 * 错误页面地址 /page/error
	 */
	public static String ERROR_PAGE = "/page/error";
	
	
}
