package com.slimesoccer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Slime {
	
	public Texture texture;
	public Actor actor;
	public float speedX = 0;
	public float speedY = 0;
	public float maxSpeed = 5;
	boolean airborne = false;
	
	public Slime(String path, int x, int y){
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
			speedY = 0;
			airborne = false;
		}
		
		if(getX() < 0.0) {
			setX(0);
		}
		else if(getX() > 580) {
			setX(580);
		}
	}

}
