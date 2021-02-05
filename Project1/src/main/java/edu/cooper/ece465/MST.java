package edu.cooper.ece465;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.lang.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
/**
* The MST class implements an application that uses multi-threading
* to create an MST, displayed in standard output.
*/
public class MST {
    private static final Logger logger = LogManager.getLogger(MST.class);
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
        long time_count = 0;
        long time_count2 = 0;

        while (vcount<v){
            segSize = (int) Math.ceil((double)remain.size()/div);
            //if (segSize == 0) segSize = remain.size();
            threadCount = 0;


            long start2 = System.nanoTime();
            // assign tasks to different threads
            for (int i=0; i<remain.size(); i+=segSize){
                if (i+segSize < remain.size()) segLim = i+segSize;
                else segLim = remain.size();
                int index = i/segSize;
                threads[index] = new VertexThread(remain.subList(i, segLim));

                long start = System.nanoTime();

                threads[index].start();

                long end = System.nanoTime();
                time_count +=(end-start);

                threadCount++;
            }

            long end2 = System.nanoTime();
            time_count2 +=(end2-start2);



            // make calling threads go to waiting state
            try{
                for (int i=0; i<threadCount; i++){
                    threads[i].join();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            //NumberFormat formatter = new DecimalFormat("#0.00000");

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

        logger.info("Thread.start time is " + time_count + " nanoseconds");
        logger.info("for loop time is " + time_count2 + " nanoseconds");

        Collections.sort(tree, new Comparator<Vertex>(){
            public int compare(Vertex n1, Vertex n2){
                return n1.val < n2.val ? -1 : (n1.val > n2.val ? 1 : 0);
            }
        });

        // display the MST in the standard output
        System.out.println("\nMinimum Spanning Tree: ");
        System.out.println("Edge\tDistance");
        /*
        for (int i=0; i<tree.size(); i++){
            //if (i != src)
                System.out.println(tree.get(i).src+" - "+tree.get(i).val+"\t\t"+tree.get(i).dist);
        }

         */
    }
}
