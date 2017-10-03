package pl.rychu.cielabdemo;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ColorPicker {
	private final List<ColorYUV> colors = new ArrayList<>();

	private static final Random random = new Random();

	public Color getNewColor() {
		Collection<Color> rndcols = getRandomColors(5 + colors.size());
		Color farthest = getFarthest(colors, rndcols);
		ColorYUV fyuv = new ColorYUV(farthest);
		colors.add(fyuv);
		return farthest;
	}

	private static Collection<Color> getRandomColors(int n) {
		List<Color> result = new ArrayList<>(n);

		for (int i = 0; i < n; i++) {
			result.add(new Color(random.nextInt(0x1000000)));
		}

		return result;
	}

	private static Color getFarthest(Collection<ColorYUV> current, Collection<Color> colors) {
		Iterator<Color> iter = colors.iterator();
		Color best = iter.next();
		double bestDist = dist(current, new ColorYUV(best));
		while (iter.hasNext()) {
			Color next = iter.next();
			ColorYUV nextYUV = new ColorYUV(next);
			double nextDist = dist(current, nextYUV);
			if (nextDist > bestDist) {
				best = next;
				bestDist = nextDist;
			}
		}
		return best;
	}

	public double dist(ColorYUV col) {
		return dist(colors, col);
	}

	private static double dist(Collection<ColorYUV> base, ColorYUV col) {
		return base.stream().map(colYuv -> colYuv.sqDistFrom(col)).reduce(Math::min).orElse(100.0);
	}

}
