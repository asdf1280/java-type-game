package type.client.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.commons.io.IOUtils;

import type.client.component.FallingWord;
import type.client.work.Utils;

public class SPlay extends JPanel {
	private static final long serialVersionUID = 3860718781073591552L;
	SCursor cursor;
	
	public SPlay(SCursor cursor) {
		this.cursor = cursor;
		addMouseListener(cursor);
		addMouseMotionListener(cursor);
	}
	
	private JTextPane TextPane1;
	private JTextField TextField1;
	
	int score = 0;
	public void initializeComponent() {
		// SLoad
		setLayout(null);
		// TextPane1
		TextPane1 = new JTextPane();
		TextPane1.setText("점수: 0000000000000000");
		TextPane1.setFont(new Font("굴림", 0, 50));
		TextPane1.setLocation(100, 100);
		TextPane1.setForeground(Color.white);
		TextPane1.setOpaque(false);
		TextPane1.setDisabledTextColor(TextPane1.getForeground());
		TextPane1.setEnabled(false);
		TextPane1.setSize(Utils.getSize(TextPane1.getPreferredSize()));
		add(TextPane1);
		
		try {
			String[] wds = IOUtils.toString(getClass().getResourceAsStream("/type/client/resource/words"), "UTF8").split(Pattern.quote("\n"));
			
			ArrayList<FallingWord> words = new ArrayList<>();
			
			TextField1 = new JTextField(15);
			TextField1.setText("");
			TextField1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					for(FallingWord wd : words) {
						if(wd.text.equalsIgnoreCase(TextField1.getText())) {
							remove(wd);
							TextPane1.setText("점수: " + ++score);
						}
					}
					TextField1.setText("");
				}
			});
			TextField1.setFont(new Font("맑은 고딕", 1, 35));
			TextField1.setSize(TextField1.getPreferredSize());
			TextField1.setLocation(0, getHeight() - TextField1.getHeight());
			add(TextField1);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					TextPane1.setText("점수: 0");
					while(isDisplayable()) {
						Utils.sleep(333);
						FallingWord e = new FallingWord();
//						e.init(SPlay.this, false);
						e.setSize(200, 50);
						e.setLocation((int)(Math.random() * (getWidth() - 200)), (int)(Math.random() * 100));
						e.text = wds[(int)(Math.random() * wds.length)].split(Pattern.quote("\r"))[0];
						words.add(e);
						add(e);
					}
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(isDisplayable()) {
						for(int i=0;i<words.size();i++) {
							FallingWord fw = words.get(i);
							fw.setLocation(fw.getX(), fw.getY() + 2);
							if(fw.getY() > getHeight()) {
								remove(fw);
								words.remove(fw);
								i--;
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
