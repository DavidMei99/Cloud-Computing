package edu.cooper.ece465.message;

import edu.cooper.ece465.*;
import java.util.*;

public class Message {
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
