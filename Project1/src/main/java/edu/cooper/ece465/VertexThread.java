package edu.cooper.ece465;
import java.util.*;
import java.lang.*;

public class VertexThread extends Thread{
    List <Vertex> remain;
    Vertex minVertex;
    public VertexThread(List<Vertex> remain){
        this.remain = remain;
    }
    public void run(){
        int min = 99999;
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
