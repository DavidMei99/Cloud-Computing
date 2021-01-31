package edu.cooper.ece465;

import java.util.*;
import java.lang.*;

/**
* The MST class implements an application that uses multi-threading
* to create an MST, displayed in standard output.
*/
public class MST {
    public void findMST(int wtMat[][], int v, int src, int div){
        List<Vertex> tree = new ArrayList<>();
        List<Vertex> remain = new ArrayList<>();
        int vcount = 1;
        Vertex minVertex;

        Vertex srcVertex = new Vertex(src, src, 0);
        tree.add(srcVertex);

        // initialize the graph by creating new vertices
        for (int i=0; i<v; i++){
            if (i != src){
                Vertex vertex = new Vertex(src, i, wtMat[src][i]);
                remain.add(vertex);
            }
        }

        VertexThread[] threads = new VertexThread[div];

        int segSize, segLim, threadCount;
        int min = 999;
        while (vcount<v){
            segSize = (int) Math.ceil((double)remain.size()/div);
            //if (segSize == 0) segSize = remain.size();
            threadCount = 0;

            for (int i=0; i<remain.size(); i+=segSize){
                if (i+segSize < remain.size()) segLim = i+segSize;
                else segLim = remain.size();
                int index = i/segSize;
                threads[index] = new VertexThread(remain.subList(i, segLim));
                threads[index].start();
                threadCount++;
            }

            try{
                for (int i=0; i<threadCount; i++){
                    threads[i].join();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            

            

        }






    }
}
