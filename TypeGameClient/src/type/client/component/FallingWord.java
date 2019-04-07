package type.client.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class FallingWord extends JComponent {
	private static final long serialVersionUID = -4281108143529872395L;
	public String text;
	public boolean fin = false;
	public JLabel jla;
	public double y = -50;
	public FallingWord() {
		setFont(new Font("맑은 고딕", 1, 30));
	}
	public JLabel getJla() {
		if(jla ==null) {
			jla =new JLabel(text);
			jla.setFont(getFont());
			jla.setHorizontalAlignment(JLabel.CENTER);
			jla.setForeground(Color.white);
			jla.setSize(jla.getPreferredSize().width + 100, jla.getPreferredSize().height);
		}
		return jla;
	}
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(255, 255, 255, 50));
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		
//		g2.setColor(Color.white);
//		g2.setFont(getFont());
//		FontMetrics fm = g2.getFontMetrics(g2.getFont());
//		g2.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
	}
}
