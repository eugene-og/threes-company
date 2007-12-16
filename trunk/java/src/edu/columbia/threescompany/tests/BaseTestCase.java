package edu.columbia.threescompany.tests;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.gameobjects.GameParameters;
import junit.framework.TestCase;

public abstract class BaseTestCase extends TestCase {
	/* At low granularities (e.g., < 100), physics results start getting
	 * super-crappy. So EPSILON depends on the game's internal accuracy. */
	public static final double EPSILON = 5 / GameParameters.GRANULARITY_OF_PHYSICS;
	
	protected void assertNegative(int x) {
		assertNegative((double) x);
	}
	
	protected void assertZero(int x) {
		assertZero((double) x);
	}
	
	protected void assertPositive(int x) {
		assertPositive((double) x);
	}
	
	protected void assertNegative(double x) {
		assertTrue("Value should be negative: " + x, x < 0.0);
	}

	protected void assertZero(double x) {
		assertEquals("Value should be zero: " + x, 0.0, Math.abs(x));
	}

	protected void assertPositive(double x) {
		assertTrue("Value should be positive: " + x, x > 0.0);
	}

	protected void assertRoughlyEqual(String message, double a, double b) {
		/* We have a lot of things that aren't necessarily equal to the tiniest
		 * degree.
		 */
		assertTrue(message + " (values " + a + " and " + b + " should be equal)",
				   Math.abs(a - b) < EPSILON);
	}

	protected void assertEquals(String message, Coordinate expected, Coordinate actual) {
		if (expected == null && actual == null);
		if (expected != null && expected.equals(actual)) return;
		fail("Coordinates should be equal (expected " + expected + ", got " + 
				actual + ")");
	}

	protected void assertSignificantlyGreaterThan(String message, double lhs, double rhs) {
		assertTrue(message + " (" + lhs + " should've been 2 epsilons greater than " + rhs + ")",
				   lhs > rhs + 2*EPSILON);
	}
	
	protected void assertSignificantlyLessThan(String message, double lhs, double rhs) {
		assertTrue(message + " (" + lhs + " should've been 2 epsilons less than " + rhs + ")",
				   lhs < rhs - 2*EPSILON);
	}
}
