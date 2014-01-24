package graspvis.logic.drawer.element;

import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.TreeNode;
import graspvis.util.Util;
import processing.core.PApplet;

public class Component implements ElementDrawer {

	@Override
	public void draw(PApplet applet, Node node) {
		float w = node.getRadius();// 11f;
		float scale = node.getPosition().z;
		
		if (((Highlight) node).isHighlighted()) {
			applet.colorMode(PApplet.HSB, 360, 100, 100);
			applet.fill(255);
			applet.noStroke();
			applet.ellipse(node.getPosition().x, node.getPosition().y, 2*w, 2*w);

			int lerpColor = Util.interpolateColor(applet, node);
			applet.fill(lerpColor);

			applet.rectMode(applet.RADIUS);
			applet.rect(node.getPosition().x, node.getPosition().y, w, w);
			applet.colorMode(PApplet.RGB, 255, 255, 255);

			applet.colorMode(PApplet.RGB, 255, 255, 255);
		} else {
			applet.fill(255);
			applet.noStroke();
			applet.ellipse(node.getPosition().x, node.getPosition().y, 2*w, 2*w);
			applet.fill(node.getShapeColor());
			applet.rectMode(applet.RADIUS);
			applet.rect(node.getPosition().x, node.getPosition().y, w, w);
		}


		applet.fill(node.getLabelColor());
		String label = Util.getLabel(node.getLabel());
		if (scale > 0) {
			applet.textSize(applet.getFont().getSize2D() * scale);	
		}
		applet.text(label, node.getPosition().x - applet.textWidth(label)/2, 
				node.getPosition().y + applet.textWidth(label)/4);
	}
}
