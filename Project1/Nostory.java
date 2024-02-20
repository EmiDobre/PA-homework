/*
 * Acest schelet citește datele de intrare și scrie răspunsul generat de voi,
 * astfel că e suficient să completați cele două metode.
 *
 * Scheletul este doar un punct de plecare, îl puteți modifica oricum doriți.
 *
 * Dacă păstrați scheletul, nu uitați să redenumiți clasa și fișierul.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.StringTokenizer;


class Person {
	long value;
	int family;
	public Person(long value, int family) {
		this.value = value;
		this.family = family;
	}
	public String toString() {
		return "(varsta:" + value + ", fam:" + family + ")";
	}
}

public class Nostory {
	public static void main(final String[] args) throws IOException {
		MyScanner scanner = new MyScanner(new FileReader("nostory.in"));

		var task = scanner.nextInt();
		var n = scanner.nextInt();
		var moves = task == 2 ? scanner.nextLong() : 0;

		var a = new long[n];

		for (var i = 0; i < n; i += 1) {
			a[i] = scanner.nextLong();
		}

		var b = new long[n];
		for (var i = 0; i < n; i += 1) {
			b[i] = scanner.nextLong();
		}

		/*init vector de persoane: toate elemente din a si din b ordonate descrescator
		* in plus le am sub forma de persoana pt ca trb sa retin familia
		*  (perechea initiala) pt subpct 2 */

		Person[] people = new Person[2 * n];
		for (int i = 0; i < n; i++) {
			Person person =  new Person(a[i], i);
			people[i] = person;
		}
		for (int i = 0; i < n; i++) {
			Person person = new Person(b[i], i);
			people[i + n] = person;
		}
		quickSort(people, 0, 2 * n - 1);

		try (var printer = new PrintStream("nostory.out")) {
			if (task == 1) {
				printer.println(solveTask1(people, n));
			} else {
				printer.println(solveTask2(people, moves, n));
			}
		}
	}


	/*pt primul subpunct e de ajuns sa sortez
	/*vectorul prin qsort descrescator si sa iau primele N pers sa le adun
	varstele*/

	public static void swap(Person[] arr, int i, int j) {
		Person temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	public static int partition(Person[] arr, int low, int high) {
		long pivot = arr[high].value;
		int i = (low - 1);

		for (int j = low; j <= high - 1; j++) {
			if (arr[j].value > pivot) {
				i++;
				swap(arr, i, j);
			}
		}
		swap(arr, i + 1, high);
		return (i + 1);
	}

	public static void quickSort(Person[] arr, int low, int high) {
		if (low < high) {
			int pi = partition(arr, low, high);
			quickSort(arr, low, pi - 1);
			quickSort(arr, pi + 1, high);
		}
	}
	private static long solveTask1(Person[] people, int n) {
		long sum = 0;
		for (int i = 0; i < n; i++) {
			sum = people[i].value + sum;
		}
		return sum;
	}

	private static long solveTask2(Person[] people, long moves, int n) {

		long sum = 0L;
		int nr_pairs = 0;  /*trb sa am n perechi/familii schimbate sau nu */

		/* tin trackul pt o familie: pt familia i am true daca am ales deja un membru*/
		boolean[] family_track = new boolean[n];

		for (int i = 0; i < 2 * n; i++) {
			Person person = people[i];
			if (nr_pairs < n) {
				if (family_track[person.family] == false) {
					sum = sum + person.value;
					family_track[person.family] = true;
					nr_pairs++;

					/*daca am deja un mebru ales dar totusi si celalt se afla printre p
					rimele n perechi => vf daca mai am mutari */
				} else {
					if (moves > 0) {
						sum = sum + person.value;
						moves--;
						nr_pairs++;
					}
				}
			}
		}

		return sum;
	}


	/**
	 * A class for buffering read operations, inspired from here:
	 * https://pastebin.com/XGUjEyMN.
	 */
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

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public String nextLine() {
			String str = "";
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}
	}
}
