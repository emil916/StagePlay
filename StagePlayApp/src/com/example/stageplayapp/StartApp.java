package com.example.stageplayapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartApp  extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startapp);
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(3000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally {
					Intent intentforstartapp = new Intent(StartApp.this,MainActivity.class);
					startActivity(intentforstartapp);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
