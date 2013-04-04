package com.shootemoff.game;

import com.shootemoff.framework.*;
import com.shootemoff.framework.impl.*;

import android.view.*;
import android.graphics.*;


public class GameActivity extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new GameScreen(this); 
    }
}
