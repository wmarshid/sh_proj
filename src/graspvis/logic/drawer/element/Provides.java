package graspvis.logic.drawer.element;

import graspvis.model.Node;
import graspvis.model.TreeNode;

import processing.core.PApplet;

public class Provides implements ElementDrawer {

	private static final float RADIUS_SIDE_FACTOR = 2/3f;
	private static final float RADIUS_FACTOR = 1.414f;
	private Node reference;

	public Provides() {
	}

	@Override
	public void draw(PApplet applet, Node node) {
		// draw triangle facing the reference node
		// attribution: http://processing.org/discourse/beta/num_1262805360.html ga jadi
		float r = node.getRadius();
		float angleReference = calculateAngle(node);
		float x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0;
		x1 = RADIUS_FACTOR * r;
		//y1 = - r;
		// point 2
		y2 = RADIUS_SIDE_FACTOR * r;
		// point 3
		y3 = -RADIUS_SIDE_FACTOR * r;

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
	 * Calculates angle from reference node
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
