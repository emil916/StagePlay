package com.example.stageplayapp;

import com.example.stageplayapp.helpers.SharedPreferenceHelper;
import com.example.stageplayapp.helpers.StagePlayDbHelper;
import com.example.stageplayapp.models.PlayConfig;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
	Button btnResume, btnAllplays, btnSettings, btnAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnResume = (Button) findViewById(R.id.button1);
		btnAllplays = (Button) findViewById(R.id.button2);
		btnSettings = (Button) findViewById(R.id.button3);
		btnAbout = (Button) findViewById(R.id.button4);

		Bundle b = new Bundle();
		b = getIntent().getExtras();

		btnResume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startResumeActivity();// TODO Auto-generated method stub

			}
		});
		btnAllplays.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAllPlaysActivity();// TODO Auto-generated method stub

			}
		});
		btnSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startSettingsActivity();// TODO Auto-generated method stub

			}
		});
		btnAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAboutActivity();// TODO Auto-generated method stub

			}
		});
	}

	public void startResumeActivity() {
		final String play_id = SharedPreferenceHelper.readString(this,
				SharedPreferenceHelper.PLAY_ID, null);
		final int dialogue_id = SharedPreferenceHelper.readInteger(this,
				SharedPreferenceHelper.DIALOGUE_ID, 1);
		Intent i = new Intent(this, PlayWatchActivity.class);
		i.putExtra(PlayWatchActivity.PARCELSTRING_PLAYID_TO_PLAY, play_id);
		i.putExtra(PlayWatchActivity.PARCELSTRING_DIALOGUEID_TO_RESUME, dialogue_id);
		startActivity(i);
	}

	public void startAllPlaysActivity() {
		Intent i = new Intent(this, PlayListActivity.class);
		startActivity(i);
	}

	public void startSettingsActivity() {
		Intent i = new Intent (this,SettingsActivity.class);
		startActivity(i);
	}

	public void startAboutActivity() {
		Intent i = new Intent(this, AboutActivity.class);
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