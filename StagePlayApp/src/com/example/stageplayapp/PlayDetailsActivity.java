package com.example.stageplayapp;

import com.example.stageplayapp.models.PlayConfig;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
 
public class PlayDetailsActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstsanceState){
		TextView name, author, genre, summary ;
		
		super.onCreate(savedInstsanceState);
		setContentView(R.layout.activity_playdetails);
		//instead of using this object, have to use the intent coming from the PlayListActivity.java
		PlayConfig pconfig = new PlayConfig();
		
		name = (TextView) findViewById(R.id.tv_playdetails_name);
		name.setText(pconfig.getName());
		
		author = (TextView) findViewById(R.id.tv_playdetails_author);
		author.setText(pconfig.getAuthor());
		
		genre = (TextView) findViewById(R.id.tv_playdetails_other);
		genre.setText(pconfig.getGenre());
		
		summary = (TextView) findViewById(R.id.tv_playdetails_summary);
		summary.setText(pconfig.getSummary());
	}
	
}

