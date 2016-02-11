/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.FormatFiles;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author joao
 */
@WebServlet(name = "DecisionTree", urlPatterns = {"/DecisionTree"})
public class DecisionTreeServlet extends HttpServlet {

    RequestDispatcher rd;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
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

                String pathInput = path + "/" + request.getParameter("file");
                String pathTrainingOutput = path + "/" + fileName.substring(0, fileName.indexOf(".")) + "-training-arff.txt";
                String pathTestOutput = path + "/" + fileName.substring(0, fileName.indexOf(".")) + "-test-arff.txt";

                String name = request.getParameter("name");
                int range = Integer.parseInt(request.getParameter("range"));

                int size = Integer.parseInt(request.getParameter("counter"));
                String[] columns = new String[size];
                String[] types = new String[size];
                int[] positions = new int[size];
                for (int i = 0; i < size; i++) {
                    columns[i] = request.getParameter("column-" + (i + 1));
                    types[i] = request.getParameter("type-" + (i + 1));
                    positions[i] = Integer.parseInt(request.getParameter("position-" + (i + 1)));
                }

                FormatFiles.convertTxttoArffDecisionTree(pathInput, pathTrainingOutput, pathTestOutput, name, columns, types, positions, size, range);
                System.out.println("Range: "+range);
                try {
                    J48 j48 = new J48();
                    
                    BufferedReader readerTraining = new BufferedReader(new FileReader(pathTrainingOutput));
                    Instances instancesTraining = new Instances(readerTraining);
                    instancesTraining.setClassIndex(instancesTraining.numAttributes() - 1);
                    
                    j48.buildClassifier(instancesTraining);
                    
                    BufferedReader readerTest = new BufferedReader(new FileReader(pathTestOutput));
                    Instances instancesTest = new Instances(readerTest);
                    instancesTest.setClassIndex(instancesTest.numAttributes() - 1);
                    
                    int corrects = 0;
                    
                    for (int i = 0; i < instancesTest.size(); i++) {
                        Instance instance = instancesTest.get(i);
                        double correctValue = instance.value(instance.attribute(instancesTest.numAttributes()-1));
                        double classification = j48.classifyInstance(instance);
                        
                        if (correctValue == classification){
                            corrects++;
                        }
                    }
                    
                    System.out.println("Corrects: "+corrects);
                    System.out.println("Total: "+instancesTest.size());
                    System.out.println("Percent: "+((double)corrects/instancesTest.size())*100+"%");
                    
                    response.sendRedirect("Navigation?action=decisionTree");
                } catch (Exception e) {
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
