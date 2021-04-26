package edu.cooper.ece465;

import java.util.*;
import java.util.concurrent.*;
import java.time.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import edu.cooper.ece465.message.*;

public class Client {
    int K_SIZE;
    int[] trainingLabels;
    ArrayList<Integer> testingLabels;
    ArrayList<double[]> trainingVectors;
    ArrayList<double[]> testingVectors;
    private String host;
    private int portNumber;

    public Client(String host, int portNumber, int k, ArrayList<double[]> trainingVectors, int[] trainingLabels, ArrayList<double[]> testingVectors){
        this.host = host;
        this.portNumber = portNumber;
        this.K_SIZE = k;
        this.trainingLabels = trainingLabels;
        this.testingVectors = testingVectors;
        this.trainingVectors = trainingVectors;
    }

    public long run(){
        int startInd;
        int endInd;
        ClientMessage cmsg; // msg sent from client
        Message smsg;       // msg sent from server

        System.out.println("Worker started on port " + portNumber);
        Instant start = Instant.now();
        Instant end = Instant.now();
        try(Socket s = new Socket(host, portNumber)){
            System.out.println("Connection establish with " + host + "::" + portNumber);
            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());


            System.out.println("Input Smsg");
            smsg = (Message) objectInputStream.readObject();
            startInd = smsg.getStartInd();
            endInd = smsg.getEndInd();
            testingLabels = new ArrayList<>(Arrays.asList(new Integer[endInd-startInd]));


            for (int pointNum = startInd; pointNum < endInd; pointNum++) {
                System.out.println("Classifying point " + pointNum);
                double[] vectorVals = this.testingVectors.get(pointNum);

                int[] nearestJonNeighbors = nearestNeighbors(vectorVals); // indices of the closest neighbors

                // get the labels for those neighbors
                int[] neighborLabels = new int[K_SIZE];
                for (int j = 0; j < K_SIZE; j++) {
                    neighborLabels[j] = trainingLabels[nearestJonNeighbors[j]];
                }
                // Find the majority label
                int majorityLabel = majorityElem(neighborLabels);

                // Check if there was a majority element
                if (majorityLabel == -1) {
                    System.out.println("Point: " + pointNum + " No majority label elem for this item");
                    // Pick the closest point to this one
                    double bestDist = Double.MAX_VALUE;
                    int bestIdx = 0;
                    for (int i = 0; i < K_SIZE; i++) {
                        int pointIdx = nearestJonNeighbors[i]; //the actual index of the training vector
                        double d = euclidianDist(vectorVals, trainingVectors.get(pointIdx));
                        if (d < bestDist) {
                            bestDist = d;
                            bestIdx = pointIdx;
                        }
                    }
                    this.testingLabels.set(pointNum-startInd, this.trainingLabels[bestIdx]);
                    System.out.println(">>Point " + pointNum + " classified to label " + this.trainingLabels[bestIdx]);
                } else {
                    this.testingLabels.set(pointNum-startInd, majorityLabel);
                    System.out.println(">>Point " + pointNum + " classified to label " + majorityLabel);
                }
            }


            objectOutputStream.writeObject(new ClientMessage(startInd, endInd, testingLabels));
            objectOutputStream.reset();
            end = Instant.now();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Duration.between(start, end).toMillis();
    }

    /**
     * Finds the majority element in array
     * @param  array the array of values to find the majority elem in
     * @return       the majority element or -1 if not majority element
     */
    private int majorityElem(int[] array){
        int count = 0, majorityElement = 0;
        for(int i = 0; i < array.length; i++) {
            if (count == 0)
                majorityElement = array[i];
            if (array[i] == majorityElement)
                count++;
            else
                count--;
        }
        count = 0;
        for(int i = 0; i < array.length; i++)
            if (array[i] == majorityElement)
                count++;
        if (count > array.length/2)
            return majorityElement;
        return -1;
    }

    /**
     * Finds the k nearest neighbors by euclidian distance from the feature vector among the training points
     * @param  featureVector the point we are trying to find neighbors of
     * @return               an ArrayList of the indices of the nearest neighbors
     */
    private int[] nearestNeighbors(double[] featureVector){
        int[] result = new int[K_SIZE]; // The indicies of the k closest training points to the nearest neighbor
        double[] bestDistances = new double[K_SIZE]; // The k best distances from featureVector
        double largestDist = Double.MAX_VALUE; // The largest dist out off our k best distances

        // init bestDistances, result
        for(int i=0; i<K_SIZE; i++){
            result[i] = -1;
            bestDistances[i] = Double.MAX_VALUE;
        }

        // walkthrough all the training vectors and calculate euclidian distance
        for(int i=0; i<6000; i++){
            double d = euclidianDist(featureVector, trainingVectors.get(i));
            // We can replce one of the closest points in bestDistances
            if(d < largestDist){
                // Find the largest elem in bestDistances to replace
                int largestIdx = 0;
                double largest = 0;
                for(int j=0; j<K_SIZE; j++){
                    if(bestDistances[j] > largest){
                        largestIdx = j;
                        largest = bestDistances[j];
                    }
                }
                // Replace the largest elem with the newly calculated distance
                result[largestIdx] = i;
                bestDistances[largestIdx] = d;
                // Find the new largest elem
                largest = 0;
                for(int j=0; j<K_SIZE; j++){
                    if(bestDistances[j] > largest){
                        largest = bestDistances[j];
                    }
                }
                largestDist = largest;
            }
        }

        return result;
    }

    /**
     * Calculates the euclidian distance between v1 and v2
     * @param  v1 source vector
     * @param  v2 destination vector
     * @return    the euclidian distance between v1 and v2.
     */
    private double euclidianDist(double[] v1, double[] v2){
        double result = 0.0;
        for(int i=0; i<28*28; i++){
            result = result + Math.pow(v1[i] - v2[i], 2.0);
        }

        return Math.sqrt(result);
    }

}
