package com.slimesoccer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
public class Ball {

	public Texture texture;
	public Sprite sprite;
	public BodyDef bodyDef;
	public CircleShape shape;
	public FixtureDef fixtureDef;
	
	public float maxSpeed = 10f;
	
	public Ball(String path){
		texture = new Texture(path);
		sprite = new Sprite(texture);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) / MainGameClass.PIXELS_TO_METERS,
				(sprite.getY() + sprite.getHeight() / 2) / MainGameClass.PIXELS_TO_METERS);
	}
	
	public void createShape(){
		shape = new CircleShape();
		shape.setRadius(sprite.getHeight()/2 / MainGameClass.PIXELS_TO_METERS);
	}
	
	public void setProperties(){
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.75f;
	}
}
