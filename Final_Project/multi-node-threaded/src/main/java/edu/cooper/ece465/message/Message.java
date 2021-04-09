package edu.cooper.ece465.message;

import edu.cooper.ece465.*;
import java.util.*;
import java.io.*;

public class Message implements Serializable {
    private int startInd;
    private int endInd;
    //private ArrayList<double[]> trainingVec;
    //private ArrayList<double[]> testingVec;
    // public ArrayList<Integer> outputLab;

    public Message(int st, int ed){
        this.startInd = st;
        this.endInd = ed;

    }

    public int getStartInd(){
        return this.startInd;
    }

    public int getEndInd(){
        return this.endInd;
    }
    

//  public List<Vertex> getMsg(){
//        return msg;
//    }

//    public void setMsg(List<Vertex> msg){
//        this.msg = msg;
//    }

}
