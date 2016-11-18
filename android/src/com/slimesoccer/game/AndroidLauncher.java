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
		GameOptions options = new GameOptions(extras.getString("difficulty"), extras.getBoolean("showTrajectory"));

		Game game = new Game(true, options);
		game.setMyGameCallBack(this);
		
		initialize(game, config);
	}
	
	public void startActivity(){
		Intent intent = new Intent(AndroidLauncher.this, MainMenuActivity.class);
		startActivity(intent);
	}
}
