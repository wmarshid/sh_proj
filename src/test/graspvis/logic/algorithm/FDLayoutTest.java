package test.graspvis.logic.algorithm;

import static org.junit.Assert.*;
import graspvis.logic.algorithm.FDLayout;
import graspvis.model.Graph;
import graspvis.model.Node;
import graspvis.model.Spring;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

public class FDLayoutTest {

	private FDLayout classUnderTest;
	private Graph mockedGraph;
	private Mockery context;
	
	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery();
		
		mockedGraph = context.mock(Graph.class);
		classUnderTest = new FDLayout(mockedGraph);
	}

	@Test
	public void testRun() {
		context.checking(new Expectations(){{
			atLeast(2).of(mockedGraph).getSprings();
			will(returnValue(new Spring[0]));
			
			atLeast(2).of(mockedGraph).getNodes();
			will(returnValue(new Node[0]));
		}});
		classUnderTest.run();
	}

}
