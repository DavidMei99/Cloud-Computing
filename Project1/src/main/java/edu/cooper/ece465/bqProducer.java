package edu.cooper.ece465;

// import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class bqProducer implements Runnable {
    private final static int MAX_QUEUE_SIZE = 10;
    private static final Logger LOG = LogManager.getLogger(bqProducer.class);

    // private BlockingQueue<Message> queue;
    private ConcurrentLinkedQueue<Message> queue;
    private int wtMat[][];

    // public bqProducer(BlockingQueue<Message> q){
    public bqProducer(ConcurrentLinkedQueue<Message> q, int[][] wtMat) {
        LOG.debug("bqProducer - ctor");
        this.queue = q;
        this.wtMat = wtMat;
        LOG.debug("bqProducer - ctor - DONE");
    }

    public int matLength(){
        return wtMat.length;
    }

    @Override
    public void run() {
        LOG.debug("bqProducer - run() - START");
        //produce messages
        for (int i = 0; i < wtMat.length; i++) {
            String temp = "";
            for (int j = 0; j < wtMat.length; j++) {
                temp += wtMat[i][j];
                if (j!=wtMat.length-1){
                    temp += "-";
                }
            }
            Message msg = new Message(temp);
            try {
                Thread.sleep(1000);
                LOG.debug("Putting message on the queue - START");
                // queue.put(msg);
                queue.add(msg);
                LOG.debug("Putting message on the queue - END");
                LOG.debug(String.format("Produced MESSAGE = %s", msg.getMsg()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //adding exit message
        Message msg = new Message("exit");
        try {
            // queue.put(msg);
            queue.add(msg);
            // } catch (InterruptedException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.debug("bqProducer - run() - END");
    }
}