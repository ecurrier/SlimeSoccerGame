package com.slimesoccer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Goal {
	
	public Texture texture;
	public Sprite sprite;
	public BodyDef bodyDef_top;
	public BodyDef bodyDef_body;
	public PolygonShape shape_top;
	public PolygonShape shape_body;
	public FixtureDef fixtureDef_top;
	public FixtureDef fixtureDef_body;
	
	public Goal(FileHandle path, String userDataIdentifier){
		texture = new Texture(path);
		sprite = new Sprite(texture);
		
		bodyDef_body = new BodyDef();
		bodyDef_body.type = BodyDef.BodyType.StaticBody;
		
		bodyDef_top = new BodyDef();
		bodyDef_top.type = BodyDef.BodyType.StaticBody;
		
		float width = 0;
		float height = (-Gdx.graphics.getHeight() + 50) / Constants.PIXELS_TO_METERS;
		
		if(userDataIdentifier == "playergoal"){
			width = (-Gdx.graphics.getWidth() + 50) / Constants.PIXELS_TO_METERS;
			sprite.setPosition((-Gdx.graphics.getWidth() + 50) / 2, (-Gdx.graphics.getHeight() + 50) / 2);
		}
		else if(userDataIdentifier == "computergoal"){
			width = (Gdx.graphics.getWidth() - (50 + (sprite.getWidth() * 2))) / Constants.PIXELS_TO_METERS;
			sprite.setPosition((Gdx.graphics.getWidth() - (50 + (sprite.getWidth() * 2))) / 2, (-Gdx.graphics.getHeight() + 50) / 2);
		}
		
		bodyDef_body.position.set((width / 2) + sprite.getWidth() / (Constants.PIXELS_TO_METERS * 2),
							      (height / 2) + sprite.getHeight() / (Constants.PIXELS_TO_METERS * 2));
		
		bodyDef_top.position.set((width / 2) + sprite.getWidth() / Constants.PIXELS_TO_METERS / 2,
				 				 (height / 2) + sprite.getHeight() / Constants.PIXELS_TO_METERS / 2);
	}
	
	public void createShape(String userDataIdentifier){
		shape_body = new PolygonShape();
		shape_top = new PolygonShape();
		
		float[] bodyVerticies = null,
			topVerticies = null;
		
		if(userDataIdentifier == "playergoal"){
			bodyVerticies = new float[]{-0.15f, -0.40f,
										-0.15f, 0.40f,
										0f, 0.40f,
										-0.11f, -0.40f};
			
			topVerticies = new float[]{-0.15f, 0.34f,
									   -0.15f, 0.45f,
									   0.15f, 0.40f,
									   0.15f, 0.34f};
		}
		else if(userDataIdentifier == "computergoal"){
			bodyVerticies = new float[]{0.15f, -0.40f,
										0.15f, 0.40f,
										0f, 0.40f,
										0.11f, -0.40f};
			
			topVerticies = new float[]{0.15f, 0.34f,
									   0.15f, 0.45f,
									   -0.15f, 0.40f,
									   -0.15f, 0.34f};
		}
		shape_body.set(bodyVerticies);
		shape_top.set(topVerticies);
	}
	
	public void setProperties(){
		fixtureDef_body = new FixtureDef();
		fixtureDef_body.shape = shape_body;
		fixtureDef_body.friction = 0f;
		
		fixtureDef_top = new FixtureDef();
		fixtureDef_top.shape = shape_top;
		fixtureDef_top.friction = 0f;
	}
	
	public void draw(SpriteBatch batch){
		batch.draw(sprite, sprite.getX(), sprite.getY());
	}

}
