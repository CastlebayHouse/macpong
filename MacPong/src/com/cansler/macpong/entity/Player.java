package com.cansler.macpong.entity;

import com.cansler.macpong.Game;
import com.cansler.macpong.InputHandler;
import com.cansler.macpong.gfx.Color;
import com.cansler.macpong.gfx.Screen;

public class Player {
	private final InputHandler input;

	public Game game;
	public double x = 0;
	public double y;
	public double vy;
	public int tickTime = 0;
	public int transform = 0;
	public Integer score = 0;
	public double top;
	public double bottom;
	// paddle is 8 by 16, but the back part is transparent
	public double back = 6;
	public double front = 8;

	public Player(Game game, InputHandler input) {
		this.game = game;
		this.input = input;
		x = 0;
		y = Game.HEIGHT / 2 - 8;
		vy = 0;
		top = y;
		bottom = y + 16;
	}

	public void tick() {
		tickTime++;
		if (input.up.down) {
			vy = -1;
		} else if (input.down.down) {
			vy = 1;
		} else {
			vy = 0;
		}
		update();
	}

	public void update() {
		double tempY = y + vy * Game.DT;

		// only update position if we are inside the walls (8pixel buffer)
		if (tempY >= 8 && tempY + 16 <= (Game.HEIGHT - 8)) {
			y += vy * Game.DT;
			top = y;
			bottom = y + 16;
		}

	}

	public void render(Screen screen) {
		int xt = 0;
		int yt = 20;
		int roundedY = (int) Math.round(y);
		int col = Color.getColor(-1, -1, 555, 555);
		screen.render(0, roundedY, xt + (yt * 32), col, transform);
		screen.render(0, roundedY + 8, xt + (yt * 32), col, transform);
	}

	public void reset() {
		x = 0;
		y = Game.HEIGHT / 2 - 8;
		vy = 0;
		top = y;
		bottom = y + 16;
		score = 0;
	}
}
