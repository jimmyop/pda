/*
 * ****************************************************************************
 * Copyright (C) 2005-2013 UCWEB Corporation. All Rights Reserved File : NetworkUtils.java
 * Description : Creation : 2013-9-18 Author : mabin@ucweb.com History : Creation, 2013-9-18, mabin,
 * Create the file*****************************************************************************
 */
package com.mcms.commonlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

public final class NetworkUtils {

	/** 网络类型字段，表示蜂窝数据模式 */
	public static final int NET_CONN_TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
	/** 网络类型字段，表示wifi数据模式 */
	public static final int NET_CONN_TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
	/** 网络类型字段，表示无法识别 */
	public static final int NET_CONN_TYPE_UNKNOW = -1;

	public static final String NETWORK_TYPE_2G = "2G";
	public static final String NETWORK_TYPE_3G = "3G";
	public static final String NETWORK_TYPE_4G = "4G";
	public static final String NETWORK_TYPE_WIFI = "wifi";
	public static final String NETWORK_TYPE_UNKNOW = "unknow";

	/***
	 * 网络是否连通
	 * 
	 * @param context
	 * @return
	 */
	public static boolean networkIsAvailable(Context context) {
//		ConnectivityManager mConnectivityManager =
//				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//		if (mNetworkInfo != null) {
//			if (mNetworkInfo.getState() == State.CONNECTED)
//				return true;
//		}
//		return false;
		return true;
	}

	/***
	 *  本地的网络是否连通
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context){
		if(context == null) return false;
		// 判断是否具有可以用于通信渠道
		boolean mobileConnection = isMobileConnection(context);
		boolean wifiConnection = isWIFIConnection(context);
		return !(mobileConnection == false && wifiConnection == false);
	}
	/**
	 * 判断手机接入点（APN）是否处于可以使用的状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnection(Context context) {
		if(context == null) return false;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager != null){
			NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (networkInfo != null && networkInfo.isConnected()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断当前wifi是否是处于可以使用状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWIFIConnection(Context context) {
		if(context == null) return false;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manager != null){
			NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null && networkInfo.isConnected()) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/***
	 * WLAN网络是否连通
	 * 
	 * @param context
	 * @return
	 */
	public static boolean networkIsWifi(Context context) {
		ConnectivityManager mConnectivityManager =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWiFiNetworkInfo =
				mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWiFiNetworkInfo != null) {
			return mWiFiNetworkInfo.getState() == State.CONNECTED;
		}
		return false;
	}

	/***
	 * 移动数据网络是否连通
	 * 
	 * @param context
	 * @return
	 */
	public static boolean networkIsMobile(Context context) {
		ConnectivityManager mConnectivityManager =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mMobileNetworkInfo =
				mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mMobileNetworkInfo != null) {
			return mMobileNetworkInfo.getState() == State.CONNECTED;
		}
		return false;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectivityType(Context context) {
		ConnectivityManager connManager =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager == null) {
			return NET_CONN_TYPE_UNKNOW;
		}
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info == null) {
			return NET_CONN_TYPE_UNKNOW;
		}
		return info.getType();
	}

	/**
	 * 判断网络连接类型，如wifi、2g/3g
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context) {
		int connectivityType = getConnectivityType(context);
		if (connectivityType == NET_CONN_TYPE_UNKNOW) {
			return NETWORK_TYPE_UNKNOW;
		}
		if (connectivityType == ConnectivityManager.TYPE_WIFI) {
			return NETWORK_TYPE_WIFI;
		}
		TelephonyManager tm =
				(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int type = tm.getNetworkType();
		switch (type) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return NETWORK_TYPE_2G;
			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return NETWORK_TYPE_3G;
			case TelephonyManager.NETWORK_TYPE_LTE:
				return NETWORK_TYPE_4G;
		}
		return NETWORK_TYPE_UNKNOW;
	}


}
