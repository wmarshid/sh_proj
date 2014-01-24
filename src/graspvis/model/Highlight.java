package graspvis.model;

public interface Highlight {
	public void setId(String id);
	public String getId();
	public void highlight();
	public void unhighlight();
	public boolean isHighlighted();
}
