
/*
 * Dylan Porter
 * 12/10/2016
 * Advanced Data Structures
 * Credit to Dr. Simon --> I used code from slides
 * Credit to Sedgewick --> I took the Digraph and BreadthFirstDirectedPath code from his site to modify
 * Credit to Jimmy --> Full Disclosure, this was a collaborative effort. Throughout this assignment,
 * Jimmy and I worked on this together. He wrote a majority of the BreadthFirstDirectedPaths code while I
 * modified the Digraph code -- also with his help. I also helped with the constructor implementation as well.
 */

import java.util.NoSuchElementException;
import java.io.*;
import java.util.*;

/**
 *  The {@code Digraph} class represents a directed graph of vertices
 *  named 0 through <em>V</em> - 1.
 *  It supports the following two primary operations: add an edge to the digraph,
 *  iterate over all of the vertices adjacent from a given vertex.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the vertices adjacent from a given vertex, which takes
 *  time proportional to the number of such vertices.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class Digraph {
	private static final String NEWLINE = System.getProperty("line.separator");

	private final int V;           // number of vertices in this digraph
	private int E;                 // number of edges in this digraph
	private static Bag<String>[] adj;    // adj[v] = adjacency list for vertex v
	private int[] indegree;        // indegree[v] = indegree of vertex v
	private ArrayList<String> namesList;
	private ArrayList<String> linkST;

	/**
	 * Initializes an empty digraph with <em>V</em> vertices.
	 *
	 * @param  V the number of vertices
	 * @throws IllegalArgumentException if {@code V < 0}
	 */

	public Digraph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		this.E = 0;
		indegree = new int[V];
		adj = (Bag<String>[]) new Bag[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new Bag<String>();
		}
	}
	

	/**  
	 * Initializes a digraph from the specified input stream.
	 * The format is the number of vertices <em>V</em>,
	 * followed by the number of edges <em>E</em>,
	 * followed by <em>E</em> pairs of vertices, with each entry separated by whitespace.
	 *
	 * @param  vertices the number of vertices
	 * @param  edges the number of edges
	 * @param  inputFile the file input
	 * @param  verticesList the Symbol Table for vertices
	 * @param  relationshipST the Symbol Table for relationships
	 * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
	 * @throws IllegalArgumentException if the number of vertices or edges is negative
	 * @throws IllegalArgumentException if the input stream is in the wrong format
	 */
	public Digraph(int vertices, int edges, Scanner inputFile, ArrayList<String> namesList, ArrayList<String> linkST) {
		try {
			String encode; //Taken in the form of name + " " + number for future interpretation
			
			this.linkST = linkST; //We needed a global variable for future use of linkST and namesList
			this.namesList = namesList;
			
			//There cannot be a negative number of vertices
			this.V = vertices; //We define the global variable, V, as the number past into this constructor
			if (V < 0) throw new IllegalArgumentException("number of vertices in a Digraph must be nonnegative");
			
			//There cannot be a negative number of Edges
			this.E = edges;
			if (E < 0) throw new IllegalArgumentException("number of edges in a Digraph must be nonnegative");
			
			indegree = new int[V]; // We declare this included global variable and adj for cuntioinality
			adj = (Bag<String>[]) new Bag[V];
			
			//Initialize the Bags to stop errors
			for (int v = 0; v < V; v++) {
				adj[v] = new Bag<String>(); 
			}
			
		    //We must traverse the input file again
			String[] elementList;
			while(inputFile.hasNextLine()) {
				int x, y, z; //x and y are names, z is the relationship					
				String word = inputFile.nextLine();
				elementList = word.split(",");

				x = namesList.indexOf(elementList[0]);
				y = namesList.indexOf(elementList[2]);
				z = linkST.indexOf(elementList[1]);
				
				encode = y + " " + z; //Function defined at top

				addEdge(x, encode); //Why define a new edge where x points to encode which consists of relationship to person
				
				switch (z) {
					case 0: 						//x is husband to...
						encode = y + " " + 2;		//Person X is the spouse of Y. (Aux Relation)
						addEdge(x, encode);
						encode = x + " " + 1;		//Person Y is the wife of X. (Aux Relation)
						addEdge(y, encode);
						encode = x + " " + 2;		//Person Y is the spouse of X. (Aux Relation)
						addEdge(y, encode);
						break;
					case 1:							//x is wife to...
						encode = y + " " + 2;		//Person X is the spouse of Y. (Aux Relation)
						addEdge(x, encode);
						encode = x + " " + 0;		//Person Y is the husband of X. (Aux Relation)
						addEdge(y, encode);
						encode = x + " " + 2;		//Person Y is the spouse of X. (Aux Relation)
						addEdge(y, encode);
						break;
					case 3: case 4:				    //x is son/daughter to...
						encode = y + " " + 5;		//Person X is the child of Y. (Aux Relation)
						addEdge(x, encode);
					case 5:	encode = x + " " + 6;	//y is the parent to x
						addEdge(y, encode);
						break;
					case 7: case 8:				    //x is the mother/father to...
						encode = x + " " + 5;		//Person Y is the child of X. (Aux Relation)
						addEdge(y, encode);
						break;
					case 9:	case 11:			    //x is the brother/sister to...
						encode = y + " " + 10;
						addEdge(x, encode);
						encode = x + " " + 10;		//Y is the sibling to X (Aux Relation)
						addEdge(y, encode);
						break;
				}
			}
			
			
			for(int v = 0; v < V; v++) {
				for(String w : adj[v]) { // Contains pair: Name, Relationship
					String word[] = w.split(" ");
					for(String u : adj[Integer.parseInt(word[0])]) {
						//Finding extraneous cases for making an edge
						String word2[] = u.split(" ");
						int a = Integer.parseInt(word2[1]);
						if( (Integer.parseInt(word[1]) == 0) || (Integer.parseInt(word[1]) == 1) || (Integer.parseInt(word[1]) == 2) ) {
							if( a == 6) {
								encode = word2[0] + " " + 6;
								addEdge(v, encode);
							}
							else if( a == 7 ) {
								encode = word2[0] + " " + 8;
								addEdge(v, encode);
							}
							else if( a == 8 ) {
								encode = word2[0] + " " + 7;
								addEdge(v, encode);
							}
						}
					}
					
					ArrayList<Integer> params = new ArrayList<>(); params.add(6); params.add(7); params.add(8);
					if(params.contains(word[1])) {
						encode = v + " " + 5;
						addEdge(Integer.parseInt(word[0]), encode);
					}
				}
			}
		}
		catch (NoSuchElementException e) {
			throw new IllegalArgumentException("invalid input format in Digraph constructor", e);
		}
	}

	/**
	 * Returns the number of vertices in this digraph.
	 *
	 * @return the number of vertices in this digraph
	 */
	public int V() {
		return V;
	}

	/**
	 * Returns the number of edges in this digraph.
	 *
	 * @return the number of edges in this digraph
	 */
	public int E() {
		return E;
	}


	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
	 * Adds the directed edge v→w to this digraph.
	 *
	 * @param  v the tail vertex
	 * @param  w the head vertex
	 * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
	 */
	public void addEdge(int x, String carrier) {
		for(String s : adj[x]) {
			if(carrier.equals(s))	
				return;
		}
		validateVertex(x);
		adj[x].add(carrier);
		String[] parsed = carrier.split(" ");
		indegree[Integer.parseInt(parsed[0])]++;
	}

	/**
	 * Returns the vertices adjacent from vertex {@code v} in this digraph.
	 *
	 * @param  v the vertex
	 * @return the vertices adjacent from vertex {@code v} in this digraph, as an iterable
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public Iterable<String> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	/**
	 * Returns the number of directed edges incident from vertex {@code v}.
	 * This is known as the <em>outdegree</em> of vertex {@code v}.
	 *
	 * @param  v the vertex
	 * @return the outdegree of vertex {@code v}               
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public int outdegree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	/**
	 * Returns the number of directed edges incident to vertex {@code v}.
	 * This is known as the <em>indegree</em> of vertex {@code v}.
	 *
	 * @param  v the vertex
	 * @return the indegree of vertex {@code v}               
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public int indegree(int v) {
		validateVertex(v);
		return indegree[v];
	}

	/**
	 * Returns a string representation of the graph.
	 *
	 * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,  
	 *         followed by the <em>V</em> adjacency lists
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		String word[];
		s.append(V + " vertices, " + E + " edges " + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(namesList.get(v) + ": ");
			for (String w : adj[v]) {
				word = w.split(" ");
				s.append(linkST.get(Integer.parseInt(word[1])) + ",");
				s.append(namesList.get(Integer.parseInt(word[0])) + " ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	/**
	 * Unit tests the {@code Digraph} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		try {
			
			int vertices = 0;			
			int edges = 0;

			String temp[];
			
			//Add the Relevant Relationships with Respect to the Given File
			ArrayList<String> linkST = new ArrayList<>();	
			linkST.add("husband");				//0
			linkST.add("wife");				    //1
			linkST.add("spouse");				//2
			linkST.add("son");				    //3
			linkST.add("daughter");				//4
			linkST.add("child");				//5
			linkST.add("parent");				//6	
			linkST.add("mother");				//7
			linkST.add("father");				//8
			linkST.add("sister");				//9
			linkST.add("sibling");				//10
			linkST.add("brother");				//11

			ArrayList<String> namesList = new ArrayList<String>();	//Creates the symbol table representation for the vertices.

			//Get the File Path and set input as that filepath for Scanner
			System.out.println("Please enter the file path: ");
			Scanner input = new Scanner(System.in);
			File file = new File(input.nextLine());
			input = new Scanner(file);

			String names;
			String[] elementList;
			while(input.hasNextLine()) {
				names = input.nextLine();
				elementList = names.split(",");
				if(!namesList.contains(elementList[0])) {namesList.add(elementList[0]);} //Counts as a Vertice
				if(!namesList.contains(elementList[2])) {namesList.add(elementList[2]);} // Counts as a Vertice
				edges++; //We went through 2 of the 3 elements, therefore there is one edge namely elementList[1]
			}

			//Prints out Vertices / Edges
			vertices = namesList.size();
			System.out.println("Number of vertices: " + vertices);
			System.out.println("Number of edges: " + edges);
			
			//Recreate input as a new Scanner for the Constructor of Digraph
			input = new Scanner(file);
			
			//Call the Constructor after object creation with the relevant parameters for functioning, and then call toString()
			Digraph G = new Digraph(vertices, edges, input, namesList, linkST);
			System.out.println(G+"\n");

			
			names = " "; // We are defining a different usage for the variable names, as a SENTINAL
			while(!names.equals("")) {
				System.out.println();
				System.out.println("Enter the relationship you would wish to find: Person1 Person2 ");
				input = new Scanner(System.in);
				names = input.nextLine();
				names.trim(); //Remove excess space on ends
				elementList = names.split(" "); //Store the two names in elementList for BFS
				
				//Call BFS on the specified element
				BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, namesList.indexOf(elementList[0]) + "" , namesList, linkST);

				//Traverse the QuickestPath to the finishing side
				for(String w : bfs.pathTo(namesList.indexOf(elementList[1]))) {
					temp = w.split(" ");
					if ( temp.length < 2 ) {System.out.print( "->" + namesList.get(Integer.parseInt(temp[0])) );} //We've found a relationship, therefore print it
					else if ( temp[0].equals( elementList[0] ) ) {System.out.print( linkST.get(Integer.parseInt(temp[1])) + "->" + namesList.get(Integer.parseInt(temp[0])) );} //If the name in temp[0] = list[0], we found a matching name, so print out relationship and name
					else { System.out.print( "->" + namesList.get(Integer.parseInt(temp[0])) ); System.out.print( "->" + linkST.get(Integer.parseInt(temp[1])) );} // Print out name and relationship
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
			System.exit(0);
		} catch (NullPointerException e) {
			System.out.println(e);
			System.exit(1);
		}
	}
}