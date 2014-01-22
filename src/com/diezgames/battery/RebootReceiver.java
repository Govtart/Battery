package com.diezgames.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RebootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		 if(sp.getBoolean("autoRunService", false))
		 {
			 Intent batteryNotificIntent = new Intent(context, BatteryService.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 Intent batteryWidgetIntent = new Intent(context, BatteryWidgetService.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 context.startService(batteryNotificIntent);
			 context.startService(batteryWidgetIntent);
		 }
	}

}
