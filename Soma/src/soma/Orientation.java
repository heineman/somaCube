package soma;

import java.util.ArrayList;

/**
 * The anchor for each piece (0,0) is assumed. Can appear in one of six orientations.
 * For all pieces defined in the model, this orientation is defined as Y+ that is, in the Y direction (up).
 * 
 * This was chosen because placing a piece on the table, y coordinate is up, x is left and right, z is back and front.
 * 
 * All six positions are:
 * 
 * Y+    baseline                                          Can rotate XZ   THIS IS BASELINE NORMAL POSITION
 * Y-    180 degrees rotation on YZ axis                   Can rotate XZ
 * X-    90 degree counter-clockwise rotation on XY axis   Can rotate YZ
 * X+    90 degree clockwise rotation on XY axis           Can rotate YZ
 * Z+    90 degree clockwise rotation on YZ axis           Can rotate XY
 * Z-    90 degree counter-clockwise rotation on YZ axis   Can rotate XY
 */
public class Orientation {
	final Axis axis;
	final int sign;  // + is up, - is down
	
	public Orientation (Axis axis, int sign) {
		this.axis = axis;
		if (sign < 0) { this.sign = -1; }
		else if (sign > 0) { this.sign = 1; }
		else { this.sign = 0; }
	}
		
	public Orientation(Orientation orientation) {
		this.axis = orientation.axis;
		this.sign = orientation.sign;
	}

	static ArrayList<Orientation> values = null;
	
	/** Return all six orientations. */
	public static Iterable<Orientation> iterable() {
		if (values == null) {
			values = new ArrayList<>();
			values.add(new Orientation(Axis.Y, +1));
			values.add(new Orientation(Axis.Y, -1));
			values.add(new Orientation(Axis.X, -1));
			values.add(new Orientation(Axis.X, +1));
			values.add(new Orientation(Axis.Z, -1));
			values.add(new Orientation(Axis.Z, +1));
		}
		
		return values;
	}
	
	/** 
	 * The fundamental operation for this entire project! Transform the original piece
	 * (which was in orientation Y+ and rotation 0) into a new piece with transformed coordinates.
	 * 
	 * The key insight (See handout) was the observation that when rotating on a specific axis (like XZ)
	 * the coordinates undergo a consistent transformation:
	 * 
	 * Piece: Here I have filled in the z coordinate even when not needed
	 * 
	 *   ASSUMED
	 *   (0,0,0) -> (1, 0, 0) -> (0,1,0) -> (0,  0,-1)         Y+ and XZ-rotation of 0
	 *   (0,0,0) -> (0, 0,-1) -> (0,1,0) -> (-1, 0, 0)         Y+ and XZ-rotation of 90 clockwise
	 *   (0,0,0) -> (-1,0, 0) -> (0,1,0) -> (0,  0, +1)        Y+ and XZ-rotation of 180 clockwise
	 *   (0,0,0) -> (0, 0, 1) -> (0,1,0) -> (1,  0, 0)         Y+ and XZ-rotation of 270 clockwise
	 *                   
	 *     ^^^                      ^^^
	 *     
	 * With the XZ rotation on this piece in Y+ orientation, all coordinates that have x=0 and z=0 are untouched.
	 * The others have their respective X/Z coordinates adjust in the sequence (-1 -> 0 -> +1 -> 0) but you have
	 * to know how to start. Do this by finding the non-zero coordinate and use that to drive the decision.
	 * 
	 * What about for the "T" piece?
	 * 
	 *   ASSUMED
	 *   (0,0,0) -> (1, 0, 0) -> (1, 0, 0)  -> (-1,1, 0)          Y+ and XZ-rotation of 0
	 *   (0,0,0) -> (0, 0,-1) -> (0, 0, -1) -> (0, 1, 1)          Y+ and XZ-rotation of 90 clockwise
	 *   (0,0,0) -> (-1,0, 0) -> (-1,0, 0)  -> (1, 1, 0)          Y+ and XZ-rotation of 180 clockwise
	 *   (0,0,0) -> (0, 0, 1) -> (0, 0, 1)  -> (0, 1, -1)         Y+ and XZ-rotation of 270 clockwise
	 *   
	 *  ALL PIECES ARE AFFECTED since the right most one has 1 in the middle
	 *  
	 * Try again with T this time fixing Z and rotating XY
	 * 
	 * (0,0,0) -> (1, 0, 0) -> (1, 0, 0)  -> (-1,1, 0)            Y+ and XY-rotation of 0
	 * (0,0,0) -> (0,-1, 0) -> (0, -1,0)  -> (1, 1, 0)    
	 * (0,0,0) -> (-1,0, 0) -> (-1, 0,0)  -> (1, -1,0),
	 * (0,0,0) -> (0, 1, 0) -> (0, 1, 0)  -> (-1,-1,0)        
	 *  
	 *   
	 * Note that to flip a piece by orientation (+ or -) is relatively easy. For example, Y- is simply
	 * an XZ rotation 180 degrees. So that transformation is applied first
	 */
	public static Piece rotateClockwise(Piece p, Axis fixedAxis) {
		Coord[] coords = new Coord[p.size()];
		
		Axis[] rot = fixedAxis.other();    // get the axes being rotated, since this is fixed
		
		int idx = 0;
		for (Coord c : p) {
			int c0 = c.get(rot[0]);
			int c1 = c.get(rot[1]);
			
			if (c0 == 0 && c1 == 0) {
				// this piece does not change since not in plane of rotation
				coords[idx++] = c;
			} else {
				// adjust [1,0] -> [0,-1] -> [-1, 0] -> [0,1] -> ...
				coords[idx++] = c.rotateClockwise(fixedAxis);
			}
		}
		
		Piece newPiece = new Piece(p.id, coords);
		return newPiece;
	}

	/** Helpful debug method. */
	public String toString () {
		if (sign < 0) { return axis + "-"; }
		return axis + "+";
	}
	
}
