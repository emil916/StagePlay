package com.example.stageplayapp;
import java.util.*;

import com.example.stageplayapp.helpers.SharedPreferenceHelper;
import com.example.stageplayapp.helpers.StagePlayDbHelper;
import com.example.stageplayapp.models.PlayConfig;
import com.example.stageplayapp.PlayDetailsActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PlayListAdapter extends ArrayAdapter<PlayConfig>{
Context context;
int layoutResourceId;
ArrayList<PlayConfig> data;

public PlayListAdapter(Context context,int layoutResourceId, ArrayList<PlayConfig> data){
	super(context,layoutResourceId,data);
	this.context = context;
	this.layoutResourceId = layoutResourceId;
	this.data = data;
}
@Override
public View getView(int position,View row,ViewGroup parent){
	Button btn_details, btn_play, btn_resume, btn_delete;
	TextView tv_title, tv_author, tv_genre_and_lang;
	
	if(row == null)
	{
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
	}
	
        btn_details = (Button)row.findViewById(R.id.btn_details);
        btn_play = (Button)row.findViewById(R.id.btn_play);
        btn_resume=(Button)row.findViewById(R.id.btn_resume);
        btn_delete = (Button)row.findViewById(R.id.btn_delete);
        tv_title = (TextView)row.findViewById(R.id.tv_title);
        tv_author = (TextView)row.findViewById(R.id.tv_author);
        tv_genre_and_lang = (TextView)row.findViewById(R.id.tv_genre_and_lang);
        
        if(data!=null && data.size()>position){
    		final PlayConfig playconfig = data.get(position);
    		String last_playID = SharedPreferenceHelper.readString(context,
    				SharedPreferenceHelper.PLAY_ID, null);
            
    		 if((last_playID!=null) && (last_playID.equals(playconfig.getId())))
    		 {
    			 btn_resume.setEnabled(true);
    		 }
    		 else
    		 {
    			 btn_resume.setEnabled(false);
    		 }
    		 
    		tv_title.setText(playconfig.getName());
    		tv_author.setText(playconfig.getAuthor());
    		    		
    		
    		String other = playconfig.getGenre() + " • " + playconfig.getLanguage();
    		tv_genre_and_lang.setText(other);
    		
    		btn_details.setTag(playconfig);
    		btn_play.setTag(playconfig);
    		btn_delete.setTag(playconfig);
    		btn_resume.setTag(playconfig);
    		
    		btn_details.setOnClickListener(new View.OnClickListener(){
    			@Override public void onClick(View v){
    				Intent i = new Intent(context, PlayDetailsActivity.class);
    				i.putExtra(PlayDetailsActivity.PARCELSTRING_PLAYCONFIG_TO_DISPLAY_DETAILS, playconfig);
    				context.startActivity(i);
    			}
    		});
    		
    		btn_play.setOnClickListener(new View.OnClickListener(){
    			@Override public void onClick(View v){
    				Intent i = new Intent(context, PlayWatchActivity.class);
    				PlayConfig tag = (PlayConfig)v.getTag();
    				i.putExtra(PlayWatchActivity.PARCELSTRING_PLAYID_TO_PLAY, tag.getId());
    				context.startActivity(i);
    			}
    		});
    		
    		btn_resume.setOnClickListener(new View.OnClickListener(){
    			@Override public void onClick(View v){
    				int dialogueId = SharedPreferenceHelper.readInteger(context,
    						SharedPreferenceHelper.DIALOGUE_ID, 1);
    				Intent i = new Intent(context, PlayWatchActivity.class);
    				PlayConfig tag = (PlayConfig)v.getTag();
    				i.putExtra(PlayWatchActivity.PARCELSTRING_PLAYID_TO_PLAY, tag.getId());
    				i.putExtra(PlayWatchActivity.PARCELSTRING_DIALOGUEID_TO_RESUME, dialogueId);
    				context.startActivity(i);
    			}
    		});
    		
    		btn_delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// Call DB's delete method and pass in playId
					// After delete, refresh the data in this adapter!
					PlayConfig tag = (PlayConfig)v.getTag();
					StagePlayDbHelper dbHelper = new StagePlayDbHelper(context);
					dbHelper.deletePlay(tag.getId());
					
					String last_playID = SharedPreferenceHelper.readString(context,
		    				SharedPreferenceHelper.PLAY_ID, null);
					
					if((last_playID!=null)&&(last_playID.equals(tag.getId())))
					{
						SharedPreferenceHelper.writeString(context, SharedPreferenceHelper.PLAY_ID, null);
					}
					
					data.remove(tag);
					notifyDataSetChanged();
				}
			});
    		
    		if(position%2==0)
    		{
    			row.setBackgroundColor(Color.LTGRAY);
    		}
    		else
    		{
    			row.setBackgroundColor(Color.parseColor("#ECECEC"));
    		}
    	}
	
    
    return row;
}

}

