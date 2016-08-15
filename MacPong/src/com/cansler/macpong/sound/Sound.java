package com.cansler.macpong.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.Random;

public class Sound {
	public static final Sound pause = new Sound("/pause.wav");
	public static final Sound ping = new Sound("/ping.wav");
	public static final Sound pong = new Sound("/pong.wav");
	public static final Sound wall = new Sound("/wall.wav");
	public static final Sound score = new Sound("/score.wav");
	public static final Sound menuSelect = new Sound("/menuSelect.wav");
	private static final Random random = new Random();

	private AudioClip clip;

	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			new Thread() {
				@Override
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void playRandom() {
		switch (random.nextInt(2)) {
		case 0:
			Sound.ping.play();
			break;
		case 1:
			Sound.pong.play();
			break;
		}
	}
}