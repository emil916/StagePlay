package com.example.stageplayapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.example.stageplayapp.helpers.StagePlayDbHelper;
import com.example.stageplayapp.models.Dialogue;

public class PlayWatchActivity extends Activity{
	public static final String TAG = "PlayWatchActivity";
	public static final String PARCELSTRING_PLAYID_TO_PLAY = "playConfigToPlay";
	public static final String PARCELSTRING_DIALOGUEID_TO_RESUME = "playConfigDialogueId";
	
	TextView tv_dialogue;
	LinearLayout layoutDialogue;
	RelativeLayout layout_main, layoutNarrative;
	ImageView imageView;
	ImageButton im_prev, im_play, im_next;
	PlayDirector playDirector;
	AsyncTask<String, Void, Integer> myTask = null;
	ProgressBar progressBar;
	
	long transitionTime;
	boolean isSlideshowOn;
	boolean isPlaying = true;
	boolean isNightMode = false;
	
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if (playDirector.hasNext()) {
				playDirector.moveToNextDialogue();
				render();
				
				handler.postDelayed(this, transitionTime);
			} 
		}
	};

	@Override 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Programmatically hide the title bar
//	     requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getActionBar().hide();
		
		SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		
		isNightMode = sharedPrefs.getBoolean("prefNightMode", false);
		this.setTheme(isNightMode ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
		
		setContentView(R.layout.activity_watchplay);
		
		String playId = getIntent().getStringExtra(PARCELSTRING_PLAYID_TO_PLAY);
		int dialogueId = getIntent().getIntExtra(PARCELSTRING_DIALOGUEID_TO_RESUME, 1);
	
		tv_dialogue = (TextView)findViewById(R.id.tv_wp_dialogue);
		
		im_prev = (ImageButton)findViewById(R.id.imageButton_prev);
		im_play = (ImageButton)findViewById(R.id.imageButton_play);
		im_next = (ImageButton)findViewById(R.id.imageButton_next);
		
		layoutNarrative = (RelativeLayout)findViewById(R.id.watchLayoutNarrative);
		layoutDialogue = (LinearLayout)findViewById(R.id.watchLayoutDialogue);
		layout_main = (RelativeLayout)findViewById(R.id.RelativeLayout_wp);
		
		imageView = (ImageView)findViewById(R.id.imageView_stickfigure);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		
		isSlideshowOn = sharedPrefs.getBoolean("prefSlideshowMode", true);
		transitionTime = Long.parseLong(sharedPrefs.getString("prefTransitionTime", "5000"));
		
		init_listeners();
		
		myTask = new MyAsynchTask().execute(playId, Integer.toString(dialogueId));
	}
	
	
	private void render() {
		Dialogue currDialogue = playDirector.getCurrentDialogue();
		if(currDialogue!=null && currDialogue.getActorSeqId()>0){
			layoutNarrative.setVisibility(View.GONE);
			layoutDialogue.setVisibility(View.VISIBLE);
			Picture pic = playDirector.getCurrentPicture();
			if (pic != null) {
				int width = pic.getWidth();
				int height = pic.getHeight();
				Bitmap newImage = Bitmap.createBitmap(width * 2, height, Config.ARGB_8888);
				Canvas c = new Canvas(newImage);
				c.drawPicture(pic, new Rect(width, 0, width * 2, height));
				Paint paint = new Paint();
				paint.setColor(isNightMode ? Color.WHITE : Color.BLACK);
				paint.setStyle(Style.FILL);
				paint.setTypeface(Typeface.create(Typeface.DEFAULT,	Typeface.BOLD_ITALIC));
				paint.setTextSize(60);
				c.drawText(playDirector.getCurrentDialogue().getActorName(), 20, 200, paint);

				imageView.setImageBitmap(newImage);
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
		
		updateProgress(currDialogue.getDialogueId(), playDirector.getMaxDialogueId());
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
					if (isPlaying) {
						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, transitionTime);
					}
				}
		    }
			
			@Override
			public void onSwipeRight() {
				if (playDirector.hasPrevious()) {
					playDirector.moveToPreviousDialogue();
					render();
					if (isPlaying) {
						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, transitionTime);
					}
				}
		    }
		});
		
		im_prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (playDirector.hasPrevious()) {
					playDirector.moveToPreviousDialogue();
					render();
					if (isPlaying) {
						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, transitionTime);
					}
				}
			}
		});
		
		im_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isPlaying){
					handler.removeCallbacks(runnable);
					im_play.setImageResource(isNightMode ? 
							R.drawable.play_night : R.drawable.play);
					isPlaying = false;
				}else {
					handler.postDelayed(runnable, transitionTime);
					im_play.setImageResource(isNightMode ? 
							R.drawable.pause_night : R.drawable.pause);
					isPlaying = true;
				}
			}
		});
		
		im_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (playDirector.hasNext()) {
					playDirector.moveToNextDialogue();
					render();
					if (isPlaying) {
						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, transitionTime);
					}
				} 
			}
		});
	}
	

	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacks(runnable);
		if(myTask != null && myTask.getStatus()==Status.RUNNING)
			myTask.cancel(true);
		savePrefs();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(!isSlideshowOn) {
			handler.removeCallbacks(runnable);
			im_play.setVisibility(View.INVISIBLE);
			isPlaying = false;
		} 
		
//		render();
		if(myTask != null && myTask.getStatus() == Status.FINISHED && isSlideshowOn && isPlaying) {
			handler.removeCallbacks(runnable);
			handler.postDelayed(runnable, transitionTime);
		}
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
	
	private class MyAsynchTask extends AsyncTask<String, Void, Integer> {
		private ProgressDialog mDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			new ProgressDialog(PlayWatchActivity.this);
			mDialog = new ProgressDialog(PlayWatchActivity.this, 
					isNightMode ? R.style.MyDialogTheme_Dark 
							: R.style.MyDialogTheme_Light);
			mDialog.setMessage("Please wait...");
			mDialog.show();
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			int dialogueId = Integer.parseInt(params[1]);
			playDirector = new PlayDirector(PlayWatchActivity.this, params[0], 
					dialogueId);
	        updateProgress(dialogueId, playDirector.getMaxDialogueId());
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) { 
			super.onPostExecute(result);
			render();
	        if(mDialog!=null && mDialog.isShowing()) {
	            mDialog.dismiss();
	        }

	        if(isSlideshowOn)
	        	handler.postDelayed(runnable, transitionTime);
		}
	}
	
	private void updateProgress(int cur, int max) {
		progressBar.setProgress((int) (100*(cur*1.0/max)));
	}
}
