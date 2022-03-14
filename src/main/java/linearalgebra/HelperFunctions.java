package linearalgebra;

public class HelperFunctions {
	public static double pythagoras(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}
	
	public static double distance(double startX, double startY, double toX, double toY) {
		return pythagoras(toX - startX, toY - startY);
	}
}
