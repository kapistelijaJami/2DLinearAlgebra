package linearalgebra;

/**
 * 
 * Matrix2D class.
 * Has a 2 by 2 matrix, with added third column for a translation. (Making it an AffineTransform)
 * 
 * Matrix and vector multiplication:
 * 
 * <pre>
 * [ a  c  e ]   [ x ]   [ a * x + c * y + e ]
 * [ b  d  f ] * [ y ] = [ b * x + d * y + f ]
 *               [ 1 ]
 * </pre>
 * The 1 is automatically there for the translate in this example. This is 2d vector multiplication.
 * 
 * Matrix and matrix multiplication:
 * 
 * <pre>
 * [ A  C  E ]   [ a  c  e ]   [ A * a + C * b    A * c + C * d    A * e + C * f + E ]
 * [ B  D  F ] * [ b  d  f ] = [ B * a + D * b    B * c + D * d    B * e + D * f + F ]
 *               [ 0  0  1 ]   
 * </pre>
 * The 0, 0, 1 row is automatically there to make the calculation work in this example.
 * Right matrix is the first to apply (current matrix, small letters), the left one is the latter one (given as parameter, shown in big letters).
 * 
 * <pre>
 * a is the scale of x direction. (How will the x axis get scaled)
 * b is the shear of up and down. (Everything right side of origo will get more positive y with positive b value)
 * c is the shear of left and right. (Everything above of origo will get more positive x with positive c value)
 * d is the scale of y direction. (How will the y axis get scaled)
 * e is just the translation in x direction. Defaulting to 0.
 * f is just the translation in y direction. Defaulting to 0.
 * 
 * If you just set the e and f to translate values, it will rotate before translating.
 * If you need translation before rotation, the e will be (a, c).dot(e, f), and the f will be (b, d).dot(e, f).
 * Then you would need to update the e and f every time the matrix changes at all with the same dot products,
 * but you would need the original e and f in the calculation, so you would need to keep track of them separately or take the inverse:
 * e = (a, b).dot(e, f), and f = (c, d).dot(e, f). (Here input e and f are the old already rotated values, and the result e and f are the translate values)
 * (The inverse of a rotation matrix is its transpose)
 * </pre>
 */
public class Matrix2D {
	//Order:
	// [ a  c  e ]
	// [ b  d  f ]
	private double a, b, c, d, e, f;
	
	public Matrix2D(double a, double b, double c, double d) {
		this(a, b, c, d, 0, 0);
	}
	
	public Matrix2D(double a, double b, double c, double d, double e, double f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}
	
	/**
	 * Copies the values of the parameter matrix to this one.
	 * @param m
	 * @return 
	 */
	public Matrix2D match(Matrix2D m) {
		this.a = m.a;
		this.b = m.b;
		this.c = m.c;
		this.d = m.d;
		this.e = m.e;
		this.f = m.f;
		return this;
	}
	
	public static Matrix2D identity() {
		return new Matrix2D(1, 0, 0, 1);
	}
	
	public static Matrix2D rotation(double degrees) {
		double rad = Math.toRadians(degrees);
		return new Matrix2D(Math.cos(rad), Math.sin(rad), -Math.sin(rad), Math.cos(rad));
	}
	
	public static Matrix2D scaleMatrix(Point2D p) {
		Matrix2D m = identity();
		return m.scale(p);
	}
	
	public static Matrix2D shearMatrix(Point2D p) {
		Matrix2D m = identity();
		return m.shear(p);
	}
	
	public static Matrix2D translation(Point2D p) {
		Matrix2D m = identity();
		return m.translate(p);
	}
	
	public Matrix2D rotate(double degrees) {
		return this.mult(rotation(degrees));
	}
	
	public Matrix2D scale(Point2D p) {
		a *= p.x;
		d *= p.y;
		return this;
	}
	
	public Matrix2D shear(Point2D p) {
		b += p.y;
		c += p.x;
		return this;
	}
	
	public Matrix2D translate(Point2D p) {
		e += p.x;
		f += p.y;
		return this;
	}
	
	public Matrix2D mult(double scale) {
		return new Matrix2D(a, b, c, d, e, f).multA(scale);
	}
	
	/**
	 * Multiply by scalar. Apply to this matrix.
	 * @param scale
	 * @return 
	 */
	public Matrix2D multA(double scale) {
		a *= scale;
		b *= scale;
		c *= scale;
		d *= scale;
		e *= scale;
		f *= scale;
		return this;
	}
	
	public Point2D mult(Point2D p) {
		return mult(p.x, p.y);
	}
	
	public Point2D mult(double x, double y) {
		return new Point2D(a * x + c * y + e, b * x + d * y + f);
	}
	
	/**
	 * Matrix matrix multiplication.
	 * See the class Javadoc for explanation.
	 * @param m
	 * @return 
	 */
	public Matrix2D mult(Matrix2D m) {
		return new Matrix2D(m.a * a + m.c * b,  m.b * a + m.d * b,    m.a * c + m.c * d,  m.b * c + m.d * d,    m.a * e + m.c * f + m.e,  m.b * e + m.d * f + m.f);
	}
	
	@Override
	public String toString() {
		String s = "[ " + getPrintableVal(a, 0) + "  " + getPrintableVal(c, 1) + "  " + getPrintableVal(e, 2) + " ]\n";
		s		+= "[ " + getPrintableVal(b, 0) + "  " + getPrintableVal(d, 1) + "  " + getPrintableVal(f, 2) + " ]";
		
		return s;
	}
	
	private String getPrintableVal(double val, int col) {
		int w = getColumnMaxWidth(col);
		return spaces(w - width(val)) + val;
	}
	
	private String spaces(int amount) {
		String s = "";
		for (int i = 0; i < amount; i++) {
			s += " ";
		}
		return s;
	}
	
	private int width(double d) {
		return ("" + d).length();
	}
	
	private int getColumnMaxWidth(int i) {
		switch (i) {
			case 0:
				return Math.max(width(a), width(b));
			case 1:
				return Math.max(width(c), width(d));
			case 2:
				return Math.max(width(e), width(f));
		}
		
		return 0;
	}
}
