package soma;

import java.util.HashSet;

import soma.sets.Set;

/**
 * Conduct a standard backtracking search to place the Soma Pieces.

 Result  

V	1 @ (0,0,0) at Y+ 0
L	2 @ (0,0,1) at Y+ 270
T	3 @ (2,0,2) at X+ 270
Z	4 @ (1,0,2) at X- 270
A	5 @ (0,1,0) at X+ 90
B	6 @ (0,1,1) at Y+ 270
P	7 @ (1,2,0) at Z- 180

23000001	2020-04-28 16:22:11.993033627 
24000001	2020-04-28 16:22:58.464969649
 1000000 every 47 seconds

1 @ (0,0,0) at Y+ 0
2 @ (0,0,1) at Y+ 270
3 @ (2,0,2) at X+ 270
4 @ (1,0,2) at X- 270
5 @ (0,1,0) at X+ 90
6 @ (0,1,1) at Y+ 270
7 @ (1,2,0) at Z- 180
Found at 28559497

[[[1,2,2],[5,6,2],[5,5,2]],
 [[1,4,4],[1,6,6],[7,5,6]],
 [[4,4,3],[7,3,3],[7,7,3]]]

 */
public class State {
	final Target target;
	final int numSolutions;
	
	Coord[] anchor;   // where each piece is placed.
	Orientation[] orientation;
	Rotation[] rotation;
	Set set;
	Piece[] pieces;

	static HashSet<String> allSolutions = new HashSet<>();

	public State (Target t, Set set) {
		this(t, set, 0);
	}
	
	public State (Target t, Set set, int numSolutions) {
		this.target = t;
		this.set = set;
		this.numSolutions = numSolutions;
	}
	
	void search() {
		int actualSize = set.size();
		anchor = new Coord[actualSize+1];
		orientation = new Orientation[actualSize+1];
		rotation = new Rotation[actualSize+1];
		pieces = new Piece[actualSize+1];

		for (Piece p : set) {
			pieces[p.id] = p;
		}

		explore (0);
	}

	int count = 0;

	/** 
	 * Standard backtracking algorithm to find where to place piece. There are six orientations
	 * for each piece, and you can sweep each piece around clockwise in four rotations, thus there
	 * are a total of 24 possibilities for each piece.
	 *  
	 * Simply try each one, in any open space, and advance where possible until all seven pieces are placed.
	 * Don't even bother (for now) to check whether a particular orientation+rotation is possible. Just
	 * let the brute force proceed.
	 * 
	 * Return TRUE to terminate recursion after the last desired solution was found.
	 */
	boolean explore(int idx) {
		// generate a pulse, roughly every 50 seconds. Not truly "# of configurations checked" but useful.
		if (count++ % 1000000 == 0) {
			System.out.println(count + "...");
		}
		
		if (idx == pieces.length - 1) {
			String key = key();
			if (!allSolutions.contains(key)) {
				allSolutions.add(key);

				System.out.println(key);
				for (int p = 1; p < pieces.length; p++) {
					System.out.println(p + " @ " + anchor[p] + " at " + orientation[p] + " " + rotation[p]);
				}
				return allSolutions.size() >= numSolutions;
			}
		}

		for (Coord c : target) {
			// Move on if occupied.
			if (!target.unused(c)) { continue; }

			// try each remaining piece
			for (int p = 1; p <pieces.length; p++) {
				if (anchor[pieces[p].id] != null) { continue; }

				// try each orientation for each piece. There are six faces to orient. Then there
				// are three axes to rotate. Then each piece is rotated one of four degrees for each
				// axis (0, 90, 180, 270). Finally, the spaces relative to the (x,y,z) position are
				// checked to see if the piece can be placed. If so, advance!
				for (Orientation o : Orientation.iterable()) {
					Piece oriented = pieces[p].orient(o);

					for (Axis a : Axis.values()) {                                   // for each axis
						for (Rotation rotated: Rotation.rotations(oriented, a)) {    //   ... each possible rotations
							if (place(rotated.piece, c, o, rotated)) {         //   ... try to place piece
								if (explore(idx+1)) {
									return true;  // continue terminating recursion if requested
								}
								unplace(rotated.piece, c);                     // begin backtrack...
							} 
						}
					}
				}
			}
		}
		
		return false;  // if we get here, we have nothing to add...
	}

	/** 
	 * Given a piece, an orientation (6) and a rotation (4) see if it fits, given the available
	 * open spaces.
	 * 
	 * @param piece
	 * @param o
	 * @param r
	 * @return
	 */
	boolean place(Piece piece, Coord here, Orientation o, Rotation r) {
		if (!target.place(piece, here)) { return false; }
		
		anchor[piece.id] = here;
		orientation[piece.id] = o;
		rotation[piece.id] = r;
		return true;
	}

	void unplace(Piece piece, Coord here) {
		anchor[piece.id] = null;
		orientation[piece.id] = null;
		rotation[piece.id] = null;
		target.unplace(piece, here);
	}

	/** Generate a KEY to uniquely identify the state. Note: not unique with regards to 2x2x3=24 rotations. */
	public String key() {
		return target.key();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int p = 1; p <= pieces.length; p++) {
			if (anchor[p] != null) {
				sb.append(p + " @ " + anchor[p] + " at " + orientation[p] + " " + rotation[p] + "\n");
			}
		}
		return sb.toString();
	}


}
