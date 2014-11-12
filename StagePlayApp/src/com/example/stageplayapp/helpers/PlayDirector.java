package com.example.stageplayapp.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Picture;
import android.util.Log;
import android.util.SparseArray;

import com.caverock.androidsvg.*;
import com.example.stageplayapp.models.*;

public class PlayDirector {

	private static final String TAG = "PlayDirector";
	private Context context;
	private String playId;
	private int currDialogueId;
	
	private static final String defaultDeckAssetsSubDir = "deck/default";
	
	private SparseArray<Dialogue> dialogues;
	private HashMap<String, Actor> actors;
	private HashMap<String, ArrayList<DeckImage>> decks;
	private ArrayList<Picture> defaultDeck;
	private StagePlayDbHelper dbHelper;
	private int maxDialogueIdForPlay;
	
	// Call this when the play is to start from the beginning
	public PlayDirector(Context context, String playId)
	{
		Init(context, playId, 1);
	}
	
	public PlayDirector(Context context, String playId, int dialogueId)
	{
		Init(context, playId, dialogueId);
	}
	
	private void Init(Context context, String playId, int dialogueId)
	{
		this.context = context;
		this.playId = playId;
		this.currDialogueId = dialogueId;
		
		dbHelper = new StagePlayDbHelper(this.context);
		dialogues = new SparseArray<Dialogue>();
		actors = new HashMap<String, Actor>();
		decks = new HashMap<String, ArrayList<DeckImage>>();
		defaultDeck = new ArrayList<Picture>();
		
		loadDefaultDecks();
		loadMaxDialogueId();
		loadDialogues();
		loadActors();
		loadCustomDecks();
	}
	
	private void loadDefaultDecks()
	{
		try{
			AssetManager assetMgr = context.getAssets();
			String[] imgNames = assetMgr.list(defaultDeckAssetsSubDir);
			
			for(String imgName: imgNames)
			{
				SVG svg = SVG.getFromAsset(assetMgr, defaultDeckAssetsSubDir + File.separator + imgName);
				defaultDeck.add(svg.renderToPicture());
			}
		}
		catch(SVGParseException spe)
		{
			Log.e(TAG, spe.getMessage());
		}
		catch(IOException ex)
		{
			Log.e(TAG, ex.getMessage());
		}
	}
	
	private void loadMaxDialogueId()
	{
	 	maxDialogueIdForPlay = dbHelper.getMaxDialogueId(this.playId);
	}
	
	private void loadDialogues()
	{
		//TODO: For now, we'll load everything ... eventually we can load a few things at a time
		ArrayList<Dialogue> dialogueList = dbHelper.getDialogues(playId, 1, maxDialogueIdForPlay);
		
		for(int i=0;i<dialogueList.size();i++)
		{
			Dialogue d = dialogueList.get(i);
			dialogues.put(d.getDialogueId(), d);
		}
	}
	
	private void loadActors()
	{
		ArrayList<Actor> actorList = dbHelper.getAllActors(playId);
		
		for(int i=0; i<actorList.size();i++)
		{
			Actor a = actorList.get(i);
			actors.put(a.getName(), a);
		}
	}
	
	private void loadCustomDecks()
	{
		Iterator<String> it = actors.keySet().iterator();
		while(it.hasNext())
		{
			String actorName = it.next();
			ArrayList<DeckImage>actorImages = dbHelper.getDeckImages(playId, actorName);
			if(actorImages!=null && actorImages.size()>0)
			{
				decks.put(actorName, actorImages);
			}
		}
	}
	
	public boolean hasPrevious()
	{
		return currDialogueId > 1;
	}
	
	public boolean hasNext()
	{
		return currDialogueId < maxDialogueIdForPlay;
	}
	
	public Dialogue getCurrentDialogue()
	{
		return dialogues.get(currDialogueId);
	}
	
	public void moveToNextDialogue()
	{
		if(currDialogueId<=maxDialogueIdForPlay)
			currDialogueId++;
	}
	
	public void moveToPreviousDialogue()
	{
		if(currDialogueId>1)
			currDialogueId--;
	}
	
	public Picture getCurrentPicture()
	{
		Dialogue d = getCurrentDialogue();
		if(d==null || d.getActorSeqId()==0) return null;
		
		ArrayList<DeckImage> deckToUse;
		if(decks.containsKey(d.getActorName()))
		{
			deckToUse = decks.get(d.getActorName());
			DeckImage img = deckToUse.get(d.getActorSeqId() % deckToUse.size());
			// TODO: Convert to picture object, cache and return it
			return null;
		}
		
		Picture p = defaultDeck.get(d.getActorSeqId() % defaultDeck.size());
		return p;
	}
	
}
