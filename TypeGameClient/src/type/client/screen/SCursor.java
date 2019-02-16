package type.client.screen;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class SCursor extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 2987606278112063180L;

	public SCursor() {
		setOpaque(false);
	}
	
	public void intiializeComponent() {
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	}
	@Override
	public void paint(Graphics g) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Object source = e.getSource();
		if(source instanceof Component)
			setCursor(((Component) source).getCursor());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Object source = e.getSource();
		if(source instanceof Component)
			setCursor(((Component) source).getCursor());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Object source = e.getSource();
		if(source instanceof Component)
			setCursor(((Component) source).getCursor());
	}
}
