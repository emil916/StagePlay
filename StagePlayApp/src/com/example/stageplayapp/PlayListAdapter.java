package com.example.stageplayapp;
import java.util.*;

import com.example.stageplayapp.models.PlayConfig;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PlayListAdapter extends ArrayAdapter<ArrayList<PlayConfig>>{
Context context;
int layoutResourceId;
ArrayList<PlayConfig> data;

public PlayListAdapter(Context context,int layoutResourceId, ArrayList<PlayConfig> data){
	super(context,layoutResourceId);
	this.context = context;
	this.layoutResourceId = layoutResourceId;
	this.data = data;
}
@Override
public View getView(int position,View convertView,ViewGroup parent){
	Button btn_details, btn_play;
	TextView tv_title, tv_author;
	View row = convertView;
	if(row == null){
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
    
        btn_details = (Button)row.findViewById(R.id.btn_details);
        btn_play = (Button)row.findViewById(R.id.btn_play);
        tv_title = (TextView)row.findViewById(R.id.tv_title);
        tv_author = (TextView)row.findViewById(R.id.tv_author);
        if(data!=null){
    		PlayConfig playconfig = data.get(position);
    		tv_title.setText(playconfig.getName());
    		tv_author.setText(playconfig.getAuthor());
    		btn_details.setText(context.getString(R.string.btn_details_label));
    		btn_play.setText(context.getString(R.string.btn_play_label));
    	}
	}
    
    return row;
}

}

