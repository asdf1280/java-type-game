package type.client.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.IOUtils;

import type.client.component.ExplosionEffect;
import type.client.component.FallingWord;
import type.client.main.App.Layers;
import type.client.main.Client;
import type.client.net.PlayCallback;
import type.common.packet.play.PacketCbPlayCountdownTime;
import type.common.packet.play.PacketCbPlayGameMessage;
import type.common.packet.play.PacketCbPlayGameOver;
import type.common.packet.play.PacketCbPlayHealthStatus;
import type.common.packet.play.PacketCbPlayLeftUsers;
import type.common.packet.play.PacketCbPlayPrepareGame;
import type.common.packet.play.PacketCbPlaySetHealth;
import type.common.packet.play.PacketCbPlaySetMaxHealth;
import type.common.work.Utils;

public class SPlayOnline extends JPanel {
	private static final long serialVersionUID = 3860718781073591552L;
	SCursor cursor;
	public PlayCallback pc;

	public SPlayOnline(SCursor cursor) {
		this.cursor = cursor;
		addMouseListener(cursor);
		addMouseMotionListener(cursor);
	}

	private JLabel Label1;
	private JLabel Label2;
	private JLabel Label3;
	private JLabel Label4;
	private JTextField TextField1;

	// countdown comp
	private JLabel CountTime;
	private JLabel CountDesc;

	long lastWord = System.currentTimeMillis();
	long keys = 0;

	final long startTime = System.currentTimeMillis();
	Random r = new Random();
	double score = 10;

	public void initializeComponent(String filename) {
		// SLoad
		setLayout(null);

		// Label1
		Label1 = new JLabel();
		Label1.setText("ALIVE: ");
		Label1.setFont(new Font("Segoe UI", 1, 35));
		Label1.setLocation(100, 100);
		Label1.setForeground(Color.white);
		Label1.setOpaque(false);
		Label1.setSize(400, Utils.getSize(Label1.getPreferredSize()).height);
		add(Label1);
		Label1.setText("JOINED: ");

		// Label2
		Label2 = new JLabel();
		Label2.setText("-1");
		Label2.setFont(new Font("맑은 고딕", 2, 60));
		Label2.setSize(300, Label2.getPreferredSize().height);
		Label2.setLocation(Label1.getX(), Label1.getHeight() + Label1.getY());
		Label2.setForeground(Color.white);
		Label2.setOpaque(false);
		add(Label2);

		// Label3
		Label3 = new JLabel("현재 타수: 0");
		Label3.setFont(new Font("맑은 고딕", 1, 30));
		Label3.setLocation(300, 0);
		Label3.setSize(300, Label3.getPreferredSize().height);
		Label3.setOpaque(false);
		Label3.setForeground(Color.white);
		add(Label3);

		// Label4
		Label4 = new JLabel("낙하 속도: 0");
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
											Client.nw.play_score(fw.text.length() / 2);
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
							Client.nw.play_score((int) scored);
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
			TextField1.setLocation((getWidth() - TextField1.getWidth()) / 2, getHeight() - TextField1.getHeight());
			add(TextField1);

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (isDisplayable()) {
						Utils.sleep(2000);
						if (!gameStarted)
							continue;
						FallingWord e = new FallingWord();
						e.setSize(200, 50);
						e.text = wds[(int) (Math.random() * wds.length)].split(Pattern.quote("\r"))[0].toLowerCase();
						double rd = r.nextDouble();
						if ((rd) < 0.44) {
							e.getJla().setForeground(Color.green);
						} else if ((rd -= 0.44) < 0.03) {
							e.getJla().setForeground(Color.red);
						} else if ((rd -= 0.03) < 0.4) {
							e.getJla().setForeground(Color.yellow);
						}
						words.add(e);
						add(e.getJla());
						e.setLocation((int) (Math.random() * (getWidth() - e.getJla().getPreferredSize().width * 2))
								+ e.getJla().getPreferredSize().width, -50);
						e.getJla().setLocation(e.getX() - e.getJla().getWidth() / 2, e.getJla().getY());
						if (e.getJla().getX() + e.getJla().getWidth() > getWidth()) {
							e.getJla().setLocation(getWidth() - e.getJla().getWidth(), e.getJla().getY());
						} else if (e.getJla().getX() < 0) {
							e.getJla().setLocation(0, e.getJla().getY());
						}
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
						double d = 1.7 / 14 * Math.max(score, 20)
								/ (60.0 + (startTime - System.currentTimeMillis()) / -3000.0);
						d = Math.max(d, 0.7);
						Label4.setText("낙하 계수: " + ((double) Math.round(d * 10000) / 10000));
						for (int i = 0; i < words.size(); i++) {
							FallingWord fw = null;
							try {
								fw = words.get(i);
							} catch (ArrayIndexOutOfBoundsException e) {
								continue;
							}
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
									score -= 0.5;
								}
							}
						}
						Utils.sleep(10);
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		CountTime = new JLabel("    ");
		CountTime.setFont(new Font("Segoe UI", 1, 130));
		CountTime.setSize(getWidth() / 2, CountTime.getPreferredSize().height);
		CountTime.setLocation(getWidth() / 4, getHeight() / 2 - CountTime.getHeight() / 2);
		CountTime.setHorizontalAlignment(JLabel.CENTER);
		CountTime.setForeground(Color.white);
		add(CountTime);

		CountDesc = new JLabel("게임 시작까지 남은 시간");
		CountDesc.setFont(new Font("맑은 고딕", 0, 30));
		CountDesc.setSize(CountDesc.getPreferredSize());
		CountDesc.setLocation((getWidth() - CountDesc.getWidth()) / 2, CountTime.getY() - CountDesc.getHeight() - 10);
		CountDesc.setForeground(Color.white);
		add(CountDesc);

		pc = new PlayCallback() {

			@Override
			public void process(PacketCbPlaySetMaxHealth packet) {
				maxHealth = (int) packet.maxhealth;
			}

			@Override
			public void process(PacketCbPlaySetHealth packet) {
				health = (int) packet.health;
			}

			@Override
			public void process(PacketCbPlayLeftUsers packet) {
				Label2.setText("" + packet.left);
			}

			@Override
			public void process(PacketCbPlayGameMessage packet) {
				message = packet.message;
				messageTime = System.currentTimeMillis();
			}

			@Override
			public void process0(PacketCbPlayGameOver packet) {
				Client.app.lp.remove(SPlayOnline.this);

				SEnd em = new SEnd(cursor);
				Client.app.lp.setLayer(em, Layers.MENU.layer);
				Client.app.lp.add(em);
				em.initializeComponent(null);

				int r = packet.rank;
				if (r == 1) {
					em.Label1.setText("이겼닭! 오늘 저녁은 치킨이닭!");
				} else if (r <= 10) {
					em.Label1.setText("TOP 10 달성!");
				} else {
					em.Label1.setText("그럴 수 있어. 이런 날도 있는 거지 뭐.");
				}
				em.TextPane1.setText("사망했습니다.이번 판에서 얻은 점수:\n" + score);
				em.Label2.setText("#" + r);
			}

			@Override
			public void process(PacketCbPlayPrepareGame packet) {
				health = maxHealth;
				gameStarted = true;
				Label1.setText("ALIVE: ");

				remove(CountTime);
				remove(CountDesc);
				CountTime = null;
				CountDesc = null;
			}

			@Override
			public void process(PacketCbPlayCountdownTime packet) {
				if (packet.seconds != -1)
					CountTime.setText(packet.seconds + "");
				Label2.setText(packet.joined + "");
			}

			@Override
			public void process(PacketCbPlayHealthStatus packet) {
				avgHealth = packet.avg;
				maxEHealth = packet.max;
				minHealth = packet.min;
			}
		};
	}

	int maxHealth = 0;
	int health = 0;
	String message = "";
	long messageTime = System.currentTimeMillis();
	boolean gameStarted = false;
	double avgHealth = 0;
	double maxEHealth = 0;
	double minHealth = 0;

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (gameStarted && maxHealth != 0) {
			g.setColor(Color.gray);
			int hgt = getHeight() - 220;
			g.fillRect(getWidth() / 4, hgt, getWidth() / 2, 30);

			g.setColor(Color.RED);
			g.fillRect(getWidth() / 4, hgt, getWidth() * health / 2 / maxHealth, 30);

			g.setColor(Color.white);
			g.fillRect(getWidth() / 4, hgt + 30, getWidth() / 2, 30);

			g.setColor(Color.MAGENTA);
			g.fillRect(getWidth() / 4, hgt + 30, (int) (getWidth() * maxEHealth / 2 / maxHealth), 10);

			g.setColor(Color.blue);
			g.fillRect(getWidth() / 4, hgt + 40, (int) (getWidth() * avgHealth / 2 / maxHealth), 10);

			g.setColor(Color.red);
			g.fillRect(getWidth() / 4, hgt + 50, (int) (getWidth() * minHealth / 2 / maxHealth), 10);
		}

		if (messageTime + 1300 > System.currentTimeMillis() && Label2 != null) {
			long leftTime = messageTime + 1300 - System.currentTimeMillis();
			int alpha = 255;
			if (leftTime < 300) {
				alpha = (int) (leftTime * 255 / 300);
			}
			g2.setColor(new Color(255, 255, 255, alpha));
			g2.setFont(Label2.getFont());
			FontMetrics fm = g2.getFontMetrics();
			g2.drawString(message, (getWidth() - fm.stringWidth(message)) / 2, getHeight() - 300);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
