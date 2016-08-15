package com.cansler.macpong.menu;

import com.cansler.macpong.Game;
import com.cansler.macpong.gfx.Color;
import com.cansler.macpong.gfx.Font;
import com.cansler.macpong.gfx.Screen;
import com.cansler.macpong.sound.Sound;

public class WonMenu extends Menu {
	private int inputDelay = 60;
	private int selected = 0;
	private static final String[] options = { "Yes", "No" };

	public WonMenu() {
	}

	@Override
	public void tick() {
		if (inputDelay > 0) {
			inputDelay--;
			return;
		}

		if (input.up.clicked) {
			Sound.menuSelect.play();
			selected--;
		}
		if (input.down.clicked) {
			Sound.menuSelect.play();
			selected++;
		}

		int len = options.length;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		if (input.enter.clicked) {
			if (selected == 0) {
				game.resetGame();
				game.setMenu(null);
			}
			if (selected == 1) {
				game.resetGame();
				game.setMenu(new TitleMenu());
			}
		}
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);
		Font.renderFrame(screen, 1, 3, 18, 9);
		if (game.player.score == Game.WINNING_SCORE) {
			Font.draw("You won!", screen, 2 * 8, 4 * 8, Color.getColor(-1, 550, 550, 550));
		} else {
			Font.draw("You lost.", screen, 2 * 8, 4 * 8, Color.getColor(-1, 550, 550, 550));
		}

		for (int i = 0; i < 2; i++) {
			String msg = options[i];
			int col = Color.getColor(-1, 222, 222, 222);
			if (i == selected) {
				msg = "> " + msg + " <";
				col = Color.getColor(-1, 555, 555, 555);
			}
			Font.draw("Play again?", screen, 2 * 8, 5 * 8, Color.getColor(-1, 555, 555, 555));
			Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (7 + i) * 8, col);

			// Font.draw("Press Y or N.", screen, 2 * 8, 7 * 8,
			// Color.getColor(-1, 333, 333, 333));

		}

	}
}
