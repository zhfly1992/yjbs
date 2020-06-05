package com.fx.commons.utils.wx.util;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.UUID;

public class Sign {
   /*public static void main(String[] args) {
        String jsapi_ticket = "sM4AOVdWfPE4DxkXGEs8VGOeMMG-zobFQJK4s3jU9CNI-uOGdA5m1_h4do1hiwzFQnOfyYBHPttMsmj9yGyuzA";

        // 注意 URL 一定要动态获取，不能 hardcode
        String url = "http://localhost:8080/rentalcar/wechat/wxAuthority";
        Map<String, Object> ret = sign(jsapi_ticket, url);
        for (Map.Entry entry : ret.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
       }
    };*/

    public static HashMap<String, Object> sign(String jsapi_ticket, String url) {
    	HashMap<String, Object> ret = new HashMap<String, Object>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String content;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        content = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        try{
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(content.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }
    /**
     * sha1
     * @param hash
     * @return
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    /**
	 * 获取随机字符串
	 */
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
    /**
	 * 获取时间戳
	 */
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
