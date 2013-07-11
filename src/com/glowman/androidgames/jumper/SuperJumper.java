package com.glowman.androidgames.jumper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.util.Log;

import com.glowman.android.framework.Screen;
import com.glowman.android.framework.impl.GLGame;
import com.glowman.androidgames.jumper.screens.MainMenuScreen;

public class SuperJumper extends GLGame {
	boolean firstTimeCreate = true;
	
	@Override
	public Screen getStartScreen() {
		return new MainMenuScreen(this);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		if(firstTimeCreate) {
			Settings.load(getFileIO());
			Assets.load(this);
			firstTimeCreate = false;
		} else {
			Assets.reload();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(Settings.soundEnabled)
			Assets.music.pause();
	}
}
