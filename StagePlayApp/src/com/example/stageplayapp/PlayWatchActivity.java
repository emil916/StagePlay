package com.example.stageplayapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.stageplayapp.helpers.PlayDirector;
public class PlayWatchActivity extends Activity{
	
	LinearLayout layout1, layout2;
	ImageView imageView;
	boolean test = true;
	PlayDirector playDirector;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watchplay);
		
		playDirector = new PlayDirector(this, "0e557084-6a76-4534-9a10-544cb91f6fbb");
		
		ImageButton im_prev = (ImageButton)findViewById(R.id.imageButton_prev);
		ImageButton im_play = (ImageButton)findViewById(R.id.imageButton_play);
		ImageButton im_next = (ImageButton)findViewById(R.id.imageButton_next);
		
		layout1 = (LinearLayout)findViewById(R.id.linearLayout_1);
		layout2 = (LinearLayout)findViewById(R.id.linearLayout_2);
		
		imageView = (ImageView)findViewById(R.id.imageView1);
		
		Picture pic = playDirector.getCurrentPicture();
		imageView.setImageBitmap(pictureDrawable2Bitmap(pic));
	
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
				// Draw the label text
				 //  canvas.drawText(mData.get(mCurrentItem).mLabel, mTextX, mTextY, mTextPaint);
				
			}
		});
	}


	//Convert Picture to Bitmap
	private Bitmap pictureDrawable2Bitmap(Picture picture) {
	    PictureDrawable pd = new PictureDrawable(picture);
	    Bitmap bitmap = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap);
	    canvas.drawPicture(pd.getPicture());
	    return bitmap;
	}
}
