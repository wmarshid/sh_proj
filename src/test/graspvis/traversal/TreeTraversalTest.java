package test.graspvis.traversal;

import static org.junit.Assert.*;
import graspvis.exception.AlgorithmException;
import graspvis.model.Graph;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.Tree;
import graspvis.model.TreeNode;
import graspvis.traversal.TreeTraversal;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversalTest {

	private TreeTraversal classUnderTest;
	private Tree mockedTree;
	private Graph mockedGraph;
	private Node mockedNode;
	private TreeNode mockedTreeNode;
	private Mockery context;
	
	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery();
		
		mockedTree = context.mock(Tree.class);
		mockedGraph = context.mock(Graph.class);
		mockedNode = context.mock(Node.class);
		mockedTreeNode = context.mock(TreeNode.class);
		
		classUnderTest = new TreeTraversal(mockedGraph);
	}

	@Test
	public void testTraverseNodeInt() throws AlgorithmException {
		context.checking(new Expectations(){{
			allowing(mockedTree).getRoot(); will(returnValue(mockedTreeNode));
			ignoring(mockedNode);
			ignoring(mockedTreeNode);
			allowing(mockedGraph).getNode(with(any(String.class)));
			allowing(mockedGraph).getSprings();
			allowing(mockedGraph).getNodes();
			allowing(mockedTree).addChild(with(any(TreeNode.class)), 
					with(any(TreeNode.class)), 
					with(any(RelationshipType.class)));
		}});
		classUnderTest.traverse(mockedNode, 2);
	}

	
	@Test(expected=AlgorithmException.class)
	public void testTraverseAlgorithmException() throws AlgorithmException {
		classUnderTest.setGraph(null);
		context.checking(new Expectations(){{
			allowing(mockedTree).getRoot(); will(returnValue(mockedTreeNode));
		}});
		classUnderTest.traverse(mockedNode, 2);
	}
}
