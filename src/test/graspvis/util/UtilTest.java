package test.graspvis.util;

import static org.junit.Assert.*;
import graspvis.util.Util;

import org.junit.Before;
import org.junit.Test;

public class UtilTest {

	
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testRandom() {
		for (int i = 0; i < 100; i++) {
			float random = Util.random(-200, 200);
			assertTrue(random >= -200 && random <= 200);	
		}
	}

	@Test
	public void testGetLabel() {
		String label = "MainLabel";
		assertTrue(Util.getLabel(label).equals("ML"));
	}

}
