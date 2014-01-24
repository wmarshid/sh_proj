package graspvis.logic.drawer.relationship;

import graspvis.model.Spring;
import graspvis.model.TreeNode;
import processing.core.PApplet;

public class Causality implements RelationshipDrawer {

	@Override
	public void draw(PApplet applet, Spring spring) {
		float x1 = spring.getFromNode().getPosition().x;
		float y1 = spring.getFromNode().getPosition().y;
		float x2 = spring.getToNode().getPosition().x;
		float y2 = spring.getToNode().getPosition().y;
		
		applet.stroke(10);
		applet.strokeWeight(2);
		
		applet.line(x1, y1, x2, y2);

	}

	@Override
	public void draw(PApplet applet, TreeNode node) {
		float x1 = node.getParent().getPosition().x;
		float y1 = node.getParent().getPosition().y;
		float x2 = node.getPosition().x;
		float y2 = node.getPosition().y;
		
		applet.stroke(10);
		applet.strokeWeight(2);
		
		applet.line(x1, y1, x2, y2);
	}

}
