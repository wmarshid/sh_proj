package graspvis.logic.drawer.element;

import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.TreeNode;
import graspvis.util.Util;
import processing.core.PApplet;

public class Rationale implements ElementDrawer {

	/**
	 * Curve factor of bezier curve
	 */
	private float gap = 5;

	/**
	 * Upper left corner position
	 */
	private float cornerX, cornerY;

	/**
	 * Shape's width and height
	 */
	private float width, height;

	@Override
	public void draw(PApplet applet, Node node) {
		float rad = node.getRadius();
		float scale = node.getPosition().z;
		calculatePoints(node);

		if (((Highlight) node).isHighlighted()) {
			applet.colorMode(PApplet.HSB, 360, 100, 100);
			// draw circle
			applet.fill(0,0,100);
			applet.noStroke();
			//applet.ellipse(node.getPosition().x, node.getPosition().y, 2.1f*rad, 2.1f*rad);

			int lerpColor = Util.interpolateColor(applet, node);
			applet.fill(lerpColor);

			drawShape(applet);

			applet.colorMode(PApplet.RGB, 255, 255, 255);
		} else {
			// draw circle
			applet.fill(255);
			applet.noStroke();
			applet.fill(node.getShapeColor());

			drawShape(applet);

		}

		applet.fill(node.getLabelColor());
		String label = Util.getLabel(node.getLabel());
		if (scale > 0) {
			applet.textSize(applet.getFont().getSize2D() * scale);	
		}
		applet.text(label, node.getPosition().x, 
				node.getPosition().y + applet.textWidth(label)/4);
	}

	private void drawShape(PApplet applet) {
		applet.beginShape();
		applet.vertex(cornerX + gap, cornerY);
		applet.vertex(cornerX + gap + width, cornerY);
		applet.vertex(cornerX + width, cornerY + height);
		applet.vertex(cornerX ,cornerY + height);
		applet.endShape();
	}

	public void draw(PApplet applet, TreeNode node) {
		float scaleFactor = 8;
		float x = node.getPosition().x;
		float y = node.getPosition().y;

		float scale = node.getPosition().z;
		applet.textSize(applet.getFont().getSize2D() * scale);
		
		calculatePoints(node);
		applet.noStroke();

		if (((Highlight) node).isHighlighted()) {
			applet.colorMode(PApplet.HSB, 360, 100, 100);
			// draw circle
			applet.fill(0,0,100);
			applet.noStroke();
			//applet.ellipse(node.getPosition().x, node.getPosition().y, 2.1f*rad, 2.1f*rad);

			int lerpColor = Util.interpolateColor(applet, node);
			applet.fill(lerpColor);

			drawShape(applet);

			applet.colorMode(PApplet.RGB, 255, 255, 255);
		} else {
			// draw circle
			applet.fill(255);
			applet.noStroke();
			applet.fill(node.getShapeColor());

			drawShape(applet);

		}

		applet.fill(node.getLabelColor());
		String label = Util.getLabel(node.getLabel());
		applet.text(label, x, 
				y + applet.textWidth(label)/4);
	}

	private void calculatePoints(Node node) {

		width = 2f * node.getRadius();
		height = width;

		gap = node.getRadius() / 2;

		cornerX = node.getPosition().x - node.getRadius(); 
		cornerY = node.getPosition().y - height/2;
	}

	private void calculatePoints(TreeNode node) {

		width = 2f * node.getRadius();
		height = width;

		gap = node.getRadius() / 2;

		cornerX = node.getPosition().x - node.getRadius(); 
		cornerY = node.getPosition().y - height/2;
	}

}
