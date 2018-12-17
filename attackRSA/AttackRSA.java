import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;


public class AttackRSA {

	public static void main(String[] args) {
		String filename = "input.txt";
		BigInteger[] N = new BigInteger[3];
		BigInteger[] e = new BigInteger[3];
		BigInteger[] c = new BigInteger[3];
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			for (int i = 0; i < 3; i++) {
				String line = br.readLine();
				String[] elem = line.split(",");
				N[i] = new BigInteger(elem[0].split("=")[1]);
				e[i] = new BigInteger(elem[1].split("=")[1]);
				c[i] = new BigInteger(elem[2].split("=")[1]);
			}
			br.close();
		} catch (Exception err) {
			System.err.println("Error handling file.");
			err.printStackTrace();
		}
		BigInteger m = recoverMessage(N, e, c);
		System.out.println("Recovered message: " + m);
		System.out.println("Decoded text: " + decodeMessage(m));
	}

	public static String decodeMessage(BigInteger m) {
		return new String(m.toByteArray());
	}

	/**
	 * Tries to recover the message based on the three intercepted cipher texts.
	 * In each array the same index refers to same receiver. I.e. receiver 0 has
	 * modulus N[0], public key d[0] and received message c[0], etc.
	 *
	 * @param N
	 *            The modulus of each receiver.
	 * @param e
	 *            The public key of each receiver (should all be 3).
	 * @param c
	 *            The cipher text received by each receiver.
	 * @return The same message that was sent to each receiver.


	 */
	private static BigInteger recoverMessage(BigInteger[] N, BigInteger[] e,
			BigInteger[] c) {
		// TODO Solve assignment.

		// chineseresult : X, MOD
		BigInteger[] c1 = chineseRemainder(c[0],N[0],c[1],N[1]);
		BigInteger[] c2 = chineseRemainder(c1[0],c1[1],c[2],N[2]);

		System.out.println(c2[0]);

		BigInteger msg = CubeRoot.cbrt(c2[0].mod(c2[1]));

		return msg;
	}

/*
	-- Given x0 ≡ a1 mod n1
	-- and   x1 ≡ a2 mod n2
	--       x01 ≡ a  mod n1*n2

	-- a = a1 * n2 * m2 + a2 * n1 *m1
	-- m2 / m1 can be computed using EEA.
	-- m1*n1 + m2*n2 = 1

	*/

	private static BigInteger[] chineseRemainder(BigInteger a1, BigInteger n1, BigInteger a2, BigInteger n2){

		BigInteger m1 = BigInteger.ZERO;
		BigInteger m2 = BigInteger.ZERO;
		BigInteger eea[] = EEA(n1, n2);
		if(eea[0].equals(BigInteger.valueOf(1))){
			m1 = eea[1];
			m2 = eea[2] ;
		} else {
			System.out.println("EEA did not result in 1");
			return  new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO};
	}

		BigInteger n = n1.multiply(n2);
	  BigInteger x = (a1.multiply(m2).multiply(n2)).add((a2.multiply(m1).multiply(n1)));
		BigInteger[] result = new BigInteger[]{x,n};
		return result;
	}

	public static BigInteger[] EEA(BigInteger a, BigInteger b) {
		// Note: as you can see in the test suite,
		// your function should work for any (positive) value of a and b.
		// a*s + t*b = gcd(a,b)

		BigInteger[] result = new BigInteger[3];

		if (b.equals(BigInteger.valueOf(0))){
			result[0] = a;
			result[1] = BigInteger.valueOf(1);
			result[2] = BigInteger.valueOf(0);
		}else{
			result = EEA(b,a.mod(b));
			BigInteger temp = result[1];
			result[1] = result[2];
													//temp - (a/b) * result[2]
													//result[2] = temp - (a/b)*result[2];
			result[2] = temp.subtract((a.divide(b)).multiply(result[2]));
			if(a.equals(b)){
				BigInteger t = result[1];
				result[1] = result[2];
				result[2] = t;
			}
			//System.out.println("result:" + result[0] +" " + result[1]+ " " + result[2]);
		}
		return result;
}

}
