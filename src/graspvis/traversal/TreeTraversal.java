package graspvis.traversal;

import grasp.lang.IArchitecture;
import graspvis.exception.AlgorithmException;
import graspvis.model.BasicTree;
import graspvis.model.BasicTreeNode;
import graspvis.model.ElementType;
import graspvis.model.Graph;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.Spring;
import graspvis.model.Tree;
import graspvis.model.TreeNode;


public class TreeTraversal implements Traversal {
	private final int INTERFACE_RADIUS_SCALE_FACTOR = 3;
	private Graph graph;

	
	public TreeTraversal(Graph graph) {
		this.graph = graph;
	}
	
	private TreeNode createTreeNode(Node node) {
		TreeNode n = new BasicTreeNode(node.getId(), node.getLabel(), node.getElementType());
		if (node.getElementType() == ElementType.PROVIDES 
				|| node.getElementType() == ElementType.REQUIRES) {
			n.setRadius(node.getRadius()*INTERFACE_RADIUS_SCALE_FACTOR);
		} else {
			n.setRadius(node.getRadius());
		}
		
		n.setShapeColor(node.getShapeColor());
		n.setLabelColor(node.getLabelColor());
		return n;
	}
	
	private void buildTree(TreeNode node, int level) throws AlgorithmException{
		if (level > 0) {
			Node n = graph.getNode(node.getId());
			if (n == null) {
				throw new AlgorithmException("Can not find node " + node.getId());
			}
			
			// get nodes connected from the node
			for (Spring s : graph.getSprings()) {
				Node fromNode = s.getFromNode();
				Node toNode = s.getToNode();
				
				// add to node to the tree  
				if (s.getFromNode().getId().equals(n.getId())) {
					TreeNode child = createTreeNode(toNode);
					// add child node to theNode
					if (s.getRelationshipType() == RelationshipType.INTERFACE) {
						node.addChild(child, RelationshipType.CONTAINMENT);
					} else {
						node.addChild(child, s.getRelationshipType());
					}
					//toNode.setVisited(true);
					// go to next level
					buildTree(child, level - 1);
				}
				
				// add from node to the tree 
				if (s.getToNode().getId().equals(n.getId())) {
					TreeNode child = createTreeNode(fromNode);
					child.setRadius(fromNode.getRadius());
					
					if (s.getRelationshipType() == RelationshipType.CONTAINMENT 
							|| s.getRelationshipType() == RelationshipType.INTERFACE) {
						node.addChild(child, RelationshipType.CONTAINED);
					} else if (s.getRelationshipType() == RelationshipType.EXTENSION) {
						node.addChild(child, RelationshipType.EXTENDED);
					}else {
						node.addChild(child, s.getRelationshipType());
					}
					//fromNode.setVisited(true);
					// go to next level
					buildTree(child, level - 1);
				}
			}
		}
	}
	
	/**
	 * Not used in this class
	 */
	@Override
	public void setArchitecture(IArchitecture theArchitecture) {
	}

	/**
	 * Not used in this class
	 */
	@Override
	public Graph traverse() {
		return null;
	}

	@Override
	public Tree traverse(Node node, int level) throws AlgorithmException {
		if (graph == null) {
			throw new AlgorithmException("Graph is null");
		}
		Tree tree = new BasicTree(createTreeNode(node));
		//tree.setAsRoot();
		buildTree(tree.getRoot(), level);
		return tree;
	}
	
	@Override
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	public Graph getGraph() {
		return graph;
	}

}
