package com.example.stageplayapp.helpers;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import com.example.stageplayapp.models.Actor;

public class StagePlayConfigFile {

	private static final String TAG = "StagePlayConfigFile";
	
	private String title;
	private String author;
	private String language;
	private String genre;
	private String published;
	private String summary;
	private HashMap<String, ArrayList<String>> actors = new HashMap<String, ArrayList<String>>();
	
	public StagePlayConfigFile()
	{
		
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public HashMap<String, ArrayList<String>> getActors() {
		return actors;
	}

	public void setActors(HashMap<String, ArrayList<String>> actors) {
		this.actors = actors;
	}
	
}
