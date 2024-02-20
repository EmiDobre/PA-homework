import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class DijkstraResult {
	List<Integer> d;
	List<Integer> p;
	DijkstraResult(List<Integer> _d, List<Integer> _p) {
		d = _d;  //d[nod] = dist minima de la sursa la acest nod
		p = _p;
	}
}

//la teleporatre se adauga doar 1 sec la cost
class Node implements Comparable<Node>  {
	public int destination;
	public int time;
	public int portal;
	Node(int destination, int time, int portal) {
		this.destination = destination;
		this.time = time;
		this.portal = portal;
	}

	public int compareTo(Node rhs) {
		return Integer.compare(time, rhs.time);
	}
}

public class Teleportare {
	static int N, M, K;
	public static final int INF = (int) 1e9;

	//adj[nod] = pair unde dest = copil, cost = cost pana la copil
	public static int solve(ArrayList<Node>[] adj) {
		DijkstraResult result = dijkstra(1, adj);
		return result.d.get(N);
	}
	public static DijkstraResult dijkstra(int source,  ArrayList<Node>[] adj) {
		List<Integer> d = new ArrayList<>();
		List<Integer> p = new ArrayList<>();
		for (int node = 0; node <= N; node++) {
			d.add(INF);
			p.add(0);
		}

		PriorityQueue<Node> pq = new PriorityQueue<>();
		d.set(source, 0);
		pq.add(new Node(source, 0, 0));

		while (!pq.isEmpty()) {
			int cost = pq.peek().time;
			int node = pq.poll().destination;

			if (cost > d.get(node)) {
				continue;
			}

			// Ii parcurgem toti vecinii.
			for (var e : adj[node]) {
				int child = e.destination;
				int cost_ch = e.time;
				int portal = e.portal;

				boolean available = true;
				//decid daca iau in calcul copilul:
				if (portal > 0) {
					if (d.get(node) % portal != 0) {
						available = false;
					}
				}

				if (available == false) {
					continue;
				}

				//vf imbunatatire dist
				if (d.get(node) + cost_ch < d.get(child)) {
					// actualizre distanta si parinte
					d.set(child, d.get(node) + cost_ch);
					p.set(child, node);
					pq.add(new Node(child, d.get(child), portal));
				}
			}
		}

		for (int i = 1; i <= N; i++) {
			if (d.get(i) == INF) {
				d.set(i, -1);
			}
		}

		return new DijkstraResult(d, p);
	}

	public static void main(final String[] args) throws IOException {
		try {
			//pt citire mai rapida: myscanner cu bufferreader (scanner si bf era mai lent)
			MyScanner sc = new MyScanner(new FileReader("teleportare.in"));
			N = sc.nextInt();
			M = sc.nextInt();
			K = sc.nextInt();

			//constructie graf: vector[k] = nod -> nod = k+1
			ArrayList<Node>[] adj = new ArrayList[N + 1];
			for (int node = 1; node <= N; node++) {
				adj[node] = new ArrayList<>();
			}

			//citire graf adj si telep:
			for (int node = 1; node <= M ; node++) {
				int x = sc.nextInt();
				int y = sc.nextInt();
				int t = sc.nextInt();
				adj[x].add(new Node(y, t, 0));
			}
			for (int node = 1; node <= K ; node++) {
				int x = sc.nextInt();
				int y = sc.nextInt();
				int p = sc.nextInt();
				adj[x].add(new Node(y, 1, p)); //cele tip portal
			}

			int ans = solve(adj);
			//scriere rezultat
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("teleportare.out"));
				bw.write(Integer.toString(ans) + '\n');
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

