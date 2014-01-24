package graspvis.logic.drawer.element;

import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.TreeNode;
import graspvis.util.Util;
import processing.core.PApplet;

public class Connector implements ElementDrawer {

	@Override
	public void draw(PApplet applet, Node node) {
		float rad = node.getRadius();
		float scale = node.getPosition().z;
		
		if (((Highlight) node).isHighlighted()) {
			applet.colorMode(PApplet.HSB, 360, 100, 100);
			// draw circle
			applet.fill(0,0,100);
			applet.noStroke();
			applet.ellipse(node.getPosition().x, node.getPosition().y, 2f*rad, 2f*rad);
			
			int lerpColor = Util.interpolateColor(applet, node);
			applet.fill(lerpColor);
			
			applet.fill(lerpColor);
			applet.ellipse(node.getPosition().x, node.getPosition().y, 2*rad, 2*rad);
			applet.colorMode(PApplet.RGB, 255, 255, 255);
		} else {
			// draw circle
			applet.fill(255);
			applet.noStroke();
			
			applet.ellipse(node.getPosition().x, node.getPosition().y, 2f*rad, 2f*rad);
			applet.fill(node.getShapeColor());
			applet.ellipse(node.getPosition().x, node.getPosition().y, 2*rad, 2*rad);
			
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
