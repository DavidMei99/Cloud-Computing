package edu.cooper.ece465;

import java.util.*;

public class prim {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int v, src, div;

        System.out.print("Enter number of vertex: ");
        v = sc.nextInt();

        System.out.print("\nEnter source vertex: ");
        src = sc.nextInt();

        System.out.print("\nEnter div number: ");
        div = sc.nextInt();

        int wtMat[][] = {{0,1,3,999,999,2}, {1,0,5,1,999,999}, {3,5,0,2,1,999}, {999,1,2,0,4,999}, {999,999,1,4,0,5}, {2,999,999,999,5,0}};

        MST mst = new MST();
        mst.findMST(wtMat, v, src, div);


    }
}
