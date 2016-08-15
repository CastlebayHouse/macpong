package com.cansler.macpong.entity;

import com.cansler.macpong.Game;
import com.cansler.macpong.gfx.Color;
import com.cansler.macpong.gfx.Screen;

public class Cpu {

	public Game game;
	public double x;
	public double y;
	public double vy;
	public int tickTime = 0;
	public int transform = 1;
	public Integer score = 0;
	public double top;
	public double bottom;
	public double front;
	public double back;
	public boolean isSet = true;

	public Cpu(Game game) {
		this.game = game;
		x = Game.WIDTH - 8;
		y = Game.HEIGHT / 2 - 8;
		top = y;
		bottom = y + 16;
		isSet = true;

		// Paddle only moves up and down. Once these are set here, they will not
		// change
		front = x;
		back = front + 2;
	}

	public void tick() {
		tickTime++;
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
		int col = Color.getColor(-1, 0, 555, 555);
		int roundedX = (int) Math.round(x);
		int roundedY = (int) Math.round(y);
		screen.render(roundedX, roundedY, xt + (yt * 32), col, transform);
		screen.render(roundedX, roundedY + 8, xt + (yt * 32), col, transform);
	}

	public void reset() {
		x = Game.WIDTH - 8;
		y = Game.HEIGHT / 2 - 8;
		top = y;
		bottom = y + 16;
		vy = 0;
		score = 0;
		isSet = true;
	}
}
