package com.landicorp.core.utils;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QrcodeUtil {
	/**
	 * 功能描述：生成二维码
	 */
	public static boolean createQrcode(String content,String path,String fileName) {
		try {
		     MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		     
		    Map<EncodeHintType,String> hints = new HashMap<EncodeHintType,String>();
		    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		    BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
			File dir = new File(path);
			if(!dir.exists()){
				dir.mkdirs();
			}
		     File file1 = new File(path,fileName+".jpg");
		     MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		return true;
	}
}
