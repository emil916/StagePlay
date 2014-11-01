package com.example.stageplayapp.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.example.stageplayapp.models.Dialogue;
import com.example.stageplayapp.models.PlayConfig;

import android.content.Context;
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
			outputDir = context.getCacheDir().getAbsolutePath();
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
		
		//PlayConfig playConfig = getPlayConfig(contents);
		//ArrayList<Dialogue> dialogues = getDialogues(contents);
				
		// TODO: Change this to reflect actual result
		// null or empty means import went ok...otherwise hte string returned should be the error msg
		return contents; 
	}
	
	public PlayConfig getPlayConfig(StagePlayZipContents contents)
	{
		PlayConfig config = new PlayConfig();
		
		//TODO:
		
		return config;
	}
	
	public ArrayList<Dialogue> getDialogues(StagePlayZipContents contents)
	{
		ArrayList<Dialogue> dialogues = new ArrayList<Dialogue>();
		
		//TODO:
		return dialogues;		
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
				}else
				{
					String innerFileName = ze.getName();
					files.add(innerFileName);
					
					FileOutputStream fout = new FileOutputStream(outputLocation 
																	+ File.pathSeparator 
																	+ subDirName 
																	+ File.pathSeparator 
																	+ innerFileName);
					for(int c = zin.read(); c!=-1; c=zin.read())
					{
						fout.write(c);
					}
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
	
	private void createDir(String dir, String outputLocation)
	{
		File f = new File(outputLocation + File.pathSeparator + dir);
		if(!f.isDirectory())
			f.mkdirs();
	}
}
