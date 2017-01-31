package com.maplerain.termmanager;

/**
 * Term Manager App
 * A terminology management application written by Vivian Ng.
 *
 * Adapted from "JavaFX 8 Tutorial"
 * at http://code.makery.ch/library/javafx-8-tutorial/
 * by Marco Jakob
 *
 *
 * Version 0.1
 * - Basic new, open, save, save as, exit, add term, edit term, delete term.
 * - Import from CSV, export to CSV.
 */

import com.maplerain.termmanager.model.Term;
import com.maplerain.termmanager.model.TermListWrapper;
import com.maplerain.termmanager.view.RootLayoutController;
import com.maplerain.termmanager.view.TermEditDialogController;
import com.maplerain.termmanager.view.TermOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private String versionNumber = "0.1";
    private String appName = "Term Manager";
    private String appTitle = appName + " " + versionNumber;

    /**
     * The data as an observable list of Terms.
     */
    private ObservableList<Term> termData = FXCollections.observableArrayList();

    /**
     * The data as a hashmap of sourceTerm string and targetTerm string.
     */
    private Map<String, String> termMap = new HashMap<String, String>();

    /**
     * Constructor
     */
    public MainApp() {
    }

    /**
     * Returns the data as an observable list of terms.
     * @return
     */
    public ObservableList<Term> getTermData() {
        return termData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(appTitle);

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

        initRootLayout();

        showTermOverview();

        // Add a listener to update termMap whenever termData is changed.
        termData.addListener(new ListChangeListener<Term>() {
            @Override
            public void onChanged(Change<? extends Term> c) {
                updateTermMap();
            }
        });
    }

    /**
     * Initializes the root layout.
     */
    /**
     * Initializes the root layout and tries to load the last opened
     * term file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened term file.
        File file = getTermFilePath();
        if (file != null) {
            loadTermDataFromFile(file);
        }
    }

    /**
     * Shows the term overview inside the root layout.
     */
    public void showTermOverview() {
        try {
            // Load term overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TermOverview.fxml"));
            SplitPane termOverview = (SplitPane) loader.load();

            // Set term overview into the center of root layout.
            rootLayout.setCenter(termOverview);

            // Give the controller access to the main app.
            TermOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified term. If the user
     * clicks OK, the changes are saved into the provided term object and true
     * is returned.
     *
     * @param term the term object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showTermEditDialog(Term term) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TermEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Term");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the term into the controller.
            TermEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTerm(term);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the term file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getTermFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setTermFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle(appTitle + " - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle(appTitle);
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Loads term data from the specified file. The current term data will
     * be replaced.
     *
     * @param file
     */
    public void loadTermDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(TermListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            TermListWrapper wrapper = (TermListWrapper) um.unmarshal(file);

            termData.clear();
            termData.addAll(wrapper.getTerms());
            updateTermMap();

            // Save the file path to the registry.
            setTermFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current term data to the specified file.
     *
     * @param file
     */
    public void saveTermDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(TermListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our term data.
            TermListWrapper wrapper = new TermListWrapper();
            wrapper.setTerms(termData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setTermFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Imports term data from the specified file, appending it to the current
     * term data.
     *
     * @param file
     */
    public void importTermDataFromFile(File file) {
        try {
            Reader in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            //Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRowAsHeader().parse(in);
            for (CSVRecord record : records) {
                Term tempTerm = new Term(record.get(0), record.get(1));
                Term foundTerm = findTermInTermMap(tempTerm.getSourceTerm());
                if(foundTerm != null) {
                    // TODO: check if targetTerm already exists
                    foundTerm.setTargetTerm(foundTerm.getTargetTerm() + "; " + tempTerm.getTargetTerm());
                } else {
                    termData.add(tempTerm);
                }
            }
            updateTermMap();
        } catch (IOException e) { // catches IO exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not import data");
            alert.setContentText("Could not import data from file:\n" + file.getPath());

            alert.showAndWait();
        } catch (ArrayIndexOutOfBoundsException ae) {
            // TODO: find out what is causing this exception
            System.out.println(ae.getMessage());
        }
    }

    /**
     * Exports the current term data to the specified file.
     *
     * @param file
     */
    public void exportTermDataToFile(File file) {
        try {
            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader();
            FileWriter fileWriter = new FileWriter(file);
            CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            for (Term term: termData) {
                csvFilePrinter.printRecord(term.getSourceTerm(), term.getTargetTerm());
            }

            fileWriter.flush();
            fileWriter.close();
            csvFilePrinter.close();
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not export data");
            alert.setContentText("Could not export data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Searches for a sourceTerm string inside the termMap dictionary.
     *
     * @param sourceTerm the sourceTerm string to search for.
     * @return term the Term if the string is found, null if not.
     */
    public Term findTermInTermMap(String sourceTerm) {
        String targetTerm = termMap.get(sourceTerm);
        if (targetTerm != null) {
            System.out.format("source: %s\ttarget: %s\n", sourceTerm, targetTerm);
            return findTermInTermData(sourceTerm, targetTerm);
        } else {
            return null;
        }
    }

    /**
     * Searches for a term (sourceTerm and targetTerm strings) inside
     * the termData list.
     *
     * @param sourceTerm the sourceTerm string to search for.
     * @param targetTerm the targetTerm string to search for.
     * @return term the Term if the string pair is found, null if not.
     */

    public Term findTermInTermData(String sourceTerm, String targetTerm) {
        for (Term term: termData) {
            System.out.println(term.getSourceTerm() + " " + term.getTargetTerm());
            if (sourceTerm.equals(term.getSourceTerm()) && targetTerm.equals(term.getTargetTerm())) {
                System.out.println("found\n");
                return term;
            }
        }
        return null; // String pair not found.
    }

    /**
     * Updates the contents of termMap with the terms stored in termData.
     */
    private void updateTermMap() {
        termMap.clear();
        for (Term term: termData) {
            termMap.put(term.getSourceTerm(), term.getTargetTerm());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Map<String,String> getTermMap() {
        return termMap;
    }
}
