package soma;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Standard 3x3x3 cube target
 */
public class CubeTarget extends Target  {
	/**
	 * State is represented by a 3D matrix, where each entry is 0 if free, otherwise the id
	 * of the piece assigned to that cube.
	 */
	int state[][][] = new int[3][3][3];

	final ArrayList<Coord> coordinates = new ArrayList<>();
	
	public CubeTarget() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					Coord c = new Coord(x, y, z);
					coordinates.add(c);
				}
			}
		}
	}

	@Override
	public boolean unused(Coord here) {
		return state[here.x][here.y][here.z] == 0; 
	}
	
	/** Returns FALSE if invalid or if occupied. */
	@Override
	public boolean safe(Coord here) {
		if (here.x < 0 || here.x > 2) { return false; }
		if (here.y < 0 || here.y > 2) { return false; }
		if (here.z < 0 || here.z > 2) { return false; }

		return state[here.x][here.y][here.z] == 0; 
	}

	@Override
	public Iterator<Coord> iterator() { return coordinates.iterator(); }

	@Override
	public boolean occupy(Coord here, int id) {
		if (state[here.x][here.y][here.z] != 0) { return false; }
		
		state[here.x][here.y][here.z] = id;
		return true;
	}

	@Override
	public void clear(Coord here) {
		state[here.x][here.y][here.z] = 0;
	}

	@Override
	public String key() {
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
}
