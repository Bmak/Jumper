package com.glowman.androidgames.jumper.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.glowman.android.framework.Game;
import com.glowman.android.framework.Input.TouchEvent;
import com.glowman.android.framework.gl.Camera2D;
import com.glowman.android.framework.gl.SpriteBatcher;
import com.glowman.android.framework.gl.Texture;
import com.glowman.android.framework.gl.TextureRegion;
import com.glowman.android.framework.impl.GLScreen;
import com.glowman.android.framework.math.OverlapTester;
import com.glowman.android.framework.math.Rectangle;
import com.glowman.android.framework.math.Vector2;
import com.glowman.androidgames.jumper.Assets;

public class HelpScreen extends GLScreen {
	private final int HELP_1 = 1;
	private final int HELP_2 = 2;
	private final int HELP_3 = 3;
	private final int HELP_4 = 4;
	private final int HELP_5 = 5;
	
	int selectedHelp = 1;
	
	Camera2D guiCam;
	SpriteBatcher batcher;
	Rectangle nextBounds;
	Vector2 touchPoint;
	Texture helpImage;
	TextureRegion helpRegion;
	
	public HelpScreen(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		nextBounds = new Rectangle(320 - 64, 0, 64, 64);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 10);
		
		helpImage = new Texture(glGame, "help" + selectedHelp + ".png" );
		helpRegion = new TextureRegion(helpImage, 0, 0, 320, 480);
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
				if(OverlapTester.pointInRectangle(nextBounds, touchPoint)) {
					Assets.playSound(Assets.clickSound);
					selectedHelp++;
					if (selectedHelp > HELP_5) {
						game.setScreen(new MainMenuScreen(game));
						selectedHelp = 1;
						return;
					}
					helpImage = new Texture(glGame, "help" + selectedHelp + ".png" );
					helpRegion = new TextureRegion(helpImage, 0, 0, 320, 480);
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
		
		batcher.beginBatch(helpImage);
		batcher.drawSprite(160, 240, 320, 480, helpRegion);
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items);
		batcher.drawSprite(320 - 32, 32, -64, 64, Assets.arrow);
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	@Override
	public void resume() {
		batcher = new SpriteBatcher(glGraphics, 10);
		
		helpImage = new Texture(glGame, "help" + selectedHelp + ".png" );
		helpRegion = new TextureRegion(helpImage, 0, 0, 320, 480);
		
		Log.d("ResumeHelp", "help:: " + selectedHelp);
	}
	
	@Override
	public void pause() {
		helpImage.dispose();
	}
	
	@Override
	public void dispose() {
	}
}
