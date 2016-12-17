package com.slimesoccer.game;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewAnimator;

public class MainMenuActivity extends AndroidApplication implements OnClickListener {

	ViewAnimator viewAnimator;
	Animation slideLeft, slideRight;
	Button startButton, optionsButton, easyButton, normalButton, hardButton;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		initialize(listener);
		
		sharedPreferences = this.getSharedPreferences("com.slimesoccer.game", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		initializeLayout();
		initializeAnimations();
		initializeButtons();
		initializeOptions();
	}
	
	public void initializeOptions(){
		RadioGroup showTrajectoryGroup = (RadioGroup) findViewById(R.id.showTrajectoryGroup);
		RadioGroup playSoundGroup = (RadioGroup) findViewById(R.id.playSoundGroup);
		
		if(sharedPreferences.getBoolean("com.slimesoccer.game.showTrajectory", true)){
			showTrajectoryGroup.check(R.id.showTrajectoryYes);
		}
		else{
			showTrajectoryGroup.check(R.id.showTrajectoryNo);
		}
		
		if(sharedPreferences.getBoolean("com.slimesoccer.game.playSound", true)){
			playSoundGroup.check(R.id.playSoundYes);
		}
		else{
			playSoundGroup.check(R.id.playSoundNo);
		}
	}

	public void initializeAnimations() {
		viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
		slideLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
		slideRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
	}

	public void initializeLayout() {
		FrameLayout layout = new FrameLayout(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new Game(false, null), config);

		View view = getLayoutInflater().inflate(R.layout.mainmenulayout, null);

		layout.addView(gameView);
		layout.addView(view);
		setContentView(layout);
	}

	public void initializeButtons() {
		startButton = (Button) findViewById(R.id.startButton);
		optionsButton = (Button) findViewById(R.id.optionsButton);
		easyButton = (Button) findViewById(R.id.easyButton);
		normalButton = (Button) findViewById(R.id.normalButton);
		hardButton = (Button) findViewById(R.id.hardButton);

		startButton.setOnClickListener(this);
		optionsButton.setOnClickListener(this);
		easyButton.setOnClickListener(this);
		normalButton.setOnClickListener(this);
		hardButton.setOnClickListener(this);
	}

	public void launchGame(String difficulty) {
    	Intent intent = new Intent(MainMenuActivity.this, AndroidLauncher.class);
		intent.putExtra("difficulty", difficulty);
		intent.putExtra("showTrajectory", sharedPreferences.getBoolean("com.slimesoccer.game.showTrajectory", true));
		intent.putExtra("playSound", sharedPreferences.getBoolean("com.slimesoccer.game.playSound", true));
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v == startButton) {
			viewAnimator.showNext();
		} else if (v == optionsButton) {
			viewAnimator.setDisplayedChild(2);
		} else if (v == easyButton) {
			launchGame("easy");
		} else if (v == normalButton) {
			launchGame("normal");
		} else if (v == hardButton) {
			launchGame("hard");
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        viewAnimator.setDisplayedChild(0);
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	public void onTrajectoryButtonClicked(View v) {
	    boolean checked = ((RadioButton)v).isChecked();

	    switch(v.getId()) {
	        case R.id.showTrajectoryYes:
	            if (checked){
	            	editor.putBoolean("com.slimesoccer.game.showTrajectory", true);
	            }
	            break;
	        case R.id.showTrajectoryNo:
	            if (checked){
	            	editor.putBoolean("com.slimesoccer.game.showTrajectory", false);
	            }
	            break;
	    }
	    
	    editor.commit();
	}
	
	public void onSoundButtonClicked(View v) {
	    boolean checked = ((RadioButton)v).isChecked();

	    switch(v.getId()) {
	        case R.id.playSoundYes:
	            if (checked){
	            	editor.putBoolean("com.slimesoccer.game.playSound", true);
	            }
	            break;
	        case R.id.playSoundNo:
	            if (checked){
	            	editor.putBoolean("com.slimesoccer.game.playSound", false);
	            }
	            break;
	    }
	    
	    editor.commit();
	}
	
	public void onBackButtonClicked(View v) {
		viewAnimator.setDisplayedChild(0);
	}
}
