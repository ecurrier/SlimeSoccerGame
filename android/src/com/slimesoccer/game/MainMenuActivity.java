package com.slimesoccer.game;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ViewAnimator;

public class MainMenuActivity extends AndroidApplication {

	ViewAnimator viewAnimator;
	Animation slideLeft, slideRight;
	Button startButton, optionsButton;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		initialize(listener);

		initializeAnimations();
		initializeLayout();
		initializeButtons();
	}

	public void initializeAnimations() {
		viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
		slideLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
		slideRight = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
	}

	public void initializeLayout() {
		FrameLayout layout = new FrameLayout(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new Game(false), config);

		View view = getLayoutInflater().inflate(R.layout.mainmenulayout, null);

		layout.addView(gameView);
		layout.addView(view);
		setContentView(layout);
	}

	public void initializeButtons() {
		startButton = (Button) findViewById(R.id.startButton);
		optionsButton = (Button) findViewById(R.id.optionsButton);

		startButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenuActivity.this, AndroidLauncher.class);
				startActivity(intent);
			}
		});
	}

}
