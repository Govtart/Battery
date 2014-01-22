package com.diezgames.battery;

import java.util.Timer;
import java.util.TimerTask;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView batteryLevel;
	TextView batteryStatus;
	TextView serviceStatus;
	TextView tempText;
	Button serviceSwitcher;
	Button settings;
	static boolean serviceRunning = false;
	int batteryLevelValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		batteryLevel = (TextView)findViewById(R.id.procent);
		batteryStatus = (TextView)findViewById(R.id.status_action);
		serviceStatus = (TextView)findViewById(R.id.service_run);
		serviceSwitcher = (Button)findViewById(R.id.service_switch);
		settings = (Button)findViewById(R.id.settings);
		
		serviceTurnOnOff(true);
		startService(new Intent(MainActivity.this,BatteryWidgetService.class));
		
		Timer changeBatteryStatus = new Timer("BatteryCheckerActivity",true);
		changeBatteryStatus.scheduleAtFixedRate(new GetBatteryStatus(), 0, 3000);
		
		serviceSwitcher.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(serviceRunning)
					serviceTurnOnOff(false);
						else
							serviceTurnOnOff(true);
			}
		});
		
		settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent settings = new Intent(MainActivity.this,SettingsActivity.class);
				startActivity(settings);				
			}
		});
	}

	public int getBatteryLevel() {
	    Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	    if(level == -1 || scale == -1) {
	        return 50;
	    }
	    return (int)(((float)level / (float)scale) * 100.0f); 
	}
	
	void serviceTurnOnOff(boolean switchSignal)
	{
		if(switchSignal)
		{
			startService(new Intent(MainActivity.this,BatteryService.class));
			serviceSwitcher.setText(getResources().getString(R.string.stop_service));
			serviceStatus.setText(getResources().getString(R.string.service_running));
			serviceStatus.setTextColor(Color.GREEN);
		}
		else
		{			
			stopService(new Intent(MainActivity.this,BatteryService.class));
			serviceSwitcher.setText(getResources().getString(R.string.start_service));
			serviceStatus.setText(getResources().getString(R.string.service_stoped));
			serviceStatus.setTextColor(Color.RED);
		}
		
		serviceRunning = switchSignal;
		Log.i("MainActivity", serviceRunning + "");
	}
	
	public class GetBatteryStatus extends TimerTask
	{	
		@Override
		public void run() {
			 runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					batteryLevelValue = getBatteryLevel();					
					batteryLevel.setText(batteryLevelValue+"%");
					
					if(batteryLevelValue >= 50)
					batteryLevel.setTextColor(Color.GREEN);
					else
						if(batteryLevelValue < 50 && batteryLevelValue >= 20)
							batteryLevel.setTextColor(Color.YELLOW);
						else
							if(batteryLevelValue < 20)
								batteryLevel.setTextColor(Color.RED);
					
					batteryStatus.setText(chargingState(batteryLevelValue));
					
				}
			});
				 
			 }
		}
		
	String chargingState(int batteryLevel)
		{
			
			if(isPhonePluggedIn(getApplicationContext()))
			{
				if(batteryLevel < 100){
					batteryStatus.setTextColor(Color.YELLOW);
					return getResources().getString(R.string.battery_charging);
				}
						else{
							batteryStatus.setTextColor(Color.GREEN);
							return getResources().getString(R.string.battery_full_charging);						}
			}
			else
			{
				batteryStatus.setTextColor(Color.RED);
				return getResources().getString(R.string.battery_no_charging);
			}
		}
		
	boolean isPhonePluggedIn(Context context){
	    boolean charging = false;

	    final Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	    int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
	    boolean batteryCharge = status==BatteryManager.BATTERY_STATUS_CHARGING;

	    int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
	    boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
	    boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

	    if (batteryCharge) charging=true;
	    if (usbCharge) charging=true;
	    if (acCharge) charging=true; 

	    return charging;
	}

}
