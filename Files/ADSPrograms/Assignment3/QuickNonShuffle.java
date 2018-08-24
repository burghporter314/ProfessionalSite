/*
 * Credit to Robert Sedgewick and Kevin Wayne
 * Sedgewick, Robert, and Kevin Daniel Wayne. Algorithms. Boston, Mass: Addison-Wesley, 2011. Print.
 * Also Credit to Dr. Simon for Power Point Unit 8 Slides.
 */
import java.util.Random;

public class QuickNonShuffle {
	
	public <T extends Comparable<T>> QuickNonShuffle(T[] a) {
		sort(a, 0, a.length -1);
	}
	
    private static int INSERTION_SORT_CUTOFF = 10; //Thanks to Sedgewick
    
    public static <T extends Comparable<T>> void sort(T[] a) {
    	int max = 0;
    	for(int i=1; i<a.length; i++)
    	  if(a[i].compareTo(a[max]) > 0) max = i;
    	swap(a, max, a.length-1); 
    	sort(a, 0, a.length - 2);
    }
    
	public static <T extends Comparable<T>> void sort(T[] a, int lo, int hi) { 
		int n = hi - lo + 1;
		// cutoff to insertion sort
		if (n <= INSERTION_SORT_CUTOFF) {
			insertionSort(a, lo, hi);
		return;
		}

		if (hi <= lo) return;
		int j = partition(a, lo, hi);
		sort(a, lo, j-1);
		sort(a, j+1, hi);
	
	}
    public static <T extends Comparable<T>> int partition(T[] a, int lo, int hi) {
    	int i = lo, j = hi + 1;
    	Random random = new Random();
    	swap(a, lo, ( i + random.nextInt(j-i)) ); //Pick a random partition, not the first one since that will yield ridiculous runtime
    	T v = a[lo];
    	
    	while (true) { 
    		while (a[++i].compareTo(v) < 0);
    		while (v.compareTo(a[--j]) < 0);
    		if (i >= j) break;
    		swap(a, i, j);
    	}
    	swap(a, lo, j);
    	return j;
    }
	static <T> void swap(T[] a, int lo, int hi) {
		T temp = a[lo];
		a[lo] = a[hi];
		a[hi]=temp;
	}
	
	//InsertionSort thanks to Sedgewick
    // sort from a[lo] to a[hi] using insertion sort
    private static void insertionSort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1]); j--)
                swap(a, j, j-1);
    }
    
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
}