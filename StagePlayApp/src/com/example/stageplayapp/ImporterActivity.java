package com.example.stageplayapp;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserDialog;

import com.example.stageplayapp.helpers.ImportStagePlayPackageHelper;
import com.example.stageplayapp.helpers.StagePlayConfigFile;
import com.example.stageplayapp.helpers.StagePlayDialoguesFile;
import com.example.stageplayapp.helpers.StagePlayZipContents;

public class ImporterActivity extends Activity {
	private static final String TAG = "ImporterActivity";
	private static final int READ_REQUEST_CODE = 42;
	Button btn_pick;
	ImportStagePlayPackageHelper ispph;
	TextView tv_playTitle, tv_author;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importer);
		
		tv_playTitle = (TextView)findViewById(R.id.tv_ai_playTitle);
		tv_author = (TextView)findViewById(R.id.tv_ai_author);
		btn_pick = (Button)findViewById(R.id.button_pickFile);
		
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
				
			}
		});
	}
	
	private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
		public void onFileSelected(Dialog source, File file) {
			source.hide();
			Toast toast = Toast.makeText(ImporterActivity.this, "File selected: " + file.getAbsolutePath(), Toast.LENGTH_LONG);
			toast.show();
			
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
        StagePlayZipContents zipContents = ispph.deflateZipFile(filePath);
        
        StagePlayConfigFile playConfig = ispph.getStagePlayConfigFile(zipContents);
        String playTitle = playConfig.getTitle();
        tv_playTitle.setText(playTitle);
        String author = playConfig.getAuthor();
        tv_author.setText(author);
        String genre = playConfig.getGenre();
        String language = playConfig.getLanguage();
        String summary = playConfig.getSummary();
        
        StagePlayDialoguesFile dialogues = ispph.getStagePlayDialoguesFile(zipContents);
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

}
