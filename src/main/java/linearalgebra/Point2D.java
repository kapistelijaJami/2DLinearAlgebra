package linearalgebra;

import org.ejml.simple.SimpleMatrix;

public class Point2D {
	public double x, y;
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2D copy() {
		return new Point2D(x, y);
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public Point2D add(double add) {
		return add(add, add);
	}
	
	public Point2D add(Point2D o) {
		return add(o.x, o.y);
	}
	
	public Point2D add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Point2D mult(double mult) {
		x *= mult;
		y *= mult;
		return this;
	}
	
	public Point2D rotated(double degrees) {
		double rad = Math.toRadians(degrees);
		SimpleMatrix m = new SimpleMatrix(
				new double[][] {
					{Math.cos(rad), -Math.sin(rad)},
					{Math.sin(rad), Math.cos(rad)}
				});
		
		SimpleMatrix res = m.mult(new SimpleMatrix(new double[][] {{x}, {y}}));
		return new Point2D(res.get(0), res.get(1));
	}
	
	//distance from origo
	public double magnitude() {
		return HelperFunctions.pythagoras(x, y);
	}
	
	public Point2D normalize() {
		double magnitude = magnitude();
		if (magnitude > 0) {
			x /= magnitude;
			y /= magnitude;
		}
		return this;
	}
	
	public Point2D negate() {
		x *= -1;
		y *= -1;
		return this;
	}
	
	public Point2D negated() {
		return copy().negate();
	}
	
	public double dot(Point2D p) {
		return x * p.x + y * p.y;
	}
	
	public Point2D perpendicular(boolean right) {
		Point2D p = new Point2D(y, -x);
		
		return right ? p : p.negate();
	}
	
	/**
	 * Length of the would be 3D cross product (which is perpendicular to both of the vectors).
	 * Also the area of the parallellogram made by the two vectors.
	 * @param p
	 * @return 
	 */
	public double cross(Point2D p) {
		return x * p.y - p.x * y;
	}
}
