package com.example.stageplayapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stageplayapp.helpers.PlayDirector;
public class PlayWatchActivity extends Activity{
	
	TextView tv_dialog;
	LinearLayout layout1, layout2;
	ImageView imageView;
	boolean test = true;
	PlayDirector playDirector;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watchplay);
		
		playDirector = new PlayDirector(this, "0e557084-6a76-4534-9a10-544cb91f6fbb");
		
		tv_dialog = (TextView)findViewById(R.id.tv_wp_dialogueText);
		tv_dialog.setText(playDirector.getCurrentDialogue().getText());
		
		ImageButton im_prev = (ImageButton)findViewById(R.id.imageButton_prev);
		ImageButton im_play = (ImageButton)findViewById(R.id.imageButton_play);
		ImageButton im_next = (ImageButton)findViewById(R.id.imageButton_next);
		
		layout1 = (LinearLayout)findViewById(R.id.linearLayout_1);
		layout2 = (LinearLayout)findViewById(R.id.linearLayout_2);
		
		imageView = (ImageView)findViewById(R.id.imageView1);
		
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg_main);

        Config config = bm.getConfig();
        int width = bm.getWidth();
        int height = bm.getHeight();

        Bitmap newImage = Bitmap.createBitmap(width, height, config);

        Canvas c = new Canvas(newImage);
        c.drawBitmap(bm, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW); 
        paint.setStyle(Style.FILL);                
        paint.setTextSize(200);
        c.drawText(playDirector.getCurrentDialogue().getActorName(), 0, 250, paint);

        imageView.setImageBitmap(newImage);
		
//		Picture pic = playDirector.getCurrentPicture();
//		imageView.setImageBitmap(pictureDrawable2Bitmap(pic));
	
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
