package graspvis.util;

import graspvis.model.Node;
import graspvis.model.TreeNode;

import java.awt.Toolkit;
import java.lang.reflect.Array;
import java.util.Random;

import processing.core.PApplet;

/**
 * Util class, some of the functionalities are adopted from Pocessing 
 * <a href="http://processing.org/">http://processing.org/</a>
 * @author nauval
 *
 */
public class Util {
	
	public final static int MIN_BOUNDARY_X = 15;
	public final static int MAX_BOUNDARY_X = Toolkit.getDefaultToolkit().getScreenSize().width - 50;
	
	public final static int MIN_BOUNDARY_Y = 15;
	public final static int MAX_BOUNDARY_Y = Toolkit.getDefaultToolkit().getScreenSize().height - 200;
	
	private static PApplet applet;
	
	private Util () {
		applet = new PApplet();
	}
	
	/**
	 * Generates random number between a given minimum and maximum number. This method is adopted from 
	 * Processing <a href="http://processing.org/">http://processing.org/</a>
	 * @param min
	 * @param max
	 * @return random number
	 */
	public static float random(float min, float max) {
		Random internalRandom = null;
		if (min > max) {
			return min;
		}
		float diff = max - min;
		
		if (diff == 0 || diff != diff) {
			return 0;
		}

		if (internalRandom == null) {
			internalRandom = new Random();
		}
		
		float value = 0;
		do {
			value = internalRandom.nextFloat() * diff;
		} while (value == diff);
		
		return value + min;
	}
	
	/**
	 * Returns string with given rules:
	 * <ul>
	 * <li>If the input consist of only small letters then it will return the first three letters</li>
	 * <li>If the input consist of camel cased letters, then it will returns the first letter and the 
	 * upper cased letters</li>
	 * </ul>
	 * @param label
	 * @return
	 */
	public static String getLabel(String label) {
		StringBuilder sb = new StringBuilder();
		String[] words = label.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		if (words.length > 1) {
			for (String w : words) {
		        sb.append(w.substring(0, 1));
		    }
		} else if(label.length() > 3) {
			sb.append(label.substring(0, 3));
		} else {
			sb.append(label);
		}
		
		return sb.toString();
	}
	
	public static float random(float middle, float min, float max) {
		return middle + random(min, max);
	}
	
	public static int interpolateColor(PApplet applet, Node node) {
		float theta = node.getFlickerTheta();
		theta += 0.2;
		float H = applet.hue(node.getShapeColor());
		float S = applet.saturation(node.getShapeColor());
		float B = applet.brightness(node.getShapeColor());
		
		if (B >= 50) {
			B = 50;
		}
		
		int toColor = applet.color(H, S, 100);
		int fromColor = applet.color(H,S, B);

		float amt = PApplet.sin(theta);
		
		int lerpColor = PApplet.lerpColor(fromColor, toColor, amt, PApplet.HSB);
		node.setFlickerTheta(theta);
		return lerpColor;
	}
	
//	public static int interpolateColor(PApplet applet, TreeNode node) {
//		float theta = node.getTheta();
//		theta += 0.2;
//		float H = applet.hue(node.getColor());
//		float S = applet.saturation(node.getColor());
//		float B = applet.brightness(node.getColor());
//		
//		if (B >= 50) {
//			B = 50;
//		}
//		
//		int toColor = applet.color(H, S, 100);
//		int fromColor = applet.color(H,S, B);
//
//		float amt = PApplet.sin(theta);
//		
//		int lerpColor = PApplet.lerpColor(fromColor, toColor, amt, PApplet.HSB);
//		node.setTheta(theta);
//		return lerpColor;
//	}
	
	public static PApplet getPAppletUtil() {
		if (applet == null) {
			applet = new PApplet();
		}
		return applet;
	}
}
