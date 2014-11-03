package com.example.stageplayapp.helpers;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import com.example.stageplayapp.models.Actor;
import com.example.stageplayapp.models.PlayConfig;

public class StagePlayConfigFile {

	private static final String TAG = "StagePlayConfigFile";
	
	String playId;
	PlayConfig playConfig;
	private HashMap<String, ArrayList<String>> actors = new HashMap<String, ArrayList<String>>();
	
	public StagePlayConfigFile()
	{
		
	}
	
	public void setPlayId(String id)
	{
		this.playId = id;
	}
	
	public String getPlayId(){
		return this.playId;
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
