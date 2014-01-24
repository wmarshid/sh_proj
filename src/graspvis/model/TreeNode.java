package graspvis.model;

import java.util.ArrayList;

public interface TreeNode extends Node {
	/**
	 * Sets the element type
	 * 
	 * @param type
	 */
	public void setElementType(ElementType type);
	
	/**
	 * Gets element type
	 * 
	 * @return the element type
	 */
	public ElementType getElementType();
	
	/**
	 * Sets the relationship type
	 * 
	 * @param type
	 */
	public void setRelationshipType(TreeNode child, RelationshipType type);
	
	/**
	 * Gets relationship type 
	 * 
	 * @return the element type
	 */
	public RelationshipType getRelationshipType(TreeNode node);
	
	public void setParent(TreeNode parent);
	
	public TreeNode getParent();
	
	public boolean isRoot();
	
	public void setAsRoot();
	
	public void addChild(TreeNode child, RelationshipType type);
	
	public ArrayList<TreeNode> getChilds();
	
	public int getChildCount();
	
	public float getMaxChildRadius();
}
