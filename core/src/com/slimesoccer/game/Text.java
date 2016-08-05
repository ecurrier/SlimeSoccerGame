package com.slimesoccer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Text {
	BitmapFont font;
	String message;
	float x, y;
	
	public Text(String message){
		this.message = message;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/OpenSans-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();

		parameter.size = 60;
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 4;

		font = generator.generateFont(parameter);
		generator.dispose();

		GlyphLayout layout = new GlyphLayout();
		layout.setText(font, message);
		float fontWidth = layout.width;
		float fontHeight = layout.height;

		x = -fontWidth / 2;
		y = fontHeight / 2;
	}

}
