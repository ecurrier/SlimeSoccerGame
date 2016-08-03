package com.slimesoccer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	World world;
	Controller controller;
	OrthographicCamera camera;

	Slime player, computer;

	Ball ball;

	Goal playerGoal, computerGoal;

	Score playerScore;
	Score computerScore;

	Boundary[] boundaries = new Boundary[4];

	NpcBrain npc;

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;

	Texture dot;
	Sprite trajectoryDot;

	Sprite background;

	boolean flaggedForReset = false;

	@Override
	public void create() {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -3f), true);
		controller = new Controller();
		camera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

		createBodies();
		playerScore = new Score("player");
		computerScore = new Score("computer");
		npc = new NpcBrain(computer, ball);

		debugRenderer = new Box2DDebugRenderer();

		trajectoryDot = new Sprite(new Texture(Gdx.files.internal("Models/trajectorydot.png")));
		background = new Sprite(new Texture(Gdx.files.internal("Models/background-day.png")));

		setContactListener();
	}

	@Override
	public void render() {
		camera.update();
		world.step(1f / 60f, 6, 2);
		checkForReset();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		checkDurations();

		controller.checkMovement(player);
		npc.MoveNpcAggressive();

		adjustBodySpritePositions();

		batch.setProjectionMatrix(camera.combined);
		//debugMatrix = batch.getProjectionMatrix().cpy().scale(Constants.PIXELS_TO_METERS, Constants.PIXELS_TO_METERS,
		//		0);

		batch.begin();

		batch.draw(background, -Constants.SCREEN_WIDTH / 2, -Constants.SCREEN_HEIGHT / 2);
		drawAll(batch);

		//displayBallTrajectory();

		batch.end();
		//debugRenderer.render(world, debugMatrix); // Displays body structure
													// lines
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
				if (collision(contact, "ground", "player")) {
					player.airborne = false;
				}

				if (collision(contact, "ground", "computer")) {
					computer.airborne = false;
				}

				if (collision(contact, "ball", "playergoal")) {
					computerScore.incrementScore();
					flaggedForReset = true;
				}

				if (collision(contact, "ball", "computergoal")) {
					playerScore.incrementScore();
					flaggedForReset = true;
				}
			}

			@Override
			public void endContact(Contact contact) {
				if (collision(contact, "ground", "player")) {
					player.airborne = true;
				}
				if (collision(contact, "ground", "computer")) {
					computer.airborne = true;
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
	 * Adjusts the sprite positions of all bodies.
	 */
	private void adjustBodySpritePositions() {
		player.adjustSpritePosition();
		computer.adjustSpritePosition();
		ball.adjustSpritePosition();
	}

	/**
	 * Calls the create methods for Slimes, Goals, Balls, and Boundaries.
	 */
	private void createBodies() {
		player = createSlimeBody(Gdx.files.internal("Models/redslime-right.png"), "player", -2f);
		computer = createSlimeBody(Gdx.files.internal("Models/blueslime-left.png"), "computer", 1.25f);
		playerGoal = createGoalBody(Gdx.files.internal("Models/playergoal.png"), "playergoal");
		computerGoal = createGoalBody(Gdx.files.internal("Models/computergoal.png"), "computergoal");
		ball = createBallBody(Gdx.files.internal("Models/soccerball.png"), "ball");
		createBoundaries();
	}

	/**
	 * Creates the 4 boundaries of the world. The 'ground' is specifically
	 * labeled in order to properly identify when the slime has contact with the
	 * ground in order to identify when the slime can jump again.
	 */
	private void createBoundaries() {
		Body bodyEdgeScreen;

		for (int i = 0; i < 4; i++) {
			boundaries[i] = new Boundary();
			boundaries[i].setProperties(i);

			bodyEdgeScreen = world.createBody(boundaries[i].bodyDef);
			bodyEdgeScreen.createFixture(boundaries[i].fixtureDef);
			boundaries[i].shape.dispose();

			if (i == 0) {
				bodyEdgeScreen.setUserData("ground");
			}
		}
	}

	/**
	 * Creates the goal.
	 * 
	 * @param texturePath
	 *            FileHandle path for the associated asset.
	 * @param userDataIdentifier
	 *            String name given to identify the body for collisions, etc.
	 * @return Goal object.
	 */
	private Goal createGoalBody(FileHandle texturePath, String userDataIdentifier) {
		Goal entity = new Goal(texturePath, userDataIdentifier);
		Body goalBody = world.createBody(entity.bodyDef_body);

		entity.createShape(userDataIdentifier);
		entity.setProperties();
		goalBody.createFixture(entity.fixtureDef_body);
		entity.shape_body.dispose();
		goalBody.setUserData(userDataIdentifier);

		goalBody = world.createBody(entity.bodyDef_top);
		goalBody.createFixture(entity.fixtureDef_top);
		entity.shape_top.dispose();

		return entity;
	}

	/**
	 * Creates the ball.
	 * 
	 * @param texturePath
	 *            FileHandle path for the associated asset.
	 * @param userDataIdentifier
	 *            String name given to identify the body for collisions, etc.
	 * @return Ball object.
	 */
	private Ball createBallBody(FileHandle texturePath, String userDataIdentifier) {
		Ball entity = new Ball(texturePath);
		Body ballBody = world.createBody(entity.bodyDef);

		entity.createShape();
		entity.setProperties();
		ballBody.createFixture(entity.fixtureDef);
		entity.shape.dispose();
		ballBody.setUserData(userDataIdentifier);
		entity.body = ballBody;

		return entity;
	}

	/**
	 * Creates the slime.
	 * 
	 * @param texturePath
	 *            FileHandle path for the associated asset.
	 * @param userDataIdentifier
	 *            String name given to identify the body for collisions, etc.
	 * @param positionOffset
	 *            float amount to offset the spawn location of the body.
	 * @return Slime object.
	 */
	private Slime createSlimeBody(FileHandle texturePath, String userDataIdentifier, float positionOffset) {
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

	/**
	 * Determines if there is a collision between two bodies.
	 * 
	 * @param contact
	 *            Contact object that contains fixtures/bodies of current
	 *            collision.
	 * @param bodyA
	 *            String name of the first body.
	 * @param bodyB
	 *            String name of the second body.
	 * @return True or False.
	 */
	private boolean collision(Contact contact, String bodyA, String bodyB) {
		if ((contact.getFixtureA().getBody().getUserData() == bodyA
				&& contact.getFixtureB().getBody().getUserData() == bodyB)
				|| (contact.getFixtureA().getBody().getUserData() == bodyB
						&& contact.getFixtureB().getBody().getUserData() == bodyA)) {
			return true;
		}

		return false;
	}

	/**
	 * Checks all active durations and takes action accordingly if they have
	 * exceeded their duration.
	 */
	private void checkDurations() {
		if (player.boostActive) {
			float currentDuration = ((System.nanoTime() - player.boostStart) / Constants.NANO);
			if (currentDuration >= (Constants.BOOST_DURATION)) {
				player.setBoost(false);
			}
		}
	}

	/**
	 * Displays a parabolic trajectory for the ball when enabled
	 */
	@SuppressWarnings("unused")
	private void displayBallTrajectory() {
		for (int n = 1; n <= 32; n++) {
			float t = 6f / 60f;
			Vector2 stepVelocity = ball.body.getLinearVelocity().scl(t);
			Vector2 stepGravity = world.getGravity().scl(t * t);

			Vector2 calculation = (ball.body.getPosition().add(stepVelocity.scl(n))
					.add(stepGravity.scl(0.5f * (n * n + n))));
			batch.draw(trajectoryDot, calculation.x * Constants.PIXELS_TO_METERS,
					calculation.y * Constants.PIXELS_TO_METERS);
		}
	}

	/**
	 * Draws all bodies/sprites to the screen.
	 * 
	 * @param batch
	 *            SpriteBatch
	 */
	private void drawAll(SpriteBatch batch) {
		player.draw(batch);
		computer.draw(batch);
		ball.draw(batch);
		playerGoal.draw(batch);
		computerGoal.draw(batch);
		playerScore.draw(batch);
		computerScore.draw(batch);
	}

	public void resetPositions() {
		player.reset();
		computer.reset();
		ball.reset();
	}

	public void checkForReset() {
		if (flaggedForReset) {
			resetPositions();
			flaggedForReset = false;
		}
	}
}
