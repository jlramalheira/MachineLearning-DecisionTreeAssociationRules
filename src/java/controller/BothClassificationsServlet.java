/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.FormatFiles;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author joao
 */
@WebServlet(name="BothClassifications", urlPatterns={"/BothClassifications"})
public class BothClassificationsServlet extends HttpServlet {
   
    RequestDispatcher rd;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");

        switch (action) {
            case "view": {
                int correctsDecisionTree = Integer.parseInt(request.getParameter("correctsDecisionTree"));
                int correctsNaiveBayes = Integer.parseInt(request.getParameter("correctsNaiveBayes"));
                int totalTest = Integer.parseInt(request.getParameter("totalTest"));
                int totalTrainig = Integer.parseInt(request.getParameter("totalTrainig"));
                int range = Integer.parseInt(request.getParameter("range"));
                String fileName = request.getParameter("fileName");

                request.setAttribute("correctsDecisionTree", correctsDecisionTree);
                request.setAttribute("correctsNaiveBayes", correctsNaiveBayes);
                request.setAttribute("totalTest", totalTest);
                request.setAttribute("totalTrainig", totalTrainig);
                request.setAttribute("range", range);
                request.setAttribute("fileName", fileName);

                rd = request.getRequestDispatcher("bothClassificationsView.jsp");
                rd.forward(request, response);
                break;
            }
            case "download": {
                String fileName = request.getParameter("fileName");

                String dir = "/data/";
                String path = getServletContext().getRealPath(dir);

                File file = new File(path + "/" + fileName);
                response.setContentType("text/txt");
                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentLength((int) file.length());

                FileInputStream fileInputStream = new FileInputStream(file);
                OutputStream responseOutputStream = response.getOutputStream();
                int bytes;
                while ((bytes = fileInputStream.read()) != -1) {
                    responseOutputStream.write(bytes);
                }
                break;
            }
        }
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String dir = "/data/";
        String path = getServletContext().getRealPath(dir);

        String action = request.getParameter("action");

        switch (action) {
            case "create": {
                String fileName = request.getParameter("file");

                String aux = fileName.substring(0, fileName.indexOf("."));
                String pathInput = path + "/" + request.getParameter("file");
                String pathTrainingOutput = path + "/" + aux + "-training-arff.txt";
                String pathTestOutput = path + "/" + aux + "-test-arff.txt";
                String pathBothClassifications = path + "/" + aux + "-bothClassifications.txt";

                String name = request.getParameter("name");
                int range = Integer.parseInt(request.getParameter("range"));

                int size = Integer.parseInt(request.getParameter("counter"));
                String[] columns = new String[size];
                String[] types = new String[size];
                int[] positions = new int[size];
                int counter = 0;
                for (int i = 0; i < size; i++) {
                    if (request.getParameter("column-" + (i + 1)) != null) {
                        columns[counter] = request.getParameter("column-" + (i + 1));
                        types[counter] = request.getParameter("type-" + (i + 1));
                        positions[counter] = Integer.parseInt(request.getParameter("position-" + (i + 1)));
                        counter++;
                    }
                }

                FormatFiles.convertTxtToArff(pathInput, pathTrainingOutput, pathTestOutput, name, columns, types, positions, counter, range);
                try {
                    J48 j48 = new J48();

                    BufferedReader readerTraining = new BufferedReader(new FileReader(pathTrainingOutput));
                    Instances instancesTraining = new Instances(readerTraining);
                    instancesTraining.setClassIndex(instancesTraining.numAttributes() - 1);

                    j48.buildClassifier(instancesTraining);

                    BufferedReader readerTest = new BufferedReader(new FileReader(pathTestOutput));
                    //BufferedReader readerTest = new BufferedReader(new FileReader(pathTrainingOutput));
                    Instances instancesTest = new Instances(readerTest);
                    instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

                    int correctsDecisionTree = 0;

                    for (int i = 0; i < instancesTest.size(); i++) {
                        Instance instance = instancesTest.get(i);
                        double correctValue = instance.value(instance.attribute(instancesTest.numAttributes() - 1));
                        double classification = j48.classifyInstance(instance);

                        if (correctValue == classification) {
                            correctsDecisionTree++;
                        }
                    }

                    Evaluation eval = new Evaluation(instancesTraining);
                    eval.evaluateModel(j48, instancesTest);

                    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(pathBothClassifications, false)));
                    
                    writer.println("Árvore de Decisão\n\n");

                    writer.println(j48.toString());
                    
                    writer.println("");
                    writer.println("");
                    writer.println("Results");
                    writer.println(eval.toSummaryString());
                    
                    NaiveBayes naiveBayes = new NaiveBayes();

                    naiveBayes.buildClassifier(instancesTraining);

                    eval = new Evaluation(instancesTraining);
                    eval.evaluateModel(naiveBayes, instancesTest);
                    
                    int correctsNaiveBayes = 0;

                    for (int i = 0; i < instancesTest.size(); i++) {
                        Instance instance = instancesTest.get(i);
                        double correctValue = instance.value(instance.attribute(instancesTest.numAttributes() - 1));
                        double classification = naiveBayes.classifyInstance(instance);

                        if (correctValue == classification) {
                            correctsNaiveBayes++;
                        }
                    }
                    
                    writer.println("Naive Bayes\n\n");

                    writer.println(naiveBayes.toString());
                    
                    writer.println("");
                    writer.println("");
                    writer.println("Results");
                    writer.println(eval.toSummaryString());

                    writer.close();

                    response.sendRedirect("BothClassifications?action=view&correctsDecisionTree=" + correctsDecisionTree
                            + "&correctsNaiveBayes=" + correctsNaiveBayes
                            + "&totalTest=" + instancesTest.size()
                            + "&totalTrainig=" + instancesTraining.size()
                            + "&range=" + range
                            + "&fileName=" + aux + "-bothClassifications.txt");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    response.sendRedirect("Navigation?action=decisionTree");
                }

                break;
            }
            default:
                response.sendError(404);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
