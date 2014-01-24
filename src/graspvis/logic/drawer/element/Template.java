package graspvis.logic.drawer.element;

import processing.core.PApplet;
import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.TreeNode;
import graspvis.util.Util;

public class Template implements ElementDrawer {
	
	@Override
	public void draw(PApplet applet, Node node) {
		float size = node.getRadius();
		float gap = size/6;
		float cRad = size + gap;
		float gap2 = gap/3;
		float gap3 = (gap + gap2)/2;
		float x = node.getPosition().x;
		float y = node.getPosition().y;
		float scale = node.getPosition().z;
		
		applet.fill(node.getShapeColor());
		applet.noStroke();
		
		if (((Highlight) node).isHighlighted()) {
			applet.colorMode(PApplet.HSB, 360, 100, 100);
			
			applet.rectMode(applet.CENTER);
			applet.rect(x,y,size, size);
			applet.ellipse(x,y,cRad,cRad);
			 
			applet.fill(0,0,100);
			applet.noStroke();
			applet.rectMode(applet.CENTER);
			applet.rect(x,y,size-gap2, size-gap2);
			applet.ellipse(x,y,cRad-gap2,cRad-gap2);
			
			int lerpColor = Util.interpolateColor(applet, node);
			applet.fill(lerpColor);
			
			applet.fill(lerpColor);
			applet.noStroke();
			applet.rectMode(applet.CENTER);
			applet.rect(x,y,size-gap3, size-gap3);
			applet.ellipse(x,y,cRad-gap3,cRad-gap3);
			
			applet.colorMode(PApplet.RGB, 255, 255, 255);
		} else {
			applet.rectMode(applet.CENTER);
			applet.rect(x,y,size, size);
			applet.ellipse(x,y,cRad,cRad);
			 
			applet.fill(255);
			applet.noStroke();
			applet.rectMode(applet.CENTER);
			applet.rect(x,y,size-gap2, size-gap2);
			applet.ellipse(x,y,cRad-gap2,cRad-gap2);
			 
			 
			applet.fill(node.getShapeColor());
			applet.noStroke();
			applet.rectMode(applet.CENTER);
			applet.rect(x,y,size-gap3, size-gap3);
			applet.ellipse(x,y,cRad-gap3,cRad-gap3);
		} 
		
		applet.fill(node.getLabelColor());
		String lbl = Util.getLabel(node.getLabel());
		if (scale > 0) {
			applet.textSize(applet.getFont().getSize2D() * scale);	
		}
	    applet.text(lbl, x-applet.textWidth(lbl)/2, y+applet.textWidth(lbl)/4);
	}
	
//	public void draw(PApplet applet, TreeNode node) {
//		float x = node.getPosition().x;
//		float y = node.getPosition().y;
//
//		float size = node.getRadius();
//		float gap = size/6;
//		float cRad = size + gap;
//		float gap2 = gap/3;
//		float gap3 = (gap + gap2)/2;
//		
//		float scale = node.getPosition().z;
//		applet.textSize(applet.getFont().getSize2D() * scale);
//		
//		applet.fill(node.getShapeColor());
//		applet.noStroke();
//		
//		if (((Highlight) node).isHighlighted()) {
//			applet.colorMode(PApplet.HSB, 360, 100, 100);
//			
//			applet.rectMode(applet.CENTER);
//			applet.rect(x,y,size, size);
//			applet.ellipse(x,y,cRad,cRad);
//			 
//			applet.fill(0,0,100);
//			applet.noStroke();
//			applet.rectMode(applet.CENTER);
//			applet.rect(x,y,size-gap2, size-gap2);
//			applet.ellipse(x,y,cRad-gap2,cRad-gap2);
//			
//			int lerpColor = Util.interpolateColor(applet, node);
//			applet.fill(lerpColor);
//			
//			applet.fill(lerpColor);
//			applet.noStroke();
//			applet.rectMode(applet.CENTER);
//			applet.rect(x,y,size-gap3, size-gap3);
//			applet.ellipse(x,y,cRad-gap3,cRad-gap3);
//			
//			applet.colorMode(PApplet.RGB, 255, 255, 255);
//		} else {
//			applet.rectMode(applet.CENTER);
//			applet.rect(x,y,size, size);
//			applet.ellipse(x,y,cRad,cRad);
//			 
//			applet.fill(255);
//			applet.noStroke();
//			applet.rectMode(applet.CENTER);
//			applet.rect(x,y,size-gap2, size-gap2);
//			applet.ellipse(x,y,cRad-gap2,cRad-gap2);
//			 
//			 
//			applet.fill(node.getShapeColor());
//			applet.noStroke();
//			applet.rectMode(applet.CENTER);
//			applet.rect(x,y,size-gap3, size-gap3);
//			applet.ellipse(x,y,cRad-gap3,cRad-gap3);
//		} 
//
//		applet.fill(node.getLabelColor());
//		String lbl = Util.getLabel(node.getLabel());
//	    applet.text(lbl, x-applet.textWidth(lbl)/2, y+applet.textWidth(lbl)/4);
//	}

}
