package graspvis.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import processing.core.PVector;

public class BasicTreeNode implements Highlight, TreeNode {

	// node's type
	private ElementType elementType;

	// relationship to node's child
	private Map<String, RelationshipType> relationshipTypes;

	private ArrayList<TreeNode> childs;

	private TreeNode parent;

	private String id;

	private String label;

	private PVector position;

	private float radius;

	private int shapeColor;

	private int labelColor;

	private boolean root = false;

	private boolean highlighted = false;

	private float flickerTheta = 0;

	public BasicTreeNode(String nodeId, String nodeLabel, ElementType type) {
		id = nodeId;
		label = nodeLabel;
		elementType = type;
		childs = new ArrayList<>();
		relationshipTypes = new HashMap<String, RelationshipType>();
		position = new PVector(Float.MAX_VALUE, Float.MAX_VALUE);
	}
	
	public BasicTreeNode() {
		id = "";
		label = "";
		elementType = null;
		childs = new ArrayList<>();
		relationshipTypes = new HashMap<String, RelationshipType>();
		position = new PVector(Float.MAX_VALUE, Float.MAX_VALUE);
	}
	
	@Override
	public void setRelationshipType(TreeNode child, RelationshipType type) {
		child.getParent().setRelationshipType(child, type);
	}

	@Override
	public RelationshipType getRelationshipType(TreeNode node) {
		return relationshipTypes.get(node.getId());
	}

	@Override
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isRoot() {
		return root;
	}

	@Override
	public void setAsRoot() {
		root = true;
	}

	@Override
	public void addChild(TreeNode child, RelationshipType type) {
		child.setParent(this);
		childs.add(child);
		relationshipTypes.put(child.getId(), type);
	}

	@Override
	public ArrayList<TreeNode> getChilds() {
		return childs;
	}

	@Override
	public int getChildCount() {
		return childs.size();
	}

	@Override
	public float getMaxChildRadius() {
		float maxRad = Float.MIN_VALUE;
		for (TreeNode child : childs) {
			if (child.getRadius() > maxRad) {
				maxRad = child.getRadius();
			}
		}
		if (maxRad == Float.MIN_VALUE) {
			maxRad = 0;
		}
		return maxRad;
	}

	@Override
	public void highlight() {
		highlighted = true;
	}

	@Override
	public void unhighlight() {
		highlighted = false;
	}

	@Override
	public boolean isHighlighted() {
		return highlighted;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setElementType(ElementType type) {
		this.elementType = type;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public void setPosition(PVector position) {
		this.position = position;
	}

	@Override
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	@Override
	public void setPosition(float x, float y, float z) {
		setPosition(x, y);
		position.z = z;
	}

	@Override
	public PVector getPosition() {
		return position;
	}

	@Override
	public void setShapeColor(int color) {
		shapeColor = color;
	}

	@Override
	public int getShapeColor() {
		return shapeColor;
	}

	@Override
	public void setLabelColor(int color) {
		labelColor = color;
	}

	@Override
	public int getLabelColor() {
		return labelColor;
	}

	@Override
	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public float getRadius() {
		return radius;
	}

	@Override
	public void setFlickerTheta(float theta) {
		flickerTheta = theta;
	}

	@Override
	public float getFlickerTheta() {
		return flickerTheta;
	}

}
