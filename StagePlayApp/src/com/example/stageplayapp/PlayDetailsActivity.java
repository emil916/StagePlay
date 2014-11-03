package com.example.stageplayapp;

import com.example.stageplayapp.models.PlayConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
 
public class PlayDetailsActivity extends Activity{
	public static final String PARCELSTRING_PLAYCONFIG_TO_DISPLAY = "playConfigToDisplay";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		TextView name, author, genre, summary ;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playdetails);
		
		PlayConfig pconfig = getPlayConfig(getIntent());
		if(pconfig==null)
		{
			Toast errorMsg = Toast.makeText(this, "Error! Nothing to display", Toast.LENGTH_LONG);
			errorMsg.setGravity(Gravity.CENTER, 0, 0);
			errorMsg.show();
			return;
		}
		
		
		String playTitleText = pconfig.getName();
		String published = pconfig.getPublished();
		if(published!=null && published.length()>0)
		{
			playTitleText += " (" + published + ")";
		}
		
		name = (TextView) findViewById(R.id.tv_playdetails_name);
		name.setText(playTitleText);
		
		author = (TextView) findViewById(R.id.tv_playdetails_author);
		author.setText(pconfig.getAuthor());
		
		String other = pconfig.getGenre() + " • " + pconfig.getLanguage();
		genre = (TextView) findViewById(R.id.tv_playdetails_other);
		genre.setText(other);
		
		summary = (TextView) findViewById(R.id.tv_playdetails_summary);
		summary.setText(pconfig.getSummary());
	}
	
	private PlayConfig getPlayConfig(Intent intent)
	{
		if(intent == null)
			return null;
		
		PlayConfig pc = intent.getParcelableExtra(PARCELSTRING_PLAYCONFIG_TO_DISPLAY);
		return pc;
	}
}

