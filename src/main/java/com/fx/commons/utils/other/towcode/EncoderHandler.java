package com.fx.commons.utils.other.towcode;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class EncoderHandler {
	/**
	 * 二维码的生成
	 * @param text 二维码内容
	 * @param response
	 */
    public static void createCode(String text, HttpServletResponse response){  
        int width = 300, height = 300; //设置二维码图片的大小单位像素二维码的大小不是这个 
        // 二维码的图片格式   
        String format = "png";  
        //设置二维码的参数   
        HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();  
        // 内容所使用编码   
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
        try {  
        	BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);  
            // 生成二维码(无logo)   
            MatrixToImageWriter.writeToStream(bitMatrix, format, response.getOutputStream()); 
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
}

