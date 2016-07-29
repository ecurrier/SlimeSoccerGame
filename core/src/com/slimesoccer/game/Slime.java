package com.slimesoccer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Slime {
	
	public Texture texture;
	public Sprite sprite;
	public BodyDef bodyDef;
	public PolygonShape shape;
	public FixtureDef fixtureDef;
	
	public float maxSpeed = 3f;
	boolean airborne = false;
	
	public Slime(String path){
		texture = new Texture(path);
		sprite = new Sprite(texture);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) / MainGameClass.PIXELS_TO_METERS,
				(sprite.getY() + sprite.getHeight() / 2) / MainGameClass.PIXELS_TO_METERS);
		bodyDef.fixedRotation = true;
	}
	
	public void createShape(){
		shape = new PolygonShape();
		float[] vertices = {-0.3f, -0.15f,
							-0.28f, -0.05f,
							-0.2f, 0.05f,
							-0.1f, 0.13f,
							0.1f, 0.13f,
							0.2f, 0.05f,
							0.28f, -0.05f,
							0.3f, -0.15f };

		shape.set(vertices);
	}
	
	public void setProperties(){
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.0f;
	}

}
