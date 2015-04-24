package com.coolweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * ==============================================
 *	------------作者：连记伟---------------
 * 	------------技术支持：起点工作室----------
 *	------------日期：2015-3-19------------
 *通过此工具类得到存储的值
 *===============================================
 */
public class SharedPreferencesUtil {

	private static SharedPreferences getSharedPreferences(Context context){
		return context.getSharedPreferences("config",Context.MODE_PRIVATE);
	}
	
	
	public static void putValue(Context context,String key,String value){
		getSharedPreferences(context).edit().putString(key, value).commit();
		
	};
	/**
	 * 
	 * @param context 上下文
	 * @param key 名字
	 * @param value 值
	 */
	public static void putValue(Context context,String key,boolean value){
		getSharedPreferences(context).edit().putBoolean(key, value).commit();
		
	};
	
	
	
	public static String getValue(Context context,String key,String defValue){
		return getSharedPreferences(context).getString(key, defValue);
	};

	

	public static boolean getValue(Context context,String key,boolean defValue){
		return getSharedPreferences(context).getBoolean(key, defValue);
	};
	

}
