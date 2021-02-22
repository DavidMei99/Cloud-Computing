package edu.cooper.ece465;

import java.util.*;
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.io.*;
import java.util.concurrent.atomic.*;
import edu.cooper.ece465.message.*;
/**
* The VertextThread defines the running process in each thread
*/
public class VertexThread extends Thread{
    List <Vertex> remain;
    Vertex minVertex;
    int portNumber;
    AtomicBoolean isFinished;
    // CyclicBarrier barrier;
    // ClientMessage cmsg;
    // Message smsg;

    public VertexThread(List<Vertex> remain, int portNumber, AtomicBoolean isFinished){
        this.remain = remain;
        this.portNumber = portNumber;
        this.isFinished = isFinished;
        // this.barrier= barrier;
    }

    // find the adjacent node with minimum key value
//    public void run(){
//        int min = 999;
//        for (int i = 0; i<remain.size(); i++){
//            if (remain.get(i).dist < min){
//                min = remain.get(i).dist;
//                minVertex = remain.get(i);
//            }
//        }
//    }

    public void run(){
        System.out.println("Establishing connection on port " + portNumber);
        try(ServerSocket serversocket = new ServerSocket(portNumber)){
            Socket socket = serversocket.accept();
            System.out.println("Connection established on port " + portNumber);

            // Setup write to client
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("finished write to client");
            // Setup read from client
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("finished read from client");


            System.out.println("isfinished"+isFinished.get());

            while (!isFinished.get()) {
                //System.out.println("while loop in server starts");
                Message msg = new Message(remain);
                System.out.println(msg.getMsg());
                objectOutputStream.writeObject(msg);
                System.out.println("server write success");
                objectOutputStream.reset();

                //wait for node response
                ClientMessage cmsg = (ClientMessage)objectInputStream.readObject();
                //check received message
                if (cmsg.getMinVertex() != null) {
                    minVertex = cmsg.getMinVertex();
                }

                isFinished.set(true);
            }

            socket.close();
            //wait for other threads to finish as well
            //barrier.await();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    public Vertex getMin(){
        return minVertex;
    }

}
