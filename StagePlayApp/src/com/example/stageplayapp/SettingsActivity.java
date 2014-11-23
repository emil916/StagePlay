package com.example.stageplayapp;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends PreferenceActivity {
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.settings);
 
    }
}


/*
public class SettingsActivity extends Activity {
		ToggleButton toggle;
		RadioGroup radioGroup;
		RadioButton btn_radio_5,btn_radio_7,btn_radio_10;
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_settings);
			toggle = (ToggleButton) findViewById(R.id.btn_toggle);
			toggle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Boolean on = ((ToggleButton)v).isChecked();
					if(on){
						toggle.setTextOff("Disabled");
						// TODO Auto-generated method stub
					}
					else {
						toggle.setTextOn("Enabled");
					}
					
				}
			});
			radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
			radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if(checkedId==R.id.btn_radio_5){
						Toast.makeText(getApplicationContext(), "choice: 5 seconds", Toast.LENGTH_SHORT).show();// TODO Auto-generated method stub
					}
					else if(checkedId==R.id.btn_radio_7){
						Toast.makeText(getApplicationContext(), "choice: 7 seconds", Toast.LENGTH_SHORT).show();
					}
					else if(checkedId==R.id.btn_radio_10){
						Toast.makeText(getApplicationContext(), "choice: 10 seconds", Toast.LENGTH_SHORT).show();
					}
				}
			});
}
}
*/