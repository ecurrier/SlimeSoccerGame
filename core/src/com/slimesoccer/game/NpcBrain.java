package com.slimesoccer.game;

import com.badlogic.gdx.Gdx;

public class NpcBrain {

	private Slime npc;
	private Ball ball;
	
	private float ball_x,
		ball_y,
		npc_x,
		npc_y,
		npc_left,
		npc_right;

	NpcBrain(Slime npc, Ball ball) {
		this.npc = npc;
		this.ball = ball;
	}

	public void MoveNpcEasy() {
		float ball_x = ball.body.getPosition().x;
		float npc_x = npc.body.getPosition().x;

		if (npc_x > ball_x + .2 && npc.body.getLinearVelocity().x > -npc.maxSpeed)
			npc.body.applyLinearImpulse(-0.80f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
					npc.body.getPosition().y, true);
		else if (npc_x < ball_x + .2 && npc.body.getLinearVelocity().x < npc.maxSpeed)
			npc.body.applyLinearImpulse(0.80f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
					npc.body.getPosition().y, true);
	}

	public void MoveNpcHard() {
		float ball_x = ball.body.getPosition().x;
		float npc_x = npc.body.getPosition().x;

		float ball_y = ball.body.getPosition().y;
		float npc_y = npc.body.getPosition().y;

		// Check if ball is going left or right
		if (ball.body.getLinearVelocity().x <= 0) {
			// ball is going left

			// check if jump is necessary
			if (!npc.airborne && ball.body.getLinearVelocity().y < 0) {
				// jump
				if (npc_y > ball_y + 10 / Constants.PIXELS_TO_METERS
						&& npc.body.getLinearVelocity().x > -npc.maxSpeed) {
					npc.body.applyLinearImpulse(-.8f / Constants.PIXELS_TO_METERS, 1.00f / Constants.PIXELS_TO_METERS,
							npc.body.getPosition().x, npc.body.getPosition().y, true);
				} else if (npc.body.getLinearVelocity().x < npc.maxSpeed && !npc.airborne) {
					npc.body.applyLinearImpulse(.8f / Constants.PIXELS_TO_METERS, 1.00f / Constants.PIXELS_TO_METERS,
							npc.body.getPosition().x, npc.body.getPosition().y, true);
				}
			} else {
				// no jump
				if (npc_x > ball_x + 10 / Constants.PIXELS_TO_METERS
						&& npc.body.getLinearVelocity().x > -npc.maxSpeed) {
					npc.body.applyLinearImpulse(-.8f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
							npc.body.getPosition().y, true);
				} else if (npc_x < ball_x + 10 / Constants.PIXELS_TO_METERS
						&& npc.body.getLinearVelocity().x < npc.maxSpeed) {
					npc.body.applyLinearImpulse(.8f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
							npc.body.getPosition().y, true);
				}
			}
		} else {
			// ball is going right

			if ( ball.body.getLinearVelocity().y < 0 && npc.body.getLinearVelocity().x < npc.maxSpeed
					&& !npc.airborne)
				npc.body.applyLinearImpulse(.8f / Constants.PIXELS_TO_METERS, 1.00f / Constants.PIXELS_TO_METERS,
						npc.body.getPosition().x, npc.body.getPosition().y, true);
			else if (npc.body.getLinearVelocity().x < npc.maxSpeed)
				npc.body.applyLinearImpulse(.8f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
						npc.body.getPosition().y, true);

		}

	}
	
	public void MoveNpcAggressive(){
		ball_x = ball.body.getPosition().x;
		npc_x = npc.body.getPosition().x;
		ball_y = ball.body.getPosition().y;
		npc_y = npc.body.getPosition().y;
		
		npc_left = npc_x - ((npc.sprite.getWidth()/2)/Constants.PIXELS_TO_METERS);
		npc_right = npc_left + (npc.sprite.getWidth()/Constants.PIXELS_TO_METERS);
		
		if(calculateBallHighSpeed()){
			commandMoveRight();
		}
		else if(calculateBallUnderSlime()){
			commandMoveRight();
		}
		else{
			if (calculateBallLeft()){	
				commandMoveLeft();
			}
			else if (calculateBallRight()){
				commandMoveRight();
			}
		}
		
		if(calculateJump()){
			commandJump();
		}
	}
	
	public void commandMoveRight(){
		npc.body.applyLinearImpulse(Constants.MOVE_VELOCITY / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
				npc.body.getPosition().y, true);
	}
	
	public void commandMoveLeft(){
		npc.body.applyLinearImpulse(-Constants.MOVE_VELOCITY / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
				npc.body.getPosition().y, true);
	}
	
	public void commandJump(){
		npc.body.applyLinearImpulse(0f, Constants.JUMP_VELOCITY / Constants.PIXELS_TO_METERS,
				npc.body.getPosition().x, npc.body.getPosition().y, true);
	}
	
	public boolean calculateBallLeft(){
		if(npc_x > ball_x + .2 && npc.body.getLinearVelocity().x > -npc.maxSpeed){
			return true;
		}
		
		return false;
	}
	
	public boolean calculateBallRight(){
		if(npc_x < ball_x + .2 && npc.body.getLinearVelocity().x < npc.maxSpeed){
			return true;
		}
		
		return false;
	}
	
	public boolean calculateJump(){
		if((ball_y < (npc_y + 1) && !npc.airborne) &&
		  ((npc_x + 0.5f) > ball_x) && ((npc_x - 0.5f) < ball_x)){
			return true;
		}
		
		return false;
	}
	
	public boolean calculateBallHighSpeed(){
		if((ball.body.getLinearVelocity().x > 3.5f || ball.body.getLinearVelocity().x < -3.5f) &&
			npc.body.getLinearVelocity().x < npc.maxSpeed){
			return true;
		}
		
		return false;
	}
	
	public boolean calculateBallUnderSlime(){
		if(ball_x > (npc_left - ((ball.sprite.getWidth()/2)/Constants.PIXELS_TO_METERS)) && 
			ball_x < npc_right &&
		    ball_y < npc_y &&
		    npc.body.getLinearVelocity().x < npc.maxSpeed &&
		    npc.airborne){
		    	return true;
	    }
		   
		return false;
	}
}
