package linearalgebra;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static final double FPS = 120.0;
	private boolean running = true;
	private Window window;
	
	private Camera cam = new Camera(0, 0);
	
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
		
	}
	
	private void render() {
		Graphics2D g = getGraphics2D();
		
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		scaleGraphics(g);
		g.translate(-cam.getTopLeftCornerX(), -cam.getTopLeftCornerY()); //anything outside translate is rendered relative to screen, anything inside is rendered relative to camera
		
		
		
		renderGrid(g);
		
		g.setColor(Color.red);
		
		g.drawLine(400, 0, 400, 2000);
		cam.render(g);
		
		
		g.translate(cam.getTopLeftCornerX(), cam.getTopLeftCornerY());
		unscaleGraphics(g);
		
		
		g.dispose();
		this.getBufferStrategy().show();
	}
	
	private void scaleGraphics(Graphics2D g) {
		g.scale(cam.getZoom(), cam.getZoom());
	}
	
	private void unscaleGraphics(Graphics2D g) {
		g.scale(-cam.getZoom(), -cam.getZoom());
	}
	
	private void renderGrid(Graphics2D g) {
		int spacing = 10;
		
		int dist = 1000;
		
		g.setStroke(new BasicStroke((float) (2 / cam.getZoom())));
		g.setColor(Color.white);
		g.drawLine(0, -dist, 0, dist);
		g.drawLine(-dist, 0, dist, 0);
		
		g.setColor(Color.gray);
		
		for (int i = 1; i < 100; i++) {
			if (i % 2 == 0) {
				g.setStroke(new BasicStroke((float) (1 / cam.getZoom())));
			} else {
				g.setStroke(new BasicStroke((float) (0.5 / cam.getZoom())));
			}
			
			int coords = i * spacing;
			g.drawLine(coords, 0, coords, dist);
			g.drawLine(0, coords, dist, coords);
			
			g.drawLine(-coords, 0, -coords, dist);
			g.drawLine(0, -coords, dist, -coords);
			
			g.drawLine(coords, 0, coords, -dist);
			g.drawLine(0, coords, -dist, coords);
			
			g.drawLine(-coords, 0, -coords, -dist);
			g.drawLine(0, -coords, -dist, -coords);
		}
		
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
