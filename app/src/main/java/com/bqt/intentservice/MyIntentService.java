package com.bqt.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * 所有请求的Intent记录会按顺序加入到【队列】中并按顺序【异步】执行，并且每次只会执行【一个】工作线程，当所有任务执行完后IntentService会【自动】停止
 */
public class MyIntentService extends IntentService {
	
	public MyIntentService() {
		super("包青天的工作线程");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {//注意，因为这里是异步操作，所以这里不能直接使用Toast。
		Log.i("bqt", "onHandleIntent");
		int intentNumber = intent.getExtras().getInt("intentNumber");//根据Intent中携带的参数不同执行不同的任务
		Log.i("bqt", "第" + intentNumber + "个工作线程启动了");
		SystemClock.sleep(3000);
		Log.i("bqt", "第" + intentNumber + "个工作线程完成了");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i("bqt", "onBind");
		return super.onBind(intent);
	}
	
	@Override
	public void onCreate() {
		Log.i("bqt", "onCreate");
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("bqt", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void setIntentRedelivery(boolean enabled) {
		super.setIntentRedelivery(enabled);
		Log.i("bqt", "setIntentRedelivery");
	}
	
	@Override
	public void onDestroy() {
		Log.i("bqt", "onDestroy");
		super.onDestroy();
	}
}