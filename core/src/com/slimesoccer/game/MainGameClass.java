package com.slimesoccer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class MainGameClass extends ApplicationAdapter{
	SpriteBatch batch;
	World world;
	Controller controller;
	
	Slime slime;
	Ball ball;
	Boundary[] boundaries = new Boundary[4];
	Goal goal;

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;

	@Override
	public void create() {
		world = new World(new Vector2(0, -2f), true);
		batch = new SpriteBatch();
		controller = new Controller();
		
		createSlimeBody();
		createBallBody();
		createGoalBody();
		createBoundaries();
		
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		setContactListener();
	}

	@Override
	public void render() {
		camera.update();
		world.step(1f / 60f, 6, 2);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		controller.checkMovement(slime);

		slime.adjustSpritePosition();
		ball.adjustSpritePosition();

		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(Constants.PIXELS_TO_METERS, Constants.PIXELS_TO_METERS, 0);
		
		batch.begin();
		
		slime.draw(batch);
		ball.draw(batch);
		goal.draw(batch);
		
		/* TESTING - TRAJECTORY PATH  FOR AI */
		for(int n=1; n<=32; n++){
			float t = 6f / 60f;
			Vector2 stepVelocity = ball.body.getLinearVelocity().scl(t);
			Vector2 stepGravity = world.getGravity().scl(t*t);
			
			Vector2 calculation = (ball.body.getPosition().add(stepVelocity.scl(n)).add( stepGravity.scl(0.5f * (n*n+n))));
			batch.draw(ball.sprite, calculation.x * 100f, calculation.y *  100f);
		}

		batch.end();
		debugRenderer.render(world, debugMatrix); // Displays body structure lines
	}

	@Override
	public void dispose() {
		batch.dispose();
		slime.texture.dispose();
		ball.texture.dispose();
	}
	
	/**
	 * Sets the rules for what happens when various objects contact each other.
	 */
	private void setContactListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				if(collision(contact, "ground", "slime")){
					slime.airborne = false;
				}
				
				if(collision(contact, "ball", "goal")){
					Gdx.app.exit();
				}
			}
			
			@Override
			public void endContact(Contact contact) {
				if(collision(contact, "ground", "slime")){
					slime.airborne = true;
				}
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
			
		});
	}

	/**
	 * Creates the 4 boundaries of the world.
	 * The 'ground' is specifically labeled in order to properly identify when the
	 * slime has contact with the ground in order to identify when the slime
	 * can jump again.
	 */
	private void createBoundaries() {
		Body bodyEdgeScreen;
		
		for(int i=0; i < 4; i++){
			boundaries[i] = new Boundary();
			boundaries[i].setProperties(i);
			
			bodyEdgeScreen = world.createBody(boundaries[i].bodyDef);
			bodyEdgeScreen.createFixture(boundaries[i].fixtureDef);
			boundaries[i].shape.dispose();
			
			if(i==0){
				bodyEdgeScreen.setUserData("ground");
			}
		}
	}

	/**
	 * Creates the goal.
	 * When the back of the goal is touched by the ball, the opposing player will have scored.
	 */
	private void createGoalBody() {
		goal = new Goal("Models/goal.png");
		Body goalBody = world.createBody(goal.bodyDef_body);
		
		goal.createShape();
		goal.setProperties();
		goalBody.createFixture(goal.fixtureDef_body);
		goal.shape_body.dispose();
		goalBody.setUserData("goal");
		
		goalBody = world.createBody(goal.bodyDef_top);
		goalBody.createFixture(goal.fixtureDef_top);
		goal.shape_top.dispose();
	}

	/**
	 * Creates the ball.
	 */
	private void createBallBody() {
		ball = new Ball("Models/soccerball.png");
		Body ballBody = world.createBody(ball.bodyDef);
		
		ball.createShape();
		ball.setProperties();
		ballBody.createFixture(ball.fixtureDef);
		ball.shape.dispose();
		ballBody.setUserData("ball");
		ball.body = ballBody;
	}

	/**
	 * Creates the slime.
	 */
	private void createSlimeBody() {
		slime = new Slime("Models/redslime-right.png");
		Body slimeBody = world.createBody(slime.bodyDef);
		
		slime.createShape();
		slime.setProperties();
		slimeBody.createFixture(slime.fixtureDef);
		slime.shape.dispose();
		slimeBody.setUserData("slime");
		slime.body = slimeBody;
	}
	
	private boolean collision(Contact contact, String bodyA, String bodyB) {
		if((contact.getFixtureA().getBody().getUserData() == bodyA &&
	    contact.getFixtureB().getBody().getUserData() == bodyB) ||
	    (contact.getFixtureA().getBody().getUserData() == bodyB &&
	    contact.getFixtureB().getBody().getUserData() == bodyA)){
			return true;
		}
		
		return false;
	}
}
