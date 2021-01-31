# Cooper Union ECE 465 Cloud-Computing

Di Mei & Zhihao Wang

## **Project 1**
**Part a: Single Node Multi-threaded Prim's Algorithm**

A Prim's algorithm is implemented with **JAVA** in JDK 11.

### Overview
Prim’s algorithm is a **greedy** way to find a minimum spanning tree (MST) given an undirected, weighted and connected graph.
An **MST** contains all the vertices of that graph and also achieves minimum weight. In our project, we present two test cases, each of them involving a graph with different scales. The graph is represented by a $|V| * |V|$ adjacency matrix, where |V| is the total number of vertices in the graph. 

### Complexity
Multi-threading is used to improve the efficiency of the Prim's Algorithm. Without such technique, the time complexity of this algorithm is $O(|V|^2)$, where V is the set of vertices in the graph. In our project, we implemented a thread-based class and each of its instances is responsible for partial optimization (find the vertex with minimum weight in a **subset** of remaining vertices). Since all threads process their tasks in a parallel fashion, Prim's Algorithm's time complexity is reduced. The time complexity is 

![partition](https://github.com/wzhlifelover/Cloud-Computing/blob/main/img/partition.png)

### Tests:
**Testcase 1 (Simple Case)**
Connected graph with 6 vertices
![Example 1](https://github.com/wzhlifelover/Cloud-Computing/blob/main/img/example1.png)




## Reference
[Parallel Graph Algorithms](https://www8.cs.umu.se/kurser/5DV050/VT10/handouts/F10.pdf)
[Multithreaded Prims](https://github.com/anurag-23/Multithreaded-Prims)