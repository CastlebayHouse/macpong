package com.cansler.macpong.menu;

import com.cansler.macpong.gfx.Color;
import com.cansler.macpong.gfx.Font;
import com.cansler.macpong.gfx.Screen;

public class Pause extends Menu {
	public Pause() {

	}

	@Override
	public void tick() {
		if (input.esc.clicked) {
			game.setMenu(null);
			game.isPaused = false;
		}
		if (input.q.clicked) {
			game.resetGame();
			game.setMenu(new TitleMenu());
		}
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);
		Font.renderFrame(screen, 1, 3, 18, 9);
		Font.draw("Paused", screen, 2 * 8, 4 * 8, Color.getColor(-1, 550, 550, 550));
		Font.draw("ESC = resume", screen, 2 * 8, 6 * 8, Color.getColor(-1, 555, 555, 555));
		Font.draw("Q = quit", screen, 2 * 8, 7 * 8, Color.getColor(-1, 555, 555, 555));
	}
}
