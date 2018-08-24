import java.util.ArrayList;

public class BreadthFirstDirectedPaths {
	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked;  // marked[v] = is there an s->v path?
	private String[] edgeTo;      // edgeTo[v] = last edge on shortest s->v path
	private int[] distTo;      // distTo[v] = length of shortest s->v path
	private ArrayList<String> verticesList;
	private ArrayList<String> relationshipST;
	private String x;

	/**
	 * Computes the shortest path from {@code s} and every other vertex in graph {@code G}.
	 * @param G the digraph
	 * @param s the source vertex
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public BreadthFirstDirectedPaths(Digraph G, String s, ArrayList<String> verticesList, ArrayList<String> relationshipST) {
		this.relationshipST = relationshipST;
		this.verticesList = verticesList;
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new String[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
		bfs(G, s);
	}

	/**
	 * Computes the shortest path from any one of the source vertices in {@code sources}
	 * to every other vertex in graph {@code G}.
	 * @param G the digraph
	 * @param sources the source vertices
	 * @throws IllegalArgumentException unless each vertex {@code v} in
	 *         {@code sources} satisfies {@code 0 <= v < V}
	 */

	private void bfs(Digraph G, String s) {
		int z;
		String word[], word3[], word4[];
		String word2;
		Queue<String> q = new Queue<String>();
		z = Integer.parseInt(s);
		marked[z] = true;
		distTo[z] = 0;
		q.enqueue(s);
		while (!q.isEmpty()) {
			word2 = q.dequeue();
			word3 = word2.split(" ");
			int v = Integer.parseInt(word3[0]); 
			for (String w : G.adj(v)) {
				word = w.split(" ");
				if (!marked[Integer.parseInt(word[0])]) {				//Unwraps the String to parse information.
					edgeTo[Integer.parseInt(word[0])] = v + " " + word[1];
					distTo[Integer.parseInt(word[0])] = distTo[v] + 1;
					marked[Integer.parseInt(word[0])] = true;
					q.enqueue(w);
				}
			}
		}
	}

	/**
	 * Is there a directed path from the source {@code s} (or sources) to vertex {@code v}?
	 * @param v the vertex
	 * @return {@code true} if there is a directed path, {@code false} otherwise
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return marked[v];
	}

	/**
	 * Returns the number of edges in a shortest path from the source {@code s}
	 * (or sources) to vertex {@code v}?
	 * @param v the vertex
	 * @return the number of edges in a shortest path
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public int distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}

	/**
	 * Returns a shortest path from {@code s} (or sources) to {@code v}, or
	 * {@code null} if no such path.
	 * @param v the vertex
	 * @return the sequence of vertices on a shortest path, as an Iterable
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public Iterable<String> pathTo(int v) {
		String[] word;
		String x;
		if (!hasPathTo(v)) return null;
		Stack<String> path = new Stack<>();

		for(x = v+""; ; ) {
			path.push(x);
			word = x.split(" ");
			x = edgeTo[Integer.parseInt(word[0])];
			if( distTo[Integer.parseInt(word[0])] == 0 )
				break;
		}
		//path.push(x);
		return path;
		}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) {
		int V = marked.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertices(Iterable<Integer> vertices) {
		if (vertices == null) {
			throw new IllegalArgumentException("argument is null");
		}
		int V = marked.length;
		for (int v : vertices) {
			if (v < 0 || v >= V) {
				throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
			}
		}
	}
}