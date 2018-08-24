import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RSA {

	static FileWriter fw;
	static BufferedWriter bw;
	static ServerSQL request;
	
	static void postBytes(String name, String msg) throws Exception {
		request.post(name, msg);
	}
	
	static String writeBytes(String msg, int coeff, int n, int convertNum) throws Exception {
		Scanner input = new Scanner(msg);
		String convertMSG = "";		
		
		for(int i = convertNum; i != 0; i--) {
			while(input.hasNextInt()) { convertMSG += String.valueOf(calculateCrypt(input.nextInt(), coeff, n) + " "); }
			input = new Scanner(convertMSG);
			convertMSG = "";
		}
		
		while(input.hasNextInt()) { convertMSG += calculateCrypt(input.nextInt(), coeff, n) + " "; }
		input.close();
		return convertMSG;
				
	}
	
	static String decodeMessage(String msg, int d, int n, int convertNum) {

		Scanner input = new Scanner(msg);
		String decoded = "";
		
		for(int i = convertNum; i != 0; i--) {
			while(input.hasNextInt()) { decoded += String.valueOf(calculateCrypt(input.nextInt(), d, n) + " "); }
			input = new Scanner(decoded);
			decoded = "";
		}
		
		while(input.hasNextInt()) { decoded += String.valueOf(calculateCrypt(input.nextInt(), d, n)) + " "; }
		
		input.close();

		return decoded;
		
	}
	
	static String finalMessage(String msg) {
		Scanner input = new Scanner(msg);
		String decoded = "";
		while(input.hasNextInt()) { decoded += String.valueOf((char)input.nextInt()); }
		return decoded;
	}
	
	static String writeMessage(String message) throws IOException {
		
		char[] charArray = message.toCharArray();
		String convertedMSG = "";
		for(char s : charArray) { convertedMSG += Integer.toString((int)s) + " ";}
		return convertedMSG;
		
	}
	
	static int returnN(int p, int q) { return p*q; }
	
	static int returnSigma(int p, int q) { return((p-1)*(q-1)); }
	
	static int returnD(int sigmaN) { int d = 2; while(GCD(d++, sigmaN) != 1) {} d--; return d; }
	
	static int returnE(int d, int sigmaN) { int e = 0; while(((e*d) % sigmaN != 1)){e++;} return e;}
		
	static int calculateCrypt(int num, int power, int mod) {
		
    	int[] itemArray = new int[32], dataOutput = new int[32];
    	
		boolean[] binArray = new boolean[32];
    	
		int temp = power, increment = 0, op = 0, el = 0;
		
		while(temp != 0) {
		  
		  if ((temp % 2) == 1) { binArray[increment++] = true; }
		  else { binArray[increment++] = false; }
		  temp /= 2;
		  
		}
		
		for(int i = 0; i <= 10; i++) {
			
		    num %= mod;
		    itemArray[i] = num;
		    
		    num *= num;
		    
		}
		
		for(int z = 0; z < increment; z++) { if(binArray[z]) { dataOutput[op++] = itemArray[z]; el++;} }
		
		for(int w = 0; w != el-1; w++) { dataOutput[w+1] = (dataOutput[w]*dataOutput[w+1]) % mod; }
		
		return dataOutput[el-1];
		
	}
	
	static int GCD(int a, int b) { return b==0 ? a : GCD(b, a % b); }
	
}
