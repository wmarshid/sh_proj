package graspvis.logic.drawer.element;

import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.TreeNode;
import graspvis.util.Util;
import processing.core.PApplet;

public class Layer implements ElementDrawer {
	
	/**
	 * Curve factor of bezier curve
	 */
	private final float CURVE_FACTOR = 3;

	/**
	 * Upper left corner position
	 */
	private float cornerX, cornerY;

	/**
	 * Bezier control points
	 */
	private float cx1, cy1, cx2, cy2;

	/**
	 * Shape's width and height
	 */
	private float width, height;

	
	@Override
	public void draw(PApplet applet, Node node) {
		calculatePoints(node);
		applet.noStroke();
		float scale = node.getPosition().z;
		
		
		if (((Highlight) node).isHighlighted()) {
			applet.colorMode(PApplet.HSB, 360, 100, 100);
	
			int lerpColor = Util.interpolateColor(applet, node);
			applet.fill(lerpColor);
			drawLayer(applet);
			applet.colorMode(PApplet.RGB, 255, 255, 255);
		} else {
			applet.fill(node.getShapeColor());
			drawLayer(applet);
		}
		
		
		applet.fill(node.getLabelColor());
		String label = Util.getLabel(node.getLabel());
		if (scale > 0) {
			applet.textSize(applet.getFont().getSize2D() * scale);	
		}
		float x = node.getPosition().x-applet.textWidth(label)/2;
		float y = node.getPosition().y+applet.textWidth(label)/4;
	    applet.text(label, x, y);
	}
	
	private void drawLayer(PApplet applet) {
		applet.beginShape();
		applet.vertex(cornerX, cornerY);
		applet.bezierVertex(cx1, cy1, cx2, cy2, cornerX + width, cornerY);
		applet.vertex(cornerX + width, cornerY + height);
		applet.bezierVertex(cx2, cy2 + height, cx1, cy1 + height, cornerX, cornerY + height);
		applet.endShape(applet.CLOSE);
	}
	
	/**
	 * Calculates top left corner point and bezier control points
	 */
	private void calculatePoints(Node node) {
		float wCFactor = 1;
		float hCFactor = 1;
		
		width = 2f * node.getRadius();
		height = 2f/3f * width;
		
		wCFactor = width/CURVE_FACTOR;
		hCFactor = height/CURVE_FACTOR;
		
		cornerX = node.getPosition().x - node.getRadius(); 
		cornerY = node.getPosition().y - height/2;
		
		cx1 = cornerX + wCFactor;
		cy1 = cornerY + hCFactor;
		cx2 = cornerX + width - wCFactor;
		cy2 = cornerY - hCFactor;
	}
	
	/**
	 * Calculates top left corner point and bezier control points
	 */
	private void calculatePoints(TreeNode node) {
		float wCFactor = 1;
		float hCFactor = 1;
		
		width = 2f * node.getRadius();
		height = 2f/3f * width;
		
		wCFactor = width/CURVE_FACTOR;
		hCFactor = height/CURVE_FACTOR;
		
		cornerX = node.getPosition().x - node.getRadius(); 
		cornerY = node.getPosition().y - height/2;
		
		cx1 = cornerX + wCFactor;
		cy1 = cornerY + hCFactor;
		cx2 = cornerX + width - wCFactor;
		cy2 = cornerY - hCFactor;
	}
}
