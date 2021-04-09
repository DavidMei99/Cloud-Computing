/**
 * K Nearest Neighbors
 * main function
 * @author dimei
 * @author zhihaowang
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

public class KNNmain{
    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Usage: java KNearest k TestVectorsFilename [testLabelsFilename]");
            System.exit(1);
        }

        // parse the command
        int k = Integer.parseInt(args[0]); // k
        int vectorSize = 28*28;            // default resolution of the plot
        String trainingVectorsFileName = "multi-node-threaded/digitsDataset/trainFeatures.csv";
        String trainingLabelsFileName = "multi-node-threaded/digitsDataset/trainLabels.csv";
        String testVectorsFileName = args[1];
        String testLabelsFileName = "";
        boolean checkOutput = false;
        if(args.length > 2){
            checkOutput = true;
            testLabelsFileName = args[2];
        }

        KNN classifier;

        try{
            long startTime = System.currentTimeMillis();
            classifier = new KNN(k, vectorSize, trainingVectorsFileName, trainingLabelsFileName);
            classifier.classify(testVectorsFileName);
            if(checkOutput){
                classifier.checkOutput(testLabelsFileName, 0);
            }else{
                classifier.outputClassification("KNeighborsOut.csv");
            }
            long endTime = System.currentTimeMillis();
            long total = endTime - startTime;
            total = total/1000;
            System.out.println("Total runtime: "+total+" s");
        }
        catch(FileNotFoundException fe){
            System.out.println("Could not find one of the input files");
        }
        catch(IOException ie){
            System.out.println("Error reading one of the input files");
        }
        catch(InterruptedException iie){
            System.out.println("Thread interrupted");
        }

    }
}
