package com.slimesoccer.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Slime {
	
	public Texture texture;
	public Sprite sprite;
	public Body body;
	public BodyDef bodyDef;
	public PolygonShape shape;
	public FixtureDef fixtureDef;
	
	public float startingPositionX;
	public float startingPositionY;
	
	public float maxSpeed = 2f;
	public float speedFactor = 1f;
	public boolean airborne = true;
	public boolean boostActive = false;
	
	public long boostStart;
	
	public Slime(FileHandle path, float positionOffset){
		texture = new Texture(path);
		sprite = new Sprite(texture);
		
		startingPositionX = (sprite.getWidth() / 2) / Constants.PIXELS_TO_METERS + positionOffset;
		startingPositionY = ((sprite.getHeight() / 2) / Constants.PIXELS_TO_METERS - 2.25f) + ((sprite.getHeight()/2) / Constants.PIXELS_TO_METERS);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(startingPositionX, startingPositionY);
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 2.0f;
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
		fixtureDef.filter.groupIndex = -1;
	}
	
	public void adjustSpritePosition(){
		sprite.setPosition((body.getPosition().x * Constants.PIXELS_TO_METERS) - sprite.getWidth() / 2,
				(body.getPosition().y * Constants.PIXELS_TO_METERS) - sprite.getHeight() / 2);
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
	}
	
	public void draw(SpriteBatch batch){
		batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(),
				   sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
	}
	
	public void setBoost(boolean active){
		if(active){
			speedFactor = 1.5f;
			boostStart = System.nanoTime();
			maxSpeed *= speedFactor;
		}
		else{
			maxSpeed /= speedFactor;
			speedFactor = 1f;
		}
		
		boostActive = active;
	}
	
	public void reset(){
        body.setTransform(startingPositionX, startingPositionY, body.getAngle());
	}

}
