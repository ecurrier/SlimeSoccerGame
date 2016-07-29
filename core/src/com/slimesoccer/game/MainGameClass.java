package com.slimesoccer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MainGameClass extends ApplicationAdapter {
	SpriteBatch batch;
	
	Physics physics;
	Slime slime;
	Ball ball;
	
	@Override
	public void create () {
		physics = new Physics(0.33f);
		
		batch = new SpriteBatch();
		slime = new Slime("Models/redslime-right.png", 100, 0);
		ball = new Ball("Models/soccerball.png", 320, 200);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(slime.getY() > 0.0) {
			slime.airborne = true;
		}
		
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if(!slime.airborne) {
				slime.speedY += 5;
			}
		}
		
		physics.applyPhysics(slime);
		physics.applyPhysics(ball);
		checkBounds();
		checkOverlap();
		
		if (Gdx.input.isKeyPressed(Keys.A)) {
			slime.speedX -= 0.5;
		}
		else if (Gdx.input.isKeyPressed(Keys.D)) {
			slime.speedX += 0.5;
		}
		
		batch.begin();
		batch.draw(slime.texture, slime.getX(), slime.getY());
		batch.draw(ball.texture, ball.getX(), ball.getY());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		slime.texture.dispose();
		ball.texture.dispose();
	}
	
	public void checkBounds () {
		slime.checkBoundaries();
		ball.checkBoundaries();
		
	}
	
	public void checkOverlap() {
		Circle slimeBounds = new Circle((int)slime.getX() + ((int)slime.actor.getWidth()/2), (int)slime.getY() + ((int)slime.actor.getHeight()/2), (int)slime.actor.getHeight());
		Circle ballBounds = new Circle((int)ball.getX() + ((int)ball.actor.getWidth()/2), (int)ball.getY() + ((int)ball.actor.getHeight()/2), (int)ball.actor.getHeight()/2);
		
		if(slimeBounds.overlaps(ballBounds)) {
			ball.speedX += slime.speedX * 0.5f;
			ball.speedY += slime.speedY * 0.5f;
			
			if(slime.speedX == 0){
				ball.speedX = ball.speedX * -0.75f;
			}
			
			if(slime.speedY == 0){
				ball.speedY = ball.speedY * -0.75f;
			}
		}
	}
}
