package graspvis.logic.algorithm;

import graspvis.exception.AlgorithmException;
import graspvis.model.BasicTree;
import graspvis.model.Tree;
import graspvis.model.TreeNode;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Generate radial layout for given tree with fisheye projection.
 * Algorithm is adopted from [1,2]
 * 
 * <br><br>
 * [1] Janet M. Six and Ioannis G. Tollis, A framework and algorithms for circular drawings of graphs, 2006. <br>
 * [2] Bernard, Margaret A, On the automated drawing of graphs, 1981. 
 * @author nauval
 *
 */
public class FishEyeRadialTree implements Algorithm {

	private final float MINIMUM_DISTANCE = 100;
	private float rootAngleOffset;
	
	//private float initialDistance;
	private float diskRadius;

	// centre of the disk
	private PVector diskCentre;

	//private TreeNode tree;
	private Tree tree;

	private FisheyeProjection fishyeTransformation;

	public FishEyeRadialTree() {
		this.tree = new BasicTree();
		this.diskCentre = new PVector();
		this.diskRadius = 0;
		this.fishyeTransformation = new FisheyeProjection(this.diskCentre, this.diskRadius);
		initCalculation();
	}
	
	public FishEyeRadialTree(Tree tree, PVector centre, float diskRadius) {
		this.tree = tree;
		this.diskCentre = new PVector(centre.x, centre.y);
		this.diskRadius = diskRadius;
		this.fishyeTransformation = new FisheyeProjection();
		initCalculation();
	}

	private void initCalculation() {
		// calculate the given angle of each child [2]
		rootAngleOffset = tree.getRoot().getChildCount() > 1? 
				PApplet.TWO_PI/tree.getRoot().getChildCount() : PApplet.PI;

		// calculate the distance from parent to child, with regards of childs' radius [1]
		//initialDistance = MINIMUM_DISTANCE + (tree.getRadius()/2) + (tree.getMaxChildRadius()/2);
		fishyeTransformation.setDiskCentre(diskCentre);
		fishyeTransformation.setDiskRadius(diskRadius);
	}
	
	@Override
	public void run() throws AlgorithmException {
		if (tree == null || diskCentre == null || diskRadius <= 0) {
			throw new AlgorithmException("tree and disk centre position can not be null, " +
					"disk radius has to be grater than 0");
		}
		initCalculation();
		updatePosition(tree.getRoot(), tree.getRoot().getChildCount(), rootAngleOffset, rootAngleOffset, 0, 0, 0);
	}

	private void updatePosition(TreeNode node, int childIndex, 
							float angleOffsetParent, float angleOffset, 
							float angle, float distanceParent, int level) {

		// if node is root then set position to centre
		if (node.isRoot()) {
			node.setPosition(new PVector(diskCentre.x, diskCentre.y, 1));
		}
		// calculate the distance from parent to child, using calculation from [1] 
		float distance = MINIMUM_DISTANCE + (node.getRadius()/2) + (node.getMaxChildRadius()/2);

		// set base position from root node [2]
		float xParent = tree.getRoot().getPosition().x;
		float yParent = tree.getRoot().getPosition().y;
		
		// start angle of the children calculated by subtracting the parent angle with half of the offset [2]
		// if child size is one then the angle is the same as parent
		if (level > 0) {
			angle = node.getChildCount() == 1? angle : angle - (angleOffsetParent/2);
		}
		for (TreeNode child : node.getChilds()) {
			float parentAngle = angle + angleOffset * childIndex;
			childIndex --;
			
			// calculates position of each child
			float distChild = distance + distanceParent;
			float xChild = xParent + distChild * PApplet.cos(parentAngle);
			float yChild = yParent + distChild * PApplet.sin(parentAngle);

			PVector p = new PVector(xChild, yChild, 1);
			fishyeTransformation.project(p);
			child.setPosition(p);
			float size = p.z * child.getRadius();
			child.setRadius(size);

			updatePosition(child, child.getChildCount() - 1, angleOffset, 
							angleOffset/child.getChildCount(), parentAngle, 
							distChild, level + 1);
		}
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
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
	}
}
