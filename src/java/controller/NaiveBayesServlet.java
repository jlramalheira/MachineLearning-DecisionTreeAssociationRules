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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Limits;
import util.FormatFiles;
import weka.associations.Apriori;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author joao
 */
@WebServlet(name = "NaiveBayes", urlPatterns = {"/NaiveBayes"})
public class NaiveBayesServlet extends HttpServlet {

    RequestDispatcher rd;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch (action) {
            case "view": {
                int corrects = Integer.parseInt(request.getParameter("corrects"));
                int totalTest = Integer.parseInt(request.getParameter("totalTest"));
                int totalTrainig = Integer.parseInt(request.getParameter("totalTrainig"));
                int truePositive = Integer.parseInt(request.getParameter("truePositive"));
                int trueNegative = Integer.parseInt(request.getParameter("trueNegative"));
                int falsePositive = Integer.parseInt(request.getParameter("falsePositive"));
                int falseNegative = Integer.parseInt(request.getParameter("falseNegative"));
                String fileName = request.getParameter("fileName");

                request.setAttribute("corrects", corrects);
                request.setAttribute("totalTest", totalTest);
                request.setAttribute("totalTrainig", totalTrainig);
                request.setAttribute("fileName", fileName);
                request.setAttribute("truePositive", truePositive);
                request.setAttribute("trueNegative", trueNegative);
                request.setAttribute("falsePositive", falsePositive);
                request.setAttribute("falseNegative", falseNegative);

                rd = request.getRequestDispatcher("naiveBayesView.jsp");
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
                String pathNaivebayes = path + "/" + aux + "-naiveBayes.txt";

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
                    NaiveBayes naiveBayes = new NaiveBayes();

                    BufferedReader readerTraining = new BufferedReader(new FileReader(pathTrainingOutput));
                    Instances instancesTraining = new Instances(readerTraining);
                    instancesTraining.setClassIndex(instancesTraining.numAttributes() - 1);

                    naiveBayes.buildClassifier(instancesTraining);

                    BufferedReader readerTest = new BufferedReader(new FileReader(pathTestOutput));
                    //BufferedReader readerTest = new BufferedReader(new FileReader(pathTrainingOutput));
                    Instances instancesTest = new Instances(readerTest);
                    instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

                    Evaluation eval = new Evaluation(instancesTraining);
                    eval.evaluateModel(naiveBayes, instancesTest);

                    int corrects = 0;
                    int truePositive = 0;
                    int trueNegative = 0;
                    int falsePositive = 0;
                    int falseNegative = 0;

                    for (int i = 0; i < instancesTest.size(); i++) {
                        Instance instance = instancesTest.get(i);
                        double correctValue = instance.value(instance.attribute(instancesTest.numAttributes() - 1));
                        double classification = naiveBayes.classifyInstance(instance);

                        if (correctValue == classification) {
                            corrects++;
                        }
                        if (correctValue == 1 && classification == 1) {
                            truePositive++;
                        }
                        if (correctValue == 1 && classification == 0) {
                            falseNegative++;
                        }
                        if (correctValue == 0 && classification == 1) {
                            falsePositive++;
                        }
                        if (correctValue == 0 && classification == 0) {
                            trueNegative++;
                        }
                    }

                    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(pathNaivebayes, false)));

                    writer.println(naiveBayes.toString());

                    writer.println("");
                    writer.println("");
                    writer.println("Results");
                    writer.println(eval.toSummaryString());

                    writer.close();

                    response.sendRedirect("NaiveBayes?action=view&corrects=" + corrects
                            + "&totalTest=" + instancesTest.size()
                            + "&totalTrainig=" + instancesTraining.size()
                            + "&range=" + range
                            + "&truePositive=" + truePositive
                            + "&trueNegative=" + trueNegative
                            + "&falsePositive=" + falsePositive
                            + "&falseNegative=" + falseNegative
                            + "&fileName=" + aux + "-naiveBayes.txt");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    response.sendRedirect("Navigation?action=naiveBayes");
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
