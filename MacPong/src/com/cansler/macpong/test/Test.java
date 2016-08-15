package com.cansler.macpong.test;

import com.cansler.macpong.gfx.Color;

public class Test {
	public static void main(String[] args) {
		new Test().run();
	}

	private void run() {
		ColorTest();
	}

	private void ColorTest() {

		int blue = Color.getIndex(666);
		System.out.println("index of notch-rgb 555 = " + blue);
		/*
		 * Game g = new Game(); g.init();
		 * System.out.printf("palette[blue] = 0x%h\n", g.palette[blue]);
		 * 
		 * int bbbb = Color.getColorInt(555, 555, 555, 555);
		 * 
		 * System.out.println("555,555,555,555 = " + bbbb);
		 */

	}
}
