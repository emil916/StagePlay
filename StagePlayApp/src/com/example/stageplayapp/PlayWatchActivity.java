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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stageplayapp.helpers.PlayDirector;
import com.example.stageplayapp.models.Dialogue;
import com.example.stageplayapp.models.PlayConfig;
public class PlayWatchActivity extends Activity{
	public static final String TAG = "PlayWatchActivity";
	public static final String PARCELSTRING_PLAYCONFIG_TO_PLAY = "playConfigToPlay";
	public static final String PARCELSTRING_PLAYCONFIG_DIALOGUEID = "playConfigDialogueId";
	
	TextView tv_dialog;
	LinearLayout layoutNarrative, layoutDialogue;
	ImageView imageView;
	boolean test = true;
	PlayDirector playDirector;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watchplay);
		
		PlayConfig config = (PlayConfig)getIntent().getParcelableExtra(PARCELSTRING_PLAYCONFIG_TO_PLAY);
		String playId = config.getId();
		int dialogueId = getIntent().getIntExtra(PARCELSTRING_PLAYCONFIG_DIALOGUEID, 1);
		playDirector = new PlayDirector(this, playId, dialogueId);
		
		tv_dialog = (TextView)findViewById(R.id.tv_wp_dialogue);
		tv_dialog.setText(playDirector.getCurrentDialogue().getText());
		
		ImageButton im_prev = (ImageButton)findViewById(R.id.imageButton_prev);
		ImageButton im_play = (ImageButton)findViewById(R.id.imageButton_play);
		ImageButton im_next = (ImageButton)findViewById(R.id.imageButton_next);
		
		layoutNarrative = (LinearLayout)findViewById(R.id.watchLayoutNarrative);
		layoutDialogue = (LinearLayout)findViewById(R.id.watchLayoutDialogue);
		
		imageView = (ImageView)findViewById(R.id.imageView1);
		imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); //NOTE: This HAS to be set on image view to render pictures
		
		Dialogue currDialogue = playDirector.getCurrentDialogue();
		if(currDialogue!=null && currDialogue.getActorSeqId()>0){
			layoutNarrative.setVisibility(View.GONE);
			layoutDialogue.setVisibility(View.VISIBLE);
			Picture pic = playDirector.getCurrentPicture();
			if(pic!=null) {
				Drawable drawable = new PictureDrawable(pic);
				imageView.setImageDrawable(drawable);
				/*
				 * Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg_main);
				 * Config config = bm.getConfig();
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
				 */
			}
			//Display dialogue
			TextView textViewDialogue = (TextView) findViewById(R.id.tv_wp_dialogue);
			textViewDialogue.setText(currDialogue.getText());
		}
		else {
			layoutDialogue.setVisibility(View.GONE);
			layoutNarrative.setVisibility(View.VISIBLE);
			//Display narrative text
			TextView textViewNarrative = (TextView) findViewById(R.id.tv_wp_narrative);
			textViewNarrative.setText(currDialogue.getText());
		}
		
		if(playDirector.hasNext()) im_next.setEnabled(true);
		if(playDirector.hasPrevious()) im_prev.setEnabled(true);
		
		im_prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		im_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
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
	
}
