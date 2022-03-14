package linearalgebra;

public class HelperFunctions {
	public static double pythagoras(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}
	
	public static double distance(double startX, double startY, double toX, double toY) {
		return pythagoras(toX - startX, toY - startY);
	}
	
	public static double lerp(double t, double a, double b) {
		return (1 - t) * a + t * b;
	}
}
