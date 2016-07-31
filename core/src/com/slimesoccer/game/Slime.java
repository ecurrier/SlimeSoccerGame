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
	
	public float maxSpeed = 2f;
	public boolean airborne = true;
	
	public Slime(FileHandle path, float positionOffset){
		texture = new Texture(path);
		sprite = new Sprite(texture);
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((sprite.getWidth() / 2) / Constants.PIXELS_TO_METERS + positionOffset,
							 (sprite.getHeight() / 2) / Constants.PIXELS_TO_METERS - 2.25f);
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
