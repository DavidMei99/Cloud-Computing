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

public class ClientMain {
    public final int VEC_VAL = 28*28;
    int[] trainingLabels;
    ArrayList<double[]> trainingVectors;
    ArrayList<double[]> testingVectors;
    int numTrainingPoints = 6000; // number of training observations
    int numOutputPoints = 1000; // number of validation/testing labels

    public ClientMain(){
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
    private void parseTrainingLabels(String trainingLabels) throws FileNotFoundException,IOException {
        BufferedReader br = new BufferedReader(new FileReader(trainingLabels));
        System.out.println("Parsing training labels from " + trainingLabels);

        // Init label array
        this.trainingLabels = new int[numTrainingPoints];

        String label = "";
        int actualNumLabels = 0;
        int progressIncrement = numTrainingPoints / 10; //Want to report in increments of 10%

        // read the file
        System.out.print("|");
        while ((label = br.readLine()) != null) {
            // make sure we dont go out of bounds of the array
            if (actualNumLabels == numTrainingPoints) {
                System.out.println("\nError: Too many data points in training labels file");
                br.close();
                return;
            }
            // Parse the label into an int
            this.trainingLabels[actualNumLabels] = Integer.parseInt(label);

            actualNumLabels++;
            // Report progress
            if ((actualNumLabels % progressIncrement) == 0) {
                int amount = (actualNumLabels / progressIncrement) * 10;
                System.out.print(" " + amount + "% ");
            }
        }
        System.out.print("|");
        System.out.println("\nTotal labels processed: " + actualNumLabels);
        if (actualNumLabels != numTrainingPoints) {
            System.out.println("Error: Number of labels doesn't match up to number of training data points!");
        }

        br.close();
    }

    private void parseTestingVectors(String testingFileName) throws FileNotFoundException,IOException{
        BufferedReader br = new BufferedReader(new FileReader(testingFileName));
        System.out.println("Classifying datapoints from "+testingFileName);

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
    }

    public static void main(String[] args) throws IOException {
        int k = Integer.parseInt(args[0]); // k
        String trainingVectorsFileName = "multi-node-threaded/digitsDataset/trainFeatures.csv";
        String trainingLabelsFileName = "multi-node-threaded/digitsDataset/trainLabels.csv";
        String testingVectorsFileName = args[1];

        ClientMain clientm = new ClientMain();

        clientm.parseTrainingVectors(trainingVectorsFileName);
        System.out.println("");
        clientm.parseTrainingLabels(trainingLabelsFileName);
        System.out.println("");
        clientm.parseTestingVectors(testingVectorsFileName);
        System.out.println("finish parsing files!");

        Client client = new Client("localhost", 6666, k, clientm.trainingVectors, clientm.trainingLabels, clientm.testingVectors);
        long runtime = client.run();
        System.out.println(runtime);
    }


}
