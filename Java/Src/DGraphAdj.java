//-----------------------------------------------------
// Author: Olivia Anastassov     
// Date:   11/4/21     
// Description: directed DGraph represented with adjacency lists
//              templates for dfs, strong components etc.
//-----------------------------------------------------
import java.util.*;
public class DGraphAdj{
    private ArrayList<Integer>[] adjLists;

//local var - used to help with debugging
    final static boolean DEBUG = false;

//utilities
    public static int toMathId(int n){
        return n+ 1;
    }
    public static void printDebug(String message){
        if (DEBUG){
            System.out.println(message);
        }
    }
    public static void printArrayInt(int [] array){
        for (int i = 0; i< array.length; i++){
            printDebug("" + toMathId(i)+ ": "+ array[i]);
        }
    }

//------------------------------------- 
// Constructors 
//-------------------------------------
    public DGraphAdj(int nrVertices){
        adjLists = new ArrayList[nrVertices];//throws a warning 
        for(int i = 0; i<nrVertices; i++){
            adjLists[i] = new ArrayList<Integer>();
        }
    }
    public DGraphAdj(ArrayList<Integer>[] adjLists){
        this.adjLists = adjLists;
    }

//------------------------------------- 
// Getters
//-------------------------------------
    public ArrayList<Integer>[] getAdjLists(){
        return adjLists;
    }
    public ArrayList<Integer> getAdjLists(int i){
        return adjLists[i];
    }
    public int getNrVertices(){
        return adjLists.length;
    }
    public int getNrNeighbors(int i){
        return adjLists[i].size();
    }

//------------------------------------- 
// Setters
//-------------------------------------
    public void setAdjLists(ArrayList<Integer>[] adjLists){
        this.adjLists = adjLists;
    }
//------------------------------------- 
// Serialize and print
//-------------------------------------
    public String toString(){
        int nrVertices = this.getNrVertices();
        String graphString = "";

        for(int v = 0; v<nrVertices; v++){
            graphString += toMathId(v) + ": ";
            ArrayList<Integer> adjList = getAdjLists(v);
            for(int i = 0; i < adjList.size(); i++){
                int neighbr = adjList.get(i);
                graphString += "" + toMathId(neighbr) + " ";
            }
            if (v < nrVertices - 1){
                graphString += "\n";
            }
        }
        return graphString;
    }
    public void printDGraph(){
        System.out.println(this.toString());
    }
    //debugging helper
    private void printVisited(boolean[] visited){
        printDebug("\nvisited:");
        for(int j = 0; j<visited.length; j++){
            printDebug(toMathId(j)+ ": " + visited[j]);
        }
        printDebug("");
    }

//------------------------------------- 
// Modifiers
//-------------------------------------
    public void addEdge(int u, int v){
        if (u != v){
            adjLists[u].add(v);
        }
    }
//------------------------------------- 
// Converters
//-------------------------------------
    public DGraphEdges toDGraphEdges(){
        ArrayList<DEdge> dEdges = new ArrayList<DEdge>();
        for(int i = 0; i<this.getNrVertices(); i++){
            for(int r = 0; r<adjLists[i].size(); r++){
                dEdges.add(new DEdge(i,adjLists[i].get(r)));
            }
        }
        DGraphEdges toDGraph = new DGraphEdges(this.getNrVertices(),dEdges);
        return toDGraph;
    }

//------------------------------------- 
// Operations
//-------------------------------------
    public DGraphAdj reverseDGraph(){
        DGraphAdj revDGraph = new DGraphAdj(this.getNrVertices());
        for(int i = 0; i<this.getNrVertices(); i++){
            for(int r = 0; r<adjLists[i].size(); r++){
                revDGraph.addEdge(adjLists[i].get(r),i);
            }
        }
        return revDGraph;
    }
//------------------------------------- 
// Helper functions
//-------------------------------------
    public int[] makeStdVertexOrdering(){
        // an array of vertices in increasing order of their ids
        // 0, 1, 2, ....
        int [] stdVertexOrdering = new int [this.getNrVertices()];
        for(int i = 0; i<this.getNrVertices(); i++){
            stdVertexOrdering[i] = i;
        }
        return stdVertexOrdering;
    }
    public boolean allVisited(DfsDataStructures TSData){
        for (int i= 0; i<TSData.visited.length; i++){
            if(!TSData.visited[i]){return false;} 
        }return true;
    }
    public int nextUnvisited(DfsDataStructures TSData){
        for (int i = 0; i<TSData.visited.length; i++){
            if(!TSData.visited[i]){return i;}
        }return 0;
    }
//------------------------------------- 
// Topological Sort
//-------------------------------------
    public DfsDataStructures topologicalSort(){
        DfsDataStructures TSData = new DfsDataStructures(this.getNrVertices());
        for(int i = 0; i<TSData.vertexColor.length; i++){
            TSData.vertexColor[i] = "white";
            TSData.visited[i] = false;
        }
        while(! allVisited(TSData)){
            int next = nextUnvisited(TSData);
            TSData = marked(next, TSData);
        }
        if (TSData.hasCycles == true){
            TSData.sortedVerts.clear();
        }
        return TSData;
    }
    public DfsDataStructures marked(int n, DfsDataStructures TSData){
        TSData.visited[n] = true;
        if(TSData.vertexColor[n] == "black"){return TSData;}
        if(TSData.vertexColor[n] == "gray"){
            System.out.println("There is a cycle in the graph. Cannot be topologically sorted."); 
            TSData.hasCycles = true; 
            return TSData;
        }
        TSData.vertexColor[n] = "gray";
        for (int i = 0; i<adjLists[n].size(); i++){
            int nn = adjLists[n].get(i); //gets the individual items in the adjlist. 
            TSData = marked(nn,TSData);
        }
        TSData.vertexColor[n] = "black";
        TSData.sortedVerts.add(0,n);
        return TSData;
    }
}