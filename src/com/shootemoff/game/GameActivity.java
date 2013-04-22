package com.shootemoff.game;

import android.content.Intent;

import com.shootemoff.framework.Screen;
import com.shootemoff.framework.impl.AndroidGame;


public class GameActivity extends AndroidGame 
{
    @Override
    public Screen getStartScreen() 
    {
        return new GameScreen(this); 
    }
    
    public void finishActivity(String score)
    {
    	Intent returnIntent = new Intent();
		returnIntent.putExtra("result",score);
		setResult(RESULT_OK, returnIntent);     
		finish();
    }
    
    public void onBackPressed(){
    	//Your code here
    	super.onBackPressed();
	}
}

