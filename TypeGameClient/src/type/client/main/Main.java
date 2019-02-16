package type.client.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main {
	public static String version = "1.0";
	public static String verSuf = "alpha";
	public static void main(String[] args) {
		new Main();
	}
	public Main() {
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frm = new JFrame("Typing game");
		frm.setUndecorated(true);
		frm.setSize(ss);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		App app = new App();
		app.setBackground(Color.BLACK);
		frm.setContentPane(app);
		
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = environment.getDefaultScreenDevice();
		device.setFullScreenWindow(frm);
		
		frm.setTitle(frm.getTitle() + version + verSuf);
	}
}
