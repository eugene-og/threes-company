package edu.columbia.threescompany.common;

public class Coordinate {
	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double x;
	public double y;
	
	public Coordinate copy() {
		return new Coordinate(this.x, this.y);
	}
	
	public Coordinate minus(Coordinate c) {
		return new Coordinate(x - c.x, y - c.y);
	}
	
	/* Return length (as if this were a vector) */
	public double length() {
		return Math.sqrt(x * x + y * y);
	}
	
	public double distanceFrom(Coordinate c) {
		return (minus(c).length());
	}
}
