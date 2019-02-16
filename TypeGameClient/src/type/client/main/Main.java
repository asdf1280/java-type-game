package type.client.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;

import type.client.main.App.Layers;
import type.client.screen.SConnect;
import type.client.screen.SCursor;
import type.client.screen.SMenu;
import type.client.work.Utils;

public class Main {
	public static String version = "1.0";
	public static String verSuf = "alpha";
	public static void main(String[] args) {
		new Main();
	}
	public static App app;
	public Main() {
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frm = new JFrame("Typing game");
		frm.setUndecorated(true);
		frm.setSize(ss);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		app = new App();
		app.setBackground(Color.BLACK);
		frm.setContentPane(app);
		
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = environment.getDefaultScreenDevice();
		device.setFullScreenWindow(frm);
		
		frm.setTitle(frm.getTitle() + version + verSuf);
		
		SConnect connect = new SConnect();
		app.lp.setLayer(connect, Layers.LOAD.layer);
		app.lp.add(connect);
		connect.initializeComponent();
		
		SCursor cursor = new SCursor();
		app.lp.setLayer(cursor, Layers.CURSOR.layer);
		app.lp.add(cursor);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Utils.sleep(500);
				app.lp.remove(connect);
				SMenu menu = new SMenu(cursor);
				app.lp.setLayer(menu, Layers.MENU.layer);
				app.lp.add(menu);
				menu.initializeComponent();
			}
		}).start();
	}
}
