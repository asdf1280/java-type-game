package type.client.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JComponent;

import type.common.work.Utils;

public class ExplosionEffect extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int alpha = 255;

	public ExplosionEffect(Dimension size) {
		setSize(size);
		setPreferredSize(size);

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (alpha > 0) {
					alpha -= 5;
					for (int i = 0; i < 5; i++) {
						ps.add(new Point((int) (Math.random() * getWidth()), (int) (Math.random() * getHeight())));
					}
					Utils.sleep(10);
				}
				if (getParent() != null)
					getParent().remove(ExplosionEffect.this);
			}
		}).start();
	}

	Vector<Point> ps = new Vector<>();

	@Override
	protected void paintComponent(Graphics g) {
		if (alpha < 0)
			return;
		Graphics2D g2 = (Graphics2D) g;
//		g2.setColor(new Color(255, 165, 0, alpha / 3));
		g2.setPaint(new GradientPaint(0, getHeight() * alpha / 255, new Color(255, 165, 0, 0), 0, getHeight(),
				new Color(255, 165, 0, alpha / 3)));
		g2.fillRect(0, 0, getWidth(), getHeight());
//		if(alpha > 100) {
//			g2.setColor(new Color(255, 255, 255));
//			g2.fillRect(0, 0, getWidth(), getHeight());
//		}

		g2.setColor(new Color(255, 165, 0, alpha));
		for (int i = 0; i < ps.size(); i++) {
			Point pt = ps.get(i);
			g2.fillOval(pt.x, pt.y, 15, 15);
		}
	}
}
