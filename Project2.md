## **Project 2**
**Part a: Multi Node Multi-threaded Prim's Algorithm**


### Building and Running
- Maven
``mvn clean compile``
- Build and run main in **test** class 

### Overview
Prim’s algorithm is a **greedy** way to find a minimum spanning tree (MST) given an undirected, weighted and connected graph.
An **MST** contains all the vertices of that graph and also achieves minimum weight. In the program, we incorporate this algorithm, which makes use of multi-threading, into a 2-node architecture. Such model has two sides, producer and consumer, with each of them responsible for delievering and receving graph info concurrently. The Prim's algorithm starts to be run once consumer receives a complete graph. We present one test case, where the graph is represented by a |V| * |V| adjacency matrix (|V| is the total number of vertices in the graph).

### Performance
Excution Time:
|# of vertices| 2 Threads |
|------------|------------|-------------| 
|10|0.014 s|
|20| 0.015 s| 
|30| 0.019 s| 
|40| 0.021 s| 
</br>

### Future Work
- Continue to improve performance for the Prim's algorithm
- Continue to discover the utility of multi-node implementation of the algorithm


## Reference
[ece465_at_cooper](https://github.com/robmarano/ece465_at_cooper/tree/Session_04/threading/java)