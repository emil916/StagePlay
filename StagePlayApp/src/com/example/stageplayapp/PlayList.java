package com.example.stageplayapp;

import android.widget.Button;

public class PlayList {
public String Title;
public Button Details;
public Button Play;

	public PlayList(){
		super();
	}
	public PlayList(String Title,Button Details,Button Play){
		super();
		this.Title = Title;
		this.Details = Details;
		this.Play = Play;
	}
}

