package com.diezgames.battery;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class BatteryWidgetService extends Service {

	Timer myTimer;
	Notification noti;
	NotificationManager notificationManager;
	String numbersTheme;
	
	@Override
	public void onCreate() {
		super.onCreate();
		  IntentFilter intentFilter = new IntentFilter();
		  intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		  registerReceiver(myReceiver, intentFilter);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(myReceiver);
	}
	
	String setPickedTheme()
	{
		SharedPreferences theme = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		numbersTheme = theme.getString("theme", "ocra");
		return numbersTheme;
	}
	
	void updateAppWidget(Context context,int batteryLevel)
	{
		setPickedTheme();
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.battery_widget_layout);	
		int resId = getResources().getIdentifier(numbersTheme +"_" + batteryLevel,"drawable","com.diezgames.battery");
	    updateViews.setImageViewResource(R.id.battery_widget, resId);
	    ComponentName myComponentName = new ComponentName(context, BatteryWidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myComponentName, updateViews);
        Log.i("WidgetService", "Widget:" + batteryLevel + "%");
	}
	
	int getBatteryLevel(Context context) {
	    Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

	    if(level == -1 || scale == -1)
	        return 50;
	    
	    return (int)(((float)level / (float)scale) * 100.0f); 
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		updateAppWidget(getApplicationContext(),getBatteryLevel(getApplicationContext()));
		return super.onStartCommand(intent, flags, startId);
	}

	private BroadcastReceiver myReceiver = new BroadcastReceiver()
	   {
	       @Override
	       public void onReceive(Context context, Intent intent)
	       {
	        String action = intent.getAction();
	        if (action.equals(Intent.ACTION_BATTERY_CHANGED))
	        {
	        	updateAppWidget(context, intent.getIntExtra("level", 0));
	        }
	       }
	   };
}
