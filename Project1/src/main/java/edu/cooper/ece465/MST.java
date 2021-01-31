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

        // add the initial node to the tree
        Vertex srcVertex = new Vertex(src, src, 0);
        tree.add(srcVertex);
        // initialize the graph by creating new vertices
        for (int i=0; i<v; i++){
            if (i != src){
                Vertex vertex = new Vertex(src, i, wtMat[src][i]);
                remain.add(vertex);
            }
        }

        // declare an array of threads
        VertexThread[] threads = new VertexThread[div];
        // declare thread properties
        int segSize, segLim, threadCount, min;
        // create the MST through iteration
        while (vcount<v){
            segSize = (int) Math.ceil((double)remain.size()/div);
            //if (segSize == 0) segSize = remain.size();
            threadCount = 0;
            // assign tasks to different threads
            for (int i=0; i<remain.size(); i+=segSize){
                if (i+segSize < remain.size()) segLim = i+segSize;
                else segLim = remain.size();
                int index = i/segSize;
                threads[index] = new VertexThread(remain.subList(i, segLim));
                threads[index].start();
                threadCount++;
            }
            // make calling threads go to waiting state
            try{
                for (int i=0; i<threadCount; i++){
                    threads[i].join();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            // find the node with the minimum key value
            min = 999;
            minVertex = null;
            for (int i=0; i<threadCount; i++){
                Vertex tempVertex = threads[i].getMin();
                if (tempVertex != null && tempVertex.dist < min){
                    min = tempVertex.dist;
                    minVertex = tempVertex;
                }
            }
            // add minVertex to the MST and remove it from remaining vertices
            if (minVertex != null){
                tree.add(minVertex);
                remain.remove(minVertex);
            }
            // update properties of nodes
            for (int i=0; i<remain.size(); i++){
                if (wtMat[minVertex.val][remain.get(i).val] <= remain.get(i).dist){
                    remain.get(i).src = minVertex.val;
                    remain.get(i).dist = wtMat[minVertex.val][remain.get(i).val];
                }
            }
            vcount++;
        }

//        Collections.sort(tree, new Comparator<Vertex>(){
//            public int compare(Vertex n1, Vertex n2){
//                return n1.val < n2.val ? -1 : (n1.val > n2.val ? 1 : 0);
//            }
//        });

        // display the MST in the standard output
        System.out.println("\nMinimum Spanning Tree: ");
        System.out.println("Edge\tDistance");
        for (int i=0; i<tree.size(); i++){
            //if (i != src)
                System.out.println(tree.get(i).src+" - "+tree.get(i).val+"\t"+tree.get(i).dist);
        }
    }
}
