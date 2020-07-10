package com.fx.commons.utils.other;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fx.commons.utils.tools.U;

/**
 * 微信工具类
 */
public class WeiXinUtil {
	/** 日志记录 */
	public static final Logger log = LogManager.getLogger(WeiXinUtil.class.getName());
	
	//微信支付回调地址
	public static String REDIRECT_PAY_URI = "http://51ekc.cn/rentalcar/wallet/payOpenId";
	//获取access_token
	public static String GET_ACCESS_TOKEN="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//获取code网页授权
	public static String GET_CODE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&" +
			"redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	//获取access_token，获取openId
	public static String GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
			"appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//生成带参数的二维码
	public static String CREATE_QCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
	//获取二维码
	public static String GETQCODE_URL = "http://cz.51ygt.com/Api/Weixin/makeWeichatqrcode/wid/65/recid/RECID.html";
	//获取jsApi票据
	public static String GETJSAPI_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	//下载图片
	public static String DOWNlOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	
	  /**
	   * 获取媒体文件
	   * @param accessToken 接口访问凭证
	   * @param media_id 媒体文件id
	   * @param savePath 文件在服务器上的存储路径
	 * @throws Exception 
	   **/
	  public static String downloadMedia(String accessToken, String mediaId, String savePath,String fRelName) throws Exception {
		  String filePath = null;
		  // 初始化请求地址
		  DOWNlOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	      DOWNlOAD_URL = DOWNlOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
	      URL url = new URL(DOWNlOAD_URL);
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setDoInput(true);
	      conn.setRequestMethod("GET");

	      if (!savePath.endsWith("/")) {
	        savePath += "/";
	      }
	      // 根据内容类型获取扩展名
	      String fileExt = MyStringUtils.getFileEndWith(conn.getHeaderField("Content-Type"));
	      // 将mediaId作为文件显示名
	      filePath = mediaId +","+ fileExt;

	      BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
	      FileOutputStream fos = new FileOutputStream(new File(savePath + fRelName + fileExt));
	      byte[] buf = new byte[8096];
	      int size = 0;
	      while ((size = bis.read(buf)) != -1)
	        fos.write(buf, 0, size);
	      fos.close();
	      bis.close();

	      conn.disconnect();
	      //String info = String.format("下载媒体文件成功，filePath=" + savePath + fRelName + fileExt);
	      //System.out.println(info);
	    return filePath;
	  }
	  
	/**
	 * 获取access_token
	 * @param appid 商户唯一凭证
	 * @param secret 唯一凭证密钥
	 */
	public static JsonNode getAccessToken(String appid,String secret){
		GET_ACCESS_TOKEN="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		GET_ACCESS_TOKEN=GET_ACCESS_TOKEN.replace("APPID", urlEnodeUTF8(appid));
		GET_ACCESS_TOKEN=GET_ACCESS_TOKEN.replace("APPSECRET", urlEnodeUTF8(secret));
		JsonNode node = httpRequest(GET_ACCESS_TOKEN);
		
		if(!node.has("access_token")){// 不存在access_token
        	String code = U.Cq(node, "errcode");
            if(!"0".equals(code)){
            	U.log(log, "============请求微信access_token============");
            	ObjectNode on = (ObjectNode)node;
            	on.put("access_token", "");// 出现错误，默认设置access_token为字符串空
            	node = (JsonNode)on;
            	
            	if("40001".equals(code)){
            		U.log(log, "AppSecret错误或者AppSecret不属于这个公众号，请开发者确认AppSecret的正确性");
                }else if("40002".equals(code)){
                	U.log(log, "请确保grant_type字段值为client_credential");
                }else if("40164".equals(code)){
                	U.log(log, "调用接口的IP地址不在白名单中，请在接口IP白名单中进行设置");
                }else{
                	U.log(log, "系统繁忙，此时请开发者稍候再试");
                }
            }
        }
		
        return node;
	}
	
	/**
	 * 获取网页授权
	 */
	public static String getCodeUrl(String state,String AppId){
		GET_CODE_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&" +
				"redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
		GET_CODE_URL=GET_CODE_URL.replace("APPID", AppId);
		GET_CODE_URL=GET_CODE_URL.replace("REDIRECT_URI", urlEnodeUTF8(REDIRECT_PAY_URI));
		GET_CODE_URL=GET_CODE_URL.replace("STATE", state);
		return GET_CODE_URL;
	}
	
    /**
    * 获得openId,ACCESS_TOKEN
    * @Title: getAccess_token
    * @Description: 获得ACCESS_TOKEN
    * @param @return 设定文件
    * @return String 返回类型
    * @throws
    */
   public static JsonNode getOpenId(String code,String AppId,String secret) throws Exception {
	   JsonNode node = null;
		try {
			GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
						"appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			   GET_TOKEN_URL=GET_TOKEN_URL.replace("APPID", urlEnodeUTF8(AppId));
			   GET_TOKEN_URL=GET_TOKEN_URL.replace("SECRET", urlEnodeUTF8(secret));
			   GET_TOKEN_URL=GET_TOKEN_URL.replace("CODE", code);
			   
			   node = httpRequest(GET_TOKEN_URL);
			   U.log(log, "请求微信获取openId："+node.toString());
		} catch (Exception e) {
			U.log(log, "异常：请求微信oppenid:"+node.toString(), e);
			e.printStackTrace();
		}
        return node;
   }
   /**
    * 根据access_token和oppenid拉取用户信息
    * @param access_token 
    * @param openid
    */
   public static JsonNode getUserInfo(String access_token, String openid) {
	   GET_TOKEN_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	   GET_TOKEN_URL=GET_TOKEN_URL.replace("ACCESS_TOKEN", urlEnodeUTF8(access_token));
	   GET_TOKEN_URL=GET_TOKEN_URL.replace("OPENID", urlEnodeUTF8(openid));
	   
	   JsonNode node = httpRequest(GET_TOKEN_URL);
	   U.log(log, "拉取的微信用户信息:"+node.toString());
       return node;
   }
   //进行转码
   public static String urlEnodeUTF8(String str){
       try {
    	   str = URLEncoder.encode(str,"UTF-8");
       } catch (Exception e) {
           e.printStackTrace();
       }
       return str;
   }
   /**
    * 获取二维码地址
    * @param uid
    * @return
    */
   public static JsonNode getQCode(String uid) {
	   GETQCODE_URL = "http://cz.51ygt.com/Api/Weixin/makeWeichatqrcode/wid/65/recid/RECID.html";
	   GETQCODE_URL=GETQCODE_URL.replace("RECID", uid);
	   JsonNode demoJson = httpRequest(GETQCODE_URL);
       return demoJson;
   }
   /**
    * 获取jsAPI授权信息
    * @param uid
    * @return
    */
   public static JsonNode getJSAPI(String accessToken) {
	   GETJSAPI_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	   GETJSAPI_URL=GETJSAPI_URL.replace("ACCESS_TOKEN", accessToken);
	   JsonNode node = httpRequest(GETJSAPI_URL);
       return node;
   }
   /**
    * 公用请求
    */
   public static JsonNode httpRequest(String url) {
	   ObjectMapper mapper = new ObjectMapper();
	   JsonNode node = null;
       try {
           URL urlGet = new URL(url);
           HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
           http.setRequestMethod("GET"); // 必须是get方式请求
           http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
           http.setDoOutput(true);
           http.setDoInput(true);
           System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
           System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
           http.connect();
           InputStream is = http.getInputStream();
           int size = is.available();
           byte[] jsonBytes = new byte[size];
           is.read(jsonBytes);
           String message = new String(jsonBytes, "UTF-8");
           node = mapper.readTree(message);
           is.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return node;
   }
   //生成带参数的二维码
   public JsonNode qcode(String param,String token){
	   CREATE_QCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
	   CREATE_QCODE=CREATE_QCODE.replace("TOKEN", token);
	   ObjectMapper mapper = new ObjectMapper();
	   JsonNode demoJson = null;
       PrintWriter out = null;
       try {
           URL realUrl = new URL(CREATE_QCODE);
           // 打开和URL之间的连接
           URLConnection conn = realUrl.openConnection();
           // 设置通用的请求属性
           conn.setRequestProperty("accept", "*/*");
           conn.setRequestProperty("connection", "Keep-Alive");
           conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
           // 发送POST请求必须设置如下两行
           conn.setDoOutput(true);
           conn.setDoInput(true);
           // 获取URLConnection对象对应的输出流
           out = new PrintWriter(conn.getOutputStream());
           // 发送请求参数
           out.print(param);
           // flush输出流的缓冲
           out.flush();
           //输入流来读取URL的响应
           InputStream is = conn.getInputStream();
           int size = is.available();
           byte[] jsonBytes = new byte[size];
           is.read(jsonBytes);
           String message = new String(jsonBytes, "UTF-8");
           demoJson = mapper.readTree(message);
       } catch (Exception e) {
           System.out.println("发送 POST 请求出现异常！"+e);
           e.printStackTrace();
       }finally{//使用finally块来关闭输出流、输入流
           try{
               if(out!=null){
                   out.close();
               }
           }catch(Exception ex){
               ex.printStackTrace();
           }
       }
       return demoJson;
   }
}