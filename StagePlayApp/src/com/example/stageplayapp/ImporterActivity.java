package com.example.stageplayapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.daidalos.afiledialog.FileChooserDialog;

import com.example.stageplayapp.helpers.ImportStagePlayPackageHelper;
import com.example.stageplayapp.helpers.StagePlayConfigFile;
import com.example.stageplayapp.helpers.StagePlayDbHelper;
import com.example.stageplayapp.helpers.StagePlayDialoguesFile;
import com.example.stageplayapp.helpers.StagePlayZipContents;
import com.example.stageplayapp.models.Actor;
import com.example.stageplayapp.models.Dialogue;

public class ImporterActivity extends Activity {
	private static final String TAG = "ImporterActivity";
	
	ImportStagePlayPackageHelper ispph;
	TextView tv_info;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importer);		
	
		tv_info = (TextView)findViewById(R.id.tv_ai_info);

		Button btn_pick = (Button)findViewById(R.id.button_ai_pickFile);
		Button btn_ok = (Button)findViewById(R.id.button_ai_ok);
		
		ispph = new ImportStagePlayPackageHelper(this, null);
		btn_pick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
//				Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
				
				FileChooserDialog dialog = new FileChooserDialog(ImporterActivity.this);
				// Assign listener for the select event.
	    		dialog.addListener(ImporterActivity.this.onFileSelectedListener);
	    		dialog.setTitle("Select a ZIP File");
	    		dialog.setFilter(".*zip");
				dialog.show();
//	    		fetchFromDialog("/storage/emulated/0/_MyFTP/windermere_last.zip");
				
			}
		});
	
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}
	
	private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
		public void onFileSelected(Dialog source, File file) {
			source.dismiss();
//			Toast toast = Toast.makeText(ImporterActivity.this, "File selected: " + file.getAbsolutePath(), Toast.LENGTH_LONG);
//			toast.show();
			
			fetchFromDialog(file.getAbsolutePath());
		}

		@Override
		public void onFileSelected(Dialog source, File folder, String name) {
			// This is for folder selection. Should be left empty
			
		}
	};


	private void fetchFromDialog(String filePath) {
		// Get the path
        Log.d(TAG, "File Path: " + filePath);
        
        new MyAsynchTaskFromFile().execute(filePath);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	StagePlayZipContents zipContents;
	StagePlayConfigFile stagePlayConfigFile;
	StagePlayDialoguesFile dialogues;
	
	private class MyAsynchTaskFromFile extends AsyncTask<String, Void, Integer> {
		private ProgressDialog mDialog;
		private ImageView iv;
		
		@Override
		protected void onPreExecute() {
			iv = (ImageView)findViewById(R.id.imageView1);
		    new ProgressDialog(ImporterActivity.this);
		    mDialog = ProgressDialog.show(ImporterActivity.this, "", "Extracting zip..");
		    super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			StagePlayDbHelper db = new StagePlayDbHelper(ImporterActivity.this);
	        zipContents = ispph.deflateZipFile(params[0]);
	        String currFilePlayId = zipContents.getSubDirName();
	        if(db.getPlayConfig(currFilePlayId).getId() == null) {
	        	db.close();
	        	stagePlayConfigFile = ispph.getStagePlayConfigFile(zipContents);
		        dialogues = ispph.getStagePlayDialoguesFile(zipContents);
		        	
	        } else {
	        	db.close();
	        	return 1;
	        }
	        
	        if (zipContents==null || stagePlayConfigFile==null ||
	        		dialogues==null)
	        	return 2;
	        	
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) {        

	        if(mDialog!=null && mDialog.isShowing()) {
	            mDialog.dismiss();
	        }
	        if (result == 1) {
	        	iv.setImageResource(R.drawable.done);
	        	tv_info.setText("The selected file already exists in "
	        			+ "the app database");
	        }else if (result == 2) {
	        	iv.setImageResource(R.drawable.error);
	        	tv_info.setText("Something went wrong when pulling"
	        			+ "data from the ZIP file :(");
	        }
	        else {
	        	new MyAsynchTaskToDB().execute();
	        }
	        	
		}
		
	}
	
	private class MyAsynchTaskToDB extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog mDialog;
		private ImageView iv;
		
		@Override
		protected void onPreExecute() {
			iv = (ImageView)findViewById(R.id.imageView1);
		    new ProgressDialog(ImporterActivity.this);
		    mDialog = ProgressDialog.show(ImporterActivity.this, "", "Loading into DB..");
		    super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			StagePlayDbHelper db = new StagePlayDbHelper(ImporterActivity.this);
	        boolean success = (db.insertPlayConfig(stagePlayConfigFile.getPlayConfig())>0);
	        if(!success) return false;
	        success = db.insertDialogues(dialogues.getDialogues());
	        if(!success) return false;
	        
	        ArrayList<Actor> actors = new ArrayList<Actor>();
	        Iterator<String> it2 = dialogues.getActors().iterator();
	        while (it2.hasNext()) {
	        	Actor actor = new Actor();
	        	actor.setPlayId(stagePlayConfigFile.getPlayConfig().getId());
	        	actor.setName(it2.next());
				actors.add(actor);
	        }
	       
	        if(actors.size()>0)
        	{
	        	success = db.insertActors(actors);
        	}
	       
			return success;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				tv_info.setText("The selected play has been successfully "
					+ "added into the app database");			
				iv.setImageResource(R.drawable.done);
			} else {
				tv_info.setText("Something went wrong when reading the data"
						+ "into the app database :(");
				iv.setImageResource(R.drawable.error);
			}
				
	        if(mDialog!=null && mDialog.isShowing()) {
	            mDialog.dismiss();
	        }
		}
		
	}
}
