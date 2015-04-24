package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	public static String CREATE_CITY="create table city(" +
			"id integer primary key autoincrement," +//id 主键
			"cityid text ," +//城市气象编号
			"citynm text," +//城市名
			"cityno text," +//城市拼音
			"weaid text)";//城市的id
	
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase dbs) {
		//创建一个城市表，可以存取全国所有的城市
		System.out.println("你正在创建表");
		dbs.execSQL(CREATE_CITY);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
