/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.File;

/**
 *
 * @author joao
 */
public class DaoFile {

    private static final String fileName = "dataTopTypeFiles.txt";
    private String path;

    public DaoFile(String path) {
        this.path = path;
    }

    public void insert(File file) {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path + "/" + fileName, true)));

            writer.println(file.getName() + "&" + file.getPath());

            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DaoFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<File> list() {
        List<File> files = new ArrayList<>();
        FileReader fileReader;
        try {
            fileReader = new FileReader(path + "/" + fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                String[] values = line.split("&");
                files.add(new File(values[0], values[1]));

                line = bufferedReader.readLine();
            }
            
            return files;
        } catch (Exception ex) {
            Logger.getLogger(DaoFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
