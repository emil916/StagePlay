package com.example.stageplayapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
Button Resume,AllPlays,Settings,About;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Resume = (Button) findViewById(R.id.button1);
		AllPlays = (Button) findViewById(R.id.button2);
		Settings = (Button) findViewById(R.id.button3);
		About = (Button) findViewById(R.id.button4);
		
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		
		Resume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startResumeActivity();// TODO Auto-generated method stub
				
			}
		});
		AllPlays.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startAllPlaysActivity();// TODO Auto-generated method stub
				
			}
		});
		Settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startSettingsActivity();// TODO Auto-generated method stub
				
			}
		});
		About.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startAboutActivity();// TODO Auto-generated method stub
				
			}
		});
	}
	public void startResumeActivity(){
		Intent i = new Intent(this,PlayWatchActivity.class);
		startActivity(i);
	}
	public void startAllPlaysActivity(){
		System.out.println("In this activity");
		Intent i = new Intent(this,PlayListActivity.class);
		startActivity(i);
	}
	public void startSettingsActivity(){
		
	}
	public void startAboutActivity(){
		Intent i = new Intent(this,AboutActivity.class);
		startActivity(i);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}