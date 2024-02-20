import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Sushi {

	static int n, m, x;
	static int[] prices;
	static int[][] grades;
	static int[] reviews_plate;

	Sushi(){}

	static int task1() {
		/*dp[i][j] = suma notelor folosind primele I platouri si avand un buget j */
		int[][] dp = new int[m + 1][n * x + 1];

		/*CAZ DE BAZA: daca nu aleg niciun platou: valoarea va fi 0 pt orice buget as avea*/
		for (int budget = 0; budget <= n * x; budget++) {
			dp[0][budget] = 0;
		}

		/*CAZ GENERAL: */
		for (int plate = 1; plate <= m; plate++) {

			/*pentru un buget dat:*/
			for (int budget = 0; budget <= n * x; budget++) {

				/*nu folosesc platoul atunci cand nu mai am bani pentru acesta
				deci suma cauta se pastreaza ca fiind cea de la pasul anterior */
				dp[plate][budget] = dp[plate - 1][budget];

				/*daca pretul platoului se incadreaza in buget, folosesc platoul:*/
				if (prices[plate - 1] <= budget) {
					/*cat as avea suma notelor daca adaug platoul*/
					int value = reviews_plate[plate - 1]
								+ dp[plate - 1][budget - prices[plate - 1]];

					if (dp[plate][budget] < value) {
						dp[plate][budget] = value;
					}
				}
			}
		}

		return dp[m][n * x];
	}

	/*fata de t1, aici pot alege sa selectez platoul, sa il selectez o data sau de 2 ori*/
	static int task2() {
		int[][] dp = new int[m + 1][n * x + 1];

		/*CAZ DE BAZA:*/
		for (int budget = 0; budget <= n * x; budget++) {
			dp[0][budget] = 0;
		}

		/*CAZ GENERAL:*/
		for (int plate = 1; plate <= m; plate++) {

			/*pentru un buget dat:*/
			for (int budget = 0; budget <= n * x; budget++) {
				/*nu am bani de platou => val este cea anterioara */
				dp[plate][budget] = dp[plate - 1][budget];

				/* il folosesc de 2 ori sau o data */
				if (prices[plate - 1] * 2 <= budget) {
					/* cat as avea suma notelor daca adaug platoul */
					int value_2plate = 2 * reviews_plate[plate - 1]
									+ dp[plate - 1][budget - 2 * prices[plate - 1]];
					int value_1plate = reviews_plate[plate - 1]
									+ dp[plate - 1][budget - prices[plate - 1]];
					int max;
					if (value_1plate > value_2plate) {
						max = value_1plate;
					} else {
						max = value_2plate;
					}
					if (dp[plate][budget] < max) {
						dp[plate][budget] = max;
					}

				} else {
					if (prices[plate - 1] <= budget) {
						/*cat as avea suma notelor daca adaug platoul*/
						int value = reviews_plate[plate - 1]
									+ dp[plate - 1][budget - prices[plate - 1]];
						if (dp[plate][budget] < value) {
							dp[plate][budget] = value;
						}
					}
				}
			}
		}
		return dp[m][n * x];
	}

	/*fata de task 2 se adauga conditia de a avea maxim N platouri
	*  dp[i][j][k] - suma notelor pentru primele i platouri
	* 				avand buget j folosind maxim k platouri in total*/

	static int task3() {
		int[][][] dp = new int[m + 1][n * x + 1][n + 1];

		/*CAZ DE BAZA:
		 * daca nu aleg niciun platou: valoarea va fi 0 pt orice buget as avea,
		 * si avand 0 platouri => pentru oricate "n"platouri folosite tot 0 o sa am*/

		for (int budget = 0; budget <= n * x; budget++) {
			for (int use = 0; use <= n; use++) {
				dp[0][budget][use] = 0;
			}
		}

		/*CAZ GENERAL:*/

		for (int plate = 1; plate <= m; plate++) {
			/*pentru un buget dat:*/
			for (int budget = 0; budget <= n * x; budget++) {
				/*pentru maxim "use" platouri*/
				for (int use = 1; use <= n; use++) {

					dp[plate][budget][use] = dp[plate - 1][budget][use];

					/* daca aleg 1 platou: use -1, ca am folosit un platou, bugetul scade cu pretul
					 * daca aleg 2 platouri: use - 2, bugetul scade cu suma platoului de 2 ori
					 * in functie de ce aleg se aduna la notele review ale platoului curent */

					if (prices[plate - 1] * 2 <= budget && use >= 2) {
						/*cat as avea suma notelor daca adaug platoul*/
						int value_2plate = 2 * reviews_plate[plate - 1]
										+ dp[plate - 1][budget - 2 * prices[plate - 1]][use - 2];
						int value_1plate = reviews_plate[plate - 1]
										+ dp[plate - 1][budget - prices[plate - 1]][use - 1];
						int max;
						if (value_1plate > value_2plate) {
							max = value_1plate;
						} else {
							max = value_2plate;
						}
						if (dp[plate][budget][use] < max) {
							dp[plate][budget][use] = max;
						}
					} else {

						/*daca imi permit platoul SI exista (use >=1)*/
						if (prices[plate - 1] <= budget  && use >= 1) {
							/*cat as avea suma notelor daca adaug platoul*/
							int value = reviews_plate[plate - 1]
									+ dp[plate - 1][budget - prices[plate - 1]][use - 1];
							if (dp[plate][budget][use] < value) {
								dp[plate][budget][use] = value;
							}
						}
					}
				}
			}
		}
		return dp[m][n * x][n];
	}

	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("sushi.in"));

			final int task = sc.nextInt(); // task number

			n = sc.nextInt(); // number of friends
			m = sc.nextInt(); // number of sushi types
			x = sc.nextInt(); // how much each of you is willing to spend

			prices = new int[m]; // prices of each sushi type
			grades = new int[n][m]; // the grades for each sushi type

			reviews_plate = new int[m];

			// price of each sushi
			for (int i = 0; i < m; ++i) {
				prices[i] = sc.nextInt();
			}

			// each friends rankings of sushi types + notele adunate
			for (int i = 0; i < n; ++i) {

				for (int j = 0; j < m; ++j) {
					grades[i][j] = sc.nextInt();
				}
			}

			/*pt fiecare platou j, review e suma notelor*/
			for (int i = 0; i < m; i++) {
				reviews_plate[i] = 0;
				for (int pers = 0; pers < n; pers ++) {
					reviews_plate[i] = reviews_plate[i] + grades[pers][i];
				}
			}

			int ans;
			switch (task) {
				case 1:
					ans = Sushi.task1();
					break;
				case 2:
					ans = Sushi.task2();
					break;
				case 3:
					ans = Sushi.task3();
					break;
				default:
					ans = -1;
					System.out.println("wrong task number");
			}

			try {
				FileWriter fw = new FileWriter("sushi.out");
				fw.write(Integer.toString(ans) + '\n');
				fw.close();

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
