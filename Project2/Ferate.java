import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.StringTokenizer;


public class Ferate {
	static int N;
	static int M;
	static int S;
	static ArrayList<Integer>[] inNodes; //ce noduri intra in fiecare nod
	static int[] which_scc; 	//in ce scc se afla fiecare nod
	static int current_scc = 0; //contor pt scc curent

	//Pentru TARJAN:
	static int[] parent;  // parintele nodului in dfs
	static int[] found;	  //timestamp cand a fost gasit nodul/ ii da un id nodului

	//pt un nod va fi unul din nodurile copii/sau el insusi cu timestampul cel mai mic
	static int[] low_link;
	static Stack<Integer> nodes_stack = new Stack<>(); //folosita in dfs
	static boolean[] in_stack;
	static int timestamp;

	public static int solve(ArrayList<Integer>[] adj) {
		ArrayList<ArrayList<Integer>> all_sccs = new ArrayList<>();
		which_scc = new int[N + 1];
		tarjan(adj, all_sccs);

		//un scc izolat care nu contine sursa are nevoie de muchie care sa intre
		//in acesta
		int need_connection = 0;
		for (ArrayList<Integer> scc : all_sccs) {
			int other_connections = 0;	//nr conexiuni cu alte scc uri
			boolean source_scc = false;
			for (Integer node : scc) {
				if (node == S) {
					source_scc = true;
				}
				for (Integer parent : inNodes[node]) {
					if (which_scc[parent] != which_scc[node]) {
						other_connections++;
					}
				}
				//nodul cu sursa: nu se pune la calcul
			}
			if (other_connections == 0) {
				if (source_scc == false) {
					need_connection++;
				}
			}
		}
		return need_connection;
	}

	public static void tarjan(ArrayList<Integer>[] adj, ArrayList<ArrayList<Integer>> all_sccs) {
		parent = new int[N + 1];
		found = new int[N + 1];
		low_link = new int[N + 1];
		in_stack = new boolean[N + 1];

		Arrays.fill(parent, -1);
		Arrays.fill(found, -1);
		Arrays.fill(low_link, -1);
		Arrays.fill(in_stack, false);

		timestamp = 0;
		for (int node = 1; node <= N; ++node) {
			if (parent[node] == -1) {   //nod nevizitat
				parent[node] = node;
				dfs(node, all_sccs, adj);
			}
		}
	}

	private static void dfs(int node, ArrayList<ArrayList<Integer>> all_sccs,
							ArrayList<Integer>[] adj) {
		//un nou nod se viziteaza: timpul de gasire de incrementeaza
		timestamp++;
		found[node] = timestamp;
		low_link[node] = found[node]; //init low link cu primul timestampul sau pt a face minim apoi
		nodes_stack.push(node);		 //in curs de vizitare
		in_stack[node] = true;

		//copii nodului
		//pt a afla low link nod trebuie sa parcurg copii(dfs) sai si sa aleg
		//timestampul cel mai mic dintre ei, daca sunt pe stiva
		//(altfel ar interfera cu alte scc uri)

		for (Integer child : adj[node]) {
			if (parent[child] != -1) {
				if (in_stack[child]) {
					low_link[node] = Math.min(low_link[node], found[child]);
				}
				continue;
			}
			parent[child] = node;
			dfs(child, all_sccs, adj);
			low_link[node] = Math.min(low_link[node], low_link[child]);
		}

		//copii nodului parinte au fost parcursi si low_link se stie
		//daca acesta a ajuns sa fie cat timestampul nodului inseamna ca nodul
		//este inceputul unei noi componente SCC (noduri in acc SCC au acc timestamp)
		if (low_link[node] == found[node]) {
			//scoatere din stiva nodurile din acelasi scc(adica copii/copii copiilor samd, nodurile
			// care au facut sa se ajunga inapoi la nodul parinte)
			ArrayList<Integer> scc = new ArrayList<>();
			current_scc++;

			do {
				Integer x = nodes_stack.peek();
				nodes_stack.pop();
				in_stack[x] = false;
				//setare ce scc are nodul x:
				which_scc[x] = current_scc;
				scc.add(x);
			} while (scc.get(scc.size() - 1) != node); //oprire cand nodul parinte e scos din stiva

			//adaugare componenta:
			all_sccs.add(scc);
		}
	}
	public static void main(final String[] args) throws IOException {
		try {
			//pt citire mai rapida: myscanner cu bufferreader (scanner si bf era mai lent)
			MyScanner sc = new MyScanner(new FileReader("ferate.in"));
			N = sc.nextInt();
			M = sc.nextInt();
			S = sc.nextInt();

			//constructie graf: x -> y
			ArrayList<Integer>[] adj = new ArrayList[N + 1];
			inNodes = new ArrayList[N + 1];
			for (int node = 1; node <= N; node++) {
				adj[node] = new ArrayList<>();
				inNodes[node] = new ArrayList<>();
			}

			//init lista adiacenta + lista noduri care intra
			for (int i = 1; i <= M; i++) {
				int x = sc.nextInt();
				int y = sc.nextInt();
				adj[x].add(y);
				inNodes[y].add(x);
			}

			//rasp:
			int ans = solve(adj);

			//scriere rezultat
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("ferate.out"));
				bw.write(Integer.toString(ans) + '\n');
				bw.close();
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
