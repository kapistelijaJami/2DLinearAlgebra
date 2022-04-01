package linearalgebra;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class LineSegment {
	private Point2D start;
	private Point2D end;
	
	public LineSegment(Point2D end) {
		this.end = end;
	}
	
	public LineSegment(Point2D start, Point2D end) {
		this.start = start;
		this.end = end;
	}
	
	public void render(Graphics2D g) {
		render(g, Color.red);
	}
	
	public void render(Graphics2D g, double thickness) {
		render(g, Color.red, thickness);
	}
	
	public void render(Graphics2D g, Color color) {
		g.setColor(color);
		if (start == null) {
			drawLine(g, end);
		} else {
			drawLine(g, start, end);
		}
	}
	
	public void render(Graphics2D g, Color color, double thickness) {
		g.setColor(color);
		if (start == null) {
			drawLine(g, end, thickness);
		} else {
			drawLine(g, start, end, thickness);
		}
	}
	
	private void drawLine(Graphics2D g, Point2D end) {
		drawLine(g, new Point2D(0, 0), end);
	}
	
	private void drawLine(Graphics2D g, Point2D end, double thickness) {
		drawLine(g, new Point2D(0, 0), end, thickness);
	}
	
	private void drawLine(Graphics2D g, Point2D start, Point2D end) {
		drawLine(g, start, end, 2);
	}
	
	private void drawLine(Graphics2D g, Point2D start, Point2D end, double thickness) {
		g.setStroke(new BasicStroke((float) Game.GAME.screenToWorldUnits(thickness)));
		g.drawLine((int) Game.gridToPixels(start.x), (int) Game.gridToPixels(start.y), (int) Game.gridToPixels(end.x), (int) Game.gridToPixels(end.y));
	}
}
