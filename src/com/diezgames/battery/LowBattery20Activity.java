package com.diezgames.battery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LowBattery20Activity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_low_battery_20);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		TextView lowLevel20Text = (TextView)findViewById(R.id.lowLevel20Text);
		lowLevel20Text.setText(sp.getInt("LowLevelFirstValue", 20) + "% " + getResources().getString(R.string.ref_low_level_20_notify_sum));
		Button dissmis = (Button)findViewById(R.id.dissmis_button_20);
		dissmis.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
