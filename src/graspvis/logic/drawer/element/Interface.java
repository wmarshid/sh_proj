package graspvis.logic.drawer.element;

import processing.core.PApplet;
import graspvis.model.Node;
import graspvis.model.TreeNode;
import graspvis.util.Util;

public class Interface implements ElementDrawer {
	@Override
	public void draw(PApplet applet, Node node) {
		//applet.noStroke();
		applet.stroke(196);
		applet.strokeWeight(.5f);
		applet.fill(node.getShapeColor());
	    applet.ellipse(node.getPosition().x, node.getPosition().y, node.getRadius(), node.getRadius());
	}
}
