package edu.cooper.ece465;

import java.util.*;
import java.util.concurrent.*;
import java.time.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import edu.cooper.ece465.message.*;

public class Client {
    private List<Vertex> remain;
    private Vertex minVertex;
    private String host;
    private int portNumber;

    public Client(String host, int portNumber){
        this.host = host;
        this.portNumber = portNumber;
    }

    public long run(){
        int min = 999;
        ClientMessage cmsg; // msg sent from client
        Message smsg;       // msg sent from server

        System.out.println("Worker started on port " + portNumber);
        Instant start = Instant.now();
        Instant end = Instant.now();
        try(Socket s = new Socket(host, portNumber)){
            System.out.println("Connection establish with " + host + "::" + portNumber);
            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());


            System.out.println("Input Smsg");
            smsg = (Message) objectInputStream.readObject();
            this.remain = smsg.getMsg();
            for (int i = 0; i<remain.size(); i++){
                if (remain.get(i).dist < min){
                    min = remain.get(i).dist;
                    minVertex = remain.get(i);
                }
            }

            System.out.println("Write minVertex");
            objectOutputStream.writeObject(new ClientMessage(minVertex));
            objectOutputStream.reset();
            end = Instant.now();
            //s.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Duration.between(start, end).toMillis();
    }

}
