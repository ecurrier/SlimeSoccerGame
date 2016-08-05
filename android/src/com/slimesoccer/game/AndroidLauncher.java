package com.slimesoccer.game;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.slimesoccer.game.Game;

public class AndroidLauncher extends AndroidApplication implements Game.MyGameCallBack{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		Bundle extras = getIntent().getExtras();
		String difficulty = extras.getString("difficulty");

		Game game = new Game(true);
		game.setMyGameCallBack(this);
		
		initialize(game, config);
	}
	
	public void startActivity(){
		Intent intent = new Intent(AndroidLauncher.this, MainMenuActivity.class);
		startActivity(intent);
	}
}
