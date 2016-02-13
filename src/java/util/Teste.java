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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Limits;
import static util.FormatFiles.DIVIDERRANGE;
import static util.FormatFiles.MAXLINES;
import static util.FormatFiles.getLimitsValues;
import static util.FormatFiles.numBox;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author joao
 */
public class Teste {

    public static void writeInFile(String line, int size, PrintWriter writer) {
        String[] values = line.split(", ");
        for (int i = 0; i < size; i++) {
            writer.print(values[i] + ",");
        }
        writer.println();
        writer.flush();
    }

    public static void main(String[] args) {
        FileReader fileReader;
        try {
            fileReader = new FileReader("/home/joao/NetBeansProjects/Toptype/data/adult.data");
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
            
            PrintWriter writerTraining = new PrintWriter(new BufferedWriter(new FileWriter("/home/joao/NetBeansProjects/Toptype/data/adult-training.arff", false)));
            PrintWriter writerTest = new PrintWriter(new BufferedWriter(new FileWriter("/home/joao/NetBeansProjects/Toptype/data/adult-test.arff", false)));

            //print header
            writerTraining.println("@relation Teste");
            writerTest.println("@relation Teste");
            writerTraining.println();
            writerTest.println();

            for (int i = 0; i < size; i++) {
                writerTraining.println("@attribute c" + i + " {" + sets.get(i).toString().replace("[", "").replace("]", "") + "}");
                writerTest.println("@attribute c" + i + " {" + sets.get(i).toString().replace("[", "").replace("]", "") + "}");
            }

            writerTraining.println();
            writerTest.println();
            writerTraining.println("@data");
            writerTest.println("@data");
            writerTraining.println();
            writerTest.println();

            writerTraining.flush();
            writerTest.flush();

            int range = 70;

            List<String> lines = new ArrayList<>();

            fileReader = new FileReader("/home/joao/NetBeansProjects/Toptype/data/adult.data");
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

            createTree();
            System.out.println("Valores faltantes:" + co);

            createRules();
            
            createBayes();
        } catch (Exception ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void createTree() {
        try {
            J48 j48 = new J48();

            BufferedReader readerTraining = new BufferedReader(new FileReader("/home/joao/NetBeansProjects/Toptype/data/adult-training.arff"));
            Instances instancesTraining = new Instances(readerTraining);
            instancesTraining.setClassIndex(instancesTraining.numAttributes() - 1);

            j48.buildClassifier(instancesTraining);

            BufferedReader readerTest = new BufferedReader(new FileReader("/home/joao/NetBeansProjects/Toptype/data/adult-test.arff"));
            //BufferedReader readerTest = new BufferedReader(new FileReader(pathTrainingOutput));
            Instances instancesTest = new Instances(readerTest);
            instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

            int corrects = 0;

            for (int i = 0; i < instancesTest.size(); i++) {
                Instance instance = instancesTest.get(i);
                double correctValue = instance.value(instance.attribute(instancesTest.numAttributes() - 1));
                double classification = j48.classifyInstance(instance);

                if (correctValue == classification) {
                    corrects++;
                }
            }

            Evaluation eval = new Evaluation(instancesTraining);
            eval.evaluateModel(j48, instancesTest);

            System.out.println(eval.toSummaryString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createRules() {
        try {
            Apriori apriori = new Apriori();

            System.out.println("Iniciando Regra de Associação");

            BufferedReader readerTraining = new BufferedReader(new FileReader("/home/joao/NetBeansProjects/Toptype/data/adult-training.arff"));
            Instances instancesTraining = new Instances(readerTraining);
            instancesTraining.setClassIndex(instancesTraining.numAttributes() - 1);

            apriori.buildAssociations(instancesTraining);

            BufferedReader readerTest = new BufferedReader(new FileReader("/home/joao/NetBeansProjects/Toptype/data/adult-test.arff"));
            //BufferedReader readerTest = new BufferedReader(new FileReader(pathTrainingOutput));
            Instances instancesTest = new Instances(readerTest);
            instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

            AssociationRules as = apriori.getAssociationRules();
            System.out.println(as.getProducer());
            for (AssociationRule rule : as.getRules()) {
               // System.out.println(rule.toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void createBayes(){
        try{
            NaiveBayes naiveBayes = new NaiveBayes();
            
            BufferedReader readerTraining = new BufferedReader(new FileReader("/home/joao/NetBeansProjects/Toptype/data/adult-training.arff"));
            Instances instancesTraining = new Instances(readerTraining);
            instancesTraining.setClassIndex(instancesTraining.numAttributes() - 1);
            
            naiveBayes.buildClassifier(instancesTraining);
            
            BufferedReader readerTest = new BufferedReader(new FileReader("/home/joao/NetBeansProjects/Toptype/data/adult-test.arff"));
            //BufferedReader readerTest = new BufferedReader(new FileReader(pathTrainingOutput));
            Instances instancesTest = new Instances(readerTest);
            instancesTest.setClassIndex(instancesTest.numAttributes() - 1);
            
            Evaluation eval = new Evaluation(instancesTraining);
            eval.evaluateModel(naiveBayes, instancesTest);

            System.out.println(eval.toSummaryString());
            
        } catch (Exception e){
            
        }
    }
}
