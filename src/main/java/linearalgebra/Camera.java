package linearalgebra;

import java.awt.Color;
import java.awt.Graphics2D;

public class Camera {
	private Point2D pos; //midpoint of camera
	private double zoom = Constants.DEFAULT_ZOOM;
	
	public Camera(Point2D pos) {
		this.pos = pos;
	}
	
	public Camera(double x, double y) {
		pos = new Point2D(x, y);
	}
	
	public Point2D getPos() {
		return pos;
	}
	
	public double getX() {
		return pos.getX();
	}
	
	public double getY() {
		return pos.getY();
	}
	
	public double getLeft() {
		return pos.getX() - getWidth() / 2;
	}
	
	public double getTop() {
		return pos.getY() + getHeight() / 2;
	}
	
	public double getBottom() {
		return pos.getY() - getHeight() / 2;
	}
	
	public Point2D getTopLeftCorner() {
		return new Point2D(getLeft(), getTop());
	}
	
	public double getWidth() {
		return Game.WIDTH / zoom;
	}
	
	public double getHeight() {
		return Game.HEIGHT / zoom;
	}
	
	public Point2D getSize() {
		return new Point2D(getWidth(), getHeight());
	}
	
	public void setPos(Point2D pos) {
		this.pos = pos;
	}
	
	public void setX(double x) {
		pos.setX(x);
	}
	
	public void setY(double y) {
		pos.setY(y);
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		double min = Constants.DEFAULT_ZOOM * Constants.MIN_ZOOM_MULT;
		double max = Constants.DEFAULT_ZOOM * Constants.MAX_ZOOM_MULT;
		if (zoom < min || zoom > max) {
			return;
		}
		this.zoom = zoom;
	}
	
	@Override
	public String toString() {
		return pos.toString() + ", size: " + getSize() + ", zoom: " + zoom;
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.blue);
		g.drawRect((int) getLeft(), (int) getTop(), (int) getWidth(), (int) getHeight());
		g.fillOval((int) getLeft(), (int) (getTop() - Game.gridToPixels(1)), (int) Game.gridToPixels(1), (int) Game.gridToPixels(1));
		
		g.fillOval((int) (pos.x - Game.gridToPixels(0.1)), (int) (pos.y - Game.gridToPixels(0.1)), (int) Game.gridToPixels(0.2), (int) Game.gridToPixels(0.2));
	}

	public boolean isInsideX(double x) {
		double left = getLeft();
		double res = left + getWidth();
		return x >= left && x < res;
	}

	public boolean isInsideY(double y) {
		double top = getTop();
		double res = top - getHeight();
		return y <= top && y > res;
	}
	
	public boolean isInside(Point2D p) {
		return isInsideX(p.x) && isInsideY(p.y);
	}
}
