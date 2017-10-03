package pl.rychu.cielabdemo;

import java.awt.*;

public class ColorYUV implements Comparable<ColorYUV> {
	private double y;
	private double u;
	private double v;
	private static final double SCALE = 1.0 / 255.0;
	private static final double UNSCALE = 255.0;

	public ColorYUV(double y, double u, double v) {
		this.y = y;
		this.u = u;
		this.v = v;
	}

	public ColorYUV(int r, int g, int b) {
		y = (0.299 * r + 0.587 * g + 0.114 * b) * SCALE;
		u = (-0.14713 * r - 0.28886 * g + 0.436 * b) * SCALE;
		v = (0.615 * r - 0.51499 * g - 0.10001 * b) * SCALE;
	}

	public ColorYUV(Color color) {
		this(color.getRed(), color.getGreen(), color.getBlue());
	}

	public ColorYUV(int color) {
		this((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff);
	}

	public int toRGB() {
		int r = (int) Math.round((1.0 * y + 1.13983 * v) * UNSCALE);
		int g = (int) Math.round((1.0 * y - 0.39465 * u - 0.5806 * v) * UNSCALE);
		int b = (int) Math.round((1.0 * y + 2.03211 * u) * UNSCALE);
		return (((r & 0xff) << 8) | (g & 0xff)) << 8 | (b & 0xff);
	}

	public double getY() {
		return y;
	}


	public double sqDistFrom(ColorYUV c) {
		double yd = y - c.y;
		double ud = u - c.u;
		double vd = v - c.v;
		return yd * yd + ud * ud + vd * vd;
	}

	public int compareTo(ColorYUV o) {
		if (y < o.y) return -1;
		if (y > o.y) return 1;
		if (u < o.u) return -1;
		if (u > o.u) return 1;
		if (v < o.v) return -1;
		if (v > o.v) return 1;
		return 0;
	}

	public boolean equals(Object o) {
		if (!(o instanceof ColorYUV)) return false;
		ColorYUV c = (ColorYUV) o;
		return (y == c.y && u == c.u && v == c.v);
	}
}
