package graspvis.logic.drawer.relationship;

import graspvis.model.Spring;
import graspvis.model.TreeNode;
import processing.core.PApplet;

public class Contained implements RelationshipDrawer {

	/**
	 * unused
	 */
	@Override
	public void draw(PApplet applet, Spring spring) {
		
	}

	@Override
	public void draw(PApplet applet, TreeNode node) {
		float x1 = node.getPosition().x;
		float y1 = node.getPosition().y;
		float x2 = node.getParent().getPosition().x;
		float y2 = node.getParent().getPosition().y;
		float x3 = x2  - node.getRadius();
		float y3 = y2  - node.getRadius();
		
		//applet.pushStyle();
		applet.stroke(10);
		applet.strokeWeight(3);
		
		applet.fill(35, 75);
		applet.noStroke();
		applet.triangle(x1, y1, x2, y2, x3, y3);
//		System.out.printf("Draw contained of node %s [%s,%s] [%s,%s] [%s,%s] with radius = %s\n", 
//				node.getId(), x1, y1, x2, y2, x3, y3, node.getRadius());
		//applet.popStyle();
	}

}
