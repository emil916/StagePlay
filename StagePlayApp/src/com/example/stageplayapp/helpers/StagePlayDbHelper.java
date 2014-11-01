package com.example.stageplayapp.helpers;

import java.util.ArrayList;

import com.example.stageplayapp.models.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StagePlayDbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "StagePlayDbHelper";
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "stageplay";
	
	private static final String TABLE_PLAYCONFIGS = "playconfigs";
	private static final String COLUMN_PLAYCONFIGS_ID = "_id";
	private static final String COLUMN_PLAYCONFIGS_NAME = "name";
	private static final String COLUMN_PLAYCONFIGS_AUTHOR = "author";
	private static final String COLUMN_PLAYCONFIGS_LANGUAGE = "language";
	private static final String COLUMN_PLAYCONFIGS_GENRE = "genre";
	private static final String COLUMN_PLAYCONFIGS_PUBLISHED="published";
	private static final String COLUMN_PLAYCONFIGS_SUMMARY = "summary";
	
	private static final String TABLE_ACTORS = "actors";
	private static final String COLUMN_ACTORS_PLAYID = "play_id";
	private static final String COLUMN_ACTORS_NAME = "name";
	
	private static final String TABLE_DECKS = "decks";
	private static final String COLUMN_DECKS_PLAYID = "play_id";
	private static final String COLUMN_DECKS_ACTORNAME = "actor_name";
	private static final String COLUMN_DECKS_IMAGENAME = "image_name";
	private static final String COLUMN_DECKS_IMAGETYPE = "image_type";
	private static final String COLUMN_DECKS_IMAGE = "image";
	
	private static final String TABLE_DIALOGUES = "dialogues";
	private static final String COLUMN_DIALOGUES_PLAYID = "play_id";
	private static final String COLUMN_DIALOGUES_DIALOGUEID = "dialogue_id";
	private static final String COLUMN_DIALOGUES_ACTORSEQID = "actor_seqid";
	private static final String COLUMN_DIALOGUES_ACTORNAME = "actor_name";
	private static final String COLUMN_DIALOGUES_TEXT = "text";
	
	public StagePlayDbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlPlayConfig = String.format("CREATE TABLE %s" +
				"(%s TEXT PRIMARY KEY, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT)",
				TABLE_PLAYCONFIGS,
				COLUMN_PLAYCONFIGS_ID,
				COLUMN_PLAYCONFIGS_NAME,
				COLUMN_PLAYCONFIGS_AUTHOR,
				COLUMN_PLAYCONFIGS_LANGUAGE,
				COLUMN_PLAYCONFIGS_GENRE,
				COLUMN_PLAYCONFIGS_PUBLISHED,
				COLUMN_PLAYCONFIGS_SUMMARY
				);
		
		String sqlActors = String.format("CREATE TABLE %s " +
				"(%s TEXT REFERENCES %s(%s), " +
				"%s TEXT, " +
				"PRIMARY KEY(%s, %s))",
				TABLE_ACTORS,
				COLUMN_ACTORS_PLAYID, TABLE_PLAYCONFIGS, COLUMN_PLAYCONFIGS_ID,
				COLUMN_ACTORS_NAME,
				COLUMN_ACTORS_PLAYID,COLUMN_ACTORS_NAME
				);
		
		String sqlDecks = String.format("CREATE TABLE %s " +
				"(%s TEXT REFERENCES %s(%s), " +
				"%s TEXT REFERENCES %s(%s), " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s BLOB, " +
				"PRIMARY KEY(%s,%s,%s))",
				TABLE_DECKS,
				COLUMN_DECKS_PLAYID, TABLE_PLAYCONFIGS,	COLUMN_PLAYCONFIGS_ID,
				COLUMN_DECKS_ACTORNAME, TABLE_ACTORS, COLUMN_ACTORS_NAME,
				COLUMN_DECKS_IMAGENAME,
				COLUMN_DECKS_IMAGETYPE,
				COLUMN_DECKS_IMAGE,
				COLUMN_DECKS_PLAYID, COLUMN_DECKS_ACTORNAME, COLUMN_DECKS_IMAGENAME
				);
		
		String sqlDialogues = String.format("CREATE TABLE %s " +
				"(%s TEXT REFERENCE %s(%s), " +
				"%s INTEGER, " +
				"%s INTEGER, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"PRIMARY KEY(%s,%s))",
				TABLE_DIALOGUES,
				COLUMN_DIALOGUES_PLAYID, TABLE_PLAYCONFIGS, COLUMN_PLAYCONFIGS_ID,
				COLUMN_DIALOGUES_DIALOGUEID,
				COLUMN_DIALOGUES_ACTORSEQID,
				COLUMN_DIALOGUES_ACTORNAME,
				COLUMN_DIALOGUES_TEXT,
				COLUMN_DIALOGUES_PLAYID, COLUMN_DIALOGUES_DIALOGUEID
				);
		
		try{
			db.execSQL(sqlPlayConfig);
			db.execSQL(sqlActors);
			db.execSQL(sqlDialogues);
			db.execSQL(sqlDecks);
			Log.i(TAG, "DB creation SUCCESS!");
		}
		catch(android.database.SQLException sqlex)
		{
			Log.i(TAG, "DB creation FAILED :( ...");
			Log.e(TAG, sqlex.getMessage() + sqlex.getStackTrace());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		Log.i(TAG, "Upgrading DB...");
		//TODO: Try to retain data?
		try{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIALOGUES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTORS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYCONFIGS);		
		onCreate(db);
		
		Log.i(TAG, "DB upgrade SUCCESS");
		}
		catch(android.database.SQLException sqlex)
		{
			Log.i(TAG, "DB upgrade FAILED :( ...");
			Log.e(TAG, sqlex.getMessage() + sqlex.getStackTrace());
		}
	}
	
	public PlayConfig getPlayConfig(String playId)
	{
		PlayConfig config = new PlayConfig();
		
		SQLiteDatabase db = this.getReadableDatabase();
		String playQuery = String.format("select * from %s where %s=%s", 
				TABLE_PLAYCONFIGS, 
				COLUMN_PLAYCONFIGS_ID, 
				playId);
		Cursor cursor = db.rawQuery(playQuery, null);
		
		while(cursor.moveToFirst() && !cursor.isAfterLast())
		{
			config.setId(cursor.getString(0));
			config.setName(cursor.getString(1));
			config.setAuthor(cursor.getString(2));
			config.setLanguage(cursor.getString(3));
			config.setGenre(cursor.getString(4));
			config.setPublished(cursor.getString(5));
			config.setSummary(cursor.getString(6));
		}
		
		cursor.close();
		db.close();
		
		return config;
	}
	
	public ArrayList<Actor> getActors(String playId)
	{
		ArrayList<Actor> actors = new ArrayList<Actor>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		String actorsQuery = String.format("select * from %s where %s=%s", 
				TABLE_ACTORS, 
				COLUMN_ACTORS_PLAYID, 
				DatabaseUtils.sqlEscapeString(playId));
		Cursor cursor = db.rawQuery(actorsQuery, null);
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			Actor actor = new Actor();
			actor.setPlayId(cursor.getString(0));
			actor.setName(cursor.getString(1));
			actors.add(actor);
		}
		
		cursor.close();
		db.close();
		
		return actors;
	}
	
	public ArrayList<DeckImage> getDeckImages(String playId, String actorName)
	{
		ArrayList<DeckImage> deckImages = new ArrayList<DeckImage>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		String deckQuery = String.format("select * from %s where %s=%s and %s=%s", 
				TABLE_DECKS, 
				COLUMN_DECKS_PLAYID, 
				DatabaseUtils.sqlEscapeString(playId),
				COLUMN_DECKS_ACTORNAME,
				DatabaseUtils.sqlEscapeString(actorName));
		Cursor cursor = db.rawQuery(deckQuery, null);
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			DeckImage deckImage = new DeckImage();
			deckImage.setPlayId(cursor.getString(0));
			deckImage.setActorName(cursor.getString(1));
			deckImage.setImageName(cursor.getString(2));
			deckImage.setImageType(cursor.getString(3));
			deckImage.setImage(cursor.getBlob(4));
			deckImages.add(deckImage);
		}
		
		cursor.close();
		db.close();
		
		return deckImages;
	}
	
	public ArrayList<Dialogue> getDialogues(String playId, int min, int max)
	{
		ArrayList<Dialogue> dialogues = new ArrayList<Dialogue>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		String dialoguesQuery = String.format("select * from %s where %s=%s and %s>=%d and %s<%d", 
				TABLE_DIALOGUES, 
				COLUMN_DIALOGUES_PLAYID, 
				DatabaseUtils.sqlEscapeString(playId),
				COLUMN_DIALOGUES_DIALOGUEID,
				min,
				COLUMN_DIALOGUES_DIALOGUEID,
				max);
		Cursor cursor = db.rawQuery(dialoguesQuery, null);
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			Dialogue dialogue = new Dialogue();
			
			dialogue.setPlayId(cursor.getString(0));
			dialogue.setDialogueId(cursor.getInt(1));
			dialogue.setActorSeqId(cursor.getInt(2));
			dialogue.setActorName(cursor.getString(3));
			dialogue.setText(cursor.getString(4));
			
			dialogues.add(dialogue);
		}
		
		cursor.close();
		db.close();
		
		return dialogues;
	}
	
	public long insertPlayConfig(PlayConfig playConfig)
	{
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_PLAYCONFIGS_ID, playConfig.getId());
		cv.put(COLUMN_PLAYCONFIGS_NAME, playConfig.getName());
		cv.put(COLUMN_PLAYCONFIGS_AUTHOR, playConfig.getAuthor());
		cv.put(COLUMN_PLAYCONFIGS_LANGUAGE, playConfig.getLanguage());
		cv.put(COLUMN_PLAYCONFIGS_GENRE, playConfig.getGenre());
		cv.put(COLUMN_PLAYCONFIGS_PUBLISHED, playConfig.getPublished());
		cv.put(COLUMN_PLAYCONFIGS_SUMMARY, playConfig.getSummary());
		
		return getWritableDatabase().insert(TABLE_PLAYCONFIGS, null, cv);
	}
	
	public long insertActor(Actor actor)
	{
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ACTORS_PLAYID, actor.getPlayId());
		cv.put(COLUMN_ACTORS_NAME, actor.getName());
		
		return getWritableDatabase().insert(TABLE_ACTORS, null, cv);
	}
	
	public long insertDeckImage(DeckImage deckImage)
	{
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_DECKS_PLAYID, deckImage.getPlayId());
		cv.put(COLUMN_DECKS_ACTORNAME, deckImage.getActorName());
		cv.put(COLUMN_DECKS_IMAGENAME, deckImage.getImageName());
		cv.put(COLUMN_DECKS_IMAGETYPE, deckImage.getImageType());
		cv.put(COLUMN_DECKS_IMAGE, deckImage.getImage());
		
		return getWritableDatabase().insert(TABLE_DECKS, null, cv);
	}
	
	public long insertDialogue(Dialogue dialogue)
	{
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_DIALOGUES_PLAYID, dialogue.getPlayId());
		cv.put(COLUMN_DIALOGUES_DIALOGUEID, dialogue.getDialogueId());
		cv.put(COLUMN_DIALOGUES_ACTORSEQID, dialogue.getActorSeqId());
		cv.put(COLUMN_DIALOGUES_ACTORNAME, dialogue.getActorName());
		cv.put(COLUMN_DIALOGUES_TEXT, dialogue.getText());
		
		return getWritableDatabase().insert(TABLE_DIALOGUES, null, cv);
	}
}