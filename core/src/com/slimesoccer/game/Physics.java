package com.slimesoccer.game;

import java.util.Random;

public class Physics {

	public Physics() {

	}

	public void commandMoveRight(Slime slime, float x, float y) {
		slime.body.applyLinearImpulse(Constants.MOVE_VELOCITY / Constants.PIXELS_TO_METERS, 0, x, y, true);
	}

	public void commandMoveLeft(Slime slime, float x, float y) {
		slime.body.applyLinearImpulse(-Constants.MOVE_VELOCITY / Constants.PIXELS_TO_METERS, 0, x, y, true);
	}

	public void commandJump(Slime slime, float x, float y) {
		slime.body.applyLinearImpulse(0f, Constants.JUMP_VELOCITY / Constants.PIXELS_TO_METERS, x, y, true);
	}

	public boolean calculateBallCentered(boolean rightSide, float slimeX, float ballX) {
		float offsetLeft = 0.24f;
		float offsetRight = 0.26f;
		if (rightSide) {
			offsetLeft *= -1;
			offsetRight *= -1;
		}

		if (slimeX + offsetLeft < ballX && slimeX + offsetRight > ballX) {
			return true;
		}

		return false;
	}

	public boolean calculateBallLeft(boolean rightSide, Slime slime, float slimeX, float ballX) {
		float offset = 0.2f;
		if (!rightSide) {
			offset = -0.2f;
		}

		if (slimeX > ballX + offset && slime.body.getLinearVelocity().x > -slime.maxSpeed) {
			return true;
		}

		return false;
	}

	public boolean calculateBallRight(boolean rightSide, Slime slime, float slimeX, float ballX) {
		float offset = 0.2f;
		if (!rightSide) {
			offset = -0.2f;
		}

		if (slimeX < ballX + offset && slime.body.getLinearVelocity().x < slime.maxSpeed) {
			return true;
		}

		return false;
	}

	public boolean calculateJump(Slime slime, float slimeX, float slimeY, float ballX, float ballY) {
		Random rand = new Random();
		float yOffset = rand.nextFloat() * (1.25f - 0.75f) + 0.75f;

		if ((ballY < (slimeY + yOffset) && !slime.airborne) && ((slimeX + 0.5f) > ballX) && ((slimeX - 0.5f) < ballX)) {
			return true;
		}

		return false;
	}

	public boolean calculateBallHighSpeed(Ball ball, Slime slime) {
		if ((ball.body.getLinearVelocity().x > 3.5f || ball.body.getLinearVelocity().x < -3.5f)
				&& slime.body.getLinearVelocity().x < slime.maxSpeed && slime.body.getLinearVelocity().x > -slime.maxSpeed) {
			return true;
		}

		return false;
	}

	public boolean calculateBallUnderSlime(Slime slime, Ball ball, float slimeLeft, float slimeRight, float slimeY, float ballX, float ballY) {
		if (ballX > (slimeLeft - ((ball.sprite.getWidth() / 2) / Constants.PIXELS_TO_METERS)) && ballX < slimeRight
				&& ballY < slimeY && slime.body.getLinearVelocity().x < slime.maxSpeed && slime.airborne) {
			return true;
		}

		return false;
	}

}
