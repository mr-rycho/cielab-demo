package pl.rychu.cielabdemo;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ColorPickerLab {
	private final List<Lab> colors = new ArrayList<>();

	private static final Random random = new Random();

	public Color getNewColor() {
		Collection<Color> rndcols = getRandomColors(5 + 2 * colors.size());
		Color farthest = getFarthest(colors, rndcols);
		Lab farthestLab = Lab.fromColor(farthest);
		colors.add(farthestLab);
		return farthest;
	}

	private static Collection<Color> getRandomColors(int n) {
		List<Color> result = new ArrayList<>(n);

		for (int i = 0; i < n; i++) {
			result.add(new Color(random.nextInt(0x1000000)));
		}

		return result;
	}

	private static Color getFarthest(Collection<Lab> current, Collection<Color> colors) {
		Iterator<Color> iter = colors.iterator();
		Color best = iter.next();
		double bestDist = dist(current, Lab.fromColor(best));
		while (iter.hasNext()) {
			Color next = iter.next();
			Lab nextLab = Lab.fromColor(next);
			double nextDist = dist(current, nextLab);
			if (nextDist > bestDist) {
				best = next;
				bestDist = nextDist;
			}
		}
		return best;
	}

	public double dist(Lab col) {
		return dist(colors, col);
	}

	private static double dist(Collection<Lab> base, Lab col) {
		return base.stream().map(lab -> Lab.ciede2000(lab, col)).reduce(Math::min).orElse(1000.0);
	}

}
