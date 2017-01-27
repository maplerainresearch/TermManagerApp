package com.maplerain.termmanager.view;

/**
 * Created by vivian on 1/26/17.
 *
 * Adapted from "JavaFX 8 Tutorial"
 * at http://code.makery.ch/library/javafx-8-tutorial/
 * by Marco Jakob
 */
import java.io.File;

import com.maplerain.termmanager.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;


/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and a search bar, followed by
 * space where other JavaFX elements can be placed.
 *
 * @author vivian
 */
public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Creates an empty glossary file.
     */
    @FXML
    private void handleNew() {
        mainApp.getTermData().clear();
        mainApp.setTermFilePath(null);
    }

    /**
     * Opens a FileChooser to let the user select a glossary file to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadTermDataFromFile(file);
        }
    }

    /**
     * Saves the file to the term file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File termFile = mainApp.getTermFilePath();
        if (termFile != null) {
            mainApp.saveTermDataToFile(termFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveTermDataToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Term Manager " + mainApp.getVersionNumber());
        alert.setContentText("Author: Vivian Ng\nWebsite: http://www.maplerain.com/");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
