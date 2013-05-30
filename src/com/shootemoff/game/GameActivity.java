package com.shootemoff.game;

import android.content.Intent;

import com.shootemoff.framework.Screen;
import com.shootemoff.framework.impl.AndroidGame;


public class GameActivity extends AndroidGame 
{
    @Override
    public Screen getStartScreen() 
    {
    	OptionsObject options = GetGameOptions();
    	
        return new GameScreen(this, options); 
    }
    
    public void finishActivity(String score)
    {

    	Intent returnIntent = new Intent();
		returnIntent.putExtra("result",score);
		
		setResult(RESULT_OK, returnIntent);

		finish();
    }
    
    private OptionsObject GetGameOptions()
    {
    	OptionsObject options = new OptionsObject();
    	
    	return options;
    }
    
    public void onBackPressed(){
    	super.onBackPressed();
	}
}

