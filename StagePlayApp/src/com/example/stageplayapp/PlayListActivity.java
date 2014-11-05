package com.example.stageplayapp;


import java.util.ArrayList;

import com.example.stageplayapp.helpers.StagePlayDbHelper;
import com.example.stageplayapp.models.PlayConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;


public class PlayListActivity extends Activity{
	
	private ListView ListView1;
	StagePlayDbHelper dbHelper;
	ArrayList<PlayConfig> data = new ArrayList<PlayConfig>();
	PlayListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist);
		dbHelper = new StagePlayDbHelper(this);
		data = dbHelper.getAllPlayConfigs();
		adapter = new PlayListAdapter(this, R.layout.row,data);
		ListView l = (ListView) findViewById(R.id.listView1);
		l.setAdapter(adapter);
		Button btn_pickPlay = (Button)findViewById(R.id.button_download);
		 
		btn_pickPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PlayListActivity.this, ImporterActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	//@Override public void onResume()
	{
		//this.data = dbHelper.getAllPlayConfigs();
		//adapter.setNotifyOnChange(true);
	}
}

