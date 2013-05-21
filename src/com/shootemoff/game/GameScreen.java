package com.shootemoff.game;

import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;

import com.shootemoff.framework.Graphics;
import com.shootemoff.framework.Screen;


public class GameScreen extends Screen 
{
	GameActivity gActivity;
	
    long startTime = System.nanoTime();
	World world;

	Paint paint = new Paint();
	RectF rect = new RectF();
	RectF shield_pad_rect = new RectF();
	RectF gun_pad_rect = new RectF();
	
	GradientDrawable gradient;

	Context r;
	
	int healthColor = 0xff429E4C;
	int mainCoreColor = 0xff0f4915;
	int enemyDotColor = 0xff000000;
	int shieldControlColor = 0x801b8e26;
	int shieldControlShieldColor = 0xee000000;
	int gunControlColor = 0x80727673;
	int gunshotColor = 0xffFF000D;
        
    public GameScreen(GameActivity game)
    {
    	super(game);
    	gActivity = game;
		r = (Context) game;
		world = new World(game, game.getResources().getConfiguration().screenLayout);	
		world.renew();
		world.gameOn.play(50);
		rect.top = world.core.coords.y - world.core.shieldRadius;
		rect.left = world.core.coords.x - world.core.shieldRadius;
		rect.bottom = world.core.coords.y + world.core.shieldRadius;
		rect.right = world.core.coords.x + world.core.shieldRadius;
		
		shield_pad_rect.top = world.shieldControl.coords.y - world.shieldControl.ARC_RADIUS;
		shield_pad_rect.left = world.shieldControl.coords.x - world.shieldControl.ARC_RADIUS;
		shield_pad_rect.bottom = world.shieldControl.coords.y + world.shieldControl.ARC_RADIUS;
		shield_pad_rect.right = world.shieldControl.coords.x + world.shieldControl.ARC_RADIUS;
		
		gun_pad_rect.top = world.gunControl.coords.y - world.gunControl.ARC_RADIUS;
		gun_pad_rect.left = world.gunControl.coords.x - world.gunControl.ARC_RADIUS;
		gun_pad_rect.bottom = world.gunControl.coords.y + world.gunControl.ARC_RADIUS;
		gun_pad_rect.right = world.gunControl.coords.x + world.gunControl.ARC_RADIUS;

		paint.setAntiAlias(true);
		paint.setStrokeWidth(0.0F);
		
		gradient = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
				new int[]{0xffc2c2c2, 0xffb1b1b1, 0xff949494, 0xff727272, 0xff5c5c5c,});
		
		gradient.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		gradient.setGradientRadius((int) world.offScreenRadius);
		gradient.setDither(false);
		gradient.setGradientCenter(0.2F, 0.5F);
		gradient.setBounds(new Rect(0, 0, game.getGraphics().getWidth(),
				   	game.getGraphics().getHeight()));
		
		paint.setTextSize(((float)game.getGraphics().getHeight()) / 16F);
		paint.setTextAlign(Paint.Align.CENTER);
    }
    
    @Override
    public void update(float deltaTime) 
    {
		world.update(deltaTime);
    }
	
    @Override
    public void present(float deltaTime)
    {
	    Canvas c = game.getGraphics().getCanvas();    
		gradient.draw(c);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		if(world.core.shieldEnergy > 0.0F){
			paint.setColor(0xff003cca);
			paint.setAlpha((int) (80.0F +
					   	(255.0F - 80.0F) * world.core.shieldEnergy));
			c.drawCircle(world.core.coords.x, world.core.coords.y,
				world.core.shieldRadius, paint);
			paint.setAlpha(255);
		}
		paint.setColor(mainCoreColor);
		c.drawCircle(world.core.coords.x, world.core.coords.y,
			   	world.core.maxRadius * world.core.health,
				paint);
		paint.setStyle(Paint.Style.STROKE);
		
		paint.setColor(mainCoreColor);
		paint.setStrokeWidth(Core.SHIELD_WIDTH);
		c.drawArc(rect, (360.0F - world.core.angle),
				(360.0F - world.core.GAP_ANGLE), false, paint);
		paint.setStrokeWidth(0.0F);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		//draw shield control arc
		paint.setColor(shieldControlColor);
		c.drawArc(shield_pad_rect, -90,
				180, true, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(ControlPad.SHIELD_WIDTH);
		paint.setColor(shieldControlShieldColor);
		c.drawArc(shield_pad_rect, (360.0F - world.shieldControl.angle),
				(360.0F - world.shieldControl.GAP_ANGLE), false, paint);
		paint.setStrokeWidth(0.0F);
		
		//draw gun control arc
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(gunControlColor);
		c.drawArc(gun_pad_rect, -90,
				180, true, paint);
		paint.setStyle(Paint.Style.STROKE);
		
		Graphics g = world.game.getGraphics();
		float screenWidth = (float) g.getWidth();
	   	float screenHeight = (float) g.getHeight();
	   	
	   	//set shield control rectangle for touch area
	   	world.shield_top = (float) (screenHeight * world.shield_top_coef);
	   	world.shield_bottom = (float) (screenHeight * world.shield_bottom_coef);
	   	world.shield_right = (float) (screenWidth * world.shield_right_coef);
	   	world.shield_left = (float) (screenWidth * world.shield_left_coef);
	  //set gun control rectangle for touch area
	   	world.gun_top = (float) (screenHeight * world.gun_top_coef);
	   	world.gun_bottom = (float) (screenHeight * world.gun_bottom_coef);
	   	world.gun_right = (float) (screenWidth * world.gun_right_coef);
	   	world.gun_left = (float) (screenWidth * world.gun_left_coef);
	   	
	   	if(world.just_aimed == true){
	   		paint.setStrokeWidth(1.0F);
	   		int color = gunshotColor;
			paint.setColor(color);
	   		c.drawLine(world.core.coords.x, world.core.coords.y, world.aim_beam.x, world.aim_beam.y, paint);
	   	}
		
		//color the dots
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(0.0F);
		Iterator<Dot> iterator = world.dots.iterator();
		while(iterator.hasNext())
		{
			int color = 0;
			Dot dot = iterator.next();
			if(dot.type == Dot.Type.Enemy){
				color = enemyDotColor;
			}
			else if(dot.type == Dot.Type.Health){
				color = healthColor;
			}

			paint.setColor(color);
			c.drawCircle(dot.coords.x, dot.coords.y,
					dot.maxRadius * dot.energy, paint);
	    }
		
		iterator = world.shots.iterator();
		while(iterator.hasNext()){
			int color = 0;
			Dot dot = iterator.next();
			color = gunshotColor;
			paint.setColor(color);
			c.drawCircle(dot.coords.x, dot.coords.y,
					dot.maxRadius * dot.energy, paint);
		}
	
		if(world.state == World.GameState.Running)
			drawMessage(world.getTime(), c);
//		else if(world.state == World.GameState.Ready)
//			drawMessage(r.getString(R.string.ready), c);
//		else if(world.state == World.GameState.Paused)
//			drawMessage(r.getString(R.string.paused), c);
		else if(world.state == World.GameState.GameOver){
			ReturnGameResult(world.getTime());
		}
	}
    
    private void ReturnGameResult(String score)
    {
    	gActivity.finishActivity(score);
    }
    
    private void drawShieldPadMessage(String message, Canvas c)
    {
    	float oldSize = paint.getTextSize();
    	paint.setTextSize((float)(Math.ceil(oldSize*0.9)));
    	
    	paint.setStrokeWidth(0.0F);
		paint.setColor(0xee111111);
	    paint.setStyle(Paint.Style.FILL);
	    
	    float coordX = (float)(c.getWidth()* 0.25);
	    float coordY = (float)(c.getHeight()* 0.95);

		c.drawText(message, coordX, coordY, paint);
		
		paint.setTextSize((float)oldSize);
    }

	private void drawMessage(String message, Canvas canvas)
	{
		float y = (float)(paint.getTextSize());
		float x = (float)(canvas.getWidth() - paint.getTextSize());
		for(String line: message.split("\n")){
			// Draw black stroke
			paint.setStrokeWidth(2F);
			paint.setColor(0xff000000);
		    paint.setStyle(Paint.Style.STROKE);
		    
		    canvas.save();
		    canvas.rotate(90, x, y);
			canvas.drawText(line, x, y, paint);
			canvas.restore();
			// Draw white text
			paint.setStrokeWidth(0.0F);
			paint.setColor(0xffffffff);
		    paint.setStyle(Paint.Style.FILL);
	
		    canvas.save();
		    canvas.rotate(90, x, y);
		    canvas.drawText(line, x, y, paint);
		    canvas.restore();
			//c.drawText(line, c.getWidth(), y, paint);
	
			y += paint.getTextSize();
		}
	}

    @Override
    public void pause() 
    {
		world.state = World.GameState.Paused;
    }

    @Override
    public void resume()
    {
    	//nothing to do
    }

    @Override
    public void dispose()
    {
    	//nothing to do
    }            
}
