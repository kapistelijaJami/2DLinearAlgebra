package linearalgebra;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import static linearalgebra.Constants.PIXELS_PER_GRID;
import static linearalgebra.Game.HEIGHT;
import static linearalgebra.Game.WIDTH;
import static linearalgebra.Game.gridToPixels;

public class Grid {
	
	public void render(Graphics2D g) {
		g.setColor(Color.darkGray);
		
		int spacing = 1;
		while (Game.worldToScreenUnits(gridToPixels(spacing)) < 8) {
			spacing *= 5;
		}
		
		//Vertical lines
		int lines = (int) Math.ceil(WIDTH / Game.worldToScreenUnits(gridToPixels(spacing)));
		
		int start = (int) Math.ceil(Game.pixelsToGrid(Game.cam.getLeft()));
		int startLine = (int) Game.cam.getTop();
		int length = (int) (HEIGHT / Game.cam.getZoom());
		
		while (start % spacing != 0) {
			start++;
		}
		
		int count = 0;
		for (int i = start; count < lines; i += spacing) {
			if (i % (spacing * 25) == 0) {
				g.setStroke(new BasicStroke((float) (3 / Game.cam.getZoom())));
			} else if (i % (spacing * 5) == 0) {
				g.setStroke(new BasicStroke((float) (2 / Game.cam.getZoom())));
			} else if (i % spacing == 0) {
				g.setStroke(new BasicStroke((float) (0.5 / Game.cam.getZoom())));
			} else {
				continue;
			}
			int coords = i * PIXELS_PER_GRID;
			g.drawLine(coords, startLine, coords, startLine - length);
			
			count++;
		}
		
		//Horizontal lines
		lines = (int) Math.ceil(HEIGHT / Game.worldToScreenUnits(gridToPixels(spacing)));
		
		start = (int) (Game.pixelsToGrid(Game.cam.getTop()));
		startLine = (int) Game.cam.getLeft();
		length = (int) (WIDTH / Game.cam.getZoom());
		
		while (start % spacing != 0) {
			start--;
		}
		
		count = 0;
		for (int i = start; count < lines; i -= spacing) {
			if (i % (spacing * 25) == 0) {
				g.setStroke(new BasicStroke((float) (3 / Game.cam.getZoom())));
			} else if (i % (spacing * 5) == 0) {
				g.setStroke(new BasicStroke((float) (2 / Game.cam.getZoom())));
			} else if (i % spacing == 0) {
				g.setStroke(new BasicStroke((float) (0.5 / Game.cam.getZoom())));
			} else {
				continue;
			}
			int coords = i * PIXELS_PER_GRID;
			g.drawLine(startLine, coords, startLine + length, coords);
			
			count++;
		}
	}
	
	//TODO: somehow calculate how many lines are needed after transform.
	//Idea: Apply matrix to unit vectors (or the spacing vectors), then calculate how many of them would be needed to pass a circle that encircles the screen.
	public void render(Graphics2D g, Matrix2D matrix) {
		g.setColor(Color.darkGray);
		
		int spacing = 1;
		while (Game.worldToScreenUnits(gridToPixels(spacing)) < 8) {
			spacing *= 5;
		}
		
		//Vertical lines
		int lines = (int) Math.ceil(WIDTH / Game.worldToScreenUnits(gridToPixels(spacing)));
		
		int start = (int) Math.ceil(Game.pixelsToGrid(Game.cam.getLeft()));
		int startLine = (int) Game.cam.getTop();
		int length = (int) (HEIGHT / Game.cam.getZoom());
		
		while (start % spacing != 0) {
			start++;
		}
		
		int count = 0;
		for (int i = start; count < lines; i += spacing) {
			if (i % (spacing * 25) == 0) {
				g.setStroke(new BasicStroke((float) (3 / Game.cam.getZoom())));
			} else if (i % (spacing * 5) == 0) {
				g.setStroke(new BasicStroke((float) (2 / Game.cam.getZoom())));
			} else if (i % spacing == 0) {
				g.setStroke(new BasicStroke((float) (0.5 / Game.cam.getZoom())));
			} else {
				continue;
			}
			int coords = i * PIXELS_PER_GRID;
			Point2D s = matrix.mult(coords, startLine);
			Point2D e = matrix.mult(coords, startLine - length);
			g.drawLine((int) s.x, (int) s.y, (int) e.x, (int) e.y);
			
			count++;
		}
		
		//Horizontal lines
		lines = (int) Math.ceil(HEIGHT / Game.worldToScreenUnits(gridToPixels(spacing)));
		
		start = (int) (Game.pixelsToGrid(Game.cam.getTop()));
		startLine = (int) Game.cam.getLeft();
		length = (int) (WIDTH / Game.cam.getZoom());
		
		while (start % spacing != 0) {
			start--;
		}
		
		count = 0;
		for (int i = start; count < lines; i -= spacing) {
			if (i % (spacing * 25) == 0) {
				g.setStroke(new BasicStroke((float) (3 / Game.cam.getZoom())));
			} else if (i % (spacing * 5) == 0) {
				g.setStroke(new BasicStroke((float) (2 / Game.cam.getZoom())));
			} else if (i % spacing == 0) {
				g.setStroke(new BasicStroke((float) (0.5 / Game.cam.getZoom())));
			} else {
				continue;
			}
			int coords = i * PIXELS_PER_GRID;
			Point2D s = matrix.mult(startLine, coords);
			Point2D e = matrix.mult(startLine + length, coords);
			g.drawLine((int) s.x, (int) s.y, (int) e.x, (int) e.y);
			
			count++;
		}
	}
}
