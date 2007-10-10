package edu.columbia.threescompany.common.tests;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.tests.BaseTestCase;

public class CoordinateTest extends BaseTestCase {
	public void testDistance() {
		Coordinate base = new Coordinate(0, 0);
		double dist = base.distanceFrom(new Coordinate(3, 4));
		assertEquals("Distance calculation incorrect", 5.0, dist);
		
		dist = base.distanceFrom(new Coordinate(5, 12));
		assertEquals("Distance calculation incorrect", 13.0, dist);
	}
	
	public void testRotation() {
		Coordinate base = new Coordinate(1, 0);
		assertCoordinateEquals("Rotation failed",
							   new Coordinate(0, 1),
							   base.rotate(90));
		assertCoordinateEquals("Rotation failed",
				   			   new Coordinate(-1, 0),
				   			   base.rotate(180));
		assertCoordinateEquals("Rotation failed",
				   			   new Coordinate(0, -1),
				   			   base.rotate(-90));
	}
	
	public void assertCoordinateEquals(String message, Coordinate expected, Coordinate actual) {
		if (Double.toString(actual.x).length() < 5 &&
			Double.toString(actual.y).length() < 5) {
			assertTrue(message + " (expected " + expected + " and got " +
					   actual + ")", expected.equals(actual));
		} else {
			assertRoughlyEqual(message + " (expected x=" + expected.x + ", got " +
					 	actual.x, expected.x, actual.x);
			assertRoughlyEqual(message + " (expected y=" + expected.y + ", got " +
				 		actual.y, expected.y, actual.y);
		}
	}
	
	public void testUnitVector() {
		assertUnitVectorEquals(0, 0, 0, 0);
		assertUnitVectorEquals(0, 1, 0, 1);
		assertUnitVectorEquals(-1, 0, -1, 0);
		assertUnitVectorEquals(1, 1, Math.sqrt(0.5), Math.sqrt(0.5));
		assertUnitVectorEquals(1, -1, Math.sqrt(0.5), -Math.sqrt(0.5));
	}
	
	void assertUnitVectorEquals(double x, double y, double xU, double yU) {
		Coordinate vec = new Coordinate(x, y).toUnitVector();
		
		assertCoordinateEquals("Unit vector incorrect",
							   new Coordinate(xU, yU), vec);
	}
}
