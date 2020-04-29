package magic;

/**
 * A 4x4 magic square has 16 digits from 1-16. There are 16! possible arrangements
 * or 20,922,789,888,000 possible.
 * 
 *  A  B  C  d      * compute d from 34 - (A+B+C)
 *  E  Q  k  r    
 *  F  H  N  p      - all lower case values are computed from earlier selections (leave out i,j,l,m since these are commonly used elsewhere)
 *  g  s  o  t      
 * 
 * MUCH more efficient than brute force search! Takes advantage of partial information.
 */
public class FourByFour {
	public static void main(String[] args) {
		int count = 0;

		// compute known target value=34 for 4x4 square
		int M=34;

		// first row
		for (int A = 1; A <= 16; A++) {
			for (int B = 1; B <= 16; B++) {
				if (B == A) { continue; }
				for (int C = 1; C <= 16; C++) {
					if (C == B || C == A) { continue; }

					// TRICK! Can compute instead of searching! All computed digits are in lower case.
					int d = M - (A + B + C);
					if (d < 1 || d > 16) { continue; }
					if (d == A || d == B || d == C) { continue; }

					// first column
					for (int E = 1; E <= 16; E++) {
						if (E == A || E == B || E ==C || E == d) { continue; }
						for (int F = 1; F <= 16; F++) {
							if (F == A || F == B || F == C || F == d || F == E) { continue; }

							int g  = M - (A + E + F);
							if (g < 1 || g > 16) { continue; }
							if (g == A || g == B || g == C || g == d || g == E || g == F) { continue; }

							// long diagonal Northeast
							for (int H = 1; H <= 16; H++) {
								if (H == A || H == B || H == C || H == d || H == E || H == F || H == g) { continue; }

								int k = M - (g + H + d);
								if (k < 1 || k > 16) { continue; }
								if (k == A || k == B || k == C || k == d || k == E || k == F || k == g || k == H) { continue; }


								// third column
								for (int N = 1; N <= 16; N++) {
									if (N == A || N == B || N == C || N == d || N == E || N == F || N == g || N == H || N == k) { continue; }

									int o = M - (C + k + N);
									if (o < 1 || o > 16) { continue; }
									if (o == A || o == B || o == C || o == d || o == E || o == F || o == g || o == H || o == k || o == N) { continue; }

									// third row
									int p = M - (F + H + N);
									if (p < 1 || p > 16) { continue; }
									if (p == A || p == B || p == C || p == d || p == E || p == F || p == g || p == H || p == k || p == N || p == o) { continue; }

									// second column
									for (int Q = 1; Q <= 16; Q++) {
										if (Q == A || Q == B || Q == C || Q == d || Q == E || Q == F || Q == g || Q == H || Q == k || Q== N || Q == o || Q == p) { continue; }

										int r = M - (E + Q + k);
										if (r < 1 || r > 16) { continue; }
										if (r == A || r == B || r == C || r == d || r == E || r == F || r == g || r == H || r == k || r== N || r == o || r == p || r == Q) { continue; }

										// second row
										if (E + Q + k + r != M) { continue; }

										int s = M - (B + Q + H);
										if (s < 1 || s > 16) { continue; }
										if (s == A || s == B || s == C || s == d || s == E || s == F || s == g || s == H || s == k || s == N || s == o || s == p || s == Q || s == r) { continue; }
										int t = M - (d + r + p);
										if (t < 1 || t > 16) { continue; }
										if (t == A || t == B || t == C || t == d || t == E || t == F || t == g || t == H || t == k || t == N || t == o || t == p || t == Q || t == r || t == s) { continue; }

										// NorthWest Diagonal
										if (A + Q + N + t != M) { continue; }

										// fourth Column
										if (d + r + p + t != M) { continue; }

										if (count == 0) {
											System.out.println("First one found...");
											System.out.printf("%2d %2d %2d %2d %n%2d %2d %2d %2d %n%2d %2d %2d %2d %n%2d %2d %2d %2d %n%n",
													A,B,C,d,E,Q,k,r,F,H,N,p,g,s,o,t);
										}

										count++;
									}
								}
							}
						}
					}
				}
			}
		}

		System.out.println("total found:" + count);
		System.out.println("total unique (ignoring symmetry):" + count/8);
	}
}