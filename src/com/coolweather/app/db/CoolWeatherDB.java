package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.NullCipher;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.Citys;
import com.coolweather.app.model.Citys.City;

public class CoolWeatherDB {

	public  static String DB_NAME="CoolWeather";
	public static int VERSION=1;
	public static CoolWeatherDB coolWeatherDB;
	public static SQLiteDatabase db;
	private  CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper helper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db=helper.getWritableDatabase();
	}
	//得到一个实例
	public static CoolWeatherDB getInstance(Context context){
		/*if(coolWeatherDB==null){//加锁
			synchronized (CoolWeatherDB.class) {
				if(coolWeatherDB==null){
					coolWeatherDB=new CoolWeatherDB(context);
				}
			}
		
		}
		*/
		synchronized (CoolWeatherDB.class) {
			if(coolWeatherDB==null){//加锁
				coolWeatherDB=new CoolWeatherDB(context);
			}
		};
		return coolWeatherDB;
	}
	//保存所有的城市信息，这点为了提高效率，查询一下批量添加
	public void saveAllCity(List<City> list){
		/*db.beginTransaction();*/
		for(City city:list){
			System.out.println("你的数据库请求是："+city.cityid);
			ContentValues values = new ContentValues();
			values.putNull("id");
			values.put("cityid",city.cityid);
			values.put("citynm",city.citynm);
			values.put("cityno",city.cityno);
			values.put("weaid",city.weaid);
			db.insert("city",null, values);
		};
		/*
		db.setTransactionSuccessful();
		db.endTransaction();
		*/
	}
	/**
	 * 便利得到所有的城市
	 * @return
	 */
	public List<City> loadAllCitys(){
		Cursor cursor = null;
		ArrayList<City> list = null;
		try {
			cursor = db.rawQuery("select * from city", null);
			list = new ArrayList<City>();
			City city;
			while (cursor.moveToNext()) {
				city=new City();
				city.id=cursor.getInt(0);
				city.cityid=cursor.getString(1);
				city.citynm=cursor.getString(2);
				city.cityno=cursor.getString(3);
				city.weaid=cursor.getString(4);
				list.add(city);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
			
		}
		return list;
		
	}
	
	
	public int getColumnSum(){
		Cursor cursor = db.rawQuery("select id from city", null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	};
	/**模糊查询很有可能有毛病，明天上网查询一下
	 * @param confidition 通过条件进行查询
 	 * @return 返回值是一个模糊查询的集合
	 */
	public List<City> loadByConfidition(String confidition){
		Cursor cursor = null ;
		List<City> list = null;
		try {
			cursor = db.rawQuery("select * from city where cityno =?",new String[]{confidition.trim()});
			list = new ArrayList<City>();
			City city;
			while (cursor.moveToNext()) {
				city=new City();
				city.id=cursor.getInt(0);
				city.cityid=cursor.getString(1);
				city.citynm=cursor.getString(2);
				city.cityno=cursor.getString(3);
				city.weaid=cursor.getString(4);
				list.add(city);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
		}
		return list;
	}
	
	public String getWeaidByCityName(String name){

		Cursor cursor = db.rawQuery("select cityid from city where citynm=?",new String[]{name});
		String weaid = null;
		while (cursor.moveToNext()) {
			 weaid=cursor.getString(0);
		}
		cursor.close();
		return weaid;
	}
	
	
}
