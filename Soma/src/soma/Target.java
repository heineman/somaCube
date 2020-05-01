package soma;

/**
 * Designated target state.
 * 
 * Must be able to expose an Iterator<Coord> for its coordinates.
 */
public abstract class Target implements Iterable<Coord> {
	/**
	 * Is the given coordinate both safe and unused?
	 */
	public abstract boolean safe(Coord here);

	/** 
	 * Determine if coordinate is unused.
	 */
	public abstract boolean unused(Coord c);

	/**
	 * Claim this coordinate on behalf of the piece identified by id
	 */
	public abstract boolean occupy(Coord here, int id);

	/** Clear the given coordinate in target. */
	public abstract void clear(Coord here);
	
	/** 
	 * Place entire piece in the target, anchored at given coordinate, returning TRUE on success.
	 */
	public boolean place(Piece piece, Coord here) {
		Coord original = here;
		// Note this (x,y,z) is clear and is the assumed (0,0,0) starting point. Thereafter, all
		// coordinates are relative
		for (Coord c : piece) {
			here = here.add(c);
			if (!safe(here) || !unused(here)) { return false; }
		}

		// now PLACE since no more constraints.
		here = original;
		occupy(here, piece.id);

		for (Coord c : piece) {
			here = here.add(c);
			occupy(here, piece.id);
		}

		return true;
	}

	/** Remove place from the target. */
	public void unplace(Piece piece, Coord here) {
		clear(here);
		for (Coord c : piece) {
			here = here.add(c);
			clear(here);
		}
	}

	/** Return a key that might be useful to detect duplicate solutions. */
	public abstract String key();

}
