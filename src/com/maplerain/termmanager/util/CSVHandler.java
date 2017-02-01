package com.maplerain.termmanager.util;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.*;

/**
 * Class to handle reading and writing of CSV files.
 * This will replace the use of commons-csv.
 *
 * Created by vivian on 2/1/17.
 */
public class CSVHandler {

    public CSVHandler() {
    }

    /**
     * Method to read a CSV file, and return them as a record of String[].
     *
     * @param file the CSV file to read
     * @return csvStrings which is an ArrayList of String[], each String[] being a CSV record
     */
    public List<String[]> readCSVFile(File file) {
        List<String[]> csvStrings = new ArrayList<String[]>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                if (!str.isEmpty()) { // only process strings with values, skip empty lines
                    String[] strArr = str.split(",");
                    csvStrings.add(strArr);
                }
            }
            in.close();
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not read file");
            alert.setContentText("CSVHandler: Could not read data from file:\n" + file.getPath());

            alert.showAndWait();
        }
        return csvStrings;
    }

    /**
     * Method to write records to a CSV file.
     * The records should be in a List of String[], with each String[] being a record.
     *
     * @param file the file to write to
     * @param csvStrings a List of String[], each String[] being a record
     */
    public void writeCSVFile(File file, List<String[]> csvStrings) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String[] str : csvStrings) {
                String joined = String.join(",", str);
                writer.write(joined + System.getProperty("line.separator"));
            }
            writer.flush();
            writer.close();
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not write file");
            alert.setContentText("CSVHandler: Could not write data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
}
