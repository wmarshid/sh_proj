package test.graspvis.model;

import static org.junit.Assert.*;
import graspvis.model.BasicGraph;
import graspvis.model.BasicNode;
import graspvis.model.BasicSpring;
import graspvis.model.ElementType;
import graspvis.model.Graph;
import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.Spring;

import org.junit.Before;
import org.junit.Test;

public class BasicGraphTest {

	private BasicGraph classUnderTest;
	private Node from;
	private Node to;
	@Before
	public void setUp() throws Exception {
		classUnderTest = new BasicGraph();
		from = new BasicNode("from");
		from.setPosition(100, 100);
		to = new BasicNode("to");
		to.setPosition(10, 10);
	}

	@Test
	public void testAddSpringStringStringStringRelationshipType() {
		addNodesToGraph();
		classUnderTest.addSpring("spring1", "from", "to", RelationshipType.CONNECTION);
		assertTrue(classUnderTest.getSprings().length + "", classUnderTest.getSprings().length > 0);
	}

	private void addNodesToGraph() {
		classUnderTest.addNode(from);
		classUnderTest.addNode(to);
	}

	@Test
	public void testGetSpringNodeNodeRelationshipType() {
		addNodesToGraph();
		from = classUnderTest.getNode(from.getId());
		to = classUnderTest.getNode(to.getId());
		classUnderTest.addSpring("spring1", from.getId(), to.getId(), RelationshipType.CONNECTION);		
		Spring s = classUnderTest.getSpring(from, to, RelationshipType.CONNECTION);
		assertTrue(s.getId() == "spring1");
	}

	@Test
	public void testSetNodes() {
		Node[] nodes = new BasicNode[2];
		nodes[0] = new BasicNode("1");
		nodes[1] = new BasicNode("2");
		classUnderTest.setNodes(nodes);
		assertTrue(classUnderTest.getNodes().length == 2);
	}
	
	@Test
	public void testSetSprings() {
		addNodesToGraph();
		from = classUnderTest.getNode(from.getId());
		to = classUnderTest.getNode(to.getId());
		Spring s = new BasicSpring(from, to);
		s.setId("s1");
		s.setRelationshipType(RelationshipType.CONTAINMENT);
		Spring[] springs = new Spring[1];
		springs[0] = s;
		classUnderTest.setSprings(springs);
		assertTrue(classUnderTest.getSprings().length == 1);
	}

	@Test
	public void testClear() {
		addNodesToGraph();
		classUnderTest.addSpring("spring1", "from", "to", RelationshipType.CONNECTION);
		assertTrue(classUnderTest.getSprings().length == 1);
		assertTrue(classUnderTest.getNodes().length == 2);
		
		classUnderTest.clear();
		
		assertTrue(classUnderTest.getSprings().length == 0);
		assertTrue(classUnderTest.getNodes().length == 0);
	}


	@Test
	public void testAddSpringStringNodeNodeRelationshipType() {
		addNodesToGraph();
		classUnderTest.addSpring("spring1", from, to, RelationshipType.EXTENSION);
		assertTrue(classUnderTest.getSprings().length == 1);
		assertTrue(classUnderTest.getSprings()[0].getId() == "spring1");
	}

	@Test
	public void testAddSpringStringNodeNodeNodeNodeRelationshipType() {
		addNodesToGraph();
		String id ="springWithHooks";
		classUnderTest.addSpring(id, from, from, to, to, RelationshipType.CONNECTION);
		assertTrue(classUnderTest.getSprings().length == 1);
		assertTrue(classUnderTest.getSprings()[0].getId() == id);
	}

	@Test
	public void testGetNodeString() {
		addNodesToGraph();
		Node n = classUnderTest.getNode(to.getId());
		assertTrue(n.getId() == to.getId());
	}

	@Test
	public void testGetNodeFloatFloat() {
		addNodesToGraph();
		Node n = classUnderTest.getNode(102, 101);
		assertTrue(n.getId() == from.getId());
	}

	@Test
	public void testGetSpringString() {
		addNodesToGraph();
		String id = "spring1";
		classUnderTest.addSpring(id, from, to, RelationshipType.EXTENSION);
		Spring s = classUnderTest.getSpring(id);
		assertTrue(s.getId() == id);
	}

	@Test
	public void testHighlight() {
		addNodesToGraph();
		classUnderTest.highlight((Highlight) from);
		Node n = classUnderTest.getNode(from.getId());
		assertTrue(((Highlight) n).isHighlighted() == true);
	}

	@Test
	public void testUnhighlight() {
		addNodesToGraph();
		classUnderTest.highlight((Highlight) from);
		Node n = classUnderTest.getNode(from.getId());
		assertTrue(((Highlight) n).isHighlighted() == true);
		classUnderTest.unhighlight();
		for (Node node : classUnderTest.getNodes()) {
			Highlight h = (Highlight) node;
			assertTrue(h.isHighlighted() == false);
		}
	}

	@Test
	public void testRemoveNode() {
		addNodesToGraph();
		classUnderTest.removeNode(from.getId());
		assertTrue(classUnderTest.getNodes().length == 1);
	}

	@Test
	public void testRemoveSpring() {
		addNodesToGraph();
		String id ="springWithHooks";
		classUnderTest.addSpring(id, from, from, to, to, RelationshipType.CONNECTION);
		assertTrue(classUnderTest.getSprings().length == 1);
		classUnderTest.removeSpring(id);
		assertTrue(classUnderTest.getSprings().length == 0);
	}

	@Test
	public void testSetSpringLength() {
		addNodesToGraph();
		String id ="springWithHooks";
		classUnderTest.addSpring(id, from, from, to, to, RelationshipType.CONNECTION);
		float l = 15;
		classUnderTest.setSpringLength(RelationshipType.CONNECTION, l);
		for (Spring s : classUnderTest.getSprings()) {
			assertTrue(s.getLength() == l);
		}
	}

	@Test
	public void testAddNodeStringStringElementType() {
		classUnderTest.addNode("id", "label", ElementType.COMPONENT);
		assertTrue(classUnderTest.getNodes().length == 1);
	}

}
