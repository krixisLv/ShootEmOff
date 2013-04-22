package com.shootemoff.game;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class StorageHandler
{
	private ScoreObject[] theScores = new ScoreObject[10];
	private int size = 0;
	
	public StorageHandler()
	{
		//nothing to do
	}
	
	// Implementing example from http://www.mysamplecode.com/2012/06/android-internal-external-storage.html
	public void ReadScores(FileInputStream fis)
	{
		try{
			DataInputStream in = new DataInputStream(fis);
		    BufferedReader buffer = 
		     new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    
		    int number = 0;
		    while ((strLine = buffer.readLine()) != null) {
		    	ScoreObject scoreEntry = new ScoreObject();
		    	for(Integer i = 0; i < 2; i++){
		    		if(i == 0){
		    			scoreEntry.name = strLine;
		    		}
		    		else{
		    			scoreEntry.score = Integer.parseInt(strLine);
		    		}
		    	}
		    	theScores[number++] = scoreEntry;
		    }
		    in.close();
		    size = theScores.length;
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public int GetSize()
	{
		return size;
	}
	
	public boolean AddScore(String username, int game_score)
	{
		boolean scoreAdded = false;
		
		ScoreObject newEntry = new ScoreObject(username, game_score);
//		if(size == 0){
			theScores[0] = newEntry;
			size++;
			scoreAdded = true;
//		}
//		else{
//			ScoreObject tmp = newEntry;
//			for(int i = 0; i < size; i++){
//				//need to loop through the hashMap keep it sorted and insert the new value
//				if(tmp.score > theScores[i].score){
//					ScoreObject local = theScores[i];
//					theScores[i] = tmp;
//					tmp = local;
//					scoreAdded = true;
//				}
//				if(size < 10){
//					theScores[size++] = tmp;
//					scoreAdded = true;
//				}
//			}
//		}
		
		return scoreAdded;
	}
	
	public void WriteScores(FileOutputStream fos)
	{
		try{
			String myInputText = "";
			for(int i = 0; i < size; i++){
				//format the data into one string with line breaks
				myInputText += theScores[i].name + "\n" + theScores[i].score + "\n";
			}
			
			fos.write(myInputText.toString().getBytes());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public ScoreObject[] GetHighScores()
	{
		return theScores;
	}

}