package edu.cooper.ece465.message;

import java.io.Serializable;
import edu.cooper.ece465.*;
import java.util.*;

public class ClientMessage implements Serializable {
    private int startInd;
    private int endInd;
    private ArrayList<Integer> outputLab;

    public ClientMessage(int si, int ei, ArrayList<Integer> outputLab){
        this.startInd = si;
        this.endInd = ei;
        this.outputLab = outputLab;
    }

    public int getStartInd(){
        return this.startInd;
    }

    public int getEndInd(){
        return this.endInd;
    }

    public ArrayList<Integer> getOutputLab(){
        return this.outputLab;
    }
}
