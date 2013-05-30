package com.shootemoff.game;

public class ScoreObject 
{
	ScoreObject(){};
	
	ScoreObject(String new_name, int new_score)
	{
		name = new_name;
		score = new_score;
	}
	
	public String name;
	public int score;
}
