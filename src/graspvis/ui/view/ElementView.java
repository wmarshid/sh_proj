package graspvis.ui.view;

import graspvis.controller.FrameController;
import graspvis.exception.AlgorithmException;
import graspvis.exception.SystemException;
import graspvis.logic.algorithm.FishEyeRadialTree;
import graspvis.logic.drawer.view.ElementViewDrawer;
import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.Tree;
import graspvis.model.TreeNode;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Textlabel;

public class ElementView extends PApplet {
	private float gapDiskRadius = 100;
	private FrameController controller;
	//private TreeNode tree;
	private Tree tree;
	
	private PFont font;
	private FishEyeRadialTree fRadialTree;
	
	private PVector diskCentre;
	private float diskRadius;
	private ElementViewDrawer drawer;
	
	private ControlP5 cp5;
	
	private PropertyPanel propertyPanel;
	private boolean filled = false;
	
	private TreeNode navigatedNode = null;
	
	public ElementView(Tree tree, FrameController controller) {
		this.tree = tree;
		this.controller = controller;
		diskCentre = new PVector();
		fRadialTree = new FishEyeRadialTree();
	}
	
	public void setup() {
		frameRate(20);
		background(255);
		smooth();
		noStroke();
		//noLoop();
		font = createFont("Arial", 16, true);
		calculateDisk();
		
		drawer = new ElementViewDrawer(this, tree, 2);
		cp5 = new ControlP5(this);
		
		propertyPanel = new PropertyPanel(this, tree.getRoot());
	}
	
	public void draw() {
		background(255);
		textFont(font);
		fill(0);
		if (!tree.isEmpty()) {
			try {
				pushStyle();
				stroke(122);
				noFill();
				strokeWeight(1);
				ellipse(diskCentre.x, diskCentre.y, diskRadius + gapDiskRadius, diskRadius + gapDiskRadius);
				popStyle();
				
				if (navigatedNode != null) {
					// put navigation animation here
				}
				
				drawer.draw();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void showTree() throws AlgorithmException {
		calculateDisk();
		
		drawer.setTree(tree);
		
		fRadialTree.setTree(this.tree);
		fRadialTree.setDiskCentre(diskCentre);
		fRadialTree.setDiskRadius(diskRadius);
		fRadialTree.run();
		
		//redraw();
	}
	
	public void mousePressed() {
		TreeNode node = tree.get(mouseX, mouseY);
		navigatedNode = node;
		
		if (node != null) {
			controller.showElement(node, this);
			((Highlight) node).highlight();
			controller.highlightElement(node);
		} else {
			controller.unhighlight();
		}
		// edit codebase here
		if (mouseButton == RIGHT) {
			System.out.println("respond to right click..");
			
		}
		
	}
	
	public void mouseReleased() {
		//println("//");
	}
	
	public void highlight(Node node) {
		tree.highlight(node);
	}
	
	public void unhighlight() {
		tree.unhighlight();
	}
	
	private void calculateDisk() {
		diskCentre = new PVector(width/2, height/2);
		diskRadius = min(width, height)/2f;
	}

	/**
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 * @throws AlgorithmException 
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
		propertyPanel.setTree(tree.getRoot());
		filled = true;
	}

	/**
	 * @return the filled
	 */
	public boolean isFilled() {
		return filled;
	}

	/**
	 * @param filled the filled to set
	 */
	public void setFilled(boolean filled) {
		this.filled = filled;
	}
}

class PropertyPanel {
	private final int BACKGROUND = 35;
	private final int HEIGHT = 130;
	private final int WIDTH = 400;
	private final int ALPHA = 50;

	private PApplet applet;
	private ControlP5 cp5;
	private Group cPanel;

	private Textlabel lblElement;
	private Textlabel lblElementValue;
	private Textlabel lblContains;
	private Textlabel lblContainValue;
	private Textlabel lblConnectedTo;
	private Textlabel lblConnectedToValue;

	private int x = 10, y = 20;
	private int xCbe = 5, yCbe = 5;

	private TreeNode tree;
	
	public PropertyPanel(PApplet applet, TreeNode tree) {
		this.applet = applet;
		this.tree = tree;
		
		cp5 = new ControlP5(applet);
		initGUI();
	}

	/**
	 * Initiate CP5 GUI components and set their position on the screen
	 */
	private void initGUI() {
		cPanel = cp5.addGroup("cPanel")
				.setLabel("Element Properties")
				.setPosition(x, y)
				.setBackgroundHeight(HEIGHT)
				.setWidth(WIDTH)
				.setBackgroundColor(applet.color(BACKGROUND, ALPHA));

		// Elements buttons
		initToggleElements();
	}

	private void initToggleElements() {
		float yLblGap = 5;
		float xLblGap = 50;
		float gapColumn = 100;
		cPanel.setLabel(tree.getLabel() + " Properties");
		
		lblElement = cp5.addTextlabel("lblElement")
				.setPosition(xCbe, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setText("ELEMENT:")
				.setGroup(cPanel);
		
		lblElementValue = cp5.addTextlabel("lblElementValue")
				.setPosition(xCbe + xLblGap, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setGroup(cPanel);
		
		lblConnectedTo = cp5.addTextlabel("lblConnectedTo")
				.setPosition(xCbe + gapColumn, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setText("CONNECTED TO:")
				.setGroup(cPanel);
		
		lblConnectedToValue = cp5.addTextlabel("lblConnectedToValue")
				.setPosition(xCbe + gapColumn + 1.5f*xLblGap, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setGroup(cPanel);
		
		yCbe += 25;
		lblContains = cp5.addTextlabel("lblContains")
				.setPosition(xCbe, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setText("CONTAINS:")
				.setGroup(cPanel);
		lblContainValue = cp5.addTextlabel("lblContainValue")
				.setPosition(xCbe + xLblGap, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setGroup(cPanel);
	}
	
	/**
	 * @return the tree
	 */
	public TreeNode getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(TreeNode tree) {
		this.tree = tree;
		setLabels();
	}
	
	private void setLabels() {
		// element
		StringBuilder sb = new StringBuilder();
		sb.append(tree.getLabel());
		lblElementValue.setText(sb.toString());
		// contains
		sb.setLength(0);
		for (TreeNode node : tree.getChilds()) {
			if (node.getParent().getRelationshipType(node) == RelationshipType.CONTAINMENT) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(node.getLabel());
			}
		}
		lblContainValue.setText(sb.toString());
		// connected to
		sb.setLength(0);
		for (TreeNode node : tree.getChilds()) {
			if (node.getParent().getRelationshipType(node) == RelationshipType.CONNECTION) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(node.getLabel());
			}
		}
		lblConnectedToValue.setText(sb.toString());
		// contained by
	}
}
