package com.diezgames.battery;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class LowBatteryValuesSettingsActivity extends Activity{
	SharedPreferences sp;
	Editor ed;	
	String tag = "LowBatterySettings";
	
	CheckBox lowLevelFirstCheckBox;
	CheckBox lowLevelSecondCheckBox;
	SeekBar lowLevelFirstSeekBar;
	SeekBar lowLevelSecondSeekBar;
	EditText lowLevelFirstEditValue;
	EditText lowLevelSecondEditValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_low_battery_values_settings);
		
		lowLevelFirstCheckBox = (CheckBox)findViewById(R.id.LowLevel20Checkbox);
		lowLevelSecondCheckBox = (CheckBox)findViewById(R.id.LowLevel10Checkbox);
		lowLevelFirstSeekBar = (SeekBar)findViewById(R.id.LowLevel20seekBar);
		lowLevelSecondSeekBar = (SeekBar)findViewById(R.id.LowLevel10seekBar);
		lowLevelFirstEditValue = (EditText)findViewById(R.id.LowLevel20Value);
		lowLevelSecondEditValue = (EditText)findViewById(R.id.LowLevel10Value);
		
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed = sp.edit();
		
	    loadSettings(sp);
		
		lowLevelFirstSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			ed.putInt("LowLevelFirstValue", Integer.parseInt(lowLevelFirstEditValue.getText().toString()));
			ed.commit();
			Log.i(tag, lowLevelFirstEditValue.getText().toString());
			}			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				lowLevelFirstEditValue.setText(Integer.toString(progress));
			}
		});
		
		lowLevelSecondSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			ed.putInt("LowLevelSecondValue", Integer.parseInt(lowLevelSecondEditValue.getText().toString()));
			ed.commit();
			Log.i(tag, lowLevelSecondEditValue.getText().toString());
			}			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				lowLevelSecondEditValue.setText(Integer.toString(progress));
			}
		});
		
		lowLevelFirstCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            	ed.putBoolean("LowLevelFirstEnabled", isChecked); 
            	ed.commit();
            	setViewChecked(lowLevelFirstSeekBar,lowLevelFirstEditValue, isChecked);            	
            Log.i(tag, "LowLevFirst = " + isChecked);
			}
		});
		
		lowLevelSecondCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
            	ed.putBoolean("LowLevelSecondEnabled", isChecked);
            	ed.commit();
            	setViewChecked(lowLevelSecondSeekBar,lowLevelSecondEditValue, isChecked);
            Log.i(tag, "LowLevSecond = " + isChecked);
			}
		});

	}
	
	private void setViewChecked(SeekBar sb, EditText et, boolean set)
	{
    	sb.setEnabled(set);
		et.setEnabled(set);    	
	}
	
	private void loadSettings(SharedPreferences sp)
	{
		boolean lowLevelFirstEnabled = sp.getBoolean("LowLevelFirstEnabled", true);
		boolean lowLevelSecondEnabled = sp.getBoolean("LowLevelSecondEnabled", true);		
		int lowLevelFirstValue = sp.getInt("LowLevelFirstValue", 20);
		int lowLevelSecondValue = sp.getInt("LowLevelSecondValue", 10);
		
		lowLevelFirstCheckBox.setChecked(lowLevelFirstEnabled);
		lowLevelSecondCheckBox.setChecked(lowLevelSecondEnabled);
		
		lowLevelFirstEditValue.setText(Integer.toString(lowLevelFirstValue));
		lowLevelSecondEditValue.setText(Integer.toString(lowLevelSecondValue));
		
		lowLevelFirstSeekBar.setProgress(lowLevelFirstValue);
		lowLevelSecondSeekBar.setProgress(lowLevelSecondValue);
		
		setViewChecked(lowLevelFirstSeekBar,lowLevelFirstEditValue, lowLevelFirstEnabled);
		setViewChecked(lowLevelSecondSeekBar,lowLevelSecondEditValue, lowLevelSecondEnabled);
		
		Log.i(tag, "Enabled1 = " + lowLevelFirstEnabled + " Enabled2 = " + lowLevelSecondEnabled + " Value1 = " + lowLevelFirstValue + " Value2 = " + lowLevelSecondValue );
		
	}

}
