package com.example.stageplayapp.models;

public class DeckImage {
	
	private String playId;
	private String actorName;
	private String imageName;
	private String imageType;
	private byte[] image;
	
	public DeckImage()
	{
	}
	
	public String getPlayId()
	{
		return this.playId;
	}
	
	public void setPlayId(String playId){
		this.playId = playId;
	}
	
	public String getActorName(){
		return this.actorName;
	}
	
	public void setActorName(String name)
	{
		this.actorName = name;
	}
	
	public String getImageName(){
		return this.imageName;
	}
	
	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}
	
	public String getImageType()
	{
		return this.imageType;
	}
	
	public void setImageType(String imageType)
	{
		this.imageType = imageType;
	}
	
	public byte[] getImage()
	{
		return this.image;
	}
	
	public void setImage(byte[] image)
	{
		this.image = image;
	}
}
