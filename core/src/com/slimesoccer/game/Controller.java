package com.slimesoccer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class Controller implements InputProcessor {
	
	boolean keyPressed_D = false, 
			keyPressed_A = false,
			keyPressed_Space = false;
	
	public Controller() {
		Gdx.input.setInputProcessor(this);
	}
	
	public void checkMovement(Slime slime){
		if(keyPressed_A && slime.body.getLinearVelocity().x > -slime.maxSpeed){
			slime.body.applyLinearImpulse(-0.80f/Constants.PIXELS_TO_METERS, 0, slime.body.getPosition().x, slime.body.getPosition().y, true);
		}
		if(keyPressed_D && slime.body.getLinearVelocity().x < slime.maxSpeed){
			slime.body.applyLinearImpulse(0.80f/Constants.PIXELS_TO_METERS, 0, slime.body.getPosition().x, slime.body.getPosition().y, true);
		}
		if(keyPressed_Space && !slime.airborne){
			slime.body.applyLinearImpulse(0, 1.00f/Constants.PIXELS_TO_METERS, slime.body.getPosition().x, slime.body.getPosition().y, true);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.D){
			keyPressed_D = true;
		}
		if (keycode == Input.Keys.A){
			keyPressed_A = true;
		}
		if (keycode == Input.Keys.SPACE){
			keyPressed_Space = true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.D){
			keyPressed_D = false;
		}
		if (keycode == Input.Keys.A){
			keyPressed_A = false;
		}
		if (keycode == Input.Keys.SPACE){
			keyPressed_Space = false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenX < Gdx.graphics.getWidth()/2 &&
		   screenY > Gdx.graphics.getHeight()/2){
			keyPressed_A = true;
		}
		if(screenX >= Gdx.graphics.getWidth()/2 &&
		   screenY > Gdx.graphics.getHeight()/2){
			keyPressed_D = true;
		}
		if (screenY < Gdx.graphics.getHeight()/2){
			keyPressed_Space = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(screenX < Gdx.graphics.getWidth()/2 &&
		   screenY > Gdx.graphics.getHeight()/2){
			keyPressed_A = false;
		}
		if(screenX >= Gdx.graphics.getWidth()/2 &&
		   screenY > Gdx.graphics.getHeight()/2){
			keyPressed_D = false;
		}
		if (screenY < Gdx.graphics.getHeight()/2){
			keyPressed_Space = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
