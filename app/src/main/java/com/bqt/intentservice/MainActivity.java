package com.bqt.intentservice;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

public class MainActivity extends ListActivity {
	private int intentNumber = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"启动一个新的工作线程",
				"启动一个前台服务",
				"设置一次性定时后台服务",
				"设置一个周期性执行的定时服务",
				"取消AlarmManager的定时服务"};
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Arrays.asList(array)));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		switch (position) {
			case 0:
				Intent intent = new Intent(this, MyIntentService.class);
				Bundle bundle = new Bundle();
				bundle.putInt("intentNumber", intentNumber++);//从0开始
				intent.putExtras(bundle);
				startService(intent);//每次启动都会新建一个工作线程，但始终只有一个IntentService实例
				break;
			case 1:
				startService(new Intent(this, ForegroundService.class));
				break;
			case 2:
				Intent intent2 = new Intent(this, LongRunningService.class);
				intent2.putExtra("type", "onceAlarm");
				startService(intent2);
				break;
			case 3:
				Intent intent3 = new Intent(this, LongRunningService.class);
				intent3.putExtra("type", "repeatAlarm");
				startService(intent3);
				break;
			case 4:
				AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				manager.cancel(PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), 0));
				break;
		}
	}
}