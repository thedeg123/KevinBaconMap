import org.junit.Test;

public class MyGraphTest {
    /**
     * Some simple junit tests.
     *
     * @author David Gold!
     */
    @Test
    public void testAddandFindPath() {
	MyGraph graphy = new MyGraph();
	graphy.addVertex("actor");
	graphy.addVertex("movie");
	graphy.addVertex("actor2");
	graphy.addVertex("movie2");
	graphy.addVertex("actor3");
	graphy.addVertex("movie3");
	//graphy.addEdge("actor", "movie3" ,100);
	
	graphy.addEdge("actor", "actor2" ,1);
	graphy.addEdge("actor2", "actor3" ,1);
	graphy.addEdge("actor3", "movie3" ,1);
	graphy.addEdge("actor", "movie3" , 100);
	
	//graphy.addEdge("actor", "movie3" ,2);
	graphy.addEdge("movie3", "actor3" ,0);
	graphy.addEdge("movie3", "actor" ,0);
	graphy.findPath("movie3");
	//System.out.println(graphy.printPath("actor"));
	//graphy.addEdge( "movie3","actor3" ,1);
	
    }
}
