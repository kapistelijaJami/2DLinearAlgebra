package linearalgebra;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window {
	private JFrame frame;

	public Window(int width, int height, String title, Game game, int screen, boolean fullscreen) {
		frame = new JFrame(title);
		
		frame.setPreferredSize(new Dimension(width, height));	//laitetaan ikkunan koko
		frame.setMinimumSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		
		//FULLSCREEN STUFF
		if (fullscreen) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		}
		
		frame.addWindowListener(new WindowAdapter() { //ikkuna sulkeutuu rastista ja pystyy kutsua myös metodia
			@Override
			public void windowClosing(WindowEvent e) {
				game.stop();
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				//minimized
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				//back from minimized
			}
		});
		
		frame.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {}
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				game.windowLostFocus();
			}
		});
		
		//frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//ikkuna sulkeutuu rastista
		frame.setResizable(false);								//ikkunaa ei voida venyttää
		frame.setLocationRelativeTo(null);						//ikkuna syntyy näytön keskelle
		frame.add(game);										//lisätään peli ikkunaan
		frame.pack();											//tehdään JFrame Windowista halutun kokoinen
		frame.setVisible(true);									//laitetaan ikkuna näkyväksi
		
		Game.WIDTH = width;
		Game.HEIGHT = height;
		
		Insets inset = frame.getInsets();
		Game.WIDTH -= inset.left + inset.right;
		Game.HEIGHT -= inset.top + inset.bottom;
		
		setScreen(frame, screen);
	}
	
	public void convertFromScreenToFrame(Point p) {
		SwingUtilities.convertPointFromScreen(p, frame);
		Insets inset = frame.getInsets();
		p.x -= inset.left;
		p.y -= inset.top;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public void setTitle(String string) {
		frame.setTitle(string);
	}
	
	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	public static void setScreen(JFrame frame, int screen) {
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		
		GraphicsDevice monitor;
		
		if (screen >= 0 && screen < devices.length) {
			monitor = devices[screen];
		} else if (devices.length > 0) {
			monitor = devices[0];
		} else {
			throw new RuntimeException("No Screens Found");
		}
		
		Rectangle gcBounds = monitor.getDefaultConfiguration().getBounds();
		
		Dimension windowSize = frame.getSize();
		Point centerPoint = new Point((int) gcBounds.getCenterX(), (int) gcBounds.getCenterY());
		int dx = centerPoint.x - windowSize.width / 2;
		int dy = centerPoint.y - windowSize.height / 2;
		
		
		// Avoid being placed off the edge of the screen:
		if (dy + windowSize.height > gcBounds.y + gcBounds.height) { //bottom
			dy = gcBounds.y + gcBounds.height - windowSize.height;
		}
		
		if (dy < gcBounds.y) { //top
			dy = gcBounds.y;
		}
		
		if (dx + windowSize.width > gcBounds.x + gcBounds.width) { //right
			dx = gcBounds.x + gcBounds.width - windowSize.width;
		}
		
		if (dx < gcBounds.x) { //left
			dx = gcBounds.x;
		}
		
		frame.setLocation(dx, dy);
	}
	
	public void setFullscreen(boolean fullscreen) {
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fullscreen ? frame : null);
		
		/*Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		Game.WIDTH = rect.width;
		Game.HEIGHT = rect.height; //these didnt work as expected
		
		Insets inset = frame.getInsets(); 
		Game.WIDTH -= inset.left + inset.right;
		Game.HEIGHT -= inset.top + inset.bottom;*/
		
	}
}
