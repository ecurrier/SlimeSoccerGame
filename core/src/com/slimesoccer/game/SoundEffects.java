package com.slimesoccer.game;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundEffects {

	Boolean soundEnabled;
	
	Sound bounce1 = Gdx.audio.newSound(Gdx.files.internal("Sounds/ball-bounce1.wav"));
	Sound bounce2 = Gdx.audio.newSound(Gdx.files.internal("Sounds/ball-bounce2.wav"));
	Sound slime1 = Gdx.audio.newSound(Gdx.files.internal("Sounds/slime-contact1.wav"));
	Sound slime2 = Gdx.audio.newSound(Gdx.files.internal("Sounds/slime-contact2.wav"));
	Music crowd1 = Gdx.audio.newMusic(Gdx.files.internal("Sounds/crowd1.wav"));
	
	Sound[] bounceSounds = {bounce1, bounce2};
	Sound[] slimeSounds = {slime1, slime2};
	Music[] crowdSounds = {crowd1};
	
	Music currentCrowdSound;
	long currentCrowdId;
	
	public SoundEffects(Boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
	}
	
	public void BallBounce() {
		if(!soundEnabled) {
			return;
		}
		
		int index = ThreadLocalRandom.current().nextInt(0, bounceSounds.length);
		bounceSounds[index].play(1.0f);
	}
	
	public void SlimeContact() {
		if(!soundEnabled) {
			return;
		}
		
		int index = ThreadLocalRandom.current().nextInt(0, slimeSounds.length);
		slimeSounds[index].play(1.0f);
	}
	
	public void CrowdStart() {
		if(!soundEnabled) {
			return;
		}
		
		int index = ThreadLocalRandom.current().nextInt(0, crowdSounds.length);
		currentCrowdSound = crowdSounds[index];
		
		currentCrowdSound.play();
		currentCrowdSound.setLooping(true);
	}
	
	public void CrowdStop() {
		if(!soundEnabled) {
			return;
		}
		
		currentCrowdSound.stop();
	}
}
