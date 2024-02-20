import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Magazin {
	static int N, Q;
	static int[] source; //depoziturile de la care incep
	static int[] exp;	// expedierile pt source[i]
	static int[] dest; //solutia e aici
	static int[] children; //children[i] = nr copii pt nodul i

	static int[] indexDFS; //pt a nu folosi indexOf in Java care are O(n)
	//retin in timp ce fac dfs care e nodul i in ordinea de dfs obtinuta

	static int last = 0; // ultimul index din dfs, counter pt ordinea din dfs
	public static int dfs(int node, boolean[] visited, ArrayList<Integer>[] adj,
						ArrayList<Integer> nodes_dfs) {
		//nod curent marcat ca vizitat:
		visited[node] = true;
		nodes_dfs.add(node);
		indexDFS[node] = last + 1;
		last = indexDFS[node];

		//counter pt copii;
		int childCount = 0;
		//copii nodului curent:
		for (Integer child : adj[node]) {
			if (visited[child] == false) {
				childCount = childCount
						+ dfs(child, visited, adj, nodes_dfs) + 1;
			}
		}
		children[node] = childCount;
		return childCount;
	}

	public static void solve(ArrayList<Integer>[] adj) {
		//vizitat:
		boolean[] visited = new boolean[N + 1];
		visited[1] = true;
		//ordine:
		indexDFS = new int[N + 1];
		Arrays.fill(indexDFS, 0);
		//copii
		children = new int[N + 1];
		Arrays.fill(children, 0);
		ArrayList<Integer> nodes_dfs = new ArrayList<>();
		dfs(1, visited, adj, nodes_dfs);

		//parcurg nodurile sursa si le gasesc indexul in dfs_nodes
		for (int i = 1; i <= Q ; i++) {
			int src = source[i];
			int start = indexDFS[src];
			int index = start + exp[i];

			if (index - 1 >= N) {
				continue;
			}
			if (children[src] < exp[i]) {
				continue;
			}
			dest[i] = nodes_dfs.get(index - 1);
		}
	}

	public static void main(final String[] args) throws IOException {
		try {
			//pt citire mai rapida: myscanner cu bufferreader (scanner si bf era mai lent)
			MyScanner sc = new MyScanner(new FileReader("magazin.in"));
			N = sc.nextInt();
			Q = sc.nextInt();
			int[] vector = new int[N]; //de la 1 la N-1
			source = new int[Q + 1];
			exp = new int[Q + 1];
			for (int k = 1; k <= N - 1; k++) {
				vector[k] = sc.nextInt();
			}
			for (int i = 1; i <= Q; i++) {
				source[i] = sc.nextInt();
				exp[i] = sc.nextInt();
			}

			//constructie graf: vector[k] = nod -> nod = k+1
			ArrayList<Integer>[] adj = new ArrayList[N + 1];
			for (int node = 1; node <= N; node++) {
				adj[node] = new ArrayList<>();
			}
			//init lista adiacenta
			for (int k = 1; k <= N - 1; k++) {
				adj[vector[k]].add(k + 1);
			}

			//init solutie:
			dest = new int[Q + 1];
			Arrays.fill(dest, -1);
			solve(adj);


			//scriere rezultat
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("magazin.out"));
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= Q; i++) {
					sb.append(dest[i]);
					sb.append('\n');
				}
				bw.write(sb.toString());
				bw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
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

