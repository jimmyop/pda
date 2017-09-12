package com.mcms.commonlib.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SdcardUtils {

	public static boolean isSdcardAvaliable() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * 获得系统中剩余的空间大小
	 * 
	 * @return
	 */
	public static long phone_storage_free() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long free_memory = stat.getAvailableBlocks() * stat.getBlockSize(); // return value is in
																			// bytes

		return free_memory;
	}

	/**
	 * 获得系统中已使用的空间大小
	 * 
	 * @return
	 */
	public static long phone_storage_used() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long free_memory = (stat.getBlockCount() - stat.getAvailableBlocks()) * stat.getBlockSize(); // return
																										// value
																										// is
																										// in
																										// bytes

		return free_memory;
	}

	/**
	 * 获得系统中所有的空间大小
	 * 
	 * @return
	 */
	public static long phone_storage_total() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long free_memory = stat.getBlockCount() * stat.getBlockSize(); // return value is in bytes

		return free_memory;
	}

	/**
	 * 获得sdcard中剩余的空间大小
	 * 
	 * @return
	 */
	public static long sd_card_free() {

		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long free_memory = stat.getAvailableBlocks() * stat.getBlockSize(); // return value is in
																			// bytes

		return free_memory;
	}

	/**
	 * 获得sdcard中已使用的空间大小
	 * 
	 * @return
	 */
	public static long sd_card_used() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long free_memory = (stat.getBlockCount() - stat.getAvailableBlocks()) * stat.getBlockSize(); // return
																										// value
																										// is
																										// in
																										// bytes

		return free_memory;
	}

	/**
	 * 获得sdcard中总共的空间大小
	 * 
	 * @return
	 */
	public static long sd_card_total() {

		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long free_memory = stat.getBlockCount() * stat.getBlockSize(); // return value is in bytes

		return free_memory;
	}
}
