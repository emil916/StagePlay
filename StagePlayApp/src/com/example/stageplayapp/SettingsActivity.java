package com.example.stageplayapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {
		ToggleButton toggle;
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
}
}