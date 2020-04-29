package soma;

/** 
 * Builds all pieces
 * 
 * https://en.wikipedia.org/wiki/Soma_cube
 */
public class Model  {
	
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
	
	Piece[] pieces = new Piece[] { V, L, T, Z, A, B, P };

}
