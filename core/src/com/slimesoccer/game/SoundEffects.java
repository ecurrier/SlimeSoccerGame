package com.slimesoccer.game;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundEffects {

	
	Sound[] bounceSounds = {bounce1, bounce2};
	Sound[] slimeSounds = {slime1, slime2};
	Sound[] crowdSounds = {crowd1};
	
	Sound currentCrowdSound;
	long currentCrowdId;
	
	public SoundEffects() {
		
	}
	
	public void BallBounce() {
		bounceSounds[index].play(1.0f);
	}
	
	public void SlimeContact() {
		slimeSounds[index].play(1.0f);
	}
	
	public void CrowdStart() {
		currentCrowdSound = crowdSounds[index];
		
		currentCrowdId = currentCrowdSound.play(1.0f);
		currentCrowdSound.setLooping(currentCrowdId, true);
	}
	
	public void CrowdStop() {
		currentCrowdSound.stop(currentCrowdId);
	}
}
