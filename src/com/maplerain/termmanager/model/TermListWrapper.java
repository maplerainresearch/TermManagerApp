package com.maplerain.termmanager.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of terms. This is used for saving the
 * list of terms to XML.
 *
 * @author Vivian Ng
 *
 * Adapted from "JavaFX 8 Tutorial"
 * at http://code.makery.ch/library/javafx-8-tutorial/
 * by Marco Jakob
 */
@XmlRootElement(name = "terms")
public class TermListWrapper {

    private List<Term> terms;

    @XmlElement(name = "term")
    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }
}
