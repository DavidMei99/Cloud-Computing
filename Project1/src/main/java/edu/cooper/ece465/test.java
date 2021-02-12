package edu.cooper.ece465;

// import java.util.concurrent.ArrayBlockingQueue;
// import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.*;
import java.text.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class test {
    private static final Logger LOG = LogManager.getLogger(test.class);

    public static void main(String[] args) throws InterruptedException {

        Scanner sc = new Scanner(System.in);
        int v, src, div;


        System.out.print("\nEnter source vertex (0-#vertex): ");
        src = sc.nextInt();

        System.out.print("\nEnter thread number (default=2): ");
        div = sc.nextInt();



        //Creating BlockingQueue of size 10
        LOG.debug("Creating new queue");
        // BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
        ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();
        LOG.debug("Creating new queue - DONE");
        LOG.debug("Creating producer");
        int wtMat[][] = {{0, 1, 3, 999, 999, 2}, {1, 0, 5, 1, 999, 999}, {3, 5, 0, 2, 1, 999},
                {999, 1, 2, 0, 4, 999}, {999, 999, 1, 4, 0, 5}, {2, 999, 999, 999, 5, 0}};
        bqProducer producer = new bqProducer(queue, wtMat);
        LOG.debug("Creating producer - DONE");
        int mat_len = producer.matLength();
        int[][] wtMat2 = new int[mat_len][mat_len];
        LOG.debug("Creating consumer");
        bqConsumer consumer = new bqConsumer(queue, wtMat2);
        LOG.debug("Creating producer - DONE");

        //starting consumer to consume messages from queue
        LOG.debug("Creating consumer thread");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        LOG.debug("Creating consumer thread - RUNNING");

        //starting producer to produce messages in queue
        LOG.debug("Creating producer thread");
        Thread producerThread = new Thread(producer);
        producerThread.start();
        LOG.debug("Creating producer thread - RUNNING");

        LOG.debug("Producer and Consumer has been started");
        System.out.println("Producer and Consumer has been started");

        producerThread.join();
        consumerThread.join();
        int[][] MST_wtMat = consumer.getWtMat();


        long start = System.currentTimeMillis();
        MST mst = new MST();
        mst.findMST(MST_wtMat, MST_wtMat.length, src, 2);
        long end = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.00000");

        System.out.println("Execution time for div=2 is " + formatter.format((end - start) / 1000d) + " seconds");
        LOG.info("Program ends!");
    }

        public static void print2D ( int mat[][]){
            // Loop through all rows
            for (int[] row : mat)

                // converting each row as string
                // and then printing in a separate line
                System.out.println(Arrays.toString(row));
        }

    }



