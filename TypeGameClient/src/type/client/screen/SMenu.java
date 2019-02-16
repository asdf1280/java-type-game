package type.client.screen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import type.client.component.KButton;
import type.client.main.Main;
import type.client.main.App.Layers;
import type.client.work.Utils;

public class SMenu extends JPanel {
	private static final long serialVersionUID = -4137622001161681835L;

	private SCursor cursor;
	public SMenu(SCursor cursor) {
		loadColor = new Color(66, 134, 244);
		this.cursor = cursor;
	}
	private final Color loadColor;
	
	protected KButton Button1;
	protected JTextPane TextPane1;
	
	public void initializeComponent() {
		// SMenu
		setLayout(null);
		
		//Button1
		Button1 = new KButton();
		Button1.setFont(new Font("맑은 고딕", 1, 50));
		Button1.setText("시작");
		Button1.setSize(250, 130);
		Button1.setBackground(new Color(255, 215, 0));
		Button1.setLocation(0, getHeight() - 130);
		Button1.setForeground(Color.white);
		Button1.addMouseListener(cursor);
		Button1.addActionListener(e -> {
			Main.app.lp.remove(SMenu.this);
			
			SLoad load = new SLoad(cursor);
			Main.app.lp.setLayer(load, Layers.LOAD.layer);
			Main.app.lp.add(load);
			load.initializeComponent();
			
			new Thread(() -> {
				Utils.sleep(1000);
				
				Main.app.lp.remove(load);
				
				SPlay play = new SPlay(cursor);
				Main.app.lp.setLayer(play, Layers.MENU.layer);
				Main.app.lp.add(play);
				play.initializeComponent();
			}).start();
		});
		add(Button1);
		
		//TextPane1
		TextPane1 = new JTextPane();
		TextPane1.setFont(new Font("바탕체", 1, 120));
		TextPane1.setText("타자연습");
		TextPane1.setOpaque(false);
		TextPane1.setForeground(Color.white);
		TextPane1.setEditable(false);
		TextPane1.addMouseListener(cursor);
		TextPane1.setSelectionColor(new Color(0, 0, 0, 0));
		TextPane1.setSelectedTextColor(new Color(255, 255, 255));
		TextPane1.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		TextPane1.setSize(Utils.getSize(TextPane1.getPreferredSize()));
		add(TextPane1);
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
