/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DaoFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.File;
import util.Message;

/**
 *
 * @author joao
 */
@WebServlet(name = "Navigation", urlPatterns = {"/Navigation"})
public class NavigationServlet extends HttpServlet {

    RequestDispatcher rd;
    List<Message> messages;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        messages = new ArrayList<>();
        String dir = "/data/";
        String path = getServletContext().getRealPath(dir);

        String action = request.getParameter("action");
        switch (action) {
            case "upload":
                if (request.getParameter("uploaded") != null) {
                    if (Boolean.parseBoolean(request.getParameter("uploaded"))) {
                        messages.add(new Message("Base inserida com sucesso!", Message.TYPE_SUCCESS));
                    } else {
                        messages.add(new Message("Já existe uma base com esse nome!", Message.TYPE_ERROR));
                    }
                }

                request.setAttribute("messages", messages);

                rd = request.getRequestDispatcher("upload.jsp");
                rd.forward(request, response);
                break;
            case "index": {
                if (request.getParameter("base") != null) {
                    messages.add(new Message("Cadastre uma base clicando no link upload!", Message.TYPE_ALERT));
                }

                request.setAttribute("messages", messages);
                
                rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
                break;
            }
            case "decisionTree": {
                List<File> files = new DaoFile(path).list();

                request.setAttribute("files", files);

                rd = request.getRequestDispatcher("decisionTreeCreate.jsp");
                rd.forward(request, response);
                break;
            }
            
            case "naiveBayes": {
                List<File> files = new DaoFile(path).list();

                request.setAttribute("files", files);

                rd = request.getRequestDispatcher("naiveBayesCreate.jsp");
                rd.forward(request, response);
                break;
            }
            
            case "bothClassifications":{
                List<File> files = new DaoFile(path).list();

                request.setAttribute("files", files);

                rd = request.getRequestDispatcher("bothClassificationsCreate.jsp");
                rd.forward(request, response);
                break;
            }
            default:
                response.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
