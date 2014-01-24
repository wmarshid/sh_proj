package graspvis.model;

import processing.core.PVector;

public interface ForceDirectedNode {
	public void setPosition(PVector position);
	public void setPosition(float x, float y);
	public void setPosition(float x, float y, float z);
	public PVector getPosition();
	
	public void setVelocity(PVector velocity);
	public void setVelocity(float x, float y);
	public void setVelocity(float x, float y, float z);
	public PVector getVelocity();
	
	public void update();
	
	public void attract(Node[] nodes);
	public void attract(ForceDirectedNode[] nodes);
	
	public void setBoundary(float minX, float minY, float maxX, float maxY);
	
	public void setStrength(float strength);
	public float getStrength();
	
	public void setImpactRadius(float radius);
	public float getImpactRadius();
	
	public void setDamping(float damping);
	public float getDamping();
	
	public void setRamp(float ramp);
	public float getRamp();
}
