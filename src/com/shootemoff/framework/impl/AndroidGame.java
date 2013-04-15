package com.shootemoff.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shootemoff.framework.Audio;
import com.shootemoff.framework.FileIO;
import com.shootemoff.framework.Game;
import com.shootemoff.framework.Graphics;
import com.shootemoff.framework.Input;
import com.shootemoff.framework.Screen;
import com.shootemoff.framework.Vibration;
import com.shootemoff.game.GameOverActivity;
import com.shootemoff.shootemoffgame.R;


public abstract class AndroidGame extends Activity implements Game
{
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Vibration vibration;
	Screen screen;
	public static Context context;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bitmap frameBuffer = 
			Bitmap.createBitmap(
					getWindowManager().getDefaultDisplay().getWidth(),
					getWindowManager().getDefaultDisplay().getHeight(),
					Config.RGB_565);
		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(frameBuffer);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView);
		vibration = new AndroidVibration(this);
		screen = getStartScreen();
		setContentView(renderView);

		PowerManager powerManager = 
			(PowerManager) getSystemService(Context.POWER_SERVICE);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		screen.resume();
		renderView.resume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		renderView.pause();
		screen.pause();

		if(isFinishing())
			screen.dispose();
	}

	@Override
	public Input getInput()
	{
		return input;
	}

	@Override
	public FileIO getFileIO()
	{
		return fileIO;
	}

	@Override
	public Graphics getGraphics()
	{
		return graphics;
	}

	@Override
	public Audio getAudio()
	{
		return audio;
	}

	@Override
	public void setScreen(Screen screen)
	{
		if(screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public Screen getCurrentScreen()
	{
		return screen;
	}

	public Vibration getVibration()
	{
		return vibration;
	}
}
