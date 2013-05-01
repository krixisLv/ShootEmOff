package com.shootemoff.game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shootemoff.shootemoffgame.R;

public class ScoreBoardActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_board);
		
		Resources res = getResources();
		String[] scores = res.getStringArray(R.array.scores_array);
		String[] score_names = res.getStringArray(R.array.score_names_array);
		
		StorageHandler handler = new StorageHandler();

		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		String defaultValueString = res.getString(R.string.saved_score_default);
		int defaultValue = Integer.parseInt(defaultValueString);
		String defaultName = res.getString(R.string.saved_score_name_default);
		
		String max_scores_saved_string = res.getString(R.string.max_scores_saved);
		int max_scores_saved = Integer.parseInt(max_scores_saved_string);
		for(int i = 0; i < max_scores_saved; i++){
			//String  score1 = sharedPref.getString(scores[i], defaultValueString);
			int score = sharedPref.getInt(scores[i], defaultValue);
			//int score = Integer.parseInt(score1);
			if(score != defaultValue){
				String name = sharedPref.getString(score_names[i], defaultName);
				handler.AddScore(name, score);
			}
			else{
				break;
			}
		}
		
		TextView text = (TextView)findViewById(R.id.header);
		int size = handler.GetSize();
		ListView scoreListView = (ListView)findViewById(R.id.list);
		String[] scoreArray = new String[size];
//		
//		if(size > 0){
//			text.setText("there is some score!");
//		}
		

		
		ScoreObject[] scores_from_file = handler.GetHighScores();
		
		for(int i = 0; i < size; i++){
			String score_line;
			if(scores_from_file[i].score < 100){
				score_line = "NAME : " + scores_from_file[i].name + " SCORE : " + scores_from_file[i].score ;
			}
			else{
				int minutes = (scores_from_file[i].score / 100);
				int seconds = scores_from_file[i].score - (minutes * 100);
				score_line = "NAME : " + scores_from_file[i].name + " SCORE : " + minutes + ":" + seconds;
			}
			scoreArray[i] = score_line;
		}
		
		// Create and populate a List of names and scores.
		ArrayList<String> planetList = new ArrayList<String>();  
	    planetList.addAll( Arrays.asList(scoreArray) );
		
	    // Create ArrayAdapter using the planet list.  
	    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
	    
	    scoreListView.setAdapter( listAdapter );
	    
//		SavedFiles = getApplicationContext().fileList();
//		   ArrayAdapter<String> adapter
//		   = new ArrayAdapter<String>(this,
//		     android.R.layout.simple_list_item_1,
//		     SavedFiles);
//
//	   listSavedFiles.setAdapter(adapter);
//	   
//	   if(SavedFiles.length > 0){
//		   text.setText("1");
//	   }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_board, menu);
		return true;
	}
	
	public void BackToMenu(View view)
	{
		Intent intent = new Intent (this, StartScreenActivity.class);
		startActivity(intent);
	}
	
	public void onBackPressed(){
		//Nothing to do
		super.onBackPressed();
	}

}
