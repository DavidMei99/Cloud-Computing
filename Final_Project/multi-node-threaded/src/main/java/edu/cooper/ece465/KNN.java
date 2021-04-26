/**
 * K Nearest Neighbors
 * implementation of the server side
 */

package edu.cooper.ece465;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.lang.Thread;
import java.lang.Runnable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Scanner;

public class KNN{
    public final int K_VAL;
    public final int VEC_VAL;

    public int numTrainingPoints = 6000; // number of training observations
    public int numOutputPoints = 1000; // number of validation/testing labels

    public boolean trained = false;

    public int[] trainingLabels;
    public ArrayList<double[]> trainingVectors;
    public ArrayList<double[]> testingVectors; // correspond to outputVectors
    public ArrayList<Integer> outputLabels;

    /**
     * Initializes the KNN classifier by reading in the training data from the given file
     *
     * @param  k                number of neighbors to look at
     * @param  vectorSize       size of each vector in terms of number of features
     * @param  trainingVectors  the name of the file containing the training features
     * @param  trainingLabels   the name of the file containing the training labels
     */
    public KNN(int k, int vectorSize, String trainingVectors, String trainingLabels) throws FileNotFoundException,IOException{
        K_VAL = k;
        VEC_VAL = vectorSize;
        System.out.println("Initializing K-Nearest Neighbors Classifier");
        System.out.println("K: "+k);
        System.out.println("Vector size: "+vectorSize);
        System.out.println("Training Vector File: "+trainingVectors);
        System.out.println("Training Labels File: "+trainingLabels);
        System.out.println("");

        parseTrainingVectors(trainingVectors);
        System.out.println("");
        parseTrainingLabels(trainingLabels);

        trained = true;
        System.out.println("");
    }

    /**
     * Reads the file pointed to by trainingVectors and stores the vectors in the ArrayList trainingVectors
     * @param trainingVectors filename of file containing the training vectors
     */
    private void parseTrainingVectors(String trainingVectors) throws FileNotFoundException,IOException{
        BufferedReader br = new BufferedReader(new FileReader(trainingVectors));
        System.out.println("Parsing training vectors from "+trainingVectors);

        String vector = "";
        String csvSplitOn = ","; //split on comma for csvs
        int actualNumVectors = 0;

        int progressIncrement = numTrainingPoints/10; //Want to report in increments of 10%

        // Init the training vectors array
        this.trainingVectors = new ArrayList<double[]>(numTrainingPoints);

        // Read the file
        System.out.print("|");
        while((vector = br.readLine()) != null){
            // Split on commas
            String[] features = vector.split(csvSplitOn);

            // Ensure each vector has same size
            if(features.length != VEC_VAL){
                System.out.println("Incorrect vector size on vector "+(actualNumVectors+1));
                br.close();
                return;
            }
            // Parse each feature in the vector and put it in corresponding vector array
            double[] vectorVals = new double[VEC_VAL];
            for(int i=0; i<VEC_VAL; i++){
                vectorVals[i] = Double.parseDouble(features[i]);
            }
            this.trainingVectors.add(vectorVals);
            actualNumVectors++;

            // Report progress
            if((actualNumVectors % progressIncrement) == 0){
                int amount = (actualNumVectors / progressIncrement)*10;
                System.out.print(" "+amount+"% ");
            }
        }
        System.out.print("|");
        System.out.println("\nTotal vectors processed: "+actualNumVectors);
        numTrainingPoints = actualNumVectors;

        br.close();
    }

    /**
     * Reads the file pointed to by trainingLabels and stores the labels in the trainingLabels array
     * @param trainingLabels filename
     */
    private void parseTrainingLabels(String trainingLabels) throws FileNotFoundException,IOException{
        BufferedReader br = new BufferedReader(new FileReader(trainingLabels));
        System.out.println("Parsing training labels from "+trainingLabels);

        // Init label array
        this.trainingLabels = new int[numTrainingPoints];

        String label = "";
        int actualNumLabels = 0;
        int progressIncrement = numTrainingPoints/10; //Want to report in increments of 10%

        // read the file
        System.out.print("|");
        while((label = br.readLine()) != null){
            // make sure we dont go out of bounds of the array
            if(actualNumLabels == numTrainingPoints){
                System.out.println("\nError: Too many data points in training labels file");
                br.close();
                return;
            }
            // Parse the label into an int
            this.trainingLabels[actualNumLabels] = Integer.parseInt(label);

            actualNumLabels++;
            // Report progress
            if((actualNumLabels % progressIncrement) == 0){
                int amount = (actualNumLabels / progressIncrement) * 10;
                System.out.print(" "+amount+"% ");
            }
        }
        System.out.print("|");
        System.out.println("\nTotal labels processed: "+actualNumLabels);
        if(actualNumLabels != numTrainingPoints){
            System.out.println("Error: Number of labels doesn't match up to number of training data points!");
        }

        br.close();
    }

    /**
     * Opens the input file and loads all of the input data points into memory, then splits work across threads and launches classification
     * @param  inputVectors          the file containing all of the input vectors to classify
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InterruptedException
     */
    public void classify(String inputVectors) throws FileNotFoundException, IOException, InterruptedException{
        BufferedReader br = new BufferedReader(new FileReader(inputVectors));
        List<Integer> portlist = new ArrayList<>();



        System.out.println("Classifying datapoints from "+inputVectors);

        String vector;
        this.testingVectors = new ArrayList<double[]>(numOutputPoints);
        System.out.println("Reading vectors from file");
        while((vector = br.readLine()) != null){
            String[] v = vector.split(",");
            double[] vVals = new double[VEC_VAL];
            for(int i=0; i<v.length; i++){
                vVals[i] = Double.parseDouble(v[i]);
            }
            this.testingVectors.add(vVals);
        }

        // Setting up output array
        System.out.println("Setting up output array");
        this.outputLabels = new ArrayList<Integer>(numOutputPoints);
        for(int i=0; i<numOutputPoints; i++){
            this.outputLabels.add(-1);
        }
        System.out.println("Setting up threads");
        int NUM_THREADS = 4;
        ClassifyThread[] allThreads = new ClassifyThread[NUM_THREADS];
        int incr = numOutputPoints/NUM_THREADS;
        int start = 0;
        int end = incr;

        Scanner sc = new Scanner(System.in);
        for (int i=0; i<NUM_THREADS; i++){
            System.out.print("Enter a port number: ");
            //int port = sc.nextInt();
            int port = 6666+i;
            portlist.add(port);
        }


        for(int j=0; j<NUM_THREADS; j++){
            AtomicBoolean isFinished = new AtomicBoolean(false);
            allThreads[j] = new ClassifyThread(j, start, end, portlist.get(j), isFinished);
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

        System.out.println("All threads finished");
    }

    /**
     * Writes the contents of outputLabels array to outputFileName in CSV format
     * @param outputFileName what to name the output file
     */
    public void outputClassification(String outputFileName) throws IOException{
        System.out.println("Outputting classification to "+outputFileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));

        for(int i=0; i<numOutputPoints; i++){
            String v = outputLabels.get(i).toString();
            bw.write(v);
            bw.newLine();
        }

        System.out.println("Done outputing labels");
        bw.close();
    }

    /**
     * Compares the resulting classification with known correct labels and reports the percentage correct
     * @param  correctOutputName file containing the correct output labels
     * @param  reportNum         will print both vectors on an incorrect match to file missedPoints.txt for the first 'reportNum' mistakes
     */
    public void checkOutput(String correctOutputName, int reportNum) throws FileNotFoundException, IOException{
        if(outputLabels.size() == 0){
            System.out.println("No output to compare with");
            return;
        }
        BufferedReader br = new BufferedReader(new FileReader(correctOutputName));
        System.out.println("Comparing output labels with correct labels in "+correctOutputName);

        BufferedWriter bw;
        if(reportNum > 0)
            bw = new BufferedWriter(new FileWriter("missedPoints.txt"));
        else bw = null;

        int i = 0;
        int numErrors = 0;
        String line = "";

        while(((line = br.readLine()) != null) && i < numOutputPoints){
            int correctLabel = Integer.parseInt(line);
            if(correctLabel != outputLabels.get(i)){
                numErrors++;
                if(reportNum >0 && numErrors <= reportNum){
                    bw.write("Error: mismatch on vector " +i + ", was " + outputLabels.get(i) + ", should be " + correctLabel);
                    bw.newLine();
                    bw.write("Vector: <");
                    double[] badPoint = this.testingVectors.get(i);
                    for(int j=0; j<VEC_VAL; j++){
                        bw.write(String.valueOf(badPoint[j]));
                        if(j!=(VEC_VAL-1)){
                            bw.write(", ");
                        }
                    }
                    bw.write(">");
                    bw.newLine();
                    bw.newLine();
                }
            }
            i++;
        }

        if(i < numOutputPoints){
            System.out.println("Error: Not enough points in the correct file!");
        }
        else if(line != null){
            System.out.println("Error: More points in the correct file then in classified data!");
        }
        br.close();
        if(reportNum > 0)
            bw.close();
        double acc = ((double)(numOutputPoints-numErrors))/numOutputPoints;
        System.out.println("Accuracy: "+(numOutputPoints-numErrors)+" out of "+numOutputPoints+", "+ (acc*100) +"%");
    }

}