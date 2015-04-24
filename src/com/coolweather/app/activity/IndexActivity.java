package com.coolweather.app.activity;

import com.coolweather.app.Content;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.Citys;
import com.coolweather.app.model.Citys.City;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.JsonUtil;
import com.coolweather.app.util.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class IndexActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//首先查看城市列表在数据库中是否已经缓存下来，如果没有缓存，则进行下载，缓存到数据库中,这里就不用和服务器端的数量进行比较了
	if(CoolWeatherDB.getInstance(this).getColumnSum()<=0){
		HttpUtil.loadData(HttpMethod.GET,Content.RQUEST_CITY_URL,null,new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String resutl = JsonUtil.ObjectToArray(responseInfo.result);
				if(!TextUtils.isEmpty(resutl)){
					Gson gson=new Gson();
					Citys citys = gson.fromJson(resutl,Citys.class);
					//System.out.println("唉呦唉！！！"+citys.result.size());
					CoolWeatherDB.getInstance(IndexActivity.this).saveAllCity(citys.result);
				}
					
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(IndexActivity.this, "加载城市数据失败，请重新加载", 1).show();
			}
		});
	};
	//查看当前的城市是否已经设定，如果已经设定，则转跳到现实天气预报的页面
	String location = SharedPreferencesUtil.getValue(this, "location",null);
	
	if(TextUtils.isEmpty(location)){
		//如果没有设定当前的城市则转跳到设置城市的页面
		Intent intent = new Intent(this,SettingCity.class);
		startActivity(intent);
	}else {
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}
		
	
	}
}
