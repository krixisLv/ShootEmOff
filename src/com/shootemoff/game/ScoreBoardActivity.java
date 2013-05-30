package com.shootemoff.game;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		String defaultValueString = res.getString(R.string.saved_score_default);
		int defaultValue = Integer.parseInt(defaultValueString);
		String defaultName = res.getString(R.string.saved_score_name_default);
		
		String max_scores_saved_string = res.getString(R.string.max_scores_saved);
		int max_scores_saved = Integer.parseInt(max_scores_saved_string);
		
		StorageHandler handler = new StorageHandler(max_scores_saved);
		
		for(int i = 0; i < max_scores_saved; i++){

			int score = sharedPref.getInt(scores[i], defaultValue);

			if(score != defaultValue){
				String name = sharedPref.getString(score_names[i], defaultName);
				handler.AddScore(name, score);
			}
			else{
				break;
			}
		}
		
		Button back_button = (Button)findViewById(R.id.back_to_menu);
		back_button.setBackgroundResource(R.drawable.start_button);
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "bay6.ttf");
		back_button.setTypeface(myTypeface);
		
		//Typeface myTypeface = Typeface.createFromAsset(getAssets(), "molten.ttf");
		//text.setTypeface(myTypeface);
		
		int size = handler.GetSize();
		ListView scoreListView = (ListView)findViewById(R.id.list);

		String[] scoreArray = new String[size];

		ScoreObject[] scores_from_file = handler.GetHighScores();
		
		for(int i = 0; i < size; i++){
			String score_line;
			
			int minutes = (scores_from_file[i].score / 100);
			int seconds = scores_from_file[i].score - (minutes * 100);
			score_line = (i+1) + ". NAME : " + scores_from_file[i].name + "\t\t\t SCORE : ";
			
			if(minutes < 10){
				score_line += "0";
			}
			score_line += minutes + ":";
			if(seconds < 10){
				score_line += "0";
			}
			score_line += seconds;
			
			scoreArray[i] = score_line;
		}
		
		// Create and populate a List of names and scores.
		ArrayList<String> planetList = new ArrayList<String>();  
	    planetList.addAll( Arrays.asList(scoreArray) );
		
	    // Create ArrayAdapter using the planet list.  
	    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
	    
	    scoreListView.setAdapter( listAdapter );
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
		Intent intent = new Intent (this, StartScreenActivity.class);
		startActivity(intent);
	}

}
