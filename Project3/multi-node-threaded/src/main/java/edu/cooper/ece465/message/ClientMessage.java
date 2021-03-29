package edu.cooper.ece465.message;

import java.io.Serializable;
import edu.cooper.ece465.*;
import java.util.*;

public class ClientMessage implements Serializable {
    private Vertex minVertex;

    public ClientMessage(Vertex minVertex){
        this.minVertex = minVertex;
    }

    public Vertex getMinVertex(){
        return minVertex;
    }

    public void setMinVertex(Vertex minVertex){
        this.minVertex = minVertex;
    }
}
