import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Supercomputer {
	static int N, M;
	static int[] colour;
	static int[] indegree;
	static int[] incopy;
	public static int topSortColour(String current_colour, Queue<Integer> redq,
									Queue<Integer> blueq, ArrayList<Integer>[] adj,
									int[] indegree) {
		Queue<Integer> que = new LinkedList<Integer>();
		if (current_colour.equals(new String("red"))) {
			que = redq;
		} else {
			que = blueq;
		}

		int switches = -1;
		while (!que.isEmpty()) {
			int node = que.poll();
			for (Integer child : adj[node]) {
				indegree[child]--;
				if (indegree[child] == 0) {
					if (colour[child] == 1) {
						blueq.add(child);
					} else {
						redq.add(child);
					}
				}
			}

			//coada curenta a ajuns sa fie goala:
			//vf ce culoare a fost si mut coada pe cealalta culoare
			if (que.isEmpty()) {
				if (current_colour.equals(new String("red"))) {
					current_colour = "blue";
					switches++;
					que = blueq;
				} else {
					current_colour = "red";
					switches++;
					que = redq;
				}
			}
		}
		return switches;
	}

	public static int solve(ArrayList<Integer>[] adj) {
		//salvez indegree, cozile de 2 ori pt ca fac de 2 ori sortarea topologica
		Queue<Integer> orphans_red = new LinkedList<Integer>();
		Queue<Integer> orphans_blue = new LinkedList<Integer>();
		Queue<Integer> redq  = new LinkedList<Integer>();
		Queue<Integer> blueq = new LinkedList<Integer>();

		//stabilire indegree:
		for (int node = 1; node <= N; node++) {
			if (indegree[node] == 0) {
				if (colour[node] == 1) {
					orphans_blue.add(node); //copie pt parcurgere 2
					blueq.add(node);
				} else {
					orphans_red.add(node);
					redq.add(node);
				}
			}
		}

		//incep cu rosu:
		int switches_r = topSortColour("red", redq, blueq, adj, indegree);

		//continui cu albastru problema de la inceput
		int switches_b = topSortColour("blue", orphans_red, orphans_blue, adj, incopy);

		//aleg minimul dintre cele 2 sortari
		int context_switches;
		if (switches_r < switches_b) {
			if (switches_r == - 1) {
				context_switches = switches_b;
			} else {
				context_switches = switches_r;
			}
		} else {
			if (switches_b == -1) {
				context_switches = switches_r;
			} else {
				context_switches = switches_b;
			}
		}
		return context_switches;
	}

	public static void main(final String[] args) throws IOException {
		try {
			MyScanner sc = new MyScanner(new FileReader("supercomputer.in"));
			N = sc.nextInt();
			M = sc.nextInt();
			//lista adiacenta: format din nr nod [copii sai,...]s
			ArrayList<Integer>[] adj = new ArrayList[N + 1];
			for (int node = 1; node <= N; node++) {
				adj[node] = new ArrayList<>();
			}

			//initializare culori taskuri: colour[nr task] = 1/blue, 2/red
			colour = new int[N + 1];
			int red = 0, blue = 0;
			for (int i = 1; i <= N; i++) {
				colour[i] = sc.nextInt();
				if (colour[i] == 1) {
					red++;
				} else {
					blue++;
				}
			}
			int ans = 0; //raspuns
			//daca am nr diferit de culori citesc lista de adiacenta a grafului
			//de la u->v arc + aflu nr de noduri care intra in nodul v
			indegree = new int[N + 1];
			incopy = new int[N + 1];
			Arrays.fill(indegree, 0);
			int u, v;
			for (int i = 1; i <= M; i++) {
				u = sc.nextInt();
				v = sc.nextInt();
				adj[u].add(v);
				indegree[v]++;
				incopy[v] = indegree[v];
			}

			if (red != 0 && blue != 0) {
				ans = Supercomputer.solve(adj);
			}
			//scriere rezultat
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("supercomputer.out"));
				bw.write(Integer.toString(ans) + '\n');
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private static class MyScanner {
		private BufferedReader br;
		private StringTokenizer st;

		public MyScanner(Reader reader) {
			br = new BufferedReader(reader);
		}

		public String next() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

	}
}
