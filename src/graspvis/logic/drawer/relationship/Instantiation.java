package graspvis.logic.drawer.relationship;

import graspvis.model.Spring;
import graspvis.model.TreeNode;
import processing.core.PApplet;

public class Instantiation implements RelationshipDrawer {

	@Override
	public void draw(PApplet applet, Spring spring) {
		float x1 = spring.getFromNode().getPosition().x;
		float y1 = spring.getFromNode().getPosition().y;
		float x2 = spring.getToNode().getPosition().x;
		float y2 = spring.getToNode().getPosition().y;
		
		applet.stroke(10);
		applet.strokeWeight(2);
		
		//applet.line(x1, y1, x2, y2);
		dashline(x1, y1, x2, y2, 5, 5, applet);
	}
	
	@Override
	public void draw(PApplet applet, TreeNode node) {
		float x1 = node.getParent().getPosition().x;
		float y1 = node.getParent().getPosition().y;
		float x2 = node.getPosition().x;
		float y2 = node.getPosition().y;
		
		applet.stroke(10);
		applet.strokeWeight(2);
		
		//applet.line(x1, y1, x2, y2);
		dashline(x1, y1, x2, y2, 5, 5, applet);
	}
	
	/* Taken from J David Eisenberg (January 17 2010)
	 * OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/7013*@*
	 * 
	 * Draw a dashed line with given set of dashes and gap lengths.
	 * x0 starting x-coordinate of line.
	 * y0 starting y-coordinate of line.
	 * x1 ending x-coordinate of line.
	 * y1 ending y-coordinate of line.
	 * spacing array giving lengths of dashes and gaps in pixels;
	 *  an array with values {5, 3, 9, 4} will draw a line with a
	 *  5-pixel dash, 3-pixel gap, 9-pixel dash, and 4-pixel gap.
	 *  if the array has an odd number of entries, the values are
	 *  recycled, so an array of {5, 3, 2} will draw a line with a
	 *  5-pixel dash, 3-pixel gap, 2-pixel dash, 5-pixel gap,
	 *  3-pixel dash, and 2-pixel gap, then repeat.
	 */
	private void dashline(float x0, float y0, float x1, float y1, float[ ] spacing, PApplet applet)
	{
	  float distance = applet.dist(x0, y0, x1, y1);
	  float [ ] xSpacing = new float[spacing.length];
	  float [ ] ySpacing = new float[spacing.length];
	  float drawn = 0.0f;  // amount of distance drawn

	  if (distance > 0)
	  {
	    int i;
	    boolean drawLine = true; // alternate between dashes and gaps

	    /*
	      Figure out x and y distances for each of the spacing values
	      I decided to trade memory for time; I'd rather allocate
	      a few dozen bytes than have to do a calculation every time
	      I draw.
	    */
	    for (i = 0; i < spacing.length; i++)
	    {
	      xSpacing[i] = applet.lerp(0, (x1 - x0), spacing[i] / distance);
	      ySpacing[i] = applet.lerp(0, (y1 - y0), spacing[i] / distance);
	    }

	    i = 0;
	    while (drawn < distance)
	    {
	      if (drawLine)
	      {
	    	  applet.line(x0, y0, x0 + xSpacing[i], y0 + ySpacing[i]);
	      }
	      x0 += xSpacing[i];
	      y0 += ySpacing[i];
	      /* Add distance "drawn" by this line or gap */
	      drawn = drawn + applet.mag(xSpacing[i], ySpacing[i]);
	      i = (i + 1) % spacing.length;  // cycle through array
	      drawLine = !drawLine;  // switch between dash and gap
	    }
	  }
	}

	/*
	 * Draw a dashed line with given dash and gap length.
	 * x0 starting x-coordinate of line.
	 * y0 starting y-coordinate of line.
	 * x1 ending x-coordinate of line.
	 * y1 ending y-coordinate of line.
	 * dash - length of dashed line in pixels
	 * gap - space between dashes in pixels
	 */
	private void dashline(float x0, float y0, float x1, float y1, float dash, float gap, PApplet applet)
	{
	  float [ ] spacing = { dash, gap };
	  dashline(x0, y0, x1, y1, spacing, applet);
	}
}
