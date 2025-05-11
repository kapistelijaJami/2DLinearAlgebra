package linearalgebra;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputListener;

public class KeyInput implements MouseInputListener, MouseWheelListener, KeyListener {
	private Game game;
	Point clickLoc; //where was the click location of the mouse relative to the window
	Point2D clickPos; //what position the camera was at when the click happened
	
	public KeyInput(Game game) {
		this.game = game;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//clickRot = new Point2D(game.getRotX(), game.getRotY());
		clickLoc = e.getPoint();
		Camera cam = game.getCamera();
		clickPos = new Point2D(cam.getX(), cam.getY());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		//game.mouseReleased(e);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point p = e.getPoint();
		Point diff = new Point(p.x - clickLoc.x, p.y - clickLoc.y);
		
		if (checkMouseButtonMask(e, MouseEvent.BUTTON1_DOWN_MASK)) {
			/*game.setRotX(clickRot.getX() + diff.y / Math.max(2, 1.5 * game.zoom));
			game.setRotY(clickRot.getY() + diff.x / Math.max(2, 1.5 * game.zoom));*/
		} else if (checkMouseButtonMask(e, MouseEvent.BUTTON3_DOWN_MASK) || checkMouseButtonMask(e, MouseEvent.BUTTON2_DOWN_MASK)) {
			Camera cam = game.getCamera();
			cam.setX(clickPos.x - diff.x / cam.getZoom());
			cam.setY(clickPos.y + diff.y / cam.getZoom());
		}
	}
	
	private boolean checkMouseButtonMask(MouseEvent e, int mask) {
		return (e.getModifiersEx() & mask) == mask;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//game.mouseMoved(e);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Camera cam = game.getCamera();
		
		double zoomDelta = e.getPreciseWheelRotation() == -1 ? 1.25 : 0.8; //1.25 and 0.8 are exactly opposite zooms
		
		Point2D p = Game.convertScreenToWorld(new Point2D(e.getPoint())).round();
		Point2D diff = cam.getPos().subtract(p); //vector from p to cam mid pos
		Point2D res = diff.div(zoomDelta).round();
		cam.setPos(p.add(res));
		
		cam.setZoom(cam.getZoom() * zoomDelta);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_ESCAPE:
				game.stop();
				break;
			case KeyEvent.VK_SPACE:
				Camera cam = game.getCamera();
				cam.setPos(new Point2D(0, 0));
				cam.setZoom(Constants.DEFAULT_ZOOM);
				System.out.println(cam);
				break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			
		}
	}
}
