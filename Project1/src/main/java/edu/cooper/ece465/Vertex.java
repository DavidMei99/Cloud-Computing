package edu.cooper.ece465;

/**
* The Vertex class implements every node in the graph
*/
public class Vertex {
    int src;
    int val;
    int dist;

    public Vertex(int src, int val, int dist){
        this.src = src;
        this.val = val;
        this.dist = dist;

    }
}
