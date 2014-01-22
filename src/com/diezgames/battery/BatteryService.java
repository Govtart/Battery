package com.diezgames.battery;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.support.v4.app.NotificationCompat;


public class BatteryService extends Service{

	Intent rootIntent;
	Bitmap bigIcon;
	Timer myTimer;
	Notification noti;
	NotificationManager notificationManager;
	String numbersTheme;
	AlertDialog.Builder lowLevelDialog;
    Context context;
    boolean notif20 = false;
    boolean notif10 = false;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		  IntentFilter intentFilter = new IntentFilter();
		  intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		  registerReceiver(myReceiver, intentFilter);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("Service", "onStart");
		super.onStart(intent, startId);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(myReceiver);
		//notificationManager.cancelAll();
		stopForeground(true);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("Service", "onStartCommand");
		setPickedTheme();
		updateNotification(getApplicationContext(), getBatteryLevel());
		return super.onStartCommand(intent, flags, startId);		
	}
	
	void setPickedTheme()
	{
		SharedPreferences theme = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		numbersTheme = theme.getString("theme", "ocra");
	}
	
	boolean isPhonePluggedIn(Context context){
		
	    boolean charging = false;
	    final Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
	    boolean batteryCharge = status==BatteryManager.BATTERY_STATUS_CHARGING;
	    int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
	    boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
	    boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

	    if (batteryCharge || usbCharge || acCharge) charging = true;

	    return charging;
	}
	
	int getBatteryLevel() {
	    Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

	    // Error checking that probably isn't needed but I added just in case.
	    if(level == -1 || scale == -1) {
	        return 50;
	    }
	    return (int)(((float)level / (float)scale) * 100.0f); 
	}
	
	String isCharging(int batteryLevel)
	{
		
		if(isPhonePluggedIn(getApplicationContext()))
		{
			bigIcon = BitmapFactory.decodeResource(getResources(), R.drawable.battery_charging);
			if(batteryLevel < 100)
				return getApplicationContext().getResources().getString(R.string.battery_charging);
					else
						return getApplicationContext().getResources().getString(R.string.battery_full_charging);
		}
		else
		{
			bigIcon = null;
			return getApplicationContext().getResources().getString(R.string.battery_no_charging);
		}
	}
	

	private void updateNotification(Context context, int batteryLevel)
	{
		String batteryState = isCharging(batteryLevel);
		Log.i("NotifService", batteryState + " " + batteryLevel + "%");
		int resId = getResources().getIdentifier(numbersTheme +"_" + batteryLevel,"drawable","com.diezgames.battery");
		String batLevel = getResources().getString(R.string.battery_level);
		Intent intent = new Intent(BatteryService.this,MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent, 0);
		noti = new NotificationCompat.Builder(context)
	    .setContentTitle(batLevel + " " + batteryLevel + "%")
	    .setContentText(batteryState)
	    .setLargeIcon(bigIcon)
	    .setSmallIcon(resId)
	    .setOngoing(true) // Again, THIS is the important line
	    .setContentIntent(pendingIntent)
	    .build();//
		//notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//notificationManager.notify(0, noti);//
		startForeground(1, noti);
	}
	
	private BroadcastReceiver myReceiver = new BroadcastReceiver()
	   {
	       @Override
	       public void onReceive(Context context, Intent intent)
	       {
	        String action = intent.getAction();
	        if (action.equals(Intent.ACTION_BATTERY_CHANGED))
	        {
	        	int butteryLevel = intent.getIntExtra("level", 0);
	        	updateNotification(context, butteryLevel);
	        	
	        	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
	        	
	        	if(sp.getBoolean("notifyLowBattery", false))
	        	{
	        		Boolean firstNotifEnable = sp.getBoolean("lowLevelFirstEnabled", true);
	        		Boolean secondNotifEnable = sp.getBoolean("lowLevelSecondEnabled", true);
	        		int lowLevelFirstValue = sp.getInt("LowLevelFirstValue", 20);
	        		int lowLevelSecondValue = sp.getInt("LowLevelSecondValue", 10);
			        	if(butteryLevel == lowLevelFirstValue && !notif20 && !isPhonePluggedIn(context) && firstNotifEnable)
			        	{
			        		notif20 = true;
			        		Log.i("NotifService","start 20 activity");
			        		Intent dialogIntent = new Intent(getBaseContext(), LowBattery20Activity.class);
			        		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        		getApplication().startActivity(dialogIntent);
			        	}
				        	else 
				        		if(butteryLevel != lowLevelFirstValue && notif20)
					        	{
					        		notif20 = false;
					        	}
			        	
			        	if(butteryLevel == lowLevelSecondValue && !notif10 && !isPhonePluggedIn(context) && secondNotifEnable)//
			        	{
			        		notif10 = true;
			        		Log.i("NotifService","start 10 activity");
			        		Intent dialogIntent = new Intent(getBaseContext(), LowBattery10Activity.class);
			        		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        		getApplication().startActivity(dialogIntent);
			        	}
				        	else 
				        		if(butteryLevel != lowLevelSecondValue && notif10)
					        	{
					        		notif10 = false;
					        	}
	        	}
	        	
	        }
	       }
	   };

}
