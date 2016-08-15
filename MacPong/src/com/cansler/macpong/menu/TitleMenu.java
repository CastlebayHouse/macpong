package com.cansler.macpong.menu;

import com.cansler.macpong.gfx.Color;
import com.cansler.macpong.gfx.Font;
import com.cansler.macpong.gfx.Screen;
import com.cansler.macpong.sound.Sound;

public class TitleMenu extends Menu {
	private int selected = 0;
	private static final String[] options = { "Start", "Instructions", "Exit" };

	public TitleMenu() {
	}

	@Override
	public void tick() {
		if (input.up.clicked) {
			Sound.menuSelect.play();
			selected--;
		}
		if (input.down.clicked) {
			Sound.menuSelect.play();
			selected++;
		}

		// loop the menu selecting
		int len = options.length;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		if (input.enter.clicked) {
			if (selected == 0) {
				// Sound.test.play();
				game.resetGame();
				game.setMenu(null);
			}
			if (selected == 1) {
				game.setMenu(new Instructions());
			}
			if (selected == 2) game.stop();
		}

	}

	@Override
	public void render(Screen screen) {
		screen.clear(0);

		int h = 2;
		int w = 9;
		int titleColor = Color.getColor(0, 5, 0, 355);

		int xo = (screen.w - w * 8) / 2;
		int yo = 24;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				screen.render(xo + x * 8, yo + y * 8, x + (y + 23) * 32, titleColor, 0);
			}
		}

		for (int i = 0; i < 3; i++) {
			String msg = options[i];
			int col = Color.getColor(0, 222, 222, 222);
			if (i == selected) {
				msg = "> " + msg + " <";
				col = Color.getColor(0, 555, 555, 555);
			}
			Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (8 + i) * 8, col);
		}

		// Font.draw("(Arrow keys,X and C)", screen, 0, screen.h - 8,
		// Color.getColor(0, 111, 111, 111));

	}
}
