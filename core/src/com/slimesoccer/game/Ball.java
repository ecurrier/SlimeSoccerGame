package com.slimesoccer.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Ball{

	public Texture texture;
	public Sprite sprite;
	public Body body;
	public BodyDef bodyDef;
	public CircleShape shape;
	public FixtureDef fixtureDef;
	
	public float startingPositionX;
	public float startingPositionY;
	
	public float maxSpeed = 10f;
	
	public Ball(FileHandle path){
		texture = new Texture(path);
		sprite = new Sprite(texture);

		startingPositionX = 0 - ((sprite.getWidth() / 2) / Constants.PIXELS_TO_METERS);
		startingPositionY = 0;
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(startingPositionX, startingPositionY);
		bodyDef.linearDamping = 0.25f;
	}
	
	public void createShape(){
		shape = new CircleShape();
		shape.setRadius(sprite.getHeight() / (Constants.PIXELS_TO_METERS * 2));
	}
	
	public void setProperties(){
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.75f;
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
	
	public void reset(){
		body.setLinearVelocity(0f, 0f);
		body.setAngularVelocity(0f);
		body.setTransform(startingPositionX, startingPositionY, body.getAngle());
	}
}
