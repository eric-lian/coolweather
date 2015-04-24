package com.coolweather.app.util;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class HttpUtil {

	public static void loadData(HttpRequest.HttpMethod method ,String url,RequestParams params,RequestCallBack<String> callBack){
		System.out.println("你请求得url是："+url);
		HttpUtils http=new HttpUtils();
		//配置缓存失效的周期
		http.configCurrentHttpCacheExpiry(1000 * 1);
		//如果参数不等于空
		if (params != null) {
		
		} else {
		//如果参数等于空		
			params = new RequestParams();
		}
http://api.cnmo.com/client?module=api_libraries_sjdbg_articlelist&offset=0&isclass=1&cid=3&rows=10&apiid=3&timestamp=1428499320&token1=daf0482e632d427a4b039f3f6b371705&returnformat=json&encoding=utf8

		
		//设备ID 请求参数可以往头文件中设置一些参数，例如手机的型号
		//params.addHeader("x-deviceid", app.deviceId);
		//渠道，统计用
		//params.addHeader("x-channel", app.channel);
		
		//发送一个http请求
		http.send(method, url, params, callBack);
		
	}
}
