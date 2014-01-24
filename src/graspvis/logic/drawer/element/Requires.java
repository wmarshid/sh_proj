package graspvis.logic.drawer.element;

import graspvis.model.Node;
import graspvis.model.TreeNode;
import processing.core.PApplet;

public class Requires implements ElementDrawer {

	private static final float RADIUS_SIDE_FACTOR = 2/3f;
	private static final float RADIUS_FACTOR = 1.414f;
	private Node reference;

	public Requires() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void draw(PApplet applet, Node node) {
		// draw triangle facing the reference node
		// attribution: http://processing.org/discourse/beta/num_1262805360.html ga jadi
		float r = node.getRadius();
		float angleReference = calculateAngle(node);
		// point 1
		float x1 = 0;
		float y1 = 0;
		// point 2
		float x2 = RADIUS_FACTOR * r;
		float y2 = RADIUS_SIDE_FACTOR * r;
		// point 3
		float x3 = RADIUS_FACTOR * r;
		float y3 = - RADIUS_SIDE_FACTOR * r;
		
		applet.pushMatrix();
		applet.fill(node.getShapeColor());
		applet.stroke(135);
		applet.strokeWeight(1);
		applet.translate(node.getPosition().x, node.getPosition().y);
		applet.rotate(angleReference);
		applet.triangle(x1, y1, x2, y2, x3, y3);
		applet.popMatrix();
		
	}

	/*
	 * Calculates angle to reference node
	 */
	private float calculateAngle(Node iface) {
		float dx = reference.getPosition().x - iface.getPosition().x;
		float dy = reference.getPosition().y - iface.getPosition().y;
		
		return PApplet.atan2(dy, dx);
	}
	
	/**
	 * @return the reference
	 */
	public Node getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(Node reference) {
		this.reference = reference;
	}

}
