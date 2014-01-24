package graspvis.logic.drawer.relationship;

import graspvis.model.Node;
import graspvis.model.Spring;
import graspvis.model.TreeNode;
import processing.core.PApplet;

public class Containment implements RelationshipDrawer {

	private enum Quadrant {Q1, Q2, Q3, Q4}
	
	private Quadrant getChildQuadrant(Node parent, Node child) {
		float x1 = parent.getPosition().x;
		float y1 = parent.getPosition().y;
		float x2 = child.getPosition().x;
		float y2 = child.getPosition().y;
		float rad = child.getRadius();
		
		// select quadrant of childNode relative to parent node
		// if child node is in Q1, x1 < x2 && y1 > y2
		if (x1 < x2-rad && y1 > y2+rad) {
			return Quadrant.Q1;
		}
		// if child node is in Q2, x1 > x2 && y1 < y2
		if (x1 < x2-rad && y1 < y2-rad) {
			return Quadrant.Q2;
		}
		
		// if child node is in Q3, x1 > x2 && y1 < y2
		if (x1 > x2+rad && y1 < y2-rad) {
			return Quadrant.Q3;
		}
		
		// if child node is in Q4, x1 > x2 && y1 > y2
		if (x1 > x2+rad && y1 < y2+rad) {
			return Quadrant.Q4;
		}
		
		return Quadrant.Q1;
	}

	@Override
	public void draw(PApplet applet, Spring spring) {
		float x1 = spring.getFromNode().getPosition().x;
		float y1 = spring.getFromNode().getPosition().y;
		float x2 = spring.getToNode().getPosition().x;
		float y2 = spring.getToNode().getPosition().y;
		float x3 = x2;
		float y3 = y2;
		
		float radChild = spring.getToNode().getRadius();
		
		switch (getChildQuadrant(spring.getFromNode(), spring.getToNode())) {
		case Q1:
		case Q3:
		default:
			x2 -= radChild;
			y2 -= radChild;
			x3 += radChild;
			y3 += radChild;
			break;
		case Q2:
		case Q4:
			x2 += radChild;
			y2 -= radChild;
			x3 -= radChild;
			y3 += radChild;
			break;
		}
		
		applet.fill(35, 75);
		applet.noStroke();
		applet.triangle(x1, y1, x2, y2, x3, y3);
	}
	
	@Override
	public void draw(PApplet applet, TreeNode node) {
		float x1 = node.getParent().getPosition().x;
		float y1 = node.getParent().getPosition().y;
		float x2 = node.getPosition().x;
		float y2 = node.getPosition().y;
		float x3 = x2  - node.getRadius();
		float y3 = y2  - node.getRadius();
		
		applet.stroke(10);
		applet.strokeWeight(3);
		
		applet.fill(35, 75);
		applet.noStroke();
		applet.triangle(x1, y1, x2, y2, x3, y3);
		
	}
	
//	private void drawObject(Spring spring) {
//		Node f = spring.getHookFrom();
//		Node t = spring.getHookTo();
//		float rad = f.getRadius();
//		float dx = t.getPosition().x - f.getPosition().x;
//		float dy = t.getPosition().y - f.getPosition().y;
//		float dist = PApplet.dist(f.getPosition().x, f.getPosition().y, 
//				t.getPosition().y, t.getPosition().y);
//		float ry = PApplet.sqrt(dist*dist + rad*rad);
//		
//	}
}
