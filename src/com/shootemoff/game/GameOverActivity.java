package com.shootemoff.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shootemoff.shootemoffgame.R;

public class GameOverActivity extends Activity 
{

	public int game_score;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		
		Bundle b = getIntent().getExtras();
		
		String score_string = b.getString("score");
		score_string = score_string.replaceAll("\\D+","");
		game_score = Integer.parseInt(score_string);
		
		TextView t = (TextView)findViewById(R.id.game_score); 
		//format score into time
		int minutes = game_score/100;
		int seconds = game_score - (100 * minutes);
		score_string = minutes + ":";
		if(seconds < 10){
			score_string += "0";
		}
		score_string += seconds;
		
		t.setText(score_string);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}

	/**
	 * @param view
	 */
	public void StoreScore(View view)
	{
		TextView t = (TextView)findViewById(R.id.username); 
		String username = t.getText().toString();
		
		Resources res = getResources();
		
		// gathering the predefined variable names
		String[] scores = res.getStringArray(R.array.scores_array);
		String[] score_names = res.getStringArray(R.array.score_names_array);

		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		//read predefined values, used to check if read resaurce is valid
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
				//saving name -> values pairs, easier to manipulate when sorting and saving afterwards
				handler.AddScore(name, score);
			}
			else{
				// read until all the values are stored, array might not be full
				break;
			}
		}
		
		//score need to be higher than already saved to be considered valid, or there are no enough scores
		boolean isScoreAdded = handler.AddScore(username, game_score);
		
		if(isScoreAdded == true){
			
			SharedPreferences.Editor editor = sharedPref.edit();
			
			int size = handler.GetSize();
			ScoreObject[] scores_to_save = handler.GetHighScores();
			//distribute the sorted values in the correct sequence
			//TODO: should add date parameter so the oldest values stay higher
			for(int i = 0; i < size; i++){
				editor.putInt(scores[i], scores_to_save[i].score);
				editor.putString(score_names[i], scores_to_save[i].name);
			}
			
			editor.commit();
			
			Toast.makeText(
					GameOverActivity.this,
					"New Score saved",
				    Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(
					GameOverActivity.this,
					"Sorry! Your score is too low. You suck!",
				    Toast.LENGTH_LONG).show();
		}

		//finish by showing the scoreboard either way score was saved or not
		Intent intent = new Intent (this, ScoreBoardActivity.class);
		startActivity(intent);
	}
	
	public void onBackPressed(){
		super.onBackPressed();
	}

}
