package type.client.component;

import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

public class KButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	public KButton() {
		setBorder(null);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getBackground());
		if (getModel().isRollover()) {
			g2.setColor(g2.getColor().darker());
		}
		if(isOpaque())
			g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setFont(getFont());
		g2.setColor(getForeground());
		FontMetrics fm = g2.getFontMetrics();
		if (getModel().isPressed()) {
			g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
					(getHeight() - fm.getHeight()) / 2 + fm.getAscent() + 5);
		} else {
			g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
					(getHeight() - fm.getHeight()) / 2 + fm.getAscent());
		}
	}
}
