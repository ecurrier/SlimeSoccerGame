package com.slimesoccer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class MainGameClass extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Sprite sprite, ballSprite;

	Slime slime;
	Ball ball;
	Boundary[] boundaries = new Boundary[4];

	World world;
	Body slimeBody, ballBody;
	Body bodyEdgeScreen;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;

	float torque = 0.0f;
	boolean drawSprite = true;
	boolean keyPressed_D = false, 
			keyPressed_A = false;

	public static float PIXELS_TO_METERS = 100f;

	@Override
	public void create() {
		world = new World(new Vector2(0, -2f), true);
		batch = new SpriteBatch();
		
		slime = new Slime("Models/redslime-right.png");
		ball = new Ball("Models/soccerball.png");

		/* SLIME */

		slimeBody = world.createBody(slime.bodyDef);
		slime.createShape();
		slime.setProperties();
		slimeBody.createFixture(slime.fixtureDef);
		slime.shape.dispose();

		/* BALL */
		
		ballBody = world.createBody(ball.bodyDef);
		ball.createShape();
		ball.setProperties();
		ballBody.createFixture(ball.fixtureDef);
		ball.shape.dispose();
		
		slime.sprite.setPosition(-slime.sprite.getWidth() / 2, -slime.sprite.getHeight() / 2);
		ball.sprite.setPosition(-ball.sprite.getWidth() / 2, -ball.sprite.getHeight() / 2);
		
		/* BOUNDARIES */
		
		for(int i=0; i < 4; i++){
			boundaries[i] = new Boundary();
			boundaries[i].setProperties(i);
			
			bodyEdgeScreen = world.createBody(boundaries[i].bodyDef);
			bodyEdgeScreen.createFixture(boundaries[i].fixtureDef);
			boundaries[i].shape.dispose();
		}

		Gdx.input.setInputProcessor(this);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render() {
		camera.update();
		world.step(1f / 60f, 6, 2);

		slimeBody.applyTorque(torque, true);
		ballBody.applyTorque(torque, true);
		
		if(keyPressed_A && slimeBody.getLinearVelocity().x > -slime.maxSpeed){
			slimeBody.applyLinearImpulse(-0.80f/PIXELS_TO_METERS, 0, slimeBody.getPosition().x, slimeBody.getPosition().y, true);
		}
		if(keyPressed_D && slimeBody.getLinearVelocity().x < slime.maxSpeed){
			slimeBody.applyLinearImpulse(0.80f/PIXELS_TO_METERS, 0, slimeBody.getPosition().x, slimeBody.getPosition().y, true);
		}

		slime.sprite.setPosition((slimeBody.getPosition().x * PIXELS_TO_METERS) - slime.sprite.getWidth() / 2,
				(slimeBody.getPosition().y * PIXELS_TO_METERS) - slime.sprite.getHeight() / 2);
		slime.sprite.setRotation((float) Math.toDegrees(slimeBody.getAngle()));

		ball.sprite.setPosition((ballBody.getPosition().x * PIXELS_TO_METERS) - ball.sprite.getWidth() / 2,
				(ballBody.getPosition().y * PIXELS_TO_METERS) - ball.sprite.getHeight() / 2);
		ball.sprite.setRotation((float) Math.toDegrees(ballBody.getAngle()));

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);
		batch.begin();

		batch.draw(slime.sprite, slime.sprite.getX(), slime.sprite.getY(), slime.sprite.getOriginX(), slime.sprite.getOriginY(), slime.sprite.getWidth(),
				slime.sprite.getHeight(), slime.sprite.getScaleX(), slime.sprite.getScaleY(), slime.sprite.getRotation());

		batch.draw(ball.sprite, ball.sprite.getX(), ball.sprite.getY(), ball.sprite.getOriginX(), ball.sprite.getOriginY(),
				ball.sprite.getWidth(), ball.sprite.getHeight(), ball.sprite.getScaleX(), ball.sprite.getScaleY(),
				ball.sprite.getRotation());

		batch.end();
		debugRenderer.render(world, debugMatrix);
	}

	@Override
	public void dispose() {
		batch.dispose();
		slime.texture.dispose();
		ball.texture.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.D){
			keyPressed_D = true;
		}
		if (keycode == Input.Keys.A){
			keyPressed_A = true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.D){
			keyPressed_D = false;
		}
		if (keycode == Input.Keys.A){
			keyPressed_A = false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
