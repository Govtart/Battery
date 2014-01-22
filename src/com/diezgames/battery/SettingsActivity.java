package com.diezgames.battery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.diezgames.battery.R;

public class SettingsActivity extends PreferenceActivity{
	
	Item[] items = {
		    new Item("Ocra Green Neon", R.drawable.ocra_99),
		    new Item("Gothic Gree", R.drawable.gothic_99),
		    new Item("Minecraft Green", R.drawable.minecraft_99),
		    new Item("Paper", R.drawable.paper_99),
		    //new Item("...", 0),//no icon for this one
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
		Preference rateUsButton = (Preference) findPreference("rateUs");
		Preference themeList = (Preference) findPreference("theme");
		Preference lowBatterySettings = (Preference) findPreference("LowBatterySettings");
		
		lowBatterySettings.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent LowBatteryAlertIntent = new Intent(SettingsActivity.this, LowBatteryValuesSettingsActivity.class);
				startActivity(LowBatteryAlertIntent);
				return false;
			}
		});
		
		rateUsButton.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				Intent googlePlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.diezgames.battery"));
				startActivity(googlePlayIntent);
				return false;//
			}
		});
		
		

		themeList.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference arg0) {				

				ListAdapter adapter = new ArrayAdapter<Item>(
					    SettingsActivity.this,
					    android.R.layout.select_dialog_item,
					    android.R.id.text1,
					    items){
					        public View getView(int position, View convertView, ViewGroup parent) {
					            //User super class to create the View
					            View v = super.getView(position, convertView, parent);
					            TextView tv = (TextView)v.findViewById(android.R.id.text1);
					            //Put the image on the TextView
					            tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);
					            //Add margin between image and text (support various screen densities)
					            int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
					            tv.setCompoundDrawablePadding(dp5);
					            return v;
					        }
					    };
				
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
				
		        builder.setTitle(getResources().getString(R.string.ref_icons_list_title));
		        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int item) {
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						Editor ed = sp.edit();
						switch(item)
						{
						case 0:
							Log.i("SETTINGS","ocra");
							ed.putString("theme", "ocra");
							ed.commit();
							if(MainActivity.serviceRunning)
								startService(new Intent(SettingsActivity.this,BatteryService.class));
							startService(new Intent(SettingsActivity.this,BatteryWidgetService.class));
							break;
							
						case 1:
							Log.i("SETTINGS","gothic");
							ed.putString("theme", "gothic");
							ed.commit();
							if(MainActivity.serviceRunning)
								startService(new Intent(SettingsActivity.this,BatteryService.class));
							startService(new Intent(SettingsActivity.this,BatteryWidgetService.class));
							break;
							
						case 2:
							Log.i("SETTINGS","minecraft");
							ed.putString("theme", "minecraft");
							ed.commit();
							if(MainActivity.serviceRunning)
								startService(new Intent(SettingsActivity.this,BatteryService.class));
							startService(new Intent(SettingsActivity.this,BatteryWidgetService.class));
							break;	
							
						case 3:
							Log.i("SETTINGS","paper");
							ed.putString("theme", "paper");
							ed.commit();
							if(MainActivity.serviceRunning)
								startService(new Intent(SettingsActivity.this,BatteryService.class));
							startService(new Intent(SettingsActivity.this,BatteryWidgetService.class));
							break;
						}											
					}
				});
		        AlertDialog alert = builder.create();
		        alert.show();
				return false;
			}
		});
		
	}
	
	


}

//if(item == 0)
//{
//	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//	Editor ed = sp.edit();
//	ed.putString("theme", "ocra");
//	ed.commit();
//	if(MainActivity.serviceRunning)
//		startService(new Intent(SettingsActivity.this,BatteryService.class));
//	startService(new Intent(SettingsActivity.this,BatteryWidgetService.class));
//}
//else
//	if(item == 1)
//	{
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//		Editor ed = sp.edit();
//		ed.putString("theme", "gothic");
//		ed.commit();
//		if(MainActivity.serviceRunning)
//			startService(new Intent(SettingsActivity.this,BatteryService.class));
//		startService(new Intent(SettingsActivity.this,BatteryWidgetService.class));
//	}
