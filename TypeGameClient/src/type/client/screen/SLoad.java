package type.client.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import type.client.work.Utils;

public class SLoad extends JPanel {
	private static final long serialVersionUID = -6907061636915237874L;
	SCursor cursor;

	public SLoad(SCursor cursor) {
		this.cursor = cursor;
		addMouseListener(cursor);
		addMouseMotionListener(cursor);
	}

	private JTextPane TextPane1;
	public void initializeComponent() {
		// SLoad
		setLayout(null);
		// TextPane1
		TextPane1 = new JTextPane();
		TextPane1.setText("불러오는 중... 잠시만 기다리십시오.");
		TextPane1.setFont(new Font("굴림", 0, 50));
		TextPane1.setLocation(100, 100);
		TextPane1.setForeground(Color.white);
		TextPane1.setOpaque(false);
		TextPane1.setDisabledTextColor(TextPane1.getForeground());
		TextPane1.setEnabled(false);
		TextPane1.setSize(Utils.getSize(TextPane1.getPreferredSize()));
		add(TextPane1);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Utils.setRenderingHints(g2);
		super.paint(g2);
	}

	private final Color loadBackground = new Color(128, 0, 0);

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(loadBackground);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}
}
