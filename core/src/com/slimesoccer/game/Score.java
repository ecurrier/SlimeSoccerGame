package com.slimesoccer.game;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Score {
	
	public int score;
	public String name;
	public String identifier;
	public BitmapFont font;
	public float x;
	public float y;
	
	public Score(String identifier){
		score = 0;
		this.identifier = identifier;
		
		if(identifier == "player"){
			x = (-640 + 50) / 2;
			y = (480 - 50) / 2;
		}
		else if(identifier == "computer"){
			x = (640 - (50 + 365)) / 2;
			y = (480 - 50) / 2;
		}
		
		setName(score);
		font = new BitmapFont();
	}
	
	public void draw(SpriteBatch batch){
		font.getData().setScale(1.25f);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.setColor(0, 0, 0, 1.0f);
		font.draw(batch, name, x, y);
	}
	
	public void incrementScore(){
		score++;
		setName(score);
	}
	
	public void setName(int score){
		if(identifier == "player"){
			name = "Player Score   -   " + score;
		}
		else if(identifier == "computer"){
			name = score + "   -   Computer Score";
		}
	}

}
