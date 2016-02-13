/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Limits;

/**
 *
 * @author joao
 */
public class FormatFiles {

    public static final int MAXLINES = 10;
    public static final int DIVIDERRANGE = 10;
    public static int numBox = 10;

    public static double getAdjustedValue(String valueStr, Limits limits, int position) {
        double value = Double.parseDouble(valueStr);
        for (int i = 0; i < numBox; i++) {
            if (value < (limits.getSteps()[position][i] + limits.getMinValues()[position])) {
                if (Math.abs(value - (limits.getSteps()[position][i] + limits.getMinValues()[position])) < Math.abs(value - (limits.getSteps()[position][i - 1] + limits.getMinValues()[position]))) {
                    return limits.getSteps()[position][i] + limits.getMinValues()[position];
                }
                return limits.getSteps()[position][i - 1] + limits.getMinValues()[position];
            }
        }
        return limits.getMinValues()[position] + limits.getSteps()[position][numBox - 1];
    }

    public static void writeInFile(String line, int[] positions, PrintWriter writer, int size, Limits limits) {
        String[] values = line.split(",");
        for (int i = 0; i < size; i++) {
            double value = getAdjustedValue(values[positions[i]], limits, i);
            writer.print(value + ",");
        }
        writer.println();
        writer.flush();
    }

    public static void writeInFile(String line, int size, PrintWriter writer) {
        String[] values = line.split(", ");
        for (int i = 0; i < size; i++) {
            writer.print(values[i] + ",");
        }
        writer.println();
        writer.flush();
    }

    public static Limits getLimitsValues(String pathInput) {

        FileReader fileReader;
        try {
            fileReader = new FileReader(pathInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            String[] values = line.split(",");
            int size = values.length;

            Limits limits = new Limits(size);
            while (line != null) {
                values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    limits.insertMinValue(Double.parseDouble(values[i]), i);
                    limits.insertMaxValue(Double.parseDouble(values[i]), i);
                }
                line = bufferedReader.readLine();
            }
            return limits;
        } catch (Exception ex) {
            Logger.getLogger(FormatFiles.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static boolean convertTxtToArff(String pathInput, String pathTrainingOutput, String pathTestOutput, String title, String columns[], String types[], int positions[], int total, int range) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(pathInput);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            String[] values = line.split(", ");
            int size = values.length;

            HashMap<Integer, Set<String>> sets = new HashMap<Integer, Set<String>>();
            for (int i = 0; i < values.length; i++) {
                sets.put(i, new HashSet<String>());
            }

            while (line != null) {
                values = line.split(", ");
                if (!(line.contains("?") || values.length < size)) {
                    for (int i = 0; i < size; i++) {
                        Set<String> container = sets.get(i);
                        container.add(values[i]);
                    }
                }
                line = bufferedReader.readLine();
            }

            PrintWriter writerTraining = new PrintWriter(new BufferedWriter(new FileWriter(pathTrainingOutput, false)));
            PrintWriter writerTest = new PrintWriter(new BufferedWriter(new FileWriter(pathTestOutput, false)));

            //print header
            writerTraining.println("@relation Teste");
            writerTest.println("@relation Teste");
            writerTraining.println();
            writerTest.println();

            for (int i = 0; i < size; i++) {
                writerTraining.println("@attribute " + columns[i] + " {" + sets.get(i).toString().replace("[", "").replace("]", "") + "}");
                writerTest.println("@attribute " + columns[i] + " {" + sets.get(i).toString().replace("[", "").replace("]", "") + "}");
            }

            writerTraining.println();
            writerTest.println();
            writerTraining.println("@data");
            writerTest.println("@data");
            writerTraining.println();
            writerTest.println();

            writerTraining.flush();
            writerTest.flush();

            List<String> lines = new ArrayList<>();

            fileReader = new FileReader(pathInput);
            bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();

            int c = 0;
            int co = 0;
            while (line != null) {
                values = line.split(", ");
                if (line.contains("?") || values.length < size) {
                    co++;
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
                        writeInFile(lines.get(i), size, writerTraining);
                    }
                    for (int i = limit; i < lines.size(); i++) {
                        writeInFile(lines.get(i), size, writerTest);
                    }
                    lines.clear();
                }
            }
            for (int i = 0; i < lines.size(); i++) {
                writeInFile(lines.remove(i), size, writerTraining);
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
    /*
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

            for (int i = 0; i < total - 1; i++) {
                if (columns[i] != null) {
                    writerTraining.println("@attribute " + columns[i] + " " + types[i]);
                    writerTest.println("@attribute " + columns[i] + " " + types[i]);
                }
            }
            writerTraining.println("@attribute " + columns[total - 1] + " {" + types[total - 1] + "}");
            writerTest.println("@attribute " + columns[total - 1] + " {" + types[total - 1] + "}");

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
                        writeInFile(lines.get(i), positions, writerTraining, total);
                    }
                    for (int i = limit; i < lines.size(); i++) {
                        writeInFile(lines.get(i), positions, writerTest, total);
                    }
                    lines.clear();
                }
            }
            for (int i = 0; i < lines.size(); i++) {
                writeInFile(lines.remove(i), positions, writerTraining, total);
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
    */
}


