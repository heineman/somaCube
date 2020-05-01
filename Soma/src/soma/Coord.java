package soma;

/**
 * A coordinate when modeling a Soma cube piece.
 * 
 * Sometime a Coordinate is a relative coordinate (when used when defining a Piece) but it can also 
 * be an absolute reference (to a place in the 3D state).
 */
public class Coord {
	public final int x;     // simpler code results by having Coord be immutable.
	public final int y;
	public final int z;
	
	/** 2D constructor defaults Z=0. */
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	/** 3D constructor. */
	public Coord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** Return a new coordinate resulting from the addition of the parameter. */
	public Coord add (Coord c) {
		return new Coord(x + c.x, y + c.y, z + c.z);
	}
	
	/** Get coordinate component for given axis, */
	public int get(Axis axis) {
		if (axis == Axis.X) { return x; }
		if (axis == Axis.Y) { return y; }
		return z;
	}

	/**
	 * The heart of the rotation is based on the simultaneous adjustment of two axes in response
	 * to a rotation.
	 * 
	 * Based on my hand-computations (and no other more complicated theories) I found that 
	 * rotating a piece clockwise in succession (around different axes) cause these pair of 
	 * values to change in predictable patterns:
	 * 
	 * (1, 0) -> (0, -1) -> (-1, 0) -> (1, 0) -> ... and back again
	 * 
	 * (-1, 1) -> (1, 1) -> (1, -1) -> (-1, -1) -> ... and back again
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	int[] updated(int a, int b) {
		// adjust [1,0] -> [0,-1] -> [-1, 0] -> [0,1] -> ...
		if (a == 1 && b == 0) { return new int[] { 0, -1}; }
		if (a == 0 && b == -1) { return new int[] { -1, 0}; }
		if (a == -1 && b == 0) { return new int[] { 0, 1}; }
		if (a == 0 && b == 1) { return new int[] { 1, 0}; }
		
		// now handle all with ones
		if (a == -1 && b == 1) { return new int[] { 1, 1}; }   // This actually works!
		if (a == 1 && b == 1) { return new int[] { 1, -1}; }
		if (a == 1 && b == -1) { return new int[] { -1, -1}; }
		if (a == -1 && b == -1) { return new int[] { -1, 1}; }
		
		throw new RuntimeException ("BAD COORDINATE! (" + a + "," + b + ")");   // never gets here
	}
	
	/** Return a new coordinate rotated clockwise around this axis. */
	public Coord rotateClockwise(Axis axis) {
		if (axis == Axis.X) { // Y and Z are rotating 
			int[] newOnes = updated(y, z);
			return new Coord(x, newOnes[0], newOnes[1]);
		}
		if (axis == Axis.Y) { // X and Z are rotating 
			int[] newOnes = updated(x, z);
			return new Coord(newOnes[0], y, newOnes[1]);
		}
		// must be Z
		int[] newOnes = updated(x, y);
		return new Coord(newOnes[0], newOnes[1], z);
	}
	
	/** Equals method for Coord. */
	public boolean equals (Object o) {
		if (o == null) { return false; }
		if (o instanceof Coord) {
			Coord other = (Coord) o;
			return x == other.x && y == other.y && z == other.z;
		}
		
		return false;
	}
	
	/** Debugging toString method. */
	public String toString() { return "(" + x + "," + y + "," + z + ")"; }
}
