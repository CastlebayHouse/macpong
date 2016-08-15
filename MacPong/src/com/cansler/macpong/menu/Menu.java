package com.cansler.macpong.menu;

import com.cansler.macpong.Game;
import com.cansler.macpong.InputHandler;
import com.cansler.macpong.gfx.Screen;

public class Menu {
	protected Game game;
	protected InputHandler input;

	public void init(Game game, InputHandler input) {
		this.game = game;
		this.input = input;
	}

	public void tick() {

	}

	public void render(Screen screen) {

	}
}
