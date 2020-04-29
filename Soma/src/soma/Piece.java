package soma;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Each Soma piece has an Anchor (the one we try to place using absolute coordinates)
 * and then all other subcubes in the piece are described in relative position from
 * this first one.  The anchor is assumed, and all remaining coordinates are relative in response
 * to this anchor.
 * 
 * In this way, the piece is simply defined, and the orientation of the piece (six different
 * possibilities) is determined by the one trying to place the pieces. Note, even though the
 * pieces are connected, this isn't necessary (except in the real world). Also the coordinates
 * do not have to form a continuous "line" since they only represent the filled cubes (and also 
 * some pieces cannot have linear paths, like the T and P pieces.
 */
public class Piece implements Iterable<Coord> {

	final ArrayList<Coord> coords = new ArrayList<>();		// sequence of coordinates (0,0) is assumed as first.
	final int id;											// each piece has unique id, suitable for storing in state

	/** Form a coordinate by the associated coordinates but keep the same id. */
	public Piece (int pid, Coord... inCoords) {
		this.id = pid;
		for (Coord c : inCoords) {
			coords.add(c);
		}
	}

	public Iterator<Coord> iterator() { return coords.iterator(); }
	public int getId() { return id; }
	public int size() { return coords.size(); }

	/** Rotate a piece around a given axis. */
	public Piece rotateClockwise(Axis fixedAxis) {
		return Orientation.rotateClockwise(this, fixedAxis);
	}

	/** 
	 * Orient piece (from its initial fixed Y+) to new one. 
	 * 
	 * These unique transformations were identified by hand.
	 */
	public Piece orient(Orientation o) {
		switch (o.axis) {
		case X:
			if (o.sign == -1) return rotateClockwise(Axis.Z);                                  // Y+ -> X- needs XY-90 rotation
			return rotateClockwise(Axis.Z).rotateClockwise(Axis.Z).rotateClockwise(Axis.Z);    // Y+ -> X+ needs XY-270 rotation
		case Y:
			if (o.sign == 1) return this;                                                      // Y+ -> Y+ is identity
			return rotateClockwise(Axis.Y).rotateClockwise(Axis.Y);                            // Y+ -> Y- is XZ-180 rotation
		default:  /* Z */
			if (o.sign == -1) return rotateClockwise(Axis.Y);                                  // Y+ -> Z- is XZ-90 rotation
			return rotateClockwise(Axis.Y).rotateClockwise(Axis.Y).rotateClockwise(Axis.Y);    // Y+ -> Z+ is XZ-270 rotation
		}
	}

	/** Are two shapes equal to each other (by coordinates). */
	public boolean equals (Object o) {
		if (o == null) { return false; }
		if (o instanceof Piece) {
			Piece other = (Piece) o;

			Iterator<Coord> me = iterator();
			Iterator<Coord> it = other.iterator();

			while (me.hasNext() && it.hasNext()) {
				if (!me.next().equals(it.next())) { return false; }
			}

			boolean same = me.hasNext() == it.hasNext();
			return same;
		}

		return false;
	}

	/** Debugging toString. */
	public String toString() {
		String start = new Coord(0,0).toString();
		for (Coord c : this) {
			start += " " + c.toString();
		}
		return start;
	}

}
