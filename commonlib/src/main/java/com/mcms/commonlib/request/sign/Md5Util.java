package com.mcms.commonlib.request.sign;

import java.security.MessageDigest;

/**
 * MD5工具类
 * @author 丁伟
 */
public class Md5Util {
	public static String md5(String value) {
		try {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			byte[] strTemp = value.getBytes("UTF-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char chars[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				chars[k++] = hexDigits[byte0 >>> 4 & 0xf];
				chars[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(chars);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
