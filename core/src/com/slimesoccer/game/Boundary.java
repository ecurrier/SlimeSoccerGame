package com.slimesoccer.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Boundary {

	public BodyDef bodyDef;
	public EdgeShape shape;
	public FixtureDef fixtureDef;

	public Boundary() {
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(0, 0);
	}

	public void setProperties(int location) {
		fixtureDef = new FixtureDef();
		shape = new EdgeShape();
		float width = 0, height = 0;

		switch (location) {
		case 0:
			width = Constants.SCREEN_WIDTH / Constants.PIXELS_TO_METERS;
			height = (Constants.SCREEN_HEIGHT - 50) / Constants.PIXELS_TO_METERS;
			shape.set(-width / 2, -height / 2, width / 2, -height / 2);
			fixtureDef.friction = 100f;
			break;
		case 1:
			width = (Constants.SCREEN_WIDTH - 50) / Constants.PIXELS_TO_METERS;
			height = Constants.SCREEN_HEIGHT / Constants.PIXELS_TO_METERS;
			shape.set(-width / 2, -height / 2, -width / 2, height / 2);
			fixtureDef.friction = 0f;
			fixtureDef.restitution = 0.1f;
			break;
		case 2:
			width = (-Constants.SCREEN_WIDTH + 50) / Constants.PIXELS_TO_METERS;
			height = Constants.SCREEN_HEIGHT / Constants.PIXELS_TO_METERS;
			shape.set(-width / 2, -height / 2, -width / 2, height / 2);
			fixtureDef.friction = 0f;
			fixtureDef.restitution = 0.1f;
			break;
		case 3:
			width = Constants.SCREEN_WIDTH / Constants.PIXELS_TO_METERS;
			height = (-Constants.SCREEN_HEIGHT + 250) / Constants.PIXELS_TO_METERS;
			shape.set(-width / 2, -height / 2, width / 2, -height / 2);
			fixtureDef.friction = 0f;
			fixtureDef.restitution = 0.1f;
			break;
		default:
			break;
		}
		fixtureDef.shape = shape;
	}
}
