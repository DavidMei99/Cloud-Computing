package edu.cooper.ece465;

import java.util.*;
import java.text.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
* This project uses mulit-threading to implement the Prim's Algorithm,
*  which aims to find the minimumspanning tree (MST)given an undirected graph.
*
* @author Zhihao Wang & Di Mei
* @since 2021-01-29
*/
public class prim {
    private static final Logger logger = LogManager.getLogger(prim.class);

    public static void main(String[] args) {
        logger.info("Welcome to explore Prim's Algorithm!");

        Scanner sc = new Scanner(System.in);
        int v, src, div;

        System.out.print("Enter number of vertex: ");
        v = sc.nextInt();

        System.out.print("\nEnter source vertex (0-#vertex): ");
        src = sc.nextInt();

        System.out.print("\nEnter thread number (default=2): ");
        div = sc.nextInt();

        int wtMat6[][] = {{0,1,3,999,999,2}, {1,0,5,1,999, 999}, {3,5,0,2,1,999},
                         {999, 1, 2, 0, 4, 999}, {999,999,1,4,0,5}, {2,999,999,999,5,0}};
        /*int wtMat[][] = {{0, 5,1,4,999,999,999}, {5,0,999, 8,999,6, 999}, {1,999,0,3,2,999, 999},
                {4,8,3,0,8,999,999}, {999,999,2,999,0,7,9}, {999,999,999,8,7,0,999}, {999,999,999,999,9,999,0}}; */

        // randomly generate a weight matrix representing the connected graph
        int wtMat[][] = new int[v][v];
        Random r=new Random();
        for(int i=0;i<v;i++) {
            for (int j = 0; j < v; j++) {
                float if_connect = r.nextFloat();
                if (i==j) wtMat[i][j] = 0;
                else wtMat[i][j] =999;
            }
        }
        for(int i=0;i<v;i++) {
            int line_check = 0;
            for (int j = i; j < v; j++) {
                float if_connect = r.nextFloat();
                if (i==j) {
                    wtMat[i][j] = 0;
                } else {
                    if (if_connect<0.2) {
                        wtMat[i][j] = r.nextInt(20) + 1;
                        wtMat[j][i] = wtMat[i][j];
                        line_check += 1;
                    }
                }
            }
            if (line_check == 0 && i != v-1) {
                wtMat[i][v-1] = r.nextInt(20) + 1;
                wtMat[v-1][i] = wtMat[i][v-1];
            }
        }

        System.out.println("Adjacency Matrix:  ");
        print2D(wtMat6);


        long start = System.currentTimeMillis();
        MST mst = new MST();
        mst.findMST(wtMat6, v, src, 1);
        long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.00000");

//        long start2 = System.currentTimeMillis();
//        MST mst2 = new MST();
//        mst2.findMST(wtMat, v, src, 2);
//        long end2 = System.currentTimeMillis();


        System.out.println("Execution time for div=2 is " + formatter.format((end - start) / 1000d) + " seconds");
//        System.out.println("Execution time for div=2 is " + formatter.format((end2 - start2) / 1000d) + " seconds");
        logger.info("Program ends!");
    }
    public static void print2D(int mat[][]){
        // Loop through all rows
        for (int[] row : mat)

            // converting each row as string
            // and then printing in a separate line
            System.out.println(Arrays.toString(row));
    }
}
