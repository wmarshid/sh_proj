package graspvis.model;

import processing.core.PVector;

public interface Node {
	public void setId(String id);
	public String getId();
	/**
	 * Returns node's label. The label follows three letters rule:
	 * <ol>
	 * <li>If the name of the element is 1 word, the first three letters.</li>
	 * <li>If the name of the element is 2 words, the two initial letters of each word.</li>
	 * <li>If the name of the element is 3 words, the three initial letters of each word.</li>
	 * </ol>
	 * @param node's label
	 */
	public void setLabel(String label);
	
	/**
	 * Gets the node's label
	 * @return node's label
	 */
	public String getLabel();
	
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
	
	public void setPosition(PVector position);
	public void setPosition(float x, float y);
	public void setPosition(float x, float y, float z);
	public PVector getPosition();
	
	public void setShapeColor(int color);
	public int getShapeColor();
	
	public void setLabelColor(int color);
	public int getLabelColor();
	
	public void setRadius(float radius);
	public float getRadius();
	
	public void setFlickerTheta(float theta);
	public float getFlickerTheta();
}
