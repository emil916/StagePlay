package com.example.stageplayapp.models;

public class Dialogue {
	
	private String playId;
	private int dialogueId;
	private int actorSeqId;
	private String actorName;
	private String text;
	
	public Dialogue()
	{
		
	}

	public String getPlayId() {
		return playId;
	}

	public void setPlayId(String playId) {
		this.playId = playId;
	}

	public int getDialogueId() {
		return dialogueId;
	}

	public void setDialogueId(int dialogueId) {
		this.dialogueId = dialogueId;
	}

	public int getActorSeqId() {
		return actorSeqId;
	}

	public void setActorSeqId(int actorSeqId) {
		this.actorSeqId = actorSeqId;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
