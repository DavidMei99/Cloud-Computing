/**
 * K Nearest Neighbors
 * implementation of each thread (server side)
 * build connection to different nodes
 */

package edu.cooper.ece465;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.lang.Thread;
import java.lang.Runnable;

import java.util.*;
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.io.*;
import java.util.concurrent.atomic.*;
import edu.cooper.ece465.message.*;


class ClassifyThread extends Thread{
    int startIdx;
    int endIdx;
    int tnum;
    String clientIP;
    int portNumber;
    AtomicBoolean isFinished;
    
    ArrayList<Integer> outputLab;

    public ClassifyThread(int tnum, int start, int end, String CIP, int portNum, AtomicBoolean isFinished){
        this.startIdx = start;
        this.endIdx = end;
        this.tnum = tnum;
        this.clientIP = CIP;
        this.portNumber = portNum;
        this.isFinished = isFinished;
        System.out.println("Thread "+tnum+" assigned indices: "+start+" to "+end);
    }

    @Override public void run(){
        System.out.println("Thread "+tnum+" started");
        // classifyThread(startIdx, endIdx);

        System.out.println("Establishing connection on port " + portNumber);
        try(Socket socket = new Socket(clientIP, portNumber)){
            // Socket socket = serversocket.accept();
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
                Message msg = new Message(startIdx, endIdx);
                //System.out.println(msg.getMsg());
                objectOutputStream.writeObject(msg);
                System.out.println("server write success");
                objectOutputStream.reset();

                //wait for node response
                ClientMessage cmsg = (ClientMessage)objectInputStream.readObject();
                System.out.println("server read success");
                //check received message
                if (cmsg.getOutputLab() != null) {
                    outputLab = cmsg.getOutputLab();
                }

                isFinished.set(true);
                System.out.println("isfinished"+isFinished.get());
            }

            socket.close();
            //wait for other threads to finish as well
            //barrier.await();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
        System.out.println("Thread "+ this.tnum + " finished");
    }




        
}
