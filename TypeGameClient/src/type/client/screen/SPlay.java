package type.client.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.apache.commons.io.IOUtils;

import type.client.component.ExplosionEffect;
import type.client.component.FallingWord;
import type.client.main.App.Layers;
import type.client.main.Main;
import type.common.work.Utils;

public class SPlay extends JPanel {
	private static final long serialVersionUID = 3860718781073591552L;
	SCursor cursor;

	public SPlay(SCursor cursor) {
		this.cursor = cursor;
		addMouseListener(cursor);
		addMouseMotionListener(cursor);
	}

	private JLabel Label1;
	private JLabel Label2;
	private JLabel Label3;
	private JLabel Label4;
	private JTextField TextField1;

	long lastWord = System.currentTimeMillis();
	long keys = 0;

	final long startTime = System.currentTimeMillis();
	Random r = new Random();
	double score = 10;

	public void initializeComponent(String filename) {
		// SLoad
		setLayout(null);
		Main.app.getRootPane().getActionMap().put(this, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.app.getRootPane().getActionMap().remove(SPlay.this);
				Main.app.getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW)
						.remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));

				Main.app.lp.remove(SPlay.this);

				SEnd em = new SEnd(cursor);
				Main.app.lp.setLayer(em, Layers.MENU.layer);
				Main.app.lp.add(em);
				em.initializeComponent(new Runnable() {

					@Override
					public void run() {
						Main.nw.singleToggle(false);
					}
				});
				em.TextPane1.setText("당신의 마지막 점수: \n" + score);
				em.Label2.setText("사용자가 게임을 종료했습니다.");
			}
		});
		Main.app.getRootPane().getInputMap(JRootPane.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), this);

		// Label1
		Label1 = new JLabel();
		Label1.setText("SCORE: ");
		Label1.setFont(new Font("Segoe UI", 1, 25));
		Label1.setLocation(100, 100);
		Label1.setForeground(Color.white);
		Label1.setOpaque(false);
		Label1.setSize(Utils.getSize(Label1.getPreferredSize()));
		add(Label1);

		// Label2
		Label2 = new JLabel();
		Label2.setText("10");
		Label2.setFont(new Font("맑은 고딕", 2, 50));
		Label2.setLocation(Label1.getX() + Label1.getWidth(), 100);
		Label2.setForeground(Color.white);
		Label2.setOpaque(false);
		Label2.setSize(300, Label2.getPreferredSize().height);
		add(Label2);

		// Label3
		Label3 = new JLabel("현재타수: 0");
		Label3.setFont(new Font("맑은 고딕", 1, 30));
		Label3.setLocation(300, 0);
		Label3.setSize(300, Label3.getPreferredSize().height);
		Label3.setOpaque(false);
		Label3.setForeground(Color.white);
		add(Label3);

		// Label4
		Label4 = new JLabel("낙하계수: 0");
		Label4.setFont(Label3.getFont());
		Label4.setLocation(Label3.getX() + Label3.getWidth(), 0);
		Label4.setSize(300, Label4.getPreferredSize().height);
		Label4.setOpaque(false);
		Label4.setForeground(Color.white);
		add(Label4);

		try {
			String[] wds = IOUtils.toString(getClass().getResourceAsStream("/type/client/resource/" + filename), "UTF8")
					.split(Pattern.quote("\n"));

			final List<FallingWord> words = Collections.synchronizedList(new ArrayList<>());

			TextField1 = new JTextField(15);
			TextField1.setText("");
			TextField1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (FallingWord wd : words) {
						if (wd.text.equalsIgnoreCase(TextField1.getText())) {
							remove(wd);
							remove(wd.jla);
							wd.fin = true;
							Label2.setForeground(Color.red);
							double scored = wd.text.length();
							if (wd.jla.getForeground().equals(Color.GREEN)) {
								scored *= 2.5;
							} else if (wd.jla.getForeground().equals(Color.RED)) {
								scored *= -0.5;

								ExplosionEffect ee = new ExplosionEffect(getSize());
								ee.setLocation(0, 0);
								add(ee);
							} else if (wd.getJla().getForeground().equals(Color.yellow)) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										int st = 200;
										while (!words.isEmpty()) {
											FallingWord fw = words.remove(0);
											score += fw.text.length() / 2;
											Label2.setText("" + score);
											remove(fw);
											remove(fw.jla);
											keys += fw.text.length();
											if (fw.jla.getForeground().equals(Color.yellow))
												st = st / 8 * 5;
											Utils.sleep(st);
										}
									}
								}).start();
								continue;
							}
							score += scored;
							Label2.setText("" + score);
							if (score < -10) {
								Main.app.lp.remove(SPlay.this);

								SEnd em = new SEnd(cursor);
								Main.app.lp.setLayer(em, Layers.MENU.layer);
								Main.app.lp.add(em);
								em.initializeComponent(new Runnable() {

									@Override
									public void run() {
										Main.nw.singleToggle(false);
										
									}
								});
								
								em.TextPane1.setText("당신의 마지막 점수: \n" + score);
								em.Label2.setText("힘내세요!");
							}
						}
					}
					TextField1.setText("");
					Label3.setText("현재타수: " + ((double) keys * 1000 / (System.currentTimeMillis() - lastWord) * 60.0));
					lastWord = 0;
					keys = 0;
				}
			});
			TextField1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (lastWord == 0)
						lastWord = System.currentTimeMillis();
					if (TextField1.getText().isBlank())
						lastWord = System.currentTimeMillis();
					if (e.getKeyCode() != 13)
						keys++;
				}
			});
			TextField1.setFont(new Font("맑은 고딕", 1, 35));
			TextField1.setSize(TextField1.getPreferredSize());
			TextField1.setLocation(0, getHeight() - TextField1.getHeight());
			add(TextField1);
//			
//			setLayout(new FlowLayout() {
//
//				/**
//				 * 
//				 */
//				private static final long serialVersionUID = 1L;
//				@Override
//				public void layoutContainer(Container target) {
//					TextField1.setSize(TextField1.getPreferredSize());
//					TextField1.setLocation(0, getHeight() - TextField1.getHeight());
//					
//					Label4.setLocation(Label3.getX() + Label3.getWidth(), 0);
//					Label4.setSize(300, Label4.getPreferredSize().height);
//					
//					Label3.setLocation(300, 0);
//					Label3.setSize(300, Label3.getPreferredSize().height);
//					
//					Label2.setLocation(Label1.getX() + Label1.getWidth(), 100);
//					Label2.setSize(100, Label2.getPreferredSize().height);
//					
//					Label1.setLocation(100, 100);
//					Label1.setSize(Utils.getSize(Label1.getPreferredSize()));
//				}
//				
//			});

			new Thread(new Runnable() {

				@Override
				public void run() {
					String last = "1";
					while (isDisplayable()) {
						Utils.sleep(last.length() * 200);
						FallingWord e = new FallingWord();
						e.setSize(200, 50);
						e.text = wds[(int) (Math.random() * wds.length)].split(Pattern.quote("\r"))[0].toLowerCase();
						double rd = r.nextDouble();
//						System.out.println(rd);
						if ((rd) < 0.05) {
							e.getJla().setForeground(Color.green);
						} else if ((rd -= 0.05) < 0.05) {
							e.getJla().setForeground(Color.red);
						} else if ((rd -= 0.05) < 0.2) {
							e.getJla().setForeground(Color.yellow);
						}
						last = e.text;
						words.add(e);
						add(e.getJla());
						e.setLocation((int) (Math.random() * (getWidth() - e.getJla().getPreferredSize().width * 2))
								+ e.getJla().getPreferredSize().width, -50);
						e.getJla().setLocation(e.getX() - e.getJla().getWidth() / 2, e.getJla().getY());
						if (e.getJla().getX() < 0) {
							e.getJla().setLocation(0, e.getJla().getY());
						} else if (e.getJla().getX() >= getWidth() - e.getJla().getWidth()) {
							e.getJla().setLocation(getWidth() - e.getJla().getWidth(), e.getJla().getY());
						}
						add(e);
					}
				}
			}).start();

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (isDisplayable()) {
						double d = 2.0 / 10 * Math.max(score, 20)
								/ (70.0 + (startTime - System.currentTimeMillis()) / -6000.0);
						d = Math.max(d, 0.7);
						Label4.setText("낙하 계수: " + ((double) Math.round(d * 10000) / 10000));
						for (int i = 0; i < words.size(); i++) {
							FallingWord fw = words.get(i);
							if (fw.jla == null) {
								continue;
							}
							fw.y += d * 10 / fw.jla.getText().length();
							fw.setLocation(fw.getX(), (int) fw.y);
							if (fw.jla != null)
								fw.jla.setLocation(fw.getX() + fw.getWidth() / 2 - fw.jla.getWidth() / 2,
										fw.getY() + fw.getHeight() / 2 - fw.jla.getHeight() / 2);
							if (fw.getY() > getHeight()) {
								remove(fw);
								remove(fw.jla);
								words.remove(fw);
								i--;
								if (!fw.fin && !fw.jla.getForeground().equals(Color.RED)) {
									Label2.setForeground(Color.green);
									score -= 0.5;
									Label2.setText("" + score);
								}
							}
						}
						Utils.sleep(6);
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
