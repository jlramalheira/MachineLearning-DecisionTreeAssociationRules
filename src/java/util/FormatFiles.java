/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 *
 * @author joao
 */
public class FormatFiles {

    public static final int MAXLINES = 10;
    public static final int DIVIDERRANGE = 10;

    public static void writeInFile(String line, int[] positions, PrintWriter writer) {
        int j = 0;
        String[] values = line.split(",");
        for (int i = 0; (i < values.length && j < positions.length); i++) {
            if (i == positions[j]) {
                writer.print(values[i] + ",");
                j++;
            }
        }
        writer.println();
        writer.flush();
    }

    public static boolean convertTxttoArffDecisionTree(String pathInput, String pathTrainingOutput, String pathTestOutput, String title, String columns[], String types[], int positions[], int total, int range) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(pathInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            PrintWriter writerTraining = new PrintWriter(new BufferedWriter(new FileWriter(pathTrainingOutput, false)));
            PrintWriter writerTest = new PrintWriter(new BufferedWriter(new FileWriter(pathTestOutput, false)));

            //print header
            writerTraining.println("@relation " + title);
            writerTest.println("@relation " + title);
            writerTraining.println();
            writerTest.println();

            for (int i = 0; i < columns.length - 1; i++) {
                writerTraining.println("@attribute " + columns[i] + " " + types[i]);
                writerTest.println("@attribute " + columns[i] + " " + types[i]);
            }
            writerTraining.println("@attribute " + columns[columns.length - 1] + " {" + types[columns.length - 1] + "}");
            writerTest.println("@attribute " + columns[columns.length - 1] + " {" + types[columns.length - 1] + "}");

            writerTraining.println();
            writerTest.println();
            writerTraining.println("@data");
            writerTest.println("@data");
            writerTraining.println();
            writerTest.println();

            writerTraining.flush();
            writerTest.flush();

            List<String> lines = new ArrayList<>();
            int c = 0;
            while (line != null) {
                if (line.contains("?")) {
                    line = bufferedReader.readLine();
                    continue;
                }
                lines.add(line);
                line = bufferedReader.readLine();
                c++;
                
                if (c == MAXLINES) {
                    c = 0;
                    Collections.shuffle(lines);
                    int limit = (range / DIVIDERRANGE);
                    for (int i = 0; i < limit; i++) {
                        writeInFile(lines.get(i), positions, writerTraining);
                    }
                    for (int i = limit; i < lines.size(); i++) {
                        writeInFile(lines.get(i), positions, writerTest);
                    }
                    lines.clear();
                }
            }
            for (int i = 0; i < lines.size(); i++) {
                writeInFile(lines.remove(i), positions, writerTraining);
            }
            bufferedReader.close();
            fileReader.close();
            writerTraining.close();
            writerTest.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(FormatFiles.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
