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


public class PlayListActivity extends Activity {
	
	StagePlayDbHelper dbHelper;
	PlayListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist);
		dbHelper = new StagePlayDbHelper(this);
		ArrayList<PlayConfig> data = dbHelper.getAllPlayConfigs();
		adapter = new PlayListAdapter(this, R.layout.playdetails_row, data);
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
	
	@Override public void onResume()
	{
		super.onResume();
		refreshData();
	}
	
	public void refreshData()
	{		
		adapter.clear();
		adapter.addAll(dbHelper.getAllPlayConfigs());
		adapter.notifyDataSetChanged();
	}
}

