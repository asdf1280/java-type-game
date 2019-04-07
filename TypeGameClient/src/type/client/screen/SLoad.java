package type.client.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import type.common.work.Utils;

public class SLoad extends JPanel {
	private static final long serialVersionUID = -6907061636915237874L;
	SCursor cursor;

	public SLoad(SCursor cursor) {
		this.cursor = cursor;
		addMouseListener(cursor);
		addMouseMotionListener(cursor);
	}
	
	JLabel Label1;
	JLabel Label2;
	JPanel Panel1;
	public void initializeComponent() {
		// SLoad
		setLayout(null);
		// Label1
		Label1 = new JLabel("불러오는 중... 잠시만 기다려주세요");
		Label1.setForeground(Color.white);
		Label1.setFont(new Font("맑은 고딕", 1, 40));
		Label1.setSize(Utils.getSize(Label1.getPreferredSize()));
		Label1.setLocation(0, getHeight() - Label1.getHeight());
		add(Label1);
		
		// Label2
		Label2 = new JLabel(".");
		Label2.setForeground(Color.white);
		Label2.setFont(new Font("맑은 고딕", 1, 40));
		Label2.setSize(100, Label2.getPreferredSize().height);
		Label2.setLocation(Label1.getX() + Label1.getWidth() - 10, Label1.getY());
		add(Label2);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(isDisplayable()) {
					Utils.sleep(300);
					Label2.setText(Label2.getText() + ".");
				}
			}
		}).start();
		
		// Panel1
		Panel1 = new JPanel();
		Panel1.setBackground(new Color(0, 0, 0, 80));
		Panel1.setOpaque(true);
		Panel1.setSize(getWidth(), Label1.getHeight());
		Panel1.setLocation(0, getHeight() - Label1.getHeight());
		add(Panel1);
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
