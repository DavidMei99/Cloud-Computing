# Cooper Union ECE 465 Cloud-Computing

Di Mei & Zhihao Wang

## **Cloud-KNN**

A Multi-node Multi-threaded KNN Algorithm in JAVA

## AWS Cloud Architecture
![Alt text](https://github.com/wzhlifelover/Cloud-Computing/blob/main/img/cloud_knn.png "Cloud Architecture")

## KNN Algorithm
The k-nearest neighbors algorithm (k-NN) is a classification method. The input consists of the k closest training examples. In k-NN classification, the output is a class. An object is classified by a plurality vote of its neighbors, with the object being assigned to the class most common among its k nearest neighbors 

## Dataset and Data Preprocessing
MNIST Handwritten Digits Dataset from Yann LeCun, Courant Institute, NYU
Training Set Size: 6000
Testing Set Size: 1000
Data type: 28*28 Normalized Pixel Values


## Backend

The backend of our application mainly consists of an Amazon S3 and instances from the AWS Elastic Compute Cloud (EC2). The S3 on the backend side is mainly responsible for the temporary storage of the uploaded files as well as those that are going to be downloaded. The bucket in it also stores a pre-uploaded dataset, which is the set of training files essential to the procedure of KNN classification.

In the architecture diagram, there is a VPC and several EC2 instances inside it, which are applied to storing the backend java codes and shell scripts. There is one supervisor node and others are worker nodes. After receving the testing data from a S3 bucket, the supervisor node will evenly split it. It creates several threads, where each of them is connected to a worker node. The supervisor assigns each thread with a partial testing set and then worker nodes will process them in a concurrent fashion with the training set delivered from the s3 bucket. After each worker node finishes its assigned task, labels will be returned back to the supervisor node and then combined together. Stored as a file, the label result will be first sent back to the knn-bucket and finally downloaded by the user.

The directory tree shown below demonstrates the structure of our KNN algorithm's implementation in a multi-node multi-thread fashion. In this directory, we have the KNN scripts run on the supervisor node and client scripts run on the worker nodes. Inside the message directory, we have two message classes defining the content to be delivered: one sent from the supervisor to the worker and the other one sent in the inverse direction.

```
ece465
├── message
│   ├── ClientMessage.java
│   └── Message.java
├── ClassifyThread.java
├── Client.java
├── ClientMain.java
├── KNN.java
└── KNNmain.java
```

* *KNN.java* (line 206~233)
```java
        for(int j=0; j<NUM_THREADS; j++){
            AtomicBoolean isFinished = new AtomicBoolean(false);
            allThreads[j] = new ClassifyThread(j, start, end, clientIPs[j], 6666, isFinished);
            start = end;
            end  = end + incr;
        }
        System.out.println("Starting threads");
        // start each thread
        for(int k =0; k<NUM_THREADS; k++){
            allThreads[k].start();
        }

        // wait for each thread to finish
        for(int k=0; k<NUM_THREADS; k++){
            allThreads[k].join();
        }

        //concatenate testing labels
        for(int k=0; k<NUM_THREADS; k++){
            ArrayList<Integer> temp_labs = allThreads[k].outputLab;
            int temp_ind = allThreads[k].startIdx;
            System.out.println("startindex: " + temp_ind);
            System.out.println(temp_labs.size());
            for(int i=0; i<temp_labs.size(); i++){
                //System.out.println(temp_ind+i + " " + i);
                outputLabels.set(temp_ind+i, temp_labs.get(i));
            }
        }
```
The KNNmain.java calls the KNN class, which is mainly responsible for making threads and assigning work. Each image in the dataset is indexed in our program and the supervisor assigns the work by parsing the start and end index of the testing images to each newly built thread. After receiving the labels from each thread connected to the worker node, it also combines these partial results together to form the final result. The instance of each thread is created by calling the ClassifyThread class.

* *ClassifyThread.java* line 40~57
```java
    public ClassifyThread(int tnum, int start, int end, String CIP, int portNum, AtomicBoolean isFinished){
        this.startIdx = start;
        this.endIdx = end;
        this.tnum = tnum;
        this.clientIP = CIP;
        this.portNumber = portNum;
        this.isFinished = isFinished;
        System.out.println("Thread "+tnum+" assigned indices: "+start+" to "+end);
    }

    @Override public void run(){
        System.out.println("Thread "+tnum+" started");
        // classifyThread(startIdx, endIdx);

        System.out.println("Establishing connection on port " + portNumber);
        try(Socket socket = new Socket(clientIP, portNumber)){
            // Socket socket = serversocket.accept();
            System.out.println("Connection established on port " + portNumber);
```
In the classifythread class, there are properties like the start and end index, which are the configuration of the partial set of images assigned by the supervisor. It also establishes a connection with a worker node and then directly delivers the testing images to it. After that, it will be in a listening state, waiting for the label results from the worker node.

The ClientMain.java run on the worker node is going to receive the assigned work from a thread in the supervisor node. It calls client to do the detailed KNN classification. To be specific, the worker iterates each testing image and then computes a set of euclidean distances (for each image) with the training set. It returns the top K entries for each image and then finds the label of it, which is the mode of the label indices associated with these entries.


## Frontend

The frontend 


## Project Presentation
![Link to Project Presentation](https://youtu.be/mjnKNFuz84U)

## Future Works

* A more user-firnedly frontend
