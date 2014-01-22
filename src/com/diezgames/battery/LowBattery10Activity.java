package com.diezgames.battery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LowBattery10Activity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_low_battery_10);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		TextView lowLevel10Text = (TextView)findViewById(R.id.lowLevel10Text);
		lowLevel10Text.setText(sp.getInt("LowLevelSecondValue", 10) + "% " + getResources().getString(R.string.ref_low_level_10_notify_sum));
		Button dissmis = (Button)findViewById(R.id.dissmis_button_10);
		dissmis.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
