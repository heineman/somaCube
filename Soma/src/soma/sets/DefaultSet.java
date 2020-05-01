package soma.sets;

import java.util.ArrayList;
import java.util.Iterator;

import soma.Coord;
import soma.Piece;

public class DefaultSet implements Set {
	/** 
	 * Each Coord is *RELATIVE* to the prior one. (0,0) is assumed! Note that three-D pieces
	 * have a coordinate that "breaks the Z plane." 
	 */
	static Piece V = new Piece(1, new Coord(1,0), new Coord(0,1));
	static Piece L = new Piece(2, new Coord(1,0), new Coord(0,1), new Coord(0, 1));
	static Piece T = new Piece(3, new Coord(1,0), new Coord(1,0), new Coord(-1,1));     // not in a row!
	static Piece Z = new Piece(4, new Coord(1,0), new Coord(0,1), new Coord(1,0)); 
	
	static Piece A = new Piece(5, new Coord(1,0), new Coord(0, 1), new Coord(0,0,1));   // three D piece (Brown/Green)
	static Piece B = new Piece(6, new Coord(1,0), new Coord(0, 1), new Coord(0,0,-1));  // three D piece (Gray/Green)
	static Piece P = new Piece(7, new Coord(0,0,1), new Coord(0,1), new Coord(1,-1));   // three D piece
	
	ArrayList<Piece> pieces = null;
	
	@Override
	public Iterator<Piece> iterator() {
		if (pieces == null) {
			pieces = new ArrayList<Piece>();
			for (Piece p : new Piece[] { V, L, T, Z, A, B, P }) {
				pieces.add(p);
			}
		}

		return pieces.iterator();
		
	}

	@Override
	public int size() {
		return 7;
	}
	
}
