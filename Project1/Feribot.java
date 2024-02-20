import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.StringTokenizer;

public class Feribot {
	public static void main(final String[] args) throws IOException {
		MyScanner scanner = new MyScanner(new FileReader("feribot.in"));

		var nr_cars = scanner.nextInt();
		var nr_ferib = scanner.nextInt();
		var car_weights = new long[nr_cars + 1];

		/*gasesc intervalul de greutati care va trebui transportat:
		[cea mai usoara masina - toate masinile]*/

		long min_weight = 0;
		long total_weights = 0;
		for (var i = 0; i < nr_cars; i += 1) {
			car_weights[i] = scanner.nextLong();
			total_weights = total_weights + car_weights[i];
			if (min_weight == 0) {
				min_weight = car_weights[i];
			}
			if (min_weight < car_weights[i]) {
				min_weight = car_weights[i];
			}
		}

		try (var printer = new PrintStream("feribot.out")) {
			printer.println(solve(car_weights, nr_ferib, nr_cars, min_weight, total_weights));
		}
	}

	public static long solve(long[] car_weights, int nr_ferib, int nr_cars,
									long min_weight, long total_weights) {
		long left = min_weight;
		long right = total_weights;

		/*avand un cost impus incerc sa pun toate masinile pe un singur feribot
		avand acest cost pt ca am presupus ca este costul maximal cautat al unui feribot*/

		while (left < right) {
			long cost_max = (left + right) / 2;
			long cost_current = 0;
			int feriboats_used = 1;

			for (int i = 0; i < nr_cars; i++) {
				cost_current = car_weights[i] + cost_current;

				/*pentru a salva din timp si a nu parcurge degeaba:
				*daca am o masina > costul_max presupus nu poate fi transportata*/
				if (car_weights[i] > cost_max) {
					feriboats_used = nr_ferib + 1;
					break;
				}

				/*fol feribot nou pentru masinile ramase -resetez cost curent
				*schimb feribotul pt ca adaug masina care nu incapea*/
				if (cost_current > cost_max) {
					feriboats_used++;
					cost_current = car_weights[i];
				}
			}

			/*verific daca costul presupus este cea mai buna solutie*/
			if (feriboats_used <= nr_ferib) {
				right = cost_max;
			} else {
				left = cost_max + 1;
			}
		}

		return right;
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

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

}
