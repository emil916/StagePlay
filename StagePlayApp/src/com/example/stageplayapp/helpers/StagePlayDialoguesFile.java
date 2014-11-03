package com.example.stageplayapp.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

import com.example.stageplayapp.models.Actor;
import com.example.stageplayapp.models.Dialogue;

public class StagePlayDialoguesFile {
	private final static String TAG = "StagePlayDialoguesFile";
	
	private ArrayList<Dialogue> dialogues = new ArrayList<Dialogue>();
	private HashSet<String> actors = new HashSet<String>();
	
	public StagePlayDialoguesFile()
	{
		Log.i(TAG, "Default Constructor");
	}

	public ArrayList<Dialogue> getDialogues() {
		return dialogues;
	}

	public void setDialogues(ArrayList<Dialogue> dialogues) {
		this.dialogues = dialogues;
	}

	public HashSet<String> getActors() {
		return actors;
	}

	public void setActors(HashSet<String> actors2) {
		this.actors = actors2;
	}
}
