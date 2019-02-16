package type.client.work;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Utils {
	public static void setRenderingHints(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	}
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
		}
	}
	public static Dimension getSize(Dimension preferred) {
		return new Dimension(preferred.width + 10, preferred.height);
	}
	private Utils() {
	}
}
