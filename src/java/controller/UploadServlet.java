/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DaoFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.File;
import util.Upload;

/**
 *
 * @author joao
 */
@WebServlet(name = "Upload", urlPatterns = {"/Upload"})
public class UploadServlet extends HttpServlet {

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

        Upload upload = new Upload(path);

        List list = upload.processRequest(request);

        Map<String, String> map = upload.getFormValues(list);

        if (map == null) {
            response.sendRedirect("Navigation?action=upload&uploaded=false");
        } else {

            File file = new File(map.get("name"), map.get("name") + ".txt");

            DaoFile daoFile = new DaoFile(path);

            daoFile.insert(file);

            response.sendRedirect("Navigation?action=upload&uploaded=true");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
