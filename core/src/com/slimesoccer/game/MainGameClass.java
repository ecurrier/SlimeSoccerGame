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
	
	Slime player,
		computer;

	
	Ball ball;
	NpcBrain npc;
	Boundary[] boundaries = new Boundary[4];
	Goal playerGoal,
		computerGoal;

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;

	@Override
	public void create() {
		world = new World(new Vector2(0, -2f), true);
		batch = new SpriteBatch();
		controller = new Controller();
		
		player = createSlimeBody("Models/redslime-right.png", "player", -2f);
		computer = createSlimeBody("Models/blueslime-left.png", "computer", 1.25f);
		
		createBallBody();
		
		npc = new NpcBrain(computer,ball);
		
		playerGoal = createGoalBody("Models/playergoal.png", "playergoal");
		computerGoal = createGoalBody("Models/computergoal.png", "computergoal");
		
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
		
		controller.checkMovement(player);
		npc.MoveNpc();
		player.adjustSpritePosition();
		computer.adjustSpritePosition();
		ball.adjustSpritePosition();

		batch.setProjectionMatrix(camera.combined);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(Constants.PIXELS_TO_METERS, Constants.PIXELS_TO_METERS, 0);
		
		batch.begin();
		
		player.draw(batch);
		computer.draw(batch);
		ball.draw(batch);
		playerGoal.draw(batch);
		computerGoal.draw(batch);
		
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
		player.texture.dispose();
		ball.texture.dispose();
	}
	
	/**
	 * Sets the rules for what happens when various objects contact each other.
	 */
	private void setContactListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				if(collision(contact, "ground", "player")){
					player.airborne = false;
				}
				
				if(collision(contact, "ball", "playergoal")){
					Gdx.app.exit();
				}
			}
			
			@Override
			public void endContact(Contact contact) {
				if(collision(contact, "ground", "player")){
					player.airborne = true;
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
	private Goal createGoalBody(String texturePath, String userDataIdentifier) {
		Goal goal = new Goal(texturePath, userDataIdentifier);
		Body goalBody = world.createBody(goal.bodyDef_body);
		
		goal.createShape(userDataIdentifier);
		goal.setProperties();
		goalBody.createFixture(goal.fixtureDef_body);
		goal.shape_body.dispose();
		goalBody.setUserData(userDataIdentifier);
		
		goalBody = world.createBody(goal.bodyDef_top);
		goalBody.createFixture(goal.fixtureDef_top);
		goal.shape_top.dispose();
		
		return goal;
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
	private Slime createSlimeBody(String texturePath, String userDataIdentifier, float positionOffset) {
		Slime entity = new Slime(texturePath, positionOffset);
		Body slimeBody = world.createBody(entity.bodyDef);
		
		entity.createShape();
		entity.setProperties();
		slimeBody.createFixture(entity.fixtureDef);
		entity.shape.dispose();
		slimeBody.setUserData(userDataIdentifier);
		entity.body = slimeBody;
		
		return entity;
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
