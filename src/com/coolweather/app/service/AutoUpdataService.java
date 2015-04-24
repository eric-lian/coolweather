package com.coolweather.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import com.coolweather.app.Content;
import com.coolweather.app.activity.MainActivity;
import com.coolweather.app.receiver.UpdataReceiver;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.SharedPreferencesUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AutoUpdataService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		loadWeather();
		AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour=8*60*60*1000;
		long trigglerAtTime=SystemClock.elapsedRealtime()+anHour;
		Intent i = new Intent(this,UpdataReceiver.class);
		PendingIntent operation =PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,trigglerAtTime, operation);
		return super.onStartCommand(intent, flags, startId);
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
				SharedPreferencesUtil.putValue(AutoUpdataService.this,"Today",responseInfo.result);
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(AutoUpdataService.this,"更新失败！", 1).show();
			}
		});
		//得到未来五天的天气
		HttpUtil.loadData(HttpMethod.GET,Content.REQUEST_CITY_WEATHER_URL+"&weaid="+weaid,null,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(AutoUpdataService.this,"更新失败！", 1).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> requesInfo) {
				SharedPreferencesUtil.putValue(AutoUpdataService.this,"Future",requesInfo.result);
			}
			
		});
	}
}
