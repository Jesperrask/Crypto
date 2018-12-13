//Fiat-Shamir sigma-protocol
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public class FiatShamir {

	public static class ProtocolRun {
		public final BigInteger R;
		public final int c;
		public final BigInteger s;

		public ProtocolRun(BigInteger R, int c, BigInteger s) {
			this.R = R;
			this.c = c;
			this.s = s;
		}
	}

	public static void main(String[] args) {
		String filename = "input.txt";
		BigInteger N = BigInteger.ZERO;
		BigInteger X = BigInteger.ZERO;
		ProtocolRun[] runs = new ProtocolRun[10];
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			N = new BigInteger(br.readLine().split("=")[1]);
			X = new BigInteger(br.readLine().split("=")[1]);
			for (int i = 0; i < 10; i++) {
				String line = br.readLine();
				String[] elem = line.split(",");
				runs[i] = new ProtocolRun(
						new BigInteger(elem[0].split("=")[1]),
						Integer.parseInt(elem[1].split("=")[1]),
						new BigInteger(elem[2].split("=")[1]));
			}
			br.close();
		} catch (Exception err) {
			System.err.println("Error handling file.");
			err.printStackTrace();
			System.exit(1);
		}
		BigInteger m = recoverSecret(N, X, runs);
		System.out.println("Recovered message: " + m);
		System.out.println("Decoded text: " + decodeMessage(m));
	}

	public static String decodeMessage(BigInteger m) {
		return new String(m.toByteArray());
	}

	/**
	 * Recovers the secret used in this collection of Fiat-Shamir protocol runs.
	 *
	 * @param N
	 *            The modulus
	 * @param X
	 *            The public component
	 * @param runs
	 *            Ten runs of the protocol.
	 * @return
	 */
	private static BigInteger recoverSecret(BigInteger N, BigInteger X,
			ProtocolRun[] runs) {
		// TODO. Recover the secret value x such that x^2 = X (mod N).
		ProtocolRun one = new ProtocolRun(null, 0, null);
		ProtocolRun two = new ProtocolRun(null, 0, null);
		for(int i = 0; i < runs.length; i++){
			for(int j = i+1; j < runs.length; j++){
				//If R are equal we can check if c differs, we want c=0
				if(runs[i].R.equals(runs[j].R)){
					if(runs[i].c == 0){
						one = runs[i];
						two = runs[j];
					}else{
						one = runs[j];
						two = runs[i];
					}
				}
			}
		}
		//if one.R is null then we know that there are no runs of equal R
		if(one.R != null){
			BigInteger inv = one.s.modInverse(N);
			BigInteger x = inv.multiply(two.s).mod(N);
			BigInteger publicKey = x.pow(2).mod(N);
			if(publicKey.equals(X)){ //if x² = X then we found the secret key
				return x;
			}
		}

		return BigInteger.ZERO;
	}
}
