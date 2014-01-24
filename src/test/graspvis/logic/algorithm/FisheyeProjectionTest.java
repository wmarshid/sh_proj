package test.graspvis.logic.algorithm;

import static org.junit.Assert.*;
import graspvis.logic.algorithm.FisheyeProjection;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import processing.core.PVector;

public class FisheyeProjectionTest {

	private FisheyeProjection classUnderTest;
	private PVector mockedPoint;
	private Mockery context;
	
	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery(){{
			setImposteriser(ClassImposteriser.INSTANCE);
			setThreadingPolicy(new Synchroniser());
		}};
		
		mockedPoint = context.mock(PVector.class);
		classUnderTest = new FisheyeProjection(mockedPoint, mockedPoint, 20);
	}

	@Test
	public void testRun() {
		context.checking(new Expectations(){{
			oneOf(mockedPoint).x = with(any(float.class));
			oneOf(mockedPoint).y = with(any(float.class));
		}});
		classUnderTest.project(mockedPoint);
	}

}
