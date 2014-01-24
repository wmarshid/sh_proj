package graspvis.model;


public interface Spring {
	/**
	 * Sets id of the spring.
	 * @param spring id
	 */
	public void setId(String id);
	
	/**
	 * Returns spring id
	 * @return spring id
	 */
	public String getId();
	
	/**
	 * Returns spring label 
	 * @param Spring's label
	 */
	public void setLabel(String label);
	
	/**
	 * Gets the Spring's label
	 * @return Spring's label
	 */
	public String getLabel();
	
	/**
	 * Sets the relationship type
	 * 
	 * @param type
	 */
	public void setRelationshipType(RelationshipType type);
	
	/**
	 * Gets relationship type
	 * 
	 * @return the relationship type
	 */
	public RelationshipType getRelationshipType();
	
	/**
	 * Sets from node
	 * @param From node
	 */
	public void setFromNode(Node node);
	
	/**
	 * Gets from node
	 * @return From node
	 */
	public Node getFromNode();
	
	public void setHookFrom(Node hookFrom);

	public Node getHookFrom();
	
	/**
	 * Sets to node
	 * @param To node
	 */
	public void setToNode(Node node);
	
	/**
	 * Gets to node
	 * @return To node
	 */
	public Node getToNode();
	
	public void setHookTo(Node hooTo);

	public Node getHookTo();
	
	/**
	 * Sets the length
	 * @param length
	 */
	public void setLength(float length);
	
	/**
	 * Gets the length
	 * @return length
	 */
	public float getLength();
	
	/**
	 * Sets stiffness of the spring
	 * @param stiffness
	 */
	public void setStiffness(float stiffness);
	
	/**
	 * Returns the stiffness ot the spring
	 * @return stiffness
	 */
	public float getStiffness();
	
	public void setDamping(float damping);
	
	public float getDamping();
	
//	/**
//	 * Sets node to be invisible
//	 * @param visible
//	 */
//	public void setVisible(boolean visible);
	
//	/**
//	 * Returns the visibility status
//	 * @return true if visible otherwise false
//	 */
//	public boolean isVisible();
}
