package type.client.screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import type.client.component.KButton;

public class SFail extends JPanel {
	private static final long serialVersionUID = 1L;

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public JLabel Label1;
	public JLabel Label2;
	public KButton Button1;

	public SFail() {
		setLayout(null);
	}

	public void initializeComponent() {
		Label1 = new JLabel("오류");
		Label1.setFont(new Font("맑은 고딕", 1, 100));
		Label1.setForeground(Color.white);
		add(Label1);

		Label2 = new JLabel("서버에 연결할 수 없습니다.");
		Label2.setFont(new Font("맑은 고딕", 1, 50));
		Label2.setForeground(Color.white);
		add(Label2);

		ActionListener Button1Action = e -> System.exit(0);

		Button1 = new KButton();
		Button1.setBorder(new LineBorder(Color.white, 3));
		Button1.setText("종료");
		Button1.setBackground(Color.red);
		Button1.setForeground(Color.white);
		Button1.addActionListener(Button1Action);
		Button1.setFont(new Font("맑은 고딕", 1, 70));
		add(Button1);

		LayoutManager SFailLayout = new LayoutManager() {

			@Override
			public void removeLayoutComponent(Component comp) {
			}

			@Override
			public Dimension preferredLayoutSize(Container parent) {
				return getSize();
			}

			@Override
			public Dimension minimumLayoutSize(Container parent) {
				// TODO Auto-generated method stub
				return getSize();
			}

			@Override
			public void layoutContainer(Container parent) {
				Label1.setSize(Label1.getPreferredSize());
				Label1.setLocation((getWidth() - Label1.getWidth()) / 2, (getHeight() - Label1.getHeight()) / 2 - 300);

				Label2.setSize(Label2.getPreferredSize());
				Label2.setLocation((getWidth() - Label2.getWidth()) / 2, (getHeight() - Label2.getHeight()) / 2);

				Button1.setSize(300, 150);
				Button1.setLocation(0, getHeight() - Button1.getHeight());
			}

			@Override
			public void addLayoutComponent(String name, Component comp) {
			}
		};
		setLayout(SFailLayout);
		validate();
	}
}
