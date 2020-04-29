package soma;

import java.util.HashSet;

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
	/**
	 * State is represented by a 3D matrix, where each entry is 0 if free, otherwise the id
	 * of the piece assigned to that cube.
	 */
	int state[][][] = new int[3][3][3];

	// how many solutions.
	int solutions = 0;
	int count = 0;

	Coord[] anchor;   // where each piece is placed.
	Orientation[] orientation;
	Rotation[] rotation;
	Piece[] pieces;

	public static void main(String[] args) {
		new State().search();
	}

	void search() {
		Piece[] actual = new Model().pieces;
		anchor = new Coord[actual.length+1];
		orientation = new Orientation[actual.length+1];
		rotation = new Rotation[actual.length+1];
		pieces = new Piece[actual.length+1];

		for (Piece p : actual) {
			pieces[p.id] = p;
		}

		for (int i = 1; i < pieces.length; i++) {
			explore (i);  // start exploring with ith piece. Once we hit END we are done.
		}
	}

	/** Returns FALSE if invalid or if occupied. */
	boolean safe(Coord here) {
		if (here.x < 0 || here.x > 2) { return false; }
		if (here.y < 0 || here.y > 2) { return false; }
		if (here.z < 0 || here.z > 2) { return false; }

		return state[here.x][here.y][here.z] == 0; 
	}
	static int max = 1;

	/** 
	 * Standard backtracking algorithm to find where to place piece. There are six orientations
	 * for each piece, and you can sweep each piece around clockwise in four rotations, thus there
	 * are a total of 24 possibilities for each piece.
	 *  
	 * Simply try each one, in any open space, and advance where possible until all seven pieces are placed.
	 * Don't even bother (for now) to check whether a particular orientation+rotation is possible. Just
	 * let the brute force proceed.
	 */
	void explore(int idx) {
		if (count++ % 1000000 == 0) {
			System.out.println(count);
		}
		if (idx > max) {
			max = idx;

			for (int p = 0; p < pieces.length; p++) {
				System.out.println(p + " @ " + anchor[p] + " at " + orientation[p] + " " + rotation[p]);
			}
			System.out.println();
			System.out.println(denseMap());
		}

		if (idx == pieces.length) {
			solutions++;
			for (int p = 1; p < pieces.length; p++) {
				System.out.println(p + " @ " + anchor[p] + " at " + orientation[p] + " " + rotation[p]);
			}
			System.out.println("Found at " + count);
			System.exit(1);
			return;
		}

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					// Move on if occupied.
					if (state[x][y][z] != 0) { continue; }

					// try each remaining piece
					for (int p = 1; p <pieces.length; p++) {
						if (anchor[pieces[p].id] != null) { continue; }

						// try each orientation for each piece. There are six faces to orient. Then there
						// are three axes to rotate. Then each piece is rotated one of four degrees for each
						// axis (0, 90, 180, 270). Finally, the spaces relative to the (x,y,z) position are
						// checked to see if the piece can be placed. If so, advance!
						for (Orientation o : Orientation.iterable()) {
							Piece oriented = pieces[p].orient(o);

							for (Axis a : Axis.values()) {                                            // for each axis
								Piece rotated = oriented;
								for (Rotation r : Rotation.iterable()) {                              // each of four possible rotations
									if (r.degree != 0) { rotated = rotated.rotateClockwise(a);  }   //    ... just keep rotating clockwise
									if (place(x, y, z, rotated, o, r)) {
										explore(idx+1);
										unplace(x, y, z, rotated);    // begin backtrack...
									} 
								}
							}
						}
					}
				}
			}
		}
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
	boolean place(int x, int y, int z, Piece piece, Orientation o, Rotation r) {
		// Note this (x,y,z) is clear and is the assumed (0,0,0) starting point. Thereafter, all
		// coordinates are relative
		Coord here = new Coord(x,y,z);
		if (!safe(here)) { return false; } // should never happen, but just be safe...

		for (Coord c : piece) {
			here = here.add(c);
			if (!safe(here)) { return false; }
		}

		// now PLACE since no more constraints.

		here = new Coord(x,y,z);
		anchor[piece.id] = here;
		state[here.x][here.y][here.z] = piece.id;

		for (Coord c : piece) {
			here = here.add(c);
			state[here.x][here.y][here.z] = piece.id;
		}
		orientation[piece.id] = o;
		rotation[piece.id] = r;
		return true;
	}

	void unplace(int x, int y, int z, Piece piece) {
		Coord here = new Coord(x,y,z);
		state[here.x][here.y][here.z] = 0;
		anchor[piece.id] = null;
		orientation[piece.id] = null;
		rotation[piece.id] = null;

		for (Coord c : piece) {
			here = here.add(c);
			state[here.x][here.y][here.z] = 0;
		}
	}

	public String denseMap() {
		StringBuffer sb = new StringBuffer("[");
		for (int x = 0; x < 3; x++) {
			sb.append("[");
			for (int y = 0; y < 3; y++) {
				sb.append("[");
				for (int z = 0; z < 3; z++) {
					sb.append(state[x][y][z]);
					if (z < 2) { sb.append(","); }
				}
				sb.append("]");
				if (y < 2) { sb.append(","); }
			}	
			sb.append("]");
			if (x < 2) { sb.append(","); }
		}
		return sb.append("]").toString();
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
