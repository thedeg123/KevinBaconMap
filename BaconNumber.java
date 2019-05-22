import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
/**
 * A class which reads in the data for the kevin bacon game and allows a user to search
 * and modify center and other functions as well.
 *
 * @author David Gold!
 */
public class BaconNumber {
    //The center	
    String from;
    MyGraph graphy;
    public BaconNumber(Scanner s) {
	this.graphy = new MyGraph();
	//reading in a file of actors names and movies they have been in to populate graph
	while (s.hasNext()) {
	    String banana = s.nextLine();
	    //yummy
	    String[] parts = banana.split("\\|"); 
	    if(graphy.contains(parts[0]) && graphy.contains(parts[1])) {
		graphy.addEdge(parts[0], parts[1], 0);
		graphy.addEdge(parts[1], parts[0], 1);
	    }
	    else if (graphy.contains(parts[1])) {
		graphy.addVertex(parts[0]);
		graphy.addEdge(parts[0], parts[1], 0);
		graphy.addEdge(parts[1], parts[0], 1);
	    }
	    else if (graphy.contains(parts[0])) {
		graphy.addVertex(parts[1]);
		graphy.vertecies.get(parts[1]).isMovie();
		graphy.addEdge(parts[0], parts[1], 0);
		graphy.addEdge(parts[1], parts[0], 1);
	    }
	    else {
		graphy.addVertex(parts[0]);
		graphy.addVertex(parts[1]);
		graphy.vertecies.get(parts[1]).isMovie();
		graphy.addEdge(parts[0], parts[1], 0);
		graphy.addEdge(parts[1], parts[0], 1);
	    }
	}
    }
    public static void main(String[] args) throws MalformedURLException, IOException {

	BaconNumber theBaconator = new BaconNumber(new Scanner( new URL(args[0]).openStream()));
	theBaconator.recenter("Kevin Bacon (I)");
	System.out.println("This is a Kevin Bacon game (based off: https://oracleofbacon.org/).\n Here are some actions you can do:\n");
	//@deadmau5
	Scanner inputy = new Scanner(System.in);
	//dealing with user io
	while(1<2) {
	    try{
		LinkedList<String> input = new LinkedList<String>();
		printActions();
		String temp = inputy.nextLine();
		if(temp.length() < 1) {System.err.println("Invalid command");continue;}
		if(temp.contains(" ")) {
		    input.add(temp.substring(0, temp.indexOf(' ')));
		    input.add(temp.substring(temp.indexOf(' ') + 1));
		}
		else {input.add(temp);}
		switch (input.get(0)){
		case "find": {System.out.println(theBaconator.find(input.get(1))+"\n"); break;}
		case "center": {if(theBaconator.recenter(input.get(1))) {System.out.println("Center Succesfully updated!\n");}else {System.out.println("Sorry that actor/movie was not found!\n");}break;} 
		case "avgdist": {System.out.println(theBaconator.avgdist()+ " "+ theBaconator.graphy.center + " (" + (theBaconator.graphy.reachedActors)+ "," + (theBaconator.graphy.vertecies.size()-theBaconator.graphy.reachedActors-theBaconator.graphy.MovieCount) + ")\n"); break;}
		case "top": {theBaconator.topCenter(Integer.parseInt(input.get(1)));break;}
		case "table": {theBaconator.printTable();break;}
		}
	    }
	    catch(IndexOutOfBoundsException e) {
		System.err.println("Invalid Command");    
	    }
	}
    }
    //s is the center. returns avg distance in graph
    public double avgdist() {
	double temp =0;
	for(String s1: graphy.vertecies.keySet()) {
	    if(!graphy.vertecies.get(s1).Movie) {
		temp+= graphy.getCost(s1);
	    }
	}
	return (double) temp/ graphy.reachedActors;
    }
	//gets the top actors around the center given their number of neighbors i
    public PriorityQueue<topObj> topCenter(Integer i) {
	int temp =0;
	PriorityQueue<topObj> priorityQueue = new PriorityQueue<topObj>(new cmp());
	for(String s1: graphy.getConnected()) {
	    temp=0;
	    this.recenter(s1);
	    for(String s2: graphy.vertecies.keySet()) {
		if (!graphy.vertecies.get(s2).Movie) {
		    temp+= graphy.getCost(s2);
		}
	    }
	    priorityQueue.add(new topObj(s1, (double) temp/ graphy.reachedActors));
	}
	for(int z=0;z<i;z++) {
	    topObj temp2 = priorityQueue.poll();
	    System.out.println(temp2.temp + " " + temp2.name);
	}
	return priorityQueue;
    }
    private class topObj {
	public double temp;
	public String name;
	public topObj(String name, double temp) {
	    this.name = name;
	    this.temp = temp;
	}
	public double getTemp() {
	    return temp;
	}
	public void setTemp(double temp) {
	    this.temp = temp;
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}


    }
    private class cmp implements Comparator<topObj>{
	@Override
	public int compare(topObj o1, topObj o2) {
	    if(o1.temp < o2.temp) {return -1;}
	    if(o1.temp > o2.temp) {return 1;}
	    if(o1.temp == o2.temp) {return 0;}
	    return 0;
	}
    }
	//prints metadata on graph
    public void printTable() {
	int getAllz = 0;
	System.out.println("Table of distances for " + graphy.center + "\nNumber 0" +": "+ 1);
	for(int i=1;i<100;i++) {
	    int z = graphy.getAllDistance(i);
	    getAllz+=z;
	    if(z == 0) {break;}
	    System.out.println("Number " + i+": "+ z);
	}
	System.out.println("Unreachable: "+ (graphy.vertecies.size()-getAllz-graphy.MovieCount-1));
    }
	//updates center node
    public boolean recenter(String s) {
	if(graphy.contains(s)) {graphy.center = s; graphy.findPath(s); return true;}
	else return false;
    }
	//find the path to a given vertex in the graph
    public String find(String s) {
	//yes this is in reversed order but the linkedlist prints in reverse order so its right
	if(graphy.contains(s)) {return graphy.printPath(s);}
	else {System.err.println("That vertex was not found."); return "";}
    }
    public static void printActions() {
	System.out.println("Find Name: Enter find followed by name you want to enter.\nChange Center: enter center followed by the name you want to look from\nAverage Distance: enter avgdist\nTop Center: enter top\nTable: enter table");
    }
}
