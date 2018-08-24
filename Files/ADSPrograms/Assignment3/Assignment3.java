/*
 * Credit to Robert Sedgewick and Kevin Wayne
 * Sedgewick, Robert, and Kevin Daniel Wayne. Algorithms. Boston, Mass: Addison-Wesley, 2011. Print.
 * Also Credit to Dr. Simon for Power Point Unit 8 Slides.
 */
import java.util.Random;

public class Assignment3 {

	public static void main(String[] args) {
		Integer[] a = new Integer[100];
		Random random = new Random();
		
		for(int i = 99; i >= 0; i--) {
			a[i] = i;
		}
		
	    double preTime = System.nanoTime(), finalTime;
		QuickShuffle shuffle = new QuickShuffle(a);
		finalTime = System.nanoTime() - preTime;
		finalTime /= 1000000000;
		System.out.println(finalTime);
		System.out.println("Updated Array: ");
		for(Integer temp : a) {
			System.out.println(temp);
		}

		Integer[] b = new Integer[100];
		for(int i = 99; i >= 0; i--) {
			b[i] = i;
		}
		
		preTime = System.nanoTime();
		new QuickNonShuffle(b);
        finalTime = System.nanoTime() - preTime;
        finalTime /= 1000000000;
        System.out.println(finalTime);
		System.out.println("Updated Array: ");
		for(Integer temp : b) {
			System.out.println(temp);
		}
		
		Integer[] c = new Integer[100];	
		for(int i = 99; i >= 0; i--) {
			c[i] = i;
		}
		
		preTime = System.nanoTime();
		new QuickSortNinther(c);
		finalTime = System.nanoTime() - preTime;
		finalTime /= 1000000000;
		System.out.println(finalTime);
		System.out.println("Updated Array: ");
		for(Integer temp : c) {
			System.out.println(temp);
		}

	}
}
