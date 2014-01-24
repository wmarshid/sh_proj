package graspvis.model;


public interface Tree {
	public TreeNode getRoot();
	public TreeNode get(float x, float y);
	public TreeNode get(String id);
	public TreeNode get(TreeNode node);
	public void addChild(TreeNode parent, TreeNode child, RelationshipType type);
	public void highlight(TreeNode node);
	public void highlight(Node node);
	public void clear();
	public boolean isEmpty();
	public void unhighlight();
}
