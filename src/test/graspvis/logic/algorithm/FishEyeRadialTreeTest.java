package test.graspvis.logic.algorithm;

import static org.junit.Assert.*;
import graspvis.exception.AlgorithmException;
import graspvis.logic.algorithm.FishEyeRadialTree;
import graspvis.model.RelationshipType;
import graspvis.model.Tree;
import graspvis.model.TreeNode;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import processing.core.PVector;

public class FishEyeRadialTreeTest {

	private FishEyeRadialTree classUnderTest;
	private Tree mockedTree;
	private PVector mockedVector;
	private Mockery context;
	
	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery(){{
			setImposteriser(ClassImposteriser.INSTANCE);
		}};
		mockedTree = context.mock(Tree.class);
		mockedVector = context.mock(PVector.class);
		classUnderTest = new FishEyeRadialTree();
	}

	@Test
	public void testRun() throws AlgorithmException {
		classUnderTest.setTree(mockedTree);
		classUnderTest.setDiskCentre(mockedVector);
		classUnderTest.setDiskRadius(90);
		context.checking(new Expectations(){{
			allowing(mockedTree).getRoot();
			allowing(mockedTree).addChild(with(any(TreeNode.class)), 
					with(any(TreeNode.class)), with(any(RelationshipType.class)));
		}});
		classUnderTest.run();
	}
	
	@Test(expected=AlgorithmException.class)
	public void testRunException() throws AlgorithmException {
		classUnderTest.setTree(mockedTree);
		context.checking(new Expectations(){{
			allowing(mockedTree).getRoot();
			allowing(mockedTree).addChild(with(any(TreeNode.class)), 
					with(any(TreeNode.class)), with(any(RelationshipType.class)));
		}});
		classUnderTest.run();
	}
}
