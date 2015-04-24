package com.coolweather.app.activity;

import java.util.ArrayList;

import org.w3c.dom.Comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.coolweather.app.Content;
import com.coolweather.app.R;
import com.coolweather.app.model.TodayWeather;
import com.coolweather.app.model.Weathers;
import com.coolweather.app.model.Weathers.Weather;
import com.coolweather.app.service.AutoUpdataService;
import com.coolweather.app.util.CommonUtil;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MainActivity extends Activity {
	@ViewInject(R.id.ab_backs)
	private Button switchCity;
	@ViewInject(R.id.ab_updata)
	private Button freshWeather;
	@ViewInject(R.id.ab_titles)
	private TextView title;
	@ViewInject(R.id.weather_date)
	private TextView weather_date;
	@ViewInject(R.id.weather_weather)
	private TextView weather_weather;
	@ViewInject(R.id.weather_tempature)
	private TextView weather_tempature;
	@ViewInject(R.id.weather_wind)
	private TextView weather_wind;
	@ViewInject(R.id.weather_address)
	private TextView weather_address;
	@ViewInject(R.id.weather_icon)
	private ImageView weather_icon;
	@ViewInject(R.id.future_weather)
	private LinearLayout future_weather_container;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		
		loadCache();
		if(com.coolweather.app.util.CommonUtil.isNetworkAvailable(this)!=1){
			loadWeather();
		};
		
		switchCity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,SettingCity.class);
				startActivity(intent);
			}
		});
		freshWeather.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			if(CommonUtil.isNetworkAvailable(MainActivity.this)!=1){
				loadWeather();
			}else {
				loadCache();
			}	
			}
		});
		Intent intent = new Intent(this,AutoUpdataService.class);
		startService(intent);
	}

	private void loadCache() {
		String today = SharedPreferencesUtil.getValue(this,"Today","");
		String future = SharedPreferencesUtil.getValue(this,"Future","");
		if(!TextUtils.isEmpty(today)&&!TextUtils.isEmpty(future)){
			handlerFutureWeather(future);
			handlerWeatherMessage(today);
		}
		
	}

	public void loadWeather() {
		//一开始进来的时候，查看是已经设置了地方天气,如果设置了就显示当前设置地方的天气，
		//否则可以通过定位得到当前的位置，现实当前位置的天气，或者转跳到选择城市的页面
		//默认显示的是北京的天气
		String weaid = SharedPreferencesUtil.getValue(this,"location","1");
		//得到今天的天气
		HttpUtil.loadData(HttpMethod.GET,Content.REQUEST_CITY_WEATURE_TODAY_URL+"&weaid="+weaid,null,new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				SharedPreferencesUtil.putValue(MainActivity.this,"Today",responseInfo.result);
				handlerWeatherMessage(responseInfo.result);
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(MainActivity.this,"更新失败！", 1).show();
			}
		});
		//得到未来五天的天气
		HttpUtil.loadData(HttpMethod.GET,Content.REQUEST_CITY_WEATHER_URL+"&weaid="+weaid,null,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(MainActivity.this,"更新失败！", 1).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> requesInfo) {
				SharedPreferencesUtil.putValue(MainActivity.this,"Future",requesInfo.result);
				handlerFutureWeather(requesInfo.result);
			}
			
		});
	}

	protected void handlerFutureWeather(String result) {
		Gson gson = new Gson();
		Weathers weathers= gson.fromJson(result, Weathers.class);
		View view ;
		if(weathers.success.equals("1")){
			BitmapUtils utils = new BitmapUtils(MainActivity.this);
			ArrayList<Weather> future_weather = weathers.result;
			future_weather.remove(0);
			for(Weather w:future_weather){
				view=View.inflate(MainActivity.this,R.layout.weather_item ,null);
				TextView info=(TextView)view.findViewById(R.id.info);
				ImageView icon=(ImageView)view.findViewById(R.id.icon);
				info.setText(w.days +"   "+w.weather);
				utils.display(icon,w.weather_icon);
				future_weather_container.addView(view);
			}
		}	
	}

	protected void handlerWeatherMessage(String result) {
		Gson gson = new Gson();
		Weather wt= gson.fromJson(result,TodayWeather.class).result;
		weather_address.setText(wt.citynm);
		weather_date.setText(wt.days+"    "+wt.week);
		BitmapUtils utils = new BitmapUtils(MainActivity.this);
		utils.display(weather_icon,wt.weather_icon);
		weather_weather.setText(wt.weather);
		weather_tempature.setText(wt.temperature);
		weather_wind.setText(wt.winp+"    "+wt.wind);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
