package com.jimmy.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;

/**
 * PreferencesUtils, easy to get or put data
 * <ul>
 * <strong>Preference Name</strong>
 * <li>you can change preference name by {@link #PREFERENCE_NAME}</li>
 * </ul>
 * <ul>
 * <strong>Put Value</strong>
 * <li>put string {@link #putString(Context, String, String)}</li>
 * <li>put int {@link #putInt(Context, String, int)}</li>
 * <li>put long {@link #putLong(Context, String, long)}</li>
 * <li>put float {@link #putFloat(Context, String, float)}</li>
 * <li>put boolean {@link #putBoolean(Context, String, boolean)}</li>
 * </ul>
 * <ul>
 * <strong>Get Value</strong>
 * <li>get string {@link #getString(Context, String)},
 * {@link #getString(Context, String, String)}</li>
 * <li>get int {@link #getInt(Context, String)},
 * {@link #getInt(Context, String, int)}</li>
 * <li>get long {@link #getLong(Context, String)},
 * {@link #getLong(Context, String, long)}</li>
 * <li>get float {@link #getFloat(Context, String)},
 * {@link #getFloat(Context, String, float)}</li>
 * <li>get boolean {@link #getBoolean(Context, String)},
 * {@link #getBoolean(Context, String, boolean)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-3-6
 */
public class PreferencesUtils {

	public static String PREFERENCE_NAME = "ylf";
	/**
	 * SharedPreferences 名字
	 */
	public static final String SHAREDP_NAME = "College";

	/**
	 * put string preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putString(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * get string preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or null. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a string
	 * @see #getString(Context, String, String)
	 */
	public static String getString(Context context, String key) {
		return getString(context, key, null);
	}

	/**
	 * get string preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a string
	 */
	public static String getString(Context context, String key,
			String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getString(key, defaultValue);
	}

	/**
	 * put int preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putInt(Context context, String key, int value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static void clearAllData(Context context){
		SharedPreferences settings1 = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor1 = settings1.edit();
		editor1.clear();
		editor1.commit();
		SharedPreferences settings2 = context.getSharedPreferences(SHAREDP_NAME, Context.MODE_PRIVATE);
		Editor editor2 = settings2.edit();
		editor2.clear();
		editor2.commit();
	}
	/**
	 * get int preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a int
	 * @see #getInt(Context, String, int)
	 */
	public static int getInt(Context context, String key) {
		return getInt(context, key, -1);
	}

	/**
	 * get int preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a int
	 */
	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}

	/**
	 * put long preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putLong(Context context, String key, long value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	/**
	 * get long preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a long
	 * @see #getLong(Context, String, long)
	 */
	public static long getLong(Context context, String key) {
		return getLong(context, key, -1);
	}

	/**
	 * get long preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a long
	 */
	public static long getLong(Context context, String key, long defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * put float preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putFloat(Context context, String key, float value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	/**
	 * get float preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a float
	 * @see #getFloat(Context, String, float)
	 */
	public static float getFloat(Context context, String key) {
		return getFloat(context, key, -1);
	}

	/**
	 * get float preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a float
	 */
	public static float getFloat(Context context, String key, float defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getFloat(key, defaultValue);
	}

	/**
	 * put boolean preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putBoolean(Context context, String key, boolean value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 * get boolean preferences, default is false
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or false. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean
	 * @see #getBoolean(Context, String, boolean)
	 */
	public static boolean getBoolean(Context context, String key) {
		return getBoolean(context, key, false);
	}

	/**
	 * get boolean preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getBoolean(key, defaultValue);
	}

	/**
	 * 是否关注圈子索引
	 * 
	 * @param context
	 */
	public static void setIsPayAttentionCircle(Context context, int position) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putInt("is_pay_attention_circle_id", position);
		ed.commit();
	}

	public static String getIsPayAttentionCircle(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("is_pay_attention_circle_id", null);
	}

	/**
	 * 存入搜索历史
	 * 
	 * @param context
	 * @param searchKey
	 */
	public static void setSearchHistory(Context context, String searchKey) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		String string = sp.getString("search_history", null);
		String[] oldKeyArrays = null;
		if (null != string && !"".equals(string)) {
			oldKeyArrays = string.split(",");
		}
		ArrayList<String> keyList = new ArrayList<String>();
		if (oldKeyArrays != null) {
			for (int i = 0; i < oldKeyArrays.length; i++) {
				if (searchKey.equals(oldKeyArrays[i])) {
					return;
				}
			}
			if (oldKeyArrays.length <= 4) {
				for (int i = 0; i < oldKeyArrays.length; i++) {
					keyList.add(oldKeyArrays[i]);
				}
			} else {
				for (int i = 1; i < oldKeyArrays.length; i++) {
					keyList.add(oldKeyArrays[i]);
				}
			}
		}
		keyList.add(searchKey);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keyList.size(); i++) {
			sb.append(keyList.get(i) + ",");
		}

		Editor ed = sp.edit();
		ed.putString("search_history", sb.toString());
		ed.commit();
	}

	/**
	 * 获取搜索历史
	 */
	public static ArrayList<String> getSearchHistory(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		ArrayList<String> keyList = new ArrayList<String>();
		String string = sp.getString("search_history", null);
		if (null != string && !"".equals(string)) {
			String[] keyArrays = string.split(",");
			for (int i = keyArrays.length - 1; i >= 0; i--) {
				keyList.add(keyArrays[i]);
			}
		}

		return keyList;
	}

	/**
	 * 获取搜索历史
	 */
	public static void clearSearchHistory(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.remove("search_history");
		ed.commit();
	}

	/**
	 * 是否首次打开app
	 * 
	 * @param context
	 * @param isFirst
	 *            0 首次 1非首次
	 */
	public static void setIsFirst(Context context, int isFirst) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putInt("isFirst", isFirst);
		ed.commit();
	}

	public static int getIsFirst(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt("isFirst", 0);
	}

	/**
	 * 
	 * @param context
	 * @param isSetPass
	 *            0-已设置，1-未设置
	 */
	public static void setIsSetPass(Context context, int isSetPass) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putInt("isSetPass", isSetPass);
		ed.commit();
	}

	/**
	 * 
	 * @param context
	 * @return 0-已设置，1-未设置
	 */
	public static int getIsSetPass(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SHAREDP_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt("isSetPass", 0);
	}
}
