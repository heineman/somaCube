package soma;

import java.util.ArrayList;

/**
 * Used primarily for human-readibility of solutions.
 * 
 * Records the degree of rotation (either 0, 90, 180, or 270).
 */
public class Rotation {
	final int degree;  // + is up, - is down
	
	public Rotation (int degree) {
		this.degree = degree;
	}
		
	static ArrayList<Rotation> values = null;
	
	/** Return all four rotations in order. */
	public static Iterable<Rotation> iterable() {
		if (values == null) {
			values = new ArrayList<>();
			values.add(new Rotation(0));
			values.add(new Rotation(90));
			values.add(new Rotation(180));
			values.add(new Rotation(270));
		}
		
		return values;
	}

	/** Debugging toString. */
	public String toString() { return "" + degree; }
}
