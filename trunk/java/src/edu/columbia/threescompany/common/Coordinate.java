package edu.columbia.threescompany.common;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Coordinate implements Serializable {
	private static final long serialVersionUID = -9086984043560982108L;

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
		double rad = (deg * Math.PI / 180) + theta();
		double len = length();
		return new Coordinate(len * Math.cos(rad), len * Math.sin(rad));
	}

	public double theta() {
		return Math.atan2(y, x);
	}
	
	public boolean equals(Coordinate rhs) {
		return (x == rhs.x && y == rhs.y);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public String toRoundedString() {
		DecimalFormat fmt = new DecimalFormat();
		fmt.setMaximumFractionDigits(2);
		fmt.setMinimumFractionDigits(2);
		return "(" + fmt.format(x) + ", " + fmt.format(y) + ")";
	}
}
