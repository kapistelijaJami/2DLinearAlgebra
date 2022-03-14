package linearalgebra;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import static linearalgebra.Constants.LINES;
import static linearalgebra.Constants.SPACING;

public class Game extends Canvas implements Runnable {
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static final double FPS = 120.0;
	private boolean running = true;
	private Window window;
	
	private Camera cam = new Camera(0, 0);
	private double angle = 0;
	private double angle2 = 0;
	
	private void init() {
		window = new Window(WIDTH, HEIGHT, "2D Linear algebra", this, 0, false);
		
		KeyInput input = new KeyInput(this);
		
		this.addMouseListener(input);
		this.addMouseMotionListener(input);
		this.addMouseWheelListener(input);
		this.addKeyListener(input);
	}
	
	public synchronized void stop() {
		running = false;
		System.exit(0);
	}
	
	public void windowLostFocus() { }
	
	@Override
	public void run() {
		init();
		
		this.requestFocus();
		
		long now = System.nanoTime();
		long nsBetweenFrames = (long) (1e9 / FPS);
		
		long time = System.currentTimeMillis();
		int frames = 0;
		
		while (running) {
			if (now + nsBetweenFrames <= System.nanoTime()) {
				now += nsBetweenFrames;
				update();
				render();
				frames++;
				
				if (time + 250 < System.currentTimeMillis()) {
					time += 250;
					window.setTitle("2D Linear algebra " + (frames * 4) + " fps");
					frames = 0;
				}
			}
		}
		window.close();
	}
	
	private void update() {
		angle--;
		if (angle < 0) {
			angle += 360;
			angle2 -= 360 / 60.0;
			if (angle2 < 0) {
				angle2 += 360;
			}
		}
	}
	
	private void render() {
		Graphics2D g = getGraphics2D();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		
		AffineTransform oldAT = g.getTransform();
		AffineTransform tform = AffineTransform.getTranslateInstance(0, HEIGHT);
		tform.scale(1, -1);
		g.setTransform(tform);
		
		scaleGraphics(g);
		g.translate(-cam.getLeft(), -cam.getTop()); //anything outside translate is rendered relative to screen, anything inside is rendered relative to camera
		
		g.setClip((int) (cam.getLeft() - 1), (int) cam.getTop() - 1, (int) cam.getWidth() + 2, (int) cam.getHeight() + 2);
		
		renderGrid(g);
		renderAxis(g);
		
		drawLine(g, new Point2D(0, 6).rotated(angle));
		drawLine(g, new Point2D(0, 5).rotated(HelperFunctions.lerp((360 - angle) / 360.0, angle2, angle2 - (360 / 60.0))));
		
		/*Point2D naats = convertScreenToWorld(new Point2D(WIDTH - 30, HEIGHT)); //getting world coordinates from screen coordinates inside translate block.
		g.fillOval((int) naats.x, (int) naats.y, (int) screenToWorldUnits(30), (int) screenToWorldUnits(30));*/
		
		
		//cam.render(g);
		
		g.setTransform(oldAT);
		
		//g.fillOval(WIDTH - 50, (int) convertWorldToScreen(new Point2D(0, 0)).y - 15, 30, 30);
		
		
		g.setColor(Color.white);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		Point2D origo = convertWorldToScreen(new Point2D(0, 0));
		g.drawString("X", WIDTH - 30, (float) origo.y - 10);
		g.drawString("Y", (float) origo.x + 5, 30);
		g.drawString("O", (float) origo.x + 5, (float) origo.y - 5);
		
		g.dispose();
		this.getBufferStrategy().show();
	}
	
	private void scaleGraphics(Graphics2D g) {
		g.scale(cam.getZoom(), cam.getZoom()); //TODO: see if this is ok, y is flipped to get y growing up
	}
	
	private void drawLine(Graphics2D g, Point2D end) {
		drawLine(g, new Point2D(0, 0), end);
	}
	
	private void drawLine(Graphics2D g, Point2D start, Point2D end) {
		g.setStroke(new BasicStroke((float) (2 / cam.getZoom())));
		g.setColor(Color.red);
		g.drawLine((int) toGridUnits(start.x), (int) toGridUnits(start.y), (int) toGridUnits(end.x), (int) toGridUnits(end.y));
	}
	
	private void renderAxis(Graphics2D g) {
		int length = LINES * SPACING;
		
		g.setStroke(new BasicStroke((float) (3 / cam.getZoom())));
		g.setColor(Color.white);
		g.drawLine(0, -length, 0, length);
		g.drawLine(-length, 0, length, 0);
		
		/*g.setColor(Color.white);
		renderText(g, "X", cam.getLeft() + cam.getWidth() - screenToWorldUnits(30), screenToWorldUnits(10), 30);
		renderText(g, "Y", screenToWorldUnits(5), cam.getBottom() - screenToWorldUnits(30), 30);
		renderText(g, "O", screenToWorldUnits(5), screenToWorldUnits(5), 30);*/
	}
	
	private void renderGrid(Graphics2D g) {
		int length = LINES * SPACING;
		
		g.setColor(Color.gray);
		
		for (int i = 1; i < LINES; i++) {
			
			if (cam.getZoom() <= 0.10) {
				if (i % 25 == 0) {
					g.setStroke(new BasicStroke((float) (2 / cam.getZoom())));
				} else if (i % 5 == 0) {
					g.setStroke(new BasicStroke((float) (0.5 / cam.getZoom())));
				} else {
					continue;
				}
			} else {
				if (i % 5 == 0) {
					g.setStroke(new BasicStroke((float) (2 / cam.getZoom())));
				} else {
					g.setStroke(new BasicStroke((float) (0.5 / cam.getZoom())));
				}
			}
			
			
			int coords = i * SPACING;
			g.drawLine(coords, 0, coords, length);
			g.drawLine(0, coords, length, coords);
			
			g.drawLine(-coords, 0, -coords, length);
			g.drawLine(0, -coords, length, -coords);
			
			g.drawLine(coords, 0, coords, -length);
			g.drawLine(0, coords, -length, coords);
			
			g.drawLine(-coords, 0, -coords, -length);
			g.drawLine(0, -coords, -length, -coords);
		}
	}
	
	private void renderText(Graphics2D g, String s, double x, double y, double size) {
		AffineTransform oldAT = g.getTransform();
		g.scale(1, -1);
		g.setFont(new Font("TimesRoman", Font.PLAIN, Math.max(1, (int) screenToWorldUnits(size))));
		g.drawString(s, (float) x, (float) -y);
		g.setTransform(oldAT);
	}
	
	/**
	 * Converts screen pixels to world units.
	 * @param val
	 * @return 
	 */
	private double screenToWorldUnits(double val) {
		return val / cam.getZoom();
	}
	
	//These do completely different thing than the one above
	private Point2D convertScreenToWorld(Point2D p) {
		return new Point2D(cam.getX() - (-p.x + WIDTH/2) / cam.getZoom(), cam.getY() - (p.y - HEIGHT/2) / cam.getZoom());
	}
	
	private Point2D convertWorldToScreen(Point2D p) {
		return new Point2D((p.x - cam.getX()) * cam.getZoom() + WIDTH/2, (p.y + cam.getY()) * cam.getZoom() + HEIGHT/2);
	}
	
	private double toGridUnits(double val) {
		return val * SPACING;
	}
	
	private Graphics2D getGraphics2D() {
		BufferStrategy bs = this.getBufferStrategy();
		while (bs == null) {
			createBufferStrategy(3);
			bs = this.getBufferStrategy();
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		setGraphicsRenderingHints(g);
		
		return g;
	}
	
	private static void setGraphicsRenderingHints(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); //for image scaling to look sharp, might not be good for all images
	}
	
	public Camera getCamera() {
		return cam;
	}
}
