package soma;

import soma.sets.BabylonCube;

public class BabylonSolver {
	public static void main(String[] args) {
		for (int v = 1; v <= 12; v++) {
			System.out.println("Variation " + v);
			// Create the nth bablyon set and try to find a solution.
			new State(new CubeTarget(), BabylonCube.variation(v)).search();
		}
	}
}
