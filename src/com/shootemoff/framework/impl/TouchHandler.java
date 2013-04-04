package com.shootemoff.framework.impl;

import java.util.List;

import com.shootemoff.framework.Input.TouchEvent;

import android.view.View.OnTouchListener;


public interface TouchHandler extends OnTouchListener
{
    public boolean isTouchDown();
    
    public int getTouchX();
    
    public int getTouchY();
    
    public List<TouchEvent> getTouchEvents();
}
