package com.bqt.intentservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LongRunningService extends Service {
	public static final int INTERVAL_MILLIS = 3 * 1000;//设置定时任务的间隔时间
	private int repeatCount = 0;
	
	@Override
	public void onCreate() {
		Log.i("bqt", "onCreate");
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i("bqt", "onBind");
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("bqt", "onStartCommand");
		switch (intent.getStringExtra("type")) {
			case "onceAlarm":
				setOnceAlarm();
				break;
			case "repeatAlarm":
				setRepeatAlarm();
				break;
			default://开辟一条线程，用来执行具体的定时逻辑操作
				repeatCount++;
				new Thread(new Runnable() {
					@Override
					public void run() {
						Log.i("bqt", new SimpleDateFormat("开始工作 yyyy.MM.dd HH-mm-ss", Locale.getDefault()).format(new Date()));
					}
				}).start();
				break;
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	/*AlarmManager中定义的type有五个可选值：
	 ELAPSED_REALTIME  闹钟在睡眠状态下不可用，如果在系统休眠时闹钟触发，它将不会被传递，直到下一次设备唤醒；使用相对系统启动开始的时间
	 ELAPSED_REALTIME_WAKEUP  闹钟在手机睡眠状态下会唤醒系统并执行提示功能，使用相对时间
	 RTC  闹钟在睡眠状态下不可用，该状态下闹钟使用绝对时间，即当前系统时间
	 RTC_WAKEUP  表示闹钟在睡眠状态下会唤醒系统并执行提示功能，使用绝对时间
	 POWER_OFF_WAKEUP  表示闹钟在手机【关机】状态下也能正常进行提示功能，用绝对时间，但某些版本并不支持！ */
	
	/*设置在triggerAtTime时间启动的定时服务。该方法用于设置一次性闹钟*/
	private void setOnceAlarm() {
		//定时任务为：发送一条广播。在收到广播后启动本服务，本服务启动后又发送一条广播……
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), 0);
		
		//参数表示闹钟(首次)执行时间。相对于系统启动时间，Returns milliseconds since boot, including time spent in sleep.
		long triggerAtTime = SystemClock.elapsedRealtime() + 5 * 1000;
		
		//使用【警报、闹铃】服务设置定时任务。CPU一旦休眠(比如关机状态)，Timer中的定时任务就无法运行；而Alarm具有唤醒CPU的功能。
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
	}
	
	/*设置一个周期性执行的定时服务，参数表示首次执行时间和间隔时间*/
	private void setRepeatAlarm() {
		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("repeatCount", repeatCount);//注意，这里传给Intent的值一旦设定后就不会再变，因为以后不会再执行这里的逻辑！
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		
		//相对于1970……的绝对时间，Returns milliseconds since boot, including time spent in sleep.
		long triggerAtTime = System.currentTimeMillis() + 5 * 1000;
		
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, INTERVAL_MILLIS, pendingIntent);
		/*Frequent alarms are bad for battery life. As of API 22, the AlarmManager will override
		 near-future and high-frequency alarm requests, delaying the alarm at least 【5 seconds】 into the future
		 and ensuring that the repeat interval is at least 【60 seconds】.
		 If you really need to do work sooner than 5 seconds, post a delayed message or runnable to a Handler.*/
	}
}