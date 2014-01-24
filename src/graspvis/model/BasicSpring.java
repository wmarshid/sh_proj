package graspvis.model;

import processing.core.PVector;

/**
 * Drawable spring. Force-directed functionalities adopted from 
 * {@link http://www.generative-gestaltung.de/code} by Bohnacker, et. al (2012).
 * @author nauval
 *
 */
public class BasicSpring implements Spring, ForceDirectedSpring {

	private String id;
	private String label;
	private RelationshipType relationshipType;
	private Node fromNode;
	private Node hookFrom;
	
	private Node toNode;
	private Node hookTo;
	
	private float length;
	private float stiffness;
	private float damping = 0.9f;
	
	//private boolean visible = false;
	
	public BasicSpring(Node fromNode, Node toNode) {
		this.fromNode = fromNode;
		this.toNode = toNode;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setRelationshipType(RelationshipType type) {
		this.relationshipType = type;
	}

	@Override
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

	@Override
	public void setFromNode(Node node) {
		fromNode = node;
	}

	@Override
	public Node getFromNode() {
		return fromNode;
	}

	@Override
	public void setHookFrom(Node hookFrom) {
		this.hookFrom = hookFrom;
	}

	@Override
	public Node getHookFrom() {
		return hookFrom;
	}

	@Override
	public void setToNode(Node node) {
		toNode = node;
	}

	@Override
	public Node getToNode() {
		return toNode;
	}

	@Override
	public void setHookTo(Node hookTo) {
		this.hookTo = hookTo;
	}

	@Override
	public Node getHookTo() {
		return hookTo;
	}

	@Override
	public void setLength(float length) {
		this.length = length;
	}

	@Override
	public float getLength() {
		return length;
	}

	@Override
	public void setStiffness(float stiffness) {
		this.stiffness = stiffness;
	}

	@Override
	public float getStiffness() {
		return stiffness;
	}

	@Override
	public void setDamping(float damping) {
		this.damping = damping;
	}

	@Override
	public float getDamping() {
		return damping;
	}


	@Override
	public void update() {
		if (this.relationshipType == RelationshipType.INTERFACE) 
			return;
		
		PVector diff = PVector.sub(toNode.getPosition(), fromNode.getPosition());
		diff.normalize();
		diff.mult(length);
		PVector target = PVector.add(fromNode.getPosition(), diff);

		PVector force = PVector.sub(target, toNode.getPosition());
		force.mult(0.5f);
		force.mult(stiffness);
		force.mult(1 - damping);

		((ForceDirectedNode) toNode).getVelocity().add(force);
		((ForceDirectedNode) fromNode).getVelocity().add(PVector.mult(force, -1));
	}
}
