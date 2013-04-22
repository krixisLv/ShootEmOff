package com.shootemoff.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.shootemoff.shootemoffgame.R;

public class StartScreenActivity extends Activity
{
	
	final static int REQ_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_screen);
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
				//gameScore = data.getExtra ("result", 0);
				gameScore =  data.getStringExtra("result");
				
				Intent intent = new Intent (this, GameOverActivity.class);
				intent.putExtra("score", gameScore);
				startActivity(intent);
			}
		}
	}
	
	public void ShowScoreBoard(View view)
	{
		Intent intent = new Intent (this, ScoreBoardActivity.class);
		startActivity(intent);
	}

	public void onBackPressed(){
		//Nothing to do
		super.onBackPressed();
	}
}
