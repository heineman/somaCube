package soma;

import java.util.ArrayList;

/**
 * Used primarily for human-readability of solutions.
 * 
 * Records the degree of rotation (either 0, 90, 180, or 270).
 */
public class Rotation {
	final int degree;  // + is up, - is down
	final Piece piece;
	
	public Rotation (Piece piece, int degree) {
		this.degree = degree;
		this.piece = piece;
	}
		
	static ArrayList<Rotation> values = null;
	
	/** Return all four rotations in order. */
	public static Iterable<Rotation> rotations(Piece p, Axis axis) {
		ArrayList<Rotation> rotations = new ArrayList<>();
		rotations.add(new Rotation(p, 0));       // no rotations
		p = p.rotateClockwise(axis);   
		rotations.add(new Rotation(p, 90));
		p = p.rotateClockwise(axis);   
		rotations.add(new Rotation(p, 180));
		p = p.rotateClockwise(axis);  
		rotations.add(new Rotation(p, 270));
		return rotations;
	}

	/** Debugging toString. */
	public String toString() { return "" + degree; }
}
