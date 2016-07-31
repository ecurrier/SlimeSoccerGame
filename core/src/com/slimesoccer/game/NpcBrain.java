package com.slimesoccer.game;

public class NpcBrain {

	private Slime npc;
	private Ball ball;

	NpcBrain(Slime npc, Ball ball) {
		this.npc = npc;
		this.ball = ball;
	}

	public void MoveNpc() {
		float ball_x = ball.body.getPosition().x;
		float npc_x = npc.body.getPosition().x;

		if (npc_x > ball_x + .2 && npc.body.getLinearVelocity().x > -npc.maxSpeed)
			npc.body.applyLinearImpulse(-0.80f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
					npc.body.getPosition().y, true);
		else if (npc_x < ball_x + .2&& npc.body.getLinearVelocity().x < npc.maxSpeed)
			npc.body.applyLinearImpulse(0.80f / Constants.PIXELS_TO_METERS, 0, npc.body.getPosition().x,
					npc.body.getPosition().y, true);
	}
}
