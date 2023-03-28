package org.jacksonlaboratory.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import io.micronaut.serde.annotation.Serdeable;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;

@Serdeable
@Entity
@Table(name = "term")
public class OntologyTerm {
    @Id
    private TermId id;

    private String name;

    private String definition;

    private String comment;

    public OntologyTerm() {}

    public OntologyTerm(Term term) {
        this.id = term.id();
        this.name = term.getName();
        this.definition = term.getDefinition();
        this.comment = term.getComment();
    }


    public TermId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String definition() {
        return definition;
    }

    public String comment() {
        return comment;
    }
}
