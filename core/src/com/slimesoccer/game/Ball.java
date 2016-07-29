package com.slimesoccer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Ball {

	public Texture texture;
	public Sprite sprite;
	public BodyDef bodyDef;
	public PolygonShape shape;
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
		shape = new PolygonShape();
		float[] vertices = {-0.075f, 0f,
							-0.05f, 0.05f,
							0f, 0.075f,
							0.05f, 0.05f,
							0.075f, 0f,
							0.05f, -0.025f,
							0f, -0.075f,
							-0.05f, -0.05f };
		shape.set(vertices);
	}
	
	public void setProperties(){
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.75f;
	}
}
