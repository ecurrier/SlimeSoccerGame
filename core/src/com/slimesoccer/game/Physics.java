package com.slimesoccer.game;


public class Physics {
	
	public float gravityConstant;
	
	public Physics(float gravity) {
		gravityConstant = gravity;
	}
	
	public void applyPhysics(Slime slime) {
		// Apply air resistance
		slime.speedX = slime.speedX * 0.90f;
		
		// Apply gravity
		slime.speedY -= gravityConstant;
		
		// Update position
		slime.setY(slime.getY() + slime.speedY);
		slime.setX(slime.getX() + slime.speedX);
		
		if(slime.speedX > slime.maxSpeed) {
			slime.speedX = slime.maxSpeed;
		}
		else if(slime.speedX < -slime.maxSpeed) {
			slime.speedX = -slime.maxSpeed;
		}
	}

	public void applyPhysics(Ball ball) {
		// Apply air resistance
		ball.speedX = ball.speedX * 0.98f;
		
		// Apply gravity
		ball.speedY -= gravityConstant;
		
		// Update position
		ball.setY(ball.getY() + ball.speedY);
		ball.setX(ball.getX() + ball.speedX);
		
		if(ball.speedX > ball.maxSpeed) {
			ball.speedX = ball.maxSpeed;
		}
		else if(ball.speedX < -ball.maxSpeed) {
			ball.speedX = -ball.maxSpeed;
		}
	}
}
