package linearalgebra;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Line {
	//Multiple ways to store an infinite line, here's one:
	private Point2D dir;		//This line is spanned by dir, literally the direction of the line.
	private Point2D point;		//Any point on the line.
	
	public Line(Point2D dir) {
		this(dir, new Point2D(0, 0));
	}
	
	public Line(Point2D dir, Point2D point) {
		this.dir = dir;
		this.point = point;
	}
	
	public void render(Graphics2D g, double thickness) {
		g.setColor(Color.red);
		g.setStroke(new BasicStroke((float) Game.GAME.screenToWorldUnits(thickness)));
		drawLine(g);
	}
	
	private void drawLine(Graphics2D g) { //TODO: Finish this. You could also make the lines on the map be these lines, and not line segments.
		//check if the line is visible (maybe a distance to the line from the center and if it's shorter than a circle that would surround the screen, then draw it)
	}
}
