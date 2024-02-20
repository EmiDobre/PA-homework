import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Semnale {

	static int sig_type, x, y;
	static final int  mod = 1000000007;

	Semnale(){}
	static int type1() {
		int[][][] dp = new int[x + 1][y + 1][2];

		/*CAZ DE BAZA - initializari*/
		dp[1][1][0] = 1; //{0} {1} in 0 => 10
		dp[1][1][1] = 1; //{0} {1} in 1 <=> in 01 <=> 01

		/*CORNER CASE1: daca am mai multi biti de 1 decat 2 am alte cazuri de init*/
		for (int bits_1 = 2; bits_1 <= y; bits_1++) {
			if (bits_1 == 2) {
				dp[1][2][0] = 0; 		//{0} {1,1} => 110, nu convine
				dp[1][2][1] = 1; 		//{0} {1,1} => 101
			} else {
				dp[1][bits_1][0] = 0;  //{0} si {1,1,.} => imposibil
				dp[1][bits_1][1] = 0;
			}
		}

		/*CORNER CASE2:
		*PT terminare in 1: {0,..00} {1} se termine in 01 => [{0..0} { } 1] care va fi mereu 1*/

		for (int bits_0 = 2; bits_0 <= x; bits_0++) {
			dp[bits_0][1][1] = 1;
		}

		/*CAZ GENERAL:
		* se termina in 0 - subproblema este cea cu un bit de 0 mai putin
		* se termina in 1 <=> in 01: subproblmea este cea cu un bit de 0 mai putin si un
		* 					 	     bit de 1 mai putin */

		for (int bits_0 = 2; bits_0 <= x; bits_0++) {
			for (int bits_1 = 1; bits_1 <= y; bits_1++) {
				/*se termina in 0*/
				dp[bits_0][bits_1][0] = (dp[bits_0 - 1][bits_1][0]
									+	dp[bits_0 - 1][bits_1][1]) % mod;

				/*se termina in 1 - vf sa nu fie corner case ul*/
				if (bits_1 != 1) {
					dp[bits_0][bits_1][1] = (dp[bits_0 - 1][bits_1 - 1][0]
										+	dp[bits_0 - 1][bits_1 - 1][1]) % mod;
				}
			}
		}

		/*SOLUTIA: adunare dintre cele 2 subprobleme*/
		int mod_sol = (dp[x][y][0] + dp[x][y][1]) % mod;

		System.out.println(mod_sol);
		return mod_sol;
	}

	/*se termina in 0
	* se termina in 01
	* se termina in 011*/
	static int type2() {
		long[][][] dp = new long[x + 1][y + 1][3];

		/*CAZ DE BAZA - 1 bit de {0} - initializari pt cele 3 tipuri*/
		dp[1][1][0] = 1; 			//{0} {1} in 0 => 10
		dp[1][1][1] = 1; 			//{0} {1} in 01 <=> 01
		dp[1][1][2] = 0; 			//{0} {1} in 011 - imposibil

		/*CORNER CASE la 1 bit 0  - corner case urile apar cand nu mai am biti de 0 ramasi { }*/
		for (int bits_1 = 2; bits_1 <= y; bits_1++) {
			if (bits_1 == 2) {	//{0} {1,1}
				dp[1][2][0] = 1; 	//{ } {1,1} |  0 => 110 convine
				dp[1][2][1] = 1; 	//{ } {1}   | 01 => 101
				dp[1][2][2] = 1;	//{ } { }   |011 => 011
			}
			if (bits_1 == 3) {	//{0} {1,1,1}
				dp[1][3][0] = 0;	//{ } {1,1,1} |  0 => 1110 nu convine
				dp[1][3][1] = 1 ;	//{ } {1,1}   | 01 => 1101
				dp[1][3][2] = 1;	//{ } {1} 	  |011 => 1011
			}
			if (bits_1 == 4) {	//{0} {1,1,1,1}
				dp[1][4][0] = 0;	//{ } {1,1,1,1} |  0 => imposibil
				dp[1][4][1] = 0;	//{ } {1,1,1}   | 01 => imposibil
				dp[1][4][2] = 1; 	//{ } {1,1}		|011 => 11011
			}
			/*pt orice numar > 5, nu pot respecta conditia => init cu 0*/
			if (bits_1 >= 5) {
				dp[1][bits_1][0] = 0;
				dp[1][bits_1][1] = 0;
				dp[1][bits_1][2] = 0;
			}
		}

		/*CORNER CASE2: cand am bits_1 = 1 bit de 1 sau 2 biti de 1
		* apar corner case uri pentru "01" "011"=*/

		for (int bits_0 = 2; bits_0 <= x; bits_0++) {
			dp[bits_0][1][1] = 1; 	//{0...0,0} {1} terminat in "01" => {0,0...0} { } | 01
			dp[bits_0][1][2] = 0;   //{0,...0,0} {1} term in "011" - imposibil

			dp[bits_0][2][2] = 1;	//{0...0,0} {1, 1} term in "011" => {0,0,...0} { } |011
		}

		/*CAZ GENERAL: 	-se termina in 0 <=> subproblema este cea cu un bit de 0 mai putin
		 * 			   	-se termina in 01 <=> subproblmea este cea cu un bit de 0 mai putin si un
		 * 											bit de 1 mai putin
		 * 			 	-se termina in 011 <=> subproblema este cea cu un bit de 0 mai putin si 2
		 * 											biti de 1 mai putin
		 *  */

		for (int bits_0 = 2; bits_0 <= x; bits_0++) {
			for (int bits_1 = 1; bits_1 <= y; bits_1++) {
				/*se termina in 0*/
				dp[bits_0][bits_1][0] = (dp[bits_0 - 1][bits_1][0]
									+	dp[bits_0 - 1][bits_1][1]
									+ 	dp[bits_0 - 1][bits_1][2]) % (long) mod;

				/*se termina in 01 - vf sa nu fie corner case uri pt bits_1 = 1 sau 2*/
				if (bits_1 > 1) {
					dp[bits_0][bits_1][1] = (dp[bits_0 - 1][bits_1 - 1][0]
										+	dp[bits_0 - 1][bits_1 - 1][1]
										+	dp[bits_0 - 1][bits_1 - 1][2]) % (long) mod;
				}

				/*se termina in 011 - vf s anu fie corner case ul pt bits_1*/
				if (bits_1 > 2) {
					dp[bits_0][bits_1][2] = (dp[bits_0 - 1][bits_1 - 2][0]
										+	dp[bits_0 - 1][bits_1 - 2][1]
										+	dp[bits_0 - 1][bits_1 - 2][2]) % (long) mod;
				}
			}
		}

		/*SOLUTIA: adunare dintre cele 3 tipuri */
		int mod_sol =  (int) ((dp[x][y][0] + dp[x][y][1] + dp[x][y][2]) % (long) mod);

		System.out.println(mod_sol);
		return mod_sol;
	}

	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(new File("semnale.in"));

			sig_type = sc.nextInt();
			x = sc.nextInt();
			y = sc.nextInt();

			int ans;
			switch (sig_type) {
				case 1:
					ans = Semnale.type1();
					break;
				case 2:
					ans = Semnale.type2();
					break;
				default:
					ans = -1;
					System.out.println("wrong task number");
			}

			try {
				FileWriter fw = new FileWriter("semnale.out");
				fw.write(Integer.toString(ans));
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