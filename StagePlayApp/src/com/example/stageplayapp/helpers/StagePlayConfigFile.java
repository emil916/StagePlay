package com.example.stageplayapp.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import com.example.stageplayapp.models.PlayConfig;

public class StagePlayConfigFile {

	private static final String TAG = "StagePlayConfigFile";
	
	String playId;
	PlayConfig playConfig;
	private HashMap<String, ArrayList<String>> actors = new HashMap<String, ArrayList<String>>();
	
	public StagePlayConfigFile()
	{
		Log.i(TAG, "Default Constructor");
	}
	

	public void setPlayConfig(PlayConfig pc)
	{
		this.playConfig = pc;
	}
	
	public PlayConfig getPlayConfig()
	{
		return this.playConfig;
	}

	public HashMap<String, ArrayList<String>> getActors() {
		return actors;
	}

	public void setActors(HashMap<String, ArrayList<String>> actors) {
		this.actors = actors;
	}
	
}
