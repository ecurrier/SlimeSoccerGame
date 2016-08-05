package com.slimesoccer.game;

public class NpcBrain {

	private Slime npc;
	private Ball ball;
	private boolean rightSide;
	private Physics physics;

	private float ball_x, ball_y, npc_x, npc_y;

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

		if (physics.calculateBallHighSpeed(ball, npc)) {
			if (rightSide) {
				physics.commandMoveRight(npc, npc_x, npc_y);
			} else {
				physics.commandMoveLeft(npc, npc_x, npc_y);
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
