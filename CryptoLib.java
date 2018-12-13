// Compilation (CryptoLibTest contains the main-method):
//   javac CryptoLibTest.java
// Running:
//   java CryptoLibTest

public class CryptoLib {

	/**
	 * Returns an array "result" with the values "result[0] = gcd",
	 * "result[1] = s" and "result[2] = t" such that "gcd" is the greatest
	 * common divisor of "a" and "b", and "gcd = a * s + b * t".
	 **/

	 public static void main(String args[]){

		 EEA(4,4);

	 }
	public static int[] EEA(int a, int b) {
		// Note: as you can see in the test suite,
		// your function should work for any (positive) value of a and b.
		// a*s + t*b = gcd(a,b)

		int[] result = new int[3];

		if (b == 0){
			result[0] = a;
			result[1] = 1;
			result[2] = 0;
		}else{
			result = EEA(b,a%b);
			int temp = result[1];
			result[1] = result[2];
			result[2] = temp - (a/b)*result[2];
			if(a == b){
				int t = result[1];
				result[1] = result[2];
				result[2] = t;
			}
			//System.out.println("result:" + result[0] +" " + result[1]+ " " + result[2]);
		}
		return result;
}

	/**
	 * Returns Euler's Totient for value "n".
	 **/
	public static int EulerPhi(int n) {
		int result = 1;
		if(n < 0){
			return 0;
		}
		for(int i = 2; i < n; i++){
			int eea[] = EEA(n,i);
			if(eea[0] == 1){
				result++;
			}
		}

		return result;
	}

	/**
	 * Returns the value "v" such that "n*v = 1 (mod m)". Returns 0 if the
	 * modular inverse does not exist.
	 **/
	public static int ModInv(int n, int m) {
		int v = (n%m+m)%m;

		for (int i=1; i<m; i++){
			if((i*v)%m == 1){
				return i;
			}
		}
		return 0;

	}

	/**
	 * Returns 0 if "n" is a Fermat Prime, otherwise it returns the lowest
	 * Fermat Witness. Tests values from 2 (inclusive) to "n/3" (exclusive).
	 **/
	public static int FermatPT(int n) {

		int r;

		for(int i = 2; i < (n/3) ; i++){

			if(n == 1){
				r = 0;
			}

			r = 1;
			for(int ep = 1; ep <= n-1 ; ep++){
				r = (r*i) % n;
			}

			if(r != 1){
				return i;
			}
}
return 0;
}

	/**
	 * Returns the probability that calling a perfect hash function with
	 * "n_samples" (uniformly distributed) will give one collision (i.e. that
	 * two samples result in the same hash) -- where "size" is the number of
	 * different output values the hash function can produce.
	 **/
	public static double HashCP(double n_samples, double size) {
		return -1;
	}

}
