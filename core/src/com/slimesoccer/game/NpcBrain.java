package com.slimesoccer.game;

import com.badlogic.gdx.Gdx;

public class NpcBrain {

	private Slime npc;
	private Ball ball;

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
			
			//check if jump is necessary
			if (npc_y - ball_y > 20 / Constants.PIXELS_TO_METERS && !npc.airborne)
				//jump
				if (npc_x > ball_x + 10 / Constants.PIXELS_TO_METERS && npc.body.getLinearVelocity().x > -npc.maxSpeed)
					npc.body.applyLinearImpulse(-.8f / Constants.PIXELS_TO_METERS, 1.00f/Constants.PIXELS_TO_METERS, npc.body.getPosition().x,
							npc.body.getPosition().y, true);
				else if (npc_x < ball_x + 10 / Constants.PIXELS_TO_METERS
						&& npc.body.getLinearVelocity().x < npc.maxSpeed && !npc.airborne)
					npc.body.applyLinearImpulse(.8f / Constants.PIXELS_TO_METERS, 1.00f/Constants.PIXELS_TO_METERS, npc.body.getPosition().x,
							npc.body.getPosition().y, true);
				else {
					//no jump
					if (npc_x > ball_x + 10 / Constants.PIXELS_TO_METERS
							&& npc.body.getLinearVelocity().x > -npc.maxSpeed)
						npc.body.applyLinearImpulse(-.8f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
								npc.body.getPosition().y, true);
					else if (npc_x < ball_x + 10 / Constants.PIXELS_TO_METERS
							&& npc.body.getLinearVelocity().x < npc.maxSpeed)
						npc.body.applyLinearImpulse(.8f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
								npc.body.getPosition().y, true);
				}
		} else {
			// ball is going right

			if (npc_y - ball_y > 20 / Constants.PIXELS_TO_METERS  && npc.body.getLinearVelocity().x < npc.maxSpeed  && !npc.airborne)
				npc.body.applyLinearImpulse(.8f / Constants.PIXELS_TO_METERS, 1.00f/Constants.PIXELS_TO_METERS, npc.body.getPosition().x,
						npc.body.getPosition().y, true);
			else if (npc.body.getLinearVelocity().x < npc.maxSpeed)
				npc.body.applyLinearImpulse(.8f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
						npc.body.getPosition().y, true);

		}

	}
}
