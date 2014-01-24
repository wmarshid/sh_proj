package graspvis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.core.PApplet;

import graspvis.util.Util;

/**
 * Graph containing nodes and springs. Force-directed functionalities adopted from 
 * {@link http://www.generative-gestaltung.de/code} by Bohnacker, et. al (2012).
 * @author nauval
 *
 */
public class BasicGraph implements Graph{

	private static final int MAX_VALUE = 100;
	private static final int MIN_VALUE = -MAX_VALUE;
	private static final int MIDDLE_POINT = 400;
	
	private Map<String, Node> nodes;
	private List<Spring> springs;
	
	public BasicGraph() {
		nodes = new HashMap<String, Node>();
		springs = new ArrayList<Spring>();
	}
	
	/**
	 * 
	 * @param spring id with format of fromId_toId_relationshipType.
	 * @param fromId
	 * @param toId
	 * @param type
	 */
	public void addSpring(String id, String fromId, String toId, RelationshipType type) {
		Node fromNode = nodes.get(fromId);
		Node toNode = nodes.get(toId);
		
		if (fromNode == null || toNode == null) {
			return;
		}
		
		if (getSpring(fromNode, toNode, type) == null) {
			Spring spring = new BasicSpring(fromNode, toNode);
			spring.setId(id);
			spring.setRelationshipType(type);
			
			// add spring default length, spring stiffness and spring damping
			spring.setLength(type.getLength());
			spring.setStiffness(type.getStiffness());
			springs.add(spring);
		}
	}
	
	public Spring getSpring(Node fromNode, Node toNode, RelationshipType type) {
		for (Spring spring : springs) {
			if (spring.getFromNode() == fromNode 
					&& spring.getToNode() == toNode 
					&& spring.getRelationshipType() == type) {
				return spring;
			}
		}
		return null;
	}
	
	@Override
	public void setNodes(Node[] nodes) {
		this.nodes.clear();
		for (Node node : nodes) {
			addNode(node);
		}
	}

	@Override
	public Node[] getNodes() {
		return nodes.values().toArray(new Node[0]);
	}

	@Override
	public void setSprings(Spring[] springs) {
		for (Spring spring : springs) {
			addSpring(spring.getId(), spring.getFromNode(), spring.getToNode(), spring.getRelationshipType());
		}
	}

	@Override
	public Spring[] getSprings() {
		return springs.toArray(new Spring[0]);
	}

	@Override
	public void clear() {
		nodes.clear();
		springs.clear();
	}

	@Override
	public void addNode(Node node) {
		// create a new node and add it to the map.
		if (!nodes.containsKey(node.getId())) {
			//float x, y;
//			x = Util.random(MIDDLE_POINT, MIN_VALUE, MAX_VALUE);
//			y = Util.random(MIDDLE_POINT, MIN_VALUE, MAX_VALUE);
			Node n = new BasicNode(node.getId(), node.getPosition().x, node.getPosition().y);
			n.setLabel(node.getLabel());
			n.setElementType(node.getElementType());
			n.setRadius(node.getElementType().getSize());
			n.setShapeColor(node.getShapeColor());
			n.setLabelColor(node.getLabelColor());
			if (((Highlight) node).isHighlighted())
				((Highlight) n).highlight();
			nodes.put(node.getId(), n);
		}	
	}

	@Override
	public void addSpring(String id, Node from, Node to, RelationshipType type) {
		addSpring(id, from.getId(), to.getId(), type);
	}

	@Override
	public void addSpring(String id, Node from, Node hookFrom, Node to,
			Node hookTo, RelationshipType type) {
		addSpring(id, from, to, type);
		
		Node hFrom = nodes.get(hookFrom.getId()) == null? from : nodes.get(hookFrom.getId());
		Node hTo = nodes.get(hookTo.getId()) == null? to : nodes.get(hookTo.getId());
		
		Spring s = getSpring(id);
		if (s != null) {
			s.setHookFrom(hFrom);
			s.setHookTo(hTo);	
		}
	}

	@Override
	public Node getNode(String id) {
		return nodes.get(id);
	}

	@Override
	public Node getNode(float x, float y) {
		float maxDist = 20;
		Node n = null;
		for (int i = 0; i < getNodes().length; i++) {
			Node c = getNodes()[i];
			float d = PApplet.dist(x, y, c.getPosition().x, c.getPosition().y);
			if (d < maxDist) {
				n = c;
				maxDist = d;
			}
		}
		return n;
	}

	@Override
	public Spring getSpring(String id) {
		Spring s = null;
		for (Spring spring : springs) {
			if (spring.getId().equals(id)) {
				s = spring;
			}
		}
		return s;
	}

	@Override
	public void highlight(Highlight node) {
		for (Node n : getNodes()) {
			Highlight h = (Highlight) n;
			if (h.getId().equals(node.getId())) {
				h.highlight();
			} else {
				h.unhighlight();
			}
		}	
	}

	@Override
	public void unhighlight() {
		for (Node n : getNodes()) {
			Highlight h = (Highlight) n;
			h.unhighlight();
		}
	}

	@Override
	public void removeNode(String id) {
		Node node = nodes.get(id);
		for (Spring spring : getSprings()) {
			if (spring.getFromNode() == node || spring.getToNode() == node) {
				springs.remove(spring);
			}
		}
		nodes.remove(id);
	}

	@Override
	public void removeSpring(String id) {
		for (Spring spring : getSprings()) {
			if (spring.getId().equals(id)) {
				springs.remove(spring);
				break;
			}
		}
	}

	@Override
	public void setSpringLength(RelationshipType type, float length) {
		for (Spring s : springs) {
			if (s.getRelationshipType() == type) {
				s.setLength(length);
			}
		}
	}

	@Override
	public void addNode(String id, String label, ElementType type)  {
		// create a new node and add it to the map.
		if (!nodes.containsKey(id)) {
			float x, y;
			x = Util.random(MIDDLE_POINT, MIN_VALUE, MAX_VALUE);
			y = Util.random(MIDDLE_POINT, MIN_VALUE, MAX_VALUE);
			Node node = new BasicNode(id, x, y);
			node.setLabel(label);
			node.setElementType(type);
			node.setRadius(type.getSize());
			nodes.put(id, node);
		}
	}

}
