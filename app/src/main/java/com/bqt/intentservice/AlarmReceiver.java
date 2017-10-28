package com.bqt.intentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int repeatCount = intent.getIntExtra("repeatCount", 0);//注意，这里获取的值是恒久不变的，因为这个值是在设置定时任务时固定写死的。
		Toast.makeText(context, "定时任务已经执行了  " + repeatCount, Toast.LENGTH_SHORT).show();
		Intent workIntent = new Intent(context, LongRunningService.class);
		workIntent.putExtra("type", "work");
		context.startService(workIntent);
	}
}