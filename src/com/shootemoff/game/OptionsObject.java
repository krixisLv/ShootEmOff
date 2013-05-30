package com.shootemoff.game;

/**
 * 
 * @author Kristaps
 *	Class stores user settings. Used when initializing a new game to set up th screen and game experience
 */
public class OptionsObject
{
	public OptionsObject(){}
	
	/**
	 * @param reload - offset between gunshots
	 * @param diff - difficulty level [1 - 3]
	 * @param shield_left - shield position, swapping shield/gun control
	 * @param sound_on - sound effects on collisions
	 * @param vibr_on - vibration effects on collisions
	 */
	public OptionsObject(
			int reload, 
			int diff, 
			int shield_left, 
			int sound,
			int vibration
		)
	{
		gun_reload_time = reload;
		difficulty = diff;
		lefty = shield_left;
		sound_on = sound;
		vibration_on = vibration;
	}
	
	public int GetGunOffset()
	{
		return gun_reload_time;
	}
	
	public void SetGunOffset(int time)
	{
		gun_reload_time = time;
	}
	
	//min time between gunshots
	private int gun_reload_time;
	//novice = 1 up to 5
	private int difficulty;
	//control pad placement
	private int lefty;
	//
	private int sound_on;
	private int vibration_on;
	
}