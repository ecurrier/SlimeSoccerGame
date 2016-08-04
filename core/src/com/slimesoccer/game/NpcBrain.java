package com.slimesoccer.game;

import java.util.Random;

public class NpcBrain {

	private Slime npc;
	private Ball ball;
	private boolean rightSide;

	private float ball_x, ball_y, npc_x, npc_y, npc_left, npc_right;

	NpcBrain(Slime npc, Ball ball, boolean rightSide) {
		this.npc = npc;
		this.ball = ball;
		this.rightSide = rightSide;
	}

	public void MoveNpcAggressive() {
		ball_x = ball.body.getPosition().x;
		npc_x = npc.body.getPosition().x;
		ball_y = ball.body.getPosition().y;
		npc_y = npc.body.getPosition().y;

		npc_left = npc_x - ((npc.sprite.getWidth() / 2) / Constants.PIXELS_TO_METERS);
		npc_right = npc_left + (npc.sprite.getWidth() / Constants.PIXELS_TO_METERS);

		if (calculateBallHighSpeed()) {
			if (rightSide) {
				commandMoveRight();
			} else {
				commandMoveLeft();
			}
		} else if (calculateBallUnderSlime()) {
			if (rightSide) {
				commandMoveRight();
			} else {
				commandMoveLeft();
			}
		} else {
			if (calculateBallCentered()) {
				npc.body.setLinearVelocity(0, npc.body.getLinearVelocity().y);
			} else if (calculateBallLeft()) {
				commandMoveLeft();
			} else if (calculateBallRight()) {
				commandMoveRight();
			}
		}

		if (calculateJump()) {
			commandJump();
		}
	}

	public void commandMoveRight() {
		npc.body.applyLinearImpulse(Constants.MOVE_VELOCITY / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
				npc.body.getPosition().y, true);
	}

	public void commandMoveLeft() {
		npc.body.applyLinearImpulse(-Constants.MOVE_VELOCITY / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
				npc.body.getPosition().y, true);
	}

	public void commandJump() {
		npc.body.applyLinearImpulse(0f, Constants.JUMP_VELOCITY / Constants.PIXELS_TO_METERS, npc.body.getPosition().x,
				npc.body.getPosition().y, true);
	}

	public boolean calculateBallCentered() {
		float offsetLeft = 0.175f;
		float offsetRight = 0.225f;
		if (rightSide) {
			offsetLeft *= -1;
			offsetRight *= -1;
		}

		if (npc_x + offsetLeft < ball_x && npc_x + offsetRight > ball_x) {
			return true;
		}

		return false;
	}

	public boolean calculateBallLeft() {
		float offset = 0.2f;
		if (!rightSide) {
			offset = -0.2f;
		}

		if (npc_x > ball_x + offset && npc.body.getLinearVelocity().x > -npc.maxSpeed) {
			return true;
		}

		return false;
	}

	public boolean calculateBallRight() {
		float offset = 0.2f;
		if (!rightSide) {
			offset = -0.2f;
		}

		if (npc_x < ball_x + offset && npc.body.getLinearVelocity().x < npc.maxSpeed) {
			return true;
		}

		return false;
	}

	public boolean calculateJump() {
		Random rand = new Random();
		float yOffset = rand.nextFloat() * (1.25f - 0.75f) + 0.75f;

		if ((ball_y < (npc_y + yOffset) && !npc.airborne) && ((npc_x + 0.5f) > ball_x) && ((npc_x - 0.5f) < ball_x)) {
			return true;
		}

		return false;
	}

	public boolean calculateBallHighSpeed() {
		if ((ball.body.getLinearVelocity().x > 3.5f || ball.body.getLinearVelocity().x < -3.5f)
				&& npc.body.getLinearVelocity().x < npc.maxSpeed && npc.body.getLinearVelocity().x > -npc.maxSpeed) {
			return true;
		}

		return false;
	}

	public boolean calculateBallUnderSlime() {
		if (ball_x > (npc_left - ((ball.sprite.getWidth() / 2) / Constants.PIXELS_TO_METERS)) && ball_x < npc_right
				&& ball_y < npc_y && npc.body.getLinearVelocity().x < npc.maxSpeed && npc.airborne) {
			return true;
		}

		return false;
	}
}
