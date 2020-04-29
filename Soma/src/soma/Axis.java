package soma;

/**
 * Represent one of the 3-dimensional Axes. 
 */
public enum Axis {

	X(0), Y(1), Z(2);
	
	final int id;
	
	Axis(int id) {
		this.id = id;
	}
	
	/** Return out id. */
	public int id() { return id; }
	
	/** Return other axis affected. */
	public Axis[] other() {
		if (this == X) { return new Axis[] { Y, Z}; }
		if (this == Y) { return new Axis[] { X, Z}; }
		return new Axis[] { X, Y };
	}
}
