package type.client.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import type.common.work.Utils;

public class GlowComponent extends JComponent {
	private static final long serialVersionUID = 8920816865645949594L;

	public GlowComponent() {
		setBackground(new Color(255, 255, 255, 150));
	}
	private long time = 0;
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Utils.setRenderingHints(g2);
		
		long n = System.currentTimeMillis() - time;
		if(n < 1000) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)n / 1000));
			g2.setColor(getBackground());
		}
	}
}
