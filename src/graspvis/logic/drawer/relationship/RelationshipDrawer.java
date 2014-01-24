package graspvis.logic.drawer.relationship;

import graspvis.model.Spring;
import graspvis.model.TreeNode;
import processing.core.PApplet;

public interface RelationshipDrawer {
	public void draw(PApplet applet, Spring spring);
	public void draw(PApplet applet, TreeNode node);
}
