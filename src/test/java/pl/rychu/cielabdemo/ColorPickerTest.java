package pl.rychu.cielabdemo;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorPickerTest {

	private static final long COLOR_PICKER_SEED = 10L;
	private static final long COLOR_RANDOM_SEED = 2L;

	@Test
	public void testMatrixLab() {
		int colsBase = 10;
		int colsCmp = 10;
		int cw = 70;
		int ch = 40;
		int w = cw * (colsCmp + 1);
		int h = ch * (colsBase + 1);
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = out.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		try {
			ColorPickerLab colorPicker = new ColorPickerLab(COLOR_PICKER_SEED);
			List<ColorYUV> cols = new ArrayList<>();
			List<Lab> colLabs = new ArrayList<>();
			for (int i = 0; i < colsBase; i++) {
				Color newColor = colorPicker.getNewColor();
				g.setColor(newColor);
				g.fillRect(0, ch * i, cw, ch);
				cols.add(new ColorYUV(newColor));
				colLabs.add(Lab.fromColor(newColor));
			}
			Random rnd = new Random(COLOR_RANDOM_SEED);
			NumberFormat nf = new DecimalFormat("0.0000");
			Double maxDist = null;
			int maxx = -1;
			Double maxDistLab = null;
			int maxLabX = -1;
			for (int x = 0; x < colsCmp; x++) {
				Color color = new Color(rnd.nextInt(0x1000000));
				ColorYUV colorYUV = new ColorYUV(color);
				Lab colorLab = Lab.fromColor(color);
				g.setColor(color);
				g.fillRect(cw * (x + 1), ch * colsBase, cw, ch);
				g.setColor(Color.BLACK);
				Double minDist = null;
				int miny = -1;
				Double minDistLab = null;
				int minLabY = -1;
				for (int y = 0; y < colsBase; y++) {
					{
						double dist = cols.get(y).sqDistFrom(colorYUV);
						if (minDist == null || dist < minDist) {
							minDist = dist;
							miny = y;
						}
						String text = nf.format(dist);
						int textX = cw * (x + 1);
						int textY = ch * y;
						g.drawString(text, textX, textY + 10);
					}
					{
						double dist = Lab.ciede2000(colLabs.get(y), colorLab);
						if (minDistLab == null || dist < minDistLab) {
							minDistLab = dist;
							minLabY = y;
						}
						String text = nf.format(dist);
						int textX = cw * (x + 1);
						int textY = ch * y;
						g.drawString(text, textX, textY + 25);
					}
				}
				if (maxDist == null || minDist > maxDist) {
					maxDist = minDist;
					maxx = x;
				}
				{
					String text = nf.format(minDist);
					int textX = cw * (x + 1);
					int textY = ch * colsBase;
					g.drawString(text, textX, textY + 10);
					g.drawLine(cw * (x + 1), ch * miny + 11, cw * (x + 1) + cw, ch * miny + 11);
				}
				if (maxDistLab == null || minDistLab > maxDistLab) {
					maxDistLab = minDistLab;
					maxLabX = x;
				}
				{
					String text = nf.format(minDistLab);
					int textX = cw * (x + 1);
					int textY = ch * colsBase + 15;
					g.drawString(text, textX, textY + 10);
					g.drawLine(cw * (x + 1), ch * minLabY + 26, cw * (x + 1) + cw, ch * minLabY + 26);
				}
			}
			g.drawLine(cw * (maxx + 1), ch * colsBase + 11, cw * (maxx + 1) + cw, ch * colsBase + 11);
			g.drawLine(cw * (maxLabX + 1), ch * colsBase + 26, cw * (maxLabX + 1) + cw, ch * colsBase + 26);
		} finally {
			g.dispose();
		}
		try {
			ImageIO.write(out, "png", new File("out.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
