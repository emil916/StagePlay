package com.example.stageplayapp.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.stageplayapp.models.Dialogue;

public class StagePlayDialoguesFile {
	private final static String TAG = "StagePlayDialoguesFile";
	
	private ArrayList<Dialogue> dialogues = new ArrayList<Dialogue>();
	private HashMap<String, String> actors = new HashMap<String, String>();
	
	public StagePlayDialoguesFile()
	{
		
	}

	public ArrayList<Dialogue> getDialogues() {
		return dialogues;
	}

	public void setDialogues(ArrayList<Dialogue> dialogues) {
		this.dialogues = dialogues;
	}

	public HashMap<String, String> getActors() {
		return actors;
	}

	public void setActors(HashMap<String, String> actors) {
		this.actors = actors;
	}
}
