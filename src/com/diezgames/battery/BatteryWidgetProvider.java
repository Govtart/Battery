package com.diezgames.battery;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class BatteryWidgetProvider extends AppWidgetProvider{
	
	String numbersTheme;
@Override
public void onUpdate(Context context, AppWidgetManager appWidgetManager,
		int[] appWidgetIds) {
	updateWidget(context);
	context.startService(new Intent(context, BatteryWidgetService.class));
	//BatteryWidgetService.
}

private void updateWidget(Context context)
{
	
	RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.battery_widget_layout);
	int resId = context.getResources().getIdentifier(setPickedTheme(context) +"_" + getBatteryLevel(context),"drawable","com.diezgames.battery");
    updateViews.setImageViewResource(R.id.battery_widget, resId);
    ComponentName myComponentName = new ComponentName(context, BatteryWidgetProvider.class);
    AppWidgetManager manager = AppWidgetManager.getInstance(context);
    manager.updateAppWidget(myComponentName, updateViews);
}

String setPickedTheme(Context context)
{
	SharedPreferences theme = PreferenceManager.getDefaultSharedPreferences(context);
	numbersTheme = theme.getString("theme", "ocra");
	return numbersTheme;
}

int getBatteryLevel(Context context) {
    Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

    // Error checking that probably isn't needed but I added just in case.
    if(level == -1 || scale == -1) {
        return 50;
    }
    return (int)(((float)level / (float)scale) * 100.0f); 
}

}
