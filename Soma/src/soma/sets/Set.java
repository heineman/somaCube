package soma.sets;

import soma.Piece;

public interface Set extends Iterable<Piece> { 

	/** Return number of pieces. */
	int size();
}
