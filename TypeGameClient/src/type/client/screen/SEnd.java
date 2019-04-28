package type.client.screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import type.client.component.KButton;
import type.client.main.App.Layers;
import type.client.main.Client;
import type.common.work.Utils;

public class SEnd extends JPanel {
	private static final long serialVersionUID = 1L;

	SCursor c;

	public SEnd(SCursor cur) {
		c = cur;
		addMouseListener(cur);
		addMouseMotionListener(cur);
	}

	KButton Button1;
	JLabel Label1;
	public JLabel Label2;
	public JTextPane TextPane1;

	public void initializeComponent(final Runnable endCallback) {
		setLayout(null);
		setBackground(Color.white);
		
		Button1 = new KButton();
		Button1.setText("메인 화면");
		Button1.setForeground(Color.white);
		Button1.setBackground(Color.black);
		Button1.setFont(new Font("맑은 고딕", 0, 25));
		Button1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Client.app.lp.remove(SEnd.this);
				
				SMenu menu = new SMenu(c, Client.nw);
				Client.app.lp.setLayer(menu, Layers.MENU.layer);
				Client.app.lp.add(menu);
				menu.initializeComponent();
				Utils.l.info("SEnd", "Showed main menu");
				
				if(endCallback != null)
					endCallback.run();
			}
		});
		add(Button1);

		Label1 = new JLabel("게임 오버!");
		Label1.setFont(new Font("맑은 고딕", 1, 50));
		Label1.setOpaque(false);
		add(Label1);

		Label2 = new JLabel("");
		Label2.setFont(new Font("맑은 고딕", 0, 40));
		Label2.setOpaque(false);
		add(Label2);
		
		TextPane1 = new JTextPane();
		TextPane1.setFont(new Font("맑은 고딕", 0, 30));
		TextPane1.setOpaque(false);
		TextPane1.setForeground(Color.black);
		TextPane1.setDisabledTextColor(TextPane1.getForeground());
		TextPane1.setEnabled(false);
		add(TextPane1);

		setLayout(new EndLayout());
		validate();
	}

	private class EndLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return getSize();
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return null;
		}

		@Override
		public void layoutContainer(Container parent) {
			Button1.setSize(300, 150);
			Button1.setLocation(0, getHeight() - 150);

			Label1.setSize(Label1.getPreferredSize());
			Label1.setLocation((getWidth() - Label1.getWidth()) / 2, getHeight() / 4);

			Label2.setSize(Label2.getPreferredSize());
			Label2.setLocation((getWidth() - Label2.getWidth()) / 2, getHeight() / 2 - getHeight() / 6);
			
			TextPane1.setSize(getWidth() / 2, getHeight() / 2);
			TextPane1.setLocation(150, getHeight() / 2);
		}

	}
}
