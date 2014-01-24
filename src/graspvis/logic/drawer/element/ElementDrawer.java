package graspvis.logic.drawer.element;

import graspvis.model.Node;
import graspvis.model.TreeNode;
import processing.core.PApplet;

/**
 * Interface of the shape's drawing mechanism.
 * 
 * @author nauval
 *
 */
public interface ElementDrawer {
	/**
	 * Draws the shape
	 */
	public void draw(PApplet applet, Node node);
	
	//public void draw(PApplet applet, TreeNode node);
}
