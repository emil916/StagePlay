package com.example.stageplayapp.models;

import java.util.ArrayList;

public class Actor {

	private String playId;
	private String name;
	
	public Actor()
	{
	}
	
	public String getPlayId(){
		return this.playId;
	}
	
	public void setPlayId(String playId){
		this.playId = playId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
		
}
