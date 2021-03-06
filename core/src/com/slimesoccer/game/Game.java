package com.slimesoccer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
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
	float timeStep;

	Slime player, computer;
	NpcBrain computerBrain, playerBrain;
	Ball ball;
	Goal playerGoal, computerGoal;
	SoundEffects soundEffects;

	Score playerScore, computerScore;
	int scoreLimit = 5, currentCountDownTime;

	Text gameOverText, countDownText;

	Boundary[] boundaries = new Boundary[4];

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	GameOptions options;

	Sprite trajectoryDot, background;

	boolean flaggedForReset = false, realGame = false, ballUnderPlayer = false, ballUnderComputer = false,
			ballOnGround = false, drawCountDownTime = false;

	public enum State {
		PAUSE, RUN, RESUME, STOPPED
	}

	private State state = State.RUN;

	public interface MyGameCallBack {
		public void startActivity();
	}

	private MyGameCallBack myGameCallBack;

	public void setMyGameCallBack(MyGameCallBack callBack) {
		myGameCallBack = callBack;
	}

	public Game(boolean realGame, GameOptions options) {
		this.realGame = realGame;
		this.options = options;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		world = new World(new Vector2(0, -3f), true);
		controller = new Controller();
		soundEffects = new SoundEffects(options != null ? options.playSound : true);
		camera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		timeStep = 1f / 60f;

		createBodies();
		controller.ball = ball;
		playerScore = new Score("player");
		if (!realGame) {
			playerBrain = new NpcBrain(player, ball, false);
		}
		computerScore = new Score("computer");
		computerBrain = new NpcBrain(computer, ball, true);

		debugRenderer = new Box2DDebugRenderer();

		trajectoryDot = new Sprite(new Texture(Gdx.files.internal("Models/trajectorydot.png")));
		background = new Sprite(new Texture(Gdx.files.internal("Models/background-day.png")));

		setContactListener();
		soundEffects.CrowdStart();
	}

	@Override
	public void render() {
		camera.update();
		world.step(timeStep, 6, 2);
		checkForReset();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (state == State.RUN) {
			checkDurations();
			moveBodies();
			adjustBodySpritePositions();
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		drawAll(batch);
		if (options != null && options.showTrajectory) {
			displayBallTrajectory();
		}
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		player.texture.dispose();
		ball.texture.dispose();
		soundEffects.CrowdStop();
	}

	/**
	 * Sets the rules for what happens when various objects contact each other.
	 */
	private void setContactListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				// Airborne checks
				if (collision(contact, "ground", "player")) {
					player.airborne = false;
				}

				if (collision(contact, "ground", "computer")) {
					computer.airborne = false;
				}

				// Score checks
				if (collision(contact, "ball", "playergoal")) {
					if(realGame) {
						computerScore.incrementScore();
					}
					flaggedForReset = true;
					soundEffects.GoalScore();
				}
				if (collision(contact, "ball", "computergoal")) {
					if(realGame) {
						playerScore.incrementScore();
					}
					flaggedForReset = true;
					soundEffects.GoalScore();
				}

				// Ball Stuck checks
				if (collision(contact, "ball", "player")) {
					ballUnderPlayer = true;
					soundEffects.SlimeContact();
				}
				if (collision(contact, "ball", "computer")) {
					ballUnderComputer = true;
					soundEffects.SlimeContact();
				}
				if (collision(contact, "ball", "ground")) {
					ballOnGround = true;
					soundEffects.BallBounce();
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
				if (collision(contact, "ball", "player")) {
					ballUnderPlayer = false;
				}
				if (collision(contact, "ball", "computer")) {
					ballUnderComputer = false;
				}
				if (collision(contact, "ball", "ground")) {
					ballOnGround = false;
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

	private void checkBallSquished() {
		float ballX = ball.body.getPosition().x;
		float ballY = ball.body.getPosition().y;

		if (ballUnderPlayer && ballOnGround) {
			ball.body.applyLinearImpulse(0.001f, 0f, ballX, ballY, true);
		} else if (ballUnderComputer && ballOnGround) {
			ball.body.applyLinearImpulse(-0.001f, 0f, ballX, ballY, true);
		}
	}

	private void moveBodies() {
		if (realGame) {
			controller.checkMovement(player);
		} else {
			playerBrain.MoveNpcAggressive();
		}
		computerBrain.MoveNpcAggressive();

		checkBallSquished();
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
		batch.draw(background, -Constants.SCREEN_WIDTH / 2, -Constants.SCREEN_HEIGHT / 2);
		player.draw(batch);
		computer.draw(batch);
		ball.draw(batch);
		playerGoal.draw(batch);
		computerGoal.draw(batch);

		if (realGame) {
			playerScore.draw(batch);
			computerScore.draw(batch);
		}

		if(drawCountDownTime){
			Text countDown = new Text(Integer.toString(currentCountDownTime));
			countDown.font.draw(batch, countDown.message, countDown.x, countDown.y);
		}
		
		if (state == State.STOPPED) {
			drawGameOverText();
		}
	}

	public void resetPositions() {
		player.reset();
		computer.reset();
		ball.reset();
	}

	public void checkForReset() {
		checkForWin();

		if (flaggedForReset) {
			resetPositions();
			adjustBodySpritePositions();
			countDownAfterGoal(3);
			flaggedForReset = false;
		}
	}

	public void resetGame() {
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			state = State.RUN;
			playerScore.resetScore();
			computerScore.resetScore();
		} else if (Gdx.app.getType() == ApplicationType.Android) {
			dispose();
			myGameCallBack.startActivity();
		}
	}

	public void checkForWin() {
		if (playerScore.score >= scoreLimit) {
			endGame("Player");
			flaggedForReset = false;
		} else if (computerScore.score >= scoreLimit) {
			endGame("Computer");
			flaggedForReset = false;
		}
	}

	public void drawGameOverText() {
		gameOverText.font.draw(batch, gameOverText.message, gameOverText.x, gameOverText.y);
	}

	public void endGame(String winner) {
		state = State.STOPPED;
		gameOverText = new Text(winner + " wins!!!");
		resetPositions();
		adjustBodySpritePositions();
		countDownTimer(5);
	}

	/**
	 * Initializes a countdown after a goal has been scored. Play will resume as normal after the countdown has finished.
	 * @param time Number in seconds the countdown should run for.
	 */
	public void countDownAfterGoal(final int time) {

		currentCountDownTime = time;
		timeStep = 0f;
		state = State.PAUSE;
		new Thread(new Runnable() {
			int threadTime = time;
			boolean timerRunning = true;

			@Override
			public void run() {
				while (timerRunning) {
					try {
						if (threadTime == 0) {
							timerRunning = false;
							timeStep = 1f / 60f;
							state = State.RUN;
							drawCountDownTime = false;
							soundEffects.RoundBegin();
							return;
						} else {
							drawCountDownTime = true;
						}

						threadTime--;
						Thread.sleep(1000);
						currentCountDownTime--;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

		}).start();
	}

	/**
	 * Starts a countdown timer after a player has scored the winning goal. The game will reset after the countdown is finished.
	 * @param time Number in seconds the timer should run for
	 */
	public void countDownTimer(final int time) {

		timeStep = 0f;
		new Thread(new Runnable() {
			int threadTime = time;
			boolean timerRunning = true;

			@Override
			public void run() {
				while (timerRunning) {
					try {
						Thread.sleep(1000);
						if (threadTime == 0) {
							timerRunning = false;
							resetGame();
							timeStep = 1f / 60f;
							return;
						} else {
							threadTime--;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

		}).start();
	}
}
