package com.example.stageplayapp.models;

import com.example.stageplayapp.PlayDetailsActivity;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class PlayConfig implements Parcelable{
	private String id;
	private String name;
	private String author;
	private String language;
	private String genre;
	private String published;
	private String summary;
	
	public PlayConfig()
	{
	}
	
	public PlayConfig(Parcel p)
	{
		id = p.readString();
		name = p.readString();
		author = p.readString();
		language = p.readString();
		genre = p.readString();
		published = p.readString();
		summary = p.readString();
		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(author);
		dest.writeString(language);
		dest.writeString(genre);
		dest.writeString(published);
		dest.writeString(summary);
	}
	
	
	public static final Parcelable.Creator<PlayConfig> CREATOR = new Parcelable.Creator<PlayConfig>() {
		public PlayConfig createFromParcel(Parcel p) {
			return null;
		
		}

		public PlayConfig[] newArray(int size) {
			return new PlayConfig[size];
		}
	};
	
		
	

	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
