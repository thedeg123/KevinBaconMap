import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
/**
 * My graph class. This is a directed weighted graph. Weights must be positive. 
 *
 * @author David Gold!
 */
public class MyGraph {
    //The string is the actor/movie name the vertex is that actors vertex
    String center;
    int reachedActors;
    int MovieCount;
    HashMap<String, Vertex> vertecies;
    /**
     * My vertex class. This class stores values and a linked list of edges it points to 
     *
     * @author David Gold!
     */
    class Vertex {
	boolean Movie;
	public boolean hasParent;
	private Vertex parent;
	public int distanceFrom;
	private boolean isMarked;
	private String Title;
	private LinkedList<Edge> edges;
	public Vertex(String s) {
	    this.Movie = false;
	    this.Title = s;
	    this.isMarked = false;
	    this.distanceFrom = 0;
	    this.edges = new LinkedList<Edge>();
	}
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;
	    Vertex other = (Vertex) obj;
	    if (!getOuterType().equals(other.getOuterType()))
		return false;
	    if (Title == null) {
		if (other.Title != null)
		    return false;
	    } else if (!Title.equals(other.Title))
		return false;
	    return true;
	}
	//Various getter and setter methods for vertex class
	public void isMovie() {
	    this.Movie = true;
	}
	public String getTitle() {
	    return this.Title;
	}
	public String toString() {
	    return Title;
	}
	public void mark() {
	    this.isMarked = true;
	}
	public void setParent(Vertex v) {
	    this.parent = v;
	    this.hasParent = true;
	}
	public Vertex getParent() {
	    return this.parent;
	}
	public boolean isMarked() {
	    return this.isMarked;
	}
	public void setTitle(String s) {
	    this.Title = s;
	}
	public void addEdge(Edge e) {
	    edges.add(e);
	}
	public LinkedList<Edge> getEdges(){
	    return edges;
	}
	private MyGraph getOuterType() {
	    return MyGraph.this;
	}
    }
    public int GetReachedActors(){
	return this.reachedActors;
    }
    /**
     * My edge class. stores the edge value and destination.
     *
     * @author David Gold!
     */
    private class Edge {
	int cost;
	Vertex destination;
	public Edge(int cost,Vertex destination) {
	    this.cost =cost;
	    this.destination = destination;
	}
	public int getcost() {
	    return this.cost;
	}
    }
    public MyGraph() {
	this.vertecies = new HashMap<String, Vertex>();
	this.reachedActors =0;
	this.MovieCount= 0;
    }
    public void addVertex(String s) {
	Vertex temp = new Vertex(s);
	vertecies.put(s, temp);
    }
    public void addEdge(String from, String to, int cost) {
	try {
	    vertecies.get(from).addEdge(new Edge(cost, vertecies.get(to)));
	}
	catch(NullPointerException e) {
	    System.err.println("One of the vertecies was not found in the Graph");
	}
    }
    private void clearParents() {
	this.reachedActors =0;
	this.MovieCount =0;
	Iterator ity = vertecies.entrySet().iterator();
	while (ity.hasNext()) {
	    Map.Entry temp = (Map.Entry<String, MyGraph.Vertex>) ity.next();
	    vertecies.get(temp.getKey()).setParent(null);
	    vertecies.get(temp.getKey()).distanceFrom = 0;
	    vertecies.get(temp.getKey()).isMarked = false;
	    vertecies.get(temp.getKey()).hasParent = false;
	}
    }
	//prints the path from a given vetex in the tree back to the center vertex
    public String printPath(String vertex){
	String s1 = "\n" + vertex + " -> ";
	String current = vertex;
	while(vertecies.get(current).parent != null) {
	    if(vertecies.get(current).parent.parent == null) {s1+= vertecies.get(current).parent;}
	    else {s1+= vertecies.get(current).parent + " -> ";}
	    current = vertecies.get(current).parent.toString();
	}
	if(vertex.equals(center)) {
	    return (vertex + " is the center and has a distance of 0");
	}
	if(s1.contains(vertex)) {
	    return s1 + " (" + vertecies.get(vertex).distanceFrom + ")";
	}
	else return (vertex + " is unreachable!");
    }
	//updates the number of movies in the graph
    private void getMovieCount() {
	for(Vertex s1: vertecies.values()) {
	    if(s1.Movie) {
		this.MovieCount++;
	    }
	}
    }
//populates a linked list with all connected nodes
    public LinkedList<String> getConnected(){
	LinkedList<String> queue = new LinkedList<>();
	for(Vertex v: vertecies.values()) {
	    if(v.hasParent && v.Movie ==false) {
		queue.add(v.toString());
	    }
	}
	return queue;
    }
    public void findPath(String centers) {
	//a modified bfs algorithm keeping a counter of the actors weve travered
	this.clearParents();
	this.getMovieCount();
	this.center = centers;
	Vertex center = vertecies.get(centers);
	Queue<Vertex> queue = new LinkedList<>();
	queue.add(center);
	reachedActors++;
	//This next line of code is courtesy of Chris Egerton, the God of CS and Cello himself.
	center.mark();
	while(!queue.isEmpty()) {
	    Vertex temp = queue.poll();
	    for(int i=0;i<temp.getEdges().size();i++) {
		Vertex surrounding = temp.getEdges().get(i).destination;
		if(!surrounding.isMarked && surrounding.Movie == false) {reachedActors++;}
		if(!surrounding.isMarked) {
		    surrounding.mark();
		    surrounding.distanceFrom = temp.distanceFrom + temp.getEdges().get(i).cost;
		    surrounding.setParent(temp);
		    queue.add(surrounding);
		}
	    }
	}
    }
	//for finding avg distance in tree
    public int getAllDistance(Integer i) {
	int z=0;
	for(Vertex v: vertecies.values()) {
	    if(v.distanceFrom == i && v.Movie==false && v.hasParent) {z++;}
	}
	return z;
    }
    public int getCost(String vertex) {
	return vertecies.get(vertex).distanceFrom;
    }
    public boolean contains(String s) {
	return vertecies.containsKey(s);
    }
}
