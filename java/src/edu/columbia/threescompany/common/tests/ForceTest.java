package edu.columbia.threescompany.common.tests;

import edu.columbia.threescompany.common.Force;
import edu.columbia.threescompany.tests.BaseTestCase;

public class ForceTest extends BaseTestCase {
	public void testInverse() {
		Force f = Force.newRawForce(1, -1);
		assertEquals("Inverse shouldn't involve granularity",
					 Force.newRawForce(-1, 1), f.inverse());
		f = Force.newRawForce(1.7, 0.1);
		assertEquals("Inverse shouldn't involve granularity",
				 Force.newRawForce(-1.7, -0.1), f.inverse());
	}
}
