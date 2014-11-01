package com.example.stageplayapp.helpers;

import java.util.ArrayList;

public class StagePlayZipContents {
	
	private static final String CONFIG_FILENAME = "config.json";
	private static final String CONFIG_PLAYFILE = "config.playfile";
	
	private boolean hasErrors;
	private String errorMessage;
	private String subDirName;
	private ArrayList<String> fileNames;
	
	public StagePlayZipContents()
	{
		
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public void setErrorMessage(String msg)
	{
		this.errorMessage = msg;
	}
	
	public boolean getHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public String getSubDirName() {
		return subDirName;
	}

	public void setSubDirName(String subDirName) {
		this.subDirName = subDirName;
	}

	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(ArrayList<String> fileNames) {
		this.fileNames = fileNames;
	}
	
	public boolean hasConfigFile()
	{
		return fileListContains(CONFIG_FILENAME);
	}
	
	public boolean hasPlayFile()
	{
		return fileListContains(CONFIG_PLAYFILE);
	}
	
	private boolean fileListContains(String filename)
	{
		if(fileNames==null || fileNames.size()==0) return false;
		
		for(int i = 0; i<fileNames.size();i++)
		{
			String fileName = fileNames.get(i);
			if(fileName!=null && fileName.equalsIgnoreCase(fileName))
				return true;
		}
		
		return false;
	}
	
}
