package com.slimesoccer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ball {

	public Texture texture;
	public Actor actor;
	public float speedX = 0;
	public float speedY = 0;
	public float maxSpeed = 15;
	
	public Ball(String path, int x, int y){
		texture = new Texture(path);
		actor = new Actor();
		actor.setBounds(x, y, texture.getWidth(), texture.getHeight());
	}
	
	/* ------ SETTERS ------- */
	public void setX(float x){
		actor.setX(x);
	}
	
	public void setY(float y){
		actor.setY(y);
	}
	
	/* ------- GETTERS ------- */
	public float getX(){
		return actor.getX();
	}
	
	public float getY(){
		return actor.getY();
	}
	
	public void checkBoundaries(){
		if(getY() < 0.0) {
			setY(0);
			speedY = speedY * -0.75f;
		}
		
		if(actor.getX() < 0.0) {
			actor.setX(0);
			speedX = speedX * -0.75f;
		}
		else if(actor.getX() > 625) {
			actor.setX(625);
			speedX = speedX * -0.75f;
		}
	}
}
