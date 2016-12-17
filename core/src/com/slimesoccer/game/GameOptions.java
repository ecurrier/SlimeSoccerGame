package com.slimesoccer.game;

public class GameOptions {
	
	String difficulty;
	Boolean showTrajectory, playSound;

	public GameOptions(String difficulty, Boolean showTrajectory, Boolean playSound){
		this.difficulty = difficulty;
		this.showTrajectory = showTrajectory;
		this.playSound = playSound;
	}
}
