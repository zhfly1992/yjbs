package com.fx.commons.utils.other.towcode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.common.BitMatrix;

/**
 * 生成二维码工具类
 */
public class MatrixToImageWriter {
	private static final int BLACK = 0xFF000000;//颜色 黑色（二维码主色）
	private static final int WHITE = 0xFFFFFFFF;//颜色 白色（二维码背景色）

	private MatrixToImageWriter() {}

	/**
	 * 根据二维码获取图片对象
	 * @param matrix 二维码
	 * @return image 图片对象
	 */
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		// 开始利用二维码数据创建Bitmap图片，分别设为黑白两色 
		int width = matrix.getWidth(), height = matrix.getHeight();//获取二维码的宽、高
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	/**
	 * 将二维码写入指定路径的文件中
	 * @param matrix 二维码
	 * @param format 图片格式
	 * @param file 文件（随便指定一个文件路径）
	 */
	public static void writeToFile(BitMatrix matrix, String format, File file) 
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format "+ format + " to " + file);
		}
	}
	/**
	 * 将二维码写入流，输出到页面
	 * @param matrix 二维码
	 * @param format 图片格式
	 * @param stream 输出流
	 */
	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) 
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format "+ format);
		}
	}
	/** 
     * 二维码绘制logo 
     * @param twodimensioncodeImg 二维码图片文件 
     * @param logoImg logo图片文件 
     * */  
	public static BufferedImage encodeImgLogo(BitMatrix matrix, File logoImg) {
		BufferedImage twodimensioncode = null;
		try {
			if (!logoImg.isFile()) {
				System.out.println("输入非图片");
				return null;
			}
			// 读取二维码图片
			twodimensioncode = toBufferedImage(matrix);
			// 获取画笔
			Graphics2D g = twodimensioncode.createGraphics();
			// 读取logo图片
			BufferedImage logo = ImageIO.read(logoImg);
			// 设置二维码大小，太大，会覆盖二维码，此处20%
			int logoWidth = logo.getWidth(null) > twodimensioncode.getWidth() * 2 / 10 ? (twodimensioncode
					.getWidth() * 2 / 10) : logo.getWidth(null);
			int logoHeight = logo.getHeight(null) > twodimensioncode
					.getHeight() * 2 / 10 ? (twodimensioncode.getHeight() * 2 / 10)
					: logo.getHeight(null);
			// 设置logo图片放置位置
			// 中心
			int x = (twodimensioncode.getWidth() - logoWidth) / 2;
			int y = (twodimensioncode.getHeight() - logoHeight) / 2;
			// 右下角，15为调整值
			// int x = twodimensioncode.getWidth() - logoWidth-15;
			// int y = twodimensioncode.getHeight() - logoHeight-15;
			// 开始合并绘制图片
			g.drawImage(logo, x, y, logoWidth, logoHeight, null);
			g.drawRoundRect(x, y, logoWidth, logoHeight, 15, 15);
			// logo边框大小
			g.setStroke(new BasicStroke(2));
			// logo边框颜色
			g.setColor(Color.WHITE);
			g.drawRect(x, y, logoWidth, logoHeight);
			g.dispose();
			logo.flush();
			twodimensioncode.flush();
		} catch (Exception e) {
			System.out.println("二维码绘制logo失败");
		}
		return twodimensioncode;
	}
	/**
	 * 将带logo的二维码写入指定路径的文件中
	 * @param matrix 二维码
	 * @param format 图片格式
	 * @param file 文件（随便指定一个文件路径）
	 */
	public static void writeToFileAndLogo(BitMatrix matrix, String format, File file, File logoImg) 
			throws IOException {
		BufferedImage image = encodeImgLogo(matrix, logoImg);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format "+ format + " to " + file);
		}
	}
	/**
	 * 将带logo的二维码写入流，输出到页面
	 * @param matrix 二维码
	 * @param format 图片格式
	 * @param stream 输出流
	 */
	public static void writeToStreamAndLogo(BitMatrix matrix, String format, OutputStream stream, File logoImg) 
			throws IOException {
		BufferedImage image = encodeImgLogo(matrix, logoImg);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format "+ format);
		}
	}
}
