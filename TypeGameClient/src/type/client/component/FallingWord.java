package type.client.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class FallingWord extends JComponent {
	private static final long serialVersionUID = -4281108143529872395L;
	public String text;
	public FallingWord() {
		setFont(new Font("맑은 고딕", 1, 30));
	}
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(255, 255, 255, 50));
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		
		g2.setColor(Color.white);
		g2.setFont(getFont());
		FontMetrics fm = g2.getFontMetrics(g2.getFont());
		g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
	}
}
