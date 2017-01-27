package com.maplerain.termmanager.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a terminology Term.
 *
 * Created by vivian on 1/26/17.
 */
public class Term {

    private final StringProperty sourceTerm;
    private final StringProperty targetTerm;

    /**
     * Default constructor.
     */
    public Term() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param sourceTerm
     * @param targetTerm
     */
    public Term(String sourceTerm, String targetTerm) {
        this.sourceTerm = new SimpleStringProperty(sourceTerm);
        this.targetTerm = new SimpleStringProperty(targetTerm);
    }

    public String getSourceTerm() {
        return sourceTerm.get();
    }

    public StringProperty sourceTermProperty() {
        return sourceTerm;
    }

    public void setSourceTerm(String sourceTerm) {
        this.sourceTerm.set(sourceTerm);
    }


    public String getTargetTerm() {
        return targetTerm.get();
    }

    public StringProperty targetTermProperty() {
        return targetTerm;
    }

    public void setTargetTerm(String targetTerm) {
        this.targetTerm.set(targetTerm);
    }
}
