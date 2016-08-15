package com.cansler.macpong.gfx;

public class Color {

	// takes FOUR rgb colors of 000-555, gets their indexes, and mashes the four
	// indexes into a single int
	// On spritesheet, a = trans, b = dk. gray, c = lt. gray, d = white
	public static int getColor(int a, int b, int c, int d) {
		return (getIndex(d) << 24) + (getIndex(c) << 16) + (getIndex(b) << 8)
				+ (getIndex(a));
	}

	// takes an rgb color (000, 050, 555, etc) and returns an index into the
	// color palette. Note: 000 is 0, 050 is 50.
	public static int getIndex(int d) {
		if (d < 0)
			return 255; // returns last index in palette; used as transparency
		int r = d / 100 % 10;
		int g = d / 10 % 10;
		int b = d % 10;
		return r * 36 + g * 6 + b;
	}

}