package soma.sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import soma.Coord;
import soma.Piece;

/**
 * A 3x3x3 cube can be built from:

The "T" (referred to as "natural color")
Either of the two 3-block pieces (black "V" OR white "I")
Any five (5) of the remaining six (6)  4-block pieces.

 * @author Laptop
 *
 */
public class BabylonCube implements Set {
	
	static Coord[] T = new Coord[] { new Coord(1,0), new Coord(1,0), new Coord(-1,1) };
	
	static Coord[] V = new Coord[] { new Coord(1,0), new Coord(0,1) };
	static Coord[] I = new Coord[] { new Coord(1,0), new Coord(1,0) };
	
	static Coord[] L = new Coord[] { new Coord(1,0), new Coord(0,1), new Coord(0, 1) };
	static Coord[] Z = new Coord[] { new Coord(1,0), new Coord(0,1), new Coord(1,0) }; 
	static Coord[] O = new Coord[] { new Coord(1,0), new Coord(0,1), new Coord(-1, 0) };    // 2x2 new piece
	
	static Coord[] A = new Coord[] { new Coord(1,0), new Coord(0, 1), new Coord(0,0,1) };   // three D piece (Brown/Green)
	static Coord[] B = new Coord[] { new Coord(1,0), new Coord(0, 1), new Coord(0,0,-1)};  // three D piece (Gray/Green)
	static Coord[] P = new Coord[] { new Coord(0,0,1), new Coord(0,1), new Coord(1,-1) };   // three D piece
	
		
	/**
	 * T + V + { L, Z, O, A, B }
	 * T + V + { L, Z, O, A, P }
	 * T + V + { L, Z, O, B, P }
	 * T + V + { L, Z, A, B, P }
	 * T + V + { L, O, A, B, P }
	 * T + V + { Z, O, A, B, P }
	 * 
	 * T + I + { L, Z, O, A, B }
	 * T + I + { L, Z, O, A, P }
	 * T + I + { L, Z, O, B, P }
	 * T + I + { L, Z, A, B, P }
	 * T + I + { L, O, A, B, P }
	 * T + I + { Z, O, A, B, P }
	 * 
	 */
	static ArrayList<Coord[]> full = new ArrayList<>(Arrays.asList(L, Z, O, A, B, P));

	/** Specify a variation. */
	public static BabylonCube variation(int var) {
		if (var < 0 || var > 12) { throw new IllegalArgumentException("Only 12 possible variations."); }
		
		ArrayList<Piece> selectedSet = new ArrayList<>();
		selectedSet.add(new Piece(1, T));
		if (var <= 6) { 
			selectedSet.add(new Piece(2, V));
		} else {
			selectedSet.add(new Piece(2, I));
		}
		
		// choose five
		int pid = 3;
		for (int i = 0; i < full.size(); i++) {
			if (i != (var-1) % 6) {
				selectedSet.add(new Piece(pid++, full.get(i)));
			}
		}
		
		return new BabylonCube(selectedSet);
	}
	
	ArrayList<Piece> pieces = null;
	
	BabylonCube(ArrayList<Piece> selected) {
		this.pieces = new ArrayList<>();
		pieces.addAll(selected);
	}
	
	@Override
	public Iterator<Piece> iterator() {
		return pieces.iterator();
	}

	@Override
	public int size() {
		return 7;
	}
}
