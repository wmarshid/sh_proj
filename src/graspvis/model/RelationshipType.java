package graspvis.model;

public enum RelationshipType {
	CONNECTION,
	CONTAINMENT,
	RATIONALE,
	INSTANTIATION,
	CAUSALITY,
	EXTENSION,
	EXTENDED,
	INTERFACE,
	KIND,
	CONTAINED;
	
	private RelationshipType type;
	
	private RelationshipType() {
		type = this;
	}
	
	public float getLength() {
		switch (type) {
		case CONNECTION:
			return 70;
		case CONTAINMENT:
			return 50;
		case INSTANTIATION:
			return 50;
		case INTERFACE:
			return 15;
		case KIND:
			return 15;
		case CAUSALITY:
			return 50;
		case EXTENSION:
			return 75;
		default:
			return 50;
		}
	}
	
	public float getStiffness() {
		switch (type) {
		case CONNECTION:
			return 1;
		case CONTAINMENT:
			return 1;
		case INTERFACE:
			return 2f;
		default:
			return 1;
		}
	}
}
