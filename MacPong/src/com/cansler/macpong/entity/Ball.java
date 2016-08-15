package com.cansler.macpong.entity;

import java.util.Random;

import com.cansler.macpong.Game;
import com.cansler.macpong.gfx.Color;
import com.cansler.macpong.gfx.Screen;
import com.cansler.macpong.sound.Sound;

public class Ball {
	public Game game;
	public double ballX;
	public double ballY;
	public double vx; // velocity in m/s along x
	public double vy; // velocity in m/s along y
	public int tickTime = 0;
	public int transform = 0;

	private int timeout = 0;
	private final double[] velocityX = { 1.2, -1.2 };
	private final double[] velocityY = { -0.5, 0.5 };
	private final int[] startHeight = { Game.HEIGHT / 2 - 2, (Game.HEIGHT / 4) + 10,
			(Game.HEIGHT * 3 / 4) - 10 };

	public Ball(Game game) {
		this.game = game;
		ballX = Game.WIDTH / 2 - 2;
		// Random r = new Random();
		// ballY = startHeight[r.nextInt(3)];
		ballY = Game.HEIGHT / 2 - 2;
		vx = 0.0;
		vy = 0.0;
		timeout = 50;
	}

	public void tick() {
		tickTime++;
		if (tickTime > timeout && vx == 0 && vy == 0) {
			Random r = new Random();
			vx = velocityX[r.nextInt(velocityX.length)];
			vy = velocityY[r.nextInt(velocityY.length)];
		}
		update();
	}

	public boolean update() {

		// move the ball
		double tempBallX = ballX + vx * Game.DT;
		double tempBallY = ballY + vy * Game.DT;

		// ball coordinates a, b, c, d, going clockwise from upper left corner
		double ax = tempBallX;
		double ay = tempBallY;
		double bx = tempBallX + 4;
		double by = tempBallY;
		double cx = tempBallX + 4;
		double cy = tempBallY + 4;
		double dx = tempBallX;
		double dy = tempBallY + 4;

		// reset ball if it's past left wall
		if (tempBallX <= -4) {
			Sound.score.play();
			game.cpu.score++;
			reset();
			return false;
		}

		// reset ball if it's past right wall
		if (tempBallX >= Game.WIDTH) {
			Sound.score.play();
			reset();
			game.player.score++;
			return false;
		}

		if (game.cpu.score == Game.WINNING_SCORE || game.player.score == Game.WINNING_SCORE) {
			game.won();
		}

		// COLLISION DETECTION

		// if ball hits upper or lower wall
		if (tempBallY <= 0 || tempBallY >= Game.HEIGHT - 4) {
			vy *= -1;
			Sound.wall.play();
		}

		// if ball hits player paddle
		// check if leading edge of ball breaks front edge of paddle but is
		// inside the top and bottom edges of paddle
		if (ax < game.player.front && bx > game.player.back && dy > game.player.top
				&& ay < game.player.bottom) {
			if (ax < game.player.front && dx < game.player.front && ax > game.player.back) {
				vx *= -1;
				Sound.playRandom();
				// put some "spin" on it
				if (game.player.vy > 0 && this.vy > 0) {
					System.out.println("speed it up!");
					vy += 0.1;
				} else if (game.player.vy < 0 && this.vy < 0) {
					System.out.println("speed it up!");
					vy -= 0.1;
				} else if (game.player.vy > 0 && this.vy < 0) {
					System.out.println("slow it down...");
					vy += 0.1;
				} else if (game.player.vy < 0 && this.vy > 0) {
					System.out.println("slow it down...");
					vy -= 0.1;
				}
			} else if (dy > game.player.top && cy > game.player.top && dy < game.player.bottom) {
				vy *= -1;
				vy = Math.round(vy);
				Sound.playRandom();
			} else if (ay < game.player.bottom && by < game.player.bottom && ay > game.player.top) {
				vy *= -1;
				vy = Math.round(vy);
				Sound.playRandom();
			}
		}

		// if ball hits cpu paddle
		// check if leading edge of ball breaks front edge of paddle but is
		// inside the top and bottom edges of paddle
		if (bx > game.cpu.front && ax < game.cpu.back && dy > game.cpu.top && ay < game.cpu.bottom) {
			// check for frontal collision
			if (bx > game.cpu.front && cx > game.cpu.front && bx < game.cpu.back) {
				vx *= -1;
				Sound.playRandom();
				// put some "spin" on it
				if (game.cpu.vy > 0 && this.vy > 0) {
					System.out.println("speed it up!");
					vy += 0.1;
				} else if (game.cpu.vy < 0 && this.vy < 0) {
					System.out.println("speed it up!");
					vy -= 0.1;
				} else if (game.cpu.vy > 0 && this.vy < 0) {
					System.out.println("slow it down...");
					vy += 0.1;
				} else if (game.cpu.vy < 0 && this.vy > 0) {
					System.out.println("slow it down...");
					vy -= 0.1;
				}
			}
			// check top of paddle
			else if (dy > game.cpu.top && cy > game.cpu.top && dy < game.cpu.bottom) {
				vy *= -1;
				vy = Math.round(vy);
				Sound.playRandom();
				System.out.println("cpu top collision");
			}
			// check bottom of paddle
			else if (ay < game.cpu.bottom && by < game.cpu.bottom && ay > game.cpu.top) {
				vy *= -1;
				vy = Math.round(vy);
				Sound.playRandom();
				System.out.println("cpu bottom collision");
			}

		}

		// move the cpu paddle if the ball is headed towards it
		if (vx > 0 && tempBallX >= 100) {
			game.cpu.isSet = false;
			if ((game.cpu.y + 8) > (tempBallY + 2)) {
				game.cpu.vy = -0.7;
			} else if ((game.cpu.y + 8) < (tempBallY + 2)) {
				game.cpu.vy = 0.7;
			} else game.cpu.vy = 0;
		}
		// move cpu paddle to a neutral positon and park it
		else if (game.cpu.isSet == false) {
			if (vx < 0 && (int) Math.round(game.cpu.y) + 8 > (Game.HEIGHT / 2)) {
				game.cpu.vy = -1;
			} else if (vx < 0 && (int) Math.round(game.cpu.y) + 8 < (Game.HEIGHT / 2)) {
				game.cpu.vy = 1;
			} else {
				game.cpu.vy = 0;
				game.cpu.isSet = true;
			}
		}

		// now that we've calculated vx and vy, update the ball position
		ballX += vx * Game.DT;
		ballY += vy * Game.DT;

		return true;
	}

	public void render(Screen screen) {
		// tile x on spritesheet
		int xt = 0;
		// tile y on spritesheet
		int yt = 21;
		int col = Color.getColor(-1, 0, 0, 555);

		screen.render((int) Math.round(ballX), (int) Math.round(ballY), xt + (yt * 32), col,
				transform);
	}

	// resets the ball, like after a score
	public void reset() {
		ballX = Game.WIDTH / 2 - 2;
		// Random r = new Random();
		// ballY = startHeight[r.nextInt(3)];
		ballY = Game.HEIGHT / 2 - 2;
		vx = 0;
		vy = 0;
		timeout = tickTime + 20;
	}
}
