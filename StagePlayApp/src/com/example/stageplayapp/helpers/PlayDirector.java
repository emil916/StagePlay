package com.example.stageplayapp.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

public class PlayDirector {

	private static final String TAG = "PlayDirector";
	private Context context;
	private String playId;
	private int currDialogueId;
	
	private static final String defaultDeckAssetsSubDir = "deck/default";
	
	private SparseArray<Dialogue> dialogues;
	private HashMap<String, Actor> actors;
	private HashMap<String, ArrayList<DeckImage>> decks;
	private HashMap<String, ArrayList<ActorColor>> colors;
	private HashMap<String, ArrayList<Picture>> customizedDecks;
	private ArrayList<String> defaultDeckAsString;
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
		colors = new HashMap<String, ArrayList<ActorColor>>();
		decks = new HashMap<String, ArrayList<DeckImage>>();
		customizedDecks = new HashMap<String, ArrayList<Picture>>();
		defaultDeck = new ArrayList<Picture>();
		defaultDeckAsString = new ArrayList<String>();
		
		loadDefaultDecks();
		loadMaxDialogueId();
		loadDialogues();
		loadActors();
		loadActorColors();
		loadCustomDecks();
		prepCustomizedDecks();
	}
	
	private void loadDefaultDecks()
	{
		try{
			AssetManager assetMgr = context.getAssets();
			String[] imgNames = assetMgr.list(defaultDeckAssetsSubDir);
			
			for(String imgName: imgNames)
			{
				String svgtext = CharStreams.toString(new InputStreamReader(assetMgr.open(defaultDeckAssetsSubDir + File.separator + imgName )));
				//SVG svg = SVG.getFromAsset(assetMgr, defaultDeckAssetsSubDir + File.separator + imgName);
				SVG svg = SVG.getFromString(svgtext);
				defaultDeck.add(svg.renderToPicture());
				defaultDeckAsString.add(svgtext);
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
	
	private void prepCustomizedDecks()
	{
		
		/*
		 * NOTE: The logic for custom decks is as follows -
		 * 1. If BOTH images and colors are specified - apply colors to given images
		 * 2. If ONLY images are specified - use images
		 * 3. If ONLY colors are specified - apply colors to default deck and use these images for actor
		 * 4. If NEITHER images nor colors are specified - use default deck
		 */
		

		Iterator<String> it = actors.keySet().iterator();
		while(it.hasNext())
		{
			String actorName = it.next();
			ArrayList<ActorColor> actorColorSet = colors.get(actorName);
			ArrayList<DeckImage> actorImageSet = decks.get(actorName);
			ArrayList<Picture> perActorPictures = new ArrayList<Picture>();
			
			boolean hasColors = (actorColorSet!=null && actorColorSet.size()>0);
			boolean hasImages = (actorImageSet!=null && actorImageSet.size()>0);
			
			if((!hasColors) && (!hasImages)) continue; // no customization to be done here
			
			if(hasColors && hasImages)
			{
				for(DeckImage deckImage : actorImageSet)
				{
					String imageText = new String(deckImage.getImage());
					for(ActorColor actorColorItem : actorColorSet)
					{
						imageText = imageText.replace(actorColorItem.getReplace(), actorColorItem.getReplaceWith());
					}
					
					try {
						SVG svg = SVG.getFromString(imageText);
						perActorPictures.add(svg.renderToPicture());
					} catch (SVGParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(hasImages)
			{
				for(DeckImage deckImage : actorImageSet)
				{
					String imageText = new String(deckImage.getImage());
					try {
						SVG svg = SVG.getFromString(imageText);
						perActorPictures.add(svg.renderToPicture());
					} catch (SVGParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(hasColors)
			{
				for(String imageText: defaultDeckAsString)
				{
					String customText = imageText;
					for(ActorColor actorColorItem : actorColorSet)
					{
						customText = customText.replace(actorColorItem.getReplace(), actorColorItem.getReplaceWith());
					}
					
					try {
						SVG svg = SVG.getFromString(customText);
						perActorPictures.add(svg.renderToPicture());
					} catch (SVGParseException e) {
						e.printStackTrace();
					}
				}
			}
			
			customizedDecks.put(actorName, perActorPictures);
		}
	}
	
	private void loadActorColors()
	{
		Iterator<String> it = actors.keySet().iterator();
		while(it.hasNext())
		{
			String actorName = it.next();
			ArrayList<ActorColor> actorColors = dbHelper.getActorColors(playId, actorName);
			if(actorColors!=null && actorColors.size()>0)
			{
				colors.put(actorName, actorColors);
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
		
		ArrayList<Picture> deckToUse;
		if(customizedDecks.containsKey(d.getActorName()))
		{
			deckToUse = customizedDecks.get(d.getActorName());
			Picture img = deckToUse.get(d.getActorSeqId() % deckToUse.size());
			return img;
		}
		
		Picture p = defaultDeck.get(d.getActorSeqId() % defaultDeck.size());
		return p;
	}
	
}
