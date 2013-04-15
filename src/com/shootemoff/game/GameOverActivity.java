package com.shootemoff.game;

import com.shootemoff.shootemoffgame.R;
import com.shootemoff.shootemoffgame.R.layout;
import com.shootemoff.shootemoffgame.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GameOverActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}

}
