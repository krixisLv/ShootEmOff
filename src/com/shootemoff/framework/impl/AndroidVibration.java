package com.shootemoff.framework.impl;

import com.shootemoff.framework.Vibration;

import android.os.Vibrator;
import android.content.Context;


public class AndroidVibration implements Vibration
{
	Vibrator vibrator;

	public AndroidVibration(Context context)
	{
		vibrator = 
			(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public void vibrate(int time)
	{
			vibrator.vibrate((long)(time));
	}
}
