package com.slimesoccer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Score {
	
	public FreeTypeFontGenerator generator;
	public FreeTypeFontParameter parameter;
	
	public int score;
	public String name;
	public String identifier;
	public BitmapFont font;
	
	public float x;
	public float y;
	public float fontWidth;
	public float widthOffset = 20f;
	
	public Score(String identifier){
		score = 0;
		name = score + "";
		this.identifier = identifier;
		
		setFont();
		setName(score);
	}
	
	public void draw(SpriteBatch batch){
		font.draw(batch, name, x, y);
	}
	
	public void incrementScore(){
		score++;
		setName(score);
	}
	
	public void setFont(){
		generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/OpenSans-Regular.ttf"));
		parameter = new FreeTypeFontParameter();
		
		parameter.size = 48;
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 2;
		
		font = generator.generateFont(parameter);
		generator.dispose();
		
		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, name);
		fontWidth = layout.width;
	}
	
	public void setName(int score){
		name = score + "";
		
		if(identifier == "player"){
			x = (0 - fontWidth) - widthOffset;
			y = (480 - 50) / 2;
		}
		else if(identifier == "computer"){
			x = 0 + widthOffset;
			y = (480 - 50) / 2;
		}
	}

}
