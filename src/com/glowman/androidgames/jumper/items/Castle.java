package com.glowman.androidgames.jumper.items;

import com.glowman.android.framework.gamedev2d.GameObject;

public class Castle extends GameObject {
	public static float CASTLE_WIDTH = 1.7f;
	public static float CASTLE_HEIGHT = 1.7f;
	
	public Castle(float x, float y) {
		super(x, y, CASTLE_WIDTH, CASTLE_HEIGHT);
	}
}
