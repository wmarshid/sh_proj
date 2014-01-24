package graspvis.model;

public enum ElementType {
	COMPONENT,
	CONNECTOR,
	LAYER,
	RATIONALE,
	TEMPLATE, 
	LINK,
	PROVIDES,
	REQUIRES,
	KIND;
	
	private ElementType type;
	
	private ElementType() {
		type = this;
	}
	
	public float getSize() {
		switch (type) {
		case COMPONENT:
			return 16;
		case CONNECTOR:
			return 16;
		case LAYER:
			return 16;
		case PROVIDES:
		case REQUIRES:
			return 8;
		case TEMPLATE:
			return 28;
		case RATIONALE:
			return 16;
		default:
			return 16;
		}
	}
}
