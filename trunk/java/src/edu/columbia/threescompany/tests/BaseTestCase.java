package edu.columbia.threescompany.tests;

import junit.framework.TestCase;

public abstract class BaseTestCase extends TestCase {
	public static final double EPSILON = .0001;
	
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
		assertTrue(message + " (values should be equal)",
				   Math.abs(a - b) < EPSILON);
	}

}
