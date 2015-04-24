package com.coolweather.app.receiver;

import com.coolweather.app.service.AutoUpdataService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdataReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Intent intent = new Intent(arg0,AutoUpdataService.class);
		arg0.startActivity(intent);
		
	}

}
