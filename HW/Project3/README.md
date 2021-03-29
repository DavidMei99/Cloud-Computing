## **Project 3**
**Part3a: aws implementation and configuration/multi-node implementation**

### Building and Running
``./build.sh``
- Server node: build and run main in **prim** class 
``java -cp multi-node-threaded/target/multi-node-threaded-2.0-jar-with-dependencies.jar edu.cooper.ece465.prim``
- Client node: build and run main in **ClientMain** class
``java -cp multi-node-threaded/target/multi-node-threaded-2.0-jar-with-dependencies.jar edu.cooper.ece465.ClientMain``

### Overview
Prim’s algorithm is a **greedy** way to find a minimum spanning tree (MST) given an undirected, weighted and connected graph.
An **MST** contains all the vertices of that graph and also achieves minimum weight. In the program, we incorporate this algorithm, which makes use of multi-threading, into a multi-node architecture. This implementation will be built upon Amazon Web Services (AWS) and run in a multi-node fashion. Each node is supposed to be an executing program in the AWS Elastic Compute Service (EC2) instances. The program starts with generating an |V| * |V| adjacency matrix (|V| is the total number of vertices in the graph), which represents the input graph, in the server node. Then, the program builds different threads with each of them processing a subgraph. For each of the threads, it delievers the subgraph data to a client node where a program responsible for finding the local minimum vertex given a subgraph is running. When a single-node architecture is used, threads are serializable. After each thread receives the minimum vertex, the program on the server node computes the global minimum vertex and also updates the MST. We tested this program by using a simple graph and its detailed performance is shown below.

### Performance
This implementation was tested on local host with multiple instance of client running with the connection thorugh sockets. The running time for each testrun with different node numbers are shown in the table below.

|# of vertices| Single Node|2 Nodes      | 4 Nodes      | 
|------------|-------------| -------------|-------------| 
|6|77ms|60ms|76ms|
|10|81ms|76ms| 73ms|
|100| 106ms|80ms| 79ms|
|1000| 173ms|134ms| 113ms|
|10000|558ms| 290ms| 184ms|
</br>
We can see that the larger the number of vertices, the better performance with more nodes which matches with our expectation.

### Future Work
- Continue to optimize the performance of Prim's Algorithm
- Deploy onto Elastic Beanstalk


## Reference
[ece465_at_cooper](https://github.com/robmarano/ece465_at_cooper/tree/Session_04/threading/java)