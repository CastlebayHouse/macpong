package com.cansler.macpong;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.cansler.macpong.entity.Ball;
import com.cansler.macpong.entity.Cpu;
import com.cansler.macpong.entity.Player;
import com.cansler.macpong.gfx.Color;
import com.cansler.macpong.gfx.Font;
import com.cansler.macpong.gfx.Screen;
import com.cansler.macpong.gfx.SpriteSheet;
import com.cansler.macpong.menu.Menu;
import com.cansler.macpong.menu.Pause;
import com.cansler.macpong.menu.TitleMenu;
import com.cansler.macpong.menu.WonMenu;
import com.cansler.macpong.sound.Sound;

public class Game extends Canvas implements Runnable {

	public static int WIDTH = 160;
	public static int HEIGHT = 120;
	public static int SCALE = 4;
	public static int DT = WIDTH / 120; // WIDTH/2 * 1/60
	public static String NAME = "Mac Pong";
	public static int WINNING_SCORE = 5;
	private static final long serialVersionUID = 1L;

	private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private boolean running = false;
	private Screen screen;
	private final InputHandler input = new InputHandler(this);

	private int tickCount = 0;
	public int[] palette = new int[216];
	public Player player;
	public Cpu cpu;
	public Ball ball;
	private int wonTimer = 0;
	private boolean hasWon = false;
	public boolean isPaused = false;
	public Menu menu;

	public static void main(String[] args) {
		Game game = new Game();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();

	}

	@Override
	public void run() {
		boolean shouldRender = false;
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTime = System.nanoTime();
		long lastMilliTime = System.currentTimeMillis();

		init();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;

			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			// try {
			// Thread.sleep(2);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }

			if (shouldRender) {
				frames++;
				render();
				shouldRender = false;
			}

			if (System.currentTimeMillis() - lastMilliTime > 1000) {
				lastMilliTime += 1000;
				// System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}

	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
		System.exit(0);
	}

	public void init() {
		int pp = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

					int r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10;
					int g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10;
					int b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10;
					palette[pp++] = rr << 16 | gg << 8 | bb;
					// System.out.println("palette[" + (pp - 1) + "]: "
					// + palette[pp - 1]);

				}
			}
		}

		try {
			BufferedImage bf = ImageIO.read(Game.class.getResourceAsStream("/icons.png"));
			screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(bf));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ball = new Ball(this);
		player = new Player(this, input);
		cpu = new Cpu(this);
		resetGame();
		setMenu(new TitleMenu());
	}

	private void render() {

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear(0);
		drawBackground();
		player.render(screen);
		cpu.render(screen);
		ball.render(screen);
		renderGui();

		for (int y = 0; y < screen.h; y++) {
			for (int x = 0; x < screen.w; x++) {
				int index = screen.pixels[x + y * screen.w];
				if (index < 255) {
					pixels[x + y * WIDTH] = palette[index];
				}
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	private void drawColoredRectangles(Graphics gfx, int colorInt) {
		int off = 1;
		for (int i = 0; i < 4; i++) {
			// get the ith color
			int index = (colorInt >> (i * 8)) & 255;
			if (index < 255) {
				int col = palette[index];
				int b = col & 255;
				int g = (col >> 8) & 255;
				int r = (col >> 16) & 255;

				// System.out.printf("r:%d g:%d b:%d\n", r, g, b);
				gfx.setColor(new java.awt.Color(r * off, g * off, b * off));
				gfx.fillRect((WIDTH * i / 2) + 100, (HEIGHT / 2) + 100, 50, 50);
			}
		}
	}

	private void drawPalette(Graphics g) {
		int blockWidth = 15;
		int numBlocksPerRow = 36;
		for (int i = 0; i < palette.length; i++) {
			int col = palette[i];
			int b1 = col & 255;
			int g1 = (col >> 8) & 255;
			int r1 = (col >> 16) & 255;
			// System.out.printf("r:%d g:%d b:%d\n", r1, g1, b1);
			g.setColor(new java.awt.Color(r1, g1, b1));
			int x = ((i % numBlocksPerRow) * blockWidth);
			int y = ((i / numBlocksPerRow * blockWidth));
			g.fillRect(x, y, blockWidth, blockWidth);
		}
	}

	private void drawBackground() {
		int col = Color.getColor(-1, 0, 0, 111);
		for (int y = 0; y < HEIGHT; y += 16) {
			screen.render(WIDTH / 2 - 1, y, 0 + 22 * 32, col, 0);
		}
	}

	public void tick() {
		tickCount++;
		input.tick();

		if (menu != null) {
			menu.tick();
		} else {
			if (input.esc.clicked == true) {
				setMenu(new Pause());
				isPaused = true;
				Sound.pause.play();
			}

			if (wonTimer > 0) {
				if (--wonTimer == 0) {
					setMenu(new WonMenu());
				}
			} else {
				player.tick();
				cpu.tick();
				ball.tick();
			}
		}
	}

	public void resetGame() {

		player.reset();
		cpu.reset();
		ball.reset();

		wonTimer = 0;
		hasWon = false;
		isPaused = false;
	}

	public void won() {
		wonTimer = 60 * 1;
		hasWon = true;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
		if (menu != null) {
			menu.init(this, input);
		}
	}

	private void renderGui() {

		if (player.score < 10) {
			Font.draw(player.score.toString(), screen, WIDTH / 2 - 24, 8,
					Color.getColor(-1, 0, 0, 555));
		} else {
			Font.draw(player.score.toString(), screen, WIDTH / 2 - 32, 8,
					Color.getColor(-1, 0, 0, 555));
		}
		Font.draw(cpu.score.toString(), screen, WIDTH / 2 + 16, 8, Color.getColor(-1, 0, 0, 555));

		if (menu != null) {
			menu.render(screen);
		}
	}
}
