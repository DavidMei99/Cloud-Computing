package edu.cooper.ece465.message;

import edu.cooper.ece465.*;
import java.util.*;
import java.io.*;

public class Message implements Serializable {
    private List<Vertex> msg;

    public Message(List<Vertex> msg){
        this.msg = msg;
    }

    public List<Vertex> getMsg(){
        return msg;
    }

    public void setMsg(List<Vertex> msg){
        this.msg = msg;
    }

}
