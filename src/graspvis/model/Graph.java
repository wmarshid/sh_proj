package graspvis.model;


public interface Graph {
	/**
	 * Sets nodes
	 * @param nodes
	 */
	public void setNodes(Node[] nodes);
	
	/**
	 * Returns the nodes of the graph
	 * @return The nodes
	 */
	public Node[] getNodes();
	
	/**
	 * Sets springs
	 * @param springs
	 */
	public void setSprings(Spring[] springs);
	
	/**
	 * Returns springs of the graph
	 * @return The springs
	 */
	public Spring[] getSprings();
	
	/**
	 * Clears all the nodes and springs
	 */
	public void clear();

	public void addNode(Node node);
	
	public void addNode(String id, String label, ElementType type);
	
	public void removeNode(String id);

//	public void setUnvisited(Node unvisitedNode);
//
//	public void setVisited(Node visitedNode);

	public void addSpring(String id, Node from, Node to,
			RelationshipType type);
	
	public void addSpring(String id, Node from, Node hookFrom, 
			Node to, Node hookTo, RelationshipType type);
	
	public void addSpring(String id, String fromId, String toId, RelationshipType type);
	
	public void removeSpring(String id);

	public Node getNode(String id);
	
	public Node getNode(float x, float y);
	
	public Spring getSpring(String id);
	
	public void highlight(Highlight node);

	public void unhighlight();
	
	public void setSpringLength(RelationshipType type, float length);
}
