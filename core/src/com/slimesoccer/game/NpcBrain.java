package com.slimesoccer.game;

public class NpcBrain {

	private Slime npc;
	private Ball ball;
	private boolean rightSide;
	private Physics physics;

	private float ball_x, ball_y, npc_x, npc_y, npc_left, npc_right;

	NpcBrain(Slime npc, Ball ball, boolean rightSide) {
		this.npc = npc;
		this.ball = ball;
		this.rightSide = rightSide;
		this.physics = new Physics();
	}

	public void MoveNpcAggressive() {
		ball_x = ball.body.getPosition().x;
		npc_x = npc.body.getPosition().x;
		ball_y = ball.body.getPosition().y;
		npc_y = npc.body.getPosition().y;

		npc_left = npc_x - ((npc.sprite.getWidth() / 2) / Constants.PIXELS_TO_METERS);
		npc_right = npc_left + (npc.sprite.getWidth() / Constants.PIXELS_TO_METERS);

		if (physics.calculateBallHighSpeed(ball, npc)) {
			if (rightSide) {
				physics.commandMoveRight(npc, npc_x, npc_y);
			} else {
				physics.commandMoveLeft(npc, npc_x, npc_y);
			}
		} else if (physics.calculateBallUnderSlime(npc, ball, npc_left, npc_right, npc_y, ball_x, ball_y)) {
			if (rightSide) {
				physics.commandMoveRight(npc, npc_x, npc_y);
				ball.body.applyLinearImpulse(-0.002f, 0f, ball_x, ball_y, true);
			} else {
				physics.commandMoveLeft(npc, npc_x, npc_y);
				ball.body.applyLinearImpulse(0.002f, 0f, ball_x, ball_y, true);
			}
		} else {
			if (physics.calculateBallCentered(rightSide, npc_x, ball_x)) {
				npc.body.setLinearVelocity(0, npc.body.getLinearVelocity().y);
			} else if (physics.calculateBallLeft(rightSide, npc, npc_x, ball_x)) {
				physics.commandMoveLeft(npc, npc_x, npc_y);
			} else if (physics.calculateBallRight(rightSide, npc, npc_x, ball_x)) {
				physics.commandMoveRight(npc, npc_x, npc_y);
			}
		}

		if (physics.calculateJump(npc, npc_x, npc_y, ball_x, ball_y)) {
			physics.commandJump(npc, npc_x, npc_y);
		}
	}
}
