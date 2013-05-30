package com.shootemoff.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.SharedPreferences;

import com.shootemoff.shootemoffgame.R;

public class StorageHandler
{
	private ScoreObject[] theScores;
	private int size = 0;
	
	public StorageHandler(int max_size)
	{
		theScores = new ScoreObject[max_size];
	}
	
	public int GetSize()
	{
		return size;
	}
	
	public boolean AddScore(String username, int game_score)
	{
		boolean scoreAdded = false;
		
		ScoreObject newEntry = new ScoreObject(username, game_score);
		if(size == 0){
			theScores[0] = newEntry;
			size++;
			scoreAdded = true;
		}
		else{
			ScoreObject tmp = newEntry;
			for(int i = 0; i < size; i++){
				//need to loop through the hashMap keep it sorted and insert the new value
				if(tmp.score > theScores[i].score){
					ScoreObject local = theScores[i];
					theScores[i] = tmp;
					tmp = local;
					scoreAdded = true;
				}
			}
			if(size < 10){
				theScores[size++] = tmp;
				scoreAdded = true;
			}
		}
		
		return scoreAdded;
	}

	public ScoreObject[] GetHighScores()
	{
		return theScores;
	}

}