package graspvis.logic.drawer.view;

import graspvis.exception.SystemException;
import graspvis.logic.drawer.element.Component;
import graspvis.logic.drawer.element.Connector;
import graspvis.logic.drawer.element.Interface;
import graspvis.logic.drawer.element.Layer;
import graspvis.logic.drawer.element.Provides;
import graspvis.logic.drawer.element.Rationale;
import graspvis.logic.drawer.element.ElementDrawer;
import graspvis.logic.drawer.element.Requires;
import graspvis.logic.drawer.element.Template;
import graspvis.logic.drawer.relationship.Causality;
import graspvis.logic.drawer.relationship.Connection;
import graspvis.logic.drawer.relationship.Contained;
import graspvis.logic.drawer.relationship.Containment;
import graspvis.logic.drawer.relationship.Extended;
import graspvis.logic.drawer.relationship.Extension;
import graspvis.logic.drawer.relationship.Instantiation;
import graspvis.logic.drawer.relationship.RelationshipDrawer;
import graspvis.model.Node;
import graspvis.model.Tree;
import graspvis.model.TreeNode;
import processing.core.PApplet;

public class ElementViewDrawer implements ViewDrawer {

	private PApplet applet;
	//private TreeNode tree;
	private Tree tree;
	
	private ElementDrawer layer;
	private ElementDrawer component;
	private ElementDrawer connector;
	private ElementDrawer template;
	private ElementDrawer rationale;
	private ElementDrawer provides;
	private ElementDrawer requires;
	
	private RelationshipDrawer connection;
	private RelationshipDrawer containment;
	private RelationshipDrawer instantiation;
	private RelationshipDrawer causality;
	private RelationshipDrawer contained;
	private RelationshipDrawer interfaceRalationship;
	private RelationshipDrawer extension;
	private RelationshipDrawer extended;
	
	private int level;
	
	public ElementViewDrawer(PApplet applet, Tree tree, int level) {
		this.applet = applet;
		this.tree = tree;
		this.level = level;
		
		layer = new Layer();
		component = new Component();
		connector = new Connector();
		template = new Template();
		new Interface();
		rationale = new Rationale();
		provides = new Provides();
		requires = new Requires();
		
		connection = new Connection();
		containment = new Containment();
		instantiation = new Instantiation();
		causality = new Causality();
		contained = new Contained();
		extension = new Extension();
		extended = new Extended();
	}
	
	@Override
	public void draw() throws SystemException {
		if (tree == null) {
			throw new SystemException("Tree is null");
		}
		drawTree(tree.getRoot(), level);
	}
	
	private void drawTree(TreeNode tree, int depth) {
		for (TreeNode node : tree.getChilds()) {
			drawTree(node, depth - 1);
		}
		if (!tree.isRoot()) drawSpring(tree);
		if (depth < 1) {
			return;
		}
		drawNode(tree);
		//drawNode(root);
		//drawLabel(root);
	}
	
	private void drawNode(Node node) {
		Node parent = null;
		if (node.getElementType() == null) {
			return;
		}
		switch (node.getElementType()) {
		case LAYER:
			layer.draw(applet, node);
			break;
		case CONNECTOR:
			connector.draw(applet, node);
			break;
		case COMPONENT:
			component.draw(applet, node);
			break;
		case TEMPLATE:
			template.draw(applet, node);
			break;
		case PROVIDES:
			if (((TreeNode) node).isRoot())
				parent = ((TreeNode) node).getChilds().get(0);
			else
				parent = ((TreeNode) node).getParent();
				
			((Provides) provides).setReference(parent);
			provides.draw(applet, node);
			break;
		case REQUIRES:
			if (((TreeNode) node).isRoot()) 
				parent = ((TreeNode) node).getChilds().get(0);
			else
				parent = ((TreeNode) node).getParent();
			
			((Requires) requires).setReference(parent);
			requires.draw(applet, node);
			break;
		case RATIONALE:
			rationale.draw(applet, node);
			break;
		default:
			break;
		}
	}
	
	private void drawSpring(TreeNode node) {
		switch (node.getParent().getRelationshipType(node)) {
		case CONNECTION:
			connection.draw(applet,node);
			break;
		case CONTAINMENT:
			containment.draw(applet,node);
			break;
		case INSTANTIATION:
			instantiation.draw(applet, node);
			break;
		case CAUSALITY:
			causality.draw(applet, node);
			break;
		case CONTAINED:
			contained.draw(applet, node);
			break;
		case EXTENSION:
			extension.draw(applet, node);
			break;
		case EXTENDED:
			extended.draw(applet, node);
			break;
		case INTERFACE:
			interfaceRalationship.draw(applet,node);
			break;
		default:
			break;
		}
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
