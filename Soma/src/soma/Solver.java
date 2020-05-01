package soma;

import soma.sets.DefaultSet;

public class Solver {
	public static void main(String[] args) {
		// standard Soma cube solver.
		new State(new CubeTarget(), new DefaultSet()).search();
	}
}
