/**
 * Completed on 4/6/2017
 * Thanks to Sean Vinsick for ideas on how to implement
 * Thanks to Brandon Messineo for testing program and suggestions
 * on faster implementation
 * Collaborated with Zach Kuchar
 * Credit to Dr. Drozdek for the ideas of bitflipping
 * 
 * Works Cited
 * 
 * How to Flip a Bit at a Specific Position in an Integer in Any Language." Java - 
 * 		How to Flip a Bit at a Specific Position in an Integer in Any Language - 
 * 		Stack Overflow. N.p., n.d. Web. 17 Apr. 2017.
 * 
 * "How to Get the Value of a Bit at a Certain Position from a Byte?" Java - How to 
 * 		Get the Value of a Bit at a Certain Position from a Byte? - Stack Overflow.
 * 		 N.p., n.d. Web. 17 Apr. 2017.
 */


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Hamming {

	public static void main(String[] args) throws IOException {
		
		/**
		 * dataFile = file that contains desired bytes to be read (works on any kind of file)
		 * encoded = file that will contain the encoded bytes (should be twice the size)
		 * decoded = file that contains the original content of dataFile after bit correction
		 */
		
		String dataFile = "Bytes.txt";
		String encoded = "Encoded.txt";
		String decoded = "Decoded.txt";

	    long startTime = System.nanoTime();
	    
		encode(dataFile, encoded);
		decode(encoded, decoded);
	    
	    long stopTime = System.nanoTime();
	    long elapsedTime = stopTime - startTime;
	    System.out.println((elapsedTime/1000000000.0) + " Seconds to Complete");
		
	}
	
	public static void encode(String dataFile, String encoded) throws IOException {
		
		FileInputStream is = new FileInputStream(dataFile);
		FileOutputStream os = new FileOutputStream(encoded);
		
		Random rand = new Random();
		
		int[] codewords = { 0, 0x1E, 0x2D, 0x33, 0x4B, 0x55,
							0x66, 0x78, 0x87, 0x99, 0xAA, 0xB4,
							0xCC, 0xD2, 0xE1, 0xFF };
		
		int x, upperHalf, lowerHalf;
		while((x = is.read()) != -1) {
			upperHalf = x >>> 4;
			lowerHalf = ((x << 28) >>> 28);

			os.write((codewords[upperHalf]) ^ (1 << rand.nextInt(8)));
			os.write((codewords[lowerHalf]) ^ (1 << rand.nextInt(8)));
		}
		
		os.close();
		is.close();
	}
	
	public static void decode(String encoded, String decoded) throws IOException {
		
		FileInputStream is = new FileInputStream(encoded);
		FileOutputStream os = new FileOutputStream(decoded);
		
		int[] bitArray = new int[2];
		int sequence, combination;
		int b1, b2, b3, b4, b5, b6, b7, b8;
		int s1, s2, s3, s4;
		
		while((bitArray[0] = is.read()) != -1) {

			bitArray[1] = is.read();
			
			for(int i = 0; i < 2; i++) {
				
			    b1 = (bitArray[i]>>>7)&1;
			    b2 = (bitArray[i]>>>6)&1;
			    b3 = (bitArray[i]>>>5)&1;
			    b4 = (bitArray[i]>>>4)&1;
			    b5 = (bitArray[i]>>>3)&1;
			    b6 = (bitArray[i]>>>2)&1;
			    b7 = (bitArray[i]>>>1)&1;
			    b8 = (bitArray[i])&1;
			    
			    s1 = (b4^b5^b6^b7) << 2;
			    s2 = (b2^b3^b6^b7) << 1;
			    s3 = b1^b3^b5^b7;
			    s4 = b1^b2^b3^b4^b5^b6^b7^b8;
			    
			    sequence = s1^s2^s3;
			    
			    if(s4 == 1 && sequence != 0) { bitArray[i] ^= (1 << (8-sequence));} 
			    else if(s4 == 1 && sequence == 0) { bitArray[i] ^= 1;} 
			    else if(s4 == 0 && sequence != 0){System.out.println("Two Errors... Can't Fix");}
			    
			}
			
		    combination = (((bitArray[0] >>> 4) << 4)) ^ (bitArray[1] >>> 4);
		    os.write(combination);
		    
		}
		
		os.close();
		is.close();
		
	}
}
