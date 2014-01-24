package graspvis.model;

import processing.core.PApplet;

public class BasicTree implements Tree {

	private TreeNode root;
	private TreeNode result;
	
	public BasicTree() {
		this.root = new BasicTreeNode();
	}
	
	public BasicTree(TreeNode root) {
		this.root = root;
		root.setAsRoot();
	}
	
	@Override
	public TreeNode getRoot() {
		return root;
	}

	@Override
	public TreeNode get(float x, float y) {
		return search(x, y);
	}

	@Override
	public TreeNode get(String id) {
		result = null;
		search(root, id);
		return result;
	}

	@Override
	public TreeNode get(TreeNode node) {
		return get(node.getId());
	}

	@Override
	public void addChild(TreeNode parent, TreeNode child, RelationshipType type) {
		parent.addChild(child, type);
	}

	@Override
	public void highlight(TreeNode node) {
		highlight(root, node.getId());
	}

	@Override
	public void highlight(Node node) {
		highlight(root, node.getId());
	}

	@Override
	public void clear() {
		root = new BasicTreeNode();
	}

	@Override
	public boolean isEmpty() {
		return root.getId().length() > 0? false : true;
	}

	@Override
	public void unhighlight() {
		unhighlight(root);
	}

	private TreeNode search(float x, float y) {
		result = null;
		search(root, x, y);
		return result;
	}
	
	private void search(TreeNode node, float x, float y) {
		if (node == null) {
			return;
		}
		float maxDist = 20;
		float r = PApplet.dist(node.getPosition().x, node.getPosition().y, x, y);
		if (r < maxDist) {
			result = node;
			maxDist = r;
			return;
		}
		for (TreeNode child : node.getChilds()) {
			search(child, x, y);
		}
	}

	private void search(TreeNode node, String id) {
		if (node == null) {
			return;
		}
		if (node.getId().equals(id)) {
			result = node;
			return;
		}
		for (TreeNode n : node.getChilds()) {
			search(n, id);
		}
	}
	
	private void highlight(TreeNode node, String id) {
		Highlight h = (Highlight) node;
		if (node.getId().equals(id)) {
			h.highlight();
		} else {
			h.unhighlight();
		}
		for (TreeNode n : node.getChilds()) {
			highlight(n, id);
		}
	}
	
	private void unhighlight(TreeNode node) {
		Highlight h = (Highlight) node;
		h.unhighlight();
		for (TreeNode n : node.getChilds()) {
			unhighlight(n);
		}
	}
	
}
