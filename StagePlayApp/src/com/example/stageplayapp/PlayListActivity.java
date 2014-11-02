package com.example.stageplayapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PlayListActivity extends Activity{
	Button btn_pickPlay;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist);
		
		btn_pickPlay = (Button)findViewById(R.id.button_download);
		 
		btn_pickPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PlayListActivity.this, ImporterActivity.class);
				startActivity(intent);
				
			}
		});
	}
}

