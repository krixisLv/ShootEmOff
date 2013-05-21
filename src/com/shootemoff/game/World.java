package com.shootemoff.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import android.content.res.Configuration;

import com.shootemoff.framework.Audio;
import com.shootemoff.framework.Game;
import com.shootemoff.framework.Graphics;
import com.shootemoff.framework.Input.KeyEvent;
import com.shootemoff.framework.Input.TouchEvent;
import com.shootemoff.framework.Sound;

/* I should have used pools for my objects not to make garbage
 * collector angry. As it freezes the game sometimes, 
 * I avoided some object creations. However, it doesn't help.
 */

public class World
{
	Random random = new Random();
	Game game;
	private final int DOTS_COUNT = 15;
	private final int GUN_SHOTS_COUNT = 10;
	// In this case ArrayList is better than LinkedList:
	// list will never be resized.
	public List<Dot> dots = new ArrayList<Dot>(DOTS_COUNT);
	
	public List<Dot> shots = new ArrayList<Dot>(GUN_SHOTS_COUNT);
	
	public Core core = new Core();
	public ControlPad shieldControl = new ControlPad();
	public ControlPad gunControl = new ControlPad();
	public float offScreenRadius;
	private final float SHIELD_HEALTH = 20.0F;
	private final float CORE_HEALTH = 6.0F;
	//private final int[] ALLOWED_ANGLES = {110, 330, 10, 180, 350, 300, 30, 200, -20, 100, 50, 320, 270};
	private final int[] ALLOWED_ANGLES = {30, 200, -20, 100, 50, 320, 270, 0, 70, 340, 45, 20, 190, -80};
   	
	public boolean just_aimed = false;
	public double aim_angle;
	public VectorF aim_beam;
	
	public float shield_top_coef = (float)0.65;//0.05;
	public float shield_bottom_coef = (float)0.95;//0.35;
	public float shield_right_coef = (float)0.30;
	public float shield_left_coef = (float)0.06;
	public float shield_top;
	public float shield_bottom;
	public float shield_right;
	public float shield_left;
	
	public float gun_top_coef = (float)0.05;//0.65;
	public float gun_bottom_coef = (float)0.35;//0.95;
	public float gun_right_coef = (float)0.30;
	public float gun_left_coef = (float)0.06;
	public float gun_top;
	public float gun_bottom;
	public float gun_right;
	public float gun_left;
	
	//default base speed
	float base_speed;
	
	private float time = 0.0F; // in seconds
	private float last_shot_time = 0.0F;

	public enum GameState {Ready, Running, Paused, GameOver}

	public GameState state = GameState.Ready;

	private float difficulty = 0.04F; // Max 0.1F

	// Sounds
	// Dot collides with core
	Sound coreHurt;
	Sound coreHealth;
	Sound coreShield;
	// Dot collides with shield
	Sound shieldCollision;

	Sound gameOver;
	Sound gunShot;
	Sound shieldMove;
	Sound gunShotCollision;
	Sound gameOn;

	public World(Game game, int screen_layout)
	{
		this.game = game;
		Graphics g = game.getGraphics();
		
		// Construct core
		core.coords = new VectorF((float) g.getWidth() / 4,
			   	(float) g.getHeight() / (float) 2);
		core.shieldRadius = (float) g.getWidth() / 4;
		core.maxRadius = core.shieldRadius * 0.7F;
		core.angle = 45.0F;
		core.health = 1.0F;
		core.shieldEnergy = 0.0F;
		
		//Construct shield arc
		shieldControl.angle = 45.0F;
		shieldControl.coords = new VectorF( (float)(g.getWidth() * 0.13),(float)(g.getHeight() * 0.80) );
		shieldControl.ARC_RADIUS = (float)(g.getHeight() * 0.15);
		shieldControl.SHIELD_RADIUS = (float)(shieldControl.ARC_RADIUS * 0.9F);
		
		//Construct gun arc
		gunControl.coords = new VectorF( (float)(g.getWidth() * 0.13),(float)(g.getHeight() * 0.20) );
		gunControl.ARC_RADIUS = (float)(g.getHeight() * 0.15);
		gunControl.SHIELD_RADIUS = (float)(gunControl.ARC_RADIUS * 0.9F);
		
		// Set offScreenRadius
		offScreenRadius = (float) Math.hypot((double) g.getWidth() / 2,
				(double) g.getHeight() / (float) 1.4);
		
		// Max dot radius (when it's energy is 1.0F)
		Dot.maxRadius = core.maxRadius / 8.0F;
		
		DetermineBaseSpeed(screen_layout);
		loadSounds();
	}
	
	//return 1-small, 2-normal, 3-large, 2-other sizes
	private int DetermineScreenSize(int screen_layout)
	{
		int screen_size;
		if ( (screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK ) == Configuration.SCREENLAYOUT_SIZE_LARGE ) {     
	        screen_size = 3;
	    }
	    else if ( (screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK ) == Configuration.SCREENLAYOUT_SIZE_NORMAL ) {     
	    	screen_size = 2;
	    } 
	    else if ( (screen_layout & Configuration.SCREENLAYOUT_SIZE_MASK ) == Configuration.SCREENLAYOUT_SIZE_SMALL ) {     
	    	screen_size = 1;
	    }
	    else {
	    	screen_size = 2;
	    }
		
		return screen_size;
	}
	
	private void DetermineBaseSpeed(int screen_layout)
	{
		int screen_size = DetermineScreenSize(screen_layout);
		switch(screen_size){
			case 1:
				this.base_speed = 5F;
				break;
			case 2:
				this.base_speed = 10F;
				break;
			case 3:
				this.base_speed = 15F;
				break;
			default:
				this.base_speed = 10F;
		}
		
	}
	
	private void loadSounds()
	{
		Audio a = game.getAudio();
		coreHurt = a.newSound("core_hurt.wav");
		coreHealth = a.newSound("core_health.wav");
		coreShield = a.newSound("core_shield.wav");
		shieldCollision = a.newSound("clash_02.wav");
		gameOver = a.newSound("game_over.wav");
		gunShot = a.newSound("laser_shot.wav");
		shieldMove = a.newSound("shield_move.wav");
		gunShotCollision = a.newSound("clash_01.wav");
		gameOn = a.newSound("saber_on.wav");
	}

	// Restart the game	
	public void renew()
	{
		dots.clear();
		core.health = 1.0F;
		core.shieldEnergy = 0.0F;
		time = 0.0F;
		last_shot_time = 0.0F;
		state = GameState.Ready;
		difficulty = 0.04F;
		generateStartDots(DOTS_COUNT);
	}
	// Add randomness
	private void generateStartDots(int count)
	{
		for(int i = 0; i < count; i++)
		{
			generateNewDot(true);
		}
	}

	private VectorF generateNewDotCoordsInRandomPlace()
	{
		//should stick to predefined angles to make the enemies can from above
		int index = random.nextInt(ALLOWED_ANGLES.length -1 );
		int angle = ALLOWED_ANGLES[ index ];
		
		VectorF coords = new VectorF((float) Math.cos(angle), 
				(float) Math.sin(angle));
		
		coords = coords.multiply(core.shieldRadius + (offScreenRadius -
					core.shieldRadius) * random.nextFloat());
		return coords;
	}

	public void update(float deltaTime)
	{
		if(state == GameState.Ready)
			updateReady(deltaTime);
		if(state == GameState.Running)
			updateRunning(deltaTime);
		if(state == GameState.Paused)
			updatePaused(deltaTime);
		if(state == GameState.GameOver)
			updateGameOver(deltaTime);
	}

	private void doInput()
	{
		if(game.getInput().isTouchDown()){
			
			double touchX = (double) game.getInput().getTouchX();
			double touchY = (double) game.getInput().getTouchY();
			//shield pad controls
			if(touchY >= shield_left && touchX <= shield_right &&
					touchY >= shield_top && touchY <= shield_bottom){
				
				float shield_pad_width = shield_bottom - shield_top;
				float touchPoint = (float)((touchY - shield_top) / shield_pad_width);
				
				core.angle = (270 - (200 * touchPoint)) + 200;
				shieldControl.angle = core.angle;
				shieldMove.play(20);
			}
			else if( touchY >= gun_left && touchX <= gun_right &&
						touchY >= gun_top && touchY <= gun_bottom ){

				//double shot_offset = (int)(time - last_shot_time);
				if(shots.size() < GUN_SHOTS_COUNT){
					
					float gun_pad_width = gun_bottom - gun_top;
					float touchPoint = (float)((touchY - gun_top) / gun_pad_width);
					aim_angle = (270 - (200 * touchPoint)) + 200 + 90;
					double radians = Math.toRadians(aim_angle);
					
					float current_radius = core.maxRadius * 3;
					double x_coord = core.coords.x + (Math.sin(radians) * current_radius);
					double y_coord = core.coords.y + (Math.cos(radians) * current_radius);
					aim_beam = new VectorF((float)x_coord, (float)y_coord);
					just_aimed = true;
					last_shot_time = time;
				}
				/*
					// Y-axis is inverted. See checkCollisionWithShield(...)
					// method
					normAngle(((float) (Math.atan2(-(touchY - core.coords.y), 
							touchX - core.coords.x) / (Math.PI * 2) *
					360.0)) - Core.GAP_ANGLE/2F);
				*/
			}
		}
		//remember the aiming angle and sheooooouut...
		if(just_aimed == true && last_shot_time < time){
			double radians = Math.toRadians(aim_angle);
			
			float current_radius = core.maxRadius * core.health;
			double x_coord = core.coords.x + (Math.sin(radians) * current_radius);
			double y_coord = core.coords.y + (Math.cos(radians) * current_radius);
			
			gunShot.play(40);
			generateNewGunShot(x_coord, y_coord, radians);
			just_aimed = false;
		}
		
	}

	private void updateReady(float deltaTime)
	{
		if(checkTouchUp() || checkMenuUp())
			state = GameState.Running;
	}
	
	private boolean checkTouchUp()
	{
		for(TouchEvent event : game.getInput().getTouchEvents())
		{
			if(event.type == TouchEvent.TOUCH_UP)
				return true;
		}
		return false;
	}

	private boolean checkMenuUp()
	{
		for(KeyEvent event : game.getInput().getKeyEvents())
		{
			if(event.keyCode == android.view.KeyEvent.KEYCODE_MENU)
			{
				if(event.type == KeyEvent.KEY_UP)
					return true;
			}

		}
		return false;
	}

	private void updatePaused(float deltaTime)
	{
		if(checkTouchUp() || checkMenuUp())
			state = GameState.Running;
	}

	private void updateGameOver(float deltaTime)
	{
		if(checkTouchUp() || checkMenuUp())
			renew();
	}

	private void updateRunning(float deltaTime)
	{
		checkTouchUp(); // Just to clear touch event buffer

		if(checkMenuUp())
			state = GameState.Paused;

		countTime(deltaTime);

		doInput();

		generateNewDots(DOTS_COUNT);

		handleShotPositions();
		handleCollisions();
		moveDots(deltaTime);
		moveShots(deltaTime);
		decreaseShieldEnergy(deltaTime);
	}

	private void decreaseShieldEnergy(float deltaTime)
	{
		if(core.shieldEnergy > 0.0F)
		{
			core.shieldEnergy -= deltaTime / SHIELD_HEALTH;
			if(core.shieldEnergy < 0.0F)
			core.shieldEnergy = 0.0F;
		}
	}

	private void generateNewDots(int neededCount)
	{
		if(neededCount > dots.size())
			generateNewDot(false);
	}

	private void increaseDifficulty()
	{
		difficulty += 0.00005F;
	}
	
	private void generateNewGunShot(double x, double y, double radians)
	{
		float linearSpeed = this.base_speed * difficulty;
		Dot gun_shot = new Dot();
		
		gun_shot.coords = new VectorF((float)x, (float)y);
		
		gun_shot.angle_radians = radians;
		
		VectorF speed  = new VectorF(linearSpeed, linearSpeed);
		gun_shot.speed = speed;
		gun_shot.energy = 1.0F;
		//gun_shot.coords.subtractFromThis(core.coords);
		//gun_shot.coords = generateNewDotCoordsInRandomPlace();
		gun_shot.type = Dot.Type.Gunshot;
		
		shots.add(gun_shot);
	}

	private void generateNewDot(boolean atStart)
	{
		float linearSpeed = this.base_speed * difficulty;
		Dot dot = new Dot();
		if(atStart)
		{
			dot.coords = generateNewDotCoordsInRandomPlace();
		}
		else
		{
			dot.coords = generateNewDotAtOffScreenRadius();
			increaseDifficulty();
		}
		VectorF speed = new VectorF(
				linearSpeed * (-dot.coords.x / dot.coords.length()),
			   linearSpeed * (-dot.coords.y / dot.coords.length()));
		dot.speed = speed;
		dot.coords.addToThis(core.coords);
		dot.energy = random.nextFloat();
		if(dot.energy <= 0.3F)
			dot.energy = 0.3F;
		float typeRand = random.nextFloat();
		Dot.Type type;
		if (typeRand >= 0.8){
			type = Dot.Type.Health;
		}
		else{
			type = Dot.Type.Enemy;
		}
		dot.type = type;
		dots.add(dot);
	}

	private void countTime(float deltaTime)
	{
		time += deltaTime;
	}

	public String getTime()
	{
		int seconds = (int) time;
		int minutes = seconds / 60;
		seconds %= 60;
		String result = "";
		if(minutes > 0)
			result += minutes + ":";
		result += String.format("%02d", seconds);
		return result;
	}

	private VectorF generateNewDotAtOffScreenRadius()
	{
		//should stick to predefined angles to make the enemies can from above
		int index = random.nextInt(ALLOWED_ANGLES.length -1 );
		int angle = ALLOWED_ANGLES[ index ];
		
		VectorF coords =
		   	new VectorF(offScreenRadius * ((float) Math.cos(angle)),
					offScreenRadius * ((float) Math.sin(angle)));
		
		return coords;
	}
	
	private void moveShots(float deltaTime)
	{
		for(Dot dot : shots)
		{
			dot.coords.addToThis((float)(dot.speed.x * deltaTime * 100.0F * Math.sin(dot.angle_radians)),
					(float)(dot.speed.y * deltaTime * 100.0F * Math.cos(dot.angle_radians)));
			
			
		}
	}
	
	private void moveDots(float deltaTime)
	{
		for(Dot dot : dots)
		{
			dot.coords.addToThis(dot.speed.x * deltaTime * 100.0F,
					dot.speed.y * deltaTime * 100.0F);
		}
	}
	
	private void handleShotPositions()
	{
		Iterator<Dot> iterator = shots.iterator();
		while(iterator.hasNext())
		{
			handleShotPosition(iterator.next(), iterator);
		}
	}
	
	private void handleShotPosition(Dot shot, Iterator<Dot> iterator)
	{
//		float lengthToCoreCenter = (float)Math.hypot(
//				(double)(shot.coords.x - core.coords.x),
//				(double)(shot.coords.y - core.coords.y));
		
		Graphics g = game.getGraphics();
		if(shot.coords.x < 0 || shot.coords.y < 0 || shot.coords.x > g.getWidth() || shot.coords.x > g.getHeight()){
			iterator.remove();
		}
	}
	
	private void handleShotCollision(Dot shot, Iterator<Dot> shot_iterator, Dot dot, Iterator<Dot> dot_iterator)
	{
		if(Math.abs(shot.coords.x - dot.coords.x) <= Dot.maxRadius &&
				Math.abs(shot.coords.y - dot.coords.y) <= Dot.maxRadius){
			
			shot_iterator.remove();
			dot_iterator.remove();
			gunShotCollision.play(40);
			game.getVibration().vibrate(30);
		}
	}

	private void handleCollisions()
	{
		Iterator<Dot> iterator = dots.iterator();
		while(iterator.hasNext())
		{
			handleCollision(iterator.next(), iterator);
		}
	}

	private void handleCollision(Dot dot, Iterator<Dot> iterator)
	{
		float lengthToCoreCenter = (float)Math.hypot(
				(double)(dot.coords.x - core.coords.x),
				(double)(dot.coords.y - core.coords.y)); 
		
		if(Math.abs(lengthToCoreCenter - 
					core.shieldRadius) <= dot.maxRadius * dot.energy +
				Core.SHIELD_WIDTH)
		{
			checkCollisionWithShield(dot, iterator);
		}
		else if (lengthToCoreCenter - core.maxRadius * core.health <=
			   	Dot.maxRadius * dot.energy){
			handleCollisionWithCore(dot, iterator);
		}
		else{
			try{
				if(shots.size() > 0 && dot.type == Dot.Type.Enemy){
					Iterator<Dot> shot_iterator = shots.iterator();
					while(shot_iterator.hasNext()){
						handleShotCollision(shot_iterator.next(), shot_iterator, dot, iterator);
					}
				}
			}
			catch(NoSuchElementException e){
				// just sit this one out
			}
		}
	}

	private void checkCollisionWithShield(Dot dot, Iterator<Dot> iterator)
	{
		// I normalize (move into (0; 360) interval) angles
		// in some places. Don't know if it's needed.
		if(core.shieldEnergy > 0.0F)
		{
			iterator.remove();
			shieldCollision.play(dot.energy);
			game.getVibration().vibrate(30);
		}
		else
		{
		// Pay attention at -v.y! Y-axis is inverted, 
		// because it points downwards.
		float dotAngle = (float) Math.atan2((double) - 
				(dot.coords.y - core.coords.y),
			   	(double) (dot.coords.x - core.coords.x));
		dotAngle = dotAngle / (((float) Math.PI) * 2.0F) * 360.0F;
		dotAngle = normAngle(dotAngle);
		// For example, dotAngle = 3, and core.angle = 365
		// We need to solve this somehow:
		core.angle = normAngle(core.angle);
		while(dotAngle < core.angle)
		{
			dotAngle += 360.0F;
		}
		// OK, and check if dotAngle is within the gap
		if(!((dotAngle > core.angle) &&
				   	(dotAngle < core.angle + core.GAP_ANGLE)))
		{
			iterator.remove();
			shieldCollision.play(dot.energy);
			game.getVibration().vibrate(40);
		}
		}
	}

	private float normAngle(float angle)
	{
		float angle2 = angle;
		while(angle2 < 0.0F)
			angle2 += 360.0F;

		while(angle2 > 360.0F)
			angle2 -= 360.0F;

		return angle2;
	}

	private void handleCollisionWithCore(Dot dot, Iterator<Dot> iterator)
	{
		if(dot.type == Dot.Type.Enemy){
			core.health -= dot.energy / CORE_HEALTH;
			if(core.health < 0.0F)
			{
				state = GameState.GameOver;
				gameOver.play(1F);
				game.getVibration().vibrate(10);
				game.getVibration().vibrate(40);
				game.getVibration().vibrate(100);
				core.health = 0.0F;
			}
			coreHurt.play(dot.energy);
			}
			else if (dot.type == Dot.Type.Health)
			{
			core.health += dot.energy / CORE_HEALTH;
			if(core.health > 1.0F)	
			{
				core.health = 1.0F;
			}
			coreHealth.play(dot.energy);
		}

		iterator.remove();
		game.getVibration().vibrate(30);
	}

}
