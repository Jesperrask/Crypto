import java.io.BufferedReader;
import java.io.FileReader;

import javax.xml.bind.DatatypeConverter;

public class cbc {
	private static byte[][] sortedBlocks;

	public static void main(String[] args) {
		String filename = "input.txt";
		byte[] first_block = null;
		byte[] encrypted = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			first_block = br.readLine().getBytes();
			encrypted = DatatypeConverter.parseHexBinary(br.readLine());
			br.close();
		} catch (Exception err) {
			System.err.println("Error handling file.");
			err.printStackTrace();
			System.exit(1);
		}
		String m = recoverMessage(first_block, encrypted);
		System.out.println("Recovered message: " + m);
	}

	/**
	 * Recover the encrypted message (CBC encrypted with XOR, block size = 12).
	 *
	 * @param first_block
	 *            We know that this is the value of the first block of plain
	 *            text.
	 * @param encrypted
	 *            The encrypted text, of the form IV | C0 | C1 | ... where each
	 *            block is 12 bytes long.
	 */
	private static String recoverMessage(byte[] first_block, byte[] encrypted) {
		sortedBlocks = sortBlocks(encrypted);
		System.out.println(keyRecover(first_block));
		System.out.println(first_block);

		return new String(encrypted);

	}
	// M0 = 199305255870
	// C0 = IV
	//the first message m0 is known. Therefore the formula gives us:
	// (IV + M0) + K = C0 ->
	// K = C0 + (IV + M0), and therefore we can recover the key.

	public static byte[] keyRecover(byte[] first_block){
		byte [] IV = sortedBlocks[0];
		byte [] C0 = sortedBlocks[1];
		byte [] M0 = first_block;

		return xorBlocks(C0, xorBlocks(IV,M0));
	}

	public static byte[] xorBlocks(byte[] block1, byte[] block2){

		byte[] xorResult = new byte[12];

		for(int i = 0; i<12; i++){
			xorResult[i] = (byte) (0xff & (block1[i] ^ block2[i]));
		}
		return xorResult;
	}

	public static byte[][] sortBlocks(byte[] encrypted){
		int i = 0;
		int k = 0;
		int encCounter = 0;
		byte [][] sortedBlocks = new byte[encrypted.length/12][12];

		while(i<(encrypted.length/12)){
			 k = 0;
			while(k<12){

				sortedBlocks[i][k] = encrypted[encCounter];
				encCounter ++;
				k++;
			}
			i++;
		}
		return sortedBlocks;
	}
}
