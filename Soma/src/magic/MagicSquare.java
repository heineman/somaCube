package magic;

/**
 * Brute force method to place values from 1 .. n^2 uniquely in a two-dimensional array to seek magic squares
 * where each row and column (and diagonal) sum to the same value.
 
  
  void solve(int step) {
    if (step == last) { 
      check if a solution
      return
    }

    for (each possible item) {
      if (item used) { continue; }

	  used[item] = true;
	  update state with item
	
	  solve(step+1);
			
	  remove item from state
      used[item] = false;
	}
  }
  
 */
public class MagicSquare {
	int state[][];          // representation of state
	final boolean used[];   // has a number been used

	final int n;            // nxn size of magic square
	final int magicSum;     // used to determine whether a square is valid

	static int count;       // how many found.
	
	/** Construct the problem */
	public MagicSquare(int n) {
		this.n = n;
		state = new int[n][n];
		used = new boolean[n*n+1];
		magicSum = n*(n*n+1)/2;
	}

	/** Solve all possible solutions. */
	void solve(int step) {
		if (step == n*n) { 					      // if reached final state
			if (isValid()) {					  // CHECK if valid Magic Square
				outputSolution();				  // Output solution and increment count
				count++;
			}
			return;
		}

		for (int val = 1; val <= n*n; val++) {    // try every possible value
			if (used[val]) { continue; }          //     ... that is unused

			used[val] = true;					  // mark that it is used
			state[step/n][step%n] = val;          //     .. and update state
			
			solve(step+1);						  // explore further
			
			state[step/n][step%n] = 0;			  // undo state
			used[val] = false;					  // and record that value can be used again
		}
	}

	/** Check if state is a valid solution. */
	boolean isValid() {
		int sumD1 = 0;
		int sumD2 = 0;
		for (int i = 0; i < n; i++) {
			int sumR = 0;
			int sumC = 0;
			sumD1 += state[i][i];
			sumD2 += state[i][n-i-1];
			for (int j = 0; j < n; j++) {
				sumR += state[i][j];
				sumC += state[j][i];
			}
			if (sumR != magicSum || sumC != magicSum) { return false; }
		}

		// diagonals
		return (sumD1 == magicSum && sumD2 == magicSum);
	}

	/** Output Solution from State. */
	public void outputSolution () {
		for (int r = 0; r < n; r++) {
			for (int c = 0; c < n; c++) {
				System.out.print(state[r][c]);
				System.out.print(' ');
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		// only works for 3x3 since computationally too expensive for higher magic squares
		new MagicSquare(3).solve(0);
		System.out.println("# solutions:" + count);
	}
}
