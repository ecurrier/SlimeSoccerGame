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
	
	public float maxSpeed = 10f;
	
	public Ball(FileHandle path){
		texture = new Texture(path);
		sprite = new Sprite(texture);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((sprite.getWidth() / 2) / Constants.PIXELS_TO_METERS,
						     (sprite.getHeight() / 2) / Constants.PIXELS_TO_METERS);
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
}
