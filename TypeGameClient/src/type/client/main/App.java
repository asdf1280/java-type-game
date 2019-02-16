package type.client.main;

import java.awt.BorderLayout;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import type.client.work.Utils;

public class App extends JPanel {
	public static enum Layers {
		CURSOR(10000), LOAD(100), MENU(50);
		public final int layer;
		private Layers(int layer) {
			this.layer = layer;
		}
	}
	private static final long serialVersionUID = 9113393245310778419L;

	public JLayeredPane lp = null;
	
	public App() {
		setLayout(new BorderLayout());
		
		lp = new JLayeredPane();
		lp.setLayout(new BorderLayout());
		add(lp, BorderLayout.CENTER);
		
		new Thread(() -> {
			while(true) {
				repaint();
				validate();
				Utils.sleep(0);
			}
		}).start();
	}
}
