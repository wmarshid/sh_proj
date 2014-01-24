package test.graspvis.traversal;

import grasp.lang.IArchitecture;
import graspvis.grasp.Executor;
import graspvis.model.Graph;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.traversal.GraphTraversal;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

public class GraphTraversalTest {

	private GraphTraversal classUnderTest;
	private Mockery context;
	private Graph mockedGraph;
	private IArchitecture architecture;
	
	private Executor executor;
	
	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery();
		executor = new Executor();
		architecture = executor.execute("bin/data/wsn_simulator.grasp");
		mockedGraph = context.mock(Graph.class);
		classUnderTest = new GraphTraversal();
	}

	@Test
	public void testTraverse() {
		
		context.checking(new Expectations(){{
			allowing(mockedGraph).addNode(with(any(Node.class)));
			allowing(mockedGraph).addSpring(with(any(String.class)), 
					with(any(Node.class)), with(any(Node.class)), 
					with(any(RelationshipType.class)));
			allowing(mockedGraph).addSpring(with(any(String.class)), 
					with(any(Node.class)), 
					with(any(Node.class)), 
					with(any(Node.class)), 
					with(any(Node.class)), 
					with(any(RelationshipType.class)));
			allowing(mockedGraph).getNode(with(any(String.class)));
		}});
		classUnderTest.setArchitecture(architecture);
		classUnderTest.setGraph(mockedGraph);
		classUnderTest.traverse();
	}
}
