package com.example.stageplayapp.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stageplayapp.models.Dialogue;
import com.example.stageplayapp.models.PlayConfig;

import android.content.Context;
import android.provider.MediaStore.Files;
import android.util.Log;

public class ImportStagePlayPackageHelper {
	private static final String TAG = "ImportStagePlayPackageHelper";
	private Context context;
	private String outputDir;
	
	public ImportStagePlayPackageHelper(Context context, String outputDir)
	{
		this.context = context;
		this.outputDir = outputDir;
		
		if(outputDir==null || outputDir.length()==0)
			this.outputDir = context.getCacheDir().getAbsolutePath();
	}
	
	public StagePlayZipContents deflateZipFile(String zipFile)
	{
		//TODO: Do this via async task
		StagePlayZipContents contents = getUnzippedContents(zipFile, outputDir);
		if(contents.getHasErrors())	
			{
			contents.setErrorMessage("Error while unzipping file"); // error while unzipping
			return contents;
			}
		if((!contents.hasConfigFile())) {
			contents.setErrorMessage("No config file found"); // config file not found
			return contents;
		}
		
		if((!contents.hasPlayFile())) {
			contents.setErrorMessage("Not play file found"); // play file not found
			return contents;
		}
		
		return contents; 
	}
	
	public StagePlayConfigFile getStagePlayConfigFile(
			StagePlayZipContents contents) {
		StagePlayConfigFile config = new StagePlayConfigFile();
		String configFilePath = outputDir + File.separator + contents.getSubDirName()
				+ StagePlayZipContents.CONFIG_FILENAME;
		File configFile = new File(configFilePath);

		if (configFile.exists()) {
			StringBuilder contentStr = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(new FileReader(configFile));
				while ((line = reader.readLine()) != null) {
					contentStr.append(line).append('\n');
				}
				reader.close();
			} catch (IOException ioe) {
				Log.i(TAG, ioe.getMessage() + ioe.getStackTrace());
			}

			Log.i(TAG, "Read config file...parsing JSON");

			try {
				JSONObject jsonObj = new JSONObject(contentStr.toString());
				if (jsonObj.has("play")) {
					JSONObject playObj = (JSONObject) jsonObj.get("play");
					if (playObj.has("meta")) {
						JSONObject metaObj = (JSONObject) playObj.get("meta");
						if (metaObj.has("title"))
							config.setTitle(metaObj.getString("title"));
						if (metaObj.has("author"))
							config.setAuthor(metaObj.getString("author"));
						if (metaObj.has("language"))
							config.setLanguage(metaObj.getString("language"));
						if (metaObj.has("genre"))
							config.setGenre(metaObj.getString("genre"));
						if (metaObj.has("published"))
							config.setPublished(metaObj.getString("published"));
						if (metaObj.has("summary"))
							config.setSummary(metaObj.getString("summary"));
					}

					if (playObj.has("actors")) {
//						JSONArray actorsArray = playObj.getJSONArray("actors");
						 JSONObject actorsObj = (JSONObject)playObj.get("actors");
						HashMap<String, ArrayList<String>> actors = new HashMap<String, ArrayList<String>>();

//						for (int i = 0; i < actorsArray.length(); i++) {
//							JSONObject actorObj = actorsArray.getJSONObject(i);
						
							if (actorsObj.has("actor")) {
								// JSONArray actorArray =
								// actorsObj.getJSONArray("actor");
								// for(int i=0; i<actorArray.length();i++)
								// {
								// JSONObject a = actorArray.getJSONObject(i);
								JSONObject actorObj = actorsObj.getJSONObject("actor");
								String id = actorObj.getString("id");
								ArrayList<String> images = new ArrayList<String>();

								if (actorObj.has("deck")) {
									JSONObject deckObj = (JSONObject) actorObj.get("deck");
									if (deckObj.has("image")) {
										JSONArray imageArray = deckObj.getJSONArray("image");
										// FilenameUtils.getExtension()
										for (int j = 0; j < imageArray.length(); j++) {
											String image = imageArray.getString(j);
											images.add(image);
										}
									}
								}

								if (images.size() > 0 && id != null	&& id.length() > 0
										&& !actors.containsKey(id)) {
									actors.put(id, images);
								}
								// }
							}
//						}

						config.setActors(actors);
					}
				}
			}
			catch(JSONException jex)
			{
				Log.i(TAG, jex.getMessage() + jex.getStackTrace());
				return null;
			}
		}
		
		return config;
	}
	
	public StagePlayDialoguesFile getStagePlayDialoguesFile(StagePlayZipContents contents)
	{
		ArrayList<Dialogue> dialogues = new ArrayList<Dialogue>();
		HashSet<String> actors = new HashSet<String>();
		
		String playId = contents.getSubDirName();
		String dialoguesFilePath = outputDir + File.separator + contents.getSubDirName() + StagePlayZipContents.CONFIG_PLAYFILE;
		File dialoguesFile = new File(dialoguesFilePath);
		
		if(dialoguesFile.exists())
		{
			String line;
			try{
				BufferedReader reader = new BufferedReader(new FileReader(dialoguesFile));
				boolean isFirstLine = true;
				
				while((line = reader.readLine())!=null)
				{
					if (isFirstLine) {
						// remove the UTF8 BOM byte if exists
						line = line.replace("\ufeff", "");
						isFirstLine = false;
					}
					if(line!=null && line.trim().length()>0)
					{			
						String[] parts = line.split("\\|");
						int dialogueId = Integer.parseInt(parts[0]);
						int actorSeqId = Integer.parseInt(parts[1]);
						String actor = parts[2];
						String text = parts[3];
						Dialogue d = new Dialogue();
						d.setPlayId(playId);
						d.setDialogueId(dialogueId);
						d.setActorSeqId(actorSeqId);
						d.setActorName(actor);
						d.setText(text);
						dialogues.add(d);
						
						if(actorSeqId>0 && actor.length()>0) 
						       actors.add(actor);
					}
				}
				reader.close();
			}
			catch(IOException ioe)
			{
				Log.i(TAG, ioe.getMessage() + ioe.getStackTrace());
			}
		}		
		
		StagePlayDialoguesFile spDialoguesFile = new StagePlayDialoguesFile();
		spDialoguesFile.setDialogues(dialogues);
		spDialoguesFile.setActors(actors);
		
		return spDialoguesFile;		
	}
	
	
	private StagePlayZipContents getUnzippedContents(String zipFile, String outputLocation)
	{
		StagePlayZipContents contents = new StagePlayZipContents();
		//NOTE: Code based on http://jondev.net/articles/Unzipping_Files_with_Android_%28Programmatically%29
		try{
			FileInputStream fin = new FileInputStream(zipFile);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze = null;
			String subDirName = null;
			ArrayList<String> files = new ArrayList<String>();
			
			while((ze=zin.getNextEntry())!=null)
			{
				Log.i(TAG, "Unzipping " + ze.getName());
				
				if(ze.isDirectory()){
					subDirName = ze.getName();
					if(contents.getSubDirName()==null) // we capture just the first subdir
					{
						contents.setSubDirName(subDirName);
					}
					createDir(subDirName, outputLocation);
				} else {
					String innerFileName = ze.getName();
					files.add(innerFileName);
					
					FileOutputStream fout = new FileOutputStream(
							outputLocation + File.separator	+ innerFileName);
					for(int c = zin.read(); c!=-1; c=zin.read())
						fout.write(c);

					zin.closeEntry();
					fout.close();
				}
			}
			
			contents.setFileNames(files);
			
			zin.close();
			contents.setHasErrors(false);
		}
		catch(Exception e)
		{
			Log.e(TAG, e.getMessage()  + e.getStackTrace());
			contents.setHasErrors(true);
		}
		
		return contents; // TODO: Change this to reflect actual result
	}
	
	//public byte[] getRawFile(String fileName)
	{
		
	}
	
	private void createDir(String dir, String outputLocation)
	{
		File f = new File(outputLocation + File.separator + dir);
		if(!f.isDirectory())
			f.mkdirs();
	}
}
