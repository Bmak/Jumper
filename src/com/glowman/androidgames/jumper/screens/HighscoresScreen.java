package com.glowman.androidgames.jumper.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.glowman.android.framework.Game;
import com.glowman.android.framework.Input.TouchEvent;
import com.glowman.android.framework.gl.Camera2D;
import com.glowman.android.framework.gl.SpriteBatcher;
import com.glowman.android.framework.impl.GLScreen;
import com.glowman.android.framework.math.OverlapTester;
import com.glowman.android.framework.math.Rectangle;
import com.glowman.android.framework.math.Vector2;
import com.glowman.androidgames.jumper.Assets;
import com.glowman.androidgames.jumper.Settings;

public class HighscoresScreen extends GLScreen {
	Camera2D guiCam;
	SpriteBatcher batcher;
	Rectangle backBounds;
	Vector2 touchPoint;
	String[] highScores;
	float xOffset = 0;
	
	public HighscoresScreen(Game game) {
		super(game);
		
		guiCam = new Camera2D(glGraphics, 320, 480);
		backBounds = new Rectangle(0, 0, 64, 64);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 100);
		highScores = new String[5];
		
		for(int i = 0; i < 5; i++) {
			highScores[i] = (i + 1) + ". " + Settings.highscores[i];
			xOffset = Math.max(highScores[i].length() * Assets.font.glyphWidth, xOffset);
		}
		xOffset = 160 - xOffset / 2;
	}
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		int len = touchEvents.size();
		for(int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			if(event.type == TouchEvent.TOUCH_UP) {
				if(OverlapTester.pointInRectangle(backBounds, touchPoint)) {
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}
	
	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		batcher.beginBatch(Assets.background);
		batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegion);
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items);
		batcher.drawSprite(160, 360, 300, 33, Assets.highScoresRegion);
		
		float y = 240;
		for(int i = 4; i >= 0; i--) {
			Assets.font.drawText(batcher, highScores[i], xOffset, y);
			y += Assets.font.glyphHeight;
		}
		
		batcher.drawSprite(32, 32, 64, 64, Assets.arrow);
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
}
