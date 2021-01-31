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
*@ @since 2021-01-29
*/
public class prim {
    private static final Logger logger = LogManager.getLogger(prim.class);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int v, src, div;

        System.out.print("Enter number of vertex: ");
        v = sc.nextInt();

        System.out.print("\nEnter source vertex: ");
        src = sc.nextInt();

        System.out.print("\nEnter div number: ");
        div = sc.nextInt();

        /*int wtMat[][] = {{0,1,3,999,999,2}, {5,0,999,8,999, 6, 999}, {1,999,0,3,2,999, 999},
                         {4,1,2,0,4,999}, {999,999,1,4,0,5}, {2,999,999,999,5,0}}; */
//        int wtMat[][] = {{0, 5,1,4,999,999,999}, {5,0,999, 8,999,6, 999}, {1,999,0,3,2,999, 999},
//                {4,8,3,0,8,999,999}, {999,999,2,999,0,7,9}, {999,999,999,8,7,0,999}, {999,999,999,999,9,999,0}};
        int wtMat[][] = new int[v][v];
        Random r=new Random();
        for(int i=0;i<v;i++) {
            for (int j = 0; j < v; j++) {
                wtMat[i][j] = r.nextInt(20);
            }
        }

        MST mst = new MST();
        long start = System.currentTimeMillis();
        mst.findMST(wtMat, v, src, div);
        long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.00000");
        System.out.print("Execution time for div=1 is " + formatter.format((end - start) / 1000d) + " seconds");
        long start2 = System.currentTimeMillis();
        mst.findMST(wtMat, v, src, div+3);
        long end2 = System.currentTimeMillis();
        System.out.print("Execution time for div=4 is " + formatter.format((end2 - start2) / 1000d) + " seconds");
    }
}
