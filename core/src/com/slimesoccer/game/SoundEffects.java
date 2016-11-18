package com.slimesoccer.game;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundEffects {

	Sound bounce1 = Gdx.audio.newSound(Gdx.files.internal("Sounds/ball-bounce1.wav"));
	Sound bounce2 = Gdx.audio.newSound(Gdx.files.internal("Sounds/ball-bounce2.wav"));
	Sound slime1 = Gdx.audio.newSound(Gdx.files.internal("Sounds/slime-contact1.wav"));
	Sound slime2 = Gdx.audio.newSound(Gdx.files.internal("Sounds/slime-contact2.wav"));
	Sound crowd1 = Gdx.audio.newSound(Gdx.files.internal("Sounds/crowd1.mp3"));
	
	Sound[] bounceSounds = {bounce1, bounce2};
	Sound[] slimeSounds = {slime1, slime2};
	Sound[] crowdSounds = {crowd1};
	
	Sound currentCrowdSound;
	long currentCrowdId;
	
	public SoundEffects() {
		
	}
	
	public void BallBounce() {
		int index = ThreadLocalRandom.current().nextInt(0, bounceSounds.length);
		bounceSounds[index].play(1.0f);
	}
	
	public void SlimeContact() {
		int index = ThreadLocalRandom.current().nextInt(0, slimeSounds.length);
		slimeSounds[index].play(1.0f);
	}
	
	public void CrowdStart() {
		int index = ThreadLocalRandom.current().nextInt(0, crowdSounds.length);
		currentCrowdSound = crowdSounds[index];
		
		currentCrowdId = currentCrowdSound.play(1.0f);
		currentCrowdSound.setLooping(currentCrowdId, true);
	}
	
	public void CrowdStop() {
		currentCrowdSound.stop(currentCrowdId);
	}
}
