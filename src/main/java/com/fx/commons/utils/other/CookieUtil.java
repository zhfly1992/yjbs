package com.fx.commons.utils.other;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class CookieUtil {
	/**
     * 获取cookie中的uuid
     * 注意二、从客户端读取Cookie时，包括maxAge在内的其他属性都是不可读的，也不会被提交。浏览器提交Cookie时只会提交name与value属性。maxAge属性只被浏览器用来判断Cookie是否过期
     * @param request
     * @param response
     */
    public static String LoginUUID(HttpServletRequest request, HttpServletResponse response, String uuid){
        Cookie[] cookies = request.getCookies();//获取一个cookie数组
        if(cookies != null){
        	for (Cookie ck : cookies) {     
                if (ck.getName().equals("uuid")) { //原来存入有uuid
            		uuid = ck.getValue();
                }     
            } 
        }
        
        if(StringUtils.isEmpty(uuid)){ //cookie中不存在uuid
		    uuid = UUID.randomUUID().toString();
			addCookie(response, "uuid", uuid);
		}
        
        return uuid;
    }
    /**
     * 判断cookie中是否存在uuid
     * 注意二、从客户端读取Cookie时，包括maxAge在内的其他属性都是不可读的，也不会被提交。浏览器提交Cookie时只会提交name与value属性。maxAge属性只被浏览器用来判断Cookie是否过期
     * @param request
     */
    public static String getUUID(HttpServletRequest request, String uuid){
        Cookie[] cookies = request.getCookies();//获取一个cookie数组
        if(cookies != null){
        	for (Cookie ck : cookies) {     
                if (ck.getName().equals("uuid")) { //原来存入有uuid
            		uuid = ck.getValue();
                }     
            } 
        }
        return uuid;
    }
    /**
     * 根据键获取cookie中的值
     * @param request
     * @param response
     */
    public static String getCookie(HttpServletRequest request,HttpServletResponse response,String name,String value){
        Cookie[] cookies = request.getCookies();//获取一个cookie数组
        if(cookies!=null){
        	for (Cookie ck : cookies) {     
                if (ck.getName().equals(name)) { //原来存有验证码
                	value=ck.getValue();
                }     
            } 
        }
        return value;
    }
    /**
     * 添加cookie
     * @param response
     * @param name
     * @param value
     */
    public static void addCookie(HttpServletResponse response,String name,String value){
        Cookie cookie = new Cookie(name.trim(), value.trim());
        cookie.setMaxAge(-1);// 关闭浏览器失效
        cookie.setPath("/");
        //cookie.setDomain(QC.COOKIE_DOMAIN);
        //System.out.println("已添加"+name+"-->"+value);
        response.addCookie(cookie);
       // response.setHeader("Access-Control-Allow-Credentials","true"); //是否支持cookie跨域
    }
    /**
     * 修改cookie
     * @param request
     * @param response
     * @param name
     * @param value
     * 注意一、修改、删除Cookie时，新建的Cookie除value、maxAge之外的所有属性，例如name、path、domain等，都要与原Cookie完全一样。
     * 否则，浏览器将视为两个不同的Cookie不予覆盖，导致修改、删除失败。
     */
    public static void editCookie(HttpServletRequest request,HttpServletResponse response,String name,String value){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
        	 for(Cookie cookie : cookies){
                 if(cookie.getName().equals(name)){
                     //System.out.println("原值为:"+cookie.getValue());
                     cookie.setValue(value);
                     cookie.setPath("/");
                     cookie.setMaxAge(-1);// 关闭浏览器失效
                     //System.out.println("被修改的cookie名字为:"+cookie.getName()+",新值为:"+cookie.getValue());
                     response.addCookie(cookie);
                     break;
                 }
             }
        }
    }
    /**
     * 删除cookie
     * @param request
     * @param response
     * @param name
     */
    public static void delCookie(HttpServletRequest request,HttpServletResponse response,String name){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
        	 for(Cookie cookie : cookies){
                 if(cookie.getName().equals(name)){
                	 //System.out.println("被删除的cookie:"+cookie.getName()+"-->"+cookie.getValue());
                     cookie.setValue(null);
                     cookie.setMaxAge(0);// 立即销毁cookie
                     cookie.setPath("/");
                     response.addCookie(cookie);
                     break;
                 }
             }
        }
    }
}
