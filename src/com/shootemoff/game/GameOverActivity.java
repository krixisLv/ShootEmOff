package com.shootemoff.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
		game_score = Integer.parseInt(score_string);
		
		TextView t = (TextView)findViewById(R.id.game_score); 
		t.setText(score_string);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}

	public void StoreScore(View view)
	{
		TextView t = (TextView)findViewById(R.id.username); 
		String username = t.getText().toString();
		
		StorageHandler handler = new StorageHandler();
		
		String FILENAME = "score_file";

		try{
			//FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_APPEND);
			FileInputStream fis = new FileInputStream(FILENAME);
			handler.ReadScores(fis);
			//fis.write(string.getBytes());
			fis.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		boolean isScoreAdded = handler.AddScore(username, game_score);
		
		if(isScoreAdded == true){
			Toast.makeText(
					GameOverActivity.this,
					FILENAME + " saved",
				    Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(
					GameOverActivity.this,
					"Sorry! Your score is too low. You suck!",
				    Toast.LENGTH_LONG).show();
		}
		
		try{
			FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			handler.WriteScores(fos);
			fos.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		ScoreObject[] scores = handler.GetHighScores();
		Intent intent = new Intent (this, ScoreBoardActivity.class);
		int size = handler.GetSize();
		
		for (int i = 0; i < size; i++) {
			String name = "score_" + i;
		    intent.putExtra(name, scores[i]);
		}
		intent.putExtra("score_count", size);
		startActivity(intent);
	}
	
	public void onBackPressed(){
		//Nothing to do
		super.onBackPressed();
	}
	
	
}
