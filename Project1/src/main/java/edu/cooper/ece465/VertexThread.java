package edu.cooper.ece465;

import java.util.*;
import java.lang.*;

/**
* The VertextThread defines the running process in each thread
*/
public class VertexThread extends Thread{
    List <Vertex> remain;
    Vertex minVertex;

    public VertexThread(List<Vertex> remain){
        this.remain = remain;
    }

    // find the adjacent node with minimum key value
    public void run(){
        int min = 999;
        for (int i = 0; i<remain.size(); i++){
            if (remain.get(i).dist < min){
                min = remain.get(i).dist;
                minVertex = remain.get(i);
            }
        }
    }

    public Vertex getMin(){
        return minVertex;
    }


}
