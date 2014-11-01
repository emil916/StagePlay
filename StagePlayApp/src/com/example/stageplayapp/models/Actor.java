package com.example.stageplayapp.models;

import java.util.ArrayList;

public class Actor {

	private String playId;
	private String name;
	ArrayList<DeckImage> deck;
	
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
	
	public ArrayList<DeckImage> getDeck()
	{
		return this.deck;
	}
	
	public void setDeck(ArrayList<DeckImage> deck)
	{
		this.deck = deck;
	}
	
}
