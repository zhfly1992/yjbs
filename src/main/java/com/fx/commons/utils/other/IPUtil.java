package com.fx.commons.utils.other;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户访问的ip地址
 */
public class IPUtil {

    private static final String UNKNOWN = "unknown";

    /**
     * 通过request.getRemoteAddr()可以获取到客户端的IP地址
     * 但是若使用Nginx等反向代理软件就不能用其获取客户端IP了，
     * 比如将http://37.12.456/index反向代理为http://tycoding.cn/index，那么通过getRemoteAddr()获取的是代理服务器的地址：127.0.0.1、192.168.1.110、unknown
     * 但反向代理时会在HTTP Header中加入x-forwarded-for信息，用以跟踪原客户端IP地址
     * <p>
     * 如果通过多级反向代理，x-forwarded-for的值并不止一个，而是一串IP值，那么x-forwarded-for中第一个非unknown的字符串是IP
     * @param request
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
    
    /**
     * 获取-本地服务器ip
     */
    public static String getLocalIp() {
    	String ipAddrStr = "";
    	try {
    		InetAddress addr = InetAddress.getLocalHost();
    		
    		byte[] ipAddr = addr.getAddress();      
            for (int i = 0; i < ipAddr.length; i++) {     
                if (i > 0) {     
                    ipAddrStr += ".";     
                }     
                ipAddrStr += ipAddr[i] & 0xFF;     
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return ipAddrStr;
    } 
    
    public static void main(String[] args) {
		System.out.println(getLocalIp());
	}
    
}
