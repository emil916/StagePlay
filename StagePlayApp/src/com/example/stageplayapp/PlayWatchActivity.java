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
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stageplayapp.helpers.OnSwipeTouchListener;
import com.example.stageplayapp.helpers.PlayDirector;
import com.example.stageplayapp.helpers.SharedPreferenceHelper;
import com.example.stageplayapp.models.Dialogue;
import com.example.stageplayapp.models.PlayConfig;

public class PlayWatchActivity extends Activity{
	public static final String TAG = "PlayWatchActivity";
	public static final String PARCELSTRING_PLAYID_TO_PLAY = "playConfigToPlay";
	public static final String PARCELSTRING_DIALOGUEID_TO_RESUME = "playConfigDialogueId";
	
	TextView tv_dialogue;
	LinearLayout layoutNarrative, layoutDialogue;
	RelativeLayout layout_main;
	ImageView imageView;
	ImageButton im_prev, im_play, im_next;
	PlayDirector playDirector;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watchplay);
		
		String playId = getIntent().getStringExtra(PARCELSTRING_PLAYID_TO_PLAY);
		int dialogueId = getIntent().getIntExtra(PARCELSTRING_DIALOGUEID_TO_RESUME, 1);
		playDirector = new PlayDirector(this, playId, dialogueId);
		
		tv_dialogue = (TextView)findViewById(R.id.tv_wp_dialogue);
		
		im_prev = (ImageButton)findViewById(R.id.imageButton_prev);
		im_play = (ImageButton)findViewById(R.id.imageButton_play);
		im_next = (ImageButton)findViewById(R.id.imageButton_next);
		
		layoutNarrative = (LinearLayout)findViewById(R.id.watchLayoutNarrative);
		layoutDialogue = (LinearLayout)findViewById(R.id.watchLayoutDialogue);
		layout_main = (RelativeLayout)findViewById(R.id.RelativeLayout_wp);
		
		imageView = (ImageView)findViewById(R.id.imageView1);
		//NOTE: This HAS to be set on image view to render pictures
		//imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); 
		
		render();
		init_listeners();
	}
	
	private void render() {
		Dialogue currDialogue = playDirector.getCurrentDialogue();
		if(currDialogue!=null && currDialogue.getActorSeqId()>0){
			layoutNarrative.setVisibility(View.GONE);
			layoutDialogue.setVisibility(View.VISIBLE);
			Picture pic = playDirector.getCurrentPicture();
			if (pic != null) {
				// Drawable drawable = new PictureDrawable(pic);

				int width = pic.getWidth();
				int height = pic.getHeight();
				Bitmap newImage = Bitmap.createBitmap(width * 2, height, Config.ARGB_8888);
				Canvas c = new Canvas(newImage);
				c.drawPicture(pic, new Rect(width, 0, width * 2, height));
				// c.drawBitmap(bm, 0, 0, null);
				Paint paint = new Paint();
				paint.setColor(Color.BLACK);
				paint.setStyle(Style.FILL);
				paint.setTypeface(Typeface.create(Typeface.DEFAULT,	Typeface.ITALIC));
				paint.setTextSize(60);
				c.drawText(playDirector.getCurrentDialogue().getActorName(), 20, 200, paint);

				imageView.setImageBitmap(newImage);
//				imageView.setImageDrawable(drawable);
				
				/*
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
				 */
			}
			
			// Display dialogue text
			tv_dialogue.setText(currDialogue.getText());
		}
		else {
			layoutDialogue.setVisibility(View.GONE);
			layoutNarrative.setVisibility(View.VISIBLE);
			//Display narrative text
			TextView textViewNarrative = (TextView) findViewById(R.id.tv_wp_narrative);
			textViewNarrative.setText(currDialogue.getText());
		}
		
		// Enable/Disable navigation buttons
			im_next.setEnabled(playDirector.hasNext());
			im_prev.setEnabled(playDirector.hasPrevious());
	}
	
	private void init_listeners() {
		
		layout_main.setOnTouchListener(new OnSwipeTouchListener(this) {
			@Override
			public void onSwipeLeft() {
				if (playDirector.hasNext()) {
					playDirector.moveToNextDialogue();
					render();
				}
		    }
			
			@Override
			public void onSwipeRight() {
				if (playDirector.hasPrevious()) {
					playDirector.moveToPreviousDialogue();
					render();
				}
		    }
		});
		
		im_prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (playDirector.hasPrevious()) {
					playDirector.moveToPreviousDialogue();
					render();
				}
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
				if (playDirector.hasNext()) {
					playDirector.moveToNextDialogue();
					render();
				} 
			}
		});
	}
	

	@Override
	public void onPause() {
		super.onPause();
		savePrefs();
	}
	
	/** Called when to save the current field values into SharedPrefences. */
	private void savePrefs() {
		Dialogue curD = playDirector.getCurrentDialogue();
		final String play_id = curD.getPlayId();
		final int dialogue_id = curD.getDialogueId();

		if (play_id != null)
			SharedPreferenceHelper.writeString(this, SharedPreferenceHelper.PLAY_ID,
					play_id);

		if (dialogue_id > 0)
			SharedPreferenceHelper.writeInteger(this, SharedPreferenceHelper.DIALOGUE_ID,
					dialogue_id);

	}
	

	
}
