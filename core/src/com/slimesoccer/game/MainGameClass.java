package com.slimesoccer.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MainGameClass extends ApplicationAdapter {
	SpriteBatch batch;
	Sprite sprite, ballSprite;
	
	Physics physics;
	Slime slime;
	Ball ball;
	
    World world;
    Body slimeBody, ballBody;
    Body bodyEdgeScreen;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    OrthographicCamera camera;
    
    float torque = 0.0f;
    boolean drawSprite = true;

    final float PIXELS_TO_METERS = 100f;

	@Override
	public void create () {
		physics = new Physics(0.33f);
		
		batch = new SpriteBatch();
		slime = new Slime("Models/redslime-right.png", 100, 0);
		ball = new Ball("Models/soccerball.png", 320, 200);
		
        sprite = new Sprite(slime.texture);
        sprite.setPosition(-sprite.getWidth()/2,-sprite.getHeight()/2);
        
        ballSprite = new Sprite(ball.texture);
        ballSprite.setPosition(-ballSprite.getWidth()/2, -ballSprite.getHeight()/2);

        world = new World(new Vector2(0, -2f),true);
        
        /* SLIME */

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth()/2) /
                        PIXELS_TO_METERS,
                (sprite.getY() + sprite.getHeight()/2) / PIXELS_TO_METERS);

        slimeBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        
        float[] vertices = {-0.3f, -0.15f,
        					-0.28f, -0.05f,
        					-0.2f, 0.05f,
        					-0.1f, 0.13f,
        					0.1f, 0.13f,
        					0.2f, 0.05f,
        					0.28f, -0.05f,
        					0.3f, -0.15f};
        
        shape.set(vertices);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.0f;

        slimeBody.createFixture(fixtureDef);
        shape.dispose();
        
        /* BALL */
        
        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set((ballSprite.getX() + ballSprite.getWidth()/2) / PIXELS_TO_METERS,
        					 (ballSprite.getY() + ballSprite.getHeight()/2) / PIXELS_TO_METERS);
        
        ballBody = world.createBody(ballDef);
        
        PolygonShape ballShape = new PolygonShape();
        
        float[] ballVertices = {-0.075f, 0f,
								-0.05f, 0.05f,
								0f, 0.075f,
								0.05f, 0.05f,
								0.075f, 0f,
								0.05f, -0.025f,
								0f, -0.075f,
								-0.05f, -0.05f};
        
        ballShape.set(ballVertices);
        
        FixtureDef ballFixtureDef = new FixtureDef();
        ballFixtureDef.shape = shape;
        ballFixtureDef.density = 0.1f;
        ballFixtureDef.restitution = 0.9f;
        
        ballBody.createFixture(ballFixtureDef);
        ballShape.dispose();

        /* GROUND */
        
        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyDef.BodyType.StaticBody;
        float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS- 50/PIXELS_TO_METERS;
        bodyDef2.position.set(0,0);
        FixtureDef fixtureDef2 = new FixtureDef();

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-w/2,-h/2,w/2,-h/2);
        fixtureDef2.shape = edgeShape;

        bodyEdgeScreen = world.createBody(bodyDef2);
        bodyEdgeScreen.createFixture(fixtureDef2);
        edgeShape.dispose();
        
        
        
        

        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
                getHeight());
	}

	@Override
	public void render () {
		/*
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(slime.getY() > 0.0) {
			slime.airborne = true;
		}
		
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if(!slime.airborne) {
				slime.speedY += 5;
			}
		}
		
		physics.applyPhysics(slime);
		physics.applyPhysics(ball);
		checkBounds();
		checkOverlap();
		
		if (Gdx.input.isKeyPressed(Keys.A)) {
			slime.speedX -= 0.5;
		}
		else if (Gdx.input.isKeyPressed(Keys.D)) {
			slime.speedX += 0.5;
		}
		sprite.setPosition(body.getPosition().x, body.getPosition().y);
		
		batch.begin();
		batch.draw(slime.texture, slime.getX(), slime.getY());
		batch.draw(ball.texture, ball.getX(), ball.getY());
		batch.end();
		*/
		
		camera.update();
        // Step the physics simulation forward at a rate of 60hz
        world.step(1f/60f, 6, 2);

        slimeBody.applyTorque(torque,true);
        ballBody.applyTorque(torque, true);

        sprite.setPosition((slimeBody.getPosition().x * PIXELS_TO_METERS) - sprite.
                        getWidth()/2 ,
                (slimeBody.getPosition().y * PIXELS_TO_METERS) -sprite.getHeight()/2 )
        ;
        sprite.setRotation((float)Math.toDegrees(slimeBody.getAngle()));
        
        ballSprite.setPosition((ballBody.getPosition().x * PIXELS_TO_METERS) - ballSprite.
                getWidth()/2 ,
        (ballBody.getPosition().y * PIXELS_TO_METERS) -ballSprite.getHeight()/2 )
        ;
        ballSprite.setRotation((float)Math.toDegrees(ballBody.getAngle()));
        
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        batch.begin();

        batch.draw(sprite, sprite.getX(), sprite.getY(),sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(),sprite.getHeight(),sprite.getScaleX(),sprite.
                        getScaleY(),sprite.getRotation());
        
        batch.draw(ballSprite, ballSprite.getX(), ballSprite.getY(),ballSprite.getOriginX(),
        		ballSprite.getOriginY(),
        		ballSprite.getWidth(),ballSprite.getHeight(),ballSprite.getScaleX(),ballSprite.
                        getScaleY(),ballSprite.getRotation());

        batch.end();
        debugRenderer.render(world, debugMatrix);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		slime.texture.dispose();
		ball.texture.dispose();
	}
	
	public void checkBounds () {
		slime.checkBoundaries();
		ball.checkBoundaries();
		
	}
	
	public void checkOverlap() {
		Circle slimeBounds = new Circle((int)slime.getX() + ((int)slime.actor.getWidth()/2), (int)slime.getY(), (int)slime.actor.getHeight());
		Circle ballBounds = new Circle((int)ball.getX() + ((int)ball.actor.getWidth()/2), (int)ball.getY() + ((int)ball.actor.getHeight()/2), (int)ball.actor.getHeight()/2);
		
		float xD = slime.getX() - ball.getX();
		float yD = slime.getY() - ball.getY();
		float sqDist = xD * xD + yD * yD;
		boolean collision = sqDist <= (slimeBounds.radius + ballBounds.radius) * (slimeBounds.radius + ballBounds.radius);
		
		if(collision) {
			ball.speedX = ball.speedX * -0.75f;
			ball.speedY = ball.speedY * -0.75f;
			
			ball.speedX += slime.speedX * 0.5f;
			ball.speedY += slime.speedY * 0.5f;
		}
	}
}
