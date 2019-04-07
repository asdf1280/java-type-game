package type.client.screen;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import type.client.component.KButton;
import type.client.main.App.Layers;
import type.client.main.Main;
import type.client.net.ChatCallback;
import type.client.net.NetworkWorker;
import type.common.work.Utils;

public class SMenu extends JPanel {
	private static final long serialVersionUID = -4137622001161681835L;

	private SCursor cursor;
	private NetworkWorker nw;

	public SMenu(SCursor cursor, NetworkWorker nw) {
		loadColor = new Color(66, 134, 244);
		this.cursor = cursor;
		this.nw = nw;
	}

	private final Color loadColor;

	protected KButton Button1;
	protected KButton Button2;
	protected JTextPane TextPane1;
	protected JTextPane TextPane2;
	protected JTextField TextField1;
	protected JLabel Label1;

	public void initializeComponent() {
		// SMenu
		setLayout(null);

		Main.frm.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				nw.closeConnection();
				System.exit(0);
			}
		});

		// Button1
		Button1 = new KButton();
		Button1.setFont(new Font("맑은 고딕", 1, 50));
		Button1.setText("싱글플레이");
		Button1.setSize(350, 120);
		Button1.setBackground(new Color(255, 215, 0));
		Button1.setLocation((getWidth() - Button1.getWidth()) / 2, (getHeight() - Button1.getHeight()) / 2);
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

				Main.nw.singleToggle(true);

				SPlay play = new SPlay(cursor);
				Main.app.lp.setLayer(play, Layers.MENU.layer);
				Main.app.lp.add(play);
				play.initializeComponent("searchedwords");
			}).start();
		});
		add(Button1);

		Button2 = new KButton();
		Button2.setText("종료");
		Button2.setBackground(new Color(255, 50, 50));
		Button2.setForeground(Color.white);
		Button2.setSize(200, 100);
		Button2.addMouseListener(cursor);
		Button2.setFont(new Font("맑은 고딕", 1, 30));
		Button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Main.frm.dispose();
			}
		});
		add(Button2);

		// TextPane1
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
		TextPane1.setLocation((getWidth() - TextPane1.getWidth()) / 2, 100);
		add(TextPane1);

		TextPane2 = new JTextPane();
		TextPane2.setEnabled(false);
		TextPane2.setDisabledTextColor(Color.white);
		TextPane2.setOpaque(false);
		TextPane2.setFont(new Font("바탕", 0, 20));
		nw.setChatCallback(new ChatCallback() {
			private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			@Override
			public void processChat(String adr, String text) {
				if(text.isBlank()) return;
				
				TextPane2.setText("[" + adr + "][" + sdf.format(new Date(System.currentTimeMillis())) + "] " + text + "\n" + TextPane2.getText());
				if(TextPane2.getText().split(Pattern.quote("\n")).length > 11) {
					String[] ar = TextPane2.getText().split(Pattern.quote("\n"));
					String sp = "\n" + ar[ar.length-1];
					TextPane2.setText(TextPane2.getText().substring(0, TextPane2.getText().length() - sp.length()));
				}
			}
		});
		// add(TextPane2);
		JScrollPane jsp = new JScrollPane(TextPane2);
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jsp.setSize(getWidth() / 2, 400);
		jsp.setLocation(0, getHeight() - 400);

		TextPane2.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				scrollToBottom();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				scrollToBottom();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				scrollToBottom();
			}

			private void scrollToBottom() {
				jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());

			}
		});
		jsp.setBorder(null);
		jsp.setViewportBorder(null);
		add(jsp);

		TextField1 = new JTextField();
		TextField1.setFont(new Font("맑은 고딕", 1, 40));
		TextField1.setSize(getWidth() / 2, 60);
		TextField1.setLocation(0, getHeight() - 460);
		TextField1.setBorder(null);
		TextField1.setBackground(Color.black);
		TextField1.setForeground(Color.white);
		TextField1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(TextField1.getText().length() < 70) {
					nw.chat(TextField1.getText());
				}
				TextField1.setText("");
			}
		});
		add(TextField1);

		Label1 = new JLabel("채팅: ");
		Label1.setFont(new Font("맑은 로딕", 0, 40));
		Label1.setSize(Label1.getPreferredSize());
		Label1.setForeground(Color.white);
		Label1.setOpaque(false);
		add(Label1);
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