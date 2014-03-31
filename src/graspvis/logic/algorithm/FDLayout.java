package graspvis.logic.algorithm;

import processing.core.PApplet;
import processing.core.PVector;
import graspvis.model.ForceDirectedNode;
import graspvis.model.ForceDirectedSpring;
import graspvis.model.Graph;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.Spring;

public class FDLayout implements Algorithm {

	private static final float SQUARE_ROOT_OF_TWO = 1.414f;
	private static final float RADIUS_FACTOR = 1.5f;
	private Graph graph;
	private float minX;
	private float minY;
	private float maxX;
	private float maxY;
	public FDLayout(Graph graph) {
		this.graph = graph;
		minX = 15;
		minY = 15;
		maxX = 400;
		maxY = 400;
	}
	@Override
	public void run() {
		// let all nodes repel each other
		for (Node node : graph.getNodes()) {
			((ForceDirectedNode) node).setBoundary(minX, minY, maxX, maxY);
			((ForceDirectedNode) node).attract(graph.getNodes());
		}
		// apply spring forces
		for (Spring spring : graph.getSprings()) {
			((ForceDirectedSpring) spring).update();
		}
		// apply velocity vector and update position
		for (Node node : graph.getNodes()) {
			((ForceDirectedNode) node).update();
		}
		// correction for interface position
		// look for springs that have INTERFACE type
		// Adjust the position of each interface
		for (Spring spring : graph.getSprings()) {
			if (spring.getRelationshipType() == RelationshipType.INTERFACE) {
				Node parent = spring.getFromNode();
				Node iFace = spring.getToNode();
				adjustInterfacePosition(parent, iFace);
			}
			if (spring.getRelationshipType() == RelationshipType.CONNECTION) {
				Node from = spring.getFromNode();
				Node ifaceFrom = spring.getHookFrom();
				Node to = spring.getToNode();
				Node ifaceTo = spring.getHookTo();
				
				System.out.println("var dump.. " + from + " " + ifaceFrom + " " + to + " " + ifaceTo);
				adjustInterfacePosition(from, ifaceFrom, to, ifaceTo);
			}
		}
	}
	
	private void adjustInterfacePosition(Node from, Node ifaceFrom, Node to,
			Node ifaceTo) {
		// from node to to node
		float dist = RADIUS_FACTOR * from.getRadius() * SQUARE_ROOT_OF_TWO;
		PVector start = new PVector(from.getPosition().x, from.getPosition().y);
		PVector end = new PVector(to.getPosition().x, to.getPosition().y);
		
		PVector result = calculateMiddlePoint(start, end, dist);
		ifaceFrom.getPosition().x = result.x;
		ifaceFrom.getPosition().y = result.y;
		
		// from to node to from node
		start.x = to.getPosition().x;
		start.y = to.getPosition().y;
		end.x = from.getPosition().x;
		end.y = from.getPosition().y;
		
		result = calculateMiddlePoint(start, end, dist);
		ifaceTo.getPosition().x = result.x;
		ifaceTo.getPosition().y = result.y;
	}
	
	/* (non javadoc)
	 * Calculates a point between start and end with distance dist from start point.
	 * The resulting point is the line (literally) between start and end point.
	 */
	private PVector calculateMiddlePoint(PVector start, PVector end, float dist) {
		PVector result = new PVector();
		float dx = end.x - start.x;
		float dy = end.y - start.y;
		
		float angle = PApplet.atan2(dy, dx);
		float x = dist * PApplet.cos(angle) + start.x;
		float y = dist * PApplet.sin(angle) + start.y;
		
		result.x = x;
		result.y = y;
		return result;
	}
	
	// calculate the angle and distance between two points and calibrate the distance to 
	// equal 1.414 * 2 * parent radius
	private void adjustInterfacePosition(Node parent, Node iFace) {
		float x;
		float y;
		// difference from to to node
		float dx = iFace.getPosition().x  - parent.getPosition().x ;
		float dy = iFace.getPosition().y - parent.getPosition().y;
		
		//float dist = PApplet.dist(parent.getX(), parent.getX(), iFace.getX(), iFace.getY());
		float angle = PApplet.atan2(dy, dx);
		
		float minR = RADIUS_FACTOR * parent.getRadius() * SQUARE_ROOT_OF_TWO;
		
		x = minR * PApplet.cos(angle) + parent.getPosition().x;
		y = minR * PApplet.sin(angle) + parent.getPosition().y;
		
		iFace.setPosition(x, y, 1);
	}
	/**
	 * @return the minX
	 */
	public float getMinX() {
		return minX;
	}
	/**
	 * @param minX the minX to set
	 */
	public void setMinX(float minX) {
		this.minX = minX;
	}
	/**
	 * @return the minY
	 */
	public float getMinY() {
		return minY;
	}
	/**
	 * @param minY the minY to set
	 */
	public void setMinY(float minY) {
		this.minY = minY;
	}
	/**
	 * @return the maxX
	 */
	public float getMaxX() {
		return maxX;
	}
	/**
	 * @param maxX the maxX to set
	 */
	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}
	/**
	 * @return the maxY
	 */
	public float getMaxY() {
		return maxY;
	}
	/**
	 * @param maxY the maxY to set
	 */
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
}
