package com.slimesoccer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Boundary {

	public BodyDef bodyDef;
	public EdgeShape shape;
	public FixtureDef fixtureDef;
	
	public Boundary(){
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(0, 0);
	}
	
	public void setProperties(int location){
		fixtureDef = new FixtureDef();
		shape = new EdgeShape();
		float width = 0,
			  height = 0;
		
		switch(location){
		case 0:
			width = 640 / Constants.PIXELS_TO_METERS;
			height = (480 - 50) / Constants.PIXELS_TO_METERS;
			shape.set(-width / 2, -height / 2, width / 2, -height / 2);
			fixtureDef.friction = 100f;
			break;
		case 1:
			width = (640 - 50) / Constants.PIXELS_TO_METERS;
			height = 480 / Constants.PIXELS_TO_METERS;
			shape.set(-width / 2, -height / 2, -width / 2, height / 2);
			fixtureDef.friction = 0f;
			break;
		case 2:
			width = (-640 + 50) / Constants.PIXELS_TO_METERS;
			height = 480 / Constants.PIXELS_TO_METERS;
			shape.set(-width / 2, -height / 2, -width / 2, height / 2);
			fixtureDef.friction = 0f;
			break;
		case 3:
			width = 640 / Constants.PIXELS_TO_METERS;
			height = (-480  + 50) / Constants.PIXELS_TO_METERS;
			shape.set(-width / 2, -height / 2, width / 2, -height / 2);
			fixtureDef.friction = 0f;
			break;
		default:
			break;
		}
		fixtureDef.shape = shape;
	}
}
