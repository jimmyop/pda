package com.mcms.commonlib.request;

import java.util.HashMap;

/**
 * 网络请求参数
 * Created by jimmy on 2017/7/27.
 *
 */
public class HttpParam extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;
	
	public static final String PARAM_PAGE_INDEX = "pageIndex";
	public static final String PARAM_PAGE_SIZE = "pageSize";
	
	public static HttpParam getBaseHttpParam(){
		return new HttpParam();
	}
	
	public static HttpParam getPagingParam(int pageIndex, int pageSize){
		HttpParam params = getBaseHttpParam();
		params.putInt(PARAM_PAGE_INDEX, pageIndex);
		params.putInt(PARAM_PAGE_SIZE, pageSize);
		return params;
	}
	
	public void putInt(String key, int value){
		put(key, String.valueOf(value));
	}
	
	public void putLong(String key, long value){
		put(key, String.valueOf(value));
	}

	public void putDouble(String key, double value){
		put(key, String.valueOf(value));
	}
}
