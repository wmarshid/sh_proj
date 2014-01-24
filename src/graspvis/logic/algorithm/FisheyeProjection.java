package graspvis.logic.algorithm;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Calculates fisheye projection of a given point. This class will calculate the new 
 * point after projected to a fisheye view and also the resulting scale to maintain 
 * proportion. 
 * 
 * The x, y will be calculated based on (0,0) coordinate with the centre of 
 * the disc as the center of the coordinate.  </br>
 * 
 * Adapted from book "Generative Design: Visualize, Program, and Create with Processing" 
 * page 454-455 by Bohnacker et. al.
 * 
 * @author nauval
 *
 */
public class FisheyeProjection implements Algorithm {
	
	private final float OBSERVER_DISTANCE_FACTOR = .5f;
	// fish eye angel is 180 or PI, the needed value is PI/2
	private final float HALF_FISHEYE_ANGLE = PApplet.HALF_PI;
	
	// Radius of fisheye disc
	private float diskRadius;
	// centre of the disk
	private PVector diskCentre;
	
	// the resulting point
	private PVector point;
	// original point given
	private PVector originalPoint;
	
	private float observerDistance;
	
	public FisheyeProjection() {
		this.diskCentre = new PVector();
		this.diskRadius = 0;
		this.point = new PVector(diskCentre.x, diskCentre.y);
		
	}
	
	public FisheyeProjection(PVector centre, float diskRadius) {
		this.diskCentre = centre;
		this.diskRadius = diskRadius;
		this.point = new PVector(diskCentre.x, diskCentre.y);
	}
	
	/**
	 * Constructor, new objects are created to avoid accessing the same object 
	 * @param point
	 * @param centre
	 * @param diskRadius
	 */
	public FisheyeProjection(PVector point, PVector centre, float diskRadius) {
		this.originalPoint = new PVector(point.x, point.y);
		this.diskCentre = centre;
		this.diskRadius = diskRadius;
		this.point = point;
	}
	
	private void initCalculation() {
		this.observerDistance = diskRadius * OBSERVER_DISTANCE_FACTOR;
	}

	@Override
	public void run() {
		initCalculation();
		project();
	}
	
	public void project(PVector point) {
		this.originalPoint = new PVector(point.x, point.y);
		this.point = point;
		run();
	}
	
	public void project() {
		float x = originalPoint.x - diskCentre.x;
		float y = originalPoint.y - diskCentre.y;
		
		// convert to polar coordinate system => (distance, theta)
		// distance of the position
		float distance = PApplet.mag(x, y);

		// angle of the position
		float theta = PApplet.atan2(y, x);
		
		// calculate the viewing angle
		float projectionAngel = PApplet.atan(distance/observerDistance)/HALF_FISHEYE_ANGLE;
		float projectedDistance = projectionAngel * diskRadius;
		
		// new position needs to be added with disk centre as the calculation is simulated on (0,0)
		float projectedX = projectedDistance * PApplet.cos(theta) + diskCentre.x;
		float projectedY = projectedDistance * PApplet.sin(theta) + diskCentre.y;
		float projectedScale = PApplet.min(1.2f-projectionAngel, 1);
		
		// assign new points
		point.x = projectedX;
		point.y = projectedY;
		point.z = projectedScale;
	}

	/**
	 * @return the diskCentre
	 */
	public PVector getDiskCentre() {
		return diskCentre;
	}

	/**
	 * @param diskCentre the diskCentre to set
	 */
	public void setDiskCentre(PVector diskCentre) {
		this.diskCentre = diskCentre;
	}

	/**
	 * @return the diskRadius
	 */
	public float getDiskRadius() {
		return diskRadius;
	}

	/**
	 * @param diskRadius the diskRadius to set
	 */
	public void setDiskRadius(float diskRadius) {
		this.diskRadius = diskRadius;
	}

	/**
	 * @return the point
	 */
	public PVector getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(PVector point) {
		this.point = point;
	}

}
