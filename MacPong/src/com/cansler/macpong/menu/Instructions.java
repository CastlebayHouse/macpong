package com.cansler.macpong.menu;

import com.cansler.macpong.gfx.Color;
import com.cansler.macpong.gfx.Font;
import com.cansler.macpong.gfx.Screen;

public class Instructions extends Menu {
	public Instructions() {

	}

	@Override
	public void tick() {
		if (input.esc.clicked) {
			game.setMenu(new TitleMenu());
		}
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.clear(0);

		Font.renderFrame(screen, 1, 3, 18, 10);
		// I used ~ as up arrow and @ as down arrow in Font.java
		Font.draw("W or ~ = Up", screen, 2 * 8, 4 * 8, Color.getColor(-1, 555, 555, 555));
		Font.draw("S or @ = down", screen, 2 * 8, 5 * 8, Color.getColor(-1, 555, 555, 555));
		Font.draw("ESC    = Pause", screen, 2 * 8, 6 * 8, Color.getColor(-1, 555, 555, 555));
		Font.draw("Score 5 to win", screen, 2 * 8, 7 * 8, Color.getColor(-1, 555, 555, 555));
		Font.draw("(press esc)", screen, 2 * 8, 9 * 8, Color.getColor(-1, 550, 550, 550));

	}
}
