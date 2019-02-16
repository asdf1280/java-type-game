package type.client.main;

import java.awt.BorderLayout;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class App extends JPanel {
	private static final long serialVersionUID = 9113393245310778419L;

	private JLayeredPane lp = null;
	public App() {
		setLayout(new BorderLayout());
		
		lp = new JLayeredPane();
		add(lp, BorderLayout.CENTER);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true)
					repaint();
			}
		}).start();
	}
}
