package graspvis.model;

public interface Select {
	/**
	 * Selects the node
	 */
	public void select();
	
	/**
	 * Deselect the node
	 */
	public void deselect();
	
	/**
	 * Returns the selection state of the node
	 * @return selected
	 */
	public boolean isSelected();
}
