package com.mcms.commonlib.utils;


public class M9Secure {

	public native static byte[] m8Encode(byte[] src);

	public native static byte[] m8Decode(byte[] src);

	public native static byte[] m9Encode(byte[] src);

	public native static byte[] m9Decode(byte[] src);


	static {
		System.loadLibrary("m9secure");
	}
}
