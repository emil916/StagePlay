package com.example.stageplayapp;

import com.example.stageplayapp.helpers.ImportStagePlayPackageHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ImporterActivity extends Activity {
	private static final String TAG = "ImporterActivity";
	private static final int READ_REQUEST_CODE = 42;
	Button btn_pick;
	ImportStagePlayPackageHelper ispph;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importer);
		
//		btn_pick = (Button)findViewById(R.id.button_pick);
		
		ispph = new ImportStagePlayPackageHelper(this, null);
		btn_pick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
				
			    // Search for all documents available via installed storage providers
			    intent.setType("*/*"); 
			    
			    // Filter to only show results that can be "opened", such as a
			    // file (as opposed to a list of contacts or timezones)
			    intent.addCategory(Intent.CATEGORY_OPENABLE);

			    try {
			        startActivityForResult(
			                Intent.createChooser(intent, "Select a ZIP File"),
			                READ_REQUEST_CODE);
			    } catch (android.content.ActivityNotFoundException ex) {
			        // Potentially direct the user to the Market with a Dialog
			        Toast.makeText(ImporterActivity.this, "Please install a File Manager.", 
			                Toast.LENGTH_SHORT).show();
			    }
				
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
	        Intent data) {

	    if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
	        if (data != null) {	            
	         // Get the Uri of the selected file 
	            Uri uri = data.getData();
	            Log.d(TAG, "File Uri: " + uri.toString());
	            
	            // Get the path
	            String path = uri.getPath();
	            Log.d(TAG, "File Path: " + path);
	            ispph.deflateZipFile(path);
	            
	            // do here
	        }
	    }
	    super.onActivityResult(requestCode, resultCode, data);
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
