package com.maplerain.termmanager.view;

import com.maplerain.termmanager.MainApp;
import com.maplerain.termmanager.model.Term;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Created by vivian on 1/26/17.
 *
 * Adapted from "JavaFX 8 Tutorial"
 * at http://code.makery.ch/library/javafx-8-tutorial/
 * by Marco Jakob
 */
public class TermOverviewController {
    @FXML
    private TextField sourceTermField;

    @FXML
    private TableView<Term> termTable;
    @FXML
    private TableColumn<Term, String> sourceTermColumn;

    @FXML
    private Label sourceTermLabel;
    @FXML
    private Label targetTermLabel;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TermOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the term table with the one column.
        sourceTermColumn.setCellValueFactory(cellData -> cellData.getValue().sourceTermProperty());

        // Clear term details.
        showTermDetails(null);

        // Listen for selection changes and show the term details when changed.
        termTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTermDetails(newValue));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        termTable.setItems(mainApp.getTermData());
    }

    /**
     * Fills all text fields to show details about the term.
     * If the specified term is null, all text fields are cleared.
     *
     * @param term the term or null
     */
    private void showTermDetails(Term term) {
        if (term != null) {
            // Fill the labels with info from the term object.
            sourceTermLabel.setText(term.getSourceTerm());
            targetTermLabel.setText(term.getTargetTerm());
        } else {
            // Term is null, remove all the text.
            sourceTermLabel.setText("");
            targetTermLabel.setText("");
        }
    }


    /**
     * Called when the user clicks the find button. Searches for the
     * term inside termTable.
     */
    @FXML
    private void handleFindTerm() {
        String findTerm = sourceTermField.getText();

        if (findTerm == "") {
            // Nothing to find.
            // Do nothing.
        } else {
            Term foundTerm = mainApp.findTermInTermMap(findTerm);
            termTable.scrollTo(foundTerm);
            termTable.getSelectionModel().clearSelection();
            termTable.getSelectionModel().select(foundTerm);
            showTermDetails(foundTerm);
        }
    }

    /**
     * Called when the user clicks the add button. Opens a dialog to edit
     * details for a new term.
     */
    @FXML
    private void handleAddTerm() {
        Term tempTerm = new Term();
        boolean okClicked = mainApp.showTermEditDialog(tempTerm);
        if (okClicked) {
            Term foundTerm = mainApp.findTermInTermMap(tempTerm.getSourceTerm());
            if(foundTerm != null) {
                foundTerm.setTargetTerm(foundTerm.getTargetTerm() + ", " + tempTerm.getTargetTerm());
                termTable.scrollTo(foundTerm);
                termTable.getSelectionModel().clearSelection();
                termTable.getSelectionModel().select(foundTerm);
            } else {
                mainApp.getTermData().add(tempTerm);
            }
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected term.
     */
    @FXML
    private void handleEditTerm() {
        Term selectedTerm = termTable.getSelectionModel().getSelectedItem();
        if (selectedTerm != null) {
            boolean okClicked = mainApp.showTermEditDialog(selectedTerm);
            if (okClicked) {
                showTermDetails(selectedTerm);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Term Selected");
            alert.setContentText("Please select a term in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteTerm() {
        int selectedIndex = termTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            termTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Term Selected");
            alert.setContentText("Please select a term in the table.");

            alert.showAndWait();
        }
    }
}
