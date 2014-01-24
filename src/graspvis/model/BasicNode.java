package graspvis.model;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Drawable nodes.Force-directed functionalities adopted from 
 * {@link http://www.generative-gestaltung.de/code} by Bohnacker, et. al (2012).
 * @author nauval
 *
 */
public class BasicNode implements Node, ForceDirectedNode, Highlight, Select{

	private static final int MAX_VELOCITY = 10;
	private boolean selected = false;
	private boolean highlighted = false;
	
	private float minX;
	private float minY;
	private float maxX;
	private float maxY;
	private float minZ;
	private float maxZ;
	
	
	private String id;
	private String label;
	
	private int shapeColor;
	private int labelColor;
	
	private float flickerTheta = 0;
	private float radius;
	private float impactRadius = 300;
	private float damping = 0.5f;
	private float ramp = 0.5f;
	private float strength = -5;
	
	private ElementType elementType;
	
	private PVector position;
	private PVector velocity;
	
	public BasicNode(String id) {
		this.id = id;
		position = new PVector(0, 0, 0);
		this.elementType = ElementType.COMPONENT;
	}

	public BasicNode(String id, float x, float y) {
		this.id = id;
		position = new PVector(x, y, 0);
		this.elementType = ElementType.COMPONENT;
		initNode();
	}

	public BasicNode(String id, float x, float y, ElementType type) {
		this.id = id;
		position = new PVector(x, y, 0);
		this.elementType = type;
		initNode();
	}
	
	public BasicNode(String id, float x, float y, float z, ElementType type) {
		this.id = id;
		position = new PVector(x, y, z);
		this.elementType = type;
		initNode();
	}

	public BasicNode(String id, PVector position, ElementType type) {
		this.id = id;
		this.position = new PVector(position.x, position.y, position.z);
		this.elementType = type;
		initNode();
	}
	
	private void initNode() {
		velocity = new PVector(0, 0, 0);
		shapeColor = 0;
		labelColor = 230;
		
		flickerTheta = 0;
		radius = 10;
		impactRadius = 300;
		damping = 0.5f;
		ramp = 0.5f;
		strength = -5;
	}
	
	@Override
	public void select() {
		selected = true;
	}

	@Override
	public void deselect() {
		selected = false;
	}

	@Override
	public boolean isSelected() {
		return selected;
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
	public void setVelocity(PVector velocity) {
		
	}

	@Override
	public void setVelocity(float x, float y) {
		velocity.x = x;
		velocity.y = y;
	}

	@Override
	public void setVelocity(float x, float y, float z) {
		velocity.x = x;
		velocity.y = y;
		velocity.z = z;
	}

	@Override
	public PVector getVelocity() {
		return velocity;
	}

	@Override
	public void update() {
		if (elementType == ElementType.REQUIRES || elementType == ElementType.PROVIDES) {
			return;
		}
		update(false, false, false);
	}
	
	public void update(boolean theLockX, boolean theLockY, boolean theLockZ) {

		velocity.limit(MAX_VELOCITY);

		if (!theLockX) position.x += velocity.x;
		if (!theLockY) position.y += velocity.y;
		if (!theLockZ) position.z += velocity.z;

		if (position.x < minX) {
			position.x = minX - (position.x - minX);
			velocity.x = -velocity.x;
		}
		if (position.x > maxX) {
			position.x = maxX - (position.x - maxX);
			velocity.x = -velocity.x;
		}

		if (position.y < minY) {
			position.y = minY - (position.y - minY);
			velocity.y = -velocity.y;
		}
		if (position.y > maxY) {
			position.y = maxY - (position.y - maxY);
			velocity.y = -velocity.y;
		}

		if (position.z < minZ) {
			position.z = minZ - (position.z - minZ);
			velocity.z = -velocity.z;
		}
		if (position.z > maxZ) {
			position.z = maxZ - (position.z - maxZ);
			velocity.z = -velocity.z;
		}

		velocity.mult(1 - damping);
	}

	@Override
	public void attract(Node[] nodes) {
		// attraction or repulsion part
		for (int i = 0; i < nodes.length; i++) {
			ForceDirectedNode otherNode = (ForceDirectedNode) nodes[i];
			// stop when empty
			if (otherNode == null)
				break;
			// not with itself
			if (otherNode == this)
				continue;

			attract(otherNode);
		}
	}
	
	@Override
	public void attract(ForceDirectedNode[] nodes) {
		// attraction or repulsion part
		for (int i = 0; i < nodes.length; i++) {
			ForceDirectedNode otherNode = nodes[i];
			// stop when empty
			if (otherNode == null)
				break;
			// not with itself
			if (otherNode == this)
				continue;

			attract(otherNode);
		}
	}
	
	public void attract(ForceDirectedNode node) {
		float d = PVector.dist(position, node.getPosition());
		
		if (elementType == ElementType.REQUIRES || elementType == ElementType.PROVIDES) 
			return;
		
		if (d > 0 && d < impactRadius) {
			float s = PApplet.pow(d / impactRadius, 1 / ramp); // could be considered as size or mass
			float f = s * 9 * strength * (1 / (s + 1) + ((s - 3) / 4)) / d;
			PVector df = PVector.sub(position, node.getPosition());
			df.mult(f);
			
			node.getVelocity().x += df.x;
			node.getVelocity().y += df.y;
			node.getVelocity().z += df.z;
		}
	}

	@Override
	public void setBoundary(float minX, float minY, float maxX, float maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	@Override
	public void setStrength(float strength) {
		this.strength = strength;
	}

	@Override
	public float getStrength() {
		return strength;
	}

	@Override
	public void setImpactRadius(float radius) {
		this.impactRadius = radius;
	}

	@Override
	public float getImpactRadius() {
		return impactRadius;
	}

	@Override
	public void setDamping(float damping) {
		this.damping = damping;
	}

	@Override
	public float getDamping() {
		return damping;
	}

	@Override
	public void setRamp(float ramp) {
		this.ramp = ramp;	
	}

	@Override
	public float getRamp() {
		return ramp;
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
		this.position.x = x;
		this.position.y = y;
	}

	@Override
	public void setPosition(float x, float y, float z) {
		setPosition(x, y);
		this.position.z = z;
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
