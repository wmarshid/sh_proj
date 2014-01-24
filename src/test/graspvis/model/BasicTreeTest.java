package test.graspvis.model;

import static org.junit.Assert.*;
import graspvis.model.BasicTree;
import graspvis.model.BasicTreeNode;
import graspvis.model.ElementType;
import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.TreeNode;

import org.junit.Before;
import org.junit.Test;

public class BasicTreeTest {

	private BasicTree classUnderTest;
	private TreeNode root;
	private TreeNode child1;
	@Before
	public void setUp() throws Exception {
		root = new BasicTreeNode();
		root.setId("root");
		child1 = new BasicTreeNode("child1", "Child 1", ElementType.COMPONENT);
		child1.setPosition(100, 100);
		classUnderTest = new BasicTree(root);
	}

	@Test
	public void testGetRoot() {
		assertTrue(classUnderTest.getRoot().getId() == root.getId());
	}

	@Test
	public void testGetFloatFloat() {
		addChild();
		assertTrue(classUnderTest.get(102, 103) != null);
	}

	private void addChild() {
		classUnderTest.addChild(root, child1, RelationshipType.CONNECTION);
	}

	@Test
	public void testGetString() {
		addChild();
		assertTrue(classUnderTest.get(child1.getId()).getId() == child1.getId());
	}

	@Test
	public void testGetTreeNode() {
		addChild();
		assertTrue(classUnderTest.get(child1).getId() == child1.getId());
	}

	@Test
	public void testAddChild() {
		addChild();
		assertTrue(classUnderTest.getRoot().getChildCount() == 1);
	}

	@Test
	public void testHighlightTreeNode() {
		addChild();
		classUnderTest.highlight(child1);
		assertTrue(((Highlight) child1).isHighlighted() == true);
	}

	@Test
	public void testHighlightNode() {
		addChild();
		Node n = child1;
		classUnderTest.highlight(n);
		assertTrue(((Highlight) child1).isHighlighted() == true);
	}

	@Test
	public void testClear() {
		addChild();
		classUnderTest.clear();
		assertTrue(classUnderTest.getRoot().getChildCount() == 0);
	}

	@Test
	public void testIsEmpty() {
		classUnderTest = new BasicTree();
		assertTrue(classUnderTest.isEmpty());
	}

	@Test
	public void testUnhighlight() {
		addChild();
		classUnderTest.highlight(child1);
		assertTrue(((Highlight) child1).isHighlighted() == true);
		
		classUnderTest.unhighlight();
		assertTrue(((Highlight) child1).isHighlighted() == false);
	}

}
