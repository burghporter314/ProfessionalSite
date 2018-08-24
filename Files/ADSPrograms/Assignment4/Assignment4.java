import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Assignment4 {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String[] str = new String[1000000];
		Scanner sc = new Scanner(new File("C:\\Users\\Dylan Porter\\Desktop\\Program4.txt"));
		SplayBST<String, Integer> bst = new SplayBST();
		RedBlackBST<String, Integer> rbt = new RedBlackBST();
		String tempString;
		String[] listOfWords = new String[4353]; //Amount of words
		double tempStorage = 0; //Used for Proportionality
		int currentPos = 0, wordsPos = 0; 
		int total = 0;
		while(sc.hasNext()) { //Read through the text file
			tempString = sc.next(); //Store the word, not the number
			listOfWords[wordsPos] = tempString; //Store new word in array
			wordsPos++; 
			tempStorage = Double.valueOf(sc.next()); //We need the number to be a double for division
			tempStorage /= 268729919; //Divide by the total number of frequency (I found this by testing
			tempStorage *= 1000000; //Multiply to get proportionality
			tempStorage = (int)tempStorage; //Get an Integer Representation of frequency so we can sort in array
			for(int i = 0; i < tempStorage; i++) {
				str[i+total] = tempString; //Place word in appropriate place in array 
			}							   //Total keeps track of the previously accessed place in array for future reference
			total += tempStorage;
		}
		total--; //adjust the total variable for random access into array --> could cause arrayindexoutofbounds exception
		Random rand = new Random(); //We need to Randomly insert elements of String array into the Splay Tree
		int randNum = 0;


		while(total != 0) { //Loop until no more elements to randomize
			randNum = rand.nextInt(total); //Generate random number from 0 to total with total decremented every loop
			bst.put(str[randNum], null); //Put random position in array into the bst with a null key 
			str[randNum] = str[total]; //Do a swap to simulate elimination of the element picked
			str[total] = null;
			total--;
		}
		
		wordsPos--;
		
		int outer = wordsPos;

		for(int i = 0; i < outer; i++) {
			randNum = rand.nextInt(wordsPos);
			rbt.put(listOfWords[randNum], i); //Put all words in tree regardless of frequency
			listOfWords[randNum] = listOfWords[wordsPos]; //Do a swap to simulate elimination of the element picked
			wordsPos--;
		}
		

		//ALL DATA FOR ALL WORDS IN BOTH TREES
//		for(String s : rbt.keys()) {
//			bst.get(s);
//			System.out.println("Splay Tree -- Word: " + s + "  Comparisons: " + bst.comparisons);
//			rbt.get(s);
//			System.out.println("RBTree -- Word: " + s + "  Comparisons: " + rbt.comparisons);
//		}
	bst.get("the");
	System.out.println(bst.comparisons);
	rbt.get("the");
	System.out.println(rbt.comparisons);
	}
}
