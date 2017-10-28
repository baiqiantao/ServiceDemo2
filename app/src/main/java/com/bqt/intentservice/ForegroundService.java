package com.bqt.intentservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

/**
 * 所谓的前台服务就是状态栏显示的Notification，可以让Service没那么容易被系统杀死
 */
public class ForegroundService extends Service {
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i("bqt", "onCreate");
		super.onCreate();
		Notification.Builder localBuilder = new Notification.Builder(this)
				.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
				.setAutoCancel(true)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setTicker("Foreground Service Start")
				.setContentTitle("前台服务")
				.setContentText("正在运行...");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			startForeground(1, localBuilder.build());
		}
	}
	
	@Override
	public void onDestroy() {
		Log.i("bqt", "onDestroy");
		super.onDestroy();
	}
}