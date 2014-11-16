package com.example.stageplayapp.models;

public class ActorColor {
	String playId;
	String actorName;
	String replace;
	String replaceWith;
	
	public ActorColor()
	{
		
	}
	
	public ActorColor (String playId, String actorName, String replace, String replaceWith)
	{
		this.playId = playId;
		this.actorName = actorName;
		this.replace = replace;
		this.replaceWith = replaceWith;
	}
	
	public String getPlayId()
	{
		return this.playId;
	}
	
	public void setPlayId(String playId)
	{
		this.playId = playId;
	}
	
	public String getActorName()
	{
		return this.actorName;
	}
	
	public void setActorName(String actorName)
	{
		this.actorName = actorName;
	}
	
	public String getReplace()
	{
		return this.replace;
	}
	
	public void setReplace(String replace)
	{
		this.replace = replace;
	}
	
	public String getReplaceWith()
	{
		return this.replaceWith;
	}
	
	public void setReplaceWith(String replaceWith)
	{
		this.replaceWith = replaceWith;
	}
}
