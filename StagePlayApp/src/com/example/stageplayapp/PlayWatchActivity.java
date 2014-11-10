package com.example.stageplayapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PlayWatchActivity extends Activity{
	
	LinearLayout layout1, layout2;
	TextureView textv;
	boolean test = true;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watchplay);
		
		ImageButton im_prev = (ImageButton)findViewById(R.id.imageButton_prev);
		ImageButton im_play = (ImageButton)findViewById(R.id.imageButton_play);
		ImageButton im_next = (ImageButton)findViewById(R.id.imageButton_next);
		
		layout1 = (LinearLayout)findViewById(R.id.linearLayout_1);
		layout2 = (LinearLayout)findViewById(R.id.linearLayout_2);
		
		textv = (TextureView)findViewById(R.id.textureView1);
		
		im_prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		im_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(test){
					layout1.setVisibility(View.GONE);
					layout2.setVisibility(View.VISIBLE);
					test = false;
				} else {
					layout2.setVisibility(View.GONE);
					layout1.setVisibility(View.VISIBLE);
					test = true;
				}
				
			}
		});
		
		im_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
