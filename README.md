# Cooper Union ECE 465 Cloud-Computing

Di Mei & Zhihao Wang

## **Project 1**
**Part a: Single Node Multi-threaded Prim's Algorithm**

A Prim's algorithm is implemented with **JAVA** in JDK 11.

### Overview
Prim’s algorithm is a **greedy** way to find a minimum spanning tree (MST) given an undirected, weighted and connected graph.
An **MST** contains all the vertices of that graph and also achieves minimum weight. In our project, we present two test cases, each of them involving a graph with different scales. The graph is represented by a |V| * |V| adjacency matrix, where |V| is the total number of vertices in the graph. 

### Complexity
Multi-threading is used to improve the efficiency of the Prim's Algorithm. Without such technique, the time complexity of this algorithm is O(|V|^2), where V is the set of vertices in the graph. In our project, we implemented a thread-based class and each of its instances is responsible for partial optimization (find the vertex with minimum weight in a **subset** of remaining vertices). Since all threads process their tasks in a parallel fashion, Prim's Algorithm's time complexity is reduced. The time complexity is O(|V|^2/p)+O(|V|*log(p)), which is dependent on the number of threads used p.


### Tests:
**Test Case 1 (Simple Case)**

In case 1, we tried to find the MST of a connected graph with 6 vertices. Its topology is shown in the plot below with bold lines indicating the MST. The execution time is also recorded in the table below. 

![Example 1](https://github.com/wzhlifelover/Cloud-Computing/blob/main/img/example1.png)

Excution Time:
| Single Thread     | 4 Threads      | 
|------------|-------------| 
|0.022s|0.006s| 

**Test Case 2 (Complex Case)**

In case 2, we tried to find the MSTs of connected graphs that are larger than that in previous case. The number of their vertices goes from 10 to 40 and the execution time is recorded in the table below.

Excution Time:
|# of vertices| Single Thread     | 4 Threads      | 
|------------|------------|-------------| 
|10|0.025s | 0.008s|
|20| 0.03s| 0.015s| 
|30| 0.032s| 0.025s| 
|40| 0.044s| 0.019s| 



## Reference
[Parallel Graph Algorithms](https://www8.cs.umu.se/kurser/5DV050/VT10/handouts/F10.pdf)

[Multithreaded Prims](https://github.com/anurag-23/Multithreaded-Prims)