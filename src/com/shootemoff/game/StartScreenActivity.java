package com.shootemoff.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shootemoff.shootemoffgame.R;

public class StartScreenActivity extends Activity
{
	
	final static int REQ_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_screen);
		
		//find views to customize
		Button button_play = (Button)findViewById(R.id.button_play);
		Button button_score = (Button)findViewById(R.id.button_score);
		Button button_settings = (Button)findViewById(R.id.button_settings);
		TextView start_text = (TextView) findViewById(R.id.start_text);
		
		//set custom design
		button_play.setBackgroundResource(R.drawable.start_button);
		button_score.setBackgroundResource(R.drawable.start_button);
		button_settings.setBackgroundResource(R.drawable.start_button);
		
		//assign font to all the elements on start screen
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "bay6.ttf");
		button_play.setTypeface(myTypeface);
		button_score.setTypeface(myTypeface);
		button_settings.setTypeface(myTypeface);
		start_text.setTypeface(myTypeface);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_screen, menu);
		return true;
	}
	
	public void StartGame(View view)
	{
		Intent intent = new Intent (this, GameActivity.class);
		startActivityForResult(intent, REQ_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		String gameScore = "";
		
		if(requestCode == REQ_CODE){
			if (resultCode == RESULT_OK){
				
				gameScore =  data.getStringExtra("result");
				
				//just in case, check if the score is high enough
				String score_string = gameScore.replaceAll("\\D+","");
				int score = Integer.parseInt(score_string);

				if(!IsScoreWorthSaving(score)){
					Toast.makeText(
						StartScreenActivity.this,
						"Sorry! Your score is too low. You suck!",
						Toast.LENGTH_LONG).show();
			
					Intent intent = new Intent (this, ScoreBoardActivity.class);
					startActivity(intent);
				}
				else{
					Intent intent = new Intent (this, GameOverActivity.class);
					intent.putExtra("score", gameScore);
					startActivity(intent);
				}
			}
		}
	}
	
	
	/**
	 * @param score
	 * @return boolean is the score high enough to save it in the shared preferences
	 */
	public boolean IsScoreWorthSaving(int score)
	{
		boolean isWorthSaving = false;
		
		Resources res = getResources();
		String[] scores = res.getStringArray(R.array.scores_array);
		
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		String defaultValueString = res.getString(R.string.saved_score_default);
		int defaultValue = Integer.parseInt(defaultValueString);
		
		String max_scores_saved_string = res.getString(R.string.max_scores_saved);
		int max_scores_saved = Integer.parseInt(max_scores_saved_string);
		
		//just need to know if there is enough free space or this time is high enough
		for(int i = 0; i < max_scores_saved; i++){
			int tmp_score = sharedPref.getInt(scores[i], defaultValue);
			if(tmp_score != defaultValue){
				if(tmp_score < score){
					isWorthSaving = true;
					break;
				}
			}
			else {
				isWorthSaving = true;
				break;
			}
		}
		
		return isWorthSaving;
	}
	
	public void ShowScoreBoard(View view)
	{
		Intent intent = new Intent (this, ScoreBoardActivity.class);
		startActivity(intent);
	}
	
	public void ShowSettings(View view)
	{
		Intent intent = new Intent (this, SettingsActivity.class);
		startActivity(intent);
	}

	public void onBackPressed(){
		//Nothing to do
		super.onBackPressed();
	}
}
