package com.fx.commons.utils.other;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.commons.lang3.StringUtils;

public class EncodeUtils{
	
	public static void main(String[] args) {
		String cn = "成都市";
		System.out.println(getEncode(cn));
		
		
	}
	
	/**
	 * 中信接口返回状态代码
	 */
	public final static String [] returnStatus = new String[]{"AAAAAAA","AAAAAAB","AAAAAAC","AAAAAAD","AAAAAAE","AAAAAAF"};
	
	/**这里可以提供更多地编码格式,另外由于部分编码格式是一致的所以会返回 第一个匹配的编码格式 GBK 和 GB2312*/
    public static final String[] encodes = new String[] { "UTF-8", "GBK", "GB2312", "ISO-8859-1", "ISO-8859-2" };

    /**
     * 获取字符串编码格式
     * @param str
     * @return
     */
    public static String getEncode(String str) {
        byte[] data = str.getBytes();
        byte[] b = null;
        a:for (int i = 0; i < encodes.length; i++) {
            try {
                b = str.getBytes(encodes[i]);
                if (b.length!=data.length)
                    continue;
                for (int j = 0; j < b.length; j++) {
                    if (b[j] != data[j]) {
                        continue a;
                    }
                }
                return encodes[i];
            } catch (UnsupportedEncodingException e) {
                continue;
            }
        }
        return null;
    }

    /**
     * 将字符串转换成指定编码格式
     * @param str
     * @param encode
     * @return
     */
    public static String transcoding(String str, String encode) {
        String df = "ISO-8859-1";
        try {
            String en = getEncode(str);
            if (en == null)
                en = df;
            return new String(str.getBytes(en), encode);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    
    /**
     * 将中文GBK转换成UTF-8
     * @param gbkStr 需要转换的字符串
     * @return 转换后的UTF-8字符串
     */
    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {  
        int n = gbkStr.length();  
        byte[] utfBytes = new byte[3 * n];  
        int k = 0;  
        for (int i = 0; i < n; i++) {  
            int m = gbkStr.charAt(i);  
            if (m < 128 && m >= 0) {  
                utfBytes[k++] = (byte) m;  
                continue;  
            }  
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));  
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));  
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));  
        }  
        if (k < utfBytes.length) {  
            byte[] tmp = new byte[k];  
            System.arraycopy(utfBytes, 0, tmp, 0, k);  
            return tmp;  
        }  
        return utfBytes;  
    }
    
    
    
    public static String gbk2utf8(String gbk) {
        String l_temp = GBK2Unicode(gbk);
        l_temp = unicodeToUtf8(l_temp);
 
        return l_temp;
    }
    
    public static String GBK2Unicode(String str) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr1 = (char) str.charAt(i);
 
            if (!isNeedConvert(chr1)) {
                result.append(chr1);
                continue;
            }
 
            result.append("\\u" + Integer.toHexString((int) chr1));
        }
 
        return result.toString();
    }
    
    public static boolean isNeedConvert(char para) {
        return ((para & (0x00FF)) != para);
    }
    
    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
    
    
    
    
    
    
    
	
	/**
	 * 微信获取文件扩展名
	 */
	public static String getFileEndWith(String type){
		if ("image/jpeg".equals(type))
			type = ".jpg";
        else if ("audio/mpeg".equals(type))
        	type = ".mp3";
        else if ("audio/amr".equals(type))
        	type = ".amr";
        else if ("video/mp4".equals(type))
        	type = ".mp4";
        else if ("video/mpeg4".equals(type))
        	type = ".mp4";
		return type;
	}
	/**
	 * GB2312转化为utf-8编码
	 * @param str 转换的字符串
	 * @return 编码后的字符串
	 */
	public static String gb2utf(String str){
		String result = StringUtils.stripToEmpty(str);
		try{
			result = new String(result.getBytes("GB2312"), "UTF-8");
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * iso8859-1转化为utf-8编码
	 * @param str 转换的字符串
	 * @return 编码后的字符串
	 */
	public static String iso2utf(String str){
		String result = StringUtils.stripToEmpty(str);
		try{
			result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * utf-8转化为iso8859-1编码
	 * @param str 转换的字符串
	 * @return 编码后的字符串
	 */
	public static String utf2iso(String str){
		String result = StringUtils.stripToEmpty(str);
		try{
			result = new String(result.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 页面提交转utf-8
	 * @param str
	 * @return
	 */
	public static String decode(String str){
		try {
			if(StringUtils.isNotEmpty(str)){
				str=URLDecoder.decode(str , "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
}
