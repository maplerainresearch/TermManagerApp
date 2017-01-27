package com.maplerain.termmanager.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.maplerain.termmanager.model.Term;

/**
 * Dialog to edit details of a term.
 *
 * @author Vivian Ng
 *
 * Adapted from "JavaFX 8 Tutorial"
 * at http://code.makery.ch/library/javafx-8-tutorial/
 * by Marco Jakob
 */
public class TermEditDialogController {

    @FXML
    private TextField sourceTermField;
    @FXML
    private TextField targetTermField;


    private Stage dialogStage;
    private Term term;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the term to be edited in the dialog.
     *
     * @param term
     */
    public void setTerm(Term term) {
        this.term = term;

        sourceTermField.setText(term.getSourceTerm());
        targetTermField.setText(term.getTargetTerm());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            term.setSourceTerm(sourceTermField.getText());
            term.setTargetTerm(targetTermField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (sourceTermField.getText() == null || sourceTermField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (targetTermField.getText() == null || targetTermField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}

