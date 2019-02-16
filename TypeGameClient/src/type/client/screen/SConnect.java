package type.client.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import type.client.work.Utils;

public class SConnect extends JPanel {
	private static final long serialVersionUID = -9136329134291001979L;

	public SConnect() {
		loadColor = new Color(66, 134, 244);
	}

	private final Color loadColor;

	protected JTextPane TextPane1;
	protected JPanel Panel1;
	protected JTextPane TextPane2;

	public void initializeComponent() {
		// SConnect
		setLayout(null);

		// TextPane1
		TextPane1 = new JTextPane();
		TextPane1.setText("온라인 타자연습");
		TextPane1.setFont(new Font("맑은 고딕", 1, 100));
		TextPane1.setSize(TextPane1.getPreferredSize().width + 1, TextPane1.getPreferredSize().height);
		TextPane1.setOpaque(false);
		TextPane1.setForeground(Color.white);
		TextPane1.setLocation((getWidth() - TextPane1.getWidth()) / 2, 100);
		add(TextPane1);

		// Panel1
		Panel1 = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(getForeground());
				g2.fillOval(0, 0, getWidth(), getHeight());

				g2.setColor(getBackground());
				g2.fillRoundRect(30, 30, getWidth() - 60, getHeight() - 60, 30, 30);

				String[] strs = { "--", "\\", "|", "/" };
				g2.setColor(Color.white);
				g2.setFont(getFont());
				FontMetrics fm = g2.getFontMetrics();
				String txt = strs[(int) ((System.currentTimeMillis() / 100) % strs.length)];
				g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2,
						(getHeight() - fm.getHeight()) / 2 + fm.getAscent() - fm.getDescent());
			}
		};
		Panel1.setForeground(Color.white);
		Panel1.setBackground(Color.BLACK);
		Panel1.setFont(new Font("Segoe UI", 1, 100));
		Panel1.setSize(200, 200);
		Panel1.setLocation(getWidth() / 2 - 100, 300);
		add(Panel1);

		TextPane2 = new JTextPane();
		TextPane2.setFont(new Font("맑은 고딕", 1, 40));
		TextPane2.setText("INITIALIZING...");
		TextPane2.setSize(TextPane2.getPreferredSize().width + 1, TextPane2.getPreferredSize().height);
		TextPane2.setForeground(Color.white);
		TextPane2.setOpaque(false);
		TextPane2.setLocation((getWidth() - TextPane2.getWidth()) / 2, getHeight() - 300);
		add(TextPane2);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Utils.setRenderingHints(g2);
		super.paint(g2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Utils.setRenderingHints(g2);

		g2.setColor(loadColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}
}
