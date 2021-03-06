package com.example.stageplayapp.helpers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.stageplayapp.models.ActorColor;
import com.example.stageplayapp.models.Dialogue;
import com.example.stageplayapp.models.PlayConfig;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

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
		PlayConfig pc = new PlayConfig();
		pc.setId(contents.getSubDirName());
		String configFilePath = outputDir + File.separator + contents.getSubDirName()
				+ File.separator + StagePlayZipContents.CONFIG_FILENAME;
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
							pc.setTitle(metaObj.getString("title"));
						if (metaObj.has("author"))
							pc.setAuthor(metaObj.getString("author"));
						if (metaObj.has("language"))
							pc.setLanguage(metaObj.getString("language"));
						if (metaObj.has("genre"))
							pc.setGenre(metaObj.getString("genre"));
						if (metaObj.has("published"))
							pc.setPublished(metaObj.getString("published"));
						if (metaObj.has("summary"))
							pc.setSummary(metaObj.getString("summary"));
						
						config.setPlayConfig(pc);
					}

					if (playObj.has("actors")) {
						JSONObject actorsObj = playObj.getJSONObject("actors");
						JSONArray actorsArray = actorsObj.getJSONArray("actor");
						HashMap<String, ArrayList<String>> actorImages = new HashMap<String, ArrayList<String>>();
						HashMap<String, ArrayList<ActorColor>> actorColors = new HashMap<String, ArrayList<ActorColor>>();
						
						for (int i = 0; i < actorsArray.length(); i++) 
						{
							JSONObject actorObj = actorsArray.getJSONObject(i);
				
							String id = actorObj.getString("id");

							if (actorObj.has("deck")) {
								JSONObject deckObj = (JSONObject) actorObj.get("deck");
								if (deckObj.has("image")) {
									JSONArray imageArray = deckObj.getJSONArray("image");
									ArrayList<String> imagesPerActor = new ArrayList<String>();
									
									for (int j = 0; j < imageArray.length(); j++) 
									{
										String image = imageArray.getString(j);
										imagesPerActor.add(image);
									}
									
									if (imagesPerActor.size() > 0 && id != null	&& id.length() > 0
											&& !actorImages.containsKey(id)) {
										actorImages.put(id, imagesPerActor);
									}
								}
							}
							
							if(actorObj.has("colors"))
							{
								JSONObject colorsObj = (JSONObject)actorObj.get("colors");
								if(colorsObj.has("color"))
								{
									ArrayList<ActorColor> colorsPerActor = new ArrayList<ActorColor>();
									JSONArray colorArray = colorsObj.getJSONArray("color");
									
									for(int k =0; k < colorArray.length(); k++)
									{
										JSONObject color = (JSONObject)colorArray.get(k);
										String replace = color.getString("replace");
										String replaceWith = color.getString("replaceWith");
										ActorColor actorColor = new ActorColor( contents.getSubDirName(), id, replace, replaceWith);
										colorsPerActor.add(actorColor);
									}
									
									if(colorsPerActor.size()>0 && id!=null && id.length() > 0 && !actorColors.containsKey(id))
									{
										actorColors.put(id, colorsPerActor);
									}
								}
							}
									
						}

						config.setActors(actorImages);
						config.setActorColors(actorColors);
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
		String dialoguesFilePath = outputDir + File.separator + contents.getSubDirName() 
				+ File.separator + StagePlayZipContents.CONFIG_PLAYFILE;
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
		byte[] buffer = new byte[2048];
		int size;
		
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
				
				if(ze.isDirectory())
				{
					subDirName = ze.getName().substring(0, ze.getName().length()-1);
					if(contents.getSubDirName()==null) // we capture just the first subdir
					{
						contents.setSubDirName(subDirName);
					}
					createDir(subDirName, outputLocation);
				} 
				else 
				{
					String innerFileName = ze.getName();
					files.add(innerFileName);
					
					FileOutputStream fout = new FileOutputStream(
							outputLocation + File.separator	+ innerFileName);
					BufferedOutputStream bos = new BufferedOutputStream(fout, buffer.length);
					
					while((size = zin.read(buffer, 0, buffer.length))!=-1)
					{
						bos.write(buffer, 0, size);
					}

					bos.flush();
					bos.close();
					fout.flush();
					fout.close();
					zin.closeEntry();
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
		
		return contents; 
	}
	
	public byte[] getRawFile(String subDir, String fileName)
	{
		//NOTE: Since we're only supporting SVG, this below code is fine.
		// If we were to support other file types, we would need to be change this!
		
		String svgText;
		try {
			String completeFilePath = outputDir + File.separator + subDir
					+ File.separator + fileName;
			svgText = Files.toString(new File(completeFilePath), Charsets.UTF_8);
			return svgText.getBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void createDir(String dir, String outputLocation)
	{
		File f = new File(outputLocation + File.separator + dir);
		if(!f.isDirectory())
			f.mkdirs();
	}
}
