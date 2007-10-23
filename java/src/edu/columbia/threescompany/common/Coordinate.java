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

	public Coordinate plus(Coordinate c) {
		return new Coordinate(x + c.x, y + c.y);
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
	
	public Coordinate toUnitVector() {
		double len = this.length();
		if (len == 0) return new Coordinate(0, 0);
		
		return new Coordinate(x / len, y / len);
	}

	public Coordinate times(double d) {
		return new Coordinate(x * d, y * d);
	}

	public Coordinate rotate(int deg) {
		double rad = (deg * Math.PI / 180) + Math.atan2(y, x);
		double len = length();
		return new Coordinate(len * Math.cos(rad), len * Math.sin(rad));
	}
	
	public boolean equals(Coordinate rhs) {
		return (x == rhs.x && y == rhs.y);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}