package edu.cooper.ece465;

//import edu.cooper.ece465.commons.utils.Utils;

// import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.PrintWriter;

public class bqConsumer implements Runnable {
    private static final Logger LOG = LogManager.getLogger(bqConsumer.class);

    // private BlockingQueue<Message> queue;
    private ConcurrentLinkedQueue<Message> queue;
    private int[][] wtMat;

    // public bqConsumer(BlockingQueue<Message> q){
    public bqConsumer(ConcurrentLinkedQueue<Message> q, int[][] wtMat){
        LOG.debug("bqConsumer - ctor");
        this.queue=q;
        this.wtMat=wtMat;
        LOG.debug("bqConsumer - ctor - DONE");
    }

    @Override
    public void run() {
        LOG.debug("bqConsumer - run() - START");
        try{
            Message msg;
            int i = 0;
            //consuming messages until exit message is received
            LOG.debug("Looping through through messages on the queue - START");
            // while((msg = queue.take()).getMsg() != "exit"){
            do {
                msg = queue.poll();
                if (msg != null) {
                    String message = msg.getMsg();
                    if (message.equals("exit")) {
                        LOG.debug(String.format("ENDING %s",msg.getMsg()));
                        break;
                    }
                    String[] arrOfStr = message.split("-", 1000);
                    i = i%arrOfStr.length;
                    int j = 0;

                    for (String a: arrOfStr) {
                        //System.out.println(a);
                        wtMat[i][j] = Integer.parseInt(a);
                        j++;
                    }
                    LOG.debug(String.format("Consumed MESSAGE = %s",msg.getMsg()));
                    //System.out.println("i="+i+"\n");
                    i++;
                }
            Thread.sleep(250);
            }
            while (true);
            LOG.debug("Looping through through messages on the queue - END");
        } catch(NoSuchElementException ex1) {
            String errorMessage = String.format("Encountered a NoSuchFieldException: %s", ex1.getMessage());
            // Utils.handleException(LOG, ex1, errorMessage);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex1.printStackTrace(pw);
            LOG.error(errorMessage, ex1);
            LOG.error(pw.toString());
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        LOG.debug("bqConsumer - run() - END");

        // for testing
        for (int i = 0; i < wtMat.length; i++){
            for (int j = 0; j < wtMat.length; j++){
                System.out.print(wtMat[i][j]+" ");
            }
            System.out.println();
        }
    }

    public int[][] getWtMat(){
        return wtMat;
    }

}
