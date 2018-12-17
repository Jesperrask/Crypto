import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;


public class ElGamal {

  public static String decodeMessage(BigInteger m) {
    return new String(m.toByteArray());
  }

  public static void main(String[] arg) {
    String filename = "input.txt";
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      BigInteger p = new BigInteger(br.readLine().split("=")[1]);
      BigInteger g = new BigInteger(br.readLine().split("=")[1]);
      BigInteger y = new BigInteger(br.readLine().split("=")[1]);
      String line = br.readLine().split("=")[1];
      String date = line.split(" ")[0];
      String time = line.split(" ")[1];
      int year  = Integer.parseInt(date.split("-")[0]);
      int month = Integer.parseInt(date.split("-")[1]);
      int day   = Integer.parseInt(date.split("-")[2]);
      int hour   = Integer.parseInt(time.split(":")[0]);
      int minute = Integer.parseInt(time.split(":")[1]);
      int second = Integer.parseInt(time.split(":")[2]);
      BigInteger c1 = new BigInteger(br.readLine().split("=")[1]);
      BigInteger c2 = new BigInteger(br.readLine().split("=")[1]);
      br.close();
      BigInteger m = recoverSecret(p, g, y, year, month, day, hour, minute,
          second, c1, c2);
      System.out.println("Recovered message: " + m);
      System.out.println("Decoded text: " + decodeMessage(m));
    } catch (Exception err) {
      System.err.println("Error handling file.");
      err.printStackTrace();
      System.exit(1);
    }
  }

  //return YEAR.multiply(BigInteger.valueOf(10).pow(10)).add(month.multiply(BigInteger.valueOf(10).pow(8))).add(days.multiply(BigInteger.valueOf(10).pow(6))).add(hours.multiply(BigInteger.valueOf(10).pow(4))).add(minute.multiply(BigInteger.valueOf(10).pow(2))).add(sec);


  public static BigInteger createRandomNumber(int YEAR, int month, int days,
            int hours, int minute, int sec, BigInteger g, BigInteger p, BigInteger c1){

            BigInteger ye = BigInteger.valueOf(YEAR);
            BigInteger m = BigInteger.valueOf(month);
            BigInteger d = BigInteger.valueOf(days);
            BigInteger h = BigInteger.valueOf(hours);
            BigInteger min = BigInteger.valueOf(minute);
            BigInteger s = BigInteger.valueOf(sec);

            BigInteger r = BigInteger.valueOf(1);

            for(int i = 1; i <= 999; i++){
              BigInteger millis = BigInteger.valueOf(i);
              r = ye.multiply(BigInteger.valueOf(10).pow(10)).add(m.multiply(BigInteger.valueOf(10).pow(8))).add(d.multiply(BigInteger.valueOf(10).pow(6))).add(h.multiply(BigInteger.valueOf(10).pow(4))).add(min.multiply(BigInteger.valueOf(10).pow(2))).add(s).add(millis);

              if(c1.mod(p).equals(g.modPow(r,p))){

                return r;

                }
              }
            return BigInteger.ZERO;
            //return (YEAR*((int)Math.pow(10,10)) + month*((int)Math.pow(10,8)) + days*((int)Math.pow(10,6)) + hours*((int)Math.pow(10,4)) + minute*((int)Math.pow(10,2)) + sec);
     }

  /*
   c1 = g^r
   k=c^x
   => k = (g^x)^r

   y=g^x
   =>
   k=y^r (y and r are given/calculated)

   This results in the decryption of c2 by: m= c2*modinv(k)
  */

  public static BigInteger recoverSecret(BigInteger p, BigInteger g,
      BigInteger y, int year, int month, int day, int hour, int minute,
      int second, BigInteger c1, BigInteger c2) {
      BigInteger message = c1;


        BigInteger r = createRandomNumber(year, month, day, hour, minute, second, g, p, c1);
        if(!r.equals(BigInteger.ZERO)){
            BigInteger k = y.modPow(r, p);
            BigInteger kInv = k.modInverse(p);
            message = c2.multiply(kInv).mod(p);
        }

        return message;

        //return c1;
  }

}
